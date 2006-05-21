<div class="contentItem">

  <div class="title">Categories</div>
  <div class="subtitle">&nbsp;</div>

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
        <td><a href="${aCategory.permalink}">${aCategory.name}</a></td>
        <td align="right" class="small">
          <a href="${aCategory.permalink}rss.xml"><fmt:message key="newsfeed.rss" /></a> <a href="${aCategory.permalink}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="RSS feed" border="0" /></a> |
          <a href="${aCategory.permalink}atom.xml"><fmt:message key="newsfeed.atom" /></a> <a href="${aCategory.permalink}atom.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="Atom feed" border="0" /></a>
        </td>
      </tr>
      </c:forEach>
    </table>
  </div>

</div>