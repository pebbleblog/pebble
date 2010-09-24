<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/managingUsers.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.user"/></h1>
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

    <form name="user" action="saveUser.secureaction" method="post">
      <pebble:token/>
      <input type="hidden" name="preference.openids"
             value="<c:out value="${user.preferences['openids']}"/>"/>

      <table width="99%" cellspacing="0" cellpadding="4">
        <c:choose>
          <c:when test="${newUser}">
            <tr>
              <td valign="top">Username</td>
              <td>
                <input type="text" name="username" size="40" value="${user.username}"/>
                <input type="hidden" name="newUser" value="${newUser}"/>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <tr>
              <td valign="top">Username</td>
              <td>
                <input type="hidden" name="username" value="${user.username}"/>${user.username}
                <input type="hidden" name="newUser" value="false"/>
              </td>
            </tr>
          </c:otherwise>
        </c:choose>
        <tr>
          <td valign="top">Password</td>
          <td><input type="password" name="password1" value="" size="40"/></td>
        </tr>
        <tr>
          <td valign="top">Confirm</td>
          <td><input type="password" name="password2" value="" size="40"/><br />Leave the password fields blank to retain the current password.</td>
        </tr>
        <tr>
          <td colpsan="2">&nbsp;</td>
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
            <input type="checkbox" name="role" value="ROLE_BLOG_ADMIN"
                   <c:if test="${user.blogAdmin == true}">checked="true"</c:if>
            />&nbsp;Blog admin
            <br />
            <input type="checkbox" name="role" value="ROLE_BLOG_OWNER"
                   <c:if test="${user.blogOwner == true}">checked="true"</c:if>
            />&nbsp;Blog owner
            <br />
            <input type="checkbox" name="role" value="ROLE_BLOG_PUBLISHER"
                   <c:if test="${user.blogPublisher == true}">checked="true"</c:if>
            />&nbsp;Blog publisher
            <br />
            <input type="checkbox" name="role" value="ROLE_BLOG_CONTRIBUTOR"
                   <c:if test="${user.blogContributor == true}">checked="true"</c:if>
            />&nbsp;Blog contributor
          </td>
        </tr>
        <tr>
          <td valign="top">
            Details/password changeable
          </td>
          <td>
            <input type="checkbox" name="detailsUpdateable" value="true"
                   <c:if test="${user.detailsUpdateable == true}">checked="true"</c:if>
            />&nbsp;
          </td>
        </tr>
        <tr>
          <td valign="top">
            Rich text editor
          </td>
          <td>
            <input type="checkbox" name="preference.richTextEditorForBlogEntriesEnabled" value="true"
                   <c:if test="${user.preferences['richTextEditorForBlogEntriesEnabled'] == 'true'}">checked="true"</c:if>
            />&nbsp;Blog entries
            <input type="checkbox" name="preference.richTextEditorForStaticPagesEnabled" value="true"
                   <c:if test="${user.preferences['richTextEditorForStaticPagesEnabled'] == 'true'}">checked="true"</c:if>
            />&nbsp;Static pages
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