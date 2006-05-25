<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${pageContext.request.contextPath}/docs/plugins.html" target="_blank">Help</a>
  </div>

  <div class="title">Plugins</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody" style="overflow: auto">
    <form name="pluginsForm" action="savePlugins.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
    <table>
      <tr>
        <td valign="top">
          Blog entry decorators (<span class="help"><a href="${pageContext.request.contextPath}/docs/blog-entry-decorators.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="blogEntryDecorators" rows="10" cols="60">${blog.blogEntryDecorators}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          Permalink provider (<span class="help"><a href="${pageContext.request.contextPath}/docs/permalink-providers.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <input type="text" name="permalinkProviderName" size="60" value="${blog.permalinkProviderName}" />
        </td>
      </tr>

      <tr>
        <td valign="top">
          Blog listeners (<span class="help"><a href="${pageContext.request.contextPath}/docs/blog-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="blogListeners" rows="4" cols="60">${blog.blogListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          Blog entry listeners (<span class="help"><a href="${pageContext.request.contextPath}/docs/blog-entry-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="blogEntryListeners" rows="10" cols="60">${blog.blogEntryListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          Comment listeners (<span class="help"><a href="${pageContext.request.contextPath}/docs/comment-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="commentListeners" rows="10" cols="60">${blog.commentListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          TrackBack listeners (<span class="help"><a href="${pageContext.request.contextPath}/docs/trackback-listeners.html" target="_blank">Help</a></span>)
        </td>
        <td>
          <textarea name="trackBackListeners" rows="10" cols="60">${blog.trackBackListeners}</textarea>
        </td>
      </tr>

      <tr>
        <td valign="top">
          Properties (<span class="help"><a href="${pageContext.request.contextPath}/docs/plugins.html" target="_blank">Help</a></span>)
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
          Logger (<span class="help"><a href="${pageContext.request.contextPath}/docs/logs.html" target="_blank">Help</a></span>)
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