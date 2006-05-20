<%--
  Renders a collection of comments, calling comment.jsp for each comment.

  ${blog} : the current Blog instance
  ${blogEntry} : the current BlogEntry instance
--%>
<a name="comments"></a>

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
