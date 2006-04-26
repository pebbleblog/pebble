<%--
  the sidebar that includes the calendar, recent blog entries, links, etc
--%>
<div id="sidebar">

  <%@ include file="/WEB-INF/fragments/admin.jspf" %>

  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span>Subscribe</span></div>
    <br />
    <a href="rss.xml"><fmt:message key="newsfeed.rss" /></a> <a href="rss.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="RSS feed" border="0" /></a> |
    <a href="atom.xml"><fmt:message key="newsfeed.atom" /></a> <a href="atom.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="Atom feed" border="0" /></a>
    <br /><br />
  </div>

</div>