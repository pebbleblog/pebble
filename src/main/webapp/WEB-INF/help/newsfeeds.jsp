<div class="contentItem">
  <h1>Newsfeeds</h1>
  <h2>RSS and Atom</h2>

  <div class="contentItemBody">
    <p>
      Pebble can generate the most common type of newsfeeds that news aggregators
      can subscribe to, the links of which are as follows.
    </p>

    <ul>
    <li>RSS : ${blogUrl}rss.xml</li>
    <li>Atom : ${blogUrl}atom.xml</li>
    </ul>

    <p>
      At any point in time, each of these feeds contains only those blog entries that
      are seen on the front page of your blog, the number of which is configured by the
      "Recent blog entries" property on the <a href="viewBlogProperties.secureaction">Blog properties</a> page.
      With the default theme, links to these feeds are shown at the top of page.
    </p>

    <h3>Category and tag specific newsfeeds</h3>
    <p>
      Pebble can also generate newsfeeds that are specific to a particular category or tag. See <a href="./help/categories.html">Categories</a> or <a href="./help/tags.html">Tags</a> for more information.
    </p>

    <h3>Validating your newsfeeds</h3>
    <p>
      Click either of the following links to validate your newsfeeds.<br /><br />
      <a href="http://www.feedvalidator.org/check.cgi?url=${blogUrl}rss.xml"><img src="${pageContext.request.contextPath}/common/images/valid-rss.png" alt="[Valid RSS]" title="Validate my RSS feed" width="88" height="31" border="0" /></a>
      &nbsp;
      <a href="http://www.feedvalidator.org/check.cgi?url=${blogUrl}atom.xml"><img src="${pageContext.request.contextPath}/common/images/valid-atom.png" alt="[Valid Atom]" title="Validate my Atom feed" width="88" height="31" border="0" /></a>
    </p>
  </div>
</div>