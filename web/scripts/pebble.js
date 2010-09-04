function openWindow(url, windowName, width, height) {
  popup = window.open(url, windowName, 'width='+width+',height='+height+',toolbar=0,resizable=1,scrollbars=1,menubar=0,status=1');
  popup.focus();
}

function confirmRemove(url) {
  if (confirm("Are you sure?")) {
    window.location = url;
  }
}

function populateFilename(fromField, toField) {
  var indexOfSlash = fromField.value.lastIndexOf("/");
  if (indexOfSlash < 0) {
    indexOfSlash= fromField.value.lastIndexOf("\\");
  }
  if (indexOfSlash < 0) {
    indexOfSlash = 0;
  }

  toField.value = fromField.value.substring(indexOfSlash, fromField.value.length);
}

function checkAll(checkboxes) {
  if (!checkboxes.length) {
    checkboxes.checked = true;
  } else {
    for (i = 0; i < checkboxes.length; i++) {
      checkboxes[i].checked = true;
    }
  }
}

function uncheckAll(checkboxes) {
  if (!checkboxes.length) {
    checkboxes.checked = false;
  } else {
    for (i = 0; i < checkboxes.length; i++) {
      checkboxes[i].checked = false;
    }
  }
}

function toggleCheckAll(checkbox, checkboxes) {
  if (checkbox.checked) {
    checkAll(checkboxes);
  } else {
    uncheckAll(checkboxes);
  }
}

function toggleVisibility(elementId) {
    var element = document.getElementById(elementId);
    if(element.style.display == 'none') {
        element.style.display = 'block';
    } else {
        element.style.display = 'none';
    }
}

function setFocus(elementId) {
  var element = document.getElementById(elementId);
  element.focus();
}

function showComponent(elementId) {
  var element = document.getElementById(elementId);
  element.style.display = 'block';
}

function hideComponent(elementId) {
    var element = document.getElementById(elementId);
    element.style.display = 'none';
}

function showMenu(elementId) {
  var element = document.getElementById(elementId);
  element.style.display = 'inline';
}

function hideMenu(elementId) {
    var element = document.getElementById(elementId);
    element.style.display = 'none';
}

function switchStyle(elementId, style) {
    var element = document.getElementById(elementId);
    element.className = style;
}

function showCommentForm() {
  Effect.Appear('commentFormDiv', 'blind');
  var oFCKeditor = null;
  try {
    oFCKeditor = FCKeditorAPI.GetInstance('commentBody') ;
  } catch (error) {
    // do nothing - FCKeditor isn't available so we'll just use the regular commentBody textarea
  }
  if (oFCKeditor != null) {
    oFCKeditor.SwitchEditMode();
    oFCKeditor.SwitchEditMode();
  }
}

function previewComment() {
  var data = { author:null, email:null, website:null, title:null };
  dwr.util.getValues(data);
  var oFCKeditor = null;
  try {
    oFCKeditor = FCKeditorAPI.GetInstance('commentBody') ;
  } catch (error) {
    // do nothing - FCKeditor isn't available so we'll just use the regular commentBody textarea
  }
  if (oFCKeditor != null) {
    data.body = oFCKeditor.GetHTML();
  } else {
    data.body = dwr.util.getValue("commentBody");
  }
  Pebble.previewComment(dwr.util.getValue("blogId"), data, function(data) {
    dwr.util.setValue("previewComment.body", data.body, { escapeHtml:false });
    dwr.util.setValue("previewComment.title", dwr.util.getValue("title"));
    var author = '';
    if (dwr.util.getValue("website") != null && dwr.util.getValue("website") != '') {
      dwr.util.setValue("previewComment.author", "<a href='" + dwr.util.getValue("website") + "'>" + dwr.util.getValue("author") + "</a>",  { escapeHtml:false });
    } else {
      dwr.util.setValue("previewComment.author", dwr.util.getValue("author"));
    }
  });
  new Effect.Highlight('previewComment');
}
