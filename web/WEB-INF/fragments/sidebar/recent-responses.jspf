<c:if test="${not empty recentResponses && blog.recentResponsesOnHomePage > 0}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="sidebar.recentResponses" /> <a href="responses/rss.xml"><img src="common/images/feed-icon-10x10.png" alt="RSS feed for responses" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <ul>
    <c:forEach var="aResponse" items="${recentResponses}" varStatus="status" end="${blog.recentResponsesOnHomePage-1}">
      <li><a href="${aResponse.permalink}" title="${aResponse.permalink}">${aResponse.title}</a><br />${aResponse.truncatedContent}</li>
    </c:forEach>
    </ul>
  </div>
</div>
</c:if>