<%--
  The sidebar that includes the calendar, recent blog entries, links, etc.
--%>
<div id="sidebar">

  <%@ include file="/WEB-INF/fragments/admin.jspf" %>

  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span><fmt:message key="sidebar.navigate" /></span></div>
    <pebble:calendar/>
    <a href="./categories/"><fmt:message key="category.categories" /></a> |
    <a href="./tags/"><fmt:message key="tag.tags" /></a> |
    <a href="search.action"><fmt:message key="search.advancedSearch" /></a>
    <br /><br />
  </div>

  <%-- Uncomment this block if you would like to show archives by month.
  --%>
  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span>Archives</span></div>
    <table width="99%" cellpadding="0" cellspacing="0">
      <c:forEach var="year" items="${archives}" varStatus="status">
        <c:if test="${status.count % 2 == 1}">
        <tr>
        </c:if>
        <td valign="top">
          <b><fmt:formatDate value="${year.date}" pattern="yyyy"/></b><br />
          <c:forEach var="month" items="${year.archives}">
          <a href="${month.permalink}"><fmt:formatDate value="${month.date}" pattern="MMMM"/></a> (<fmt:formatNumber value="${month.numberOfBlogEntries}" type="number" />)
          <br />
          </c:forEach>
          <br />
        </td>
        <c:if test="${status.count % 2 == 0}">
        </tr>
        </c:if>
      </c:forEach>
    </table>
  </div>

  <c:if test="${not empty categories[0].subCategories}">
  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span><fmt:message key="category.categories" /></span></div>
    <c:forEach var="category" items="${categories}" begin="1" varStatus="status">
      <a href="${category.permalink}"><c:out value="${category.name}" escapeXml="true"/></a> (<fmt:formatNumber value="${category.numberOfBlogEntries}" type="number" />)
      <a href="${category.permalink}rss.xml" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a>
      <br />
    </c:forEach>
  </div>
  </c:if>

  <c:if test="${not empty tags}">
  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span><fmt:message key="tag.tags" /></span></div>
    <div class="sidebarTagCloud">
    <ul>
    <c:forEach var="tag" items="${tags}" varStatus="status">
      <li><span class="tagCloud${tag.rank}"><a href="${tag.permalink}" title="<fmt:formatNumber value='${tag.numberOfBlogEntries}'/>"><c:out value="${tag.name}" escapeXml="true"/></a></span></li>
    </c:forEach>
    </ul>
    </div>
  </div>
  </c:if>

  <c:if test="${not empty recentBlogEntries}">
  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span><fmt:message key="sidebar.recentBlogEntries" /></span></div>
    <ul>
    <c:forEach var="recentBlogEntry" items="${recentBlogEntries}" >
      <li><a href="${recentBlogEntry.permalink}" title="${recentBlogEntry.permalink}">${recentBlogEntry.title}</a><br />${recentBlogEntry.truncatedContent}</li>
    </c:forEach>
    </ul>
  </div>
  </c:if>

  <c:if test="${not empty recentResponses && blog.recentResponsesOnHomePage > 0}">
  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span><fmt:message key="sidebar.recentResponses" /> <a href="responses/rss.xml"><img src="common/images/feed-icon-10x10.png" alt="RSS feed for responses" border="0" /></a></span></div>
    <ul>
    <c:forEach var="aResponse" items="${recentResponses}" varStatus="status" end="${blog.recentResponsesOnHomePage-1}">
      <li><a href="${aResponse.permalink}" title="${aResponse.permalink}">${aResponse.title}</a><br />${aResponse.truncatedContent}</li>
    </c:forEach>
    </ul>
  </div>
  </c:if>

  <%--
    To show your del.icio.us links on your blog, add the following to your plugin properties.
     - delicious.username
     - delicious.password
  --%>
  <c:set var="deliciousUsername" value='${pluginProperties.properties["delicious.username"]}' />
  <c:set var="deliciousPassword" value='${pluginProperties.properties["delicious.password"]}' />
  <c:if test="${not empty deliciousUsername}">
  <delicious:recentPosts var="deliciousPosts" username="${deliciousUsername}" password="${deliciousPassword}" count="3" />
  <div class="sidebarItem">
    <div class="sidebarItemTitle"><span>del.icio.us <a href="http://del.icio.us/rss/${deliciousUsername}" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
    <ul>
    <c:forEach var="post" items="${deliciousPosts}">
      <li><a href="${post.href}">${post.description}</a><br /><c:if test="${not empty post.extended}">${post.extended}<br /></c:if></li>
    </c:forEach>
    </ul>
    <br />
    <a href="http://del.icio.us/${deliciousUsername}"><fmt:message key="common.readMore" /></a>
    <br /><br />
  </div>
  </c:if>

</div>
