<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
 Generic sidebar item component.

 Parameters
  - name : the name, displayed in the sidebar item title
--%>
<%@ attribute name="name"%>

<div class="sidebarItem">
  <div class="sidebarItemTitle"><span>${name}</span></div>
  <div class="sidebarItemBody">
    <jsp:doBody/>
  </div>
</div>
