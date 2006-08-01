<div class="contentItem">
  <h1>Writing TrackBack Listeners</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    <a href="./help/trackbackListeners.html">TrackBack listeners</a> are a type of Pebble plugin that allow you to write code that is called
    whenever a TrackBack is added, removed, approved or rejected. To write your own listener, you need to write a Java class that implements the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/trackback/TrackBackListener.html">TrackBackListener</a> interface
    as shown below. To gain access to the associated <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/TrackBack.html">TrackBack</a>, use the <code>getTrackBack()</code> method on the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/trackback/TrackBackEvent.html#getTrackBack()">TrackBackEvent</a> object
    that is passed to the callback methods.
    </p>

    <p>
      An empty implementation is provided by the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/event/trackback/TrackBackListenerSupport.html">TrackBackListenerSupport</a> class, which
      you can use as a starting point. If you are writing an implementation that is useful for both comments and TrackBacks, you can
      use the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/event/response/BlogEntryResponseListenerSupport.html">BlogEntryResponseListenerSupport</a>
      as a starting point instead, where there are similar blogEntryResponseX() methods that can be overridden.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.api.event.trackback;

/**
 * Implemented by classes wanting to be notified of TrackBack events.
 *
 * @author Simon Brown
 */
public interface TrackBackListener {

  /**
   * Called when a TrackBack has been added.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackAdded(TrackBackEvent event);

  /**
   * Called when a TrackBack has been removed.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackRemoved(TrackBackEvent event);

  /**
   * Called when a TrackBack has been approved.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackApproved(TrackBackEvent event);

  /**
   * Called when a TrackBack has been rejected.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackRejected(TrackBackEvent event);

}</pre>

  </div>
</div>