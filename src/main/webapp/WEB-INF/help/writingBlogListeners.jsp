<div class="contentItem">
  <h1>Writing Blog Listeners</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      <a href="./help/blogListeners.html">Blog Listeners</a> are a type of Pebble plugin that allow you to write code that is called
      whenever your blog is started and stopped. To write your own listener, you need to write a Java class that implements the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/blog/BlogListener.html">net.sourceforge.pebble.api.event.blog.BlogListener</a> interface
      as shown below. To gain access to the associated <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/Blog.html">blog</a>, use the <code>getBlog()</code> method on the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/blog/BlogEvent.html#getBlog()">net.sourceforge.pebble.api.event.blog.BlogEvent</a> object
      that is passed to the callback methods.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.api.event.blog;

/**
 * Implemented by classes wanting to be notified of blog events.
 *
 * @author Simon Brown
 */
public interface BlogListener {

  /**
   * Called when a blog has been started.
   *
   * @param event   a BlogEvent instance
   */
  public void blogStarted(BlogEvent event);

  /**
   * Called when a blog has been stopped.
   *
   * @param event   a BlogEvent instance
   */
  public void blogStopped(BlogEvent event);

}</pre>
  </div>
</div>