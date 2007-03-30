<%--
 Reads the specified feed and formats it for the sidebar. Each entry is rendered as (approximately) :

  <a href="entryLink">entryTitle</a><br />
  entryBody

 Parameters
  - name : the name of the feed, which is displayed in the sidebar item title
  - url : the URL of the RSS/RDF/Atom feed
  - maxEntries : the maximum number of entries to display from this feed
  - showBody : flag to indicate whether the body of the entry should be displayed
  - truncateBody : flag to indicate whether the body of the entry should be truncated (i.e. HTML stripped and truncated to 255 characters max)
--%>
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
          <c:if test="${param.showBody eq 'true'}">
          <br />
          <c:choose>
            <c:when test="${param.truncateBody eq 'false'}">${entry.body}</c:when>
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
<c:if test="${not empty e}"><!-- Exception with feed URL ${param.url} : ${e} --></c:if>
