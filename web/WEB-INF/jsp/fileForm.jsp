<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${parent.url}">Back to ${file.path}</a>
  </div>

  <div class="title">${file.name}</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form name="editFile" action="saveFile.secureaction" method="POST">

    <input type="hidden" name="name" value="${file.name}" />
    <input type="hidden" name="path" value="${file.path}" />
    <input type="hidden" name="type" value="${type}" />

    <textarea name="fileContent" cols="60" rows="40"><c:out value="${fileContent}" escapeXml="true"/></textarea>
    <br /><br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
        </td>
        <td align="right">
          <input name="submit" type="submit" Value="Save File">
        </td>
      </tr>
    </table>

    </form>
  </div>

</div>