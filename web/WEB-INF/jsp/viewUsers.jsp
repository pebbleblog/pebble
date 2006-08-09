<div class="contentItem">

  <h1>Users</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <form name="manageUsersForm" action="manageUsers.secureaction" method="post">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <th></th>
        <th>Username</th>
        <th>Name</th>
        <th>Admin</th>
        <th>Owner</th>
        <th>Publisher</th>
        <th>Contributor</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="user" items="${users}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even">
          </c:when>
          <c:otherwise>
              <tr class="odd">
          </c:otherwise>
        </c:choose>

        <td valign="top" class="small">
          <input type="checkbox" name="user" value="${user.username}" />
        </td>
        <td><a href="viewUser.secureaction?user=${user.username}">${user.username}</a></td>
        <td>${user.name}</td>
        <td align="center"><c:if test="${user.blogAdmin}">X</c:if></td>
        <td align="center"><c:if test="${user.blogOwner}">X</c:if></td>
        <td align="center"><c:if test="${user.blogPublisher}">X</c:if></td>
        <td align="center"><c:if test="${user.blogContributor}">X</c:if></td>
      </tr>
      </c:forEach>
      </tbody>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.manageUsersForm.user)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.manageUsersForm.user)" />
        </td>
        <td align="right">
          <input type="submit" name="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>

  <%--
    <form name="addUser" action="addUser.secureaction" method="post">

      <table width="99%" cellspacing="0" cellpadding="4">
        <tr>
          <td valign="top"><b>Username</b></td>
          <td><input type="text" name="username" value="${newUser.username}"></td>
        </tr>
        <tr>
          <td valign="top"><b>Password</b></td>
          <td><input type="password" name="password1"></td>
        </tr>
        <tr>
          <td valign="top"><b>Confirm</b></td>
          <td><input type="password" name="password2"></td>
        </tr>
        <tr>
          <td valign="top"><b>Name</b></td>
          <td><input type="text" name="name" value="${newUser.name}"></td>
        </tr>
        <tr>
          <td valign="top"><b>E-mail address</b></td>
          <td><input type="text" name="username" value="${newUser.emailAddress}"></td>
        </tr>
        <tr>
          <td valign="top"><b>Website</b></td>
          <td><input type="text" name="username" value="${newUser.website}"></td>
        </tr>
        <tr>
          <td colspan="2" align="right">
            <input type="submit" value="Add User" />
          </td>
        </tr>
      </table>
    </form>
    --%>
  </div>

</div>