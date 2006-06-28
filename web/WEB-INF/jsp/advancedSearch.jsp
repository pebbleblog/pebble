<div class="contentItem">

  <h1><fmt:message key="search.advancedSearch" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form method="post" action="advancedSearch.action" accept-charset="${blog.characterEncoding}">
    <table width="99%" cellpadding="4" cellspacing="0">

      <tr>
      <td valign="top"><b><fmt:message key="blogentry.title" /></b></td>
      <td><input type="text" name="title" size="60" /></td>
      </tr>

      <tr>
      <td valign="top"><b><fmt:message key="blogentry.body" /></b></td>
      <td><input type="text" name="body" size="60" /></td>
      </tr>

      <tr>
      <td valign="top"><b><fmt:message key="blogentry.author" /></b></td>
      <td><input type="text" name="author" size="30" /></td>
      </tr>

      <c:if test="${not empty blog.rootCategory.subCategories}">
      <tr>
        <td valign="top"><b><fmt:message key="blogentry.categories" /></b></td>
        <td>
          <table width="99%" cellpadding="0" cellspacing="0">
            <c:forEach var="category" items="${blog.categories}" varStatus="status" begin="1">
            <c:if test="${status.count % 2 == 1}">
            <tr>
            </c:if>
              <td>
              <input type="checkbox" name="category" value="${category.id}" />&nbsp;<c:out value="${category.name}" escapeXml="true"/>
              </td>
            <c:if test="${status.count % 2 == 0}">
            </tr>
            </c:if>
            </c:forEach>
          </table>
        </td>
      </tr>
      </c:if>

      <tr>
      <td valign="top"><b><fmt:message key="blogentry.tags" /></b></td>
      <td>
        <input type="text" name="tags" size="60" />
      </td>
      </tr>

      <tr>
      <td colspan="2" align="right">
      <input type="submit" value="<fmt:message key="common.search" />" />
      </td>
      </tr>
    </table>
    </form>
  </div>
</div>