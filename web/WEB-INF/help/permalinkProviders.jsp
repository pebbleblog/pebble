<div class="contentItem">
  <h1>Permalink Providers</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Permalink Providers are a type of Pebble plugin that changes the way
      permalinks are generated and handled. The following implementations are included in the distribution.
    </p>

    <p>
      <b>net.sourceforge.pebble.permalink.DefaultPermalinkProvider</b><br />
      Generates permalinks of the form /yyyy/mm/dd/time-in-millis.html (this is the default)
      <br /><br />
      <b>net.sourceforge.pebble.permalink.ShortPermalinkProvider</b><br />
      Generates permalinks of the form /time-in-millis.html
      <br /><br />
      <b>net.sourceforge.pebble.permalink.TitlePermalinkProvider</b><br />
      Generates permalinks of the form /yyyy/mm/dd/each_word_in_the_title.html
    </p>

    <p>
      By default, Pebble will use the <code>DefaultPermalinkProvider</code>. To change this,
      modify the permalink provider on the <a href="viewPlugins.secureaction#permalinkProvider">Plugins</a> page.
      If another is chosen, Pebble will additionally use the <code>DefaultPermalinkProvider</code> as a fallback, which means you can change
      your permalink provider and retain backwards compatibility with permalinks that have already been published.
    </p>
  </div>
</div>