<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<c:choose>

  <%-- // todo only show nav links when detail is blog entry, not static page --%>
  <c:when test="${displayMode == 'detail'}">
    <c:if test="${not empty blogEntry.previousBlogEntry}">
      <a href="${blogEntry.previousBlogEntry.localPermalink}">&lt;&lt; ${blogEntry.previousBlogEntry.title}</a> |
    </c:if>
    <a href="${blog.url}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty blogEntry.nextBlogEntry}">
      | <a href="${blogEntry.nextBlogEntry.localPermalink}">${blogEntry.nextBlogEntry.title} &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'month'}">
    <c:if test="${not empty previousMonth}">
      <a href="${previousMonth.permalink}">&lt;&lt; <fmt:formatDate value="${previousMonth.date}" pattern="MMMM yyyy"/></a> |
    </c:if>
    <a href="${blog.url}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextMonth}">
      | <a href="${nextMonth.permalink}"><fmt:formatDate value="${nextMonth.date}" pattern="MMMM yyyy"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'logSummaryForMonth'}">
    <c:if test="${not empty previousMonth}">
      <a href="${logAction}.secureaction?year=${previousMonth.year.year}&month=${previousMonth.month}">&lt;&lt; <fmt:formatDate value="${previousMonth.date}" pattern="MMMM yyyy"/></a> |
    </c:if>
    <a href="${blog.url}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextMonth}">
      | <a href="${logAction}.secureaction?year=${nextMonth.year.year}&month=${nextMonth.month}"><fmt:formatDate value="${nextMonth.date}" pattern="MMMM yyyy"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'logSummaryForDay'}">
    <c:if test="${not empty previousDay}">
      <a href="${logAction}.secureaction?year=${previousDay.month.year.year}&month=${previousDay.month.month}&day=${previousDay.day}">&lt;&lt; <fmt:formatDate value="${previousDay.date}" type="date" dateStyle="long"/></a> |
    </c:if>
    <a href="${blog.url}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextDay}">
      | <a href="${logAction}.secureaction?year=${nextDay.month.year.year}&month=${nextDay.month.month}&day=${nextDay.day}"><fmt:formatDate value="${nextDay.date}" type="date" dateStyle="long"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'day'}">
    <c:if test="${not empty previousDay}">
      <a href="${previousDay.permalink}">&lt;&lt; <fmt:formatDate value="${previousDay.date}" type="date" dateStyle="long"/></a> |
    </c:if>
    <a href="${blog.url}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextDay}">
      | <a href="${nextDay.permalink}"><fmt:formatDate value="${nextDay.date}" type="date" dateStyle="long"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'page'}">
    <c:if test="${pageable.page < pageable.maxPageRange}">
      <a href="blogentries/${pageable.nextPage}.html"><fmt:message key="common.previous" /></a> |
    </c:if>
    <a href="${blog.url}"><fmt:message key="common.home" /></a>
    <c:if test="${pageable.page > pageable.minPageRange}">
      | <a href="blogentries/${pageable.previousPage}.html"><fmt:message key="common.next" /></a>
    </c:if>
  </c:when>

  <c:otherwise>
    <a href="${blog.url}"><fmt:message key="common.home" /></a>
  </c:otherwise>

</c:choose>