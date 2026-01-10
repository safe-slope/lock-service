package io.github.safeslope.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.mqtt.config.MqttProperties;
import io.github.safeslope.mqtt.dto.*;
import io.github.safeslope.mqtt.service.CommandAuthorizationService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MqttLockAdapter implements MqttCallbackExtended {

    private static final Logger log = LoggerFactory.getLogger(MqttLockAdapter.class);

    private final IMqttAsyncClient client;
    private final MqttProperties props;
    private final MqttTopics mqttTopics;
    private final ObjectMapper objectMapper;
    private final CommandAuthorizationService commandAuthorizationService;

    public MqttLockAdapter(
            IMqttAsyncClient client,
            MqttProperties props,
            MqttTopics mqttTopics,
            ObjectMapper objectMapper,
            CommandAuthorizationService commandAuthorizationService) {
        this.client = client;
        this.props = props;
        this.mqttTopics = mqttTopics;
        this.objectMapper = objectMapper;
        this.commandAuthorizationService = commandAuthorizationService;
    }

    @PostConstruct
    public void start() {
        client.setCallback(this);

        try {
            if (client.isConnected()) {
                subscribeToRequest();
                subscribeToStatus();
                subscribeToRegistration();
            }
        } catch (MqttException e) {
            log.error("MQTT subscribe failed", e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) { }


    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("MQTT connected (reconnect={}) to {}", reconnect, serverURI);
        try {
            subscribeToRequest();
            subscribeToStatus();
            subscribeToRegistration();
        } catch (MqttException e) {
            log.error("MQTT subscribe failed", e);
        }
    }

    private void subscribeToRequest() throws MqttException {
        client.subscribe(mqttTopics.getRequestTopic(), props.getQos());
        log.info("Subscribed to request topic={} qos={}", mqttTopics.getRequestTopic(), props.getQos());
    }

    private void subscribeToStatus() throws MqttException {
        client.subscribe(mqttTopics.getStatusTopic(), props.getQos());
        log.info("Subscribed to status topic={} qos={}", mqttTopics.getStatusTopic(), props.getQos());
    }

    public void subscribeToRegistration() throws MqttException {
        client.subscribe(mqttTopics.getRegistrationTopic(), props.getQos());
        log.info("Subscribed to registration topic={} qos={}", mqttTopics.getRegistrationTopic(), props.getQos());
    }

    //lock->backend
    @Override
    public void messageArrived(String topic, MqttMessage message) throws IOException, MqttException {
        try {
            //first figure out what topic the message is from (request or status)
            MqttTopics.TopicProperties topicProperties = MqttTopics.tokenizeTopic(topic);

            switch (topicProperties.type){
                // TODO create handler classes for each topic type
                case "request":
                    //parse the Mqtt message into a request dto
                    RequestDto requestDto=objectMapper.readValue(message.getPayload(), RequestDto.class);

                    //check if all criteria match to perform the requested action
                    if (!commandAuthorizationService.authorize(requestDto.getLockId(), requestDto.getCommand(), requestDto.getSkiTicketId())) {
                        return;
                    }

                    //create a CommandDto
                    CommandDto commandDto = new CommandDto(
                            requestDto.getMsgId(),
                            requestDto.getLockerId(),
                            requestDto.getLockId(),
                            requestDto.getSkiTicketId(),
                            requestDto.getCommand(),
                            requestDto.getTimestamp()
                    );

                    //send the command to the locker on the correct topic
                    sendCommand(topicProperties.tenantId, topicProperties.resortId, topicProperties.lockerId, commandDto);
                    break;

                case "status":
                    StatusDto statusDto = objectMapper.readValue(message.getPayload(), StatusDto.class);

                    commandAuthorizationService.persist(statusDto);

                    break;

                case "registration":
                    RegistrationDto registrationDto = objectMapper.readValue(message.getPayload(), RegistrationDto.class);

                    Lock lock = commandAuthorizationService.register(registrationDto);

                    if (lock == null) {
                        log.error("Registration failed: lock is null for MAC address {}", registrationDto.getMacAddress());
                        return;
                    }

                    ResponseDto responseDto = new ResponseDto(
                            registrationDto.getMsgId(),
                            DtoConstants.Status.SUCCESS.toString(),
                            lock.getMacAddress(),
                            lock.getId(),
                            lock.getLocker().getId(),
                            registrationDto.getTimestamp()
                    );

                    sendResponse(topicProperties.tenantId, topicProperties.resortId, topicProperties.lockerId, responseDto);
                    break;

                default:
                    log.warn("Received message on unknown topic type: {}", topicProperties.type);
                    break;
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse MQTT message payload from topic {}", topic, e);
            throw new IOException("Failed to parse MQTT message", e);
        } catch (MqttException e) {
            log.error("Failed to send MQTT response on topic {}", topic, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error processing MQTT message from topic {}", topic, e);
            throw e;
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("MQTT connection lost: {}", cause.toString());
    }

    // backend -> lock
    public void sendCommand(Integer tenantId, Integer resortId, Integer lockerId, CommandDto dto) throws MqttException, JsonProcessingException {
        String topic = mqttTopics.lockerCommand(tenantId, resortId, lockerId);

        MqttMessage msg = new MqttMessage(objectMapper.writeValueAsBytes(dto));
        msg.setQos(props.getQos());

        client.publish(topic, msg);
        log.info("MQTT OUT topic={} payload={}", topic, objectMapper.writeValueAsBytes(dto));
    }

    public void sendResponse(Integer tenantId, Integer resortId, Integer lockerId, ResponseDto dto) throws MqttException, JsonProcessingException {
        String topic = mqttTopics.lockerResponse(tenantId, resortId, lockerId);

        MqttMessage msg = new MqttMessage(objectMapper.writeValueAsBytes(dto));
        msg.setQos(props.getQos());

        client.publish(topic, msg);
        log.info("MQTT OUT topic={} payload={}", topic, objectMapper.writeValueAsBytes(dto));
    }
}
