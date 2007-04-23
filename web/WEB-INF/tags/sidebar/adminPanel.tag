<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<%--
  Displays the administration links that are available after logging in.
--%>
<pebble:isAuthenticated>
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span>Logged in as ${authenticatedUser.username} - <a href="./help/index.html" title="Help">Help</a></span></div>
  <div class="sidebarItemBody">
    <div id="adminPanel">
    <ul>

      <li>
        <c:if test="${authenticatedUser.detailsUpdateable == true}">
          <a href="editUserDetails.secureaction" title="Edit user details">User details</a> |
          <a href="changePassword.secureaction" title="Change password">Change password</a><br />
        </c:if>
        <a href="${pebbleContext.configuration.secureUrl}logout.action?redirectUrl=${blog.url}"><fmt:message key="common.logout" /></a>
      </li>

      <pebble:isBlogContributor>
      <li><b>Content</b>
      <br />
      <a href="addBlogEntry.secureaction#form" title="Add a new entry">New blog entry</a> (<fmt:formatNumber value="${blog.numberOfBlogEntries}"/>)
    <%--    |
      <a href="javascript:void(location.href='<c:out value="${blog.url}saveBlogEntry.secureaction"/>?title='+escape(document.title)+'&amp;body=%3Ca+href%3D%22'+escape(document.URL)+'%22%3E'+escape(document.title)+'%3C%2Fa%3E&amp;trackBacksEnabled=true&amp;commentsEnabled=true&amp;submit=Preview#form')" title="Add this bookmarklet to your favourites">Blog this</a>
      --%>
      <br />
      <a href="viewUnpublishedBlogEntries.secureaction" title="View unpublished blog entries">Unpublished blog entries</a> (<fmt:formatNumber value="${blog.numberOfUnpublishedBlogEntries}"/>)
      <br />
      <a href="addStaticPage.secureaction#form" title="Add a new static page">New static page</a> |
      <a href="viewStaticPages.secureaction" title="Manage your static pages">Static pages</a>
      <br />
      <a href="viewCategories.secureaction" title="Edit the categories associated with your blog">Categories</a> |
      <a href="files/" title="Manage files in blog">Files</a> |
      <a href="images/" title="Manage images in blog">Images</a>
      </li>

      <li><b>Responses</b>
      <br />
      <a href="viewResponses.secureaction" title="Manage approved responses">Approved (<fmt:formatNumber value="${blog.numberOfApprovedResponses}"/>)</a>
      <br />
      <a href="viewResponses.secureaction?type=pending" title="Manage pending responses">Pending (<fmt:formatNumber value="${blog.numberOfPendingResponses}"/>)</a> |
      <a href="viewResponses.secureaction?type=rejected" title="Manage rejected responses">Rejected (<fmt:formatNumber value="${blog.numberOfRejectedResponses}"/>)</a>
      </li>
      </pebble:isBlogContributor>

      <pebble:isBlogAdminOrBlogOwner>
      <li><b>Blog</b>
      <br />
      <a href="viewBlogProperties.secureaction" title="Manage your blog properties">Properties</a> |
      <a href="viewPlugins.secureaction" title="Manage your plugins">Plugins</a> |
      <a href="theme/" title="Manage and edit the files in your theme">Theme</a>
      <br />
      <a href="viewMessages.secureaction">Messages (<fmt:formatNumber value="${blog.numberOfMessages}" type="number" />)</a> |
      <a href="utilities.secureaction" title="Maintenance and upgrade utilities">Utilities</a> |
      <a href="aboutBlog.secureaction" title="About this blog">About</a>
      </li>
      </pebble:isBlogAdminOrBlogOwner>

      <pebble:isAuthorisedForBlog>
      <li><b>Logs</b>
      <br />
      <a href="viewReferers.secureaction" title="View referers for today">Referers</a> (<a href="viewReferers.secureaction?filter=false" title="View referers for today, unfiltered">unfiltered</a>) |
      <a href="viewRequests.secureaction" title="View requests for today">Requests</a>
      <br />
      <a href="viewLog.secureaction" title="View log for today">Log</a> |
      <a href="viewLogSummary.secureaction" title="View log summary for this month">Log summary</a> |
      <a href="viewRefererFilters.secureaction" title="View referer filters">Referer filters</a>
      </li>
      </pebble:isAuthorisedForBlog>

    <pebble:isBlogAdmin>
      <li><b>Administration</b>
      <br />
      <c:if test="${blogManager.multiBlog}">
      <a href="${multiBlog.url}viewPebbleProperties.secureaction" title="Manage Pebble properties">Properties</a> |
      </c:if>
      <a href="${multiBlog.url}viewBlogs.secureaction" title="View blogs">Blogs</a>
      <br />
      <a href="${multiBlog.url}addUser.secureaction" title="Add user">Add user</a> |
      <a href="${multiBlog.url}viewUsers.secureaction" title="View users">Users</a>
      </li>
    </pebble:isBlogAdmin>

    </ul>
    </div>
  </div>
</div>
</pebble:isAuthenticated>
