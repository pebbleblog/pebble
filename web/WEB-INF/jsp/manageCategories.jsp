<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${pageContext.request.contextPath}/docs/categories.html" target="_blank">Help</a>
  </div>

  <div class="title">Categories</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form name="categoriesForm" method="post" action="removeCategories.secureaction">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <td>&nbsp;</td>
        <td><b>Name</b></td>
        <td><b>ID</b></td>
        <td><b>Tags</b></td>
        <td align="right"><b>Blog Entries</b></td>
      </tr>
      </thead>
      <c:forEach var="aCategory" items="${categories}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
        <td width="2%">
          <c:if test="${not aCategory.rootCategory}">
          <input type="checkbox" name="category" value="${aCategory.id}" />
          </c:if>
        </td>
        <td><a href="editCategory.secureaction?id=${aCategory.id}" title="Edit this category">${aCategory.name}</a></td>
        <td>${aCategory.id}</td>
        <td>${aCategory.tags}</td>
        <td align="right"><fmt:formatNumber value="${aCategory.numberOfBlogEntries}"/></td>
      </tr>
      </c:forEach>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.categoriesForm.category)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.categoriesForm.category)" />
        </td>
        <td align="right">
          <input type="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>

    <c:if test="${not empty category}">
    <br />

    <a name="form"></a>
    <form name="editCategoryForm" action="saveCategory.secureaction" method="post" accept-charset="<c:out value="${blog.characterEncoding}" />">
      <table width="99%">
        <tr>
          <td valign="top"><b>ID</b></td>
          <td valign="top">
            <c:choose>
              <c:when test="${empty category.id}">
                <input type="text" name="id" size="40" value="" />
              </c:when>
              <c:otherwise>
                <input type="hidden" name="id" size="40" value="${category.id}" />${category.id}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr>
          <td valign="top"><b>Name</b></td>
          <td valign="top">
            <input type="text" name="name" size="40" value="${category.name}" />
          </td>
        </tr>
        <tr>
          <td valign="top"><b>Tags</b></td>
          <td valign="top">
            <input type="text" name="tags" size="40" value="${category.tags}" />
          </td>
        </tr>
        <tr>
          <td colspan="2" align="right">
            <input name="submit" type="submit" Value="Save Category" />
          </td>
        </tr>
      </table>
    </form>
    </c:if>
  </div>

</div>