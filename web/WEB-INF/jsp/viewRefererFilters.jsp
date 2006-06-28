<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/referers.html" target="_blank">Help</a>
  </div>

  <h1>Referer filters</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="newRefererFilterForm" action="addRefererFilters.secureaction" method="post">
      <b>Filter</b>
      <input type="text" name="expression" size="40" value=""/>
      <input name="submit" type="submit" Value="Add Filter" />
      <br />
      <div class="small">(this is a regular expression, such as .*\.somedomain\.com.*)</div>
    </form>

    <form name="refererFiltersForm" action="removeRefererFilters.secureaction" method="post">
    <table width="99%" cellspacing="0" cellpadding="4">
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
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.refererFiltersForm.expression)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.refererFiltersForm.expression)" />
        </td>
        <td align="right">
          <input type="submit" value="Remove" />
        </td>
      </tr>
    </table>
    </form>

</div>