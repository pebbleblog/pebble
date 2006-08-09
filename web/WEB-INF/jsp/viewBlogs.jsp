<div class="contentItem">

  <h1>Blogs</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <th>ID</th>
        <th>Name</th>
      </thead>
      <tbody>
      <c:forEach var="aBlog" items="${blogs}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even">
          </c:when>
          <c:otherwise>
              <tr class="odd">
          </c:otherwise>
        </c:choose>
        <td>${aBlog.id}</td>
        <td><a href="${aBlog.url}" target="_blank">${aBlog.name}</a></td>
      </tr>
      </c:forEach>
      </tbody>
    </table>

    <p>
      To add a new blog, enter the name that will be used to identify this blog underneath this web application (must contain a-zA-Z_0-9 characters only).
    </p>

    <p>
    <form name="addBlog" action="addBlog.secureaction" method="POST">
    ${blog.url}
    <input name="id" type="text" value="" />
    <input type="submit" value="Add Blog" />
    </form>
    </p>

  </div>

</div>