<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${url:rewrite(parent.url)}"><fmt:message key="admin.backTo"><fmt:param>${file.path}</fmt:param></fmt:message></a>
  </div>

  <h1>${file.name}</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="editFile" action="saveFile.secureaction" method="POST">
    <pebble:token/>

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
          <button name="submit" type="submit" Value="Save File"><fmt:message key="admin.saveFile"/></button>
        </td>
      </tr>
    </table>

    </form>
  </div>

</div>