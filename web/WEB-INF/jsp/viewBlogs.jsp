<div class="contentItem">

  <h1>Blogs</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <c:forEach var="aBlog" items="${blogs}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even">
          </c:when>
          <c:otherwise>
              <tr class="odd">
          </c:otherwise>
        </c:choose>
        <td><a href="<c:out value="${aBlog.url}"/>" target="_blank">${pageContext.request.contextPath}/<c:out value="${aBlog.id}"/></a></td>
      </tr>
      </c:forEach>
    </table>

    <p>
      To add a new blog, enter the name that will be used to identify this blog underneath this web application (must contain a-zA-Z_0-9 characters only).
    </p>

    <p>
    <form name="addBlog" action="addBlog.secureaction" method="POST">
    <b>${pageContext.request.contextPath}/</b>
    <input name="id" type="text" value="" />
    <input type="submit" value="Add Blog" />
    </form>
    </p>

  </div>

</div>