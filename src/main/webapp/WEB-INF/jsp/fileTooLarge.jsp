<div class="contentItem">

  <h1><fmt:message key="error.fileTooLargeTitle" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <c:set var="fileUploadSize" scope="page">
      <fmt:formatNumber value="${pebbleContext.configuration.fileUploadSize}" type="number" />&nbsp;KB
    </c:set>
    <fmt:message key="error.fileTooLarge">
      <fmt:param value="${fileUploadSize}"/>
    </fmt:message>
  </div>

</div>