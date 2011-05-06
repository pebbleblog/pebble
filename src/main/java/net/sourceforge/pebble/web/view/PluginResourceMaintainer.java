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
package net.sourceforge.pebble.web.view;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maintains JSPs from plugins in the WAR directory
 *
 * @author James Roper
 */
public class PluginResourceMaintainer {
  private static final Log log = LogFactory.getLog(PluginResourceMaintainer.class);

  public static final String PLUGINS_JSP = "plugins-jsp";
  public static final String WEB_INF_PLUGINS_JSP = "WEB-INF/plugins-jsp";

  private final static Map<String, Boolean> resourceStateCache = new ConcurrentHashMap<String, Boolean>();
  private final static Object lock = new Object();

  /**
   * Ensure that the given resource is deployed to somewhere where tomcat can serve it and is up to date, copying it
   * across if it's not
   *
   * @param classPathResource The classpath resource
   * @param deployLocation    The directory to deploy the resource to
   * @param sourceClass       A class that comes from the same JAR as the resource, for modification time checking
   * @param servletContext    The servlet context
   */
  public static void checkResource(String classPathResource, String deployLocation, Class sourceClass,
                              ServletContext servletContext) {
    String cacheKey = classPathResource + "|" + deployLocation;
    if (!resourceStateCache.containsKey(cacheKey)) {
      synchronized (lock) {
        if (!resourceStateCache.containsKey(cacheKey)) {
          String realPath = servletContext.getRealPath("/");
          if (realPath != null) {
            File deployTo;
            if (deployLocation != null) {
              deployTo = new File(realPath, deployLocation);
            } else {
              deployTo = new File(realPath);
            }
            String resourceName = classPathResource;
            if (classPathResource.contains("/")) {
              resourceName = classPathResource.substring(classPathResource.lastIndexOf("/") + 1);
            }
            File destResource = new File(deployTo, resourceName);
            if (destResource.exists()) {
              // Check if the resource has been updated
              try {
                if (destResource.lastModified() < new File(sourceClass.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).lastModified()) {
                  // Update the resource
                  updateResource(destResource, classPathResource, sourceClass);
                }
              } catch (URISyntaxException e) {
                log.error("Bad code source", e);
              }
            } else {
              updateResource(destResource, classPathResource, sourceClass);
            }
          }
          resourceStateCache.put(cacheKey, true);
        }
      }
    }
  }

  /**
   * Ensure that the given classpath JSP is in the WEB-INF/plugins-jsp directory and up to date, copying it across if
   * it's not
   *
   * @param classPathJsp   The classpath JSP
   * @param viewClass      The class the JSP comes from, used to find the jar to do modification time based checking
   * @param servletContext The servlet context
   */
  public static void checkJsp(String classPathJsp, Class viewClass, ServletContext servletContext) {
    checkResource(classPathJsp, WEB_INF_PLUGINS_JSP, viewClass, servletContext);
  }

  private static void updateResource(File dest, String classpathResource, Class viewClass) {
    if (!dest.getParentFile().exists()) {
      //noinspection ResultOfMethodCallIgnored
      dest.getParentFile().mkdirs();
    }
    InputStream is = null;
    OutputStream os = null;
    try {
      os = new FileOutputStream(dest);
      is = viewClass.getClassLoader().getResourceAsStream(classpathResource);
      IOUtils.copy(is, os);
    } catch (IOException e) {
      log.error("Error copying resource (" + classpathResource + ") from classpath to " + dest.getAbsolutePath(), e);
    } finally {
      IOUtils.closeQuietly(is);
      IOUtils.closeQuietly(os);
    }
  }

}
