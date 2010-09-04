<%--
  Renders a collection of TrackBacks, calling trackback.jsp for each TrackBack.

  ${blog} : the current Blog instance
  ${blogEntry} : the current BlogEntry instance
--%>
<a name="trackbacks"></a>

<c:forEach var="trackback" items="${blogEntry.trackBacks}" varStatus="status">
  <c:set var="trackback" scope="request" value="${trackback}"/>
  <c:choose>
    <c:when test="${status.count % 2 == 0}">
      <div class="even">
    </c:when>
    <c:otherwise>
      <div class="odd">
    </c:otherwise>
  </c:choose>
    <jsp:include page="trackback.jsp"/>
    </div>
</c:forEach>