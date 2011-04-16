<%@ attribute name="comment" type="net.sourceforge.pebble.domain.Comment" required="true" %>
<%@ attribute name="blogEntry" type="net.sourceforge.pebble.domain.BlogEntry" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<c:choose>
  <c:when test="${not empty comment.avatar}">
    <c:set var="avatar" value="${comment.avatar}"/>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty comment.email and blogEntry.blog.gravatarSupportForCommentsEnabled}">
      <c:set var="avatar" value="${pebble:gravatar(comment.email)}"/>
    </c:if>
  </c:otherwise>
</c:choose>
<c:if test="${not empty avatar}">
  <img class="avatar" src="<c:out value="${avatar}"/>" alt="Avatar: <c:out value="${comment.author}"/>">
</c:if>