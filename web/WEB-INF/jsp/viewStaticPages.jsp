<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/staticPages.html" target="_blank">Help</a>
  </div>

  <h1>Static pages</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="staticPagesForm" method="post" action="removeStaticPages.secureaction">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <th><input type="checkbox" name="allPages" onclick="toggleCheckAll(document.staticPagesForm.allPages, document.staticPagesForm.page)"/></th>
        <th>Title</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="staticPage" items="${staticPages}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
        <td width="2%">
          <input type="checkbox" name="page" value="${staticPage.id}" />
        </td>
        <td>
          <a href="editStaticPage.secureaction?page=${staticPage.id}#form">${staticPage.title}</a>
        </td>
      </tr>
      </c:forEach>
      </tbody>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
          <input type="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>