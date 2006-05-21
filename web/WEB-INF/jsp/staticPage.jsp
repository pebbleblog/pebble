<%--
  Renders a single static page, similar to blogEntry.jsp yet simpler.
--%>
<div class="contentItem">

  <%@ include file="/WEB-INF/fragments/staticPageLinks.jspf" %>

  <a name="a${blogEntry.id}"></a>
  <div class="title"><a href="${blogEntry.permalink}">${blogEntry.title}</a></div>
  <div class="subtitle">${blogEntry.subtitle}</div>

  <div class="contentItemBody">
    <c:out value="${blogEntry.body}" escapeXml="false" />
  </div>

</div>
