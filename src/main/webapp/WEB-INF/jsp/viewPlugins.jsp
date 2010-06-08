<%@ taglib prefix="plugins" tagdir="/WEB-INF/tags/plugins" %>
<%--@elvariable id="blog" type="net.sourceforge.pebble.domain.Blog"--%>
<%--@elvariable id="availablePlugins" type="net.sourceforge.pebble.plugins.AvailablePlugins"--%>
<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/plugins.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.plugins"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody" style="overflow: auto">
    <form name="pluginsForm" action="savePlugins.secureaction" method="POST" accept-charset="${blog.characterEncoding}">

      <a name="permalinkProvider"></a>
      Permalink Provider (<span class="help"><a href="./help/permalinkProviders.html" target="_blank">Help</a></span>)
      <br />
      <plugins:single name="permalinkProviderName" plugins="${availablePlugins.permalinkProviders}"
                     enabled="${blog.permalinkProviderName}" properties="${pluginProperties}"/>

      <a name="contentDecorators"></a>
      Content Decorators (<span class="help"><a href="./help/contentDecorators.html" target="_blank">Help</a></span>)
      <br />
      <plugins:multiple name="decorators" plugins="${availablePlugins.contentDecorators}" orderable="${true}"
                       enabled="${blog.contentDecorators}" properties="${pluginProperties}"/>

      <a name="blogListeners"></a>
      Blog Listeners (<span class="help"><a href="./help/blogListeners.html" target="_blank">Help</a></span>)
      <br />
      <plugins:multiple name="blogListeners" plugins="${availablePlugins.blogListeners}" orderable="${false}"
                       enabled="${blog.blogListeners}" properties="${pluginProperties}"/>

      <a name="blogEntryListeners"></a>
      Blog Entry Listeners (<span class="help"><a href="./help/blogEntryListeners.html" target="_blank">Help</a></span>)
      <br />
      <plugins:multiple name="blogEntryListeners" plugins="${availablePlugins.blogEntryListeners}"
                         enabled="${blog.blogEntryListeners}" properties="${pluginProperties}"/>

      <a name="commentListeners"></a>
      Comment Listeners (<span class="help"><a href="./help/commentListeners.html" target="_blank">Help</a></span>)
      <br />
      <plugins:multiple name="commentListeners" plugins="${availablePlugins.commentListeners}"
                         enabled="${blog.commentListeners}" properties="${pluginProperties}"/>

      <a name="commentConfirmationStrategy"></a>
      Comment Confirmation Strategy (<span class="help"><a href="./help/confirmationStrategies.html#commentProcess" target="_blank">Help</a></span>)
      <br />
      <plugins:single name="commentConfirmationStrategy" plugins="${availablePlugins.commentConfirmationStrategies}"
                         enabled="${blog.commentConfirmationStrategyName}" properties="${pluginProperties}"/>

      <a name="trackbackListeners"></a>
      TrackBack Listeners (<span class="help"><a href="./help/trackbackListeners.html" target="_blank">Help</a></span>)
      <br />
      <plugins:multiple name="trackBackListeners" plugins="${availablePlugins.trackbackListeners}"
                         enabled="${blog.trackBackListeners}" properties="${pluginProperties}"/>

      <a name="trackBackConfirmationStrategy"></a>
      TrackBack Confirmation Strategy (<span class="help"><a href="./help/confirmationStrategies.html#trackbackProcess" target="_blank">Help</a></span>)
      <br />
      <plugins:single name="trackBackConfirmationStrategy" plugins="${availablePlugins.trackbackConfirmationStrategies}"
                           enabled="${blog.trackBackConfirmationStrategyName}" properties="${pluginProperties}"/>

      Lucene Analyzer
      <br />
      <plugins:single name="luceneAnalyzer" plugins="${availablePlugins.luceneAnalyzers}"
                             enabled="${blog.luceneAnalyzer}" properties="${pluginProperties}"/>

      <a name="logger"></a>
      Logger (<span class="help"><a href="./help/logs.html" target="_blank">Help</a></span>)
      <br />
      <plugins:single name="logger" plugins="${availablePlugins.loggers}"
                             enabled="${blog.loggerName}" properties="${pluginProperties}"/>

      <table width="99%">
        <tr>
          <td align="right">
            <input name="submit" type="submit" Value="Save Plugins">
          </td>
        </tr>
      </table>
    </form>
    <script type="text/javascript">
        initPluginProperties();
    </script>
  </div>
</div>