<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1>
	<fmt:message key="view.logSummaryForPeriod">
  		<fmt:param><fmt:formatDate value="${logSummary.date}" pattern="yyyy" /></fmt:param>
  	</fmt:message>
  </h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <td><b>Total requests for <fmt:formatDate value="${logSummary.date}" pattern="yyyy" /></b></td>
        <td align="right"><b><fmt:formatNumber value="${logSummary.totalRequests}"/></b></td>
      </tr>
      </thead>

      <c:forEach var="logSummaryForMonth" items="${logSummary.logSummaries}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td>
            <a href="viewLogSummary.secureaction?year=${year}&month=${status.count}" title="See log summary for month"><fmt:formatDate value="${logSummaryForMonth.date}" type="date" pattern="MMMM yyyy"/></a>
          </td>
          <td align="right"><fmt:formatNumber value="${logSummaryForMonth.totalRequests}"/></td>
          </td>
        </tr>
      </c:forEach>

    </table>
  </div>

</div>