package net.sourceforge.pebble.plugins;

import java.util.Properties;

/**
 * Represents a configuration item for a plugin
 * 
 * @author James Roper
 */
public class PluginConfig {
  private final String key;
  private final String name;
  private final PluginConfigType type;
  private final boolean requried;
  private final Properties properties;

  public PluginConfig(String key, String name, PluginConfigType type, boolean requried, Properties properties) {
    this.key = key;
    this.name = name;
    this.type = type;
    this.requried = requried;
    this.properties = properties;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public PluginConfigType getType() {
    return type;
  }

  public boolean isRequried() {
    return requried;
  }

  public Properties getProperties() {
    return properties;
  }
}
