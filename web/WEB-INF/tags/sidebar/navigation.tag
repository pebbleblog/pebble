<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<%--
  Displays the basic navigation mechanisms; including the calendar, search and links to category/tag pages.
--%>
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