<div class="contentItem">
  <h1>Help</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      This is the online help for Pebble and the pages within here contain lots of information
      about the many aspects of running your blog. Should you need additional help and support, this
      is available via the pebble-users@lists.sourceforge.net mailing list. Details on subscribing
      to this list can be found <a href="http://lists.sourceforge.net/mailman/listinfo/pebble-user">on SourceForge</a>.
      Comments and feedback about this documentation are also welcomed.
    </p>

    <c:if test='${blogType == "singleblog"}'>
      <table width="99%" cellspacing="0" cellpadding="8">
        <tr>
          <td width="50%" valign="top" align="left">
              <h3>Using Pebble</h3>
              <ol>
                <li><a href="./help/configuration.html">Configuring your blog</a></li>
                <li><a href="./help/blogEntries.html">Blog Entries</a></li>
                <li><a href="./help/categories.html">Categories</a></li>
                <li><a href="./help/tags.html">Tags</a></li>
                <li><a href="./help/images.html">Images</a></li>
                <li><a href="./help/files.html">Files</a></li>
                <li><a href="./help/comments.html">Comments</a></li>
                <li><a href="./help/trackbacks.html">TrackBacks</a></li>
                <li><a href="./help/commentAndTrackbackSpam.html">Comment and TrackBack Spam</a></li>
                <li><a href="./help/staticPages.html">Static Pages</a></li>
                <li><a href="./help/newsfeeds.html">Newsfeeds (RSS and Atom)</a></li>
                <li><a href="./help/referers.html">Referers</a></li>
                <li><a href="./help/logs.html">Logs</a></li>
                <li><a href="./help/xmlrpcUpdatePings.html">XML-RPC Update Pings</a></li>
                <li><a href="./help/bloggerApi.html">Blogger API</a></li>
                <li><a href="./help/metaWeblogApi.html">MetaWeblog API</a></li>
                <li><a href="./help/themes.html">Themes</a></li>
                <li><a href="./help/newsfeedIntegration.html">Newsfeed integration</a></li>
                <li><a href="./help/securityRoles.html">Blog Security</a></li>
                <li><a href="./help/privateBlogs.html">Private blogs</a></li>
<%--
            <li><a href="team-blogs.html">Multi-contributor and Team Blogs</a></li>
--%>
              </ol>
          </td>
          <td width="50%" valign="top">
              <h3>Plugins</h3>
              <ol>
                <li><a href="./help/plugins.html">Plugins</a></li>
                <li><a href="./help/contentDecorators.html">Content Decorators</a></li>
                <li><a href="./help/permalinkProviders.html">Permalink Providers</a></li>
                <li><a href="./help/blogListeners.html">Blog Listeners</a></li>
                <li><a href="./help/blogEntryListeners.html">Blog Entry Listeners</a></li>
                <li><a href="./help/commentListeners.html">Comment Listeners</a></li>
                <li><a href="./help/trackbackListeners.html">TrackBack Listeners</a></li>
                <li><a href="./help/confirmationStrategies.html">Confirmation Strategies</a></li>
              </ol>
              <h3>Extending Pebble</h3>
              <ol>
                <li><a href="./help/developers.html">Developer Notes</a></li>
                <li><a href="./help/customTags.html">Custom tag reference</a></li>
                <li><a href="./help/writingContentDecorators.html">Writing Content Decorators</a></li>
                <li><a href="./help/writingPermalinkProviders.html">Writing Permalink Providers</a></li>
                <li><a href="./help/writingBlogListeners.html">Writing Blog Listeners</a></li>
                <li><a href="./help/writingBlogEntryListeners.html">Writing Blog Entry Listeners</a></li>
                <li><a href="./help/writingCommentListeners.html">Writing Comment Listeners</a></li>
                <li><a href="./help/writingConfirmationStrategies.html">Writing Confirmation Strategies</a></li>
                <li><a href="./help/writingTrackBackListeners.html">Writing TrackBack Listeners</a></li>
              </ol>
          </td>
        </tr>
      </table>
    </c:if>

    <table width="99%" cellspacing="0" cellpadding="8">
      <tr>
        <c:if test="${blogManager.multiBlog}">
        <td width="50%" valign="top">
            <h3>Multi-blog Features</h3>
            <ol>
              <li><a href="./help/multiBlogOverview.html">Multi-blog overview</a></li>
              <li><a href="./help/multiBlogSecurity.html">Multi-blog security</a></li>
            </ol>
        </td>
        </c:if>
        <pebble:isBlogAdmin>
        <td width="50%" valign="top" align="left">
            <h3>Pebble Administration</h3>
            <ol>
              <li><a href="./help/multiBlogConfiguration.html">Configuring Pebble</a></li>
              <li><a href="./help/managingBlogs.html">Managing Blogs</a></li>
              <li><a href="./help/managingUsers.html">Managing Users</a></li>
            </ol>
        </td>
        </pebble:isBlogAdmin>
      </tr>
    </table>
  </div>

</div>