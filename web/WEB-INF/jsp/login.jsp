<div class="contentItem">

  <h1><fmt:message key="login.title" /></h1>
  <h2><c:choose><c:when test="${param.error eq true}" ><fmt:message key="login.incorrect" /></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></h2>

  <div class="contentItemBody">
  </div>

</div>

<script type="text/javascript">
window.onload = function()
{
  document.loginForm.j_username.focus();
}
</script>
