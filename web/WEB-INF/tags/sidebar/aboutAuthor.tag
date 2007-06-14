<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%--
 Displays the "about" information, as set on the blog properties page.
--%>
<c:if test="${displayMode == 'detail' and not empty blogEntry and not empty blogEntry.user.profile}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="common.aboutAuthor" /></span></div>
  <div class="sidebarItemBody">
    ${blogEntry.user.profile}
  </div>
</div>
</c:if>
