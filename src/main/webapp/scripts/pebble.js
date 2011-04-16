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
    var avatar = dwr.util.getValue("avatar");
    if (avatar != null && avatar != "") {
      Element.insert("previewComment", {top: "<img src='" + avatar + "' class='avatar'/>"});
    }
  });
  Effect.Highlight('previewComment');
}

function updatePluginProperty(event) {
    var name = this.name;
    var value = this.value;
    var formElements = event.findElement("form").elements;
    // Find all elements in the form with the same name
    for (var i = 0; i < formElements.length; i++) {
        if (formElements[i].name == name) {
            formElements[i].value = value;
        }
    }
}

function initPluginProperties() {
    var formElements = document.forms["pluginsForm"].elements;
    for (var i = 0; i < formElements.length; i++) {
        if (formElements[i].name.indexOf("pluginProperty_") == 0) {
            Event.observe(formElements[i], "change", updatePluginProperty);
        }
    }
}

function initSingleSelect(element) {
  var select = document.forms["pluginsForm"].elements[element];
  var options = select.options;
  // Show the selected one
  for (var i = 0; i < options.length; i++) {
    var option = options[i];
    if (option.selected) {
      $("pluginsBox_" + element + "_" + option.value + "_pluginConfig").setStyle({display: "block"});
    }
  }
  // Attach callback
  Event.observe(select, "change", function(event) {
    for (var i = 0; i < options.length; i++) {
      var option = options[i];
      if (option.selected) {
        $("pluginsBox_" + element + "_" + option.value + "_pluginConfig").setStyle({display: "block"});
      } else {
        $("pluginsBox_" + element + "_" + option.value + "_pluginConfig").setStyle({display: "none"});
      }
    }
  })
}

// Update the form
function updateOpenIdCommentAuthor(author, website, email, avatar, logoutCallback) {
  var form = document.forms["commentForm"];
  form.elements["author"].value = author;
  form.elements["website"].value = website;
  form.elements["email"].value = email;
  form.elements["avatar"].value = avatar;
  // Hide everything that doesn't need to be seen
  $("commentAuthorDetails").setStyle({display : "none"});
  $("openIdCommentAuthorProviders").setStyle({display : "none"});
  // Show the user in place of the open id buttons
  Element.update("openIdCommentAuthorDetails", "<img class='avatar' src='" + avatar + "'/><a href='" + website + "'>" +
                                            author + "</a>");
  Effect.Appear("openIdCommentAuthor");
  if (logoutCallback) {
    $("openIdCommentAuthor").openIdLogoutCallback = logoutCallback;
  }
}

function logoutOpenIdCommentAuthor() {
  // Clear the form
  var form = document.forms["commentForm"];
  form.elements["author"].value = "";
  form.elements["website"].value = "";
  form.elements["email"].value = "";
  form.elements["avatar"].value = "";
  // Hide the open id div
  $("openIdCommentAuthor").setStyle({display : "none"});
  // Show the providers div and author details
  Effect.Appear("openIdCommentAuthorProviders");
  Effect.Appear("commentAuthorDetails");
  // Call the callback to log the user out of the open id provider
  if ($("openIdCommentAuthor").openIdLogoutCallback) {
    $("openIdCommentAuthor").openIdLogoutCallback()
  }
}

/**
 * LOGIN
 */
function initLoginScreen() {
  var first = true;
  $$(".loginOption").each(function(option) {
    var loginOption = $(option);
    if (first) {
      loginOption.addClassName("loginOptionSelected");
      first = false;
    } else {
      $(loginOption.id + "Area").hide();
    }
    loginOption.observe("click", selectLoginOption);
  });
  $$(".loginUsingGoogle").each(function(div) {
    div.observe("click", loginUsingGoogle);
  });
}
function selectLoginOption(event) {
  var id = this.id;
  $$(".loginOption").each(function(option) {
    var loginOption = $(option);
    var area = $(loginOption.id + "Area");
    if (loginOption.id == id) {
      if (!loginOption.hasClassName("loginOptionSelected"))
      {
        loginOption.addClassName("loginOptionSelected");
        area.show()
      }
    } else {
      if (loginOption.hasClassName("loginOptionSelected")) {
        loginOption.removeClassName("loginOptionSelected");
        area.hide()
      }
    }
  });
}
function loginUsingGoogle(event) {
  // This is the URL for google openids
  $("openIdIdentifier").value = "https://www.google.com/accounts/o8/id";
  $("openIdLoginForm").submit();
}

function etPhoneHome(context) {
  setTimeout(function() {
    var url = "/ping";
    if (context) {
      url = context + url;
    }
    new Ajax.Request(url, {method: "get"});
    etPhoneHome(context);
  }, 300000);
}


/* Companion utils */
function openCompanion() {
  $("companionContainer").setStyle({display:"block"});
  $("companionContainerOpenLink").setStyle({display:"none"});
}
function closeCompanion() {
  $("companionContainer").setStyle({display:"none"});
  $("companionContainerOpenLink").style.display="";
}
function saveCompanion() {
  $("companionResult").innerHTML = "...";
  
  var form = document.forms["editBlogEntry"];
  if (!form) form = document.forms["editStaticPage"];
  var sToken = form.elements["pebbleSecurityToken"].value;
  
  new Ajax.Request('saveBlogCompanion.secureaction',
  {
    method:'post',
    parameters: {content: $("companionData").value, pebbleSecurityToken: sToken},
    onSuccess: function(transport){
      var response = transport.responseText;
      $("companionResult").innerHTML = response;
    },
    onFailure: function(){ 
      $("companionResult").innerHTML = "KO";
    }
  });
  
}
