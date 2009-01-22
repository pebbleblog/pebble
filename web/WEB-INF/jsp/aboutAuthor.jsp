<div class="contentItem">

  <h1>${user.name}</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    ${user.profile}

    <c:if test="${not empty blogEntries}">
    <br /><br />
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th colspan="3"><fmt:message key="sidebar.recentBlogEntries" /> <a href="authors/${user.username}/rss.xml"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></th>
        </tr>
      </thead>
      <tbody>
      <c:forEach var="blogEntry" items="${blogEntries}" varStatus="status">
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
          <a href="${url:rewrite(blogEntry.permalink)}" title="${blogEntry.title}">${blogEntry.title}</a>
          <br />
          ${blogEntry.truncatedContent}
        </td>
        <td align="right" valign="top" width="15%">
          <fmt:formatDate value="${blogEntry.date}" type="date" dateStyle="medium" />
        </td>
      </tr>
      </c:forEach>
      </tbody>
    </table>
    </c:if>

  </div>

</div>

