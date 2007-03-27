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

  toField.value = fromField.value.substring(indexOfSlash+1, fromField.value.length);
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

function previewComment() {
  var data = { author:null, email:null, website:null, title:null };
  dwr.util.getValues(data);
  data.body = dwr.util.getValue("commentBody");
  Pebble.previewComment(dwr.util.getValue("blogId"), data, function(data) {
    dwr.util.setValue("previewComment.body", data.body, { escapeHtml:false });
    dwr.util.setValue("previewComment.title", dwr.util.getValue("title"));
    dwr.util.setValue("previewComment.author", dwr.util.getValue("author"));
  });
  new Effect.Highlight('previewComment');
}