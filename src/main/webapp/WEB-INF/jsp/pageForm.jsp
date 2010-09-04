<%-- 
TODO: I have not found any usage of this page - 
      perhaps it is superceded by staticPageForm.jsp
      (and blogEntryForm.jsp)?
--%>
<%-- uncomment this to enable FCKeditor
<script type="text/javascript" src="${pageContext.request.contextPath}/FCKeditor/fckeditor.js"></script>
<script type="text/javascript">
window.onload = function()
{
  var oFCKeditor = new FCKeditor( 'excerpt' ) ;
  oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
  oFCKeditor.ToolbarSet = 'Basic' ;
  oFCKeditor.ReplaceTextarea() ;

  oFCKeditor = new FCKeditor( 'body' ) ;
  oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
  oFCKeditor.ToolbarSet = 'Basic' ;
  oFCKeditor.ReplaceTextarea() ;
}
</script>
--%>

<c:set var="originalBlogEntry" scope="request" value="${blogEntry}" />
<c:set var="blogEntry" scope="request" value="${previewBlogEntry}" />
<c:set var="displayMode" scope="request" value="preview" />

<a name="preview"></a>
<c:choose>
  <c:when test="${blogEntry.staticPage}">
    <jsp:include page="staticPage.jsp" />
  </c:when>
  <c:otherwise>
    <jsp:include page="blogEntry.jsp" />
  </c:otherwise>
</c:choose>

<c:set var="blogEntry" scope="request" value="${originalBlogEntry}" />

<a name="form"></a>
<div class="contentItem">

  <div class="contentItemLinks">
    <c:choose>
      <c:when test="${blogEntry.type == 8}"><a href="./help/static-pages.html" target="_blank">Help</a></c:when>
      <c:when test="${blogEntry.type != 8}"><a href="./help/blog-entries.html" target="_blank">Help</a></c:when>
    </c:choose>
  </div>

  <h1>
    <c:choose>
      <c:when test="${blogEntry.type == 8}">Static page</c:when>
      <c:when test="${blogEntry.type != 8}">Blog entry</c:when>
    </c:choose>
  </h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="editBlogEntry" action="saveBlogEntry.secureaction#preview" method="POST" accept-charset="${blog.characterEncoding}">
    <pebble:token/>
    <input type="hidden" name="entry" value="${blogEntry.id}" />
    <input type="hidden" name="type" value="${blogEntry.type}" />

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
      <c:if test="${blogEntry.type == 8}">
      <tr>
        <td valign="top"><b>Name</b></td>
        <td>${blogUrl}pages/<input type="text" name="staticName" size="20" value="${blogEntry.staticName}">.html</td>
      </tr>
      </c:if>

      <tr>
        <td valign="top"><b>Title</b></td>
        <td><input type="text" name="title" size="60" value="${blogEntry.title}"></td>
      </tr>

      <tr>
        <td valign="top"><b>Subtitle</b></td>
        <td><input type="text" name="subtitle" size="60" value="${blogEntry.subtitle}"></td>
      </tr>

      <c:if test="${!blogEntry.staticPage}">
      <tr>
        <td valign="top"><b>Excerpt</b></td>
        <td><textarea name="excerpt" rows="20" cols="60"><c:out value="${blogEntry.excerpt}" escapeXml="true"/></textarea></td>
      </tr>
      </c:if>

      <tr>
        <td valign="top"><b>Body</b></td>
        <td><textarea name="body" rows="40" cols="60"><c:out value="${blogEntry.body}" escapeXml="true"/></textarea></td>
      </tr>

      <tr>
        <td valign="top"><b>Original permalink</b></td>
        <td>
          <input type="text" name="originalPermalink" size="60" value="${blogEntry.originalPermalink}">
          <div class="small">(optional, this will become the permalink for your entry)</div>
        </td>
      </tr>

      <c:if test="${not blogEntry.staticPage}">
      <tr>
        <td valign="top"><b>Comments</b> (<span class="help"><a href="./help/comments.html" target="_blank">Help</a></span>)</td>
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
        <td valign="top"><b>TrackBacks</b> (<span class="help"><a href="./help/trackbacks.html" target="_blank">Help</a></span>)</td>
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
      </c:if>

      <c:if test="${not blogEntry.staticPage}">
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

        <c:if test="${blogEntry.type == 1}">
        <tr>
          <td valign="top"><b>Date/time</b></td>
          <td>
            <input type="text" name="date" size="60" value="<fmt:formatDate value="${blogEntry.date}" type="both" dateStyle="medium" timeStyle="medium" />">
          </td>
        </tr>
        </c:if>

        <tr>
          <td valign="top"><br /><b>Attachment</b> (<span class="help"><a href="./help/blog-entries.html#attachments" target="_blank">Help</a></span>)</td>
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

      </c:if>

      <tr>
        <td colspan="2" align="right">
          <button name="submit" type="submit" Value="Preview" ><fmt:message key="common.preview"/></button>
          <c:choose>
          <c:when test="${blogEntry.staticPage}">
          <button name="submit" type="submit" Value="Publish" ><fmt:message key="common.publish"/></button>
          </c:when>
          <c:otherwise>
          <button name="submit" type="submit" Value="Save as Template" ><fmt:message key="common.saveAsTemplate"/></button>
          <button name="submit" type="submit" Value="Save as Draft" ><fmt:message key="common.saveAsDraft"/></button>
          <button name="submit" type="submit" Value="Publish" ><fmt:message key="common.publish"/></button>
          </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>