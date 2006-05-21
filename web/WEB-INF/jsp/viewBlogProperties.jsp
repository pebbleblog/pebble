<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${pageContext.request.contextPath}/docs/configuration.html" target="_blank">Help</a>
  </div>

  <div class="title">Blog properties</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
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
          Theme
        </td>
        <td>
          <pebble:select name="theme" items="${themes}" selected="${blog.theme}" />
        </td>
      </tr>

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
        <td>
          Lucene Analyzer
        </td>
        <td>
          <input type="text" name="luceneAnalyzer" size="40" value="${blog.luceneAnalyzer}">
        </td>
      </tr>

      <tr>
        <td colspan="2">
          <br />
          <b>Security</b> (<span class="help"><a href="${pageContext.request.contextPath}/docs/multiuser-blogs.html#security" target="_blank">Help</a></span>)
        </td>
      </tr>

      <tr>
        <td>
          Blog owners
        </td>
        <td>
          <input type="text" name="blogOwners" size="40" value="${blog.blogOwners}">
        </td>
      </tr>

      <tr>
        <td>
          Blog contributors
        </td>
        <td>
          <input type="text" name="blogContributors" size="40" value="${blog.blogContributors}">
        </td>
      </tr>

      <tr>
        <td colspan="2">
          <br />
          <b>E-mail notifications</b>
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
          SMTP (host or JNDI name)
        </td>
        <td>
          <input type="text" name="smtpHost" size="40" value="${blog.smtpHost}">
        </td>
      </tr>

      <tr>
        <td colspan="2">
          <br />
          <b>XML-RPC update notification pings</b> (<span class="help"><a href="${pageContext.request.contextPath}/docs/update-pings.html" target="_blank">Help</a></span>)
        </td>
      </tr>

      <tr>
        <td valign="top">
          Websites to ping
        </td>
        <td>
          <textarea name="updateNotificationPings" rows="8" cols="40">${blog.updateNotificationPings}</textarea>
        </td>
      </tr>

      <c:if test="${blogManager.multiUser}">
      <tr>
        <td colspan="2">
          <br />
          <b>Multi-user properties</b>
        </td>
      </tr>

      <tr>
        <td>
          Public/Private blog
        </td>
        <td>
          &nbsp;
          Public&nbsp;<input type="radio" name="private" value="false"
            <c:if test="${blog.public}">
              checked="checked"
            </c:if>
          />
          Private<input type="radio" name="private" value="true"
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