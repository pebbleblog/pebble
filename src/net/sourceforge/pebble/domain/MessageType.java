package net.sourceforge.pebble.domain;

/**
 * Represents a message type.
 *
 * @author    Simon Brown
 */
public enum MessageType {

  INFO("info", "Info"),
  WARN("warning", "Warning"),
  ERROR("error", "Error");

  private String id;
  private String name;

  MessageType(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return this.name;
  }

}