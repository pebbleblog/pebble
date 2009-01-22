<%--
  Renders a single static page, similar to blogEntry.jsp yet simpler.
--%>
<div class="contentItem">

  <%@ include file="/WEB-INF/fragments/staticPageLinks.jspf" %>

  <a name="a${staticPage.id}"></a>
  <c:if test="${not empty staticPage.title}">
  <h1><a href="${url:rewrite(staticPage.permalink)}">${staticPage.title}</a></h1>
  <h2>${staticPage.subtitle}</h2>
  </c:if>

  <div class="contentItemBody">
    <c:out value="${staticPage.body}" escapeXml="false" />
  </div>

</div>
