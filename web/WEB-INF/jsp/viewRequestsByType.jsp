<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <h1>Requests by Type for <c:out value="${logPeriod}"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

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
            Page views
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
  </div>

</div>