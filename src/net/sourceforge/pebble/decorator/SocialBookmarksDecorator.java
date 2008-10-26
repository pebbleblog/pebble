package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.PluginProperties;
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

	private static final String SLASHDOT = "SlashDot It!";
	private static final String DIGG = "Digg this!";
	private static final String REDDIT = "Reddit!";
	private static final String DELICIOUS = "Save to del.icio.us!";
	private static final String STUMBLEUPON = "Stumble it!";

	private static final String[] bookmarkingSites = { SLASHDOT_URL, DIGG_URL,
			REDDIT_URL, DELICIOUS_URL, STUMBLEUPON_URL };
	private static final String[] bookmarkingNames = { SLASHDOT, DIGG, REDDIT,
			DELICIOUS, STUMBLEUPON };

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
			StringBuffer buf = new StringBuffer();

			String permLink = blogEntry.getPermalink();
			String title = blogEntry.getTitle();

			buf.append(body);
			buf.append("<div class=\"tags\"><span>");
			buf.append(bundle.getString("common.bookmarks"));
			buf.append(" : </span>");

			for (int i = 0; i < bookmarkingSites.length; i++) {
				buf.append("<a href=\"");
				buf.append(bookmarkingSites[i] + permLink);
				buf.append(TITLE + title + "\"");
				buf.append(" target=\"_blank\">");
				buf.append(bookmarkingNames[i]);
				buf.append("</a>");

				if (i != bookmarkingSites.length - 1) {
					buf.append("   ");
				}
			}

			buf.append("</div>");
			blogEntry.setBody(buf.toString());
		}
	}
}