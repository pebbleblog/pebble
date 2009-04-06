package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.ResourceBundle;

/**
 * Allow to export current blog entry as PDF document
 * 
 * @author Alexander Zagniotov
 */
public class EntryToPdfDecorator extends ContentDecoratorSupport {

	/**
	 * Decorates the specified blog entry.
	 * 
	 * @param context
	 *            the context in which the decoration is running
	 * @param blogEntry
	 *            the blog entry to be decorated
	 */
	public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {

		String body = blogEntry.getBody();
		if (body != null && body.trim().length() > 0) {

			String html = generateDecorationHtml(blogEntry);
			blogEntry.setBody(body + html);
		}

		String excerpt = blogEntry.getExcerpt();
		if (excerpt != null && excerpt.trim().length() > 0) {

			String html = generateDecorationHtml(blogEntry);
			blogEntry.setExcerpt(excerpt + html);
		}
	}

	private String generateDecorationHtml(BlogEntry blogEntry) {

			StringBuffer buf = new StringBuffer();
			String title = blogEntry.getTitle();
			String subtitle = blogEntry.getSubtitle();

			buf.append("<p>");
			buf.append("You can ");
			buf.append("<a href=\"" + blogEntry.getBlog().getUrl() + "entryToPDF.action?entry=" + blogEntry.getId() + "\" title=\"Export " + title + " - " + subtitle + " as PDF document\">");
			buf.append("export this post");
			buf.append("</a> as PDF document</p>");

			return buf.toString();
	}

}