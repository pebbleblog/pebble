<c:if test="${blog.richTextEditorForCommentsEnabled}">
  <script type="text/javascript">
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor( 'commentBody' ) ;
    oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
    oFCKeditor.Config["CustomConfigurationsPath"] = '${pageContext.request.contextPath}/fckconfig_pebble.js';
    oFCKeditor.ToolbarSet = 'Comment' ;
    oFCKeditor.ReplaceTextarea() ;
    oFCKeditor.MakeEditable();
  }
  </script>
</c:if>

<div class="contentItem">
  <h1><fmt:message key="comment.addComment" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <%@ include file="/WEB-INF/fragments/commentForm.jsp" %>
  </div>
</div>