<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>

<%--
 Reads the specified feed and formats it for the sidebar. Each entry is rendered as (approximately) :

  <a href="entryLink">entryTitle</a><br />
  entryBody

 Parameters
  - name : the name of the feed, which is displayed in the sidebar item title
  - url : the URL of the RSS/RDF/Atom feed
  - maxEntries : the maximum number of entries to display from this feed
  - showBody : flag to indicate whether the body of the entry should be displayed
  - truncateBody : flag to indicate whether the body of the entry should be truncated (i.e. HTML stripped and truncated to 255 characters max)
--%>
<%@ attribute name="name"%>
<%@ attribute name="url"%>
<%@ attribute name="maxEntries"%>
<%@ attribute name="showBody"%>
<%@ attribute name="truncateBody"%>

<c:if test="${empty showBody}"><c:set var="showBody" value="true"/></c:if>
<c:if test="${empty truncateBody}"><c:set var="truncateBody" value="true"/></c:if> 

<c:catch var="e">
<pebble:getFeed url="${url}"/>
<c:if test="${not empty feedEntries}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span>${name}&nbsp;<a href="${url}" title="${url}" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></div>
  <div class="sidebarItemBody">
    <ul>
      <c:forEach var="entry" items="${feedEntries}" end="${maxEntries-1}">
        <li>
          <a href="${fn:escapeXml(url:rewrite(entry.link))}" title="${fn:escapeXml(name)} - ${fn:escapeXml(entry.title)}" target="_blank">${fn:escapeXml(entry.title)}</a>
          <c:if test="${showBody eq 'true'}">
          <br />
          <c:choose>
            <c:when test="${truncateBody eq 'false'}">${entry.body}</c:when>
            <c:otherwise>${entry.truncatedBody}</c:otherwise>
          </c:choose>
          </c:if>
        </li>
      </c:forEach>
    </ul>
  </div>
</div>
</c:if>
</c:catch>
<c:if test="${not empty e}"><!-- Exception with feed URL ${url} : ${e} --></c:if>
