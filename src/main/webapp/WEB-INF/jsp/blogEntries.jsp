<%--
  renders a collection of BlogEntries, calling blogEntry.jsp for each blog entry
--%>
<c:choose>
  <c:when test="${empty blogEntries}">
    <div class="contentItem">

      <div class="contentItemBody">
        <fmt:message key="blogentry.noBlogEntries" />
      </div>

    </div>
  </c:when>

  <c:otherwise>
    <c:forEach var="blogEntry" items="${blogEntries}">
      <c:set var="blogEntry" value="${blogEntry}" scope="request"/>
      <jsp:include page="blogEntry.jsp"/>
    </c:forEach>
  </c:otherwise>
</c:choose>