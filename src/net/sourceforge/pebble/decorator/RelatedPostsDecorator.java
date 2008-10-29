package net.sourceforge.pebble.decorator;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.ResourceBundle;

/**
 * Adds related posts to the current post. 
 * The posts are selected by matching tags of the current post to the tags 
 * of other posts in the blog. One related post per tag.
 * 
 * Each blog entry can have up to six related posts or none.
 *
 * @author Alexander Zagniotov
 */
public class RelatedPostsDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();
    ResourceBundle bundle = ResourceBundle.getBundle("resources", blog.getLocale());

	String body = blogEntry.getBody();

	if (body != null && body.trim().length() > 0) {

		StringBuffer buf = new StringBuffer();
		buf.append(body);
		buf.append("<p><b>" + bundle.getString("common.relatedPosts") + "</b><br/>");
		
		// tags of the current entry
		List<Tag> currentEntryTags = blogEntry.getAllTags();

        // all blog entries of the current blog
		List<BlogEntry> allBlogEntries = (List<BlogEntry>) blog.getBlogEntries();

		// temporary holder for accumulated unique related posts.
		// using hash set assures that we wont have same related post twice for different tags.
		Set<BlogEntry> relatedEntries = new HashSet<BlogEntry>();

		for (BlogEntry entry : allBlogEntries)  {

            // don't add current entry as a related post of it self, skip it
			if (entry.getTitle().equals(blogEntry.getTitle()))   
				continue;

			// loop through each of the current entry tags, and try to find related 
			// post by matching current tag to the posts tags
			for (Tag currentTag : currentEntryTags)  {
				  if (entry.hasTag(currentTag.getName())) {
					  // if we successfully selected related post - create hyperlink for it
					  if (relatedEntries.add(entry)) 
						  buf.append("<a href=\"" + entry.getPermalink() + "\">" + entry.getTitle() + "</a><br/>");
				  }
			}

            // do not allow more than 6 posts
			if (relatedEntries.size() == 6)
				break;
		}

		if (relatedEntries.size() == 0)
		buf.append("<i>" + bundle.getString("common.noRelatedPosts") + "</i>");

		buf.append("</p><br/>");
		blogEntry.setBody(buf.toString());	
	}
  }
}