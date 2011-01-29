<div class="contentItem">
  <h1>Themes</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    Pebble supports the notion of themes so that you can personalize the look and feel of your blog. By default, your blog will use the standard Pebble theme called "default" and you can change this in the <a href="viewBlogProperties.secureaction">Blog properties</a> page.
    <pebble:isBlogAdmin>All themes can be found in the <code>themes</code> directory underneath <code>${pebbleContext.webApplicationRoot}</code>.</pebble:isBlogAdmin>
    </p>

    <c:if test="${not pebbleContext.configuration.userThemesEnabled}">
      <blockquote>
        Pebble is currently running with user themes disabled so you cannot use or edit your own, blog specific theme.
      </blockquote>
    </c:if>

    <h3>Theme structure</h3>
    <p>
      The themes that are distributed with Pebble all follow the same basic layout.
    </p>

    <ul>
      <li>screen.css : the stylesheet.</li>
      <li>images : the directory containing images used by the theme.</li>
      <li>template.jsp : the main template (including header, body, footer, sidebar, etc).</li>
      <li>head.jsp : extensions to be added to the pebble head section.</li>
    </ul>

    <c:if test="${pebbleContext.configuration.userThemesEnabled}">
      <h3>Editing your own theme</h3>
      <p>
        Each blog has its own custom theme that can be edited by the owner of that blog. The name of your editable theme is <code>${blog.editableTheme.name}</code>, which was automatically created by Pebble the first time your blog started up. To edit your custom theme, click <a href="./theme/">Theme</a>.
        From this page you can edit the CSS and JSP files that your theme contains.
      </p>
    </c:if>

    <h3>Editing the template</h3>
    <p>
      The <code>template.jsp</code> file is a JSP page that contains XHTML and makes extensive
      use of JSP 2.0 tag files to provide a more component based approach to page composition. Instead of
      seeing lots of JSP includes, you see references to components. Aside from the familiar
      XHTML <code>div</code> and <code>span</code> tags, the other tags fall under two namespaces.
    </p>

    <h3>template namespace</h3>
    <p>
      The <code>template</code> namespace represents the common components on the page and the set of available
      tags is as follows.
    </p>
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr><th>Tag</th><th>Description</th></tr>
      </thead>
      <tbody>
        <tr><td valign="top">page</td><td>The overall container for the entire page. Usage of this tag is mandatory in order for the appropriate XHTML structure to surround the page content.</td></tr>
        <tr><td valign="top">feeds</td><td>Represents the feed links and icons, as shown in the top right of the page of the default theme. The attributes for this tag are :<br />
           - showRSS : whether to show the RSS link (optional, default is true)<br />
           - showAtom : whether to show the Atom link (optional, default is false)
        </td></tr>
        <tr><td valign="top">linearNavigation</td><td>Represents the linear navigation links, as shown underneath the header of the default theme.</td></tr>
        <tr><td valign="top">content</td><td>The placeholder into which the main content of the page will be rendered.</td></tr>
        <tr><td valign="top">poweredByPebble</td><td>The "Powered by Pebble" text, as shown in the footer of the default theme.</td></tr>
      </tbody>
    </table>

    <h3>sidebar namespace</h3>
    <p>
      The <code>sidebar</code> namespace represents the available sidebar components, which are as follows.
    </p>
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr><th>Tag</th><th>Description</th></tr>
      </thead>
      <tbody>
        <tr><td valign="top">about</td><td>Displays the "about" information, as set on the <a href="viewBlogProperties.secureaction">blog properties</a> page.</td></tr>
        <tr><td valign="top">aboutAuthor</td><td>Displays the profile information associated with the author of a blog entry, when viewing that blog entry on its permalinked page.</td></tr>
        <tr><td valign="top">loginForm</td><td>Displays the login form.</td></tr>
        <tr><td valign="top">navigation</td><td>Displays the basic navigation mechanisms; including the calendar, search and links to category/tag pages.</td></tr>
        <tr><td valign="top">archivesByMonth</td><td>Displays month-by-month archive links.</td></tr>
        <tr><td valign="top">categories</td><td>Displays a list of category names and links, including the blog entry count for each.</td></tr>
        <tr><td valign="top">tagCloud</td><td>Displays a tag cloud, which is customizable using the following attributes<br />
          - rankThreshold : the minimum tag ranking (1-10) to display (optional, default is 1)</td></tr>
        <tr><td valign="top">recentBlogEntries</td><td>Displays the recent blog entries<br />
          - name : the name of this sidebar component (defaults to "Recent Blog Entries")<br />
          - showBody : flag to indicate whether the (truncated) body of the entry should be displayed (optional, default is true)</td></tr>
        <tr><td valign="top">recentResponses</td><td>Displays the recent responses (comments and TrackBacks).</td></tr>
        <tr><td valign="top">blogSummary</td><td>In multi-blog mode, displays a list of all blogs along with a link back to the multi-blog home page.</td></tr>
        <tr><td valign="top"><a name="sidebar-feed" />feed</td><td>Reads the specified feed and formats it for the sidebar.<br />
          - name : the name of the feed, which is displayed in the sidebar item title (required)<br />
          - url : the URL of the RSS/RDF/Atom feed (required)<br />
          - maxEntries : the maximum number of entries to display from this feed (required)<br />
          - showBody : flag to indicate whether the body of the entry should be displayed (optional, default is true)<br />
          - truncateBody : flag to indicate whether the body of the entry should be truncated (i.e. HTML stripped and truncated to 255 characters max) (optional, default is true)
        </td></tr>
        <tr><td valign="top">item</td><td>Generic sidebar item component.<br />
          - name : the name, displayed in the sidebar item title
        </td></tr>
      </tbody>
    </table>

    <h3>Variables</h3>
    <p>
      Using the JSP expression language, you have access to the following variables within your own template.
    </p>
    <ul>
      <li>blog : your blog, an instance of <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/Blog.html">Blog</a></li>
      <li>pebbleContext : an instance of <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/PebbleContext.html">PebbleContext</a></li>
      <li>categories : the list of <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/Category.html">Category</a> objects</li>
      <li>tags : the list of <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/Tag.html">Tag</a> objects</li>
      <li>recentBlogEntries : the list of recent <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/BlogEntry.html">BlogEntry</a> objects</li>
      <li>recentResponses : the list of <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/Response.html">Response</a> objects</li>
      <li>pluginProperties : the <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/PluginProperties.html">PluginProperties</a> for your blog</li>
      <li>archives : a list of <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/domain/Year.html">Year</a> objects representing the archives for your blog</li>
      <li>pluginProperties : a <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/PluginProperties.html">PluginProperties</a> objects representing the plugin properties for your blog</li>
      <li>authenticatedUser : a <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/security/PebbleUserDetails.html">PebbleUserDetails</a> object representing the currently logged on user</li>
    </ul>

    <h3>Troubleshooting</h3>
    <p>
      If you get into a mess with your theme (e.g. cause a JSP compilation error), there are a couple of things you can do.
      The first is to <a href="resetTheme.secureaction">reset your theme</a> back to the default theme, which is the same
      as simply selecting <code>default</code> from the theme dropdown on the blog properties page.
    </p>

    <p>
      Alternatively, you can <a href="restoreTheme.secureaction">restore your theme</a>, which provides you with a fresh copy of the default theme
      to work from. <b>Any changes you made prior to restoring your theme will be gone.</b>
    </p>
  </div>
</div>