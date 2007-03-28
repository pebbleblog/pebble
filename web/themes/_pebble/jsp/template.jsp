<%--
  The main template into which all other content is placed. The following
  objects are available for use in templates.

   - blog
   - pebbleContext
   - categories
   - tags
   - recentBlogEntries
   - recentResponses
   - archives
   - pluginProperties
--%>
<%@ include file="/WEB-INF/fragments/header.jspf" %>

<div id="container">

  <%-- the header, containing blog name and description --%>
  <div id="header">
    <div id="blogName"><span>${blog.name}</span></div>
    <div id="blogDescription"><span>${blog.description}</span><a href="${blog.url}rss.xml"></a> <a href="${blog.url}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="RSS feed" border="0" valign="top" /></a></div>
  </div>

  <%@ include file="/WEB-INF/fragments/navigation.jspf" %>

  <%-- the sidebar that includes the calendar, recent blog entries, links, etc. --%>
  <div id="sidebar">
    <jsp:include page="/WEB-INF/fragments/sidebar/login-form.jsp" />
    <jsp:include page="/WEB-INF/fragments/sidebar/admin-panel.jsp" />

    <jsp:include page="/WEB-INF/fragments/sidebar/navigation.jsp" />
    <jsp:include page="/WEB-INF/fragments/sidebar/archives-by-month.jsp" />

    <jsp:include page="/WEB-INF/fragments/sidebar/categories.jsp" />

    <jsp:include page="/WEB-INF/fragments/sidebar/tag-cloud.jsp">
      <jsp:param name="threshold" value="1"/>
    </jsp:include>

    <jsp:include page="/WEB-INF/fragments/sidebar/recent-blogentries.jsp" />
    <jsp:include page="/WEB-INF/fragments/sidebar/recent-responses.jsp" />

    <%-- the following is an example of the feed component that
         lets you aggregate a RSS/RDF/Atom feed into your blog
    <jsp:include page="/WEB-INF/fragments/sidebar/feed.jsp">
      <jsp:param name="name" value="del.icio.us"/>
      <jsp:param name="url" value="http://del.icio.us/rss/simongbrown"/>
      <jsp:param name="maxEntries" value="3"/>
      <jsp:param name="truncateDescription" value="true"/>
      <jsp:param name="showDescription" value="true"/>
    </jsp:include>
    --%>
  </div>

  <%-- the main area into which content gets rendered --%>
  <div id="content">
    <jsp:include page="${content}"/>
  </div>

  <%-- the footer, containing the "powered by" link --%>
  <div id="footer">
    <fmt:message key="common.poweredBy">
      <fmt:param>
        <a href="http://pebble.sourceforge.net">Pebble ${pebbleContext.buildVersion}</a>
      </fmt:param>
    </fmt:message>
  </div>


</div>

<%@ include file="/WEB-INF/fragments/footer.jspf" %>