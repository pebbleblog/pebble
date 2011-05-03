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
public class PluginJspMaintainer {
  private static final Log log = LogFactory.getLog(PluginJspMaintainer.class);

  public static final String PLUGINS_JSP = "plugins-jsp";

  private final static Map<String, Boolean> jspStateCache = new ConcurrentHashMap<String, Boolean>();
  private final static Object lock = new Object();

  /**
   * Ensure that the given classpath JSP is in the WEB-INF/plugins-jsp directory and up to date, copying it across if
   * it's not
   *
   * @param classPathJsp   The classpath JSP
   * @param viewClass      The class the JSP comes from, used to find the jar to do modification time based checking
   * @param servletContext The servlet context
   */
  public static void checkJsp(String classPathJsp, Class viewClass, ServletContext servletContext) {
    if (!jspStateCache.containsKey(classPathJsp)) {
      synchronized (lock) {
        if (!jspStateCache.containsKey(classPathJsp)) {
          String realPath = servletContext.getRealPath("/");
          File pluginsJspPath = new File(realPath, "WEB-INF/" + PLUGINS_JSP);
          if (realPath != null) {
            File jsp = new File(pluginsJspPath, classPathJsp);
            if (jsp.exists()) {
              // Check if the jsp has been updated
              try {
                if (jsp.lastModified() < new File(viewClass.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).lastModified()) {
                  // Update the JSP
                  updateJsp(jsp, classPathJsp, viewClass);
                }
              } catch (URISyntaxException e) {
                log.error("Bad code source", e);
              }
            } else {
              updateJsp(jsp, classPathJsp, viewClass);
            }
          }
          jspStateCache.put(classPathJsp, true);
        }
      }
    }
  }

  private static void updateJsp(File dest, String classpathJsp, Class viewClass) {
    if (!dest.getParentFile().exists()) {
      dest.getParentFile().mkdirs();
    }
    InputStream is = null;
    OutputStream os = null;
    try {
      os = new FileOutputStream(dest);
      is = viewClass.getClassLoader().getResourceAsStream(classpathJsp);
      IOUtils.copy(is, os);
    } catch (IOException e) {
      log.error("Error copying JSP (" + classpathJsp + ") from classpath to WEB-INF/" + PLUGINS_JSP, e);
    } finally {
      IOUtils.closeQuietly(is);
      IOUtils.closeQuietly(os);
    }
  }

}
