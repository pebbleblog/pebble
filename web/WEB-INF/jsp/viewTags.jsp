<div class="contentItem">
  <div class="title"><fmt:message key="tag.tags" /></div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <div class="tagCloud">
    <ul>
    <c:forEach var="tag" items="${blog.tags}" varStatus="status">
      <li><span class="tagCloud${tag.rank}"><a href="${tag.permalink}" title="<fmt:formatNumber value="${tag.numberOfBlogEntries}"/>"><c:out value="${tag.name}" escapeXml="true"/></a><a href="rss.xml?tag=<c:out value="${tag.name}"/>" title="RSS feed for <c:out value="${tag.name}"/> tag" style="border: 0px;"><img src="common/images/feed-icon-10x10.png" alt="RSS feed" border="0" /></a></span></li>
    </c:forEach>
    </ul>
    </div>
  </div>
</div>