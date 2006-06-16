<div class="contentItem">
  <div class="title">Messages</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <table width="99%" class="small" style="text-align: left;">
      <c:forEach var="message" items="${blog.messages}">
        <tr>
          <td><b><fmt:formatDate value="${message.date}" type="both" dateStyle="short" timeStyle="short"/></b>
          ${message.text}<br /><br /></td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>