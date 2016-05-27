<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<%--
  Displays the search form.
--%>
<c:if test="${blogType == 'singleblog'}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="common.search" /></span></div>
  <div class="sidebarItemBody">
    <%@ include file="/WEB-INF/fragments/search.jspf" %>
    <c:if test="${not empty blog.rootCategory.subCategories}"><a title="Categories" href="./categories/"><fmt:message key="category.categories" /></a> |</c:if>
    <c:if test="${not empty tags}"><a title="Tags" href="./tags/"><fmt:message key="tag.tags" /></a> |</c:if>
    <a title="Advanced search" href="search.action"><fmt:message key="search.advancedSearch" /></a>
  </div>
</div>
</c:if>