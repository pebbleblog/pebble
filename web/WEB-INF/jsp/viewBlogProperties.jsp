<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/configuration.html" target="_blank">Help</a>
  </div>

  <h1>Blog properties</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      The properties on this page are only applicable to this blog (<a href="${blog.url}">${blog.name}</a>).
    </p>

    <form name="propertiesForm" action="saveBlogProperties.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
    <table>
      <tr>
        <td colspan="2">
          <b>General blog properties</b>
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
        <td valign="top">
          About
        </td>
        <td>
          <textarea name="about" rows="12" cols="50">${blog.about}</textarea>
        </td>
      </tr>

      <tr>
        <td>
          Image (URL)
        </td>
        <td>
          <input type="text" name="image" size="40" value="${blog.image}">
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
          E-mail address
        </td>
        <td>
          <input type="text" name="email" size="40" value="${blog.email}">
        </td>
      </tr>

      <tr>
        <td>
          Home page
        </td>
        <td>
          <pebble:select name="homePage" items="${staticPages}" selected="${blog.homePage}" label="title" value="name" />
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
        <td>
          Recent blog entries
        </td>
        <td>
          <pebble:select name="recentBlogEntriesOnHomePage" items="${numbers}" selected="${blog.recentBlogEntriesOnHomePage}" />
        </td>
      </tr>

      <tr>
        <td>
          Recent responses
        </td>
        <td>
          <pebble:select name="recentResponsesOnHomePage" items="${numbers}" selected="${blog.recentResponsesOnHomePage}" />
        </td>
      </tr>

      <tr>
        <td valign="top">
          Rich text editor
        </td>
        <td>
          <input type="checkbox" name="richTextEditorForCommentsEnabled" value="true"
                 <c:if test="${blog.richTextEditorForCommentsEnabled == true}">checked="true"</c:if>
          />&nbsp;Comments
        </td>
      </tr>

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
          <div class="small">(changing this will reindex your blog, which may take a while)</div>
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

      <c:if test="${blogManager.multiBlog}">
      <tr>
        <td colspan="2">
          <br />
          <b>Multi-blog</b>
        </td>
      </tr>

      <tr>
        <td>
          Aggregate
        </td>
        <td>
          &nbsp;
          Yes&nbsp;<input type="radio" name="private" value="false"
            <c:if test="${blog.public}">
              checked="checked"
            </c:if>
          />
          No<input type="radio" name="private" value="true"
            <c:if test="${blog.private}">
              checked="checked"
            </c:if>
          />
        </td>
      </tr>
      </c:if>

      <tr>
        <td align="right" colspan="2">
          <input name="submit" type="submit" Value="Save Properties">
        </td>
      </tr>

    </table>
    </form>
  </div>

</div>