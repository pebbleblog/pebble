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
  <%@ include file="/WEB-INF/fragments/sidebar/twitter.jspf" %>

</div>
