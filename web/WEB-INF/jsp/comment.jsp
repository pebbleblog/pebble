<%--
  renders a single comment
--%>
<c:choose>
  <c:when test="${comment.pending}">
<div id="${param.commentIdentifier}" class="response pending" style="padding-left: ${(comment.numberOfParents*16)+30}px;">
  </c:when>
  <c:when test="${comment.rejected}">
<div id="${param.commentIdentifier}" class="response rejected" style="padding-left: ${(comment.numberOfParents*16)+30}px;">
  </c:when>
  <c:otherwise>
<div id="${param.commentIdentifier}" class="response approved" style="padding-left: ${(comment.numberOfParents*16)+30}px;">
  </c:otherwise>
</c:choose>

  <%@ include file="/WEB-INF/fragments/commentLinks.jspf" %>

  <h1>
    <span id="${param.commentIdentifier}.title"><a name="comment${comment.id}"></a>${comment.title}</span>
  </h1>

  <div class="metadata">
    <c:set var="commentAuthor" scope="page">
      <c:if test="${!empty comment.website}">
        <a href="<c:out value="${comment.website}" escapeXml="true"/>" target="_blank" title="<c:out value="${comment.website}"/>" rel="nofollow"><c:out value="${comment.author}" escapeXml="true"/></a>
      </c:if>
      <c:if test="${empty comment.website}">
        ${comment.author}
      </c:if>
      <pebble:isAuthorisedForBlog>
        (<c:out value="${comment.email}" escapeXml="true" default="-" />/<c:out value="${comment.ipAddress}" default="-" />)
      </pebble:isAuthorisedForBlog>
    </c:set>
    <fmt:formatDate var="commentDate" scope="page" value="${comment.date}" timeZone="${blogEntry.timeZoneId}" type="both" dateStyle="long" timeStyle="long"/>

    <fmt:message key="comment.from">
      <fmt:param>
        <c:choose>
        <c:when test="${comment.authenticated}">
          <span id="${param.commentIdentifier}.author" class="authenticated">${commentAuthor}</span>
        </c:when>
        <c:otherwise>
          <span id="${param.commentIdentifier}.author" class="unauthenticated">${commentAuthor}</span>
        </c:otherwise>
        </c:choose>
      </fmt:param>
      <fmt:param value="${commentDate}"/>
    </fmt:message>
    <a href="${url:rewrite(comment.permalink)}" title="${comment.permalink}">#</a>
  </div>

  <div id="${param.commentIdentifier}.body" class="responseBody">
    <c:out value="${comment.body}" escapeXml="false"/>
  </div>

</div>
