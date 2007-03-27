<pebble:getFeed url="${param.url}"/>
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span>${param.name} <a href="${param.url}" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <ul>
    <c:forEach var="entry" items="${feedEntries}" end="${param.maxEntries-1}">
      <li><a href="${entry.url}">${entry.title}</a><br />${entry.truncatedBody}</li>
    </c:forEach>
    </ul>
  </div>
</div>