package io.github.safeslope.mqtt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
  private String broker;
  private String clientId;
  private String username;
  private String password;
  private boolean cleanSession = true;
  private int qos = 1;
}
