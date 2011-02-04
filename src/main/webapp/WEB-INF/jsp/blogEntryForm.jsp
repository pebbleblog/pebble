<c:if test="${authenticatedUser.preferences['richTextEditorForBlogEntriesEnabled'] == 'true'}">
  <script type="text/javascript">
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor( 'excerpt' ) ;
    oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
    oFCKeditor.Config["CustomConfigurationsPath"] = '${pageContext.request.contextPath}/fckconfig_pebble.js';
    oFCKeditor.ToolbarSet = 'BlogEntry' ;
    oFCKeditor.ReplaceTextarea() ;

    oFCKeditor = new FCKeditor( 'body' ) ;
    oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
    oFCKeditor.Config["CustomConfigurationsPath"] = '${pageContext.request.contextPath}/fckconfig_pebble.js';
    oFCKeditor.ToolbarSet = 'BlogEntry' ;
    oFCKeditor.ReplaceTextarea() ;
  }
  </script>
</c:if>
<script type="text/javascript">
  etPhoneHome("${pageContext.request.contextPath}");
</script>

<c:set var="originalBlogEntry" scope="request" value="${blogEntry}" />
<c:set var="blogEntry" scope="request" value="${previewBlogEntry}" />
<c:set var="displayMode" scope="request" value="preview" />

<a name="preview"></a>
<jsp:include page="blogEntry.jsp" />

<c:set var="blogEntry" scope="request" value="${originalBlogEntry}" />

