<%--
  The sidebar that includes the admin links.
--%>
<div id="sidebar">

  <%@ include file="/WEB-INF/fragments/admin.jspf" %>

  <c:if test="${not empty blogs}">
  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span><fmt:message key="common.summaryOfBlogs"/></span></div>
    <c:forEach var="aBlog" items="${blogs}">
      <a href="${aBlog.url}">${aBlog.name}</a>
      <c:if test="${not empty aBlog.description}"> - ${aBlog.description}</c:if>
      <br />
    </c:forEach>
  </div>
  </c:if>

</div>