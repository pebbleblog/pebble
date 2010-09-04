<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

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
