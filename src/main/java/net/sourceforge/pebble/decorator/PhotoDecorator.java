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
package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.util.StringUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Takes a simple description of photos and generates boilerplate markup.
 *
 * @author Simon Brown
 */
public class PhotoDecorator extends ContentDecoratorSupport {

  private static final Log log = LogFactory.getLog(PhotoDecorator.class);

  private static final String PHOTOS_START_TAG = "<photos>";
  private static final String PHOTOS_END_TAG = "</photos>";

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    blogEntry.setBody(markup(blogEntry.getBody()));
    blogEntry.setExcerpt(markup(blogEntry.getExcerpt()));
  }

  /**
   * Decorates the specified static page.
   *
   * @param context    the context in which the decoration is running
   * @param staticPage the static page to be decorated
   */
  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    staticPage.setBody(markup(staticPage.getBody()));
  }

  private String markup(String content) {
    // is there work to do?
    if (content == null || content.length() == 0) {
      return "";
    }

    // this pattern says "take the shortest match you can find where there are
    // one or more characters between escape tags"
    //  - the match is case insensitive and DOTALL means that newlines are
    //  - considered as a character match
    Pattern p = Pattern.compile(PHOTOS_START_TAG + ".+?" + PHOTOS_END_TAG,
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher m = p.matcher(content);

    // while there are blocks to be escaped
    while (m.find()) {
      int start = m.start();
      int end = m.end();

      // grab the text, strip off the "photos" tags and transform it
      String textToMarkup = content.substring(start, end);
      textToMarkup = textToMarkup.substring(PHOTOS_START_TAG.length(), textToMarkup.length() - PHOTOS_END_TAG.length());

      StringBuffer buf = new StringBuffer();
      buf.append("<div class=\"photos\">\n");

      try {
        BufferedReader reader = new BufferedReader(new StringReader(textToMarkup));
        String line = reader.readLine();
        buf.append("<div>\n");
        boolean foundPhotos = false;
        while (line != null) {
          if (line.trim().equals("")) {
            if (foundPhotos) {
              buf.append("</div>\n");
              buf.append("<div>\n");
            }
          } else {
            String[] tokens = line.split("\\|");
            buf.append("<img src=\"");
            buf.append(tokens[0]);
            buf.append("\" class=\"photo\" alt=\"");
            if (tokens.length == 2) {
              buf.append(tokens[1]);
            }
            buf.append("\" />\n");
            foundPhotos = true;
          }

          line = reader.readLine();
        }
        buf.append("</div>\n");
      } catch (IOException ioe) {
        log.warn(ioe);
      }

      buf.append("</div>");

      // now add it back into the original text
      content = content.substring(0, start) + buf.toString() + content.substring(end, content.length());
      m = p.matcher(content);
    }

    return content;
  }

}
