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

package net.sourceforge.pebble.web.view.impl;

import com.rometools.modules.content.ContentModuleImpl;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.mock.MockHttpServletRequest;
import net.sourceforge.pebble.mock.MockHttpServletResponse;
import net.sourceforge.pebble.web.model.Model;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.InstanceofPredicate;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;

/**
 * @author James Roper
 */
public class FeedViewTest extends SingleBlogTestCase {

  public void testGetFeed() throws Exception {
    FeedView feedView = new FeedView(AbstractRomeFeedView.FeedType.ATOM);
    Model model = new Model();
    feedView.setModel(model);

    BlogEntry entry = new BlogEntry(blog);
    entry.setTitle("My Title");
    entry.setAuthor("author");
    entry.setBody("body");
    entry.setDate(new Date(1000));

    Category category = new Category("categoryId", "category");
    category.setBlog(blog);
    entry.setCategories(Collections.singleton(category));

    model.put(Constants.BLOG_KEY, blog);
    model.put(Constants.BLOG_ENTRIES, Collections.singleton(entry));

    SyndFeed feed = feedView.getFeed();
    MockHttpServletResponse response = new MockHttpServletResponse();
    response.setWriter(new PrintWriter(System.out));
    feedView.dispatch(new MockHttpServletRequest(), response, null);
    assertEquals("tag:www.yourdomain.com,0000-00-00:default", feed.getUri());
    assertEquals("My blog", feed.getTitle());
    assertEquals(1, feed.getEntries().size());
    SyndEntry feedEntry = (SyndEntry) feed.getEntries().get(0);
    assertEquals("tag:www.yourdomain.com,1970-01-01:default/1000", feedEntry.getUri());
    assertEquals(new Date(1000), feedEntry.getPublishedDate());
    assertEquals("body", ((ContentModuleImpl)CollectionUtils.select(feedEntry.getModules(), new InstanceofPredicate(ContentModuleImpl.class)).iterator().next()).getEncodeds().get(0));
    assertEquals("My Title", feedEntry.getTitle());
    assertEquals(entry.getPermalink(), feedEntry.getLink());
    assertEquals(1, feedEntry.getCategories().size());
    SyndCategory syndCategory = (SyndCategory) feedEntry.getCategories().get(0);
    assertEquals("category", syndCategory.getName());
    assertEquals(category.getPermalink(), syndCategory.getTaxonomyUri());
  }
}
