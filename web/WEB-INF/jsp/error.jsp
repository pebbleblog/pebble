<%@ page isErrorPage="true" %>

<table width="99%" cellspacing="0" cellpadding="8">
  <tr>
    <td width="50%" align="right" valign="top" style="border-right: #c0c0c0 solid 1px">
      <h1><fmt:message key="error.errorTitle" /></h1>
    </td>
    <td width="50%" align="left">
      <fmt:message key="error.error" />
      <br /><br />

      <pebble:isAuthenticated>
      <p>
        If you were editing your theme and have caused this error, click <a href="resetTheme.secureaction">here</a>
        to switch back to the default theme.
      </p>
      </pebble:isAuthenticated>
    </td>
  </tr>

</table>