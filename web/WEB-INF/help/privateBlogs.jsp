<div class="contentItem">
  <h1>Private Blogs</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    Although weblogs are generally a public way of sharing ideas and opinions, there may be times when you want to set up a private blog - perhaps a dedicated project blog or a blog that only family members can access.
    </p>

    <p>
      To secure a single blog when running in multi-blog mode, open up the <code>/WEB-INF/applicationContext-acegi-security.xml</code> file and modify the <code>objectDefinitionSource</code> property of the <code>filterInvocationInterceptor</code> bean.
      In the example below, the bold line shows a private blog (called someblog) that has also been secured using
      different role names.
    </p>
    <pre class="codeSample">&lt;property name="objectDefinitionSource"&gt;
  &lt;value&gt;
    CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
    PATTERN_TYPE_APACHE_ANT
    <b>/someblog/**=ROLE_SOME_BLOG_CONTRIBUTOR,ROLE_SOME_BLOG_OWNER</b>
    /**/*.secureaction=ROLE_BLOG_OWNER,ROLE_BLOG_CONTRIBUTOR,ROLE_BLOG_ADMIN
    /**/files/=ROLE_BLOG_CONTRIBUTOR
    /**/images/=ROLE_BLOG_CONTRIBUTOR
    /**/theme/**=ROLE_BLOG_OWNER
    /**/help/**=ROLE_BLOG_OWNER,ROLE_BLOG_CONTRIBUTOR
  &lt;/value&gt;
&lt;/property&gt;</pre>

    <blockquote><b>Note</b> : In multi-blog mode, all blogs will automatically show up on the multi-blog front page and their content will appear in the combined news feeds. To stop this from occurring you can mark your blog to be private in the <a href="./help/configuration.html">blog properties</a>.</blockquote>
  </div>
</div>