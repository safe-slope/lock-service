package io.github.safeslope.service.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    private String broker;
    private String clientId;
    private String username;
    private String password;
    private boolean cleanSession;
    private int qos;
    private String eventsTopic;
    private String commandsTopicFormat;
}
