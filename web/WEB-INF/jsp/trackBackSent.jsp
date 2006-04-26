<div class="contentItem">
  <div class="title">TrackBack</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    TrackBack sent to ${trackBackUrl}, response code ${trackBackResponseCode}
    <br />
    <pre><c:out value="${trackBackResponseMessage}" escapeXml="true" /></pre>

    <p>
    Return to <a href="${blogEntry.permalink}">${blogEntry.title}</a>.
    </p>
  </div>

</div>