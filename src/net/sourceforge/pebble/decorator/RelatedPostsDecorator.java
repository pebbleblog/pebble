package net.sourceforge.pebble.decorator;

import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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


  private static final Log log = LogFactory.getLog(RelatedPostsDecorator.class);


	/** the name of the max number of posts property */
  public static final String MAX_POSTS = "RelatedPostsDecorator.maxPosts";

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {

	PluginProperties props = blogEntry.getBlog().getPluginProperties();
    int maxPosts = StringUtils.MAX_NUM_OF_POSTS;

	if (props.hasProperty(RelatedPostsDecorator.MAX_POSTS)) {
      try {
        maxPosts = Integer.parseInt(props.getProperty(MAX_POSTS));
      } catch (NumberFormatException nfe) {
        log.error(nfe.getMessage());
        // do nothing, the value has already been defaulted
      }
    }

    Blog blog = blogEntry.getBlog();
    ResourceBundle bundle = ResourceBundle.getBundle("resources", blog.getLocale());

	String body = blogEntry.getBody();

	if (body != null && body.trim().length() > 0) {

		StringBuffer buf = new StringBuffer();
		buf.append(body);
		buf.append("<p><b>" + bundle.getString("common.relatedPosts") + "</b><br />");
		
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
						  buf.append("<a href=\"" + entry.getPermalink() + "\" rel=\"bookmark\" title=\"" + entry.getTitle() + "\">" + entry.getTitle() + "</a><br />");
				  }
			}

            // do not allow more than default amount of posts or 
			// amount set through the RelatedPostsDecorator.maxPosts property
			if (relatedEntries.size() == maxPosts)  {
				break;
			}
		}

		if (relatedEntries.size() == 0)
		buf.append("<i>" + bundle.getString("common.noRelatedPosts") + "</i>");

		buf.append("</p><br />");
		blogEntry.setBody(buf.toString());	
	}
  }
}
