<div class="contentItem">
  <h1>Confirmation Strategies</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Confirmation Strategies are a type of Pebble plugin that can be used to better assert that somebody
      leaving a comment or trying to send a TrackBack is human, as opposed to an automated comment spam agent. It does this by providing a pluggable
      strategy for asking readers to confirm their action by means of clicking a button or through some other kind of CAPTCHA.
    </p>

    <a name="commentProcess"></a>
    <h3>Confirmation process for comments</h3>
    <p>
      Confirmation fits into the overall process of leaving a comment as follows.
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

    <a name="trackbackProcess"></a>
    <h3>Confirmation process for TrackBacks</h3>
    <p>
      Confirmation fits into the overall process of sending a TrackBack as follows.
    </p>

    <ol>
      <li>The reader finds the blog entry they want to send a TrackBack and clicks the TrackBack link.</li>
      <li>If Pebble determines that confirmation is required, the reader will be redirected to a page where they will be asked to confirm that they want to generate a TrackBack link, using the strategy configured for the blog.</li>
      <li>Only successful confirmation will result in the generation of a single use TrackBack link that the reader can use to send a TrackBack to the blog entry.</li>
    </ol>

    <h3>Prebuilt confirmation strategies</h3>
    <p>
      The following comment confirmation strategies are included with the Pebble distribution.
    </p>

    <p>
      <b>net.sourceforge.pebble.confirmation.DefaultConfirmationStrategy</b><br/>
      Asks the reader to confirm their action by clicking a button. This is the simplest strategy but also
      one that automated spambots can easily work around.
    </p>

    <p>
      <b>net.sourceforge.pebble.confirmation.SimpleMathsConfirmationStrategy</b><br/>
      Asks the reader to confirm their action by answering a simple maths question based on the
      addition, subtraction or multiplication of two numbers between 1 and 10.
    </p>

    <p>
      <b>net.sourceforge.pebble.confirmation.ImageCaptchaConfirmationStrategy</b><br/>
      Asks the reader to confirm their action by entering the text they see in a distorted image.
    </p>

    <a name="noOpConfirmationStrategy"></a>
    <p>
      <b>net.sourceforge.pebble.confirmation.NoOpConfirmationStrategy</b><br/>
      A strategy that doesn't require comments/TrackBacks to be confirmed. Use this if you want to leave comment/TrackBack spam
      detection to the set of configured comment listeners instead of introducing a manual step in the process. Also use this strategy
      if you intend to enable TrackBack Auto-Discovery.
    </p>

    <h3>Configuring the comment confirmation strategy</h3>
    <p>
      To configure the comment confirmation strategy used by your blog, simply modify the entry on the <a href="viewPlugins.secureaction#commentConfirmationStrategy">Plugins</a> page. Your blog is using the
      following strategy.
    </p>
    <pre class="codeSample">${blog.commentConfirmationStrategy['class'].name}</pre>

    <h3>Configuring the TrackBack confirmation strategy</h3>
    <p>
      To configure the TrackBack confirmation strategy used by your blog, simply modify the entry on the <a href="viewPlugins.secureaction#trackBackConfirmationStrategy">Plugins</a> page. Your blog is using the
      following strategy.
    </p>
    <pre class="codeSample">${blog.trackBackConfirmationStrategy['class'].name}</pre>
  </div>
</div>