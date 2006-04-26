<div class="contentItem">
  <div class="title">Templates</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form name="templatesForm" method="post" action="removeTemplates.secureaction">
    <table width="99%" cellspacing="0" cellpadding="4">
      <c:forEach var="blogEntry" items="${blogEntries}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
        <td width="2%">
          <input type="checkbox" name="entry" value="${blogEntry.id}" />
        </td>
        <td>
          <a href="editTemplate.secureaction?entry=<c:out value="${blogEntry.id}"/>" title="Edit/use this blog entry template">
            <c:choose>
              <c:when test="${!empty blogEntry.title}"><c:out value="${blogEntry.title}" /></c:when>
              <c:otherwise>No Title</c:otherwise>
            </c:choose>
          </a>
        </td>
      </tr>
      </c:forEach>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.templatesForm.entry)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.templatesForm.entry)" />
        </td>
        <td align="right">
          <input type="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>