<div class="contentItem">
  <h1>Comment Listeners</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Comment listeners are a type of Pebble plugin that allows custom code to be called
      whenever a comment is added, removed, approved or rejected. The following comment listeners are included with the Pebble distribution and those marked with a (*) are configured by default.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.response.IpAddressListener</b> (*)<br/>
      Maintains both a blacklist and a whitelist of IP addresses, performing the following when a new comment is added.
    </p>
    <ol>
    <li>If the IP address of the comment is contained within the blacklist, the comment is marked as pending and the comment's spam score is increased by 1 point.</li>
    <li>Otherwise, if the IP address of the comment is contained within the whitelist, the comment is left as-is.</li>
    <li>Otherwise, if the IP address of the comment is contained in neither list, the comment is marked as pending and the spam score remains unaffected.</li>
    </ol>
    <p>
      In addition to this, the comment's IP address is removed from the blacklist (if applicable) and added to the whitelist when it is approved. Vice versa for comments that are rejected.
      <br /><br />
      The comma separated lists of IP addresses are stored under the <code>IpAddressListener.blacklist</code> and <code>IpAddressListener.whitelist</code> plugin properties.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.response.LinkSpamListener</b> (*)<br/>
      Checks the content of new comments for the number of links that they contain. If this is greater than the threshold, the comment is marked as pending and the comment's spam score is increased by 1 point.
      <br /><br />
      The default threshold for the number of links is 3, but can be configured by the <code>LinkSpamListener.commentThreshold</code> plugin property.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.response.ContentSpamListener</b> (*)<br/>
      Checks the content of new comments for specific regular expressions. If the number of occurences
      is greater than the threshold, the comment is marked as pending and the comment's spam score is increased by 1 point. This is repeated
      for the title, author and website properties too.
      <br /><br />
      The default threshold for the number of occurences is 0, but can be configured by the <code>ContentSpamListener.threshold</code> plugin property.
      <br /><br />
      The list of regular expressions can be configured using the <code>ContentSpamListener.regexList</code> property.
     </p>

    <p>
      <b>net.sourceforge.pebble.event.response.SpamScoreListener</b> (*)<br />
      Checks the spam score of new comments and if greater than the threshold, the comment is marked as rejected.
      <br /><br />
      The default threshold is 1, but can be configured by the <code>SpamScoreListener.commentThreshold</code> plugin property.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.response.DeleteRejectedListener</b><br/>
      Deletes new comments that have been marked as rejected.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.comment.EmailNotificationListener</b><br/>
      Sends a notification e-mail when a new comment is added.
    </p>
    <ul>
      <li>If the status of the comment is pending, an e-mail is sent to the blog owner asking for approval.</li>
      <li>If the status of the comment is approved, an e-mail is sent to the blog owner and all comment authors for the blog entry that have left their e-mail addresses.</li>
    </ul>

    <p>
      <b>net.sourceforge.pebble.event.comment.EmailAuthorNotificationListener</b><br/>
      As above, except e-mails are sent to the blog entry author rather than the blog owner.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.response.MarkPendingListener</b><br/>
      Marks all new comments with a status of pending so that they require approval from a blog contributor before being published on the blog.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.response.MarkApprovedListener</b><br/>
      Marks all new comments with a status of approved so that they appear on your blog immediately. This is useful for internal blogs where you aren't worried about spam and don't need comments to be approved before being published.
    </p>

    <p>
      <b>net.sourceforge.pebble.event.response.DisableResponseListener</b><br/>
      Deletes new comments, effectively disabling the ability for readers to leave comments.
    </p>

    <h3>Configuring comment listeners</h3>
    <p>
      To configure the comment listeners used by your blog, simply modify the list on the <a href="viewPlugins.secureaction#commentListeners">Plugins</a> page. Your blog is using the
      following comment listeners.
    </p>
    <pre class="codeSample"><c:forEach var="listener" items="${blog.eventListenerList.commentListeners}">${listener['class'].name}<br /></c:forEach></pre>
  </div>
</div>