<div class="contentItem">
  <div class="title">Themes</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <p>
    Pebble supports the notion of themes so that you can personalize the look and feel of your blog. By default, your blog will use the standard Pebble theme called "default" and you can change this in the <a href="viewBlogProperties.secureaction">Blog properties</a> page.
    <pebble:isPebbleAdmin>All themes can be found in the <code>themes</code> directory underneath <code>${pebbleContext.webApplicationRoot}</code>.</pebble:isPebbleAdmin>
    </p>

    <div class="subsubtitle">Editing your own theme</div>
    <p>
      Each blog has its own custom theme that can be edited by the owner of that blog. The name of your editable theme is ${blog.editableTheme.name}, which was automatically created by Pebble the first time your blog started up. To edit your custom theme, click <a href="./theme/">Theme</a>.
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
  </div>
</div>