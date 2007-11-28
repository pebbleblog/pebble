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
          <input type="checkbox" name="richTextEditorForBlogEntriesEnabled" value="true"
                 <c:if test="${blog.richTextEditorForBlogEntriesEnabled == true}">checked="true"</c:if>
          />&nbsp;Blog entries
          <input type="checkbox" name="richTextEditorForStaticPagesEnabled" value="true"
                 <c:if test="${blog.richTextEditorForStaticPagesEnabled == true}">checked="true"</c:if>
          />&nbsp;Static pages
          <br />
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

      <tr>
        <td colspan="2">
          <br />
          <b>Security</b> (<span class="help"><a href="./help/configuration.html#security" target="_blank">Help</a></span>)
          <br />
          Choose those users that you would like to be <a href="./help/securityRoles.html" target="_blank">owners, publishers and contributors</a> for your blog. Optionally, you can
          <a href="./help/privateBlogs.html" target="_blank">restrict access to your blog</a> to only those readers that you specify.
        </td>
      </tr>

      <tr>
        <td colspan="2">
          <table width="99%">
            <tr>
              <td align="center">Blog owners</td>
              <td align="center">Blog publishers</td>
            </tr>
            <tr>
              <td align="center"><pebble:select name="blogOwners" items="${blogOwnerUsers}" selected="${blog.blogOwners}" label="name" value="username" size="10" multiple="true" /></td>
              <td align="center"><pebble:select name="blogPublishers" items="${blogPublisherUsers}" selected="${blog.blogPublishers}" label="name" value="username" size="10" multiple="true" /></td>
            </tr>
            </table>
        </td>
      </tr>

      <tr>
        <td colspan="2">
          <table width="99%">
            <tr>
              <td align="center">Blog contributors</td>
              <td align="center">Blog readers (<span class="help"><a href="./help/privateBlogs.html" target="_blank">Help</a></span>)</td>
            </tr>
            <tr>
              <td align="center"><pebble:select name="blogContributors" items="${blogContributorUsers}" selected="${blog.blogContributors}" label="name" value="username" size="10" multiple="true" /></td>
              <td align="center"><pebble:select name="blogReaders" items="${allUsers}" selected="${blog.blogReaders}" label="name" value="username" size="10" multiple="true" /></td>
            </tr>
            </table>
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