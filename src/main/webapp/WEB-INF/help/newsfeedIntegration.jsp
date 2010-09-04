<div class="contentItem">
  <h1>Newsfeed Integration</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Pebble allows you to integrate newsfeeds from other websites into your own blog. For example, this will allow you to
      include your most recent <a href="http://del.icio.us">del.icio.us</a> bookmarks or entries from <a href="http://www.twitter.com">Twitter</a>. There are two ways to do this.
    </p>

    <h3>Sidebar Integration</h3>
    <p>
      To integrate an RSS feed into the sidebar of your blog, simply add the following into your theme.
    </p>

    <pre class="codeSample">&lt;sidebar:feed name="A title" url="A newsfeed URL" maxEntries="3" showBody="true" truncateBody="true"/&gt;</pre>

    <p>
      Just set the <code>name</code> and <code>url</code> properties as appropriate. The sidebar component can be customised through the use of the other properties (see <a href="./help/themes.html#sidebar-feed">Themes</a> for more details).
    </p>

    <h3>General Integration</h3>
    <p>
      You can integrate a newsfeed anywhere within your theme, by using the following code as a template.
    </p>

    <pre class="codeSample">&lt;pebble:getFeed url="A newsfeed URL"/&gt;
&lt;c:if test="&#0036;{not empty feedEntries}"&gt;
  &lt;c:forEach var="entry" items="&#0036;{feedEntries}"&gt;
    &lt;a href="&#0036;{entry.link}"&gt;&#0036;{entry.title}&lt;/a&gt;
    &lt;br /&gt;
    &#0036;{entry.body} or &#0036;{entry.truncatedBody}
    &lt;br /&gt;&lt;br /&gt;
  &lt;/c:forEach&gt;
&lt;/c:if&gt;</pre>

  </div>
</div>