<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/configuration.html#security" target="_blank">Help</a>
  </div>

  <h1>Blog security</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Highlight those users that you would like to be <a href="./help/securityRoles.html" target="_blank">owners, publishers and contributors</a> for your blog. Optionally, you can
      <a href="./help/privateBlogs.html" target="_blank">restrict access to your blog</a> to only those readers that you specify.
    </p>

    <form name="securityForm" action="saveBlogSecurity.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
      <table width="99%">
        <tr>
          <td align="center">Blog owners</td>
          <td align="center">Blog publishers</td>
        </tr>
        <tr>
          <td align="center"><pebble:select name="blogOwners" items="${blogOwnerUsers}" selected="${blog.blogOwners}" label="name" value="username" size="10" multiple="true" /></td>
          <td align="center"><pebble:select name="blogPublishers" items="${blogPublisherUsers}" selected="${blog.blogPublishers}" label="name" value="username" size="10" multiple="true" /></td>
        </tr>

        <tr>
          <td align="center">Blog readers (<span class="help"><a href="./help/privateBlogs.html" target="_blank">Help</a></span>)</td>
        </tr>
        <tr>
          <td align="center"><pebble:select name="blogContributors" items="${blogContributorUsers}" selected="${blog.blogContributors}" label="name" value="username" size="10" multiple="true" /></td>
          <td align="center"><pebble:select name="blogReaders" items="${allUsers}" selected="${blog.blogReaders}" label="name" value="username" size="10" multiple="true" /></td>
        </tr>

        <tr>
          <td align="right" colspan="2">
            <input name="submit" type="submit" Value="Save">
          </td>
        </tr>

      </table>
    </form>
  </div>

</div>