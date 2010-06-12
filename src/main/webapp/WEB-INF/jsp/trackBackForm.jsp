<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/trackbacks.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="trackback.sendTrackBack"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="trackBackForm" action="sendTrackBack.secureaction" method="post" accept-charset="${blog.characterEncoding}">
    <pebble:token/>
    <input type="hidden" name="entry" value="${blogEntry.id}" />
    <table width="99%">
      <tr>
        <td valign="top"><b><fmt:message key="trackback.url" /></b></td>
        <td><input type="text" name="url" size="50" /></td>
      </tr>

      <tr>
        <td valign="top"><b><fmt:message key="blogentry.excerpt"/></b></td>
        <td>
          <textarea name="excerpt" rows="8" cols="40">${blogEntry.truncatedContent}</textarea>
        </td>
      </tr>

      <tr>
        <td colspan="2" align="right">
          <button name="submit" type="submit" Value="Send TrackBack" ><fmt:message key="common.sendTrackback"/></button>
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>