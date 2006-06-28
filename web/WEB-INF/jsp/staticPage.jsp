<%--
  Renders a single static page, similar to blogEntry.jsp yet simpler.
--%>
<div class="contentItem">

  <%@ include file="/WEB-INF/fragments/staticPageLinks.jspf" %>

  <a name="a${staticPage.id}"></a>
  <h1><a href="${staticPage.permalink}">${staticPage.title}</a></h1>
  <h2>${staticPage.subtitle}</h2>

  <div class="contentItemBody">
    <c:out value="${staticPage.body}" escapeXml="false" />
  </div>

</div>
