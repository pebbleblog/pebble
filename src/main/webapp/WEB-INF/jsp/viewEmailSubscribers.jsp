<div class="contentItem">

  <h1><fmt:message key="view.emailSubscribers"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <form name="emailSubscribersForm" action="unsubscribe.action" method="post">
    <pebble:token/>
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
      <tr>
        <th></th>
        <th><input type="checkbox" name="allEmailAddresses" onclick="toggleCheckAll(document.emailSubscribersForm.allEmailAddresses, document.emailSubscribersForm.email)"/></th>
        <th>E-mail Address</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="emailAddress" items="${emailAddresses}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even">
          </c:when>
          <c:otherwise>
              <tr class="odd">
          </c:otherwise>
        </c:choose>

        <td width="2%" valign="top">
          <fmt:formatNumber value="${status.count}"/>
        </td>
        <td valign="top" class="small">
          <input type="checkbox" name="email" value="${emailAddress}" />
        </td>
        <td>${emailAddress}</td>
      </tr>
      </c:forEach>
      </tbody>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
          <input type="submit" name="submit" value="Unsubscribe" />
        </td>
      </tr>
    </table>
    </form>
  </div>

</div>