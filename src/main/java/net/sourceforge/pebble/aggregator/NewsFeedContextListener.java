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
package net.sourceforge.pebble.aggregator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages a timer that regularly updates all news feeds that have been
 * subscribed to.
 *
 * @author    Simon Brown
 */
public class NewsFeedContextListener implements ServletContextListener {

  private static final long ONE_MINUTE = 1000 * 60;
  private static final Log log = LogFactory.getLog(NewsFeedContextListener.class);

  private Timer timer;

  public void contextInitialized(ServletContextEvent servletContextEvent) {
    log.info("Starting newsfeed updater");
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        try {
          NewsFeedCache.getInstance().refreshFeeds();
        } catch (Exception e) {
          log.error("Error while refreshing feeds " + e.getMessage());
          e.printStackTrace();
        }
      }
    }, 0, ONE_MINUTE * 15);
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    log.info("Stopping newsfeed updater");
    timer.cancel();
  }

}
