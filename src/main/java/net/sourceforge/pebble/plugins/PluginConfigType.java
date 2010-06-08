package net.sourceforge.pebble.plugins;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Represents a plugin configuration type
 *
 * @author James Roper
 */
public interface PluginConfigType {
  public static final String PLUGIN_PROPERTY_NAME_PREFIX = "pluginProperty_";

  /**
   * Render the configuration item to the given writer.  This should result in some form control element.
   * The name of the form control should be "pluginProperty_" followed by the pluginConfig.getKey().
   *
   * @param writer The writer to render to
   * @param pluginConfig The configuration to render
   * @param value The value to render
   * @throws IOException If an error occured
   */
  void render(JspWriter writer, PluginConfig pluginConfig, String value) throws IOException;

  /**
   * Validate the value entered the user
   * 
   * @param pluginConfig The config to validate against
   * @param value The value to validate
   * @return null if validation passed, or a error message if not
   */
  String validate(PluginConfig pluginConfig, String value);

}
