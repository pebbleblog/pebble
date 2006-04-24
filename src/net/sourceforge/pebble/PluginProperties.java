/*
 * Copyright (c) 2003-2006, Simon Brown
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
package net.sourceforge.pebble;

import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Contains properties that can be used by Pebble plugins.
 *
 * @author    Simon Brown
 */
public class PluginProperties {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(PluginProperties.class);

  /** the Properties object that backs this instance */
  private Properties properties;

  /** the owning blog */
  private Blog blog;

  /**
   * Creates a new instance with the specified owning blog.
   *
   * @param blog    the owning Blog instance
   */
  public PluginProperties(Blog blog) {
    this.blog = blog;
    loadProperties();
  }

  /**
   * Helper method to load the properties from disk.
   */
  private void loadProperties() {
    try {
      this.properties = new Properties();
      FileInputStream fin = new FileInputStream(blog.getPluginPropertiesFile());
      properties.load(fin);
      fin.close();
    } catch (FileNotFoundException fnfe) {
      // do nothing - there are no plugin properties to load
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Gets properties file as a String in the format it is saved on disk.
   *
   * @return  a String
   */
  public String getPropertiesAsString() {
    StringBuffer content = new StringBuffer();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(blog.getPluginPropertiesFile()));
      String line = reader.readLine();
      while (line != null) {
        content.append(line);

        line = reader.readLine();
        if (line != null) {
          content.append(System.getProperty("line.separator"));
        }
      }
      reader.close();
    } catch (FileNotFoundException fnfe) {
    } catch (IOException ioe) {
      log.error(ioe.getMessage());
    }

    return content.toString();
  }

  /**
   * Sets the properties and saves it to disk.
   *
   * @param content   the properties file format as a String
   */
  public void setPropertiesAsString(String content) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(blog.getPluginPropertiesFile()));
      writer.write(content);
      writer.flush();
      writer.close();

      loadProperties();
    } catch (FileNotFoundException fnfe) {
    } catch (IOException ioe) {
      log.error(ioe.getMessage());
    }
  }

  /**
   * Determines whether a property with the specified name exists.
   *
   * @param name    the name of th eproperty to test for
   * @return  true if the property exists, false otherwise
   */
  public boolean hasProperty(String name) {
    return properties.containsKey(name);
  }

  /**
   * Gets the named property.
   *
   * @param name    the name of the property
   * @return  the value of the property, or null if it doesn't exist
   */
  public String getProperty(String name) {
    return properties.getProperty(name);
  }

  public Properties getProperties() {
    return properties;
  }

  /**
   * Sets the named property.
   *
   * @param name    the name of the property
   * @param value   the value of the property
   */
  public void setProperty(String name, String value) {
    properties.setProperty(name, value);
  }

  /**
   * Helper method to store the properties to disk.
   */
  public void store() {
    try {
      FileOutputStream fout = new FileOutputStream(blog.getPluginPropertiesFile());
      if (fout != null) {
        properties.store(fout, "Plugin properties");
        fout.flush();
        fout.close();
      }
    } catch (FileNotFoundException fnfe) {
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

}