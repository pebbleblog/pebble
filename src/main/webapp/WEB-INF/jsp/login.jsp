<table width="99%" cellspacing="0" cellpadding="8">
  <tr>
    <td width="50%" align="right" valign="top" style="border-right: #c0c0c0 solid 1px">
      <h1><fmt:message key="login.title" /></h1>
    </td>
    <td width="50%" align="left">
      <c:if test="${param.error eq true}" ><div><fmt:message key="login.incorrect" /></div><br /></c:if>
      <form id="loginForm" name="loginForm" method="post" action="${pebbleContext.configuration.secureUrl}j_acegi_security_check">
        <input type="hidden" name="redirectUrl" value="${blog.relativeUrl}"/>
        <table>
          <tr><td><fmt:message key="login.username"/></td><td><input id="j_username" type="text" name="j_username" /></td></tr>
          <tr><td><fmt:message key="login.password"/></td><td><input type="password" name="j_password" /></td></tr>
          <tr><td><fmt:message key='login.rememberMe' /></td><td><input type="checkbox" name="_acegi_security_remember_me" /></td></tr>
          <tr><td colspan="2" align="right"><input type="submit" value="<fmt:message key='login.button' />" /></td></tr>
        </table>
      </form>
    </td>
  </tr>

</table>

<script type="text/javascript">
window.onload = function()
{
  document.loginForm.j_username.focus();
}
</script>
