<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${pageContext.request.contextPath}/docs/referers.html" target="_blank">Help</a>
  </div>

  <div class="title">Referers for ${logPeriod}</div>
  <div class="subtitle">Total : <fmt:formatNumber value="${totalReferers}"/></div>

  <div class="contentItemBody">
    <form name="referersForm" method="post" action="addRefererFilters.secureaction">
    <input type="hidden" name="redirectUrl" value="${blog.url}/viewReferers.secureaction" />
    <input type="hidden" name="year" value="${param.year}" />
    <input type="hidden" name="month" value="${param.month}" />
    <input type="hidden" name="day" value="${param.day}" />
    <table width="99%" cellspacing="0" cellpadding="4">
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
            <a href="${referer.url}" title="${referer.url}" rel="nofollow">${referer.truncatedName}</a>
          </td>
          <td align="right">
            <fmt:formatNumber value="${referer.count}"/>
          </td>
        </tr>
      </c:forEach>
    </table>

    <pebble:isBlogOwnerOrContributor>
    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.referersForm.expression)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.referersForm.expression)" />
        </td>
        <td align="right">
          <input type="submit" value="Spam" />
        </td>
      </tr>
    </table>
    </pebble:isBlogOwnerOrContributor>

    </form>
  </div>

</div>