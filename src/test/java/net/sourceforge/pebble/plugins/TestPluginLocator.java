/*
 * Copyright (c) 2003-2011, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

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
