<%--
  The main template into which all other content is placed. The following
  objects are available for use in templates.

   - blog
   - pebbleContext
   - tags
   - recentBlogEntries
   - recentResponses
--%>
<%@ include file="/WEB-INF/fragments/header.jspf" %>

<div id="subscriptions">
  <a href="${blog.url}rss.xml"><fmt:message key="newsfeed.rss" /></a> <a href="${blog.url}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="RSS feed" border="0" /></a> |
  <a href="${blog.url}atom.xml"><fmt:message key="newsfeed.atom" /></a> <a href="${blog.url}atom.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="Atom feed" border="0" /></a>
</div>

<%-- only show the search box on normal single-blog pages, not multi-blog pages --%>
<div id="search">
  <%@ include file="/WEB-INF/fragments/search.jspf" %>
</div>

<div id="header">
  <div id="blogName"><span>${blog.name}</span></div>
  <div id="blogDescription"><span>${blog.description}</span></div>
</div>

<%@ include file="/WEB-INF/fragments/navigation.jspf" %>

<%@ include file="sidebar.jsp" %>

<div id="content">
  <jsp:include page="${content}"/>
</div>

<div id="footer">
  <fmt:message key="common.poweredBy">
    <fmt:param>
      <a href="http://pebble.sourceforge.net">Pebble ${pebbleContext.buildVersion}</a>
    </fmt:param>
  </fmt:message>
</div>

<%@ include file="/WEB-INF/fragments/footer.jspf" %>