<div class="contentItem">
  <h1>Blogger API</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Pebble has a comprehensive web-based administration interface but it also supports the Blogger API for managing your blog content via compatible blogging clients. Examples here include MarsEdit, ecto, w.bloggar and others.
    </p>

    <h3>Configuration</h3>
    <p>
      When configuring a blogging client, you will typically be asked for the following information.
    </p>

    <ul>
      <li>Username : ${authenticatedUser.username}</li>
      <li>Password : (the same you used to login here)</li>
      <li>XML-API : blogger</li>
      <li>XML-RPC URL : ${pebbleContext.configuration.url}xmlrpc/</li>
      <li>Blog ID : ${blog.id}</li>
    </ul>

    <h3>Titles and Categories</h3>
    <p>
      Because the Blogger API is fairly simplistic, it has no support for things like blog entry titles and categories. However, with Pebble, it possible to specify these attributes for your blog entry. To do this, simply prepend your blog entry body with <code>&lt;title&gt;Your title here&lt;/title&gt;</code> and/or <code>&lt;category&gt;/categoryId1,/categoryId2&lt;/category&gt;</code>.
      Alternatively, consider using the <a href="./help/metaWeblogApi.html">MetaWeblog API</a>.
    </p>
  </div>
</div>