<div class="contentItem">

  <h1><fmt:message key="comment.addComment" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <c:choose>
      <c:when test="${not blogEntry.commentsEnabled}">
        <fmt:message key="comment.commentsDisabled" />
      </c:when>
      <c:when test="${comment.approved}">
        <fmt:message key="comment.commentPosted" />
      </c:when>
      <c:when test="${comment.rejected}">
        <fmt:message key="comment.commentRejected" />
      </c:when>
      <c:otherwise>
        <fmt:message key="comment.commentPending" />
      </c:otherwise>
    </c:choose>

    <p>
    <fmt:message key="common.backTo" /> <a href="${url:rewrite(blogEntry.permalink)}">${blogEntry.title}</a>.
    </p>
  </div>

</div>
