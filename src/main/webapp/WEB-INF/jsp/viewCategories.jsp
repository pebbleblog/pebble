<div class="contentItem">

  <h1><fmt:message key="category.categories" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <c:forEach var="aCategory" items="${categories}" varStatus="status" begin="1">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even">
          </c:when>
          <c:otherwise>
              <tr class="odd">
          </c:otherwise>
        </c:choose>
        <td><a href="${url:rewrite(aCategory.permalink)}">${aCategory.name}</a> (<fmt:formatNumber value="${aCategory.numberOfBlogEntries}" type="number" />)</td>
        <td align="right" class="small">
          <a href="${url:rewrite(aCategory.permalink)}rss.xml"><fmt:message key="newsfeed.rss" /></a> <a href="${url:rewrite(aCategory.permalink)}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="RSS feed" border="0" /></a>
        </td>
      </tr>
      </c:forEach>
    </table>
  </div>

</div>