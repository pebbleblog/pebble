<div class="contentItem">

  <h1><fmt:message key="view.changePassword"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <c:if test="${not empty validationContext.errors}">
    <div class="validationError">
      <b>${validationContext.numberOfErrors} error(s)</b>
      <ul>
      <c:forEach var="error" items="${validationContext.errors}">
        <li>${error.message}</li>
      </c:forEach>
      </ul>
    </div>
    </c:if>

    <form name="user" action="changePassword.secureaction" method="post">
      <pebble:token/>

      <table width="99%" cellspacing="0" cellpadding="4">
        <tr>
          <td valign="top"><fmt:message key="view.changePassword.password"/></td>
          <td><input type="password" name="password1" size="40"/></td>
        </tr>
        <tr>
          <td valign="top"><fmt:message key="view.changePassword.confirm"/></td>
          <td><input type="password" name="password2" size="40"/></td>
        </tr>
        <tr>
          <td colspan="2" align="right">
            <button type="submit" name="submit" value="Change Password"><fmt:message key="admin.changePassword"/>"</button>
          </td>
        </tr>
      </table>
    </form>

  </div>

</div>