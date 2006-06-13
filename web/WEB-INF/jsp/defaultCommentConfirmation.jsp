<div class="contentItem">

  <div class="title"><fmt:message key="comment.addComment" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form action="confirmComment.action" method="post" accept-charset="${blog.characterEncoding}">
      <input type="hidden" name="entry" value="${blogEntry.id}" />
      <input type="hidden" name="parent" value="${undecoratedComment.parent.id}" />
      <input type="hidden" name="title" value="${undecoratedComment.title}" />
      <input type="hidden" name="body" value="${undecoratedComment.body}" />
      <input type="hidden" name="author" value="${undecoratedComment.author}" />
      <input type="hidden" name="email" value="${undecoratedComment.email}" />
      <input type="hidden" name="website" value="${undecoratedComment.website}" />
      <input type="hidden" name="rememberMe" value="${rememberMe}" />

      <table width="99%">

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

        <tr>
          <td colspan="2" align="right">
            <input name="submit" type="submit" value="Confirm Comment" />
          </td>
        </tr>

      </table>
    </form>
  </div>

</div>
