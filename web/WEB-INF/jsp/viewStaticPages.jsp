<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/static-pages.html" target="_blank">Help</a>
  </div>

  <div class="title">Static pages</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form name="staticPagesForm" method="post" action="removeStaticPages.secureaction">
    <table width="99%" cellspacing="0" cellpadding="4">
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
          <input type="checkbox" name="entry" value="${staticPage.id}" />
        </td>
        <td>
          <a href="editStaticPage.secureaction?entry=${staticPage.id}#form">${staticPage.title}</a>
        </td>
        <td align="right">
        </td>
      </tr>
      </c:forEach>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.staticPagesForm.entry)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.staticPagesForm.entry)" />
        </td>
        <td align="right">
          <input type="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>