<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<%--
  Displays the basic navigation mechanisms; including the calendar, search and links to category/tag pages.
--%>
<c:if test="${blogType == 'singleblog'}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="sidebar.navigate" /></span></div>
  <div class="sidebarItemBody">
    <pebble:calendar/>
  </div>
</div>
</c:if>