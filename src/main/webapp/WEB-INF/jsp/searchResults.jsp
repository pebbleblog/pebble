<div class="contentItem">

  <h1><fmt:message key="search.results" /></h1>
  <h2>"${searchResults.query}"</h2>

  <div class="contentItemBody">
    <c:choose>
      <c:when test="${searchResults.numberOfHits > 0}">

        <c:set var="pageableUrl" value="search.action?query=${query}&amp;sort=${param.sort}" scope="request" />
        <jsp:include page="/WEB-INF/fragments/pageable.jsp">
          <jsp:param name="url" value="${pageableUrl}" />
        </jsp:include>
        <br />

        <table class="searchResults" width="99%" cellspacing="0" cellpadding="4">
          <thead>
            <tr>
              <th></th>
              <th><fmt:message key="search.header.titleAndSummary" /></th>
              <th align="right"><fmt:message key="search.header.dateTime" /></th>
            </tr>
          </thead>
          <tbody>
          <c:forEach var="hit" items="${pageable.listForPage}" varStatus="status">
            <c:choose>
              <c:when test="${status.count % 2 == 0}">
                <tr class="even small">
              </c:when>
              <c:otherwise>
                  <tr class="odd small">
              </c:otherwise>
            </c:choose>
            <td class="searchResultNumber" width="2%" valign="top">
              <fmt:formatNumber value="${hit.number}"/>
              <br />
            </td>
            <td valign="top">
              <div class="searchResultTitle"><a href="${url:rewrite(hit.permalink)}" title="${hit.score}">${hit.title}</a></div>
              <c:if test="${not empty hit.subtitle}"><div class="searchResultSubTitle"><a href="${url:rewrite(hit.permalink)}" title="${hit.score}">${hit.subtitle}</a></div></c:if>
              <div class="searchResultExcerpt">${hit.excerpt}</div>
            </td>
            <td class="searchResultDatetime" align="right" valign="top" width="15%">
              <div><fmt:formatDate value="${hit.date}" type="date" dateStyle="medium" /></div>
              <div><fmt:formatDate value="${hit.date}" type="time" dateStyle="medium" /></div>
            </td>
          </tr>
          </c:forEach>
          </tbody>
        </table>

        <br />
        <c:set var="pageableUrl" value="search.action?query=${query}&amp;sort=${param.sort}" scope="request" />
        <jsp:include page="/WEB-INF/fragments/pageable.jsp">
          <jsp:param name="url" value="${requestScope.pageableUrl}" />
        </jsp:include>

      </c:when>
      <c:otherwise>
          <div class="noResults"><fmt:message key="search.noResults" /></div>
      </c:otherwise>
    </c:choose>
  </div>
</div>