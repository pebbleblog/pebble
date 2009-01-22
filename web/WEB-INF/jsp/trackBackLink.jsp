<div class="contentItem">

  <h1><fmt:message key="trackback.sendTrackBack" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      <fmt:message key="trackback.hereIsLink">
        <fmt:param><a href="${url:rewrite(blogEntry.permalink)}">${blogEntry.title}</a></fmt:param>
      </fmt:message>
      <c:if test="${trackBackLinkExpires}">
        <fmt:message key="trackback.linkExpires" />
      </c:if>
    </p>

    <pre class="small">${trackBackLink}</pre>
  </div>

</div>