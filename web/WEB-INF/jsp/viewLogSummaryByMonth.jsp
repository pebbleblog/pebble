<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1>
  	<fmt:message key="view.logSummaryForPeriod">
  		<fmt:param><fmt:formatDate value="${logSummary.date}" pattern="MMMM" /></fmt:param>
  	</fmt:message>
	<a href="viewLogSummary.secureaction?year=<fmt:formatDate value="${logSummary.date}" pattern="yyyy" />"><fmt:formatDate value="${logSummary.date}" pattern="yyyy" /></a>

  </h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <p>
      The requests logged by Pebble (and shown here) are only those requests that result in physical page view
      (impression), a news feed or a file download. All other requests (e.g. CSS files, JavaScript files, images, etc)
      are not included in the totals.
    </p>

    <table width="99%" cellspacing="0" cellpadding="4">

      <thead>
      <tr>
        <th colspan="2">Total requests for <fmt:formatDate value="${logSummary.date}" pattern="MMMM yyyy" /></th>
        <th align="right"><fmt:formatNumber value="${logSummary.totalRequests}"/></th>
      </tr>
      </thead>

      <tbody>
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
            Requests -
            <a href="viewRequests.secureaction?year=${year}&month=${month}&day=${status.count}" title="See all requests for day">All</a> |
            <a href="viewRequestsByType.secureaction?year=${year}&month=${month}&day=${status.count}" title="See requests by type for day">By Type</a> |
            <a href="viewRequestsByHour.secureaction?year=${year}&month=${month}&day=${status.count}" title="See requests by hour for day">By Hour</a>
            <br />
            <a href="viewReferers.secureaction?year=${year}&month=${month}&day=${status.count}" title="See referers for day">Referers</a> |
            <a href="viewUserAgents.secureaction?year=${year}&month=${month}&day=${status.count}" title="See user agents for day">User Agents</a> |
            <a href="viewCountries.secureaction?year=${year}&month=${month}&day=${status.count}" title="See visitor countries for day">Countries</a>
            <br />
            Raw Log - <a href="viewLog.secureaction?year=${year}&month=${month}&day=${status.count}&flavor=text" title="See log file for day as plain text">Plain Text</a> | <a href="viewLog.secureaction?year=${year}&month=${month}&day=${status.count}&flavor=tab" title="See log file for day as a tab delimited file">Tab Delimited</a>
          </td>
          <td align="right"><fmt:formatNumber value="${logSummaryForDay.totalRequests}"/></td>
          </td>
        </tr>
      </c:forEach>
      </tbody>

    </table>
  </div>

</div>