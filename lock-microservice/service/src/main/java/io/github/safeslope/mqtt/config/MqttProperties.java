package io.github.safeslope.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
  private String broker;
  private String clientId;
  private String username;
  private String password;
  private boolean cleanSession = true;
  private int qos = 1;
  private String eventsTopic;
  private String commandsTopicFormat;

  public String getBroker() { return broker; }
  public void setBroker(String broker) { this.broker = broker; }
  public String getClientId() { return clientId; }
  public void setClientId(String clientId) { this.clientId = clientId; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  public boolean isCleanSession() { return cleanSession; }
  public void setCleanSession(boolean cleanSession) { this.cleanSession = cleanSession; }
  public int getQos() { return qos; }
  public void setQos(int qos) { this.qos = qos; }
  public String getEventsTopic() { return eventsTopic; }
  public void setEventsTopic(String eventsTopic) { this.eventsTopic = eventsTopic; }
  public String getCommandsTopicFormat() { return commandsTopicFormat; }
  public void setCommandsTopicFormat(String commandsTopicFormat) { this.commandsTopicFormat = commandsTopicFormat; }
}
