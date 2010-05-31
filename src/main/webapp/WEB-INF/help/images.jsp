<div class="contentItem">
  <h1>Images</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    Since blog entries are just short HTML fragments, the HTML <code>&lt;img&gt;</code> tag can be used to reference and include images. Images themselves can be stored and managed through the <a href="./images/">Images</a> page.
    </p>

    <p>
    <div align="center">
    <img src="${pageContext.request.contextPath}/common/help/images1.png" alt="The images file browser" />
    </div>
    </p>

    <p>
      To use any image in your blog, just reference the image using a relative URI from within your blog entry as follows.
      <pre>  &lt;img src="./images/apache-tomcat-security.jpg" /&gt;</pre>
      Pebble will take care of converting this to a full URL where necessary.
    </p>

    <%--
    <p>
      <h3>Quotas</h3>
      Although more relevant to team and multi-user blogs, quotas can be enforced to restrict how much disk space each blog has to store images and arbitrary files. By default, the maximum size of file that can be uploaded is 2048KB and quotas are not enabled.
      To modify these values, open the <code>/WEB-INF/classes/pebble.properties</code> file and edit the <code>blog.uploadFileSize</code> and <code>blog.uploadFileQuota</code> properties. You will need to restart Pebble for these changes to take effect. When quotas are enabled, all of the stored images, files and files in your theme count toward this.
    </p>
    --%>
  </div>
</div>