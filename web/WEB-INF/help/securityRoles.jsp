<div class="contentItem">
  <h1>Blog Security</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      By default, when Pebble is hosting multiple blogs, all users defined as blog contributors will be able to contribute to all blogs. To prevent this from happening,
      you can explicitly specify the users that are permitted to have access to each blog. To do this, simply go to the <a href="viewBlogSecurity.secureaction">blog security</a>
      page and highlight the users that you would like in the following roles.
    </p>
    <table border="0">
      <tr><td width="33%" valign="top">Blog owners</td><td valign="top">A list of usernames for those users that are blog owners for this blog. This is used when Pebble runs in multi-user mode.</td></tr>
      <tr><td width="33%" valign="top">Blog publishers</td><td valign="top">A list of usernames for those users that are blog publishers for this blog. This is used when Pebble runs in multi-user mode.</td></tr>
      <tr><td width="33%" valign="top">Blog contributors</td><td valign="top">A list of usernames for those users that are blog contributors for this blog. This is used when Pebble runs in multi-user mode.</td></tr>
    </table>

    <p>
      Optionally, you can <a href="./help/privateBlogs.html">mark the whole blog to be private</a>
      <c:if test="${blogManager.multiBlog}">and you can also state whether you want your blog included in the <a href="${pageContext.request.contextPath}" target="_blank">aggregated home page</a> and newsfeeds</c:if>.
    </p>

    <h3>Security Roles</h3>
    <p>
      The following table shows the privileges assigned to each of the security roles.
    </p>

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th align="left">Function</th>
          <th align="center">Admin</th>
          <th align="center">Owner</th>
          <th align="center">Publisher</th>
          <th align="center">Contributor</th>
          <th align="center">Reader</th>
        </tr>
      </thead>
      <tbody>
        <tr class="odd">
          <td>Manage Pebble properties (multi-blog only)</td>
          <td align="center">X</td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td>
        </tr>
        <tr class="even">
          <td>Add blog (multi-blog only)</td>
          <td align="center">X</td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td>
        </tr>
        <tr class="odd">
          <td>Manage blog properties, plugins and theme</td>
          <td align="center">X</td><td align="center" class="even">X</td><td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td>
        </tr>
        <tr class="even">
          <td>Export blog</td>
          <td align="center">X</td><td align="center" class="even">X</td><td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td>
        </tr>
        <tr class="odd">
          <td>View logs and log summaries</td>
          <td align="center">X</td><td align="center" class="even">X</td><td align="center">X</td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>
        <tr class="even">
          <td>Add/remove referer filter</td>
          <td align="center">X</td><td align="center" class="even">X</td><td align="center">X</td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>

        <tr class="odd">
          <td>Add/edit/remove/clone blog entry</td>
          <td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>
        <tr class="even">
          <td>Publish/unpublish blog entry</td>
          <td align="center"> </td><td align="center" class="even"> </td><td align="center">X</td><td align="center" class="even"> </td><td align="center"> </td>
        </tr>
        <tr class="odd">
          <td>Add/edit/remove category</td>
          <td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>
        <tr class="even">
          <td>Add/edit/remove static page</td>
          <td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>
        <tr class="odd">
          <td>Manage images</td>
          <td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>
        <tr class="even">
          <td>Manage files</td>
          <td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>
        <tr class="odd">
          <td>Approve/Reject/Remove responses</td>
          <td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even">X</td><td align="center"> </td>
        </tr>
        <tr class="even">
          <td>Modify user details/change password<br />(if permitted when user is created)</td>
          <td align="center">X</td><td align="center" class="even">X</td><td align="center">X</td><td align="center" class="even">X</td><td align="center">X</td>
        </tr>
        <tr class="odd">
          <td>Add/edit/remove user</td>
          <td align="center">X</td><td align="center" class="even"> </td><td align="center"> </td><td align="center" class="even"> </td><td align="center"> </td>
        </tr>
      </tbody>
    </table>

  </div>
</div>