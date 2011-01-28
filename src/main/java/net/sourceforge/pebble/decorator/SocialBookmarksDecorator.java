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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ResourceBundle;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.util.I18n;

/**
 * Adds links to the social bookmarking sites to add current blog entry
 * 
 * @author Alexander Zagniotov
 */
public class SocialBookmarksDecorator extends ContentDecoratorSupport {

	private static final String TITLE = "&amp;title=";
	private static final String TITLE_FURL_YAHOO = "&amp;t=";

	private static final String SLASHDOT_URL = "http://slashdot.org/bookmark.pl?url=";
	private static final String DIGG_URL = "http://digg.com/submit?url=";
	private static final String REDDIT_URL = "http://reddit.com/submit?url=";
	private static final String DELICIOUS_URL = "http://del.icio.us/post?url=";
	private static final String STUMBLEUPON_URL = "http://www.stumbleupon.com/submit?url=";
	private static final String GOOGLE_URL = "http://www.google.com/bookmarks/mark?op=edit&amp;bkmk=";
	private static final String TECHNORATI_URL = "http://technorati.com/faves?add=";
	private static final String BLOGLINES_URL = "http://www.bloglines.com/sub/";
	private static final String FACEBOOK_URL = "http://www.facebook.com/share.php?u=";
	private static final String FURL_URL = "http://www.furl.net/storeIt.jsp?u=";
	private static final String WINDOWSLIVE_URL = "https://favorites.live.com/quickadd.aspx?mkt=en-us&amp;url=";
	private static final String YAHOO_URL = "http://bookmarks.yahoo.com/toolbar/savebm?opener=tb&amp;u=";

	private static final String SLASHDOT_IMG = "<img src=\"common/images/slashdot.png\" alt=\"Add this post to Slashdot\" border=\"0\" />";
	private static final String DIGG_IMG = "<img src=\"common/images/digg.png\" alt=\"Add this post to Digg\" border=\"0\" />";
	private static final String REDDIT_IMG = "<img src=\"common/images/reddit.png\" alt=\"Add this post to Reddit\" border=\"0\" />";
	private static final String DELICIOUS_IMG = "<img src=\"common/images/delicious.png\" alt=\"Add this post to Delicious\" border=\"0\" />";
	private static final String STUMBLEUPON_IMG = "<img src=\"common/images/stumbleupon.png\" alt=\"Add this post to Stumble it\" border=\"0\" />";
	private static final String GOOGLE_IMG = "<img src=\"common/images/google.png\" alt=\"Add this post to Google\" border=\"0\" />";
	private static final String TECHNORATI_IMG = "<img src=\"common/images/technorati.png\" alt=\"Add this post to Technorati\" border=\"0\" />";
	private static final String BLOGLINES_IMG = "<img src=\"common/images/bloglines.png\" alt=\"Add this post to Bloglines\" border=\"0\" />";
	private static final String FACEBOOK_IMG = "<img src=\"common/images/facebook.png\" alt=\"Add this post to Facebook\" border=\"0\" />";
	private static final String FURL_IMG = "<img src=\"common/images/furl.png\" alt=\"Add this post to Furl\" border=\"0\" />";
	private static final String WINDOWSLIVE_IMG = "<img src=\"common/images/windowslive.png\" alt=\"Add this post to Windows Live\" border=\"0\" />";
	private static final String YAHOO_IMG = "<img src=\"common/images/yahoo.png\" alt=\"Add this post to Yahoo!\" border=\"0\" />";

	private static final String SLASHDOT_ALT = "socialbookmark.addToSlashdot";
	private static final String DIGG_ALT = "socialbookmark.addToDigg";
	private static final String REDDIT_ALT = "socialbookmark.addToReddit";
	private static final String DELICIOUS_ALT = "socialbookmark.addToDelicious";
	private static final String STUMBLEUPON_ALT = "socialbookmark.addToStumbleupon";
	private static final String GOOGLE_ALT = "socialbookmark.addToGoogle";
	private static final String TECHNORATI_ALT = "socialbookmark.addToTechnorati";
	private static final String BLOGLINES_ALT = "socialbookmark.addToBloglines";
	private static final String FACEBOOK_ALT = "socialbookmark.addToFacebook";
	private static final String FURL_ALT = "socialbookmark.addToFurl";
	private static final String WINDOWSLIVE_ALT = "socialbookmark.addToWindowsLive";
	private static final String YAHOO_ALT = "socialbookmark.addToYahoo";

