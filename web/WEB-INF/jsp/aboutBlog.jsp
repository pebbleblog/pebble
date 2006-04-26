<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${pageContext.request.contextPath}/docs/index.html" target="_blank">Help</a>
  </div>

  <div class="title">About this blog</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody" style="overflow: auto">

    <table width="99%" cellpadding="4" cellspacing="0" class="small">

    <tr class="odd">
      <td valign="top"><b>Blog entries</b></td>
      <td>
          <fmt:formatNumber value="${blog.numberOfBlogEntries}"/>
      </td>
    </tr>

    <tr class="odd">
      <td valign="top"><b>Responses</b></td>
      <td>
          <fmt:formatNumber value="${blog.responseManager.numberOfResponses}"/>
      </td>
    </tr>

    <tr class="odd">
      <td valign="top"><b>Last modified</b></td>
      <td>
          <fmt:formatDate value="${blog.lastModified}" type="both" dateStyle="long" timeStyle="long"/>
      </td>
    </tr>

    <tr class="odd">
      <td valign="top"><b>Uptime</b></td>
      <td>
        <fmt:formatNumber value="${pebbleContext.uptime.days}"/> days, <fmt:formatNumber value="${pebbleContext.uptime.hours}" pattern="00"/>:<fmt:formatNumber value="${pebbleContext.uptime.minutes}" pattern="00"/>:<fmt:formatNumber value="${pebbleContext.uptime.seconds}" pattern="00"/>
      </td>
    </tr>

    <tr class="odd">
      <td valign="top"><b>Version</b></td>
      <td>
          Pebble ${pebbleContext.buildVersion}, built ${pebbleContext.buildDate}
      </td>
    </tr>

    <tr class="odd">
      <td valign="top"><b>JVM memory</b></td>
      <td>
        Using <fmt:formatNumber value="${pebbleContext.memoryUsageInKB}"/> KB of <fmt:formatNumber value="${pebbleContext.totalMemoryInKB}"/> KB (<a href="gc.secureaction" title="Run the garbage collector">GC</a>)
      </td>
    </tr>

      <tr class="odd">
        <td valign="top"><b>Blog directory</b></td>
        <td>
          ${blog.root}
        </td>
      </tr>

      <tr class="even">
        <td valign="top"><b>Blog URL</b></td>
        <td>
          ${blog.url}
        </td>
      </tr>

      <tr class="odd">
        <td valign="top"><b>XML-RPC Details</b></td>
        <td>
          ${blogManager.baseUrl}xmlrpc/
          (handler is blogger or metaWeblog, blog id is ${blog.id})
        </td>
      </tr>

      <tr class="even">
        <td valign="top"><b>Blog entry decorators</b></td>
        <td>
          <c:forEach var="decorator" items="${blog.blogEntryDecoratorManager.blogEntryDecorators}">
            ${decorator.class.name}<br />
          </c:forEach>
        </td>
      </tr>

      <tr class="odd">
        <td valign="top"><b>Permalink provider</b></td>
        <td>
          ${blog.permalinkProvider.class.name}
        </td>
      </tr>

      <tr class="even">
        <td valign="top"><b>Blog listeners</b></td>
        <td>
          <c:forEach var="listener" items="${blog.eventListenerList.blogListeners}">
            ${listener.class.name}
          </c:forEach>
        </td>
      </tr>

      <tr class="odd">
        <td valign="top"><b>Blog entry listeners</b></td>
        <td>
          <c:forEach var="listener" items="${blog.eventListenerList.blogEntryListeners}">
            ${listener.class.name}<br />
          </c:forEach>
        </td>
      </tr>

      <tr class="even">
        <td valign="top"><b>Comment listeners</b></td>
        <td>
          <c:forEach var="listener" items="${blog.eventListenerList.commentListeners}">
            ${listener.class.name}<br />
          </c:forEach>
        </td>
      </tr>

      <tr class="odd">
        <td valign="top"><b>TrackBack listeners</b></td>
        <td>
          <c:forEach var="listener" items="${blog.eventListenerList.trackBackListeners}">
            ${listener.class.name}<br />
          </c:forEach>
        </td>
      </tr>

      <tr class="even">
        <td valign="top"><b>Properties</b></td>
        <td>
          <pre>${blog.pluginProperties.propertiesAsString}</pre>
        </td>
      </tr>

      <tr class="odd">
        <td valign="top"><b>Logger</b></td>
        <td>
          ${blog.logger.class.name}
        </td>
      </tr>

    </table>
  </div>

</div>