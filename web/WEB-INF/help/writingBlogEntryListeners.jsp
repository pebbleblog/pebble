<div class="contentItem">
  <h1>Writing Blog Entry Listeners</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    Blog entry listeners are a type of Pebble plugin that allow you to write code that is called
    whenever a blog entry is added, removed, changed, published or unpublished. To write your own listener, you need to write a Java class that implements the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/blogentry/BlogEntryListener.html">net.sourceforge.pebble.api.event.blogentry.BlogEntryListener</a> interface
    as shown below. To gain access to the associated <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/BlogEntry.html">blog entry</a>, use the <code>getBlogEntry()</code> method on the
    <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/blogentry/BlogEntryEvent.html#getBlogEntry()">net.sourceforge.pebble.api.listener.blogentry.BlogEntryEvent</a> object
    that is passed to the callback methods.
    </p>

    <p>
      An empty implementation is provided by the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/event/blogentry/BlogEntryListenerSupport.html">net.sourceforge.pebble.event.blogentry.BlogEntryListenerSupport</a> class, which
      you can use as a starting point.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.api.event.blogentry;

/**
 * Implemented by classes wanting to be notified of blog entry events.
 *
 * @author Simon Brown
 */
public interface BlogEntryListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event);

  /**
   * Called when a blog entry has been removed.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryRemoved(BlogEntryEvent event);

  /**
   * Called when a blog entry has been changed.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event);

  /**
   * Called when a blog entry has been published.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryPublished(BlogEntryEvent event);

  /**
   * Called when a blog entry has been unpublished.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryUnpublished(BlogEntryEvent event);

}</pre>
  </div>
</div>