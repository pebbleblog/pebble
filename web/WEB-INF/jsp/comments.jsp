<%--
  Renders a collection of comments, calling comment.jsp for each comment.

  ${blog} : the current Blog instance
  ${blogEntry} : the current BlogEntry instance
--%>
<a name="comments"></a>
<div class="contentItem">

  <div class="contentItemLinks">
    <c:if test="${blogEntry.commentsEnabled}" >
    <a href="replyToBlogEntry.action?entry=${blogEntry.id}">Reply</a>
    </c:if>
  </div>

  <div class="title"><fmt:message key="blogentry.comments" /></div>
  <div class="subtitle">&nbsp;</div>

  <c:choose>
    <c:when test="${blogEntry.numberOfComments > 0}" >
      <c:forEach var="comment" items="${blogEntry.comments}" varStatus="status">
        <c:set var="comment" scope="request" value="${comment}"/>
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <div class="even" style="padding-left: ${(comment.numberOfParents*16)}px; padding-top: 0px; padding-bottom: 0px; padding-right: 0px">
          </c:when>
          <c:otherwise>
            <div class="odd" style="padding-left: ${(comment.numberOfParents*16)}px; padding-top: 0px; padding-bottom: 0px; padding-right: 0px">
          </c:otherwise>
        </c:choose>
            <jsp:include page="comment.jsp"/>
            </div>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="contentItemBody" style="padding: 8px">
        <fmt:message key="comment.noComments" />
      </div>
    </c:otherwise>
  </c:choose>

</div>