<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <h1>Statistics for <c:out value="${logPeriod}"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <h3>Summary</h3>
    <br />

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>Type</th>
          <th align="right">Total</th>
          <th align="right">Unique</th>
        </tr>
      </thead>
      <tbody>

        <tr class="odd">
          <td>
            All requests
          </td>
          <td align="right">
            <fmt:formatNumber value="${totalRequests}"/>
          </td>
          <td align="right">
            <fmt:formatNumber value="${uniqueIps}"/>
          </td>
        </tr>

        <tr class="even">
          <td>
            Newsfeed requests
          </td>
          <td align="right">
            <fmt:formatNumber value="${totalNewsfeedRequests}"/>
          </td>
          <td align="right">
            <fmt:formatNumber value="${uniqueIpsForNewsFeeds}"/>
          </td>
        </tr>

        <tr class="odd">
          <td>
            Page views<sup>*</sup>
          </td>
          <td align="right">
            <fmt:formatNumber value="${totalPageViews}"/>
          </td>
          <td align="right">
            <fmt:formatNumber value="${uniqueIpsForPageViews}"/>
          </td>
        </tr>

        <tr class="even">
          <td>
            File downloads
          </td>
          <td align="right">
            <fmt:formatNumber value="${totalFileDownloads}"/>
          </td>
          <td align="right">
            <fmt:formatNumber value="${uniqueIpsForFileDownloads}"/>
          </td>
        </tr>

      </tbody>
    </table>

    <p>
      <sup>*</sup> A page view is defined as being a view of a "content" page, such as : the home page, blog entry by month page, blog entry by day page, blog entry, static page, tag page, category page and search results. Other pages (e.g. 404 page, administration pages, comment forms, confirmation pages, etc) are not logged or included in the statistics.
    </p>

    <h3>Requests per Hour</h3>
    <br />

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>Hour of Day (${blog.timeZoneId})</th>
          <th align="right">Requests</th>
          <th align="right">Newsfeeds</th>
          <th align="right">Unique</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="count" begin="0" end="23">
          <c:choose>
            <c:when test="${count % 2 == 0}">
              <tr class="even small">
            </c:when>
            <c:otherwise>
                <tr class="odd small">
            </c:otherwise>
          </c:choose>
          <td><fmt:formatNumber value="${count}" pattern="00"/>:00</td>
          <td align="right">${requestsPerHour[count]}</td>
          <td align="right">${requestsForNewsFeedsPerHour[count]}</td>
          <td align="right">${uniqueIpsPerHour[count]}</td>
        </tr>
        </c:forEach>
    </tbody>
    </table>
  </div>

</div>