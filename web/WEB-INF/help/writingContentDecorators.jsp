<div class="contentItem">
  <h1>Writing Content Decorators</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      <a href="./help/contentDecorators.html">Content decorators</a> are a type of Pebble plugin that allow you to extend/change the way that
      blog entries, comments, TrackBacks and static pages are displayed in the HTML pages of your blog and the XML newsfeeds that are
      generated. To write your own custom decorator, you need to write a Java class that implements the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/decorator/ContentDecorator.html">ContentDecorator</a> interface.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.api.decorator;

import net.sourceforge.pebble.domain.*;

/**
 * Interface implemented by content decorators. Decorators are created on a
 * per blog basis, meaning that multiple threads can be operating on an
 * instance at any one time.
 *
 * @author    Simon Brown
 */
public interface ContentDecorator {

  /**
   * Decorates the specified blog entry.
   *
   * @param context     the context in which the decoration is running
   * @param blogEntry   the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry);

  /**
   * Decorates the specified comment.
   *
   * @param context     the context in which the decoration is running
   * @param comment     the comment to be decorated
   */
  public void decorate(ContentDecoratorContext context, Comment comment);

  /**
   * Decorates the specified TrackBack.
   *
   * @param context     the context in which the decoration is running
   * @param trackBack   the TrackBack to be decorated
   */
  public void decorate(ContentDecoratorContext context, TrackBack trackBack);

  /**
   * Decorates the specified static page.
   *
   * @param context       the context in which the decoration is running
   * @param staticPage    the static page to be decorated
   */
  public void decorate(ContentDecoratorContext context, StaticPage staticPage);

  /**
   * Gets the blog to which this decorator is associated.
   *
   * @return  a Blog instance
   */
  public Blog getBlog();

  /**
   * Sets the blog to which this decorator is associated.
   *
   * @param blog    a Blog instance
   */
  public void setBlog(Blog blog);

}</pre>

    <p>
      A default implementation is also available to subclass, called <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/decorator/ContentDecoratorSupport.html">ContentDecoratorSupport</a>.
    </p>

    <p>
      Each of the methods are called prior to the object being rendered on a web page or within a newsfeed, with the
      idea being that you manipulate the properties of the object as necessary. The code for the <code>RelativeUriDecorator</code> is shown here as an example.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.Attachment;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Translates relative URIs in blog entries and static pages into absolute URLs.
 *
 * @author Simon Brown
 */
public class RelativeUriDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    blogEntry.setBody(replaceCommonUris(blogEntry.getBody()));
    blogEntry.setExcerpt(replaceCommonUris(blogEntry.getExcerpt()));

    Attachment attachment = blogEntry.getAttachment();
    if (attachment != null) {
      String attachmentUrl = attachment.getUrl();
      if (attachmentUrl.startsWith("./")) {
        attachment.setUrl(getBlog().getUrl() + attachmentUrl.substring(2));
      }
    }
  }

  /**
   * Decorates the specified static page.
   *
   * @param context    the context in which the decoration is running
   * @param staticPage the static page to be decorated
   */
  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    staticPage.setBody(replaceCommonUris(staticPage.getBody()));
  }

  /**
   * Helper method to replace common relative URIs with their absolute values.
   *
   * @param s   the String containing relative URIs
   * @return    a new String containing absolute URLs
   */
  private String replaceCommonUris(String s) {
    s = s.replaceAll("\\./images/", getBlog().getUrl() + "images/");
    s = s.replaceAll("\\./files/", getBlog().getUrl() + "files/");
    return s;
  }

}</pre>

  </div>
</div>