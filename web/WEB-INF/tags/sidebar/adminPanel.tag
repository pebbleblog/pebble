<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<%--
  Displays the administration links that are available after logging in.
--%>
<pebble:isAuthenticated>
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span> ${authenticatedUser.username} : <a href="${pebbleContext.configuration.secureUrl}logout.action?redirectUrl=${blog.url}">Logout</a> <pebble:isAuthorisedForBlog>| <a href="./help/index.html" title="Help">Help</a></pebble:isAuthorisedForBlog></span></div>
  <div class="sidebarItemBody">
    <div id="adminPanel">

      <c:if test="${authenticatedUser.detailsUpdateable == true}">
      <div id="admin-user-group" class="adminGroup" onclick="toggleVisibility('admin-user'); hideComponent('admin-content'); hideComponent('admin-responses'); hideComponent('admin-blog'); hideComponent('admin-logs'); hideComponent('admin-admin')"">User Details</div>
      <div id="admin-user" class="adminLinks">
        <a href="editUserDetails.secureaction" title="Edit user details">User details</a> |
        <a href="changePassword.secureaction" title="Change password">Change password</a><br />
      </div>
      <script type="text/javascript">hideComponent('admin-user');</script>
      </c:if>

      <pebble:isBlogContributor>
      <div id="admin-content-group" class="adminGroup" onclick="toggleVisibility('admin-content'); hideComponent('admin-user'); hideComponent('admin-responses'); hideComponent('admin-blog'); hideComponent('admin-logs'); hideComponent('admin-admin')">Content</div>
      <div id="admin-content" class="adminLinks">
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
      </div>
      <script type="text/javascript">hideComponent('admin-content');</script>

      <div id="admin-responses-group" class="adminGroup" onclick="showComponent('admin-responses'); hideComponent('admin-user'); hideComponent('admin-content'); hideComponent('admin-blog'); hideComponent('admin-logs'); hideComponent('admin-admin')">Comment and TrackBacks</div>
      <div id="admin-responses" class="adminLinks">
      <a href="viewResponses.secureaction" title="Manage approved responses">Approved (<fmt:formatNumber value="${blog.numberOfApprovedResponses}"/>)</a>
      <br />
      <a href="viewResponses.secureaction?type=pending" title="Manage pending responses">Pending (<fmt:formatNumber value="${blog.numberOfPendingResponses}"/>)</a> |
      <a href="viewResponses.secureaction?type=rejected" title="Manage rejected responses">Rejected (<fmt:formatNumber value="${blog.numberOfRejectedResponses}"/>)</a>
      </div>
      <script type="text/javascript">hideComponent('admin-responses');</script>
      </pebble:isBlogContributor>

      <pebble:isBlogAdminOrBlogOwner>
      <div id="admin-blog-group" class="adminGroup" onclick="toggleVisibility('admin-blog'); hideComponent('admin-user'); hideComponent('admin-content'); hideComponent('admin-responses'); hideComponent('admin-logs'); hideComponent('admin-admin')">Blog configuration and utilities</div>
      <div id="admin-blog" class="adminLinks">
      <a href="viewBlogProperties.secureaction" title="Manage your blog properties">Properties</a> |
      <a href="viewPlugins.secureaction" title="Manage your plugins">Plugins</a>
      <c:if test="${pebbleContext.configuration.userThemesEnabled}"> 
        | <a href="theme/" title="Manage and edit the files in your theme">Theme</a>
      </c:if>
      <br />
      <a href="viewMessages.secureaction">Messages (<fmt:formatNumber value="${blog.numberOfMessages}" type="number" />)</a> |
      <a href="utilities.secureaction" title="Maintenance and upgrade utilities">Utilities</a> |
      <a href="aboutBlog.secureaction" title="About this blog">About</a>
      </div>
      <script type="text/javascript">hideComponent('admin-blog');</script>
      </pebble:isBlogAdminOrBlogOwner>

      <pebble:isAuthorisedForBlog>
      <div id="admin-logs-group" class="adminGroup" onclick="toggleVisibility('admin-logs'); hideComponent('admin-user'); hideComponent('admin-content'); hideComponent('admin-responses'); hideComponent('admin-blog'); hideComponent('admin-admin')">Logs, requests and referers</div>
      <div id="admin-logs" class="adminLinks">
      <a href="viewReferers.secureaction" title="View referers for today">Referers</a> (<a href="viewReferers.secureaction?filter=false" title="View referers for today, unfiltered">unfiltered</a>) |
      <a href="viewRequests.secureaction" title="View requests for today">Requests</a>
      <br />                                                             
      <a href="viewLog.secureaction" title="View log for today">Log</a> |
      <a href="viewLogSummary.secureaction" title="View log summary for this month">Log summary</a> |
      <a href="viewStatistics.secureaction" title="View statistics for today">Statistics</a>
      <br />
      <a href="viewUserAgents.secureaction" title="View user agents for today">User Agents</a> |
      <a href="viewRefererFilters.secureaction" title="View referer filters">Referer filters</a>
      </div>
      <script type="text/javascript">hideComponent('admin-logs');</script>
      </pebble:isAuthorisedForBlog>

      <pebble:isBlogAdmin>
      <div id="admin-admin-group" class="adminGroup" onclick="toggleVisibility('admin-admin'); hideComponent('admin-user'); hideComponent('admin-content'); hideComponent('admin-responses'); hideComponent('admin-blog'); hideComponent('admin-logs')">Administration</div>
      <div id="admin-admin" class="adminLinks">
      <c:if test="${blogManager.multiBlog}">
      <a href="${multiBlog.url}viewPebbleProperties.secureaction" title="Manage Pebble properties">Properties</a> |
      </c:if>
      <a href="${multiBlog.url}viewBlogs.secureaction" title="View blogs">Blogs</a>
      <br />
      <a href="${multiBlog.url}addUser.secureaction" title="Add user">Add user</a> |
      <a href="${multiBlog.url}viewUsers.secureaction" title="View users">Users</a>
      </div>
      <script type="text/javascript">hideComponent('admin-admin');</script>
      </pebble:isBlogAdmin>

    </div>
  </div>
</div>
</pebble:isAuthenticated>
