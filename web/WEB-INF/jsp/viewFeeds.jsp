<div class="contentItem">
  <h1><fmt:message key="newsfeed.feedsTitle" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="8">
      <tr class="small">
        <td>
          ${blog.rootCategory.name}
        </td>
        <td align="right" class="small">
          <a href="rss.xml"><fmt:message key="newsfeed.rss" /></a> |
          <a href="rdf.xml"><fmt:message key="newsfeed.rdf" /></a> |
          <a href="atom.xml"><fmt:message key="newsfeed.atom" /></a>
        </td>
      </tr>

      <c:forEach var="category" items="${categories}" varStatus="status" begin="1">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="odd small">
          </c:when>
          <c:otherwise>
              <tr class="even small">
          </c:otherwise>
        </c:choose>
          <td>
            <c:out value="${category.name}" escapeXml="true"/>
          </td>
          <td align="right" class="small">
            <a href="${category.permalink}rss.xml"><fmt:message key="newsfeed.rss" /></a> |
            <a href="${category.permalink}rdf.xml"><fmt:message key="newsfeed.rdf" /></a> |
            <a href="${category.permalink}atom.xml"><fmt:message key="newsfeed.atom" /></a>
          </td>
        </tr>
      </c:forEach>

    </table>

    <pebble:isAuthorisedForBlog>
    <br />
    <div style="text-align: center">
    <a href="http://www.feedvalidator.org/check.cgi?url=${blog.url}rss.xml"><img src="${pageContext.request.contextPath}/common/images/valid-rss.png" alt="[Valid RSS]" title="Validate my RSS feed" width="88" height="31" border="0" /></a>
    &nbsp;
    <a href="http://www.feedvalidator.org/check.cgi?url=${blog.url}rdf.xml"><img src="${pageContext.request.contextPath}/common/images/valid-rss.png" alt="[Valid RSS]" title="Validate my RDF feed" width="88" height="31" border="0" /></a>
    &nbsp;
    <a href="http://www.feedvalidator.org/check.cgi?url=${blog.url}atom.xml"><img src="${pageContext.request.contextPath}/common/images/valid-atom.png" alt="[Valid Atom]" title="Validate my Atom feed" width="88" height="31" border="0" /></a>
    </div>
    </pebble:isAuthorisedForBlog>
  </div>

</div>