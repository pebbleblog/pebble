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

      <p>
        Please complete this simple maths question to confirm your comment.
      </p>

      <table width="99%">

        <tr>
          <td>${SimpleMathsCommentConfirmationStrategyArg1} + ${SimpleMathsCommentConfirmationStrategyArg2} = <input type="text" name="answer" /></td>
        </tr>

        <tr>
          <td align="right">
            <input name="submit" type="submit" value="Confirm Comment" />
          </td>
        </tr>

      </table>
    </form>
  </div>

</div>
