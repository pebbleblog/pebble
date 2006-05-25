<div class="contentItem">
  <div class="title">Blog Entries</div>
  <div class="subtitle">&nbsp;</div>

  <div class="contentItemBody">
    <p>
    A blog entry is typically something like a short article, piece of news, insight, abstract, opinion and so on. From a technical perspective, blog entries in Pebble are just short pieces of HTML that represent the content that you wish to display. This can be anything from a simple piece of text to an elaborate collection of text, images, tables, etc. In addition to this, Pebble stores other pieces of information alongside your blog entries such as a title, the date/time of publication and author.
    </p>

    <h3>Adding a Blog Entry</h3>

    <p>
    To add a new blog entry, click the <b>New blog entry</b> link after logging in. On this page, just fill in the title and main body of the entry. In addition to the title and body, a blog entry can be associated with one or more categories that you've previously defined. To do this, just tick the appropriate categories. Ignore the other fields for now - they are discussed later. When you've finished writing the entry, clicking the <b>Preview</b> button will show you what your new entry will look like. If necessary, you can make further changes and preview them.
    When you're happy with the entry, click the <b>Post to Blog</b> button to publish the new entry to your blog. After a new entry has been posted, it will show up on the main page of your blog.
    <img src="./images/blog-entries-1.png" class="screenshot" />
    </p>

    <p>
    The body of a blog entry is just a XHTML or HTML fragment, meaning that you can use any XHTML or HTML tags you like for markup and formatting. Common examples include using bold and italics. Also, you can include other elements such as hyperlinks and <a href="images.jsp">images</a>.
    </p>

    <h3>Excerpts</h3>

    <p>
    For long entries, you may not want the full content showing up on the summary (home, month or day view) pages, although still be visible via the permalinked page. For this reason, Pebble blog entries can be given excerpts. Typically these are shorter than blog entry bodies and, if populated, are displayed on the summary pages with a link to "Read more..." via the permalink.
    </p>

    <h3>Editing a Blog Entry</h3>

    <p>
    Blog entries can be edited at any time after they have been posted. To edit an entry, once you have logged in, click the <b>Edit</b> link next to the appropriate blog entry. This opens the same page used to add a new entry, and you can again edit, preview and post when finished.
    </p>

    <h3>Removing a Blog Entry</h3>

    <p>
    Existing blog entries can also be removed at any time. To remove an entry, click the <b>Remove</b> link next to the appropriate blog entry. This will open a popup window asking you to confirm your action.
    </p>

    <h3>Draft Blog Entries</h3>

    <p>
    At any time while editing a blog entry, you can save it as a draft and come back to it later. To do this, just click the <b>Save as Draft</b> button. To edit the draft at a later date, after logging in, click the <b>Drafts</b> link to open a page containing a list of all drafts that have been saved. Clicking on an entry will allow you to edit it once more where you can again save it as a draft or post it to your blog.
    </p>

    <h3>Templates</h3>

    <p>
    To help you write blog entries that are similar in some way, Pebble supports the notion of blog entry templates. By default, a blog entry contains opening and closing HTML paragraph tags as a starting point for new blog entries. For complex entries such as those containing images, bulleted lists, book reviews and so on, you can save time by creating a template that can serve as a starting point.
    At any time while writing a blog entry, click the <b>Save as Template</b> button to save the current blog entry as a template.  To edit your templates, click the <b>Templates</b> link after logging in and a page will be displayed where you can edit and remove templates. To use a template, just click on it to open the normal blog entry page.
    </p>

    <h3>Aggregating Content</h3>

    <p>
    If you also write content for an existing blog, you may want to publish/mirror that content on your Pebble blog, but still refer readers back to the original version. Pebble provides a simple way to achieve this by allowing you to store the original permalink alongside your blog content. By doing this, any content will show up on your Pebble blog, but the permalink will point always to the original content. When adding a new blog entry, simply enter the existing permalink into the <b>Original permalink</b> field.
    </p>

    <div class="note">The value you enter in this field will become the permalink for your blog entry. This means that when the entry shows up in a news aggregator, readers will be directed to the original version rather than the copy you have entered into Pebble.</div>

    <h3>Disabling Comments and TrackBacks</h3>

    <p>
    Another useful feature is that you can choose to turn off <a href="comments.html">comments</a> and <a href="trackbacks.html">TrackBacks</a> so that you can direct responses to another location (e.g. an online forum) or just not allow readers to leave comments for that entry at all.
    </p>

    <a name="attachments"></a><h3>Attachments</h3>

    <p>
    In a similar way to e-mail messages, you can "attach" a single file to a blog entry, and this can either be a file from your own blog
    or a file anywhere on the Internet. By attaching a file, readers of your blog will see an additional <b>Attachment</b> link from which
    they can download the file that you have referenced. Attached files are also represented in your RSS 2.0 feed through the enclosure element, making
    Pebble suitable for <a href="http://www.ipodder.org/whatIsPodcasting">podcasting</a>.
    </p>

    <p>
    To attach a file, enter a relative URL (e.g. <code>./files/somefile.mp3</code> to point to a <a href="files.html">file</a> that you have already uploaded to your blog) or absolute URL for the file you'd like to attach to your blog entry. If the attachment size
    and type are left blank, they will be populated automatically when the blog entry is previewed or saved. If the MIME type isn't automatically populated when you are trying to attach a file hosted from your blog, please see <a href="files.html">Files</a> for details of how to add a MIME type.
    </p>

    <a name="moderation"></a><h3>Blog entry moderation</h3>

    <p>
    Blog entries in Pebble can have one of three statuses - approved, pending or rejected. By default, all new blog entries have
    a status of approved, which means that they will be immediately published on your blog.
    To require that all new blog entries are manually approved by the blog owner before being published, you can add the
    <code>net.sourceforge.pebble.event.blogentry.MarkPendingListener</code> to your list of blog entry listeners. This will mark all new and changed
    blog entries as pending so that they require approval from a blog owner.
    </p>

    <p>
    Once logged in as a blog owner, you will see links to approve and reject next to each blog entry
    that is in the pending state. If used in conjunction with the <code>net.sourceforge.pebble.event.blogentry.EmailNotificationListener</code>, the recipients
    specified in the blog properties page
    will receive an e-mail notification asking that the blog entry is approved.
    </p>

    <h3>Hiding unapproved blog entries</h3>

    <p>
    To ensure that unapproved blog entries are hidden from view, add the
    <code>net.sourceforge.pebble.plugin.decorator.HideUnpublishedBlogEntriesDecorator</code> to your list of blog entry
    decorators. With this decorator, you will see unapproved blog entries only when logged in as a blog owner or contributor.
    </p>
  </div>
</div>
