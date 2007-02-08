<c:if test="${not empty blogs}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="common.summaryOfBlogs"/></span></div>
  <div class="sidebarItemBody">
    <c:forEach var="aBlog" items="${blogs}">
      <a href="${aBlog.url}">${aBlog.name}</a>
      <c:if test="${not empty aBlog.description}"> - ${aBlog.description}</c:if>
      <br />
    </c:forEach>
  </div>
</div>
</c:if>