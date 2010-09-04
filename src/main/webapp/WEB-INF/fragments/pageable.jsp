<c:if test="${pageable.maxPages > 1}">
  <div align="center">
  <c:if test="${pageable.previousPage > 0}">
    <a href="${url:rewrite(param.url)}&amp;page=${pageable.previousPage}"><fmt:message key="common.previous" /></a>
  </c:if>
  <c:if test="${pageable.minPageRange != 1}">...</c:if>
  <c:forEach var="i" begin="${pageable.minPageRange}" end="${pageable.maxPageRange}">
    <c:choose>
    <c:when test="${i != pageable.page}">
      <a href="${url:rewrite(param.url)}&amp;page=${i}"><fmt:formatNumber value="${i}"/></a>
    </c:when>
    <c:otherwise>
      <fmt:formatNumber value="${i}"/>
    </c:otherwise>
    </c:choose>
  </c:forEach>
  <c:if test="${pageable.maxPageRange != pageable.maxPages}">...</c:if>
  <c:if test="${pageable.nextPage > 0}">
    <a href="${url:rewrite(param.url)}&amp;page=${pageable.nextPage}"><fmt:message key="common.next" /></a>
  </c:if>
  </div>
</c:if>