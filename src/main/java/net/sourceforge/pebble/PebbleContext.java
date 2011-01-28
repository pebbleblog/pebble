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
package net.sourceforge.pebble;

import net.sourceforge.pebble.security.SecurityRealm;
import net.sourceforge.pebble.util.RelativeDate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * A bean representing the Pebble context.
 *
 * @author    Simon Brown
 */
public class PebbleContext {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(PebbleContext.class);

  private Configuration configuration;
  private ApplicationContext applicationContext;

  private String buildVersion;
  private String buildDate;

  private static final String BUILD_VERSION_KEY = "build.version";
  private static final String BUILD_DATE_KEY = "build.date";

  /** the time that Pebble was started */
  private Date startTime;

  /** the directory where Pebble is deployed */
  private String webApplicationRoot;

  private static final PebbleContext instance = new PebbleContext();

  public static PebbleContext getInstance() {
    return instance;
  }

  private PebbleContext() {
    // and note when Pebble was started
    this.startTime = new Date();

    try {
      Properties buildProperties = new Properties();
      InputStream in = getClass().getClassLoader().getResourceAsStream(
              "META-INF/maven/org.sourceforge.pebble/pebble/pom.properties");
      if (in != null) {
        buildProperties.load(in);
        this.buildVersion = buildProperties.getProperty("version");
        // It's a little harder to do this in maven, seeing as it's not used anyway, commenting out.
        // this.buildDate = buildProperties.getProperty(BUILD_DATE_KEY);
        in.close();
      } else {
        log.warn("No maven pom.properties found, build version set for development");
        this.buildVersion = "dev";
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
  }

  /**
   * Gets the version of Pebble being used.
   *
   * @return    the version number as a String
   */
  public String getBuildVersion() {
    return this.buildVersion;
  }

  /**
   * Gets the date that Pebble was built.
   *
   * @return    the date as a String
   */
  public String getBuildDate() {
    return this.buildDate;
  }

  /**
   * Gets the amount of time that Pebble has been running for.
   *
   * @return  a number of milliseconds
   */
  public RelativeDate getUptime() {
    return new RelativeDate(new Date().getTime() - startTime.getTime());
  }

  public long getMemoryUsageInKB() {
    return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
  }

  public long getTotalMemoryInKB() {
    return Runtime.getRuntime().totalMemory() / 1024;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * Sets the directory where themes are located.
   *
   * @param webApplicationRoot    the absolute path to the webapp root on disk
   */
  public void setWebApplicationRoot(String webApplicationRoot) {
    this.webApplicationRoot = webApplicationRoot;
  }

  public String getWebApplicationRoot() {
    return webApplicationRoot;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public <T> T getComponent(String name, Class<T> type) {
    return applicationContext.getBean(name, type);
  }

  public <T> T getComponent(Class<T> type) {
    Map<String, T> map = applicationContext.getBeansOfType(type);
    if (map.isEmpty()) {
      return null;
    } else if (map.size() > 1) {
      throw new IllegalArgumentException("Multiple beans of type " + type + " found.");
    } else {
      return map.values().iterator().next();
    }
  }
}