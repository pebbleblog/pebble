<div class="contentItem">

  <div class="contentItemLinks">
    <a href="${pageContext.request.contextPath}/help/blogEntries.html" target="_blank">Help</a>
  </div>

  <div class="title">
    <c:choose>
      <c:when test="${blogEntry.unpublished}">
        Publish blog entry
      </c:when>
      <c:when test="${blogEntry.published}">
        Unpublish blog entry
      </c:when>
    </c:choose>
  </div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <form name="publishBlogEntryForm" action="publishBlogEntry.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
    <input type="hidden" name="entry" value="${blogEntry.id}" />

    <c:choose>
      <c:when test="${blogEntry.unpublished}">

        <table width="99%" cellspacing="0" cellpadding="4">
          <tr>
            <td valign="top"><b>Date/time</b></td>
            <td>
              <input type="radio" name="now" value="true" checked="checked" />&nbsp;Now
              <br />
              <input type="radio" name="now" value="false" />&nbsp;<input type="text" name="date" size="20" value="<fmt:formatDate value="${blogEntry.date}" type="both" dateStyle="medium" timeStyle="short" />" onfocus="publishBlogEntryForm.now[1].checked=true">
            </td>
          </tr>

          <tr>
            <td colspan="2" align="right">
              <input name="submit" type="submit" Value="Publish" />
            </td>
          </tr>
        </table>

      </c:when>
      <c:when test="${blogEntry.published}">

        Are you sure?

        <table width="99%" cellspacing="0" cellpadding="4">
          <tr>
            <td colspan="2" align="right">
              <input name="submit" type="submit" Value="Unpublish" />
            </td>
          </tr>
        </table>

      </c:when>
    </c:choose>

    </form>
  </div>

</div>