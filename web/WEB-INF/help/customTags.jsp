<div class="contentItem">
  <h1>Custom Tags</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Pebble includes a custom tag library that can be used in customizing your own theme. To import it, use the following directive in your JSP.
    </p>

    <pre class="codeSample">&lt;%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %&gt;</pre>

    <p>
      A summary of the tags within this tag library is as follows.
    </p>

    <table width="100%" cellspacing="0" cellpadding="8">
      <tr>
        <td><b>Tag</b></td>
        <td><b>Description</b></td>
      </tr>

      <tr class="odd">
        <td valign="top">calendar</td>
        <td>Builds a calendar control for use on the blog pages.</td>
      </tr>

      <tr class="even">
        <td valign="top">isBlogAdmin</td>
        <td>Includes the body content only if the current user is a Pebble admin.</td>
      </tr>

      <tr class="odd">
        <td valign="top">isBlogOwner</td>
        <td>Includes the body content only if the current user is a blog owner of the current blog.</td>
      </tr>

      <tr class="even">
        <td valign="top">isBlogPublisher</td>
        <td>Includes the body content only if the current user is a blog publisher of the current blog.</td>
      </tr>

      <tr class="odd">
        <td valign="top">isNotBlogOwner</td>
        <td>Includes the body content only if the current user is not a blog owner of the current blog.</td>
      </tr>

      <tr class="even">
        <td valign="top">isBlogContributor</td>
        <td>Includes the body content only if the current user is a blog contributor of the current blog.</td>
      </tr>

      <tr class="odd">
        <td valign="top">isAuthorisedForBlog</td>
        <td>Includes the body content only if the current user authorised for the current blog.</td>
      </tr>

      <tr class="even">
        <td valign="top">isUserAuthenticated</td>
        <td>Includes the body content only if the current user has been authenticated.</td>
      </tr>

      <tr class="odd">
        <td valign="top">isUserUnauthenticated</td>
        <td>Includes the body content only if the current user has not been authenticated.</td>
      </tr>
    </table>

  </div>
</div>