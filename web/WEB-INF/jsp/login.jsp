<div class="contentItem">

  <div class="title"><fmt:message key="login.title" /></div>
  <div class="subtitle"><c:choose><c:when test="${param.error eq true}" ><fmt:message key="login.incorrect" /></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></div>

  <div class="contentItemBody">
  </div>

</div>

<script type="text/javascript">
window.onload = function()
{
  document.loginForm.j_username.focus();
}
</script>
