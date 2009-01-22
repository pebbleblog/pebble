<div class="contentItem">

  <h1>Community<%--<fmt:message key="community.title" />--%></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

  <c:choose>
    <c:when test="${empty entries}">
        <fmt:message key="blogentry.noBlogEntries" />
    </c:when>

    <c:otherwise>

      <table width="99%" cellspacing="0" cellpadding="4">
        <thead>
          <tr>
            <th></th>
            <th><fmt:message key="search.header.titleAndSummary" /></th>
            <th align="right"><fmt:message key="search.header.dateTime" /></th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${entries}" varStatus="status">
          <c:choose>
            <c:when test="${status.count % 2 == 0}">
              <tr class="even small">
            </c:when>
            <c:otherwise>
                <tr class="odd small">
            </c:otherwise>
          </c:choose>
          <td width="2%" valign="top">
            <fmt:formatNumber value="${status.count}"/>
            <br />
          </td>
          <td valign="top">
            <a href="${url:rewrite(entry.link)}">${entry.title}</a> - <a href="${url:rewrite(entry.feed.link)}">${entry.feed.title}</a> <a href="${url:rewrite(entry.feed.url)}"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a>
            <br />
            ${entry.truncatedBody}
          </td>
          <td align="right" valign="top" width="15%">
            <fmt:formatDate value="${entry.date}" type="date" dateStyle="medium" /><br />
            <fmt:formatDate value="${entry.date}" type="time" dateStyle="medium" />
          </td>
        </tr>
        </c:forEach>
        </tbody>
      </table>

    </c:otherwise>
  </c:choose>

  </div>
</div>