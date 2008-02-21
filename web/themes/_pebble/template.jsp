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

    <%-- the main area into which content gets rendered --%>
    <div id="content">
      <template:content/>
    </div>

    <%-- the footer, containing the "powered by" link --%>
    <div id="footer">
    </div>

  </div>

</template:page>