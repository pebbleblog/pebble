<div class="contentItem">

  <h1><fmt:message key="comment.addComment" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="commentConfirmationForm" action="confirmComment.action" method="post" accept-charset="${blog.characterEncoding}">

      <p>
        Please complete this simple maths question to confirm your comment.
      </p>

      <table width="99%">

        <tr>
          <td>${SimpleMathsCommentConfirmationStrategyArg1} ${SimpleMathsCommentConfirmationStrategyOperator} ${SimpleMathsCommentConfirmationStrategyArg2} = <input type="text" name="answer" size="4" /></td>
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

<script type="text/javascript">
window.onload = function()
{
  document.commentConfirmationForm.answer.focus();
}
</script>

