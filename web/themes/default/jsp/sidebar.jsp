<%--
  The sidebar that includes the calendar, recent blog entries, links, etc.
--%>
<div id="sidebar">

  <jsp:include page="/WEB-INF/fragments/sidebar/about.jsp"/>

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

  <%-- the following are examples of the feed component
  <jsp:include page="/WEB-INF/fragments/sidebar/feed.jsp">
    <jsp:param name="name" value="del.icio.us"/>
    <jsp:param name="url" value="http://del.icio.us/rss/simongbrown"/>
    <jsp:param name="maxEntries" value="3"/>
    <jsp:param name="truncateDescription" value="true"/>
    <jsp:param name="showDescription" value="true"/>
  </jsp:include>

  <jsp:include page="/WEB-INF/fragments/sidebar/feed.jsp">
    <jsp:param name="name" value="Twitter"/>
    <jsp:param name="url" value="http://twitter.com/statuses/user_timeline/1258081.rss"/>
    <jsp:param name="maxEntries" value="3"/>
    <jsp:param name="truncateDescription" value="true"/>
    <jsp:param name="showDescription" value="false"/>
  </jsp:include>
  --%>

</div>
