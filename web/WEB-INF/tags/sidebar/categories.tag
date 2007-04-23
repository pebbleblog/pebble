<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<%--
  Displays a list of category names and links, including the blog entry count for each.
--%>
<c:if test="${not empty categories[0].subCategories}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="category.categories" /></span></div>
  <div class="sidebarItemBody">
    <c:forEach var="category" items="${categories}" begin="1" varStatus="status">
      <a href="${category.permalink}"><c:out value="${category.name}" escapeXml="true"/></a> (<fmt:formatNumber value="${category.numberOfBlogEntries}" type="number" />)
      <a href="${category.permalink}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a>
      <br />
    </c:forEach>
  </div>
</div>
</c:if>