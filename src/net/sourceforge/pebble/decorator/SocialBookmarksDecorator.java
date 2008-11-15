package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.ResourceBundle;

/**
 * Adds links to the social bookmarking sites to add current blog entry
 * 
 * @author Alexander Zagniotov
 */
public class SocialBookmarksDecorator extends ContentDecoratorSupport {

	private static final String TITLE = "&title=";

	private static final String SLASHDOT_URL = "http://slashdot.org/bookmark.pl?url=";
	private static final String DIGG_URL = "http://digg.com/submit?url=";
	private static final String REDDIT_URL = "http://reddit.com/submit?url=";
	private static final String DELICIOUS_URL = "http://del.icio.us/post?url=";
	private static final String STUMBLEUPON_URL = "http://www.stumbleupon.com/submit?url=";
	private static final String GOOGLE_URL = "http://www.google.com/bookmarks/mark?op=edit&bkmk=";
	private static final String TECHNORATI_URL = "http://technorati.com/faves?add=";

	private static final String SLASHDOT_IMG = "<img src=\"common/images/slashdot.png\" border=\"0\"/>";
	private static final String DIGG_IMG = "<img src=\"common/images/digg.png\" border=\"0\"/>";
	private static final String REDDIT_IMG = "<img src=\"common/images/reddit.png\" border=\"0\"/>";
	private static final String DELICIOUS_IMG = "<img src=\"common/images/delicious.png\" border=\"0\"/>";
	private static final String STUMBLEUPON_IMG = "<img src=\"common/images/stumbleit.png\" border=\"0\"/>";
	private static final String GOOGLE_IMG = "<img src=\"common/images/google.ico\" border=\"0\"/>";
	private static final String TECHNORATI_IMG = "<img src=\"common/images/technorati.png\" border=\"0\"/>";

	private static final String SLASHDOT_ALT = "Add this post to Slash Dot";
	private static final String DIGG_ALT = "Digg this post";
	private static final String REDDIT_ALT = "Add this post to Reddit";
	private static final String DELICIOUS_ALT = "Save this post to Del.icio.us";
	private static final String STUMBLEUPON_ALT = "Stumble this post";
	private static final String GOOGLE_ALT = "Add this post to Google";
	private static final String TECHNORATI_ALT = "Add this post to Technorati";

	private static final String[] bookmarkingSites = { SLASHDOT_URL, DIGG_URL,
			REDDIT_URL, DELICIOUS_URL, STUMBLEUPON_URL, GOOGLE_URL };
	private static final String[] bookmarkingNames = { SLASHDOT_IMG, DIGG_IMG,
			REDDIT_IMG, DELICIOUS_IMG, STUMBLEUPON_IMG, GOOGLE_IMG };
	private static final String[] bookmarkingAltText = { SLASHDOT_ALT,
			DIGG_ALT, REDDIT_ALT, DELICIOUS_ALT, STUMBLEUPON_ALT, GOOGLE_ALT };

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
		ResourceBundle bundle = ResourceBundle.getBundle("resources", blog
				.getLocale());

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
		String title = blogEntry.getTitle();

		buf.append("<div class=\"tags\"><span>");
		buf.append(bundle.getString("common.bookmarks"));
		buf.append(" : </span>");

		for (int i = 0; i < bookmarkingSites.length; i++) {
			buf.append("<a href=\"");
			buf.append(bookmarkingSites[i] + permLink);
			buf.append(TITLE + title + "\"");
			buf.append(" target=\"_blank\"");
			buf.append(" alt=\"" + bookmarkingAltText[i] + "\"");
			buf.append(" title=\"" + bookmarkingAltText[i] + "\">");
			buf.append(bookmarkingNames[i]);
			buf.append("</a>");
			buf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		}

		buf.append("<a href=\"");
		buf.append(TECHNORATI_URL + permLink + "\"");
		buf.append(" target=\"_blank\"");
		buf.append(" alt=\"" + TECHNORATI_ALT + "\"");
		buf.append(" title=\"" + TECHNORATI_ALT + "\">");
		buf.append(TECHNORATI_IMG);
		buf.append("</a>");

		buf.append("</div>");
		return buf.toString();
	}

}