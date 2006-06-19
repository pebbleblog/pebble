<div class="contentItem">
  <div class="title">Decorators</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <p>
      Decorators are a type of Pebble plugin that provides a way to change the way that
      blog entries, static pages, comments and TrackBacks are displayed in the HTML pages of your blog and the XML newsfeeds that are
      generated. The following pre-built decorators are included in the distribution.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.HideUnapprovedBlogEntriesDecorator</b><br />
      Hides blog entries that do not have a status of approved, unless you are logged in as a blog owner or contributor.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.HideUnapprovedResponsesDecorator</b><br />
      Hides comments and TrackBacks that do not have a status of approved, unless you are logged in as a blog owner or contributor.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.HtmlDecorator</b><br />
      Filters the HTML inside comments/TrackBacks to use only a limited safe subset and allows blog entries to be rendered as HTML.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.RadeoxDecorator</b><br />
      Renders content inside blog entry/static pages between <code>&lt;wiki&gt;</code>...<code>&lt;/wiki&gt;</code> tags using the Radeox library.
      <br /><br />
      Internal style wiki links (e.g. <code>[aboutme]</code>) link to the static page with the same name. If the static page doesn't exist, the link lets you create the page.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.EscapeMarkupDecorator</b><br />
      Escapes HTML markup between <code>&lt;escape&gt;</code>...<code>&lt;/escape&gt;</code> tags (like <a href="http://www.simongbrown.com/blog/2004/05/05/1083760455000.html">this</a> except that the tags are maintained and simply decorated out at runtime). This is useful where you
      would like to post large fragments of HTML, JSP, XML or Java code.
      In doing so, any &lt; or &gt; characters will be automatically escaped to &amp;lt; and &amp;gt; respectively.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.RelativeUriDecorator</b><br />
      Replaces relative URIs with absolute URLs so that they are rendered properly in browsers and newsreaders.
      <br /><br />
      For example, a link to <code>./images/someimage.jpg</code> would be transformed to <code>http://www.yourdomain.com/blog/images/someimage.jpg</code>.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.NoFollowDecorator</b><br />
      Provides support for the <a href="http://www.google.com/googleblog/2005/01/preventing-comment-spam.html">nofollow initiative</a> by modifying all links in comments and TrackBacks.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.BlogTagsDecorator</b><br />
      Adds links from your blog entry back to your tags.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.TechnoratiTagsDecorator</b><br />
      Adds links from your blog entry back to tags on <a href="http://technorati.com/tags/">Technorati</a>.
    </p>

    <p>
      <b>net.sourceforge.pebble.plugin.decorator.DisableResponseDecorator</b><br />
      Disables comments and TrackBacks for the blog entry. This is useful if you are worried about spam when you don't have access to your blog.
      <br /><br />
      Also available are net.sourceforge.pebble.plugin.decorator.DisableCommentsDecorator and net.sourceforge.pebble.plugin.decorator.DisableTrackBacksDecorator that disable comments
      and TrackBacks, respectively.
    </p>

    <div class="subsubtitle">Configuring decorators</div>
    <p>
      To configure the decorators that your blog uses, modify the list of decorators on the <a href="viewPlugins.secureaction#decorators">Plugins</a> page.
    </p>
  </div>
</div>