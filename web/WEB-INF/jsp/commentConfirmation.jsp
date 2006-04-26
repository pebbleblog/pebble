<div class="contentItem">

  <div class="title"><fmt:message key="comment.addComment" /></div>
  <div class="subtitle">&nbsp;</div>

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
    <fmt:message key="common.backTo" /> <a href="${blogEntry.permalink}">${blogEntry.title}</a>.
    </p>
  </div>

</div>
