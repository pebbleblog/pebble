<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<%--
  Displays the login form.
--%>

<pebble:isNotAuthenticated>
<c:if test="${empty isLoginPage}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key='login.login' /></span></div>
  <form id="loginForm" name="loginForm" method="post" action="${pebbleContext.configuration.secureUrl}j_acegi_security_check">
    <div class="sidebarItemBody">
    <input type="hidden" name="redirectUrl" value="${blog.relativeUrl}"/>
      <table>
        <tr>
          <td><fmt:message key="login.username"/></td>
          <td><input id="j_username" name="j_username" type="text" size="16" /></td>
        </tr>
        <tr>
          <td><fmt:message key="login.password"/></td>
          <td><input type="password" name="j_password" size="16" /></td>
        </tr>
        <tr>
          <td colspan="2" align="center">
            <fmt:message key='login.rememberMe' /><input type="checkbox" name="_acegi_security_remember_me" />
            <input type="submit" value="<fmt:message key='login.button' />" />
          </td>
        </tr>
      </table>
    </div>
  </form>
</div>

<script type="text/javascript">
//  $('loginForm').style.display = 'none';
</script>
</c:if>
</pebble:isNotAuthenticated>