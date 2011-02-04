<c:if test="${authenticatedUser.preferences['richTextEditorForStaticPagesEnabled'] == 'true'}">
  <script type="text/javascript">
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor( 'body' ) ;
    oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/';
    oFCKeditor.Config["CustomConfigurationsPath"] = '${pageContext.request.contextPath}/fckconfig_pebble.js';
    oFCKeditor.ToolbarSet = 'StaticPage' ;
    oFCKeditor.ReplaceTextarea() ;
  }
  </script>
</c:if>

<c:set var="originalStaticPage" scope="request" value="${staticPage}" />
<c:set var="staticPage" scope="request" value="${previewStaticPage}" />
<c:set var="displayMode" scope="request" value="preview" />

<a name="preview"></a>
<jsp:include page="staticPage.jsp" />

<c:set var="staticPage" scope="request" value="${originalStaticPage}" />

<a name="form"></a>
<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/staticPages.html" target="_blank">Help</a>
  </div>

  <h1>Static page</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form class="editStaticPageForm" name="editStaticPage" action="saveStaticPage.secureaction#preview" method="POST" accept-charset="${blog.characterEncoding}">
    <pebble:token/>
    <input type="hidden" name="page" value="${staticPage.id}" />
    <input type="hidden" name="persistent" value="${staticPage.persistent}" />

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
        <td valign="top"><b>Name</b></td>
        <td><c:out value="${blogUrl}pages/"/><input type="text" name="name" size="20" value="${staticPage.name}">.html</td>
      </tr>

      <tr>
        <td valign="top"><b>Title</b></td>
        <td><input type="text" name="title" class="fullWidthText" value="${staticPage.title}"></td>
      </tr>

      <tr>
        <td valign="top"><b>Subtitle</b></td>
        <td><input type="text" name="subtitle" class="fullWidthText" value="${staticPage.subtitle}"></td>
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
        <td colspan="2"><b>Body</b></td>
      </tr>
      <tr>
        <td colspan="2"><textarea name="body" class="bodyTextArea"><c:out value="${staticPage.body}" escapeXml="true"/></textarea></td>
      </tr>

      <tr>
        <td valign="top"><b>Tags</b></td>
        <td>
          <input type="text" name="tags" class="fullWidthText" value="${staticPage.tags}">
          <div class="small">(use a + character to represent a space in a tag)</div>
        </td>
      </tr>

      <tr>
        <td valign="top"><b>Original permalink</b></td>
        <td>
          <input type="text" name="originalPermalink" class="fullWidthText" value="${staticPage.originalPermalink}">
          <div class="small">(optional, this will become the permalink for your static page)</div>
        </td>
      </tr>

      <tr>
        <td valign="top"><b>Template</b></td>
        <td><input type="text" name="template" class="fullWidthText" value="${staticPage.template}"></td>
      </tr>

      <tr>
        <td align="left">
          <button name="submit" type="submit" Value="Cancel" ><fmt:message key="common.cancel"/></button>
        </td>
        <td align="right">
          <button name="submit" type="submit" Value="Preview"><fmt:message key="common.preview"/></button>
          <button name="submit" type="submit" Value="Save"><fmt:message key="common.save"/></button>
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>