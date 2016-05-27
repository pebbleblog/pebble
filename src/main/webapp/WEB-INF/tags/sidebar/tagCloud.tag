<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>

<%--
 Displays a tag cloud.

 Parameters
  - rankThreshold : the minimum tag ranking to display (1-10)
--%>
<%@ attribute name="rankThreshold"%>

<c:if test="${empty rankThreshold or rankThreshold < 1}"><c:set var="rankThreshold" value="1"/></c:if> 

<c:if test="${blogType == 'singleblog'}">
<c:if test="${not empty tags}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="tag.tags" /></span></div>
  <div class="sidebarItemBody">
    <div class="sidebarTagCloud">
    <ul>
    <c:forEach var="tag" items="${tags}" varStatus="status">
      <c:if test="${tag.rank >= rankThreshold}">
      <li><span class="tagCloud${tag.rank}"><a href="${fn:replace(url:rewrite(tag.permalink), " ", "%20")}" title="rank=<fmt:formatNumber value="${tag.rank}"/>, blog entries=<fmt:formatNumber value="${tag.numberOfBlogEntries}"/>"><c:out value="${tag.name}" escapeXml="true"/></a></span></li>
      </c:if>
    </c:forEach>
    </ul>
    </div>
   </div>
</div>
</c:if>
</c:if>