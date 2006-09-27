<div class="contentItem">
  <h1>Multi-blog Privacy</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Although weblogs are generally a public way of sharing ideas and opinions, there may be times when you want to set up a private blog - perhaps a dedicated project blog or a blog that only family members can access.
      In multi-blog mode, all blogs will automatically show up on the multi-user front page and their content will appear in the combined news feeds. To stop this from occurring you can opt-out your blog on the <a href="./help/configuration.html">blog properties</a> page. In effect, this hides your blog but readers can still navigate directly to your blog.
    </p>

    <p>
      A truely private, password protected blog is also possible, although requires some configuration at the web application level.
      To secure a single blog when running in multi-blog mode, open up the <code>/WEB-INF/applicationContext-acegi-security.xml</code> file and modify the <code>objectDefinitionSource</code> property of the <code>filterInvocationInterceptor</code> bean.
      In the example below, the bold line shows a private blog (called someblog) that has also been secured using
      a specific role name.
    </p>
    <pre class="codeSample">&lt;property name="objectDefinitionSource"&gt;
  &lt;value&gt;
    CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
    PATTERN_TYPE_APACHE_ANT
    <b>/someblog/**=ROLE_SOMEBLOG_USER</b>
    /**/*.secureaction=ROLE_BLOG_OWNER,...
    /**/files/=ROLE_BLOG_CONTRIBUTOR
    /**/images/=ROLE_BLOG_CONTRIBUTOR
    /**/theme/**=ROLE_BLOG_OWNER
    /**/help/**=ROLE_BLOG_OWNER,ROLE_BLOG_CONTRIBUTOR
  &lt;/value&gt;
&lt;/property&gt;</pre>

    <p>
      Any additional, custom roles that you define here must be granted to users. With the default security realm, this
      can be done by opening the <code>&lt;dataDirectory&gt;/realm/&lt;username&gt;.properties</code> file and adding the role
      to the <code>roles</code> property. The web/application server should then be restarted so configuration changes
      take effect.
    </p>

    <blockquote>
      This functionality and it's behind-the-scenes configuration will be provided automatically in a future version.
    </blockquote>
  </div>
</div>