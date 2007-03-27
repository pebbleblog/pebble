<c:catch var="e">
<pebble:getFeed url="${param.url}"/>
  <c:if test="${not empty feedEntries}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span>${param.name} <a href="${param.url}" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <ul>
      <c:forEach var="entry" items="${feedEntries}" end="${param.maxEntries-1}">
        <li>
          <a href="${entry.url}">${entry.title}</a>
          <c:if test="${param.showDescription eq 'true'}">
          <br />
          <c:choose>
            <c:when test="${param.truncateDescription eq 'false'}">${entry.body}</c:when>
            <c:otherwise>${entry.truncatedBody}</c:otherwise>
          </c:choose>
          </c:if>
        </li>
      </c:forEach>
    </ul>
  </div>
</div>
</c:if>
</c:catch>
<c:if test="${not empty e}">Exception : ${e}</c:if>
