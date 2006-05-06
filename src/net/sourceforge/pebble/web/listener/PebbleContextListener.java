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
package net.sourceforge.pebble.web.listener;

import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.domain.BlogManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Allows the blog to be loaded when this web application is started up.
 *
 * @author    Simon Brown
 */
public class PebbleContextListener implements ServletContextListener {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(PebbleContextListener.class);

  /**
   * Called when the web application is started.
   *
   * @param event   a ServletContextEvent instance
   */
  public void contextInitialized(ServletContextEvent event) {
    long startTime = System.currentTimeMillis();
    log.info("Starting Pebble");

    ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
    PebbleContext pebbleContext = (PebbleContext)applicationContext.getBean("pebbleContext");

    DAOFactory.setConfiguredFactory(pebbleContext.getDaoFactory());
    BlogManager.getInstance().setPebbleContext(pebbleContext);
    BlogManager.getInstance().setWebappRoot(event.getServletContext().getRealPath("/"));
    BlogManager.getInstance().setFileUploadSize(pebbleContext.getFileUploadSize());
    BlogManager.getInstance().setFileUploadQuota(pebbleContext.getFileUploadQuota());
    BlogManager.getInstance().startBlogs();

    long endTime = System.currentTimeMillis();
    log.info("Pebble started in " + (endTime-startTime) + "ms");
  }

  /**
   * Called when the web application is shutdown.
   *
   * @param event   a ServletContextEvent instance
   */
  public void contextDestroyed(ServletContextEvent event) {
    log.info("Stopping Pebble");
    BlogManager.getInstance().stopBlogs();

    log.info("Pebble stopped");
  }

}
