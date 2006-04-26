<%--
  renders a single comment
--%>
<c:choose>
  <c:when test="${comment.pending}">
<div class="response pending">
  </c:when>
  <c:when test="${comment.rejected}">
<div class="response rejected">
  </c:when>
  <c:otherwise>
<div class="response approved">
  </c:otherwise>
</c:choose>

  <%@ include file="/WEB-INF/fragments/commentLinks.jspf" %>

  <div class="title">
    <a name="comment${comment.id}">${comment.title}</a>
  </div>

  <div class="metadata">
    <c:set var="commentAuthor" scope="page">
      <c:if test="${!empty comment.website}">
        <a href="<c:out value="${comment.website}" escapeXml="true"/>" target="_blank" title="<c:out value="${comment.website}"/>" rel="nofollow"><c:out value="${comment.author}" escapeXml="true"/></a>
      </c:if>
      <c:if test="${empty comment.website}">
        ${comment.author}
      </c:if>
      <pebble:isBlogOwnerOrContributor>
        (<c:out value="${comment.email}" escapeXml="true" default="-" />/<c:out value="${comment.ipAddress}" default="-" />)
      </pebble:isBlogOwnerOrContributor>
    </c:set>
    <fmt:formatDate var="commentDate" scope="page" value="${comment.date}" type="both" dateStyle="long" timeStyle="long"/>

    <fmt:message key="comment.from">
      <fmt:param>
        ${commentAuthor}
      </fmt:param>
      <fmt:param value="${commentDate}"/>
    </fmt:message>
    <a href="${comment.permalink}" title="${comment.permalink}">#</a>
  </div>

  <div class="responseBody">
    <c:out value="${comment.body}" escapeXml="false"/>
  </div>

</div>
