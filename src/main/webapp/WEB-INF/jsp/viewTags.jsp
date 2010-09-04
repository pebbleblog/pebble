<div class="contentItem">
  <h1><fmt:message key="tag.tags" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <div class="tagCloud">
    <ul>
    <c:forEach var="tag" items="${tags}" varStatus="status">
      <li><span class="tagCloud${tag.rank}"><a href="${url:rewrite(tag.permalink)}" title="rank=<fmt:formatNumber value="${tag.rank}"/>, blog entries=<fmt:formatNumber value="${tag.numberOfBlogEntries}"/>"><c:out value="${tag.name}" escapeXml="true"/></a><a href="${url:rewrite(tag.permalink)}rss.xml" title="RSS feed for <c:out value="${tag.name}"/> tag" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></li>
    </c:forEach>
    </ul>
    </div>
  </div>
</div>