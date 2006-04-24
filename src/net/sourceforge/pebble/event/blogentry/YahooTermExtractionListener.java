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
package net.sourceforge.pebble.event.blogentry;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * Uses the Yahoo content analysis web service to extract terms that can be
 * used to tag a blog entry.
 *
 * @author Simon Brown
 */
public class YahooTermExtractionListener extends BlogEntryListenerSupport {

  /** the Yahoo! term extraction URL */
  private static final String URL = "http://api.search.yahoo.com/ContentAnalysisService/V1/termExtraction";

  /** the log used for this class */
  private static final Log log = LogFactory.getLog(YahooTermExtractionListener.class);

  /**
   * Called when a blog entry has been added.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    tag(event.getBlogEntry());
  }

  /**
   * Tags the specified blog entry.
   *
   * @param blogEntry   the BlogEntry instance
   */
  private void tag(BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();

    try {
      HttpClient httpClient = new HttpClient();
      PostMethod postMethod = new PostMethod(URL);
      postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + blog.getCharacterEncoding());
      NameValuePair[] data = {
        new NameValuePair("appid", "PebbleWeblog"),
        new NameValuePair("context", blogEntry.getBody()),
      };
      postMethod.addParameters(data);
      int responseCode = httpClient.executeMethod(postMethod);
      if (responseCode != 200) {
        return;
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);
      factory.setIgnoringElementContentWhitespace(true);
      factory.setIgnoringComments(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new ErrorHandler() {
        public void warning(SAXParseException e) throws SAXException {
          log.warn(e);
          throw e;
        }

        public void error(SAXParseException e) throws SAXException {
          log.error(e);
          throw e;
        }

        public void fatalError(SAXParseException e) throws SAXException {
          log.fatal(e);
          throw e;
        }
      });

      StringBuffer tags = new StringBuffer(blogEntry.getTags());
      InputStream in = postMethod.getResponseBodyAsStream();
      Document doc = builder.parse(in);
      in.close();
      NodeList results = doc.getElementsByTagName("Result");
      if (results != null) {
        for (int i = 0; i < results.getLength(); i++) {
          Node node = results.item(i);
          String tag = getTextValue(node);
          if (tags.length() > 0) {
            tags.append(", ");
          }
          tags.append(tag);
        }
      }

      blogEntry.setTags(tags.toString());
      blogEntry.store();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Helper method to extract the text value from a given node.
   *
   * @param node    a Node
   * @return    the text value, or an empty string if no text value available
   */
  private String getTextValue(Node node) {
    if (node.hasChildNodes()) {
      return node.getFirstChild().getNodeValue();
    } else {
      return "";
    }
  }

}
