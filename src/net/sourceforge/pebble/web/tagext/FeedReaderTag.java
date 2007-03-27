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
package net.sourceforge.pebble.web.tagext;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *
 * @author    Simon Brown
 */
public class FeedReaderTag extends SimpleTagSupport {

  private static final long ONE_MINUTE = 1000 * 60;
  private static Map<String,Feed> feedCache = new HashMap<String,Feed>();
  private static final Log log = LogFactory.getLog(FeedReaderTag.class);

  private String url;

  public void doTag() throws JspException, IOException {
    try {
      Feed feed = null;

      synchronized (feedCache) {
        feed = feedCache.get(url);
        if (feed == null || System.currentTimeMillis() > (feed.getTimestamp() + ONE_MINUTE)) {
          log.debug("Refreshing feed from " + url);
          SyndFeedInput input = new SyndFeedInput();
          SyndFeed sf = input.build(new XmlReader(new URL(url)));

          List entries = new LinkedList();
          Iterator it = sf.getEntries().iterator();
          while (it.hasNext()) {
            SyndEntry se = (SyndEntry)it.next();
            if (log.isDebugEnabled()) {
              log.debug(se);
            }

            FeedEntry fe = new FeedEntry(
                se.getLink(),
                se.getTitle(),
                se.getDescription() != null ? se.getDescription().getValue() : ""
            );
            entries.add(fe);
          }

          feed = new Feed(url, entries);
          feedCache.put(url, feed);
        } else {
          log.debug("Using cached feed from " + url);
        }
      }

      getJspContext().setAttribute("feedEntries", feed.getEntries());
    } catch (FeedException fe) {
      throw new JspTagException(fe);
    }
  }

  public void setUrl(String url) {
    this.url = url;
  }

  private class Feed {

    private String url;
    private List entries;
    private long timestamp;

    public Feed(String url, List entries) {
      this.url = url;
      this.entries = entries;
      this.timestamp = System.currentTimeMillis();
    }

    public String getUrl() {
      return url;
    }

    public List getEntries() {
      return entries;
    }

    public long getTimestamp() {
      return timestamp;
    }
  }

}