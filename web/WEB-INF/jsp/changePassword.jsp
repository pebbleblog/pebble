<div class="contentItem">

  <h1>Change password</h1>
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

      <table width="99%" cellspacing="0" cellpadding="4">
        <tr>
          <td valign="top">Password</td>
          <td><input type="password" name="password1" size="40"/></td>
        </tr>
        <tr>
          <td valign="top">Confirm</td>
          <td><input type="password" name="password2" size="40"/></td>
        </tr>
        <tr>
          <td colspan="2" align="right">
            <input type="submit" name="submit" value="Change Password" />
          </td>
        </tr>
      </table>
    </form>

  </div>

</div>