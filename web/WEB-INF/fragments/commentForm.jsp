<c:if test="${blog.richTextEditorForCommentsEnabled}">
  <script type="text/javascript" src="${pageContext.request.contextPath}/FCKeditor/fckeditor.js"></script>
  <script type="text/javascript">
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor( 'commentBody' ) ;
    oFCKeditor.BasePath = '${pageContext.request.contextPath}/FCKeditor/' ;
    oFCKeditor.Config["CustomConfigurationsPath"] = '${pageContext.request.contextPath}/fckconfig_pebble.js';
    oFCKeditor.ToolbarSet = 'Comment' ;
    oFCKeditor.ReplaceTextarea() ;
  }
  </script>
</c:if>

<form id="commentForm" name="commentForm" action="saveComment.action" method="post" accept-charset="${blog.characterEncoding}">
  <input type="hidden" name="blogId" value="<c:out value="${blogEntry.blog.id}"/>" />
  <input type="hidden" name="entry" value="<c:out value="${blogEntry.id}"/>" />
  <input type="hidden" name="parent" value="<c:out value="${undecoratedComment.parent.id}"/>" />

<%-- <div class="response"><div id="preview.commentBody">${decoratedComment.body}</div></div> --%>

<table width="99%">

  <c:if test="${not empty validationContext.errors}">
  <tr>
    <td colspan="2" class="validationError">
      <b><c:out value="${validationContext.numberOfErrors}" /> error(s)</b>
      <ul>
      <c:forEach var="error" items="${validationContext.errors}">
        <li><c:out value="${error.message}"/></li>
      </c:forEach>
      </ul>
      <br />
    </td>
  </tr>
  </c:if>

<%--  <c:if test="${not empty decoratedComment}"> --%>
  <tr>
    <td colspan="2">
      <c:set var="comment" scope="request" value="${decoratedComment}"/>
      <c:set var="displayMode" scope="request" value="preview" />
      <div id="previewComment"><jsp:include page="comment.jsp"><jsp:param name="commentIdentifier" value="previewComment"/></jsp:include></div>
      <br />
    </td>
  </tr>
<%--  </c:if> --%>

  <c:set var="comment" scope="request" value="${undecoratedComment}"/>

  <tr>
    <td valign="top"><b><fmt:message key="comment.title" /></b></td>
    <td><input type="text" name="title" size="40" value="<c:out value="${comment.title}"/>"/></td>
  </tr>

  <tr>
    <td valign="top"><b><fmt:message key="comment.body" /></b></td>
    <td>
      <textarea id="commentBody" name="commentBody" rows="8" cols="40"><c:out value="${comment.body}"/></textarea>
      <div class="small"><b>HTML</b> : b, strong, i, em, blockquote, br, p, pre, a href="", ul, ol, li</div>
    </td>
  </tr>

  <tr>
    <td valign="top"><b><fmt:message key="comment.name" /></b></td>
    <td><input type="text" id="author" name="author" size="40" value="<c:out value="${comment.author}"/>"/></td>
  </tr>

  <tr>
    <td valign="top"><b><fmt:message key="comment.emailAddress" /></b></td>
    <td>
      <input type="text" name="email" size="40" value="<c:out value="${comment.email}"/>"/>
    </td>
  </tr>

  <tr>
    <td valign="top"><b><fmt:message key="comment.website" /></b></td>
    <td><input type="text" name="website" size="40" value="<c:out value="${comment.website}"/>"/></td>
  </tr>

  <tr>
    <td valign="top"><b><fmt:message key="comment.rememberMe" /></b></td>
    <td>
      <fmt:message key="common.yes"/>&nbsp;
      <input type="radio" name="rememberMe" value="true"
        <c:if test="${rememberMe}">
          checked="checked"
        </c:if>
      />
      <fmt:message key="common.no"/>&nbsp;<input type="radio" name="rememberMe" value="false"
        <c:if test="${not rememberMe}">
          checked="checked"
        </c:if>
      />
    </td>
  </tr>

  <tr>
    <td colspan="2">
      <p>
      <fmt:message key="comment.emailDisclaimer" />
      </p>
    </td>
  </tr>

  <tr>
    <td colspan="2" align="right">
      <input name="submit" type="submit" Value="<fmt:message key="comment.previewButton"/>" onclick="previewComment(); return false;"/>
      <input name="submit" type="submit" Value="<fmt:message key="comment.addCommentButton"/>" />
    </td>
  </tr>

</table>
</form>