<div class="contentItem">


  <div class="contentItemLinks">
    <a href="./help/managingUsers.html" target="_blank">Help</a>
  </div>

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
          <input type="submit" name="submit" value="Reset Password" />
          <input type="submit" name="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>