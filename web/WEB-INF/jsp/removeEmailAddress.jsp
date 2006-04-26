<div class="contentItem">

  <div class="title"><fmt:message key="comment.removeEmailAddressTitle" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <fmt:message key="comment.removeEmailAddress" />
    <br /><br />

    <form name="removeEmailAddress" action="removeEmailAddress.action" method="POST">
    <input type="hidden" name="entry" value=""${blogEntry.id}" />
    <input type="text" name="email" size="40" />
    <input name="submit" type="submit" Value="<fmt:message key="comment.removeEmailAddressButton" />" />
    </form>

    <p>
      <fmt:message key="common.backTo" /> <a href="${blogEntry.permalink}">${blogEntry.title}</a>.
    </p>
  </div>
</div>