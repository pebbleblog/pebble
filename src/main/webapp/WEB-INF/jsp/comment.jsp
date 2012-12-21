<jsp:useBean id="comment" scope="request" type="net.sourceforge.pebble.domain.Comment"/>
<jsp:useBean id="blogEntry" scope="request" type="net.sourceforge.pebble.domain.BlogEntry"/>
<%--
  renders a single comment
--%>

<c:set var="nestedLevel" scope="page">
	<c:choose>
	  <c:when test="${comment.numberOfParents < 10}">
	    ${comment.numberOfParents}
	  </c:when>
	  <c:otherwise>
	    10
	  </c:otherwise>
	</c:choose>
</c:set>




<c:choose>
  <c:when test="${comment.pending}">
<div id="${param.commentIdentifier}" class="response pending responseLevel${nestedLevel}">
  </c:when>
  <c:when test="${comment.rejected}">
<div id="${param.commentIdentifier}" class="response rejected responseLevel${nestedLevel}">
  </c:when>
  <c:otherwise>
<div id="${param.commentIdentifier}" class="response approved responseLevel${nestedLevel}">
  </c:otherwise>
</c:choose>

  <template:avatar comment="${comment}" blogEntry="${blogEntry}"/>
  <%@ include file="/WEB-INF/fragments/commentLinks.jspf" %>
  <h1>
    <span id="${param.commentIdentifier}.title"><a name="comment${comment.id}"></a>${comment.title}</span>
  </h1>

  <div class="metadata">
    <c:set var="commentAuthor" scope="page">
      <c:if test="${!empty comment.website}">
        <a href="<c:out value="${comment.website}"/>" target="_blank" title="<c:out value="${comment.website}"/>" rel="nofollow"><c:out value="${comment.author}"/></a>
      </c:if>
      <c:if test="${empty comment.website}">
        ${comment.author}
      </c:if>
      <pebble:isAuthorisedForBlog>
        (<c:out value="${comment.email}" default="-" />/<c:out value="${comment.ipAddress}" default="-" />)
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
