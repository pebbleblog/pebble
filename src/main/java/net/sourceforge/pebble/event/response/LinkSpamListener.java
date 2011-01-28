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
package net.sourceforge.pebble.event.response;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks comment and TrackBack content for a large number of links and sets
 * the state of such responses to pending.
 *
 * @author Simon Brown
 */
public class LinkSpamListener extends BlogEntryResponseListenerSupport {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(LinkSpamListener.class);

  /** the pattern used to find HTML links */
  private static final Pattern HTML_LINK_PATTERN = Pattern.compile("<a.*?href=.*?>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

  /** the default threshold for the number of links allowed */
  public static final int DEFAULT_THRESHOLD = 3;

  /** the name of the threshold property for comments */
  public static final String COMMENT_THRESHOLD_KEY = "LinkSpamListener.commentThreshold";

  /** the name of the threshold property for TrackBacks */
  public static final String TRACKBACK_THRESHOLD_KEY = "LinkSpamListener.trackbackThreshold";

  /**
   * Called when a comment or TrackBack has been added.
   *
   * @param response a Response
   */
  protected void blogEntryResponseAdded(Response response) {
    String content = response.getContent();
    if (content != null) {
      Matcher m = HTML_LINK_PATTERN.matcher(content);
      int count = 0;
      while (m.find()) {
        count++;
      }

      PluginProperties props = response.getBlogEntry().getBlog().getPluginProperties();
      String propertyName = "";
      if (response instanceof Comment) {
        propertyName = COMMENT_THRESHOLD_KEY;
      } else {
        propertyName = TRACKBACK_THRESHOLD_KEY;
      }
      int threshold = DEFAULT_THRESHOLD;
      if (props.hasProperty(propertyName)) {
        try {
          threshold = Integer.parseInt(props.getProperty(propertyName));
        } catch (NumberFormatException nfe) {
          log.error(nfe.getMessage());
          // do nothing, the value has already been defaulted
        }
      }

      if (count > threshold) {
        log.info(response.getTitle() + " marked as pending : number of links is " + count + ", threshold is " + threshold);
        response.setPending();
        response.incrementSpamScore();
      }
    }
  }

}
