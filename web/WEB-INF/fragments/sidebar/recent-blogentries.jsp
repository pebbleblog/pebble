<c:if test="${not empty recentBlogEntries}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="sidebar.recentBlogEntries" /> <a href="rss.xml"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <ul>
    <c:forEach var="recentBlogEntry" items="${recentBlogEntries}" >
      <li><a href="${recentBlogEntry.permalink}" title="${recentBlogEntry.permalink}">${recentBlogEntry.title}</a><br />${recentBlogEntry.truncatedContent}</li>
    </c:forEach>
    </ul>
  </div>
</div>
</c:if>