package io.github.safeslope.mqtt;

import io.github.safeslope.mqtt.config.MqttProperties;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MqttLockAdapter implements MqttCallbackExtended {

    private static final Logger log = LoggerFactory.getLogger(MqttLockAdapter.class);

    private final IMqttAsyncClient client;
    private final MqttProperties props;
    private final MqttEventHandler eventHandler;

    public MqttLockAdapter(IMqttAsyncClient client, MqttProperties props, MqttEventHandler eventHandler) {
        this.client = client;
        this.props = props;
        this.eventHandler = eventHandler;
    }

    @PostConstruct
    public void start() {
        client.setCallback(this);

        try {
            if (client.isConnected()) {
                subscribeToEvents();
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
            subscribeToEvents();
        } catch (MqttException e) {
            log.error("MQTT subscribe failed", e);
        }
    }

    private void subscribeToEvents() throws MqttException {
        client.subscribe(props.getEventsTopic(), props.getQos());
        log.info("Subscribed to events topic={} qos={}", props.getEventsTopic(), props.getQos());
    }

    //lock->backend
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        log.info("MQTT in topic={} payload={}", topic, payload);

        eventHandler.handleLockEventMessage(topic, payload);
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("MQTT connection lost: {}", cause.toString());
    }

    // backend -> lock
    public void sendCommand(Integer tenantId, String lockKey, String jsonPayload) throws MqttException {
        String topic = String.format(props.getCommandsTopicFormat(), tenantId, lockKey);

        MqttMessage msg = new MqttMessage(jsonPayload.getBytes(StandardCharsets.UTF_8));
        msg.setQos(props.getQos());

        client.publish(topic, msg);
        log.info("MQTT OUT topic={} payload={}", topic, jsonPayload);
    }
}
