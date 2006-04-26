<div class="contentItem">
  <div class="title"><fmt:message key="comment.addComment" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <a name="form"></a>
    <form name="commentForm" action="saveComment.action" method="post" accept-charset="<c:out value="${blog.characterEncoding}" />">
    <input type="hidden" name="entry" value="<c:out value="${blogEntry.id}"/>" />
    <input type="hidden" name="parent" value="<c:out value="${undecoratedComment.parent.id}"/>" />

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

      <c:if test="${not empty decoratedComment}">
      <tr>
        <td colspan="2">
          <c:set var="comment" scope="request" value="${decoratedComment}"/>
          <c:set var="displayMode" scope="request" value="preview" />
          <jsp:include page="comment.jsp" />
          <br />
        </td>
      </tr>
      </c:if>

      <c:set var="comment" scope="request" value="${undecoratedComment}"/>

      <tr>
        <td valign="top"><b><fmt:message key="comment.title" /></b></td>
        <td><input type="text" name="title" size="40" value="<c:out value="${comment.title}"/>"/></td>
      </tr>

      <tr>
        <td valign="top"><b><fmt:message key="comment.body" /></b></td>
        <td>
          <textarea name="body" rows="8" cols="40"><c:out value="${comment.body}"/></textarea>
          <div class="small"><b>HTML</b> : b, i, blockquote, br, p, pre, a href="", ul, ol, li</div>
        </td>
      </tr>

      <tr>
        <td valign="top"><b><fmt:message key="comment.name" /></b></td>
        <td><input type="text" name="author" size="40" value="<c:out value="${comment.author}"/>"/></td>
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
          <input name="submit" type="submit" Value="<fmt:message key="comment.previewButton"/>" />
          <input name="submit" type="submit" Value="<fmt:message key="comment.addCommentButton"/>" />
        </td>
      </tr>

    </table>
    </form>
  </div>
</div>