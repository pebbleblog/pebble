<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/plugins.html" target="_blank">Help</a>
  </div>

  <div class="title">Plugins</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody" style="overflow: auto">
    <form name="pluginsForm" action="savePlugins.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
    <table>
      <tr>
        <td valign="top">
          <a name="blogEntryDecorators"></a>
          Blog entry decorators (<span class="help"><a href="./help/blog-entry-decorators.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="blogEntryDecorators" rows="10" cols="60">${blog.blogEntryDecorators}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          <a name="permalinkProvider"></a>
          Permalink provider (<span class="help"><a href="./help/permalink-providers.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <input type="text" name="permalinkProviderName" size="60" value="${blog.permalinkProviderName}" />
        </td>
      </tr>

      <tr>
        <td valign="top">
          <a name="blogListeners"></a>
          Blog listeners (<span class="help"><a href="./help/blog-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="blogListeners" rows="4" cols="60">${blog.blogListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          <a name="blogEntryListeners"></a>
          Blog entry listeners (<span class="help"><a href="./help/blog-entry-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="blogEntryListeners" rows="10" cols="60">${blog.blogEntryListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          <a name="commentListeners"></a>
          Comment listeners (<span class="help"><a href="./help/comment-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="commentListeners" rows="10" cols="60">${blog.commentListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          <a name="trackbackListeners"></a>
          TrackBack listeners (<span class="help"><a href="./help/trackback-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="trackBackListeners" rows="10" cols="60">${blog.trackBackListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          <a name="properties"></a>
          Properties (<span class="help"><a href="./help/plugins.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="pluginProperties" rows="32" cols="60">${pluginProperties}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          Lucene Analyzer
        </td>
        <td>
          <input type="text" name="luceneAnalyzer" size="60" value="${blog.luceneAnalyzer}">
        </td>
      </tr>

      <tr>
        <td valign="top">
          <a name="logger"></a>
          Logger (<span class="help"><a href="./help/logs.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <input type="text" name="logger" size="60" value="${blog.loggerName}">
        </td>
      </tr>

      <tr>
        <td align="right" colspan="2">
          <input name="submit" type="submit" Value="Save Plugins">
        </td>
      </tr>
    </table>
  </form>
  </div>

</div>