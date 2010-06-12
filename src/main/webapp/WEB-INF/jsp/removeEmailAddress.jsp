<div class="contentItem">

  <h1><fmt:message key="comment.removeEmailAddressTitle" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <fmt:message key="comment.removeEmailAddress" />
    <br /><br />

    <form name="removeEmailAddress" action="removeEmailAddress.action" method="POST">
    <pebble:token/>
    <input type="hidden" name="entry" value=""${blogEntry.id}" />
    <input type="text" name="email" size="40" />
    <input name="submit" type="submit" Value="<fmt:message key="comment.removeEmailAddressButton" />" />
    </form>

    <p>
      <fmt:message key="common.backTo" /> <a href="${url:rewrite(blogEntry.permalink)}">${blogEntry.title}</a>.
    </p>
  </div>
</div>