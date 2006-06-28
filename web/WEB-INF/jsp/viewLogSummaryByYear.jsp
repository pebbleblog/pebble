<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <h1>Log summary for <fmt:formatDate value="${logSummary.date}" pattern="yyyy" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <td><b>Total for <fmt:formatDate value="${logSummary.date}" pattern="yyyy" /></b></td>
        <td align="right">&nbsp;</td>
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
            <fmt:formatDate value="${logSummaryForMonth.date}" type="date" pattern="MMMM yyyy"/>
          </td>
          <td align="right">
            <a href="viewLogSummary.secureaction?year=${year}&month=${status.count}" title="See log summary for month">Log summary</a> |
            <a href="viewReferers.secureaction?year=${year}&month=${status.count}" title="See referers for month">Referers</a> |
            <a href="viewRequests.secureaction?year=${year}&month=${status.count}" title="See requests for month">Requests</a>
          </td>
          <td align="right"><fmt:formatNumber value="${logSummaryForMonth.totalRequests}"/></td>
          </td>
        </tr>
      </c:forEach>

    </table>
  </div>

</div>