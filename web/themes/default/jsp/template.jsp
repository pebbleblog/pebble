<%--
  the main template into which all other content is placed
--%>
<%@ include file="/WEB-INF/fragments/header.jspf" %>

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
  <fmt:message key="common.copyright">
    <fmt:param>${blog.author}</fmt:param>
  </fmt:message> |
  <fmt:message key="common.poweredBy">
    <fmt:param>
      <a href="http://pebble.sourceforge.net">Pebble ${pebbleContext.buildVersion}</a>
    </fmt:param>
  </fmt:message>
</div>

<%@ include file="/WEB-INF/fragments/footer.jspf" %>