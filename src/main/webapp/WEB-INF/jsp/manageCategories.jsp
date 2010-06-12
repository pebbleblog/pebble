<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/categories.html" target="_blank">Help</a>
  </div>

  <h1>Categories</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="categoriesForm" method="post" action="removeCategories.secureaction">
    <pebble:token/>
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <th><input type="checkbox" name="allCategories" onclick="toggleCheckAll(document.categoriesForm.allCategories, document.categoriesForm.category)"/></th>
        <th>Name</th>
        <th>ID</th>
        <th>Tags</th>
        <th align="right">Blog Entries</th>
      </tr>
      </thead>
      <tbody>
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
      </tbody>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
          <button type="submit" value="Remove" ><fmt:message key="common.remove"/></button>
        </td>
      </tr>
    </table>
    </form>

    <c:if test="${not empty category}">
    <br />
    <c:choose>
      <c:when test="${empty category.id}"><h3>Add a category</h3></c:when>
      <c:otherwise><h3>Edit a category</h3></c:otherwise>
    </c:choose>
    <br />
    <a name="form"></a>
    <form name="editCategoryForm" action="saveCategory.secureaction" method="post" accept-charset="<c:out value="${blog.characterEncoding}" />">
      <pebble:token/>
      <table width="99%">
        <tr>
          <td valign="top">ID</td>
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
          <td valign="top">Name</td>
          <td valign="top">
            <input type="text" name="name" size="40" value="${category.name}" />
          </td>
        </tr>
        <tr>
          <td valign="top">Tags</td>
          <td valign="top">
            <input type="text" name="tags" size="40" value="${category.tags}" />
          </td>
        </tr>
        <tr>
          <td colspan="2" align="right">
            <button name="submit" type="submit" Value="Save Category"><fmt:message key="common.save"/></button>
          </td>
        </tr>
      </table>
    </form>
    </c:if>
  </div>

</div>