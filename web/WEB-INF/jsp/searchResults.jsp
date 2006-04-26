<div class="contentItem">

  <div class="title"><fmt:message key="search.results" /></div>
  <div class="subtitle">"${searchResults.query}"</div>

  <div class="contentItemBody">
    <c:choose>
      <c:when test="${searchResults.numberOfHits > 0}">

        <c:set var="pageableUrl" value="search.action?query=${query}&amp;sort=${param.sort}" scope="request" />
        <jsp:include page="/WEB-INF/fragments/pageable.jsp">
          <jsp:param name="url" value="${pageableUrl}" />
        </jsp:include>
        <br />

        <table width="99%" cellspacing="0" cellpadding="4">
          <c:forEach var="hit" items="${pageable.listForPage}" varStatus="status">
            <c:choose>
              <c:when test="${status.count % 2 == 0}">
                <tr class="even small">
              </c:when>
              <c:otherwise>
                  <tr class="odd small">
              </c:otherwise>
            </c:choose>
            <td width="2%" valign="top">
              <fmt:formatNumber value="${hit.number}"/>
            </td>
            <td valign="top">
              <a href="${hit.permalink}" title="${hit.score}">${hit.title}</a>
              <br />
              ${hit.excerpt}
            </td>
            <td align="right" valign="top" width="15%">
              <fmt:formatDate value="${hit.date}" type="date" dateStyle="medium" />
            </td>
          </tr>
          </c:forEach>
        </table>

        <br />
        <c:set var="pageableUrl" value="search.action?query=${query}&amp;sort=${param.sort}" scope="request" />
        <jsp:include page="/WEB-INF/fragments/pageable.jsp">
          <jsp:param name="url" value="${requestScope.pageableUrl}" />
        </jsp:include>

      </c:when>
      <c:otherwise>
          <fmt:message key="search.noResults" />
      </c:otherwise>
    </c:choose>

    <p>
      <a href="http://www.google.com/search?q=${searchResults.query}"><fmt:message key="search.googleIt" /></a>
    </p>
  </div>
</div>