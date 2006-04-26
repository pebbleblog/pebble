<%--
  Renders a collection of TrackBacks, calling trackback.jsp for each TrackBack.

  ${blog} : the current Blog instance
  ${blogEntry} : the current BlogEntry instance
--%>
<a name="trackbacks"></a>
<div class="contentItem">

  <div class="contentItemLinks">
    <c:if test="${blogEntry.trackBacksEnabled}">
    ${blog.url}addTrackBack.action?entry=${blogEntry.id}<br />
    </c:if>
    <pebble:isBlogContributor>
    <a href="sendTrackBack.secureaction?entry=${blogEntry.id}">Send TrackBack</a>
    </pebble:isBlogContributor>
  </div>

  <div class="title"><fmt:message key="blogentry.trackbacks" /></div>
  <div class="subtitle">&nbsp;</div>

  <c:choose>
    <c:when test="${blogEntry.numberOfTrackBacks != 0}" >
      <c:forEach var="trackback" items="${blogEntry.trackBacks}" varStatus="status">
        <c:set var="trackback" scope="request" value="${trackback}"/>
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <div class="even">
          </c:when>
          <c:otherwise>
            <div class="odd">
          </c:otherwise>
        </c:choose>
          <jsp:include page="trackback.jsp"/>
          </div>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="contentItemBody" style="padding: 8px">
        <fmt:message key="trackback.noTrackbacks" />
      </div>
    </c:otherwise>
  </c:choose>

</div>