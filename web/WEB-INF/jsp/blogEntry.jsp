<%--
  renders a single blog entry
--%>
<c:choose>
  <c:when test="${blogEntry.pending}">
<div class="contentItem pending">
  </c:when>
  <c:when test="${blogEntry.rejected}">
<div class="contentItem rejected">
  </c:when>
  <c:otherwise>
<div class="contentItem approved">
  </c:otherwise>
</c:choose>

  <%@ include file="/WEB-INF/fragments/blogEntryLinks.jspf" %>

  <a name="a${blogEntry.id}"></a>
  <div class="title"><a href="${blogEntry.permalink}">${blogEntry.title}</a></div>
  <div class="subtitle">${blogEntry.subtitle}</div>

  <div class="contentItemBody">
    <c:choose>
      <c:when test="${displayMode == 'detail'}">
        <%-- here the blog entry is being shown on its own page --%>
        <c:out value="${blogEntry.body}" escapeXml="false" />
        <c:if test="${blogEntry.aggregated}">
          <p><a href="${blogEntry.permalink}"><fmt:message key="common.readMore" /></a></p>
        </c:if>
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
            <c:if test="${blogEntry.aggregated}">
              <p>
                <a href="${blogEntry.permalink}"><fmt:message key="common.readMore" /></a>
              </p>
            </c:if>
          </c:when>
          <c:otherwise>
            <%-- an excerpt is present, so show this --%>
            <c:out value="${blogEntry.excerpt}" escapeXml="false" />
            <p>
              <a href="${blogEntry.permalink}"><fmt:message key="common.readMore" /></a>
            </p>
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>
  </div>

  <div class="metadata">
    <c:if test="${not empty blogEntry.allTags}">
    <fmt:message key="tag.tags"/> :
    <c:forEach var="tag" items="${blogEntry.allTags}">
      <a href="${blog.url}tags/${tag.name}" rel="tag">${tag.name}</a>
    </c:forEach>
    </c:if>
    <br />
    <%@ include file="/WEB-INF/fragments/responseLinks.jspf" %>
    <c:choose>
      <c:when test="${not empty blogEntry.user.website}"><c:set var="author" value='<a href="${blogEntry.user.website}">${blogEntry.user.name}</a>'/></c:when>
      <c:otherwise><c:set var="author" value="${blogEntry.user.name}"/></c:otherwise>
    </c:choose>
    <fmt:formatDate var="blogEntryDate" scope="page" value="${blogEntry.date}" type="both" dateStyle="long" timeStyle="long"/>
    <fmt:message key="blogentry.from">
      <fmt:param value="${author}"/>
      <fmt:param>
        ${blogEntryDate}
      </fmt:param>
    </fmt:message>
    <a href="${blogEntry.localPermalink}" title="${blogEntry.localPermalink}">#</a>
    <c:if test="${not empty blogEntry.attachment}"><a href="${blogEntry.attachment.url}" title="${blogEntry.attachment.size} bytes, ${blogEntry.attachment.type}"><fmt:message key="blogentry.attachment" /></a></c:if>
  </div>

</div>

<c:if test="${displayMode == 'detail'}">
  <pebble:isBlogContributor>
  <form name="responsesForm" method="post" action="manageResponses.secureaction">
  <input type="hidden" name="redirectUrl" value="${blogEntry.localPermalink}" />
  </pebble:isBlogContributor>

  <jsp:include page="/WEB-INF/jsp/comments.jsp"/>
  <jsp:include page="/WEB-INF/jsp/trackbacks.jsp"/>

  <pebble:isBlogContributor>
  <br />
  <table width="99%" cellspacing="0" cellpadding="0">
    <tr>
      <td align="left">
        <input type="button" value="Check All" onclick="checkAll(document.responsesForm.response)" />
        <input type="button" value="Uncheck All" onclick="uncheckAll(document.responsesForm.response)" />
      </td>
      <td align="right">
        <input type="submit" name="submit" value="Approve" />
        <input type="submit" name="submit" value="Reject" />
        <input type="submit" name="submit" value="Remove" />
      </td>
    </tr>
  </table>
  </form>
  </pebble:isBlogContributor>
</c:if>