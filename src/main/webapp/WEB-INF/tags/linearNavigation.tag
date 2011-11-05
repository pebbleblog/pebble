<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/url.tld" prefix="url" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<c:choose>

  <%-- // todo only show nav links when detail is blog entry, not static page --%>
  <c:when test="${displayMode == 'detail'}">
    <c:if test="${not empty previousBlogEntry}">
      <a href="${url:rewrite(previousBlogEntry.localPermalink)}" title="${url:rewrite(previousBlogEntry.localPermalink)}">&lt;&lt; ${previousBlogEntry.title}</a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextBlogEntry}">
      | <a href="${url:rewrite(nextBlogEntry.localPermalink)}" title="${url:rewrite(nextBlogEntry.localPermalink)}">${nextBlogEntry.title} &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'month'}">
    <c:if test="${not empty previousMonth}">
      <a href="${pebble:monthPermalink(permalinkProvider, previousMonth)}" title="${pebble:monthPermalink(permalinkProvider, previousMonth)}">&lt;&lt; <fmt:formatDate value="${previousMonth.date}" pattern="MMMM yyyy"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextMonth}">
      | <a href="${pebble:monthPermalink(permalinkProvider, nextMonth)}" title="${pebble:monthPermalink(permalinkProvider, nextMonth)}"><fmt:formatDate value="${nextMonth.date}" pattern="MMMM yyyy"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'logSummaryForMonth'}">
    <c:if test="${not empty previousMonth}">
      <a href="${logAction}.secureaction?year=${previousMonth.year}&amp;month=${previousMonth.month}" title="${logAction}.secureaction?year=${previousMonth.year}&amp;month=${previousMonth.month}">&lt;&lt; <fmt:formatDate value="${previousMonth.date}" pattern="MMMM yyyy"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextMonth}">
      | <a href="${logAction}.secureaction?year=${nextMonth.year}&month=${nextMonth.month}" title="${logAction}.secureaction?year=${nextMonth.year}&amp;month=${nextMonth.month}"><fmt:formatDate value="${nextMonth.date}" pattern="MMMM yyyy"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'logSummaryForDay'}">
    <c:if test="${not empty previousDay}">
      <a href="${logAction}.secureaction?year=${previousDay.year}&amp;month=${previousDay.month}&amp;day=${previousDay.day}" title="${logAction}.secureaction?year=${previousDay.year}&amp;month=${previousDay.month}&amp;day=${previousDay.day}">&lt;&lt; <fmt:formatDate value="${previousDay.date}" type="date" dateStyle="long"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextDFay}">
      | <a href="${logAction}.secureaction?year=${nextDay.year}&month=${nextDay.month}&day=${nextDay.day}" href="${logAction}.secureaction?year=${nextDay.year}&amp;month=${nextDay.month}&amp;day=${nextDay.day}"><fmt:formatDate value="${nextDay.date}" type="date" dateStyle="long"/> &gt;&gt;</a>
    </c:if>
  </c:when>

  <c:when test="${displayMode == 'day'}">
    <c:if test="${not empty previousDay}">
      <a href="${pebble:dayPermalink(permalinkProvider, previousDay)}" title="${pebble:dayPermalink(permalinkProvider, previousDay)}">&lt;&lt; <fmt:formatDate value="${previousDay.date}" type="date" dateStyle="long"/></a> |
    </c:if>
    <a href="${url:rewrite(blog.url)}" title="${url:rewrite(blog.url)}"><fmt:message key="common.home" /></a>
    <c:if test="${not empty nextDay}">
      | <a href="${pebble:dayPermalink(permalinkProvider, nextDay)}" title="${pebble:dayPermalink(permalinkProvider, nextDay)}"><fmt:formatDate value="${nextDay.date}" type="date" dateStyle="long"/> &gt;&gt;</a>
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