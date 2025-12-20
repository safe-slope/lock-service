package io.github.safeslope.service.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {

    @Bean
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
                System.out.println("[MQTT] Connected");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.err.println("[MQTT] Connection failed: " + exception.getMessage());
            }
        });

        return client;
    }
}
