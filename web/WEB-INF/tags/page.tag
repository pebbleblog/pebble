<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib prefix="sidebar" tagdir="/WEB-INF/tags/sidebar" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/" %>

<fmt:setTimeZone value="${blog.timeZoneId}" scope="request" />
<fmt:setBundle basename="resources" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>

    <base href="${blog.url}" />
    <meta http-equiv="Content-Type" content="text/html; charset=${blog.characterEncoding}"/>
    <meta name="description" content="${blog.name}" />
    <meta name="abstract" content="${blog.description}" />
    <meta name="author" content="${blog.author}" />
    <meta name="generator" content="Pebble (http://pebble.sourceforge.net)" />
    <title><c:choose><c:when test="${empty title}">${blog.name}</c:when><c:otherwise>${title} - ${blog.name}</c:otherwise></c:choose></title>
    <link rel="alternate" type="application/rss+xml" title="RSS" href="${blog.url}rss.xml" />
    <link rel="alternate" type="application/rdf+xml" title="RDF" href="${blog.url}rdf.xml" />
    <link rel="alternate" type="application/atom+xml" title="Atom" href="${blog.url}atom.xml" />

    <link rel="stylesheet" href="themes/${theme}/screen.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/_pebble/print.css" type="text/css" media="print" />

    <script type="text/javascript" src="${pageContext.request.contextPath}/FCKeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pebble.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptaculous.js"></script>
    <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
    <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
    <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/Pebble.js'></script>

  </head>

  <body>

  <jsp:doBody/>

  </body>
</html>