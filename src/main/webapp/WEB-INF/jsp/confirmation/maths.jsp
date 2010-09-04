<div class="contentItem">

  <h1><fmt:message key="confirmation.confirm" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="confirmationForm" action="${confirmationAction}" method="post" accept-charset="${blog.characterEncoding}">
      <pebble:token/>

      <p>
        <fmt:message key="confirmation.simpleMathsMessage" />
      </p>

      <table width="99%">

        <tr>
          <td>${SimpleMathsConfirmationStrategyArg1} ${SimpleMathsConfirmationStrategyOperator} ${SimpleMathsConfirmationStrategyArg2} = <input type="text" name="answer" size="4" /></td>
        </tr>

        <tr>
          <td align="right">
            <input name="submit" type="submit" value="<fmt:message key='confirmation.confirm' />" />
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