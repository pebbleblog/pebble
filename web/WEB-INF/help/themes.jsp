<div class="contentItem">
  <h1>Themes</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    Pebble supports the notion of themes so that you can personalize the look and feel of your blog. By default, your blog will use the standard Pebble theme called "default" and you can change this in the <a href="viewBlogProperties.secureaction">Blog properties</a> page.
    <pebble:isPebbleAdmin>All themes can be found in the <code>themes</code> directory underneath <code>${pebbleContext.webApplicationRoot}</code>.</pebble:isPebbleAdmin>
    </p>

    <h3>Editing your own theme</h3>
    <p>
      Each blog has its own custom theme that can be edited by the owner of that blog. The name of your editable theme is <code>${blog.editableTheme.name}</code>, which was automatically created by Pebble the first time your blog started up. To edit your custom theme, click <a href="./theme/">Theme</a>.
      From this page you can edit the CSS and JSP files that your theme contains.
    </p>

    <p>
      The themes that are distributed with Pebble all follow the same basic layout.
    </p>

    <ul>
      <li>screen.css : the stylesheet.</li>
      <li>images : the directory containing images used by the theme.</li>
      <li>jsp : the directory containing the JSPs used to generate HTML.</li>
    </ul>

    <p>
      Underneath the <code>jsp</code> directory are the following JSP pages.
    </p>

    <ul>
      <li>sidebar.jsp : the sidebar that includes the calendar, recent blog entries, links, etc.</li>
      <li>template.jsp : the main template into which all other content is placed.</li>
    </ul>

    <h3>Variables</h3>
    <p>
      Using the JSP expression language, you have access to the following variables within your own theme.
    </p>
    <ul>
      <li>blog : your blog, an instanceof <a href="">Blog</a></li>
      <li>pebbleContext : an instanceof <a href="">Blog</a></li>
      <li>categories : the list of <a href="">Category</a> objects</li>
      <li>tags : the list of <a href="">Tag</a> objects</li>
      <li>recentBlogEntries : the list of recent <a href="">BlogEntry</a> objects</li></li>
      <li>recentResponses : the list of <a href="">Response</a> objects</li></li>
    </ul>
  </div>
</div>