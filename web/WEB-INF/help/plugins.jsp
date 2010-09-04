<div class="contentItem">
  <h1>Plugins</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Plugins are a way to extend the functionality of Pebble in a structured way. The list of supported plugin APIs is as follows.
    </p>

    <ul>
      <li><a href="./help/contentDecorators.html">Content Decorators</a></li>
      <li><a href="./help/permalinkProviders.html">Permalink Providers</a></li>
      <li><a href="./help/blogListeners.html">Blog Listeners</a></li>
      <li><a href="./help/blogEntryListeners.html">Blog Entry Listeners</a></li>
      <li><a href="./help/commentListeners.html">Comment Listeners</a></li>
      <li><a href="./help/trackbackListeners.html">TrackBack Listeners</a></li>
      <li><a href="./help/confirmationStrategies.html">Confirmaton Strategies</a></li>
    </ul>

    <p>
      With all types of plugins, to use your own implementation, you must add your compiled class(es) to the classpath of the Pebble web application. To do this,
      either place the class file(s) underneath the <code>/WEB-INF/classes</code> directory or inside a JAR file
      placed underneath the <code>/WEB-INF/lib</code> directory.
    </p>

    <blockquote>If you've added
    new classes to the web application classpath, you may need to restart the web application so that they are
    picked up by the servlet container.</blockquote>

    <p>
      Next, open the <a href="viewPlugins.secureaction">Plugins</a> page
      and enter the fully qualified class name in the appropriate place. This will either be a textfield in which a single class name
      can be specified, or a textarea containing a whitespace (i.e. space or carriage return) separated list of class
      names. Where more than a single class name can be specified, classes are called in the order they are listed.
    </p>

    <a name="properties"></a><h3>Plugin properties</h3>
    <p>
      At the bottom of the plugins page is a textarea into which plugin properties can be entered. These are
      user configurable and vary from plugin to plugin. Each property is a name=value pair on a new line.
    </p>

    <h3>Current configuration</h3>
    <p>
      To see the list of plugins and properties that are currently active for your blog, open the <a href="aboutBlog.secureaction">About</a> page.
    </p>
  </div>
</div>