<div class="contentItem">
  <div class="title">Multi-user Blogs</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <p>
      Pebble has the ability to host multiple blogs from a single web application installation. To do this, stop Pebble and create one or more
      directories (one for each additional blog) underneath <code>${pebbleContext.configuration.dataDirectory}/blogs/</code>.
    </p>

    <p>
    Each individual blog works in the same way that blogs do in the single user mode, with the exception that the URLs are slightly different. Instead of <code>http://localhost:8080/blog/</code> (in single user mode), each blog can be accessing through a slightly longer URL such as <code>http://localhost:8080/blog/blog1/</code>.
    </p>

    <a name="security"></a><div class="subsubtitle">Security</div>
    <p>
    An important point to mention about multi-user blogs is security. Defining a number of users with the same roles (i.e. <code>blog-owner</code> and <code>blog-contributor</code>) means that anybody with the appropriate role can access the corresponding functions on any blog. For example, if you have 3 blogs (blog1, blog2 and blog3) and create a unique user for each of them, the blog1 user will be able to access all of the blog owner and blog contributor functionality of blog2 and blog3. Ideally, you probably want the blog1 user to only be able to access the functions of blog1.
    </p>

    <p>
    To do this, go to the <b>Blog properties</b> page for blog1 and, towards the middle of the page, you'll see a <b>Security</b> section with two textfields. In the <b>Blog owners</b> textfield, enter a comma separated list of usernames for all those users that you would like to access the blog owner features for this blog. Similarly, enter a comma separated list of usernames for blog contributors in the <b>Blog contributors</b> field.
    In doing this, you restrict the users that can access the protected features of the blog. So, although all three users are members of the same role, only the blog1 user will be able to access the protected features of this blog.
    </p>

    <div class="subsubtitle">Privacy</div>
    <p>
    In multi-blog mode, all blogs will automatically show up on the multi-user front page and their content will appear in the combined news feeds. To stop this from occurring you can mark your blog to be private on the <a href="./help/configuration.html">blog properties</a> page. In effect, this hides your blog and readers can still navigate directly to your blog. For a truely private, password protected blog, see <a href="./help/privateBlogs.html">Private Blogs</a>.
    </p>

    <div class="subsubtitle">Pebble Administration</div>
    <p>
    In addition to the <code>blog-owner</code> and <code>blog-contributor</code> security roles, in multi-blog mode, an additional role called <code>pebble-admin</code> can be granted to one or more users. Users in this role will be able to perform some higher level administration tasks as follows.
    </p>

    <ul>
      <li><b>Pebble properties</b> : Edit the some of the details of the blog that appear on the multi-user summary page and in the RSS combined feeds.</li>
      <li><b>Add blog</b> : Add new blogs at runtime.</li>
    </ul>

    <p>
      These links appear only when Pebble is running in multi-blog mode.
    </p>
  </div>
</div>