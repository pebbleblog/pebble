<div class="contentItem">

  <div class="contentItemLinks">
    <a href="./help/blogEntries.html" target="_blank">Help</a>
  </div>

  <h1>
    <c:choose>
      <c:when test="${blogEntry.unpublished}">
        Publish blog entry
      </c:when>
      <c:when test="${blogEntry.published}">
        Unpublish blog entry
      </c:when>
    </c:choose>
  </h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form name="publishBlogEntryForm" action="publishBlogEntry.secureaction" method="POST" accept-charset="${blog.characterEncoding}">
    <pebble:token/>
    <input type="hidden" name="entry" value="${blogEntry.id}" />

    <c:choose>
      <c:when test="${blogEntry.unpublished}">

        <table width="99%" cellspacing="0" cellpadding="4">
          <tr>
            <td valign="top"><b>Date/time</b></td>
            <td>
              <input type="radio" name="publishDate" value="as-is" checked="checked" />&nbsp;As-is (<fmt:formatDate value="${blogEntry.date}" type="both" dateStyle="medium" timeStyle="long" />)
              <br />
              <input type="radio" name="publishDate" value="now" />&nbsp;Now
              <br />
              <jsp:useBean id="now" class="java.util.Date" />
              <input type="radio" name="publishDate" value="custom" />&nbsp;<input type="text" name="date" size="30" value="<fmt:formatDate value="${now}" type="both" dateStyle="medium" timeStyle="short" />" onfocus="publishBlogEntryForm.publishDate[2].checked=true"> (can't be in the future)
            </td>
          </tr>

          <tr>
            <td colspan="2" align="right">
              <button name="submit" type="submit" Value="Publish" ><fmt:message key="common.publish"/></button>
            </td>
          </tr>
        </table>

      </c:when>
      <c:when test="${blogEntry.published}">

        Are you sure?

        <table width="99%" cellspacing="0" cellpadding="4">
          <tr>
            <td colspan="2" align="right">
              <button name="submit" type="submit" Value="Unpublish"><fmt:message key="common.unpublish"/></button>
            </td>
          </tr>
        </table>

      </c:when>
    </c:choose>

    </form>
  </div>

</div>