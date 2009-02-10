<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/referers.html" target="_blank">Help</a>
  </div>

  <h1>Referers for ${logPeriod}</h1>
  <h2>Total : <fmt:formatNumber value="${totalReferers}"/></h2>

  <div class="contentItemBody">
    <form name="referersForm" method="post" action="addRefererFilters.secureaction">
    <input type="hidden" name="redirectUrl" value="${blog.url}/viewReferers.secureaction" />
    <input type="hidden" name="year" value="${param.year}" />
    <input type="hidden" name="month" value="${param.month}" />
    <input type="hidden" name="day" value="${param.day}" />

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
            <a href="${url:escape(referer.url)}" title="${url:escape(referer.url)}" rel="nofollow">${url:escape(referer.truncatedName)}</a>
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