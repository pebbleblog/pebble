package net.sourceforge.pebble.plugins;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * @author James Roper
 */
public class PlainTextPluginConfigType implements PluginConfigType {

  public static final PlainTextPluginConfigType INSTANCE = new PlainTextPluginConfigType();

  public void render(JspWriter writer, PluginConfig pluginConfig, String value) throws IOException {
    writer.print("<input type=\"text\" name=\"");
    writer.print(PLUGIN_PROPERTY_NAME_PREFIX);
    writer.print(pluginConfig.getKey());
    writer.print("\" value=\"");
    if (value != null) {
      writer.print(StringEscapeUtils.escapeHtml(value));
    }
    writer.print("\"/>");
  }

  public String validate(PluginConfig pluginConfig, String value) {
    String regex = pluginConfig.getProperties().getProperty("regex");
    if (regex != null && value != null)
    {
      if (!value.matches(regex))
      {
        return "Property " + pluginConfig.getName() + " must match regular expression " + regex;
      }
    }
    // Success
    return null;
  }
}
