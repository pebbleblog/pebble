<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib prefix="sidebar" tagdir="/WEB-INF/tags/sidebar" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="url" uri="/WEB-INF/url.tld" %>

<fmt:setTimeZone value="${blog.timeZoneId}" scope="request" />
<fmt:setBundle basename="resources" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml" xmlns:og="http://ogp.me/ns#">
  <head>

    <c:set var="pageTitle">
      <c:choose><c:when test="${empty title}"><c:out value="${blog.name}" escapeXml="true"/></c:when><c:otherwise><c:out value="${title} - ${blog.name}" escapeXml="true"/></c:otherwise></c:choose>
    </c:set>
    <c:set var="pageTitleAlone">
      <c:choose><c:when test="${empty title}"><c:out value="${blog.name}" escapeXml="true"/></c:when><c:otherwise><c:out value="${title}" escapeXml="true"/></c:otherwise></c:choose>
    </c:set>
    <c:if test="${displayMode == 'detail'}">
      <c:choose>
        <c:when test="${not empty blogEntry}">
          <c:set var="tags" value="${blogEntry.allTags}"/>
        </c:when>
        <c:when test="${not empty staticPage}">
          <c:set var="tags" value="${staticPage.allTags}"/>
        </c:when>
      </c:choose>
    </c:if>
    <c:set var="truncatedContent">
      <c:if test="${displayMode == 'detail'}">
        <c:choose>
          <c:when test="${not empty blogEntry}"><c:out value="${blogEntry.truncatedContent}" escapeXml="true"/></c:when>
          <c:when test="${not empty staticPage}"><c:out value="${staticPage.truncatedContent}" escapeXml="true"/></c:when>
        </c:choose>
      </c:if>
    </c:set>
    <c:if test="${not empty blogEntry}">
      <c:forEach var="category" items="${blogEntry.categories}">
        <c:set var="cat" value="${category}"/>
      </c:forEach>
    </c:if>

    <base href="${url:rewrite(blogUrl)}" />
    <meta http-equiv="Content-Type" content="text/html; charset=${blog.characterEncoding}"/>
    <meta name="description" content="${pageTitle}" />
    <c:if test="${displayMode == 'detail'}"><c:choose><c:when test="${not empty blogEntry}"><meta name="keywords" content="${blogEntry.tagsAsCommaSeparated}" /></c:when><c:when test="${not empty staticPage}"><meta name="keywords" content="${staticPage.tagsAsCommaSeparated}" /></c:when></c:choose></c:if>
    <meta name="abstract" content="${blog.description}" />
    <meta name="author" content="${blog.author}" />
    <meta name="generator" content="Pebble (http://pebble.sourceforge.net)" />
    <title>${pageTitle}</title>
    <link rel="alternate" type="application/rss+xml" title="RSS" href="${blog.url}rss.xml" />
    <link rel="alternate" type="application/rdf+xml" title="RDF" href="${blog.url}rdf.xml" />
    <link rel="alternate" type="application/atom+xml" title="Atom" href="${blog.url}atom.xml" />

    <link rel="stylesheet" href="pebble.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/_pebble/handheld.css" type="text/css" media="handheld" />
    <link rel="stylesheet" href="themes/${theme}/screen.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/${theme}/print.css" type="text/css" media="print" />

    <link rel="shortcut icon" href="${pageContext.request.contextPath}/themes/${theme}/images/favicon.ico" type="image/x-icon" />

    <script type="text/javascript" src="${pageContext.request.contextPath}/FCKeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pebble.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptaculous.js"></script>
    <script type='text/javascript' src='${pageContext.request.contextPath}/scripts/dwr-engine.js'></script>
    <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
    <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/Pebble.js'></script>

    <pebble:pluginrenderer plugin="head"/>
    <c:if test="${not empty themeHeadUri}"><jsp:include page="${themeHeadUri}"/></c:if>

    <meta property="og:locale" content="${blog.locale}"/>
    <meta property="og:title" content="${pageTitleAlone}"/>
    <meta property="og:type" content="<c:choose><c:when test="${displayMode == 'detail'}">article</c:when><c:otherwise>website</c:otherwise></c:choose>"/>
    <meta property="og:description" content="${truncatedContent}"/>
    <meta property="og:image" content="${blogEntry.thumbnailURL}"/>
    <meta property="og:site_name" content="<c:out value="${blog.name}" escapeXml="true"/>"/>
    <c:if test="${displayMode == 'detail'}"><c:choose><c:when test="${not empty blogEntry}"><meta property="og:url" content="${url:rewrite(blogEntry.permalink)}" /></c:when><c:when test="${not empty staticPage}"><meta property="og:url" content="${url:rewrite(staticPage.permalink)}" /></c:when></c:choose></c:if>
    <c:if test="${displayMode == 'detail'}"><c:forEach var="token" items="${tags}">
    <meta property="article:tag" content="<c:out value="${token}" escapeXml="true"/>" /></c:forEach> </c:if>
    <c:if test="${not empty cat}"><meta property="article:section" content="<c:out value="${cat}" escapeXml="true"/>"></c:if>
    <c:if test="${displayMode == 'detail'}"><c:choose><c:when test="${not empty blogEntry}"><meta property="article:published_time" content="<fmt:formatDate pattern="yyyy-MM-dd'T'HH:mm:ssz" value="${blogEntry.date}" />"/></c:when><c:when test="${not empty staticPage}"><meta property="article:published_time" content="<fmt:formatDate pattern="yyyy-MM-dd'T'HH:mm:ssz" value="${staticPage.date}" />"/></c:when></c:choose></c:if>
    <c:if test="${displayMode == 'detail'}"><c:choose><c:when test="${not empty blogEntry}"><meta property="article:modified_time" content="<fmt:formatDate pattern="yyyy-MM-dd'T'HH:mm:ssz" value="${blogEntry.lastModified}" />"/></c:when><c:when test="${not empty staticPage}"><meta property="article:modified_time" content="<fmt:formatDate pattern="yyyy-MM-dd'T'HH:mm:ssz" value="${staticPage.lastModified}" />"/></c:when></c:choose></c:if>


    <meta name="twitter:site" content="<c:out value="${blog.name}" escapeXml="true"/>" />
    <meta name="twitter:title" content="${pageTitleAlone}"/>
    <meta name="twitter:card" content="summary"/>
    <meta name="twitter:description" content="${truncatedContent}" />
    <meta name="twitter:image" content="${blogEntry.thumbnailURL}" />
    <c:if test="${displayMode == 'detail'}"><c:choose><c:when test="${not empty blogEntry}"><meta name="twitter:url" content="${url:rewrite(blogEntry.permalink)}" /></c:when><c:when test="${not empty staticPage}"><meta name="twitter:url" content="${url:rewrite(staticPage.permalink)}" /></c:when></c:choose></c:if>
  </head>

  <body>

  <pebble:pluginrenderer plugin="header"/>

  <%@ include file="/WEB-INF/fragments/admin.jspf" %>

  <jsp:doBody/>

  <pebble:pluginrenderer plugin="footer"/>

  </body>
</html>
