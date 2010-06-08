package net.sourceforge.pebble.plugins;

import java.util.Collection;

/**
 * A plugin
 *
 * @author James Roper
 */
public class Plugin {
  private final String name;
  private final String description;
  private final String pluginClass;
  private final int weight;
  private final Collection<PluginConfig> pluginConfig;

  public Plugin(String name, String description, String pluginClass, int weight, Collection<PluginConfig> pluginConfig) {
    this.name = name;
    this.description = description;
    this.pluginClass = pluginClass;
    this.weight = weight;
    this.pluginConfig = pluginConfig;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getPluginClass() {
    return pluginClass;
  }

  public int getWeight() {
    return weight;
  }

  public Collection<PluginConfig> getPluginConfig() {
    return pluginConfig;
  }

  public boolean isConfigurable()
  {
    return pluginConfig != null && !pluginConfig.isEmpty();
  }
}
