<c:if test="${not empty tags}">
<div class="sidebarItem">
  <div class="sidebarItemTitle"><span><fmt:message key="tag.tags" /></span></div>
  <div class="sidebarItemBody">
    <div class="sidebarTagCloud">
    <ul>
    <c:forEach var="tag" items="${tags}" varStatus="status">
      <c:if test="${tag.rank >= param.threshold}">
      <li><span class="tagCloud${tag.rank}"><a href="${tag.permalink}" title="<fmt:formatNumber value='${tag.numberOfBlogEntries}'/>"><c:out value="${tag.name}" escapeXml="true"/></a></span></li>
      </c:if>
    </c:forEach>
    </ul>
    </div>
   </div>
</div>
</c:if>