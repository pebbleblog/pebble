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

import java.util.Date;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Tests for the SocialBookmarksDecorator class.
 *
 * @author    Alexander Zagniotov
 */
public class SocialBookmarksDecoratorTest extends SingleBlogTestCase {

  private ContentDecorator decorator;
  private BlogEntry blogEntry;
  private ContentDecoratorContext context;

  protected void setUp() throws Exception {
    super.setUp();

    blogEntry = new BlogEntry(blog);
    decorator = new SocialBookmarksDecorator();
    context = new ContentDecoratorContext();
  }

  /**
   * Tests that a blog entry with bookmarks gets modified, when output to a HTML page.
   */
  public void testBlogEntryHasBookmarksAndMediaIsHtml() throws Exception {
    Date date = new Date();
	long now = 1;
	date.setTime(now);

	blogEntry.setDate(date);
	blogEntry.setTitle("Bombastic Post Title");
	blogEntry.setExcerpt("Excerpt - here is some text");
    blogEntry.setBody("Body - here is some text");
	
    context.setMedia(ContentDecoratorContext.HTML_PAGE);
    decorator.decorate(context, blogEntry);

    StringBuffer bookmarks = new StringBuffer();
    bookmarks.append("<div class=\"tags\"><span>Social Bookmarks : </span>&nbsp;");
    bookmarks.append("<a href=\"http://slashdot.org/bookmark.pl?url=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;title=Bombastic+Post+Title\" target=\"_blank\" title=\"Add this post to Slash Dot\"><img src=\"common/images/slashdot.png\" alt=\"Add this post to Slashdot\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://digg.com/submit?url=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;title=Bombastic+Post+Title\" target=\"_blank\" title=\"Digg this post\"><img src=\"common/images/digg.png\" alt=\"Add this post to Digg\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://reddit.com/submit?url=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;title=Bombastic+Post+Title\" target=\"_blank\" title=\"Add this post to Reddit\"><img src=\"common/images/reddit.png\" alt=\"Add this post to Reddit\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://del.icio.us/post?url=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;title=Bombastic+Post+Title\" target=\"_blank\" title=\"Save this post to Del.icio.us\"><img src=\"common/images/delicious.png\" alt=\"Add this post to Delicious\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://www.stumbleupon.com/submit?url=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;title=Bombastic+Post+Title\" target=\"_blank\" title=\"Stumble this post\"><img src=\"common/images/stumbleupon.png\" alt=\"Add this post to Stumble it\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://www.google.com/bookmarks/mark?op=edit&amp;bkmk=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;title=Bombastic+Post+Title\" target=\"_blank\" title=\"Add this post to Google\"><img src=\"common/images/google.png\" alt=\"Add this post to Google\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://technorati.com/faves?add=http://www.yourdomain.com/blog/1970/01/01/1.html\" target=\"_blank\" title=\"Add this post to Technorati\"><img src=\"common/images/technorati.png\" alt=\"Add this post to Technorati\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://www.bloglines.com/sub/http://www.yourdomain.com/blog/1970/01/01/1.html\" target=\"_blank\" title=\"Add this post to Bloglines\"><img src=\"common/images/bloglines.png\" alt=\"Add this post to Bloglines\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://www.facebook.com/share.php?u=http://www.yourdomain.com/blog/1970/01/01/1.html\" target=\"_blank\" title=\"Add this post to Facebook\"><img src=\"common/images/facebook.png\" alt=\"Add this post to Facebook\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://www.furl.net/storeIt.jsp?u=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;t=Bombastic+Post+Title\" target=\"_blank\" title=\"Add this post to Furl\"><img src=\"common/images/furl.png\" alt=\"Add this post to Furl\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"https://favorites.live.com/quickadd.aspx?mkt=en-us&amp;url=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;title=Bombastic+Post+Title\" target=\"_blank\" title=\"Add this post to Windows Live\"><img src=\"common/images/windowslive.png\" alt=\"Add this post to Windows Live\" border=\"0\" /></a>&nbsp;&nbsp;&nbsp;&nbsp;");
	bookmarks.append("<a href=\"http://bookmarks.yahoo.com/toolbar/savebm?opener=tb&amp;u=http://www.yourdomain.com/blog/1970/01/01/1.html&amp;t=Bombastic+Post+Title\" target=\"_blank\" title=\"Add this post to Yahoo!\"><img src=\"common/images/yahoo.png\" alt=\"Add this post to Yahoo!\" border=\"0\" /></a>");
    bookmarks.append("</div>");

	assertEquals("Excerpt - here is some text" + bookmarks, blogEntry.getExcerpt());
    assertEquals("Body - here is some text" + bookmarks, blogEntry.getBody());
  }

}
