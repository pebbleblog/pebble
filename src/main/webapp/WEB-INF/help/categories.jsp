<div class="contentItem">
  <h1>Categories</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Each blog entry can be organized into one or more categories, the categories being defined as a hierarchy
      starting at the root category with an ID of "/". Underneath this, more categories can be defined in a file
      system style structure, with the forward slash character being used to specify levels in the hierarchy. For example,
      in the screenshot below, there are three top-level categories of Apple (/apple), Java (/java) and Pebble (/pebble). In addition to this, the
      Java category has a subcategory of JavaOne (/java/javaone).
    </p>

    <p>
    <div align="center">
    <img src="${pageContext.request.contextPath}/common/help/categories1.png" alt="Categories" />
    </div>
    </p>

    <p>
      To add, edit or remove the categories defined for your blog, click <a href="viewCategories.secureaction">Categories</a>.
    </p>

    <h3>Tagging</h3>
    <p>
      See <a href="./help/tags.html">Tags</a> for more information on how categories can be tagged.
    </p>

    <h3>Category-based feeds</h3>
    <p>
      Each category has an associated RSS/Atom feed, allowing readers to subscribe to only those categories they are interested in.
      <c:if test="${not empty blog.rootCategory.subCategories}">
        For example, the newsfeed URLs for your <a href="${url:rewrite(blog.categories[1].permalink)}">${blog.categories[1]}</a> category are as follows.
        <ul>
          <li>RSS : <a href="${url:rewrite(blog.categories[1].permalink)}rss.xml">${blog.categories[1].permalink}rss.xml</a></li>
          <li>Atom : <a href="${url:rewrite(blog.categories[1].permalink)}atom.xml">${blog.categories[1].permalink}atom.xml</a></li>
        </ul>
        You can find these links on the public <a href="./categories/">Categories</a> page.
      </c:if>
    </p>

    <h3>Problems?</h3>
    <p>
      If your category statistics don't seem to be accurately reflecting how you've categorised your blog entries, you might need
      to <a href="utilities.secureaction?action=buildIndexes">reindex your blog</a>.
    </p>
  </div>
</div>