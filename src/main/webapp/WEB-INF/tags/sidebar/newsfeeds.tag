<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>

<%--
  Displays the recent entries from the community.
--%>
<%@ attribute name="name"%>

<c:if test="${not empty blog.recentNewsFeedEntries}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span>${name}</span></div>
  <div class="sidebarItemBody">
    <c:forEach var="entry" items="${blog.recentNewsFeedEntries}" >
      <a title="URL for recent news entry ${entry.title}" href="${url:rewrite(entry.link)}">${entry.title}</a> - <a title="RSS feed for recent news entry ${entry.feed.title}" href="${url:rewrite(entry.feed.link)}">${entry.feed.title}</a> <a title="RSS feed for recent news entry ${entry.feed.title}" href="${entry.feed.url}"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a>
      <br />${entry.truncatedBody}
      <br /><br />
    </c:forEach>
  </div>
</div>
</c:if>