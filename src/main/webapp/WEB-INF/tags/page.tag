<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib prefix="sidebar" tagdir="/WEB-INF/tags/sidebar" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="url" uri="/WEB-INF/url.tld" %>

<fmt:setTimeZone value="${blog.timeZoneId}" scope="request" />
<fmt:setBundle basename="resources" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml">
  <head>

    <c:set var="pageTitle">
      <c:choose><c:when test="${empty title}">${blog.name}</c:when><c:otherwise>${title} - ${blog.name}</c:otherwise></c:choose>
    </c:set>

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
  </head>

  <body>

  <pebble:pluginrenderer plugin="header"/>

  <%@ include file="/WEB-INF/fragments/admin.jspf" %>

  <jsp:doBody/>

  <pebble:pluginrenderer plugin="footer"/>

  </body>
</html>
