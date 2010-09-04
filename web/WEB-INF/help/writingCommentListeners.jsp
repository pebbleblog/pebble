<div class="contentItem">
  <h1>Writing Comment Listeners</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    <a href="./help/commentListeners.html">Comment listeners</a> are a type of Pebble plugin that allow you to write code that is called
    whenever a comment is added, removed, approved or rejected. To write your own listener, you need to write a Java class that implements the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/comment/CommentListener.html">CommentListener</a> interface
    as shown below. To gain access to the associated
    <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/Comment.html">comment</a>, use the <code>getComment()</code> method on the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/event/comment/CommentEvent.html#getComment()">CommentEvent</a> object
    that is passed to the callback methods.
    </p>

    <p>
      An empty implementation is provided by the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/event/comment/CommentListenerSupport.html">CommentListenerSupport</a> class, which
      you can use as a starting point. If you are writing an implementation that is useful for both comments and TrackBacks, you can
      use the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/event/response/BlogEntryResponseListenerSupport.html">BlogEntryResponseListenerSupport</a>
      as a starting point instead, where there are similar blogEntryResponseX() methods that can be overridden.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.api.event.comment;

/**
 * Implemented by classes wanting to be notified of comment events.
 *
 * @author Simon Brown
 */
public interface CommentListener {

  /**
   * Called when a comment has been added.
   *
   * @param event   a CommentEvent instance
   */
  public void commentAdded(CommentEvent event);

  /**
   * Called when a comment has been removed.
   *
   * @param event   a CommentEvent instance
   */
  public void commentRemoved(CommentEvent event);

  /**
   * Called when a comment has been approved.
   *
   * @param event   a CommentEvent instance
   */
  public void commentApproved(CommentEvent event);

  /**
   * Called when a comment has been rejected.
   *
   * @param event   a CommentEvent instance
   */
  public void commentRejected(CommentEvent event);

}</pre>

  </div>
</div>