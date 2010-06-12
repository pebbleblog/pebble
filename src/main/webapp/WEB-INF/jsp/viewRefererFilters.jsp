<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/referers.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.refererFilters"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Referer filters let you exclude specific URLs from your referers list that, for example, you may think are spam.
      As an example, to exlude all URLs from <code>*.somedomain.com</code>, the expression would be <code>.*\.somedomain\.com.*</code>
    </p>

    <form name="newRefererFilterForm" action="addRefererFilters.secureaction" method="post">
      <pebble:token/>
      <b>Filter</b>
      <input type="text" name="expression" size="40" value=""/>
      <input name="submit" type="submit" Value="Add Filter" />
    </form>

    <br />

    <form name="refererFiltersForm" action="removeRefererFilters.secureaction" method="post">
    <pebble:token/>
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <th><input type="checkbox" name="allExpressions" onclick="toggleCheckAll(document.refererFiltersForm.allExpressions, document.refererFiltersForm.expression)"/></th>
        <th>Filter (regular expression)</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="filter" items="${filters}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td>
            <input type="checkbox" name="expression" value="<c:out value="${filter.expression}" />" />
          </td>
          <td>
            <c:out value="${filter.expression}" escapeXml="false"/>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
          <input type="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>

</div>