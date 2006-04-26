<div class="contentItem">
  <div class="title">Utilities</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <p>
      Here are some utilities that are useful when upgrading Pebble. Before running them, it is advisable
      to <a href="exportBlog.secureaction?flavor=zip" title="Export blog as ZIP file">backup</a> your blog.
      Some utilities may take a few minutes to run, depending on how many blog entries and responses
      are contained within your blog.</b>
    </p>

    <p>
      <b>1. Reload blog</b>
      <br />
      If you've made configuration changes to your blog but they don't seem to have taken effect yet,
      you can <a href="reloadBlog.secureaction" title="Reload your blog">reload your blog</a>.
    </p>

    <p>
      <b>2. Build search index</b>
      <br />
      When upgrading Pebble or moving your blog from one URL to another, the information contained within
      the search index can become out of date. Click <a href="utilities.secureaction?action=buildSearchIndex">here</a>
      to rebuild your search index.
    </p>

    <p>
      <b>3. Build IP address blacklist and whitelist</b>
      <br />
      The <code>IpAddressListener</code>
      was introduced in Pebble 1.7.1 to perform filtering against a blacklist and a whitelist.
      Click <a href="utilities.secureaction?action=ipAddressListener">here</a>
      to create the blacklist and whitelist from your current responses. The IP address of all approved responses
      will be placed into the whitelist, while the IP address of all rejected responses will be placed into the blacklist.
    </p>

    <p>
      <b>4. Fix HTML in responses</b>
      <br />
      Previous versions of Pebble have used slightly different conventions for escaping the HTML tags within
      comments and TrackBacks. If any of your responses are displaying
      escaped HTML such as &amp;lt; and &amp;gt; (particularly the list of recent responses in the sidebar),
      click <a href="utilities.secureaction?action=fixHtmlInResponses">here</a> to fix them.
    </p>

    <p>
      <b>5. Convert flat categories to hierarchical categories</b>
      <br />
      Versions of Pebble prior to 1.9 only supported flat categories, which were stored in a Java properies file.
      Click <a href="utilities.secureaction?action=convertCategories">here</a> to convert your old categories
      into the new format.
    </p>

    <p>
      <b>6. Export blog</b>
      <br />
      You can export your blog in a number of formats, including a <a href="exportBlog.secureaction?flavor=zip" title="Export blog as ZIP file">ZIP file</a>
      of the raw data or as a newsfeed in <a href="exportBlog.secureaction?flavor=rss20" title="Export blog as RSS 2.0">RSS</a>,
      <a href="exportBlog.secureaction?flavor=rdf" title="Export blog as RDF 1.0">RDF</a> or
      <a href="exportBlog.secureaction?flavor=atom" title="Export blog as Atom 1.0">Atom</a>.
    </p>

    <c:if test="${not empty blog.categories}">
    <p>
      <b>7. Move blog entries from one category to another</b>
      <br />
      Use the form below to move the blog entries associated with one category to another.
      <form action="utilities.secureaction" method="post" accept-charset="<c:out value="${blog.characterEncoding}" />">
      <table width="99%" cellspacing="0" cellpadding="4">
        <input type="hidden" name="action" value="moveBlogEntriesFromCategory" />
        <tr class="small">
          <td>
            <table width="99%" cellpadding="0" cellspacing="0">
              <tr>
              <c:forEach var="category" items="${blog.categories}" varStatus="status" begin="1">
                <c:if test="${status.count % 2 == 1}">
                <tr>
                </c:if>
                <td>
                <input type="radio" name="from" value="<c:out value="${category.id}" />" />&nbsp;<c:out value="${category.name}" /> (<c:out value="${category.numberOfBlogEntries}" />)
                </td>
                <c:if test="${status.count % 2 == 0}">
                </tr>
                </c:if>
              </c:forEach>
              <tr>
            </table>
          </td>
          <td>
          -&gt;
          </td>
          <td>
            <table width="99%" cellpadding="0" cellspacing="0">
              <tr>
              <c:forEach var="category" items="${blog.categories}" varStatus="status" begin="1">
                <c:if test="${status.count % 2 == 1}">
                <tr>
                </c:if>
                <td>
                <input type="radio" name="to" value="<c:out value="${category.id}" />" />&nbsp;<c:out value="${category.name}" /> (<c:out value="${category.numberOfBlogEntries}" />)
                </td>
                <c:if test="${status.count % 2 == 0}">
                </tr>
                </c:if>
              </c:forEach>
              <tr>
            </table>
          </td>
        </tr>
        <tr>
          <td colspan="3" align="right">
            <input type="submit" value="Move" />
          </td>
        </tr>
      </table>
      </form>
    </p>
    </c:if>

  </div>

</div>