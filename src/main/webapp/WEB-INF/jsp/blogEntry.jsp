<%--
  renders a single blog entry
--%>
<c:choose>
  <c:when test="${blogEntry.unpublished}">
<div class="contentItem unpublished">
  </c:when>
  <c:otherwise>
<div class="contentItem published">
  </c:otherwise>
</c:choose>

  <%@ include file="/WEB-INF/fragments/blogEntryLinks.jspf" %>

  <a name="a${blogEntry.id}"></a>
  <h1 class="contentItemTitle"><a href="${url:rewrite(blogEntry.permalink)}">${blogEntry.title}</a></h1>
  <h2 class="contentItemSubtitle">${blogEntry.subtitle}</h2>

  <div class="contentItemBody">
    <c:choose>
      <c:when test="${displayMode == 'detail'}">
        <%-- here the blog entry is being shown on its own page --%>
        <c:out value="${blogEntry.body}" escapeXml="false" />
      </c:when>
      <c:when test="${displayMode == 'preview'}">
        <%-- this is the blog entry preview, where both excerpt and body need to be shown --%>
        <c:if test="${not empty blogEntry.excerpt}">
        <p><b>Excerpt</b></p><c:out value="${blogEntry.excerpt}" escapeXml="false" />
        </c:if>
        <c:if test="${not empty blogEntry.body}">
        <p><b>Body</b></p><c:out value="${blogEntry.body}" escapeXml="false" />
        </c:if>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${empty blogEntry.excerpt}">
            <%-- there is no excerpt, so show the body --%>
            <c:out value="${blogEntry.body}" escapeXml="false" />
          </c:when>
          <c:otherwise>
            <%-- an excerpt is present, so show this --%>
            <c:out value="${blogEntry.excerpt}" escapeXml="false" />
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>

    <div class="metadata">
      <%@ include file="/WEB-INF/fragments/responseLinks.jspf" %>
      <c:choose>
        <c:when test="${not empty blogEntry.user.website}"><c:set var="author" value='<a href="${blogEntry.user.website}">${blogEntry.user.name}</a>'/></c:when>
        <c:when test="${not empty blogEntry.user and not empty blogEntry.user.profile}"><c:set var="author" value='<a href="authors/${blogEntry.user.username}/">${blogEntry.user.name}</a>'/></c:when>
        <c:when test="${not empty blogEntry.user}"><c:set var="author" value='<a href="authors/${blogEntry.author}/">${blogEntry.user.name}</a>'/></c:when>
        <c:otherwise><c:set var="author" value='<a href="advancedSearch.action?author=${blogEntry.author}">${blogEntry.author}</a>'/></c:otherwise>
      </c:choose>
      <fmt:formatDate var="blogEntryDate" scope="page" value="${blogEntry.date}" timeZone="${blogEntry.timeZoneId}" type="both" dateStyle="long" timeStyle="long"/>
      <fmt:message key="blogentry.from">
        <fmt:param value="${author}"/>
        <fmt:param>
        ${blogEntryDate}
        </fmt:param>
      </fmt:message>
      <a href="${url:rewrite(blogEntry.localPermalink)}" title="${blogEntry.localPermalink}">#</a>
      <c:if test="${not empty blogEntry.attachment}"><a href="${url:rewrite(blogEntry.attachment.url)}" title="${blogEntry.attachment.size} bytes, ${blogEntry.attachment.type}"><fmt:message key="blogentry.attachment" /></a></c:if>
    </div>
  </div>

  <c:if test="${displayMode == 'detail'}">

    <pebble:isBlogContributor>
    <c:if test="${blogEntry.numberOfResponses > 0}">
    <form name="responsesForm" method="post" action="manageResponses.secureaction">
      <pebble:token/>
    <input type="hidden" name="redirectUrl" value="${url:rewrite(blogEntry.localPermalink)}" />
    </c:if>
    </pebble:isBlogContributor>

    <a name="responses"></a>
    <jsp:include page="/WEB-INF/jsp/comments.jsp"/>
    <jsp:include page="/WEB-INF/jsp/trackbacks.jsp"/>

    <pebble:isBlogContributor>
    <c:if test="${blogEntry.numberOfResponses > 0}">
    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.responsesForm.response)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.responsesForm.response)" />
        </td>
        <td align="right">
          <button type="submit" name="submit" value="Approve" ><fmt:message key="common.approve"/></button>
          <button type="submit" name="submit" value="Reject" ><fmt:message key="common.reject"/></button>
          <button type="submit" name="submit" value="Remove" ><fmt:message key="common.remove"/></button>
        </td>
      </tr>
    </table>
    </form>
    </c:if>
    </pebble:isBlogContributor>

    <div align="center" class="commentsAndTrackbacks">
      <c:if test="${blogEntry.commentsEnabled}">
        <a href="javascript: showCommentForm();"><fmt:message key="comment.addComment"/></a>
      </c:if>
      <c:if test="${blogEntry.trackBacksEnabled}">
        <a href="generateTrackBackLink.action?entry=${blogEntry.id}"><fmt:message key="trackback.sendTrackBack"/></a>
      </c:if>
    </div>

    <div id="commentFormDiv" style="display:none;"><%@ include file="/WEB-INF/fragments/commentForm.jsp" %></div>

  </c:if>

</div>