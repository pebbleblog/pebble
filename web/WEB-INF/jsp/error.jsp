<%@ page isErrorPage="true" %>

<div class="contentItem">

  <h1><fmt:message key="error.errorTitle" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <fmt:message key="error.error" /> <a href="#" id="stacktraceLink" onclick="Effect.Appear('stacktrace', 'blind'); Effect.Fade('stacktraceLink', 'blind'); return false">[...]</a>
    <br /><br />

<div id="stacktrace" style="display: none;">
<textarea rows="20" cols="60" readonly="true">
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

<p>
  If you were editing your theme and have caused this error, click <a href="resetTheme.secureaction">here</a>
  to switch back to the default theme.
</p>
</div>

    </div>

</div>
