<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
 Displays the "about" information, as set on the blog properties page.
--%>
<c:if test="${blogType == 'singleblog'}">
<c:if test="${displayMode == 'detail' and not empty blogEntry and not empty blogEntry.user.profile}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="common.aboutAuthor" /> <a href="authors/${blogEntry.user.username}/rss.xml" title="RSS feed for ${blogEntry.user.username}"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    ${blogEntry.user.profile}
    <br /><br />
    <a href="authors/${blogEntry.user.username}/" title="About ${blogEntry.user.username}"><fmt:message key="common.readMore" /></a>
  </div>
</div>
</c:if>
</c:if>
