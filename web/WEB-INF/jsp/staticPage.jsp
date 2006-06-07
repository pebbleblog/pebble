<%--
  Renders a single static page, similar to blogEntry.jsp yet simpler.
--%>
<div class="contentItem">

  <%@ include file="/WEB-INF/fragments/staticPageLinks.jspf" %>

  <a name="a${staticPage.id}"></a>
  <div class="title"><a href="${staticPage.permalink}">${staticPage.title}</a></div>
  <div class="subtitle">${staticPage.subtitle}</div>

  <div class="contentItemBody">
    <c:out value="${staticPage.body}" escapeXml="false" />
  </div>

</div>
