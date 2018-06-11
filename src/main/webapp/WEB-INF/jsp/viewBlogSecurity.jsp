<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/securityRoles.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.blogSecurity"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Highlight those users that you would like to be <a href="./help/securityRoles.html" target="_blank">owners, publishers and contributors</a> for your blog. Optionally, you can
      <a href="./help/privateBlogs.html" target="_blank">restrict access to your blog</a> to only those readers that you specify.
    </p>

    <form name="securityForm" action="saveBlogSecurity.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
      <pebble:token/>
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
          <td align="center">Blog contributors</td>
          <td align="center">Blog readers (<span class="help"><a href="./help/privateBlogs.html" target="_blank">Help</a></span>)</td>
        </tr>
        <tr>
          <td align="center"><pebble:select name="blogContributors" items="${blogContributorUsers}" selected="${blog.blogContributors}" label="name" value="username" size="10" multiple="true" /></td>
          <td align="center"><pebble:select name="blogReaders" items="${allUsers}" selected="${blog.blogReaders}" label="name" value="username" size="10" multiple="true" /></td>
        </tr>

        <c:if test="${blogManager.multiBlog}">
        <tr>
          <td colspan="2">
            <br />
            Do you want this blog included in the multi-blog home page and news feeds?
            <br />
            Yes<input type="radio" name="private" value="false"
              <c:if test="${blog['public']}">
                checked="checked"
              </c:if>
            />
            No<input type="radio" name="private" value="true"
              <c:if test="${blog['private']}">
                checked="checked"
              </c:if>
            />
          </td>
        </tr>
        </c:if>

        <tr>
          <td align="right" colspan="2">
            <input name="submit" type="submit" Value="Save">
          </td>
        </tr>

      </table>
    </form>
  </div>

</div>
