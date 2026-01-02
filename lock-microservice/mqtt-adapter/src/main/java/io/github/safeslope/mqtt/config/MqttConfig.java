package io.github.safeslope.mqtt.config;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {
    private static final Logger log = LoggerFactory.getLogger(MqttConfig.class);


    @Bean(destroyMethod = "close")
    public IMqttAsyncClient mqttClient(MqttProperties props) throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(props.isCleanSession());

        if (props.getUsername() != null && !props.getUsername().isBlank()) {
            options.setUserName(props.getUsername());
        }
        if (props.getPassword() != null && !props.getPassword().isBlank()) {
            options.setPassword(props.getPassword().toCharArray());
        }

        IMqttAsyncClient client =
                new MqttAsyncClient(props.getBroker(), props.getClientId());

        client.connect(options, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                log.info("[MQTT] Connected to {}", props.getBroker());
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                log.error("[MQTT] Connection failed", exception);
            }
        });

        return client;
    }
}
