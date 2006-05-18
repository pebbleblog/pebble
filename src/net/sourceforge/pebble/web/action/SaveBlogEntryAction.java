/*
 * Copyright (c) 2003-2006, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.index.SearchIndex;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.BlogEntryFormView;
import net.sourceforge.pebble.web.validation.ValidationContext;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Saves a blog entry.
 *
 * @author    Simon Brown
 */
public class SaveBlogEntryAction extends SecureAction {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(SaveBlogEntryAction.class);

  /** the value used if the blog entry is being previewed rather than added */
  private static final String PREVIEW = "Preview";
  private static final String SAVE_AS_DRAFT = "Save as Draft";
  private static final String SAVE_AS_TEMPLATE = "Save as Template";

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    String submitType = request.getParameter("submit");

    int type = BlogEntry.NEW;
    try {
      type = Integer.parseInt(request.getParameter("type"));
    } catch (NumberFormatException nfe) {
      // do nothing, type will default to NEW
    }
    switch (type) {
      case BlogEntry.STATIC_PAGE :
        getModel().put(Constants.TITLE_KEY, "Edit static page");
        break;
      default :
        getModel().put(Constants.TITLE_KEY, "Edit blog entry");
        break;
    }

    if (submitType != null && submitType.equalsIgnoreCase(PREVIEW)) {
      return previewBlogEntry(request);
    } else if (submitType != null && submitType.equalsIgnoreCase(SAVE_AS_DRAFT)) {
      return saveBlogEntryAsDraft(request);
    } else if (submitType != null && submitType.equalsIgnoreCase(SAVE_AS_TEMPLATE)) {
      return saveBlogEntryAsTemplate(request);
    } else {
      switch (type) {
        case BlogEntry.STATIC_PAGE :
          return saveBlogEntryAsStaticPage(request);
        default :
          return postBlogEntry(request);
      }
    }
  }

  private View previewBlogEntry(HttpServletRequest request) {
    BlogEntry blogEntry = getBlogEntry(request);

    // we don't want to actually edit the original whilst previewing
    blogEntry = (BlogEntry)blogEntry.clone();
    populateBlogEntry(blogEntry, request);

    ValidationContext validationContext = new ValidationContext();
    blogEntry.validate(validationContext);
    getModel().put("validationContext", validationContext);
    getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);

    return new BlogEntryFormView();
  }

  private View postBlogEntry(HttpServletRequest request) {
    Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
    BlogEntry blogEntry = getBlogEntry(request);

    populateBlogEntry(blogEntry, request);

    ValidationContext context = new ValidationContext();
    blogEntry.validate(context);

    getModel().put("validationContext", context);
    getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);

    if (context.hasErrors())  {
      return new BlogEntryFormView();
    } else {
      BlogService service = new BlogService();
      try {
        switch (blogEntry.getType()) {
          case BlogEntry.NEW :
            service.putBlogEntry(blogEntry);
            break;
          case BlogEntry.TEMPLATE :
            blogEntry = new BlogEntry(blog);
            populateBlogEntry(blogEntry, request);
            service.putBlogEntry(blogEntry);
            break;
          case BlogEntry.DRAFT :
            BlogEntry draftBlogEntry = blogEntry;
            blogEntry = new BlogEntry(blog);
            populateBlogEntry(blogEntry, request);
            service.putBlogEntry(blogEntry);
            draftBlogEntry.remove();
            break;
          default :
            service.putBlogEntry(blogEntry);
            break;
        }

        getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
        return new RedirectView(blogEntry.getLocalPermalink());
      } catch (BlogException be) {
        log.error(be.getMessage(), be);
        be.printStackTrace();
        return new BlogEntryFormView();
      }
    }
  }

  private View saveBlogEntryAsDraft(HttpServletRequest request) {
    BlogEntry blogEntry = getBlogEntry(request);
    blogEntry = (BlogEntry)blogEntry.clone();
    blogEntry.setType(BlogEntry.DRAFT);
    populateBlogEntry(blogEntry, request);


    try {
      blogEntry.store();
    } catch (BlogException be) {
      log.error(be.getMessage(), be);
      be.printStackTrace();

      getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
      return new BlogEntryFormView();
    }

    return new ForwardView("/viewDrafts.secureaction");
  }

  private View saveBlogEntryAsTemplate(HttpServletRequest request) {
    BlogEntry blogEntry = getBlogEntry(request);
    blogEntry = (BlogEntry)blogEntry.clone();
    blogEntry.setType(BlogEntry.TEMPLATE);
    populateBlogEntry(blogEntry, request);

    try {
      blogEntry.store();
    } catch (BlogException be) {
      log.error(be.getMessage(), be);
      be.printStackTrace();

      getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
      return new BlogEntryFormView();
    }

    return new ForwardView("/viewTemplates.secureaction");
  }

  private View saveBlogEntryAsStaticPage(HttpServletRequest request) {
    BlogEntry blogEntry = getBlogEntry(request);
    blogEntry.setType(BlogEntry.STATIC_PAGE);
    populateBlogEntry(blogEntry, request);

    ValidationContext context = new ValidationContext();
    blogEntry.validate(context);

    if (context.hasErrors())  {
      getModel().put("validationContext", context);
      getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
      return new BlogEntryFormView();
    } else {
      try {
        BlogService service = new BlogService();
        service.putBlogEntry(blogEntry);
        blogEntry.store();

        blogEntry.getBlog().getSearchIndex().index(blogEntry);
        blogEntry.getBlog().getStaticPageIndex().reindex();
      } catch (BlogException be) {
        log.error(be.getMessage(), be);
        be.printStackTrace();

        getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
        return new BlogEntryFormView();
      }

      return new RedirectView(blogEntry.getLocalPermalink());
    }
  }

  private BlogEntry getBlogEntry(HttpServletRequest request) {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String id = request.getParameter("entry");
    int type = BlogEntry.NEW;
    try {
      type = Integer.parseInt(request.getParameter("type"));
    } catch (NumberFormatException nfe) {
      // do nothing, type will default to NEW
    }

    switch (type) {
      case BlogEntry.PUBLISHED :
        BlogService service = new BlogService();
        return service.getBlogEntry(blog, id);
      case BlogEntry.TEMPLATE :
        return blog.getBlogEntryTemplate(id);
      case BlogEntry.DRAFT :
        return blog.getDraftBlogEntry(id);
      case BlogEntry.STATIC_PAGE :
        BlogEntry blogEntry = blog.getStaticPage(id);
        if (blogEntry == null) {
          blogEntry = blog.getBlogForToday().createStaticPage();
        }
        return blogEntry;
      default :
        // we're creating a new blog entry
        return blogEntry = new BlogEntry(blog);
    }
  }

  private void populateBlogEntry(BlogEntry blogEntry, HttpServletRequest request) {
    Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
    String title = request.getParameter("title");
    String subtitle = request.getParameter("subtitle");
    String body = StringUtils.filterNewlines(request.getParameter("body"));
    String excerpt = StringUtils.filterNewlines(request.getParameter("excerpt"));
    String originalPermalink = request.getParameter("originalPermalink");
    String tags = request.getParameter("tags");
    String staticName = request.getParameter("staticName");
    String commentsEnabled = request.getParameter("commentsEnabled");
    String trackBacksEnabled = request.getParameter("trackBacksEnabled");
    String category[] = request.getParameterValues("category");
    String author = SecurityUtils.getUsername();

    blogEntry.setTitle(title);
    blogEntry.setSubtitle(subtitle);
    blogEntry.setBody(body);
    blogEntry.setExcerpt(excerpt);
    Set categories = new HashSet();
    if (category != null) {
      for (int i = 0; i < category.length; i++) {
        categories.add(blog.getCategory(category[i]));
      }
    }
    blogEntry.setCategories(categories);
    blogEntry.setTags(tags);
    blogEntry.setAuthor(author);
    blogEntry.setOriginalPermalink(originalPermalink);
    blogEntry.setStaticName(staticName);
    if (commentsEnabled != null && commentsEnabled.equalsIgnoreCase("true")) {
      blogEntry.setCommentsEnabled(true);
    } else {
      blogEntry.setCommentsEnabled(false);
    }
    if (trackBacksEnabled != null && trackBacksEnabled.equalsIgnoreCase("true")) {
      blogEntry.setTrackBacksEnabled(true);
    } else {
      blogEntry.setTrackBacksEnabled(false);
    }

    String attachmentUrl = request.getParameter("attachmentUrl");
    String attachmentSize = request.getParameter("attachmentSize");
    String attachmentType = request.getParameter("attachmentType");
    if (attachmentUrl != null && attachmentUrl.length() > 0) {
      Attachment attachment = populateAttachment(blogEntry, attachmentUrl, attachmentSize, attachmentType);
      blogEntry.setAttachment(attachment);
    } else {
      blogEntry.setAttachment(null);
    }
  }

  private Attachment populateAttachment(BlogEntry blogEntry, String attachmentUrl, String attachmentSize, String attachmentType) {
    if (attachmentSize == null || attachmentSize.length() == 0) {
      String absoluteAttachmentUrl =  attachmentUrl;
      try {
        HttpClient httpClient = new HttpClient();
        if (absoluteAttachmentUrl.startsWith("./")) {
          absoluteAttachmentUrl = blogEntry.getBlog().getUrl() + absoluteAttachmentUrl.substring(2);
        }

        HeadMethod headMethod = new HeadMethod(absoluteAttachmentUrl);
        int status = httpClient.executeMethod(headMethod);
        if (status == 200) {
          Header attachmentSizeHeader = headMethod.getResponseHeader("Content-Length");
          if (attachmentSizeHeader != null) {
            attachmentSize = attachmentSizeHeader.getValue();
          }
          Header attachmentTypeHeader = headMethod.getResponseHeader("Content-Type");
          if (attachmentTypeHeader != null) {
            attachmentType = attachmentTypeHeader.getValue();
          }
        }
      } catch (IOException e) {
        log.warn("Could not get details for attachment located at " + absoluteAttachmentUrl + " : " + e.getMessage());
      }
    }

    Attachment attachment = new Attachment();
    attachment.setUrl(attachmentUrl);
    if (attachmentSize != null && attachmentSize.length() > 0) {
      attachment.setSize(Long.parseLong(attachmentSize));
    }
    attachment.setType(attachmentType);

    return attachment;
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.BLOG_CONTRIBUTOR_ROLE};
  }

}
