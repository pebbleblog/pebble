<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <div class="title">Log summary for <fmt:formatDate value="${logSummary.date}" pattern="MMMM yyyy" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <%--
    You can also see a <a href="viewLogSummary.secureaction?year=${year}" title="See statistics for this year">log summary for <fmt:formatDate value="${logSummary.date}" type="date" pattern="yyyy"/></a>.
    <br /><br />
    --%>
    <table width="99%" cellspacing="0" cellpadding="4">

      <thead>
      <tr>
        <td><b>Total for <fmt:formatDate value="${logSummary.date}" pattern="MMMM yyyy" /></b></td>
        <td align="right">
          <%--
          <a href="viewLog.secureaction?year=${year}&month=${month}" title="See log file for month">Log</a> |
          <a href="viewReferers.secureaction?year=${year}&month=${month}" title="See referers for month">Referers</a> |
          <a href="viewRequests.secureaction?year=${year}&month=${month}" title="See requests for month">Requests</a>
          --%>
        </td>
        <td align="right"><b><fmt:formatNumber value="${logSummary.totalRequests}"/></b></td>
      </tr>
      </thead>

      <c:forEach var="logSummaryForDay" items="${logSummary.logSummaries}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td>
            <fmt:formatDate value="${logSummaryForDay.date}" type="date" dateStyle="long"/>
          </td>
          <td align="right">
            <a href="viewLog.secureaction?year=${year}&month=${month}&day=${status.count}" title="See log file for day">Log</a> |
            <a href="viewReferers.secureaction?year=${year}&month=${month}&day=${status.count}" title="See referers for day">Referers</a> |
            <a href="viewRequests.secureaction?year=${year}&month=${month}&day=${status.count}" title="See requests for day">Requests</a>
          </td>
          <td align="right"><fmt:formatNumber value="${logSummaryForDay.totalRequests}"/></td>
          </td>
        </tr>
      </c:forEach>

    </table>
  </div>

</div>