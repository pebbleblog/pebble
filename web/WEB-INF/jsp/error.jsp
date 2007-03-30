<%@ page isErrorPage="true" %>

<div class="contentItem">

  <h1><fmt:message key="error.errorTitle" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <fmt:message key="error.error" /> <a href="#" id="stacktraceLink" onclick="Effect.Appear('stacktrace', 'blind'); Effect.Fade('stacktraceLink', 'blind'); return false">[...]</a>
    <br /><br />

    <textarea id="stacktrace" rows="40" cols="60" readonly="true" style="display: none;">
${stackTrace}
Request URL : ${pageContext.request.requestURL}
Request URI : ${pageContext.request.requestURI}
Query string : ${pageContext.request.queryString}
External URI : ${externalUri}
Internal URI : ${internalUri}
Parameters : <c:forEach var="entry" items="${paramValues}">
<c:forEach var="value" items="${entry.value}">${entry.key} = ${value}
</c:forEach></c:forEach>
</textarea>
  </div>

</div>
