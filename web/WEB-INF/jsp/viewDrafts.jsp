<div class="contentItem">
  <div class="title">Drafts</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form name="draftsForm" method="post" action="removeDrafts.secureaction">
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
          <a href="editDraft.secureaction?entry=${blogEntry.id}" title="Edit this draft">
            <c:choose>
              <c:when test="${!empty blogEntry.title}">${blogEntry.title}</c:when>
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
          <input type="button" value="Check All" onclick="checkAll(document.draftsForm.entry)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.draftsForm.entry)" />
        </td>
        <td align="right">
          <input type="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>
  </div>
</div>