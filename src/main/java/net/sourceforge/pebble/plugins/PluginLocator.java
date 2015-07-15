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

import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Locates plugins
 *
 * @author James Roper
 */
public class PluginLocator {
  private static final Log log = LogFactory.getLog(PluginLocator.class);
  private static final Map<String, PluginConfigType> PLUGIN_CONFIG_TYPES;
  static {
    PLUGIN_CONFIG_TYPES = new HashMap<String, PluginConfigType>();
    PLUGIN_CONFIG_TYPES.put("string", PlainTextPluginConfigType.INSTANCE);
    PLUGIN_CONFIG_TYPES.put("textarea", TextAreaPluginConfigType.INSTANCE);
    PLUGIN_CONFIG_TYPES.put("checkbox", CheckboxPluginConfigType.INSTANCE);
    PLUGIN_CONFIG_TYPES.put("password", PasswordPluginConfigType.INSTANCE);
  }

  private static AvailablePlugins availablePlugins;

  /**
   * Locates all the plugins installed in Pebble
   *
   * @return A map of plugin types to lists of plugin class names
   */
  public static synchronized AvailablePlugins locateAvailablePlugins() {
    if (availablePlugins == null) {
      Map<String, List<Plugin>> plugins = new HashMap<String, List<Plugin>>();
      try {
        Enumeration<URL> resources = PluginLocator.class.getClassLoader().getResources("pebble-plugins.xml");
        while (resources.hasMoreElements()) {
          installPlugins(plugins, resources.nextElement());
        }
      }
      catch (IOException ioe) {
        log.error("Error occured while scanning context classloader for plugins", ioe);
      }
      // Sort the plugins
      for (List<Plugin> pluginsList : plugins.values())
      {
        Collections.sort(pluginsList, new Comparator<Plugin>() {
          public int compare(Plugin o1, Plugin o2) {
            return o1.getWeight() - o2.getWeight();
          }
        });
      }
      availablePlugins = new AvailablePlugins(plugins);
    }
    return availablePlugins;
  }

  public static AvailablePlugins getAvailablePluginsSortedForBlog(Blog blog) {
    Map<String, List<Plugin>> plugins = locateAvailablePlugins().copyMap();
    // Sort the decorators
    plugins.put(AvailablePlugins.CONTENT_DECORATOR, sortPlugins(plugins.get(AvailablePlugins.CONTENT_DECORATOR),
            blog.getContentDecorators()));
    plugins.put(AvailablePlugins.PAGE_DECORATOR, sortPlugins(plugins.get(AvailablePlugins.PAGE_DECORATOR),
            blog.getPageDecoratorNames()));
    plugins.put(AvailablePlugins.OPEN_ID_COMMENT_AUTHOR_PROVIDER, sortPlugins(plugins.get(AvailablePlugins.OPEN_ID_COMMENT_AUTHOR_PROVIDER),
            blog.getOpenIdCommentAuthorProviderNames()));
    return new AvailablePlugins(plugins);
  }

  private static List<Plugin> sortPlugins(List<Plugin> plugins, final List<String> installedPlugins) {
    if (plugins == null)
    {
      return null;
    }
    Collections.sort(plugins, new Comparator<Plugin>() {
      public int compare(Plugin plugin1, Plugin plugin2) {
        // This comparator ensures installed plugins are at the top, in the order specified by the user,
        // and that not installed plugins are sorted by their weight.
        int index1 = installedPlugins.indexOf(plugin1.getPluginClass());
        int index2 = installedPlugins.indexOf(plugin2.getPluginClass());
        if (index1 >= 0 && index2 >= 0)
        {
          return index1 - index2;
        }
        if (index1 < 0 && index2 < 0)
        {
          return plugin1.getWeight() - plugin2.getWeight();
        }
        if (index1 >= 0)
        {
          return -1;
        }
        return 1;
      }
    });
    return plugins;
  }

  /**
   * Install the plugins from the given resource
   * @param plugins The plugins to install
   * @param resource The resource to install them from
   */
  static void installPlugins(Map<String, List<Plugin>> plugins, URL resource) {
    SAXBuilder saxBuilder = new SAXBuilder();
    try {
      Document document = saxBuilder.build(resource.openStream());
      Element root = document.getRootElement();
      for (Element element : (Iterable<Element>) root.getChildren())
      {
        String type = element.getName();
        String name = element.getAttributeValue("name");
        Element descriptionElement = element.getChild("description");
        String description = null;
        if (descriptionElement != null)
        {
          description = descriptionElement.getText();
        }
        String className = element.getAttributeValue("class");
        String weightStr = element.getAttributeValue("weight");
        int weight = 100;
        if (weightStr != null)
        {
          try {
            weight = Integer.parseInt(weightStr);
          } catch (NumberFormatException e) {
            log.error("Invalid weight for plugin " + name + ": " + weightStr);
          }
        }
        // Try and load the class - for validation
        try {
          Thread.currentThread().getContextClassLoader().loadClass(className);
          // Successful, add it
          log.debug("Installing plugin '" + name + "' of type " + type + " with class " + className);
          List<Plugin> list = plugins.get(type);
          if (list == null)
          {
            list = new ArrayList<Plugin>();
            plugins.put(type, list);
          }
          Collection<PluginConfig> pluginConfig = parsePluginConfig(element);
          Plugin plugin = new Plugin(name, description, className, weight, pluginConfig);
          if (!list.contains(plugin)) {
            list.add(plugin);
          }
        } catch (ClassNotFoundException e) {
          log.error("Plugin class " + className + " of type " + type + " from descriptor " + resource +
                  " could not be found.");
        }
      }
    } catch (JDOMException e) {
      log.error("Error parsing plugin descriptor at " + resource, e);
    } catch (IOException e) {
      log.error("Error reading plugin descriptor at " + resource, e);
    }
  }

  private static Collection<PluginConfig> parsePluginConfig(Element element)
  {
    Collection<PluginConfig> results = new ArrayList<PluginConfig>();
    for (Element configElement : (Iterable<Element>) element.getChildren("config"))
    {
      String key = configElement.getAttributeValue("key");
      String name = configElement.getAttributeValue("name");
      boolean required = Boolean.parseBoolean(configElement.getAttributeValue("required"));
      String type = configElement.getAttributeValue("type");
      PluginConfigType pluginConfigType;
      if (type == null)
      {
        pluginConfigType = PlainTextPluginConfigType.INSTANCE;
      }
      else
      {
        pluginConfigType = PLUGIN_CONFIG_TYPES.get(type);
      }
      // todo custom class instantiation
      Properties properties = new Properties();
      for (Element property : (Iterable<Element>) configElement.getChildren())
      {
        properties.setProperty(property.getName(), property.getText());
      }
      results.add(new PluginConfig(key, name, pluginConfigType, required, properties));
    }
    return results;
  }
}
