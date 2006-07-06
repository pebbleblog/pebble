<c:if test="${blog.richTextEditorForBlogEntriesEnabled}">
  <script type="text/javascript" src="${pageContext.request.contextPath}/FCKeditor/fckeditor.js"></script>
  <script type="text/javascript">
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor( 'excerpt' ) ;
    oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
    oFCKeditor.ToolbarSet = 'BlogEntry' ;
    oFCKeditor.ReplaceTextarea() ;

    oFCKeditor = new FCKeditor( 'body' ) ;
    oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
    oFCKeditor.ToolbarSet = 'BlogEntry' ;
    oFCKeditor.ReplaceTextarea() ;
  }
  </script>
</c:if>

<c:set var="originalBlogEntry" scope="request" value="${blogEntry}" />
<c:set var="blogEntry" scope="request" value="${previewBlogEntry}" />
<c:set var="displayMode" scope="request" value="preview" />

<a name="preview"></a>
<jsp:include page="blogEntry.jsp" />

<c:set var="blogEntry" scope="request" value="${originalBlogEntry}" />

<a name="form"></a>
<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/blogEntries.html" target="_blank">Help</a>
  </div>

  <h1>Blog entry</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="editBlogEntry" action="saveBlogEntry.secureaction#preview" method="POST" accept-charset="${blog.characterEncoding}">
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
        <td valign="top"><b>Title</b></td>
        <td><input type="text" name="title" size="60" value="${blogEntry.title}"></td>
      </tr>

      <tr>
        <td valign="top"><b>Subtitle</b></td>
        <td><input type="text" name="subtitle" size="60" value="${blogEntry.subtitle}"></td>
      </tr>

      <tr>
        <td colspan="2"><b>Excerpt</b></td>
      </tr>
      <tr>
        <td colspan="2"><textarea name="excerpt" rows="20" cols="60"><c:out value="${blogEntry.excerpt}" escapeXml="true"/></textarea>        <div class="small">(optional, short version of body for home/month/day pages)</div></td>
      </tr>

      <tr>
        <td colspan="2"><b>Body</b></td>
      </tr>
      <tr>
        <td colspan="2"><textarea name="body" rows="40" cols="60"><c:out value="${blogEntry.body}" escapeXml="true"/></textarea></td>
      </tr>

      <tr>
        <td valign="top"><b>Original permalink</b></td>
        <td>
          <input type="text" name="originalPermalink" size="60" value="${blogEntry.originalPermalink}">
          <div class="small">(optional, this will become the permalink for your entry)</div>
        </td>
      </tr>

      <tr>
        <td valign="top"><b>Comments</b></td>
        <td>
          Enabled&nbsp;
          <input type="radio" name="commentsEnabled" value="true"
            <c:if test="${blogEntry.commentsEnabled}">
              checked="checked"
            </c:if>
          />
          Disabled&nbsp;<input type="radio" name="commentsEnabled" value="false"
            <c:if test="${not blogEntry.commentsEnabled}">
              checked="checked"
            </c:if>
          />
        </td>
      </tr>

      <tr>
        <td valign="top"><b>TrackBacks</b></td>
        <td>
          Enabled&nbsp;
          <input type="radio" name="trackBacksEnabled" value="true"
            <c:if test="${blogEntry.trackBacksEnabled}">
              checked="checked"
            </c:if>
          />
          Disabled&nbsp;<input type="radio" name="trackBacksEnabled" value="false"
            <c:if test="${not blogEntry.trackBacksEnabled}">
              checked="checked"
            </c:if>
          />
        </td>
      </tr>

      <c:if test="${not empty blog.rootCategory.subCategories}">
      <tr>
        <td valign="top"><b>Category</b></td>
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
        <td valign="top"><b>Tags</b></td>
        <td>
          <input type="text" name="tags" size="60" value="${blogEntry.tags}">
        </td>
      </tr>

      <c:if test="${not blogEntry.persistent}">
      <tr>
        <td valign="top"><b>Date/time</b></td>
        <td>
          <input type="text" name="date" size="60" value="<fmt:formatDate value="${blogEntry.date}" type="both" dateStyle="medium" timeStyle="short" />">
        </td>
      </tr>
      </c:if>

      <tr>
        <td>
          <b>Time zone</b>
        </td>
        <td>
          <pebble:select name="timeZone" items="${timeZones}" selected="${blogEntry.timeZoneId}" />
        </td>
      </tr>
      <tr>
        <td valign="top"><br /><b>Attachment</b></td>
        <td></td>
      </tr>
      <tr>
        <td valign="top"><b>URL</b></td>
        <td><input type="text" name="attachmentUrl" size="60" value="${blogEntry.attachment.url}"></td>
      </tr>
      <tr>
        <td valign="top"><b>Size</b></td>
        <td><input type="text" name="attachmentSize" size="20" value="${blogEntry.attachment.size}"></td>
      </tr>
      <tr>
        <td valign="top"><b>Type</b></td>
        <td><input type="text" name="attachmentType" size="20" value="${blogEntry.attachment.type}"></td>
      </tr>

      <tr>
        <td colspan="2" align="right">
          <input name="submit" type="submit" Value="Preview" />
          <input name="submit" type="submit" Value="Save" />
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>