<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/logs.html" target="_blank">Help</a>
  </div>

  <h1>User Agents for <c:out value="${logPeriod}"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <h3>Summary</h3>
    <br />

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>User Agent</th>
          <th align="right">Total</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="userAgent" items="${consolidatedUserAgents}" varStatus="status">
          <c:choose>
            <c:when test="${status.count % 2 == 0}">
              <tr class="even small">
            </c:when>
            <c:otherwise>
                <tr class="odd small">
            </c:otherwise>
          </c:choose>
          <td>${userAgent.key}</td>
          <td align="right"><fmt:formatNumber value="${userAgent.value}"/></td>
        </tr>
        </c:forEach>
    </tbody>
    </table>

    <br />

    <h3>Distinct User Agents</h3>
    <br />

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>User Agent</th>
          <th align="right">Total</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="userAgent" items="${userAgents}" varStatus="status">
          <c:choose>
            <c:when test="${status.count % 2 == 0}">
              <tr class="even small">
            </c:when>
            <c:otherwise>
                <tr class="odd small">
            </c:otherwise>
          </c:choose>
          <td>${userAgent.key}</td>
          <td align="right"><fmt:formatNumber value="${userAgent.value}"/></td>
        </tr>
        </c:forEach>
    </tbody>
    </table>
  </div>

</div>