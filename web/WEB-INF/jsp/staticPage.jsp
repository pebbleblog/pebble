<%--
  Renders a single static page, similar to blogEntry.jsp yet simpler.
--%>
<div class="contentItem">

  <%@ include file="/WEB-INF/fragments/staticPageLinks.jspf" %>

  <a name="a${blogEntry.id}"></a>
  <div class="title">${blogEntry.title}</div>
  <div class="subtitle">${blogEntry.subtitle}</div>

  <div class="contentItemBody">
    <c:out value="${blogEntry.body}" escapeXml="false" />

    <c:if test="${blogEntry.aggregated}">
      <p>
        <a href="${blogEntry.permalink}"><fmt:message key="common.readMore" /></a>
      </p>
    </c:if>
  </div>

  <div class="metadata">
    <c:if test="${not empty blogEntry.attachment}"><a href="${blogEntry.attachment.url}" title="${blogEntry.attachment.size} bytes, ${blogEntry.attachment.type}"><fmt:message key="blogentry.attachment" /></a></c:if>
  </div>

</div>
