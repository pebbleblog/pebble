<div class="contentItem">
  <h1>TrackBack</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    TrackBack sent to ${trackBackUrl}, response code ${trackBackResponseCode}
    <br />
    <pre><c:out value="${trackBackResponseMessage}" escapeXml="true" /></pre>

    <p>
    Return to <a href="${url:rewrite(blogEntry.permalink)}">${blogEntry.title}</a>.
    </p>
  </div>

</div>