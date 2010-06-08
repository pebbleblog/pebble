package net.sourceforge.pebble.plugins;

import junit.framework.TestCase;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author James Roper
 */
public class TestPluginLocator extends TestCase {
  private Map<String, List<Plugin>> plugins;
  private URL testPlugins = this.getClass().getResource("/test-plugins.xml");

  @Override
  protected void setUp() throws Exception {
    plugins = new HashMap<String, List<Plugin>>();
  }

  public void testUnweighted() {
    PluginLocator.installPlugins(plugins, testPlugins);
    List<Plugin> ps = plugins.get("plugin-type");
    assertEquals(2, ps.size());
    Plugin plugin = ps.get(0);
    assertEquals("Unweighted", plugin.getName());
    assertEquals(100, plugin.getWeight());
    assertEquals("java.util.ArrayList", plugin.getPluginClass());
    assertNull(plugin.getDescription());
    assertFalse(plugin.isConfigurable());
    assertEquals(0, plugin.getPluginConfig().size());
  }

  public void testWeighted() {
    PluginLocator.installPlugins(plugins, testPlugins);
    List<Plugin> ps = plugins.get("plugin-type");
    assertEquals(2, ps.size());
    Plugin plugin = ps.get(1);
    assertEquals("Weighted", plugin.getName());
    assertEquals(20, plugin.getWeight());
  }

  public void testDescription() {
    PluginLocator.installPlugins(plugins, testPlugins);
    assertEquals("My description", plugins.get("description-plugin-type").get(0).getDescription());
  }

  private PluginConfig getConfig(int index) {
    PluginLocator.installPlugins(plugins, testPlugins);
    Plugin plugin = plugins.get("configured-plugin-type").get(0);
    assertTrue(plugin.isConfigurable());
    return ((List<PluginConfig>) plugin.getPluginConfig()).get(index);
  }

  public void testNoType() {
    PluginConfig config = getConfig(0);
    assertEquals("No type", config.getName());
    assertEquals("notype", config.getKey());
    assertSame(PlainTextPluginConfigType.INSTANCE, config.getType());
    assertTrue(config.getProperties().isEmpty());
    assertFalse(config.isRequried());
  }

  public void testStringType() {
    assertSame(PlainTextPluginConfigType.INSTANCE, getConfig(1).getType());
  }

  public void testPasswordType() {
    assertSame(PasswordPluginConfigType.INSTANCE, getConfig(2).getType());
  }

  public void testTextAreaType() {
    assertSame(TextAreaPluginConfigType.INSTANCE, getConfig(3).getType());
  }

  public void testRequired() {
    assertTrue(getConfig(4).isRequried());
  }

  public void testProperty() {
    assertEquals("value", getConfig(5).getProperties().get("property"));    
  }

}
