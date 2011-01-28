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
package net.sourceforge.pebble.event.blogentry;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Runs W3C Tidy over the excerpt and body of blog entries so that
 * they are valid XHTML.
 *
 * @author Simon Brown
 */
public class TidyListener extends BlogEntryListenerSupport {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(TidyListener.class);

  /**
   * Called when a blog entry has been added.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    tidy(event.getBlogEntry());
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
    List propertyChangeEvents = event.getPropertyChangeEvents();
    Iterator it = propertyChangeEvents.iterator();
    while (it.hasNext()) {
      PropertyChangeEvent pce = (PropertyChangeEvent)it.next();
      String property = pce.getPropertyName();
      if (property.equals(BlogEntry.EXCERPT_PROPERTY) ||
          property.equals(BlogEntry.BODY_PROPERTY)) {
        tidy(event.getBlogEntry());
      }
    }
  }

  private void tidy(BlogEntry blogEntry) {
    blogEntry.setExcerpt(tidy(blogEntry.getExcerpt()));
    blogEntry.setBody(tidy(blogEntry.getBody()));
  }

  private String tidy(String s) {
    if (s != null && s.length() > 0) {
      s = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
          "<html xmlns=\"http://www.w3.org/1999/xhtml\"><title></title><body>" + s + "</body></html>";
      ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Tidy tidy = new Tidy();
      tidy.setXHTML(true);
      tidy.setDocType("\"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
      tidy.setQuiet(true);
      tidy.setShowWarnings(false);
      tidy.setIndentContent(false);
      tidy.setSmartIndent(false);
      tidy.setIndentAttributes(false);
      tidy.setWraplen(0);
      Document doc = tidy.parseDOM(in, null);
      tidy.pprint(doc, out);

      String tidied = new String(out.toByteArray());
      return tidied.substring(tidied.indexOf("<body>")+"<body>".length(), tidied.indexOf("</body>")).trim();
    } else {
      return "";
    }
  }

}
