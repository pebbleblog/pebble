package net.sourceforge.pebble.plugins;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * @author James Roper
 */
public class CheckboxPluginConfigType implements PluginConfigType {

  public static final CheckboxPluginConfigType INSTANCE = new CheckboxPluginConfigType();

  public void render(JspWriter writer, PluginConfig pluginConfig, String value) throws IOException {
    writer.print("<input type=\"checkbox\" name=\"");
    writer.print(PLUGIN_PROPERTY_NAME_PREFIX);    
    writer.print(pluginConfig.getKey());
    writer.print("\" value=\"true\"/>");
  }

  public String validate(PluginConfig pluginConfig, String value) {
    // Success
    return null;
  }
}