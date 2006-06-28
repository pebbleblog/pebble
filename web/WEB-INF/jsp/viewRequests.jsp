<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <h1>Requests for <c:out value="${logPeriod}"/></h1>
  <h2>Total : <fmt:formatNumber value="${totalRequests}"/></h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <c:forEach var="aRequest" items="${requests}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td>
            <a href="<c:out value="${aRequest.url}"/>" title="<c:out value="${aRequest.url}"/>"><c:out value="${aRequest.truncatedName}"/></a>
          </td>
          <td align="right">
            <fmt:formatNumber value="${aRequest.count}"/>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>

</div>