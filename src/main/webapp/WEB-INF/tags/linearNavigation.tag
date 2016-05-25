<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>

<c:choose>

  <%-- // todo only show nav links when detail is blog entry, not static page --%>
  <c:when test="${displayMode == 'detail'}">
    <c:if test="${not empty blogEntry.previousBlogEntry}">
      <a href="${url:rewrite(blogEntry.previousBlogEntry.localPermalink)}" title="${url:rewrite(blogEntry.previousBlogEntry.localPermalink)}">&lt;&lt; ${blogEntry.previousBlogEntry.title}</a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty blogEntry.nextBlogEntry}">
      | <a href="${url:rewrite(blogEntry.nextBlogEntry.localPermalink)}" title="${url:rewrite(blogEntry.nextBlogEntry.localPermalink)}">${blogEntry.nextBlogEntry.title} &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'month'}">
    <c:if test="${not empty previousMonth}">
      <a href="${url:rewrite(previousMonth.permalink)}" title="${url:rewrite(previousMonth.permalink)}">&lt;&lt; <fmt:formatDate value="${previousMonth.date}" pattern="MMMM yyyy"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextMonth}">
      | <a href="${url:rewrite(nextMonth.permalink)}" title="${url:rewrite(nextMonth.permalink)}"><fmt:formatDate value="${nextMonth.date}" pattern="MMMM yyyy"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'logSummaryForMonth'}">
    <c:if test="${not empty previousMonth}">
      <a href="${logAction}.secureaction?year=${previousMonth.year.year}&amp;month=${previousMonth.month}" title="${logAction}.secureaction?year=${previousMonth.year.year}&amp;month=${previousMonth.month}">&lt;&lt; <fmt:formatDate value="${previousMonth.date}" pattern="MMMM yyyy"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextMonth}">
      | <a href="${logAction}.secureaction?year=${nextMonth.year.year}&month=${nextMonth.month}" title="${logAction}.secureaction?year=${nextMonth.year.year}&amp;month=${nextMonth.month}"><fmt:formatDate value="${nextMonth.date}" pattern="MMMM yyyy"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'logSummaryForDay'}">
    <c:if test="${not empty previousDay}">
      <a href="${logAction}.secureaction?year=${previousDay.month.year.year}&amp;month=${previousDay.month.month}&amp;day=${previousDay.day}" title="${logAction}.secureaction?year=${previousDay.month.year.year}&amp;month=${previousDay.month.month}&amp;day=${previousDay.day}">&lt;&lt; <fmt:formatDate value="${previousDay.date}" type="date" dateStyle="long"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextDay}">
      | <a href="${logAction}.secureaction?year=${nextDay.month.year.year}&month=${nextDay.month.month}&day=${nextDay.day}" href="${logAction}.secureaction?year=${nextDay.month.year.year}&amp;month=${nextDay.month.month}&amp;day=${nextDay.day}"><fmt:formatDate value="${nextDay.date}" type="date" dateStyle="long"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'day'}">
    <c:if test="${not empty previousDay}">
      <a href="${url:rewrite(previousDay.permalink)}" title="${url:rewrite(previousDay.permalink)}">&lt;&lt; <fmt:formatDate value="${previousDay.date}" type="date" dateStyle="long"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextDay}">
      | <a href="${url:rewrite(nextDay.permalink)}" title="${url:rewrite(nextDay.permalink)}"><fmt:formatDate value="${nextDay.date}" type="date" dateStyle="long"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'page'}">
    <c:if test="${pageable.page < pageable.maxPageRange}">
      <a href="blogentries/${pageable.nextPage}.html" title="blogentries/${pageable.nextPage}.html"><fmt:message key="common.previous" /></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${pageable.page > pageable.minPageRange}">
      | <a title="blogentries/${pageable.previousPage}.html" href="blogentries/${pageable.previousPage}.html"><fmt:message key="common.next" /></a>
    </c:if>
  </c:when>

  <c:otherwise>
    <a title="${url:rewrite(blog.url)}" href="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
  </c:otherwise>

</c:choose>