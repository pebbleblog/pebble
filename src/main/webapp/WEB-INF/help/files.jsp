<div class="contentItem">
  <h1>Files</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      In the same way that images can be uploaded and linked to, Pebble allows arbitrary files to be uploaded to your blog for download by your readers. To do this, click <a href="./files/">Files</a>.
    </p>

    <h3><a name="mimeTypes"></a>MIME types</h3>
    <p>
      If, when opening/downloading files from your blog, you notice that the appropriate MIME type is not being set, this can be
      rectified by adding an entry into the <code>/WEB-INF/classes/content-types.properties</code> file. A MIME type entry takes
      the form <code>&lt;file extension&gt;=&lt;MIME type&gt;</code> and an example is as follows:
      <pre>  .mp3=audio/x-mpeg</pre>
      Pebble needs to be restarted for MIME type changes to take effect.
    </p>
  </div>
</div>