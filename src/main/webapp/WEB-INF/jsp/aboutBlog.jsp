<div class="contentItem">

  <h1><fmt:message key="view.aboutThisBlog"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <table width="99%" cellpadding="4" cellspacing="0" class="small">

    <tr class="odd">
      <td valign="top"><b><fmt:message key="view.aboutThisBlog.blogEntries"/></b></td>
      <td>
          <fmt:message key="view.aboutThisBlog.numberOfEntries">
			<fmt:param>
				<fmt:formatNumber value="${blog.numberOfBlogEntries}"/>
			</fmt:param>
			<fmt:param>
				<fmt:formatNumber value="${blog.numberOfPublishedBlogEntries}"/>
			</fmt:param>
			<fmt:param>
				<fmt:formatNumber value="${blog.numberOfUnpublishedBlogEntries}"/>
			</fmt:param>
          </fmt:message>
      </td>
    </tr>

    <tr class="even">
      <td valign="top"><b><fmt:message key="view.aboutThisBlog.responses"/></b></td>
      <td>
          <fmt:formatNumber value="${blog.numberOfResponses}"/>
      </td>
    </tr>

    <tr class="odd">
      <td valign="top"><b><fmt:message key="view.aboutThisBlog.lastModified"/></b></td>
      <td>
          <fmt:formatDate value="${blog.lastModified}" type="both" dateStyle="long" timeStyle="long"/>
      </td>
    </tr>

    <tr class="even">
      <td valign="top"><b><fmt:message key="view.aboutThisBlog.uptime"/></b></td>
      <td>
      <fmt:message key="view.aboutThisBlog.actualUptime">
      	<fmt:param>
      		<fmt:formatNumber value="${pebbleContext.uptime.days}"/>
      	</fmt:param>
      	<fmt:param>
      		<fmt:formatNumber value="${pebbleContext.uptime.hours}" pattern="00"/>
      	</fmt:param>
      	<fmt:param>
      		<fmt:formatNumber value="${pebbleContext.uptime.minutes}" pattern="00"/>
      	</fmt:param>
      	<fmt:param>
      		<fmt:formatNumber value="${pebbleContext.uptime.seconds}" pattern="00"/>
      	</fmt:param>
      </fmt:message>
      </td>
    </tr>

    <tr class="odd">
      <td valign="top"><b><fmt:message key="view.aboutThisBlog.version"/></b></td>
      <td>
          Pebble ${pebbleContext.buildVersion}, built ${pebbleContext.buildDate}
      </td>
    </tr>

    <tr class="even">
      <td valign="top"><b><fmt:message key="view.aboutThisBlog.jvmMemory"/></b></td>
      <td>
		<fmt:message key="view.aboutThisBlog.jvmMemory.actual">
			<fmt:param>
				<fmt:formatNumber value="${pebbleContext.memoryUsageInKB}"/>
			</fmt:param>
			<fmt:param>
				<fmt:formatNumber value="${pebbleContext.totalMemoryInKB}"/>
			</fmt:param>
		</fmt:message>
		(<a href="gc.secureaction" title="<fmt:message key="view.aboutThisBlog.runGC.long"/>"><fmt:message key="view.aboutThisBlog.runGC"/></a>)
      </td>
    </tr>

      <tr class="odd">
        <td valign="top"><b><fmt:message key="view.aboutThisBlog.blogDirectory"/></b></td>
        <td>
          ${blog.root}
        </td>
      </tr>

      <tr class="even">
        <td valign="top"><b><fmt:message key="view.aboutThisBlog.blogUrl"/></b></td>
        <td>
          ${blogUrl}
        </td>
      </tr>

      <tr class="odd">
        <td valign="top"><b><fmt:message key="view.aboutThisBlog.xmlRpcDetails"/></b></td>
        <td>
          <fmt:message key="view.aboutThisBlog.xmlRpcDetails.url">
			<fmt:param>${pebbleContext.configuration.url}</fmt:param>
		  </fmt:message>
          <br />
          Handler : blogger or metaWeblog
          <br />
          Blog ID : ${blog.id}
        </td>
      </tr>

      <tr class="even">
        <td colspan="2">
          <b>Permalink Provider</b><br />
            ${blog.permalinkProvider['class'].name}
        </td>
      </tr>

      <tr class="odd">
        <td colspan="2">
          <b>Content Decorators</b><br />
          <c:forEach var="decorator" items="${blog.contentDecoratorChain.contentDecorators}">
            ${decorator['class'].name}<br />
          </c:forEach>
        </td>
      </tr>

      <tr class="even">
        <td colspan="2">
          <b>Blog Listeners</b><br />
          <c:forEach var="listener" items="${blog.eventListenerList.blogListeners}">
            ${listener['class'].name}
          </c:forEach>
        </td>
      </tr>

      <tr class="odd">
        <td colspan="2">
          <b>Blog Entry Listeners</b><br />
          <c:forEach var="listener" items="${blog.eventListenerList.blogEntryListeners}">
            ${listener['class'].name}<br />
          </c:forEach>
        </td>
      </tr>

      <tr class="even">
        <td colspan="2">
          <b>Comment Listeners</b><br />
          <c:forEach var="listener" items="${blog.eventListenerList.commentListeners}">
            ${listener['class'].name}<br />
          </c:forEach>
        </td>
      </tr>

      <tr class="odd">
        <td colspan="2">
          <b>Comment Confirmation Strategy</b><br />
          ${blog.commentConfirmationStrategy['class'].name}
        </td>
      </tr>

      <tr class="even">
        <td colspan="2">
          <b>TrackBack Listeners</b><br />
          <c:forEach var="listener" items="${blog.eventListenerList.trackBackListeners}">
            ${listener['class'].name}<br />
          </c:forEach>
        </td>
      </tr>

    <tr class="odd">
      <td colspan="2">
        <b>TrackBack Confirmation Strategy</b><br />
          ${blog.trackBackConfirmationStrategy['class'].name}
      </td>
    </tr>

    <tr class="even">
      <td colspan="2">
        <b>Lucene Analyzer</b><br />
        ${blog.luceneAnalyzer}
      </td>
    </tr>

    <tr class="odd">
      <td colspan="2">
        <b>Logger</b><br />
        ${blog.logger['class'].name}
      </td>
    </tr>
    </table>

    <div style="overflow: auto;">
    <table width="99%" cellpadding="4" cellspacing="0" class="small">
    <tr class="even">
      <td>
        <b>Properties</b><pre>${blog.pluginProperties.propertiesAsString}</pre>
      </td>
    </tr>
    </table>
    </div>

  </div>

</div>