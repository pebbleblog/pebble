<div class="contentItem">

  <h1><fmt:message key="view.userDetails"/></h1>
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

    <form name="user" action="saveUserDetails.secureaction" method="post">
      <pebble:token/>

      <table width="99%" cellspacing="0" cellpadding="4">
        <tr>
          <td valign="top">Username</td>
          <td>
            ${user.username}
          </td>
        </tr>
        <tr>
          <td valign="top">Name</td>
          <td><input type="text" name="name" value="${user.name}" size="40"/></td>
        </tr>
        <tr>
          <td valign="top">E-mail address</td>
          <td><input type="text" name="emailAddress" value="${user.emailAddress}" size="40"/></td>
        </tr>
        <tr>
          <td valign="top">Website</td>
          <td><input type="text" name="website" value="${user.website}" size="40"/></td>
        </tr>
        <tr>
          <td valign="top">Profile</td>
          <td><textarea name="profile" rows="8" cols="40">${user.profile}</textarea></td>
        </tr>
        <tr>
          <td valign="top">
            Roles
          </td>
          <td>
            <c:if test="${user.blogAdmin == true}">Blog admin<br /></c:if>
            <c:if test="${user.blogOwner == true}">Blog owner<br /></c:if>
            <c:if test="${user.blogPublisher == true}">Blog publisher<br /></c:if>
            <c:if test="${user.blogContributor == true}">Blog contributor<br /></c:if>
          </td>
        </tr>
        <tr>
          <td colspan="2" align="right">
            <input type="submit" value="Save" />
          </td>
        </tr>
      </table>
    </form>

  </div>

</div>