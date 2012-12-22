<%--
  Renders a collection of comments, calling comment.jsp for each comment.

  ${blog} : the current Blog instance
  ${blogEntry} : the current BlogEntry instance
--%>
<a name="comments"></a>
<div id="comments">
<c:forEach var="comment" items="${blogEntry.comments}" varStatus="status">
  <c:set var="comment" scope="request" value="${comment}"/>
  <c:choose>
    <c:when test="${status.count % 2 == 0}">
      <div class="even">
    </c:when>
    <c:otherwise>
      <div class="odd">
    </c:otherwise>
  </c:choose>
      <jsp:include page="comment.jsp"><jsp:param name="commentIdentifier" value="comment${comment.id}"/></jsp:include>
      </div>
</c:forEach>
</div>
