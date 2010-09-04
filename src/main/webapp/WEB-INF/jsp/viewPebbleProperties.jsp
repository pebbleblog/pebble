<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/multiBlogConfiguration.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.pebbleProperties"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      The properties on this page are only applicable to the aggregated view when running in multi-blog mode. Individual blogs have their own properties that can be set separately.
    </p>

    <form name="propertiesForm" action="savePebbleProperties.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
    <pebble:token/>
    <table>
      <tr>
        <td colspan="2">
          <b>General properties</b>
        </td>
      </tr>

      <tr>
        <td>
          Name
        </td>
        <td>
          <input type="text" name="name" size="40" value="${blog.name}">
        </td>
      </tr>

      <tr>
        <td>
          Description
        </td>
        <td>
          <input type="text" name="description" size="40" value="${blog.description}">
        </td>
      </tr>

      <tr>
        <td>
          Author
        </td>
        <td>
          <input type="text" name="author" size="40" value="${blog.author}">
        </td>
      </tr>

      <tr>
        <td>
          Maximum recent blog entries
        </td>
        <td>
          <pebble:select name="recentBlogEntriesOnHomePage" items="${numbers}" selected="${blog.recentBlogEntriesOnHomePage}" />
        </td>
      </tr>

      <c:if test="${pebbleContext.configuration.userThemesEnabled}">
      <tr>
        <td>
          Theme
        </td>
        <td>
          <pebble:select name="theme" items="${themes}" selected="${blog.theme}" />
        </td>
      </tr>
      </c:if>

      <tr>
        <td colspan="2">
          <br />
          <b>Internationalization and localization</b>
        </td>
      </tr>

      <tr>
        <td>
          Country
        </td>
        <td>
          <pebble:select name="country" items="${countries}" selected="${blog.country}" />
        </td>
      </tr>

      <tr>
        <td>
          Language
        </td>
        <td>
          <pebble:select name="language" items="${languages}" selected="${blog.language}" />
        </td>
      </tr>

      <tr>
        <td>
          Time zone
        </td>
        <td>
          <pebble:select name="timeZone" items="${timeZones}" selected="${blog.timeZoneId}" />
        </td>
      </tr>

      <tr>
        <td>
          Character encoding
        </td>
        <td>
          <pebble:select name="characterEncoding" items="${characterEncodings}" selected="${blog.characterEncoding}" />
        </td>
      </tr>

      <tr>
        <td align="right" colspan="2">
          <input name="submit" type="submit" Value="Save Properties">
        </td>
      </tr>

    </table>
    </form>
  </div>

</div>