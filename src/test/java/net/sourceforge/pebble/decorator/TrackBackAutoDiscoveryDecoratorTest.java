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

import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.Calendar;

/**
 * Tests for the TrackBackAutoDisoveryDecorator class.
 *
 * @author    Simon Brown
 */
public class TrackBackAutoDiscoveryDecoratorTest extends SingleBlogTestCase {

  private static final String DISCOVERY_TEXT_START;
  private static final String DISCOVERY_TEXT_END;
  static {
    DISCOVERY_TEXT_START = "\n<!--\n" +
        "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
        "         xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
        "         xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n" +
        "<rdf:Description\n" +
        "    rdf:about=\"http://www.yourdomain.com/blog/2006/07/14/1152860400000.html\"\n" +
        "    dc:identifier=\"http://www.yourdomain.com/blog/2006/07/14/1152860400000.html\"\n" +
        "    dc:title=\"Title\"\n" +
        "    trackback:ping=\"http://www.yourdomain.com/blog/addTrackBack.action?entry=1152860400000&token=";
    DISCOVERY_TEXT_END = "\" />\n" +
        "</rdf:RDF>\n" +
        "-->";
  }

  private ContentDecorator decorator;
  private BlogEntry blogEntry;
  private ContentDecoratorContext context;

  protected void setUp() throws Exception {
    super.setUp();

    blogEntry = new BlogEntry(blog);
    blogEntry.setTitle("Title");
    blogEntry.setBody("Body");
    Calendar cal= blog.getCalendar();
    cal.set(Calendar.YEAR, 2006);
    cal.set(Calendar.MONTH, 6);
    cal.set(Calendar.DAY_OF_MONTH, 14);
    cal.set(Calendar.HOUR_OF_DAY, 8);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    blogEntry.setDate(cal.getTime());
    decorator = new TrackBackAutoDiscoveryDecorator();
    context = new ContentDecoratorContext();
  }

  /**
   * Tests that the blog entry body is decorated correctly.
   */
  public void testDecoration() throws Exception {
    decorator.decorate(context, blogEntry);
    assertTrue(blogEntry.getBody().startsWith("Body" + DISCOVERY_TEXT_START));
    assertTrue(blogEntry.getBody().endsWith(DISCOVERY_TEXT_END));
  }

}
