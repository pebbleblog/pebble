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
      <pebble:token/>
      <input type="hidden" name="preference.openids"
             value="<c:out value="${user.preferences['openids']}"/>"/>

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
  <h1>OpenID</h1>
  <h2>&nbsp;</h2>
  <div class="contentItemBody">

    <c:if test="${not empty user.openIds}">
      <p><fmt:message key="openid.existing.description"/></p>
      <table width="99%" cellspacing="0" cellpadding="4">
        <thead>
          <tr>
            <th>
              <fmt:message key="openid.identity"/>
            </th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <c:forEach items="${user.openIds}" var="openId" varStatus="status">
          <c:choose>
            <c:when test="${status.count % 2 == 0}">
              <tr class="even">
            </c:when>
            <c:otherwise>
                <tr class="odd">
            </c:otherwise>
          </c:choose>
            <td><c:out value="${openId}"/></td>
            <td><a href="<c:url value="removeOpenId.secureaction">
                  <c:param name="openid" value="${openId}"/>
                </c:url>&amp;<pebble:token query="true"/>">
              <fmt:message key="common.remove"/></a>
            </td>
          </tr>
        </c:forEach>
      </table>
      <br/>
      <br/>
    </c:if>

    <p><fmt:message key="openid.add.description"/></p>
    <div class="loginPageForm">
      <ul id="loginOptions">
        <li id="loginOptionOpenId" class="loginOption">OpenID</li>
        <li id="loginOptionGoogle" class="loginOption">Google</li>
      </ul>

      <div id="loginOptionOpenIdArea" class="loginOptionArea">
        <form id="openIdLoginForm" name="openIdloginForm" method="post"
              action="${url:rewrite(blogUrl)}addOpenId.secureaction">
          <input type="hidden" name="redirectUrl" value="${blog.relativeUrl}"/>
          <div class="field">
            <label for="openIdIdentifier">OpenId</label>
            <input id="openIdIdentifier" type="text" name="openid_identifier" />
          </div>
          <div class="loginButtons"><input type="submit" value="<fmt:message key='common.add' />" /></div>
        </form>
      </div>

      <div id="loginOptionGoogleArea" class="loginOptionArea">
        <div class="loginUsingGoogle">Add my OpenID for <img src="common/images/google_logo.jpg" alt="Google"/></div>
      </div>
    </div>
    <script type="text/javascript">
      Event.observe(window, "load", initLoginScreen);
    </script>
  </div>
</div>