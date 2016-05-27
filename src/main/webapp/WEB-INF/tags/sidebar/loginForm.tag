<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>
<%@ taglib prefix="url" uri="/WEB-INF/url.tld" %>

<jsp:useBean id="pebbleContext" scope="request" type="net.sourceforge.pebble.PebbleContext"/>

<%--
  Displays the login form.
--%>

<pebble:isNotAuthenticated>
  <c:if test="${empty isLoginPage}">
    <div class="sidebarItem">
      <div class="sidebarItemTitle"><span><fmt:message key='login.login'/></span></div>
      <div class="loginSideForm">
        <div id="loginOptionPasswordArea" class="loginOptionArea">
          <form id="passwordLoginForm" name="passwordLoginForm" method="post"
                action="${url:rewrite(blogUrl)}j_spring_security_check">
            <input type="hidden" name="redirectUrl" value="<c:out value="${originalUri}"/>"/>
            <div class="field">
              <label for="username"><fmt:message key="login.username"/></label>
              <input autocorrect="off" autocapitalize="off" id="username" type="text" name="j_username"/>
            </div>
            <div class="field">
              <label for="password"><fmt:message key="login.password"/></label>
              <input id="password" type="password" name="j_password"/>
            </div>
            <div class="field">
              <label for="rememberMe"><fmt:message key='login.rememberMe'/></label>
              <input id="rememberMe" type="checkbox" name="_spring_security_remember_me"/>
            </div>
            <div class="loginButtons"><input type="submit" value="<fmt:message key='login.button' />"/></div>
          </form>
        </div>

        <div id="loginOptionOpenIdArea" class="loginOptionArea">
          <form id="openIdLoginForm" name="openIdloginForm" method="post"
                action="${url:rewrite(blogUrl)}j_spring_openid_security_check">
            <input type="hidden" name="redirectUrl" value="<c:out value="${originalUri}"/>"/>

            <div class="field">
              <label for="openIdIdentifier">OpenId</label>
              <input id="openIdIdentifier" type="text" name="openid_identifier"/>
            </div>
            <div class="field">
              <label for="openIdRememberMe"><fmt:message key='login.rememberMe'/></label>
              <input id="openIdRememberMe" type="checkbox" name="_spring_security_remember_me"/>
            </div>
            <div class="loginButtons"><input type="submit" value="<fmt:message key='login.button' />"/></div>
          </form>
        </div>

        <div id="loginOptionGoogleArea" class="loginOptionArea">
          <div class="loginUsingGoogle">Log me in using <img src="common/images/google_logo.jpg" alt="Google"/></div>
        </div>
        <ul id="loginOptions">
          <li id="loginOptionPassword" class="loginOption loginOptionSelected">&nbsp;</li>
          <li id="loginOptionOpenId" class="loginOption">&nbsp;</li>
          <li id="loginOptionGoogle" class="loginOption">&nbsp;</li>
        </ul>
        <script type="text/javascript">
          Event.observe(window, "load", initLoginScreen);
        </script>
      </div>
    </div>
  </c:if>
</pebble:isNotAuthenticated>