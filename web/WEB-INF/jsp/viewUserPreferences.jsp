<div class="contentItem">

  <h1><fmt:message key="view.userPreferences"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">

    <c:if test="${not empty validationContext.errors}">
    <div class="validationError">
      <b>${validationContext.numberOfErrors} error(s)</b>
      <ul>
      <c:forEach var="error" items="${validationContext.errors}">
        <li>${error.message}</li>
      </c:forEach>
      </ul>
    </div>
    </c:if>

    <form name="user" action="saveUserPreferences.secureaction" method="post">

      <table width="99%" cellspacing="0" cellpadding="4">
        <tr>
          <td valign="top">
            Rich text editor
          </td>
          <td>
            <input type="checkbox" name="preference.richTextEditorForBlogEntriesEnabled" value="true"
                   <c:if test="${user.preferences['richTextEditorForBlogEntriesEnabled'] == 'true'}">checked="true"</c:if>
            />&nbsp;Blog entries
            <input type="checkbox" name="preference.richTextEditorForStaticPagesEnabled" value="true"
                   <c:if test="${user.preferences['richTextEditorForStaticPagesEnabled'] == 'true'}">checked="true"</c:if>
            />&nbsp;Static pages
          </td>
        </tr>
        <tr>
          <td colspan="2" align="right">
            <input type="submit" value="Save" />
          </td>
        </tr>
      </table>
    </form>

  </div>

</div>