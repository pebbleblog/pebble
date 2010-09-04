<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/referers.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1>
  	<fmt:message key="view.referrersForPeriod">
	  <fmt:param>${logPeriod}</fmt:param>
	</fmt:message>
  </h1>
  <h2>Total : <fmt:formatNumber value="${totalReferers}"/></h2>

  <div class="contentItemBody">
    <form name="referersForm" method="post" action="addRefererFilters.secureaction">
    <pebble:token/>
    <input type="hidden" name="redirectUrl" value="${blogUrl}/viewReferers.secureaction" />
    <input type="hidden" name="year" value="<fmt:formatNumber value="${param.year}" pattern="#"/>" />
    <input type="hidden" name="month" value="<fmt:formatNumber value="${param.month}"/>" />
    <input type="hidden" name="day" value="<fmt:formatNumber value="${param.day}"/>" />

    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th><input type="checkbox" name="allExpressions" onclick="toggleCheckAll(document.referersForm.allExpressions, document.referersForm.expression)"/></th>
          <th>Referer</th>
          <th align="right">Count</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach var="referer" items="${referers}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td width="2%">
            <input type="checkbox" name="expression" value="${referer.domainFilter}" />
          </td>
          <td>
            <a href="${fn:escapeXml(referer.url)}" title="${fn:escapeXml(referer.url)}" rel="nofollow">${fn:escapeXml(referer.truncatedName)}</a>
          </td>
          <td align="right">
            <fmt:formatNumber value="${referer.count}"/>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

    <pebble:isAuthorisedForBlog>
    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
          <input type="submit" value="Spam" />
        </td>
      </tr>
    </table>
    </pebble:isAuthorisedForBlog>

    </form>
  </div>

</div>