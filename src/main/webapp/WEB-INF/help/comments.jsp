<div class="contentItem">
  <h1>Comments</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Pebble allows visitors to leave comments on your blog entries and although comments are enabled for new blog entries by default, they can be disabled so that comments can't be left.
      The number of comments for any blog entry is shown underneath the blog entry itself along with a "Responses[x]" link that takes you to the comments for that entry.
      Pebble supports nested comments for threads of conversation and allows readers to preview their comments before posting them to your blog.
    </p>


    <h3>Removing comments</h3>
    <p>
      If for any reason you want to remove a comment, just check the checkbox next to it and click the "Remove" button at the bottom of the page. The e-mail and IP address of the comment author are not published on your blog, only logged in users can see this information.
    </p>

    <h3>E-mail notifications</h3>
    <p>
      Pebble can be configured to create an e-mail notification when new comments are added, sending it to the blog owner and
      anybody that has left a comment for that blog entry and specified their e-mail address. To encourage further
      discussion via comments and prevent e-mail addresses being harvested for spam, a separate e-mail is sent to each person.
    </p>

    <p>
      To enable e-mail notifications, ensure that your e-mail address is defined on the
      <a href="viewBlogProperties.secureaction">Blog properties</a> page and add the following class name to the list of
      comment listeners on the <a href="viewPlugins.secureaction">plugins</a> page.
    </p>

    <pre class="codeSample">net.sourceforge.pebble.listenerner.comment.EmailNotificationListener</pre>

    <p>
      Readers can opt-out of these e-mails on a per blog entry basis by clicking the opt-out link in the e-mail.
      This will remove their e-mail address from any comments that they have made on that blog entry so they will
      not receive any more e-mails when further comments are added.
    </p>

    <h3>Comment moderation and spam</h3>
    <p>
      For more information about how to deal with comment spam, please see <a href="./help/responseSpam.html">Comment and TrackBack Spam</a>.
    </p>
  </div>
</div>