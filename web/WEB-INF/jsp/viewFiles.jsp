<div class="contentItem">

  <div class="contentItemLinks">
    <c:if test='${not empty parent}'>
      <a href="${parent.url}">Back to parent</a> |
    </c:if>
    <a href="zipDirectory.secureaction?path=${directory.absolutePath}&type=${type}">Export as ZIP</a> |
    <c:choose>
      <c:when test="${type == 'blogImage'}"><a href="./help/images.html" target="_blank">Help</a></c:when>
      <c:when test="${type == 'blogFile'}"><a href="./help/files.html" target="_blank">Help</a></c:when>
      <c:when test="${type == 'themeFile'}"><a href="./help/themes.html" target="_blank">Help</a></c:when>
      <c:when test="${type == 'blogData'}"></c:when>
    </c:choose>
  </div>

  <h1>
    <c:choose>
      <c:when test="${type == 'blogImage'}">Images</c:when>
      <c:when test="${type == 'blogFile'}">Files</c:when>
      <c:when test="${type == 'themeFile'}">Theme (${blog.editableTheme.name})</c:when>
      <c:when test="${type == 'blogData'}">Files</c:when>
    </c:choose>
  </h1>
  <h2><c:out value="${directory.absolutePath}" /> (<fmt:formatNumber value="${directory.sizeInKB}" type="number" />&nbsp;KB)</h2>

  <div class="contentItemBody">
  <p>
    <c:if test="${pebbleContext.configuration.fileUploadQuota > -1}">
      You are using <fmt:formatNumber value="${currentUsage}" type="number" />&nbsp;KB of
      your <fmt:formatNumber value="${pebbleContext.configuration.fileUploadQuota}" type="number" />&nbsp;KB quota, which is shared between
      all of your images, files and theme.
    </c:if>
  </p>

  <c:choose>
  <c:when test="${not empty files}">
    <form name="filesForm" action="removeFiles.secureaction" method="post">
    <input type="hidden" name="path" value="${directory.absolutePath}" />
    <input type="hidden" name="type" value="${type}" />
    <table width="99%" cellspacing="0" cellpadding="4">
      <c:forEach var="aFile" items="${files}" varStatus="status">
        <c:choose>
          <c:when test="${status.count % 2 == 0}">
            <tr class="even small">
          </c:when>
          <c:otherwise>
              <tr class="odd small">
          </c:otherwise>
        </c:choose>
          <td>
            <input type="checkbox" name="name" value="${aFile.name}" />
            <c:choose>
              <c:when test="${aFile.directory}">
                [dir]
              </c:when>
              <c:otherwise>&nbsp;&nbsp;&nbsp;</c:otherwise>
            </c:choose>
          </td>
          <td>
            <a href="${aFile.url}">${aFile.name}</a>
          </td>
          <td align="right">
            <fmt:formatNumber value="${aFile.sizeInKB}" type="number" />&nbsp;KB
          </td>
          <td align="right">
            <fmt:formatDate value="${aFile.lastModified}" type="both" dateStyle="short" timeStyle="short" />
          </td>
          <td align="right">
            <c:if test="${aFile.editable}">
            <a href="editFile.secureaction?name=${aFile.name}&path=${aFile.path}&type=${type}" title="Edit the content of this file">Edit</a>
            |
            </c:if>
            <a href="viewFiles.secureaction?path=${aFile.path}&file=${aFile.name}&type=${type}#manageFile" title="Rename or copy this file">Manage</a>
          </td>
        </tr>
      </c:forEach>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="left">
          <input type="button" value="Check All" onclick="checkAll(document.filesForm.name)" />
          <input type="button" value="Uncheck All" onclick="uncheckAll(document.filesForm.name)" />
        </td>
        <td align="right">
          <input type="submit" name="submit" value="Remove" />
        </td>
      </tr>
    </table>

    </form>
  </c:when>
  <c:otherwise>
      <p>
        There are no files in this directory.
      </p>
  </c:otherwise>
  </c:choose>

  <c:if test="${not empty file}">
  <a name="manageFile" />
  <form name="copyFile" action="copyFile.secureaction" method="POST">
    <p>
      <b>Rename/copy file</b>
      <br />
      Name
      <input type="hidden" name="type" value="${type}" />
      <input type="hidden" name="path" value="${file.path}" />
      <input type="hidden" name="name" value="${file.name}" />
      <input type="text" name="newName" size="40" value="${file.name}" />
      <input name="submit" type="submit" Value="Rename" />
      <c:if test="${not file.directory}">
      <input name="submit" type="submit" Value="Copy" />
      </c:if>
    </p>
  </form>
  </c:if>

  <form name="createDirectory" action="createDirectory.secureaction" method="POST">
    <p>
      <b>Create directory</b>
      <br /><br />
      Name
      <input type="hidden" name="type" value="${type}" />
      <input type="hidden" name="path" value="${directory.absolutePath}" />
      <input type="text" name="name" size="40" value="" />
      <input name="submit" type="submit" Value="Create Directory" />
    </p>
  </form>

  <form name="uploadFile" enctype="multipart/form-data" action="${uploadAction}" method="post">
    <p>
      <b>Upload file</b> (files must be less than <fmt:formatNumber value="${pebbleContext.configuration.fileUploadSize}" type="number" />&nbsp;KB)
      <br /><br />
      Local Name
      <input type="hidden" name="path" value="${directory.absolutePath}" />
      <input name="file" type="file" onChange="populateFilename(this,document.uploadFile.filename)" />
      <br />
      Remote Name
      <input name="filename" type="text" value="" />
      <input type="submit" value="Upload File" />
    </p>
  </form>

  </div>

</div>