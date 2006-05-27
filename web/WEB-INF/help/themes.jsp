<div class="contentItem">
  <div class="title">Themes</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <p>
    Pebble supports the notion of themes so that you can personalize the look and feel of your blog. The following images show examples of how Pebble can be made to look different without a great deal of effort.
    </p>

    <p>
    <h5>Selecting a Theme</h5>
    By default, your blog will use the standard Pebble theme called "default" and you can change this in the <a href="vieBlogProperties.secureaction">Blog properties</a> page.
    <pebble:isPebbleAdmin>All themes can be found in the <code>themes</code> underneath <code>${pebbleContxet.webApplicationRoot}</code>.</pebble:isPebbleAdmin>
    </p>

    <p>
    <h5>Editing your own Theme</h5>
    Although Pebble is distributed with some pre-built themes, you may want to create your own. The way that Pebble allows you to do this is by providing a theme called <b>custom</b> (in single user mode) that you can edit via your web browser. This theme is created automatically by Pebble the first time that it is started. To edit your custom theme, click the <b>Theme (custom)</b> link after logging in.
    </p>

    <p>
    From this page you can edit any of the resources (e.g. JSP, HTML or CSS) that your theme contains. To edit a particular resource, just navigate to it from the list and click the <b>Edit</b> link. When you have made the relevant changes, click the <b>Save File</b> button. The changes should be reflected immediately on your blog.
    </p>

    <p>
    The themes that are distributed with Pebble all follow the same basic layout in that they all contain the following elements on disk.
    <ul>
      <li><b>theme.css</b> : the theme stylesheet</li>
      <li><b>images</b> : the directory containing images used by the theme</li>
      <li><b>jsp</b> : the directory containing the JSPs used to generate HTML</li>
    </ul>
    Of course, this structure isn't mandatory and can be changed in your own theme implementations if you wish, with the exception that the <code>jsp/template.jsp</code> page must exist. Underneath the <code>jsp</code> directory are
    the following JSP pages.
    <ul>
      <li><b>blogEntries.jsp</b> : renders a collection of BlogEntries, calling blogEntry.jsp for each blog entry</li>
      <li><b>blogEntry.jsp</b> : renders a single blog entry</li>
      <li><b>comment.jsp</b> : renders a single comment</li>
      <li><b>comments.jsp</b> : renders a collection of comments, calling comment.jsp for each comment</li>
      <li><b>sidebar.jsp</b> : the sidebar that includes the calendar, recent blog entries, links, etc</li>
      <li><b>staticPage.jsp</b> : renders a single static page (similar to blogEntry.jsp)</li>
      <li><b>template.jsp</b> : the main template into which all other content is placed</li>
      <li><b>trackBack.jsp</b> : renders a single TrackBack</li>
      <li><b>trackBacks.jsp</b> : renders a collection of TrackBacks, calling trackBack.jsp for each TrackBack</li>
    </ul>
    </p>

    <p>
    Most Pebble users find that they only need to modify the contents of theme.css, template.jsp and sidebar.jsp to produce their own unique theme and this
    is the recommended approach. By doing this, upgrading to a newer version of Pebble is more straightforward because you only need to edit these files
    again to recreate your existing theme while taking advantage of any new functionality that has been delivered elsewhere in the JSP components.
    </p>

    <p>
    You can also upload new files and delete existing files from your theme. To upload an image, the procedure is the same as for uploading <a href="images.jsp">images</a> and files to your blog. In your theme pages, The <code>&lt;pebble:theme/&gt;</code> tag can be used to represent the URI of the current theme and this is used for referencing images and other resources (see included themes for examples).
    </p>

    <p>
    <h5>Storage of your Theme</h5>
    When Pebble starts up, it restores your theme from the <code>blog.dir/theme</code> directory to the <code>themes/custom</code> directory of the webapp and when Pebble is running, any changes to your theme are also made to the <code>blog.dir/theme</code> directory. The benefit of this is that you don't have to copy your custom theme between web applications when upgrading Pebble.
    </p>
  </div>
</div>