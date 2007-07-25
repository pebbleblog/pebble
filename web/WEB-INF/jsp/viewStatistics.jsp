<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <h1>Statistics for <c:out value="${logPeriod}"/></h1>
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
  </div>

</div>