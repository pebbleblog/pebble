<div class="contentItem">

  <h1>Confirm</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="confirmationForm" action="${confirmationAction}" method="post" accept-charset="${blog.characterEncoding}">

      <p>
        Please complete this simple maths question.
      </p>

      <table width="99%">

        <tr>
          <td>${SimpleMathsConfirmationStrategyArg1} ${SimpleMathsConfirmationStrategyOperator} ${SimpleMathsConfirmationStrategyArg2} = <input type="text" name="answer" size="4" /></td>
        </tr>

        <tr>
          <td align="right">
            <input name="submit" type="submit" value="Confirm" />
          </td>
        </tr>

      </table>
    </form>
  </div>

</div>

<script type="text/javascript">
window.onload = function()
{
  document.confirmationForm.answer.focus();
}
</script>

