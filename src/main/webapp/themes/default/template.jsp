<%--
  The main template into which all other content is placed. The following
  objects are available for use in templates.

   - blog                   net.sourceforge.pebble.domain.Blog
   - pebbleContext          net.sourceforge.pebble.PebbleContext
   - categories             java.util.List<net.sourceforge.pebble.domain.Category>
   - tags                   java.util.List<net.sourceforge.pebble.domain.Tag>
   - recentBlogEntries      java.util.List<net.sourceforge.pebble.domain.BlogEntry>
   - recentResponses        java.util.List<net.sourceforge.pebble.domain.Response>
   - archives               java.util.List<net.sourceforge.pebble.domain.Year>
   - pluginProperties       net.sourceforge.pebble.PluginProperties
   - authenticatedUser      net.sourceforge.pebble.security.PebbleUserDetails
--%>
<template:page>

  <div id="body">

    <%-- the header, containing blog name and description --%>
    <div id="header">
      <div id="blogName"><span>${blog.name}</span></div>
      <div id="blogDescription"><span>${blog.description}</span></div>
    </div>

    <%-- the linear navigation links (e.g. < Previous | Home | Next >) --%> 
    <div id="linearNavigation">
      <template:linearNavigation/>
    </div>

    <%-- the sidebar that includes the calendar, recent blog entries, links, etc. --%>
    <div id="sidebar">
      <sidebar:about/>
      <%-- uncomment this to have "about the author" information, which is useful for a multi-contributor blog
      <sidebar:aboutAuthor/>
      --%>
      <sidebar:subscriptions/>
      <sidebar:navigation/>
      <sidebar:search/>
      <sidebar:archivesByMonth/>
      <sidebar:categories/>
      <sidebar:tagCloud/>
      <sidebar:recentBlogEntries/>
      <sidebar:recentResponses/>
      <sidebar:blogSummary/>
      <%-- the following is an example of the feed component that lets you aggregate a RSS/Atom feed into your blog
      <sidebar:feed name="del.icio.us" url="http://del.icio.us/rss/simongbrown" maxEntries="3" showBody="true" truncateBody="true"/>
      --%>
      <sidebar:loginForm/>
    </div>

    <%-- the main area into which content gets rendered --%>
    <div id="content">
      <template:content/>
    </div>

    <%-- the footer, containing the "powered by" link --%>
    <div id="footer">
      <template:poweredByPebble/>
      <pebble:isNotAuthenticated>
      |
      <a href="${url:rewrite(blogUrl)}login.secureaction?redirectUrl=${url:urlEncode(originalUri)}"><fmt:message key="login.title" /></a>
      </pebble:isNotAuthenticated>
      <pebble:isAuthenticated>
      |
      <a href="${url:rewrite(blogUrl)}logout.action?redirectUrl=${blogUrl}">Logout</a>
      </pebble:isAuthenticated>
    </div>

  </div>

</template:page>