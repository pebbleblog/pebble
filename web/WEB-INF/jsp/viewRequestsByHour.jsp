<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <h1>Requests by Hour for <c:out value="${logPeriod}"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>Hour of Day (${blog.timeZoneId})</th>
          <th align="right">Requests</th>
          <th align="right">Unique</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="count" begin="0" end="23">
          <c:choose>
            <c:when test="${count % 2 == 0}">
              <tr class="even small">
            </c:when>
            <c:otherwise>
                <tr class="odd small">
            </c:otherwise>
          </c:choose>
          <td><fmt:formatNumber value="${count}" pattern="00"/>:00</td>
          <td align="right"><fmt:formatNumber value="${requestsPerHour[count]}"/></td>
          <td align="right"><fmt:formatNumber value="${uniqueIpsPerHour[count]}"/></td>
        </tr>
        </c:forEach>
    </tbody>
    </table>
  </div>

</div>