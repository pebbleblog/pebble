<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>

<%@attribute name="showRSS"%>
<%@attribute name="showAtom"%>

<c:if test="${empty showRSS}"><c:set var="showRSS" value="true"/></c:if>
<c:if test="${empty showAtom}"><c:set var="showAtom" value="false"/></c:if>

<c:if test="${showRSS eq 'true'}">
<a title="Subscribe to blog via RSS feed" href="${url:rewrite(blog.url)}rss.xml"></a> <a href="${url:rewrite(blog.url)}rss.xml" title="Subscribe to blog via RSS feed" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="RSS feed" border="0" valign="top" /></a>
</c:if>

<c:if test="${showAtom eq 'true'}">
<a title="Subscribe to blog via Atom feed" href="${url:rewrite(blog.url)}atom.xml"></a> <a href="${url:rewrite(blog.url)}atom.xml" title="Subscribe to blog via Atom feed" style="border: 0px;"><img src="common/images/feed-icon-16x16.png" alt="Atom feed" border="0" valign="top" /></a>
</c:if>