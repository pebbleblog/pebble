<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>

<%--
  In multi-blog mode, displays a list of all blogs along with a link back to the multi-blog home page.
--%>
<c:if test="${not empty blogs}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="common.summaryOfBlogs"/> <a title="RSS feed for multiblog ${multiBlog.name}" href="${multiBlog.url}rss.xml"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <c:if test="${blogType == 'singleblog'}"><a href="${multiBlog.url}" title="URL for multiblog ${multiBlog.name}">${multiBlog.name}</a> <a title="RSS feed for multiblog ${multiBlog.name}" href="${multiBlog.url}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a><br /></c:if>
    <c:forEach var="aBlog" items="${blogs}">
      <a href="${url:rewrite(aBlog.url)}" title="URL for blog ${aBlog.name}">${aBlog.name}</a>
      <c:if test="${not empty aBlog.description}"> - ${aBlog.description}</c:if>
      <a href="${url:rewrite(aBlog.url)}rss.xml" title="RSS feed for blog ${aBlog.name}" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a>
      <br />
    </c:forEach>
  </div>
</div>
</c:if>