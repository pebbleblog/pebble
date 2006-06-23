<div class="contentItem">

  <div class="title"><fmt:message key="comment.addComment" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form action="confirmComment.action" method="post" accept-charset="${blog.characterEncoding}">
      
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