<a name="form"></a>
<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/blogEntries.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="blogentry.blogentry"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form class="editBlogEntryForm" name="editBlogEntry" action="saveBlogEntry.secureaction#preview" method="POST" accept-charset="${blog.characterEncoding}">
    <pebble:token/>
    <input type="hidden" name="entry" value="${blogEntry.id}" />
    <input type="hidden" name="persistent" value="${blogEntry.persistent}" />

    <c:if test="${not empty validationContext.errors}">
    <div class="validationError">
      <b>${validationContext.numberOfErrors} error(s)</b>
      <ul>
      <c:forEach var="error" items="${validationContext.errors}">
        <li>${error.message}</li>
      </c:forEach>
      </ul>
    </div>
    </c:if>

    <table width="99%" cellspacing="0" cellpadding="4">
      <tr>
        <td valign="top"><b><fmt:message key="blogentry.title"/></b></td>
        <td><input type="text" name="title" class="fullWidthText" value="${fn:escapeXml(blogEntry.title)}"></td>
      </tr>

      <tr>
        <td valign="top"><b><fmt:message key="blogentry.subtitle"/></b></td>
        <td><input type="text" name="subtitle" class="fullWidthText" value="${fn:escapeXml(blogEntry.subtitle)}"></td>
      </tr>

      <tr>
        <td colspan="2" id="companionContainerOpenLink"><a href="#" onclick="openCompanion();return false;"><fmt:message key="blogentry.openCompanion"/></a></td>
      </tr>
      <tr>
        <td colspan="2"><div id="companionContainer">
          <textarea id="companionData"><c:out value="${blog.blogCompanion.content}" escapeXml="true"/></textarea>
          <br />
          <input type="button" value="<fmt:message key="common.close"/>" onClick="closeCompanion();" />
          <input type="button" value="<fmt:message key="common.save"/>" onClick="saveCompanion();" />
          <span id="companionResult"></span>
        </div></td> 
      </tr>
      
      <tr>
        <td colspan="2"><b><fmt:message key="blogentry.excerpt"/></b></td>
      </tr>
      <tr>
        <td colspan="2"><textarea name="excerpt" class="excerptTextArea"><c:out value="${blogEntry.excerpt}" escapeXml="true"/></textarea>        <div class="small">(optional, short version of body for home/month/day pages)</div></td>
      </tr>

      <tr>
        <td colspan="2"><b><fmt:message key="blogentry.body"/></b></td>
      </tr>
      <tr>
        <td colspan="2"><textarea name="body" class="bodyTextArea"><c:out value="${blogEntry.body}" escapeXml="true"/></textarea></td>
      </tr>

      <tr>
        <td valign="top"><b><fmt:message key="blogentry.originalPermalink"/></b></td>
        <td>
          <input type="text" name="originalPermalink" class="fullWidthText" value="${blogEntry.originalPermalink}">
          <div class="small"><fmt:message key="blogentry.originalPermalink.description"/></div>
        </td>
      </tr>

      <tr>
        <td valign="top"><b><fmt:message key="blogentry.comments"/></b></td>
        <td>
          <fmt:message key="common.enabled"/>&nbsp;
          <input type="radio" name="commentsEnabled" value="true"
            <c:if test="${blogEntry.commentsEnabled}">
              checked="checked"
            </c:if>
          />
          <fmt:message key="common.disabled"/>&nbsp;<input type="radio" name="commentsEnabled" value="false"
            <c:if test="${not blogEntry.commentsEnabled}">
              checked="checked"
            </c:if>
          />
        </td>
      </tr>

      <tr>
        <td valign="top"><b><fmt:message key="blogentry.trackbacks"/></b></td>
        <td>
          <fmt:message key="common.enabled"/>&nbsp;
          <input type="radio" name="trackBacksEnabled" value="true"
            <c:if test="${blogEntry.trackBacksEnabled}">
              checked="checked"
            </c:if>
          />
          <fmt:message key="common.disabled"/>&nbsp;<input type="radio" name="trackBacksEnabled" value="false"
            <c:if test="${not blogEntry.trackBacksEnabled}">
              checked="checked"
            </c:if>
          />
        </td>
      </tr>

      <c:if test="${not empty blog.rootCategory.subCategories}">
      <tr>
        <td valign="top"><b><fmt:message key="blogentry.categories"/></b></td>
        <td>
          <table width="99%" cellpadding="0" cellspacing="0">
            <tr>
            <c:forEach var="category" items="${blog.categories}" varStatus="status" begin="1">
              <c:if test="${status.count % 2 == 1}">
              <tr>
              </c:if>
              <td>
              <input type="checkbox" name="category" value="${category.id}"
              <c:forEach var="blogEntryCategory" items="${blogEntry.categories}"><c:if test="${category eq blogEntryCategory}">checked="true"</c:if></c:forEach>                />&nbsp;${category.name}
              </td>
              <c:if test="${status.count % 2 == 0}">
              </tr>
              </c:if>
            </c:forEach>
            <tr>
          </table>
          <br />
        </td>
      </tr>
      </c:if>

      <tr>
        <td valign="top"><b><fmt:message key="blogentry.tags"/></b></td>
        <td>
          <input type="text" name="tags" class="fullWidthText" value="${blogEntry.tags}">
          <div class="small"><fmt:message key="blogentry.tags.usePlusToSeparate"/></div>
        </td>
      </tr>

      <c:if test="${not blogEntry.persistent}">
      <tr>
        <td valign="top"><b><fmt:message key="blogentry.dateTime"/></b></td>
        <td>
          <input type="text" name="date" size="30" value="<fmt:formatDate value="${blogEntry.date}" type="both" dateStyle="medium" timeStyle="short" />"><fmt:message key="blogentry.dateTime.cantBeInTheFuture"/>
        </td>
      </tr>
      </c:if>

      <tr>
        <td>
          <b><fmt:message key="blogentry.timeZone"/></b>
        </td>
        <td>
          <pebble:select name="timeZone" items="${timeZones}" selected="${blogEntry.timeZoneId}" />
        </td>
      </tr>
      <tr>
        <td valign="top"><br /><b><fmt:message key="blogentry.attachment"/></b></td>
        <td></td>
      </tr>
      <tr>
        <td valign="top"><b><fmt:message key="blogentry.attachment.url"/></b></td>
        <td><input type="text" name="attachmentUrl" class="fullWidthText" value="${blogEntry.attachment.url}"></td>
      </tr>
      <tr>
        <td valign="top"><b><fmt:message key="blogentry.attachment.size"/></b></td>
        <td><input type="text" name="attachmentSize" size="20" value="${blogEntry.attachment.size}"></td>
      </tr>
      <tr>
        <td valign="top"><b><fmt:message key="blogentry.attachment.type"/></b></td>
        <td><input type="text" name="attachmentType" size="20" value="${blogEntry.attachment.type}"></td>
      </tr>
      <tr>
        <td colspan="2" align="right">
          <button name="submit" type="submit" Value="Preview"><fmt:message key="common.preview"/></button>
          <button name="submit" type="submit" Value="Save"><fmt:message key="common.save"/></button>
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>