	private static final String[] bookmarkingSites = { SLASHDOT_URL, DIGG_URL,
			REDDIT_URL, DELICIOUS_URL, STUMBLEUPON_URL, GOOGLE_URL, TECHNORATI_URL, BLOGLINES_URL, FACEBOOK_URL,
			FURL_URL, WINDOWSLIVE_URL, YAHOO_URL};

	private static final String[] bookmarkingNames = { SLASHDOT_IMG, DIGG_IMG,
			REDDIT_IMG, DELICIOUS_IMG, STUMBLEUPON_IMG, GOOGLE_IMG, TECHNORATI_IMG, BLOGLINES_IMG, FACEBOOK_IMG,
			FURL_IMG, WINDOWSLIVE_IMG, YAHOO_IMG};

	private static final String[] bookmarkingAltText = { SLASHDOT_ALT, DIGG_ALT,
			REDDIT_ALT, DELICIOUS_ALT, STUMBLEUPON_ALT, GOOGLE_ALT, TECHNORATI_ALT, BLOGLINES_ALT, FACEBOOK_ALT,
			FURL_ALT, WINDOWSLIVE_ALT, YAHOO_ALT};

	/**
	 * Decorates the specified blog entry.
	 * 
	 * @param context
	 *            the context in which the decoration is running
	 * @param blogEntry
	 *            the blog entry to be decorated
	 */
	public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
		Blog blog = blogEntry.getBlog();
		ResourceBundle bundle = I18n.getBundle(blog.getLocale());

		String body = blogEntry.getBody();
		if (body != null && body.trim().length() > 0) {

			String html = generateDecorationHtml(bundle, blogEntry);
			blogEntry.setBody(body + html);
		}

		String excerpt = blogEntry.getExcerpt();
		if (excerpt != null && excerpt.trim().length() > 0) {

			String html = generateDecorationHtml(bundle, blogEntry);
			blogEntry.setExcerpt(excerpt + html);
		}
	}

	

	private String generateDecorationHtml(ResourceBundle bundle,
			BlogEntry blogEntry) {
		StringBuffer buf = new StringBuffer();
		String permLink = blogEntry.getPermalink();
		String title = "";
		try {
			title = URLEncoder.encode(blogEntry.getTitle(), "UTF-8");
		}
		catch(UnsupportedEncodingException e) {
			title = blogEntry.getTitle();
		}
		buf.append("<div class=\"tags\"><span>");
		buf.append(bundle.getString("common.bookmarks"));
		buf.append(" : </span>&nbsp;");

		for (int i = 0; i < bookmarkingSites.length; i++) {
			buf.append("<a href=\"");
			buf.append(bookmarkingSites[i] + permLink);


			if (bookmarkingSites[i] != TECHNORATI_URL && 
				bookmarkingSites[i] != BLOGLINES_URL && 
				bookmarkingSites[i] != FACEBOOK_URL &&
				bookmarkingSites[i] != FURL_URL &&
				bookmarkingSites[i] != YAHOO_URL) {
				buf.append(TITLE + title + "\"");
			} 

			else if (bookmarkingSites[i] == FURL_URL || bookmarkingSites[i] == YAHOO_URL) {
				buf.append(TITLE_FURL_YAHOO + title + "\"");
			}

			else {
				buf.append("\"");
			}

			buf.append(" target=\"_blank\"");
			buf.append(" title=\"" + bundle.getString(bookmarkingAltText[i]) + "\">");
			buf.append(bookmarkingNames[i]);
			buf.append("</a>");

			if (i < bookmarkingSites.length - 1) {
				buf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			}
		}

		buf.append("</div>");
		return buf.toString();
	}

}