<div class="contentItem">
  <h1>Tags and tagging</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    Pebble features the ability to tag your blog entries and, at the simplest level, this involves adding a
    space separated list of tags to your blog entry when you edit that blog entry. With a default setup, those
    tags are then displayed underneath each blog entry.
    </p>

    <p>
    <div align="center">
      <img src="${pageContext.request.contextPath}/common/help/tags1.png" alt="Tags underneath a blog entry" />
    </div>
    </p>

    <h3>Category tagging</h3>
    <p>
    Pebble also allows you to add tags to categories, again
    by specifying a space separated list of tags when you edit the category. Thereafter, whenever a blog entry is placed into
    a tagged category, that blog entry also picks up the tags belonging to all of its categories. In effect, this provides
    a way to bulk tag all blog entries in a particular category. Also, with hierarchical categories, a sub-category
    will inherit all of the tags from its parent(s).
    </p>

    <h3>Tagcloud</h3>
    <p>
    Like other blogging tools, Pebble automatically generates and maintains a tagcloud for your blog, which you can
    see by clicking the <a href="./tags/">Tags</a> link in the sidebar.
    </p>

    <p>
    <div align="center">
    <img src="${pageContext.request.contextPath}/common/help/tags2.png" alt="Tagcloud" />
    </div>
    </p>

    <p>
      The size of a link provides an indication of the relative numbers of blog entries that use
      that tag, while hovering over it shows the actual number. Clicking the link will take you to
      a list of all blog entries using that tag.
    </p>

    <h3>Tag-based feeds</h3>
    <p>
      Each tag has an associated RSS/Atom feed, allowing readers to subscribe to only those tags they are interested in.
      <c:if test="${not empty blog.tags}">
        For example, the newsfeed URLs for your <a href="${blog.tags[0].permalink}">${blog.tags[0]}</a> tag are as follows.
        <ul>
          <li>RSS : <a href="${blog.tags[0].permalink}rss.xml">${blog.tags[0].permalink}rss.xml</a></li>
          <li>Atom : <a href="${blog.tags[0].permalink}atom.xml">${blog.tags[0].permalink}atom.xml</a></li>
        </ul>
        You can find these links on the public <a href="./tags/">Tags</a> page.
      </c:if>
    </p>

    <h3>Problems?</h3>
    <p>
      If your tagcloud doesn't seem to be accurately reflecting how you've tagged your blog entries, you might need
      to <a href="utilities.secureaction?action=buildIndexes">reindex your blog</a>.
    </p>
  </div>
</div>