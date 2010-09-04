<div class="contentItem">
  <h1><fmt:message key="view.messages"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th>Date/Time</th>
          <th>Type</th>
          <th>Message</th>
        </tr>
      </thead>
      <c:forEach var="message" items="${messages}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td valign="top"><fmt:formatDate value="${message.date}" type="both" dateStyle="short" timeStyle="short"/></td>
          <td valign="top">${message.type}</td>
          <td>${message.text}</td>
        </tr>
      </c:forEach>
    </table>

    <br />

    <c:if test="${not empty messages}">
    <form name="messagesForm" action="clearMessages.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
      <pebble:token/>
      <table width="99%">
        <tr>
          <td align="right">
            <input name="submit" type="submit" Value="Clear Messages">
          </td>
        </tr>
      </table>
    </form>
    </c:if>

  </div>
</div>