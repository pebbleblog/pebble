<div class="contentItem">


  <div class="contentItemLinks">
    <a href="./help/managingBlogs.html" target="_blank"><fmt:message key="common.help"/></a>
  </div>

  <h1><fmt:message key="view.blogs"/></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <th><fmt:message key="view.blogs.id"/></th>
        <th><fmt:message key="view.blogs.name"/></th>
        <th align="right"><fmt:message key="view.blogs.ownersAbbrev"/></th>
        <th align="right"><fmt:message key="view.blogs.publishersAbbrev"/></th>
        <th align="right"><fmt:message key="view.blogs.contributorsAbbrev"/></th>
        <th align="right"><fmt:message key="view.blogs.readersAbbrev"/></th>
        <th align="right"><fmt:message key="view.blogs.adminLinks"/></th>
      </thead>
      <tbody>
      <c:forEach var="aBlog" items="${blogs}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even">
          </c:when>
          <c:otherwise>
              <tr class="odd">
          </c:otherwise>
        </c:choose>
        <td>${aBlog.id}</td>
        <td><a href="${url:rewrite(aBlog.url)}">${aBlog.name}</a></td>
        <td align="right">
          <c:choose><c:when test="${not empty aBlog.blogOwners}">X</c:when><c:otherwise>&nbsp;</c:otherwise></c:choose>
        </td>
        <td align="right">
          <c:choose><c:when test="${not empty aBlog.blogPublishers}">X</c:when><c:otherwise>&nbsp;</c:otherwise></c:choose>
        </td>
        <td align="right">
          <c:choose><c:when test="${not empty aBlog.blogContributors}">X</c:when><c:otherwise>&nbsp;</c:otherwise></c:choose>
        </td>
        <td align="right">
          <c:choose><c:when test="${not empty aBlog.blogReaders}">X</c:when><c:otherwise>&nbsp;</c:otherwise></c:choose>
        </td>
        <td align="right">
          <a href="${url:rewrite(aBlog.url)}viewBlogProperties.secureaction"><fmt:message key="admin.properties"/></a> |
          <a href="${url:rewrite(aBlog.url)}viewPlugins.secureaction"><fmt:message key="admin.plugins"/></a>
        </td>
      </tr>
      </c:forEach>
      <tr>
        <td colspan="7" align="center">
          <fmt:message key="view.blogs.opcrExplanation"/>
        </td>
      </tr>
      </tbody>
    </table>

    <c:choose>
    <c:when test="${blogManager.multiBlog}">
    <p>
      <fmt:message key="view.blogs.addNewMultiBlog"/>
    </p>

    <p>
    <form name="addBlog" action="addBlog.secureaction" method="POST">
      <pebble:token/>
      <c:choose>
        <c:when test="${pebbleContext.configuration.virtualHostingEnabled == true && pebbleContext.configuration.virtualHostingSubdomain == true}">
          ${blog.protocol}
          <input name="id" type="text" value="" />
          .${blog.domainName}
          <input type="submit" value="Add Blog" />
        </c:when>
        <c:when test="${pebbleContext.configuration.virtualHostingEnabled == true && pebbleContext.configuration.virtualHostingSubdomain == false}">
          ${blog.protocol}
          <input name="id" type="text" value="" />
          /
          <input type="submit" value="Add Blog" />
        </c:when>
        <c:otherwise>
          ${blogUrl}
          <input name="id" type="text" value="" />
          <input type="submit" value="Add Blog" />
        </c:otherwise>
      </c:choose>
    </form>
    </p>
    </c:when>
    <c:otherwise>
    <p>
      <fmt:message key="view.blogs.runningInSingleModeExplanation"/>
    </p>
    </c:otherwise>
    </c:choose>

  </div>

</div>