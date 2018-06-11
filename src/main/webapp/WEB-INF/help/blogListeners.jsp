<div class="contentItem">
  <h1>Blog Listeners</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Blog listeners are a type of Pebble plugin that allows custom code to be called
      whenever your blog is started and stopped. Example uses include:
    </p>

    <ul>
      <li>Initialising/flushing a cache when the blog is started/stopped.</li>
      <li>Opening/closing a connection to an external data source that is used by another type of Pebble plugin.</li>
      <li>Starting/stopping a thread that subscribes to an RSS feed and automatically posts new content to your blog.</li>
    </ul>

    <p>
      There are currently no user configurable implementations distributed with Pebble.
    </p>

    <h3>Configuring blog listeners</h3>
    <p>
      To configure the blog listeners used by your blog, modify the list on the <a href="viewPlugins.secureaction#blogListeners">Plugins</a> page. Your blog is using the
      following blog listeners.
    </p>
    <pre class="codeSample"><c:forEach var="listener" items="${blog.eventListenerList.blogListeners}">${listener['class'].name}<br /></c:forEach></pre>
  </div>
</div>