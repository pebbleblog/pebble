<c:if test="${not empty blog.about}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="common.about" /></span></div>
  <div class="sidebarItemBody">
    ${blog.about}
  </div>
</div>
</c:if>
