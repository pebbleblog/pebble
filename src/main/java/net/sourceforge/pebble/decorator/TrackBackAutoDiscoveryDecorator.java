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

/**
 * Generates TrackBack Auto-Discovery links for blog entries. See
 * http://www.sixapart.com/pronet/docs/trackback_spec for more details.
 *
 * @author Simon Brown
 */
public class TrackBackAutoDiscoveryDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    StringBuffer html = new StringBuffer();
    html.append("\n<!--\n");
    html.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
    html.append("         xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n");
    html.append("         xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n");
    html.append("<rdf:Description\n");
    html.append("    rdf:about=\"");
    html.append(blogEntry.getLocalPermalink());
    html.append("\"\n");
    html.append("    dc:identifier=\"");
    html.append(blogEntry.getLocalPermalink());
    html.append("\"\n");
    html.append("    dc:title=\"");
    html.append(blogEntry.getTitle());
    html.append("\"\n");
    html.append("    trackback:ping=\"");
    html.append(blogEntry.getTrackBackLink());
    html.append("\" />\n");
    html.append("</rdf:RDF>\n");
    html.append("-->");

    blogEntry.setBody(blogEntry.getBody() + html);
  }

}
