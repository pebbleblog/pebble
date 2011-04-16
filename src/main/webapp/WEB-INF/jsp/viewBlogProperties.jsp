<jsp:useBean id="blog" scope="request" type="net.sourceforge.pebble.domain.Blog"/>
<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/configuration.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.blogProperties"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      <fmt:message key="blogproperties.thePropertiesAreOnlyApplicableForThisBlog">
      	<fmt:param>${blogUrl}</fmt:param>
      	<fmt:param>${blog.name}</fmt:param>
      </fmt:message>
    </p>

    <form name="propertiesForm" action="saveBlogProperties.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
    <pebble:token/>
    <table>
      <tr>
        <td colspan="2">
          <b><fmt:message key="blogproperties.generalBlogProperties"/></b>
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.name"/>
        </td>
        <td>
          <input type="text" name="name" size="40" value="${blog.name}">
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.description"/>
        </td>
        <td>
          <input type="text" name="description" size="40" value="${blog.description}">
        </td>
      </tr>

      <tr>
        <td valign="top">
          <fmt:message key="blogproperties.about"/>
        </td>
        <td>
          <textarea name="about" rows="12" cols="50">${blog.about}</textarea>
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.imageUrl"/>
        </td>
        <td>
          <input type="text" name="image" size="40" value="${blog.image}">
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.author"/>
        </td>
        <td>
          <input type="text" name="author" size="40" value="${blog.author}">
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.emailaddress"/>
        </td>
        <td>
          <input type="text" name="email" size="40" value="${blog.email}">
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.homepage"/>
        </td>
        <td>
          <pebble:select name="homePage" items="${staticPages}" selected="${blog.homePage}" label="title" value="name" />
        </td>
      </tr>


      <c:if test="${pebbleContext.configuration.userThemesEnabled}">
      <tr>
        <td>
          <fmt:message key="blogproperties.theme"/>
        </td>
        <td>
          <pebble:select name="theme" items="${themes}" selected="${blog.theme}" />
        </td>
      </tr>
      </c:if>

      <tr>
        <td>
          <fmt:message key="blogproperties.recentBlogEntries"/>
        </td>
        <td>
          <pebble:select name="recentBlogEntriesOnHomePage" items="${numbers}" selected="${blog.recentBlogEntriesOnHomePage}" />
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.recentResponses"/>
        </td>
        <td>
          <pebble:select name="recentResponsesOnHomePage" items="${numbers}" selected="${blog.recentResponsesOnHomePage}" />
        </td>
      </tr>

      <tr>
        <td valign="top">
          <fmt:message key="blogproperties.richTextEditor"/>
        </td>
        <td>
          <input type="checkbox" name="richTextEditorForCommentsEnabled" value="true"
                 <c:if test="${blog.richTextEditorForCommentsEnabled == true}">checked="true"</c:if>
          />&nbsp;<fmt:message key="blogproperties.comments"/>
        </td>
      </tr>

      <tr>
        <td valign="top">
          <fmt:message key="blogproperties.gravatarSupport"/>
        </td>
        <td>
          <input type="checkbox" name="gravatarSupportForCommentsEnabled" value="true"
                 <c:if test="${blog.gravatarSupportForCommentsEnabled == true}">checked="true"</c:if>
          />
        </td>
      </tr>

      <tr>
        <td colspan="2">
          <br />
          <b><fmt:message key="blogproperties.i18nAndL10n"/></b>
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.country"/>
        </td>
        <td>
          <pebble:select name="country" items="${countries}" selected="${blog.country}" />
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.language"/>
        </td>
        <td>
          <pebble:select name="language" items="${languages}" selected="${blog.language}" />
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.timeZone"/>
        </td>
        <td>
          <pebble:select name="timeZone" items="${timeZones}" selected="${blog.timeZoneId}" />
          <div class="small"><fmt:message key="blogproperties.noteChangingWillReindex"/></div>
        </td>
      </tr>

      <tr>
        <td>
          <fmt:message key="blogproperties.characterEncoding"/>
        </td>
        <td>
          <pebble:select name="characterEncoding" items="${characterEncodings}" selected="${blog.characterEncoding}" />
        </td>
      </tr>

      <tr>
        <td align="right" colspan="2">
          <button name="submit" type="submit" Value="Save Properties"><fmt:message key="common.save"/></button>
        </td>
      </tr>

    </table>
    </form>
  </div>

</div>