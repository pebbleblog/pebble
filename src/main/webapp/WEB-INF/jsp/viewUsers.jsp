<div class="contentItem">


  <div class="contentItemLinks">
    <a href="./help/managingUsers.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.users"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <form name="manageUsersForm" action="manageUsers.secureaction" method="post">
    <pebble:token/>
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <th><input type="checkbox" name="allUsers" onclick="toggleCheckAll(document.manageUsersForm.allUsers, document.manageUsersForm.user)"/></th>
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
        <td><a href="viewUser.secureaction?user=<c:out value="${user.username}" escapeXml="true"/>"><c:out value="${user.username}" escapeXml="true"/></a></td>
        <td><c:out value="${user.name}" escapeXml="true"/></td>
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
        <td align="right">
          <input type="submit" name="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>
