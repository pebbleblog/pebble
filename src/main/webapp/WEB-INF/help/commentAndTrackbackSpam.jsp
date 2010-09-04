<div class="contentItem">
  <h1>Comment and TrackBack Spam</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Unfortunately comment and TrackBack spam is a real problem on the Internet today and during the course of running a public blog, you'll
      probably come across it at some point. Out of the box, Pebble is configured with a number of <a href="./help/commentListeners.html">Comment Listeners</a> and
      <a href="./help/trackbackListeners.html">TrackBack Listeners</a> to help you fight comment and TrackBack spam. Additionally, you
      can configure a <a href="./help/confirmationStrategies.html">Confirmation Strategy</a> to ask readers to confirm their
      comments/TrackBacks by means of clicking a button or through some other kind of CAPTCHA.
    </p>

    <a name="moderation"></a><h3>Comment and TrackBack moderation</h3>
    <p>
      Comments and TrackBacks in Pebble can have one of three statuses - approved, pending or rejected. By default, all new comments and TrackBacks have
      a status of approved, which means that they will be immediately published on your blog.
      To require that all new comments and TrackBacks are manually approved by the blog owner before being published, you can add the
      <code>MarkPendingListener</code> to your list of comment and/or TrackBack listeners. This will mark all incoming comments
      and TrackBacks as pending so that they require approval from a blog contributor.
      Once logged in, you are able to easily see those responses that are pending and/or rejected, and approve/reject them as required.
    </p>

    <p>
    <a name="bulkManagement"></a>
      For convenience, Pebble provides a way to manage your responses in bulk, instead of manually performing this process on a per response or per blog entry basis.
      This can be done by going to the <a href="viewResponses.secureaction?type=approved">Approved</a>, <a href="viewResponses.secureaction?type=pending">Pending</a> or
      <a href="viewResponses.secureaction?type=rejected">Rejected</a> pages where you can approve, reject or remove responses in bulk.
    </p>

    <p>
      Shown for each response is the author, e-mail address (if applicable), IP address, title, truncated content and the date/time
      at which the response was received. Clicking the title will take you to the full response if you need to review the
      complete content. To approve, reject or remove in bulk, just select one or more checkboxes next to the responses
      and click the appropriate button at the bottom of the page.
    </p>

    <a name="listeners"></a><h3>Fighting spam with Comment and TrackBack Listeners</h3>
    <p>
      Pebble contains some pre-built <a href="./help/commentListeners.html">Comment Listeners</a> and
      <a href="./help/trackbackListeners.html">TrackBack Listeners</a> that can help in the fight against spam.
    </p>

    <p>
      New responses (comments and TrackBacks) start off with a "spam score" of 0 points before being processed by the listeners that you have configured.
      If using the default, out of the box listener configuration, the following steps take place each time a response is added.
    </p>

    <ol>
      <li>The IP address for the response is checked against a blacklist and a whitelist. If a match is found on the blacklist, the response is marked as pending and the spam score increased by 1 point. If a match is found on the whitelist, the response remains unaffected. If the IP address is on neither list, the response is marked as pending and the spam score remains unaffected.</li>
      <li>The number of links is checked and if greater than the threshold, the response is marked as pending and the spam score is increased by 1 point.</li>
      <li>The content is checked against the list of regular expressions and if the number of occurences exceeds the threshold, the response is marked as pending and the spam score is increased by 1 point. This is repeated for the response title, author and website.</li>
      <li>The spam score of the response is checked against the threshold and if exceeded, marks the response as rejected.</li>
      <li>The status of the response is checked and if rejected, the response is deleted.</li>
      <li>An e-mail notification is sent if the status of the response is approved or pending.</li>
    </ol>

    <p>
      Full details of the listeners are their configurable properties can be found in <a href="./help/commentListeners.html">Comment Listeners</a> and
      <a href="./help/trackbackListeners.html">TrackBack Listeners</a>.
    </p>
  </div>
</div>