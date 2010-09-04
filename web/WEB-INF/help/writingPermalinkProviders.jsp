<div class="contentItem">
  <h1>Writing Permalink Providers</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    <a href="./help/permalinkProviders.html">Permalink Providers</a> are a type of Pebble plugin that allow you to extend the way that
    permalinks are generated and handled. To write your own peramlink provider, you need to write a Java class that implements the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/permalink/PermalinkProvider.html">net.sourceforge.pebble.api.permalink.PermalinkProvider</a> interface.
    </p>

    <p>
    A partial implementation is also available to subclass, called <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/permalink/PermalinkProviderSupport.html">net.sourceforge.pebble.permalink.PermalinkProviderSupport</a>.
    </p>

    <p>
      The code for the <code>ShortPermalinkProvider</code> is shown here as an example.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.permalink;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Blog;

/**
 * Generates permalinks using the pattern &lt;time-in-millis&gt;.
 *
 * @author Simon Brown
 */
public class ShortPermalinkProvider extends PermalinkProviderSupport {

  /** the regex used to check for a blog entry permalink */
  private static final String BLOG_ENTRY_PERMALINK_REGEX = "/\\d*.html";

  /**
   * Gets the permalink for a blog entry.
   *
   * @return  a URI as a String
   */
  public String getPermalink(BlogEntry blogEntry) {
    return "/" + blogEntry.getId() + ".html";
  }

  /**
   * Determines whether the specified URI is a blog entry permalink.
   *
   * @param uri   a relative URI
   * @return      true if the URI represents a permalink to a blog entry,
   *              false otherwise
   */
  public boolean isBlogEntryPermalink(String uri) {
    if (uri != null) {
      return uri.matches(BLOG_ENTRY_PERMALINK_REGEX);
    } else {
      return false;
    }
  }

  /**
   * Gets the blog entry referred to by the specified URI.
   *
   * @param uri   a relative URI
   * @return  a BlogEntry instance, or null if one can't be found
   */
  public BlogEntry getBlogEntry(String uri) {
    // uri is of the form /1234567890123.html, so extract the 13-digit ID
    // and use it to find the correct blog entry
    Blog blog = getBlog();
    return blog.getBlogEntry(uri.substring(1, 14));
  }

}</pre>
  </div>
</div>