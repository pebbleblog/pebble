<div class="contentItem">

  <h1><fmt:message key="login.title" /></h1>
  <h2><c:choose><c:when test="${param.error eq true}" ><fmt:message key="login.incorrect" /></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></h2>

  <div class="contentItemBody">
    <form id="loginForm" name="loginForm" method="post" action="${pebbleContext.configuration.secureUrl}j_acegi_security_check">
      <input type="hidden" name="redirectUrl" value="${blog.relativeUrl}"/>
      <table>
        <tr><td>Username</td><td><input id="j_username" type="text" name="j_username" /></td></tr>
        <tr><td>Password</td><td><input type="password" name="j_password" /></td></tr>
        <tr><td><fmt:message key='login.rememberMe' /></td><td><input type="checkbox" name="_acegi_security_remember_me" /></td></tr>
        <tr><td colspan="2" align="right"><input type="submit" value="<fmt:message key='login.button' />" /></td></tr>
      </table>
    </form>
  </div>

</div>

<script type="text/javascript">
window.onload = function()
{
  document.loginForm.j_username.focus();
}
</script>
