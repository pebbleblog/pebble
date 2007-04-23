<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%--
  In multi-blog mode, displays a list of all blogs along with a link back to the multi-blog home page.
--%>
<c:if test="${not empty blogs}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="common.summaryOfBlogs"/> <a href="${multiBlog.url}rss.xml"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <c:if test="${blogType == 'singleblog'}"><a href="${multiBlog.url}">${multiBlog.name}</a> <a href="${multiBlog.url}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a><br /></c:if>
    <c:forEach var="aBlog" items="${blogs}">
      <a href="${aBlog.url}">${aBlog.name}</a>
      <c:if test="${not empty aBlog.description}"> - ${aBlog.description}</c:if>
      <a href="${aBlog.url}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a>
      <br />
    </c:forEach>
  </div>
</div>
</c:if>