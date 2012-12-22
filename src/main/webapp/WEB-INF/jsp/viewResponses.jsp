  <div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/commentAndTrackbackSpam.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1>
  <c:choose>
    <c:when test="${param.type == 'pending'}">Pending responses</c:when>
    <c:when test="${param.type == 'rejected'}">Rejected responses</c:when>
    <c:otherwise>Approved responses</c:otherwise>
  </c:choose>
  </h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <div align="center">
    <a href="viewResponses.secureaction?type=approved">Approved (<fmt:formatNumber value="${blog.numberOfApprovedResponses}" />)</a> |
    <a href="viewResponses.secureaction?type=pending">Pending (<fmt:formatNumber value="${blog.numberOfPendingResponses}" />)</a> |
    <a href="viewResponses.secureaction?type=rejected">Rejected (<fmt:formatNumber value="${blog.numberOfRejectedResponses}" />)</a>
    </div>
    <br />

    <c:set var="pageableUrl" value="viewResponses.secureaction?type=${type}" scope="request" />
    <jsp:include page="/WEB-INF/fragments/pageable.jsp">
      <jsp:param name="url" value="${pageableUrl}" />
    </jsp:include>
    <br />

    <form name="manageResponsesForm" action="manageResponses.secureaction" method="post">
    <pebble:token/>
    <input type="hidden" name="type" value="${param.type == 'pending' ? 'pending' : param.type == 'rejected' ? 'rejected' : ''}" />
    <input type="hidden" name="page" value="${page}" />

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th><input type="checkbox" name="allResponses" onclick="toggleCheckAll(document.manageResponsesForm.allResponses, document.manageResponsesForm.response)"/></th>
          <th>Source</th>
          <th>Summary</th>
          <th align="right">Date/time</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach var="response" items="${pageable.listForPage}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even">
          </c:when>
          <c:otherwise>
              <tr class="odd">
          </c:otherwise>
        </c:choose>
        <td valign="top" class="small">
          <input type="checkbox" name="response" value="${response.guid}" />
        </td>
        <td valign="top" class="small">
          <c:if test="${!empty response.sourceLink}">
            <a href="<c:out value="${response.sourceLink}" escapeXml="true"/>" target="_blank" rel="nofollow"><c:out value="${response.sourceName}" escapeXml="true"/></a>
          </c:if>
          <c:if test="${empty response.sourceLink}">
            <c:out value="${response.sourceName}" escapeXml="true"/>
          </c:if>
          <br />
          <c:if test="${response.hasEmail}">
            <c:out value="${response.email}" escapeXml="true" default="-" />
            <br />
          </c:if>
          <c:out value="${response.ipAddress}" default="-" />
        </td>
        <td valign="top" class="small">
          <a href="<c:out value="${url:rewrite(response.permalink)}"/>" title="Go to this response"><c:out value="${response.title}"/></a>
          <br />
          <c:out value="${response.truncatedContent}" escapeXml="true" />
        </td>
        <td align="right" valign="top" class="small">
          <fmt:formatDate value="${response.date}" type="date" dateStyle="short"/>
          <br />
          <fmt:formatDate value="${response.date}" type="time" timeStyle="short"/>
        </td>
      </tr>
      </c:forEach>
      </tbody>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
          <c:choose>
            <c:when test="${param.type == 'pending'}">
              <button name="submit" type="submit" Value="Approve"><fmt:message key="admin.approve"/></button>
			  <button name="submit" type="submit" Value="Reject"><fmt:message key="admin.reject"/></button>
			  <button name="submit" type="submit" Value="Remove"><fmt:message key="admin.remove"/></button>
            </c:when>
            <c:when test="${param.type == 'rejected'}">
              <button name="submit" type="submit" Value="Approve"><fmt:message key="admin.approve"/></button>
			  <button name="submit" type="submit" Value="Remove"><fmt:message key="admin.remove"/></button>
            </c:when>
            <c:otherwise>
			  <button name="submit" type="submit" Value="Reject"><fmt:message key="admin.reject"/></button>
			  <button name="submit" type="submit" Value="Remove"><fmt:message key="admin.remove"/></button>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>

    </form>

    <c:set var="pageableUrl" value="viewResponses.secureaction?type=${type}" scope="request" />
    <jsp:include page="/WEB-INF/fragments/pageable.jsp">
      <jsp:param name="url" value="${pageableUrl}" />
    </jsp:include>
  </div>

</div>