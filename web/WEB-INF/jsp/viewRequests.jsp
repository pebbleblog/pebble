<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1>
  	<fmt:message key="view.requestsForPeriod">
	  <fmt:param>${logPeriod}</fmt:param>
	</fmt:message>
  </h1>
  <h2>Total : <fmt:formatNumber value="${totalRequests}"/></h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>
            Resource
            <c:choose>
              <c:when test="${param.sort eq 'name'}">(<a href="viewRequests.secureaction?year=<fmt:formatNumber value="${param.year}" pattern="#"/>&month=<fmt:formatNumber value="${param.month}"/>&day=<fmt:formatNumber value="${param.day}"/>">sort by count</a>)</c:when>
              <c:otherwise>(<a href="viewRequests.secureaction?year=<fmt:formatNumber value="${param.year}" pattern="#"/>&month=<fmt:formatNumber value="${param.month}"/>&day=<fmt:formatNumber value="${param.day}"/>&sort=name">sort by name</a>)</c:otherwise>
            </c:choose>
          </th>
          <th align="right">Count</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach var="aRequest" items="${requests}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td>
            <a href="<c:out value=".${aRequest.url}"/>" title="<c:out value="${aRequest.url}"/>"><c:out value="${aRequest.truncatedName}"/></a>
          </td>
          <td align="right">
            <fmt:formatNumber value="${aRequest.count}"/>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

</div>