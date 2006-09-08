<div class="contentItem">
  <h1>Messages</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" class="small" style="text-align: left;">
      <c:forEach var="message" items="${messages}">
        <tr>
          <td><b><fmt:formatDate value="${message.date}" type="both" dateStyle="short" timeStyle="short"/></b>
          ${message.text}<br /><br /></td>
        </tr>
      </c:forEach>
    </table>

    <c:if test="${not empty messages}">
    <form name="messagesForm" action="clearMessages.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
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