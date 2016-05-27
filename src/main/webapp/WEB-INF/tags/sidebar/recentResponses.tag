<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>

<%--
  Displays the recent responses (comments and TrackBacks).
--%>
<c:if test="${blogType == 'singleblog'}">
<c:if test="${not empty recentResponses && blog.recentResponsesOnHomePage > 0}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="sidebar.recentResponses" /> <a title="RSS feed for blog responses" href="responses/rss.xml"><img src="common/images/feed-icon-10x10.png" alt="RSS feed for responses" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <ul>
    <c:forEach var="aResponse" items="${recentResponses}" varStatus="status" end="${blog.recentResponsesOnHomePage-1}">
      <li><a href="${url:rewrite(aResponse.permalink)}" title="${aResponse.title} - ${aResponse.permalink}">${aResponse.title}</a><br />${aResponse.truncatedContent}</li>
    </c:forEach>
    </ul>
  </div>
</div>
</c:if>
</c:if>