/*
 * Copyright (c) 2003-2011, Simon Brown
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
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.BlogEntryFormView;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Saves a blog entry.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class SaveBlogEntryAction extends SecureAction {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(SaveBlogEntryAction.class);

  /** the value used if the blog entry is being previewed rather than added */
  private static final String PREVIEW = "Preview";

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    String submitType = request.getParameter("submit");

    if (submitType != null && submitType.equalsIgnoreCase(PREVIEW)) {
      return previewBlogEntry(request);
    } else {
      return saveBlogEntry(request);
    }
  }

  private View previewBlogEntry(HttpServletRequest request) throws ServletException {
    BlogEntry blogEntry = getBlogEntry(request);

    populateBlogEntry(blogEntry, request);

    ValidationContext validationContext = new ValidationContext();
    blogEntry.validate(validationContext);
    getModel().put("validationContext", validationContext);
    getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);

    return new BlogEntryFormView();
  }

  private View saveBlogEntry(HttpServletRequest request) throws ServletException {
    BlogEntry blogEntry = getBlogEntry(request);
    Blog blog = blogEntry.getBlog();

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
        service.putBlogEntry(blogEntry);
        blog.info("Blog entry <a href=\"" + blogEntry.getLocalPermalink() + "\">" + blogEntry.getTitle() + "</a> saved.");
        getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
        return new RedirectView(blogEntry.getLocalPermalink());
      } catch (BlogServiceException be) {
        log.error(be.getMessage(), be);
        context.addError(be.getMessage());
        be.printStackTrace();
        return new BlogEntryFormView();
      }
    }
  }

  private BlogEntry getBlogEntry(HttpServletRequest request) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String id = request.getParameter("entry");
    String persistent = request.getParameter("persistent");

    if (persistent != null && persistent.equalsIgnoreCase("true")) {
      BlogService service = new BlogService();
      try {
        return service.getBlogEntry(blog, id);
      } catch (BlogServiceException e) {
        throw new ServletException(e);
      }
    } else {
      BlogEntry blogEntry = new BlogEntry(blog);
      blogEntry.setAuthor(SecurityUtils.getUsername());
      return blogEntry;
    }
  }

  private void populateBlogEntry(BlogEntry blogEntry, HttpServletRequest request) {
    Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
    String title = StringUtils.stripScriptTags(request.getParameter("title"));
    String subtitle = StringUtils.stripScriptTags(request.getParameter("subtitle"));
    String body = StringUtils.filterNewlines(request.getParameter("body"));
    String excerpt = StringUtils.filterNewlines(request.getParameter("excerpt"));
    String originalPermalink = request.getParameter("originalPermalink");
    String tags = request.getParameter("tags");
    String commentsEnabled = request.getParameter("commentsEnabled");
    String trackBacksEnabled = request.getParameter("trackBacksEnabled");
    String category[] = request.getParameterValues("category");
    String timeZone = request.getParameter("timeZone");

    // the date can only set on those entries that have not yet been persisted
    if (!blogEntry.isPersistent()) {
      DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, blog.getLocale());
      dateFormat.setTimeZone(blog.getTimeZone());
      dateFormat.setLenient(false);

      Date now = new Date();
      String dateAsString = request.getParameter("date");
      if (dateAsString != null && dateAsString.length() > 0) {
        try {
          Date date = dateFormat.parse(dateAsString);
          if (date.after(now)) {
            date = now;
          }
          blogEntry.setDate(date);
        } catch (ParseException pe) {
          log.warn(pe);
          blogEntry.setDate(now);
        }
      } else {
        // the date has been blanked out, so reset to "now"
        blogEntry.setDate(now);
      }
    }

    blogEntry.setTimeZoneId(timeZone);
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
    blogEntry.setOriginalPermalink(originalPermalink);
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