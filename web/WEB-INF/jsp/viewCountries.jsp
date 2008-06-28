<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1>
  	<fmt:message key="view.countriesForPeriod">
  		<fmt:param>${logPeriod}</fmt:param>
  	</fmt:message>
  </h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <h3>Summary</h3>
    <br />

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>Countries</th>
          <th align="right">News Feeds</th>
          <th align="right">Page Views</th>
          <th align="right">File Downloads</th>
          <th align="right">Total Requests</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="country" items="${countries}" varStatus="status">
          <c:choose>
            <c:when test="${status.count % 2 == 0}">
              <tr class="even small">
            </c:when>
            <c:otherwise>
                <tr class="odd small">
            </c:otherwise>
          </c:choose>
          <td>${country}</td>
          <td align="right"><fmt:formatNumber value="${countriesForNewsFeeds[country]}"/></td>
          <td align="right"><fmt:formatNumber value="${countriesForPageViews[country]}"/></td>
          <td align="right"><fmt:formatNumber value="${countriesForFileDownloads[country]}"/></td>
          <td align="right"><fmt:formatNumber value="${consolidatedCountries[country]}"/></td>
        </tr>
        </c:forEach>
    </tbody>
    </table>

  </div>

</div>