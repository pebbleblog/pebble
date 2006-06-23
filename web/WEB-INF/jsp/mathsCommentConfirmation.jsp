<div class="contentItem">

  <div class="title"><fmt:message key="comment.addComment" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form action="confirmComment.action" method="post" accept-charset="${blog.characterEncoding}">
      <input type="hidden" name="entry" value="${blogEntry.id}" />
      <input type="hidden" name="rememberMe" value="${rememberMe}" />

      <p>
        Please complete this simple maths question to confirm your comment.
      </p>

      <table width="99%">

        <tr>
          <td>${SimpleMathsCommentConfirmationStrategyArg1} ${SimpleMathsCommentConfirmationStrategyOperator} ${SimpleMathsCommentConfirmationStrategyArg2} = <input type="text" name="answer" /></td>
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
