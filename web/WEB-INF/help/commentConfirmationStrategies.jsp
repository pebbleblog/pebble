<div class="contentItem">
  <h1>Comment Confirmation Strategies</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Comment Confirmation Strategies are a type of Pebble plugin that can be used to better assert that somebody
      leaving a comment is human, as opposed to an automated comment spam agent. It does this by providing a pluggable
      strategy for asking readers to confirm their comments by means of clicking a button or through some other kind of CAPTCHA.
    </p>

    <h3>Comment confirmation process</h3>
    <p>
      Comment confirmation fits into the overall process of leaving a comment as follows.
    </p>

    <ol>
      <li>The reader clicks a link to add (or reply to an existing) comment and fills in the comment form as usual.</li>
      <li>
        On submission, Pebble looks at the comment to determine whether comment confirmation is required, based upon the following rules.
        <ol>
          <li>If the user is logged in and is a blog owner/contributor for the blog, they are implicitly trusted and confirmation is not required.</li>
          <li>Pebble then performs the processing associated with a subset of the default comment listeners (IpAddressListener, LinkSpamListener, ContentSpamListener and SpamScoreListener) to determine whether the comment is spam. If, after running these listeners, the comment is marked as pending or rejected, comment confirmation is required. If the comment is still marked as approved, comment confirmation is not required.</li>
        </ol>
      </li>
      <li>If Pebble determines that confirmation is required, the reader will be redirected to a page where they will be asked to confirm their comment, using the strategy configured for the blog.</li>
      <li>Only successful confirmation will result in the comment being accepted and stored.</li>
    </ol>

    <p>
      To override the functionality outlined in step 2.2, add a <a href="viewPlugins.secureaction#properties">plugin property</a> called <code>CommentConfirmationStrategy.required</code> and set the value to <code>true</code>.
    </p>

    <p>
      The following comment confirmation strategies are included with the Pebble distribution.
    </p>

    <p>
      <b>net.sourceforge.pebble.comment.DefaultCommentConfirmationStrategy</b><br/>
      Asks the reader to confirm their comment by clicking a button. This is the simplest strategy but also
      one that comment spammers can easily work around.
    </p>

    <p>
      <b>net.sourceforge.pebble.comment.SimpleMathsCommentConfirmationStrategy</b><br/>
      Asks the reader to confirm their comment by answering a simple maths question based on the
      addition, subtraction or multiplication of two numbers between 1 and 10.
    </p>

    <p>
      <b>net.sourceforge.pebble.comment.ImageCaptchaCommentConfirmationStrategy</b><br/>
      Asks the reader to confirm their comment by entering the text they see in a distorted image.
    </p>

    <p>
      <b>net.sourceforge.pebble.comment.NoOpCommentConfirmationStrategy</b><br/>
      A strategy that doesn't require comments to be confirmed. Use this if you want to leave comment spam
      detection to the set of configured comment listeners and not ask readers to confirm their comments.
    </p>

    <h3>Configuring the comment confirmation strategy</h3>
    <p>
      To configure the comment confirmation strategy used by your blog, simply modify the entry on the <a href="viewPlugins.secureaction#commentConfirmationStrategy">Plugins</a> page. Your blog is using the
      following strategy.
    </p>
    <pre class="codeSample">${blog.commentConfirmationStrategy.class.name}</pre>
  </div>
</div>