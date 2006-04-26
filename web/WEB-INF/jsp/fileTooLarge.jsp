<div class="contentItem">

  <div class="title"><fmt:message key="error.fileTooLargeTitle" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <c:set var="fileUploadSize" scope="page">
      <fmt:formatNumber value="${pebbleContext.fileUploadSize}" type="number" />&nbsp;KB
    </c:set>
    <fmt:message key="error.fileTooLarge">
      <fmt:param value="${fileUploadSize}"/>
    </fmt:message>
  </div>

</div>