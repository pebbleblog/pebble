<%--
  The sidebar that includes the calendar, recent blog entries, links, etc.
--%>
<div id="sidebar">

  <%@ include file="/WEB-INF/fragments/sidebar/loginform.jspf" %>
  <%@ include file="/WEB-INF/fragments/sidebar/admin.jspf" %>

  <%@ include file="/WEB-INF/fragments/sidebar/navigation.jspf" %>
  <%@ include file="/WEB-INF/fragments/sidebar/archives-by-month.jspf" %>

  <%@ include file="/WEB-INF/fragments/sidebar/categories.jspf" %>
  <%@ include file="/WEB-INF/fragments/sidebar/tag-cloud.jspf" %>

  <%@ include file="/WEB-INF/fragments/sidebar/recent-blogentries.jspf" %>
  <%@ include file="/WEB-INF/fragments/sidebar/recent-responses.jspf" %>

  <%@ include file="/WEB-INF/fragments/sidebar/delicious.jspf" %>

  <jsp:include page="/WEB-INF/fragments/sidebar/feed.jsp">
    <jsp:param name="name" value="del.icio.us"/>
    <jsp:param name="url" value="http://localhost:8080/blog/rss.xml"/>
    <jsp:param name="maxEntries" value="10"/>
  </jsp:include>

  <jsp:include page="/WEB-INF/fragments/sidebar/feed.jsp">
    <jsp:param name="name" value="Recent Responses"/>
    <jsp:param name="url" value="http://localhost:8080/blog/responses/rss.xml"/>
    <jsp:param name="maxEntries" value="3"/>
  </jsp:include>

  <%-- @ include file="/WEB-INF/fragments/sidebar/twitter.jspf" --%>

</div>
