<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="sidebar.navigate" /></span></div>
  <div class="sidebarItemBody">
    <pebble:calendar/>
    <br />
    <%@ include file="/WEB-INF/fragments/search.jspf" %>
    <a href="./categories/"><fmt:message key="category.categories" /></a> |
    <a href="./tags/"><fmt:message key="tag.tags" /></a> |
    <a href="search.action"><fmt:message key="search.advancedSearch" /></a>
  </div>
</div>