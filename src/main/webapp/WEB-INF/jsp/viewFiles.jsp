<div class="contentItem">

  <div class="contentItemLinks">
    <c:if test='${not empty parent}'>
      <a href="${url:rewrite(parent.url)}"><fmt:message key="admin.backToParent"/></a> |
    </c:if>
    <a href="zipDirectory.secureaction?path=${directory.absolutePath}&amp;type=${type}"><fmt:message key="admin.exportAsZip"/></a> |
    <c:choose>
      <c:when test="${type == 'blogImage'}"><a href="./help/images.html" target="_blank"><fmt:message key="common.help"/></a></c:when>
      <c:when test="${type == 'blogFile'}"><a href="./help/files.html" target="_blank"><fmt:message key="common.help"/></a></c:when>
      <c:when test="${type == 'themeFile'}"><a href="./help/themes.html" target="_blank"><fmt:message key="common.help"/></a></c:when>
      <c:when test="${type == 'blogData'}"></c:when>
    </c:choose>
  </div>

  <h1>
    <c:choose>
      <c:when test="${type == 'blogImage'}"><fmt:message key="view.files.images"/></c:when>
      <c:when test="${type == 'blogFile'}"><fmt:message key="view.files.files"/></c:when>
      <c:when test="${type == 'themeFile'}"><fmt:message key="view.files.theme"/> (${blog.editableTheme.name})</c:when>
      <c:when test="${type == 'blogData'}"><fmt:message key="view.files.files"/></c:when>
    </c:choose>
  </h1>
  <h2><c:out value="${directory.absolutePath}" /> (<fmt:formatNumber value="${directory.sizeInKB}" type="number" minFractionDigits="0" maxFractionDigits="0" />&nbsp;KB)</h2>

  <div class="contentItemBody">
  <p>
    <c:if test="${pebbleContext.configuration.fileUploadQuota > -1}">
		<fmt:message key="admin.quotaUsage">
			<fmt:param>
				<fmt:formatNumber value="${currentUsage}" type="number" minFractionDigits="0" maxFractionDigits="0" />
			</fmt:param>
			<fmt:param>
				<fmt:formatNumber value="${pebbleContext.configuration.fileUploadQuota}" type="number" minFractionDigits="0" maxFractionDigits="0" />
			</fmt:param>
		</fmt:message>
    </c:if>
  </p>

  <c:choose>
  <c:when test="${not empty files}">
    <form name="filesForm" action="removeFiles.secureaction" method="post">
    <pebble:token/>
    <input type="hidden" name="path" value="${directory.absolutePath}" />
    <input type="hidden" name="type" value="${type}" />
    <table width="99%" cellspacing="0" cellpadding="4">
      <thead>
        <tr>
          <th><input type="checkbox" name="allFiles" onclick="toggleCheckAll(document.filesForm.allFiles, document.filesForm.name)"/></th>
          <th>Name</th>
          <th align="right">Size</th>
          <th align="right">Last modified</th>
          <th align="right">Actions</th>
        </tr>
      </thead>
      <tbody>
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
            <a href="${url:rewrite(aFile.url)}">${aFile.name}</a>
          </td>
          <td align="right">
            <fmt:formatNumber value="${aFile.sizeInKB}" type="number" minFractionDigits="2" maxFractionDigits="2" />&nbsp;KB
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
      </tbody>
    </table>

    <br />

    <table width="99%" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
          <input type="submit" name="submit" value="Remove" />
        </td>
      </tr>
    </table>

    </form>
  </c:when>
  <c:otherwise>
      <p>
        <fmt:message key="admin.noFilesInDirectory"/>
      </p>
  </c:otherwise>
  </c:choose>

  <c:if test="${not empty file}">
  <a name="manageFile" />
  <form name="copyFile" action="copyFile.secureaction" method="POST">
    <pebble:token/>
    <p>
      <b><fmt:message key="admin.renameCopyFile"/></b>
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
    <pebble:token/>
    <h3><fmt:message key="admin.createDirectory"/></h3>
    <p>
      Name
      <input type="hidden" name="type" value="${type}" />
      <input type="hidden" name="path" value="${directory.absolutePath}" />
      <input type="text" name="name" size="40" value="" />
      <input name="submit" type="submit" Value="Create Directory" />
    </p>
  </form>
  <%-- Note, pebble token must be in the action URL, because HttpController can't read a multipart/form-data request
    to find the token --%>
  <form name="uploadFile" enctype="multipart/form-data" action="${uploadAction}?<pebble:token query="true"/>" method="post">
    <input type="hidden" name="path" value="${directory.absolutePath}" />
    <h3>Upload file (files must be less than <fmt:formatNumber value="${pebbleContext.configuration.fileUploadSize}" type="number" />&nbsp;KB each)</h3>
    <p>
      <table width="99%" cellspacing="0" cellpadding="4">
        <thead>
          <tr>
          <th>
            <fmt:message key="admin.localFilename"/>
          </th>
          <th>
            <fmt:message key="admin.remoteFilename"/>
          </th>
          </tr>
        </thead>
        <tr id="file0">
          <td><input name="file0" type="file" onChange="populateFilename(this,document.uploadFile.filename0); showComponent('file1');" /></td>
          <td><input name="filename0" type="text" value="" /></td>
        </tr>
        <tr id="file1">
          <td><input name="file1" type="file" onChange="populateFilename(this,document.uploadFile.filename1); showComponent('file2');" /></td>
          <td><input name="filename1" type="text" value="" /></td>
        </tr>
        <tr id="file2">
          <td><input name="file2" type="file" onChange="populateFilename(this,document.uploadFile.filename2); showComponent('file3');" /></td>
          <td><input name="filename2" type="text" value="" /></td>
        </tr>
        <tr id="file3">
          <td><input name="file3" type="file" onChange="populateFilename(this,document.uploadFile.filename3); showComponent('file4');" /></td>
          <td><input name="filename3" type="text" value="" /></td>
        </tr>
        <tr id="file4">
          <td><input name="file4" type="file" onChange="populateFilename(this,document.uploadFile.filename4)" /></td>
          <td><input name="filename4" type="text" value="" /></td>
        </tr>
        <tr>
          <td align="right" colspan="2"><input type="submit" value="<fmt:message key="admin.uploadFileS"/>" /></td>
        </tr>
      </table>

    </p>
  </form>

  </div>

</div>

<script type="text/javascript">
  $('file1').style.display = 'none';
  $('file2').style.display = 'none';
  $('file3').style.display = 'none';
  $('file4').style.display = 'none';
</script>
