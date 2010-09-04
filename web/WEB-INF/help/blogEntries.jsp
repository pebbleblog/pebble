<div class="contentItem">
  <h1>Blog Entries</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
    A blog entry is typically something like a short article, piece of news, insight, opinion and so on. From a technical perspective, blog entries in Pebble are just short pieces of HTML that represent the content that you wish to publish. This can be anything from a simple piece of text to an elaborate collection of text, images, tables, etc. In addition to this, Pebble stores other pieces of information alongside your blog entries such as a title, the date/time of publication, author and so on.
    </p>

    <h3>Adding a blog entry</h3>
    <p>
      To add a new blog entry, click the <a href="addBlogEntry.secureaction#form">New blog entry</a> and fill in the form.
      When you've finished writing the entry, clicking the "Preview" button will show you what your new entry will look like. If necessary, you can make further changes and preview them.
      When you're happy with the entry, click the "Save" button to save the new entry to your blog. By default, new entries created in an unpublished, or draft, state and must be explicity published by clicking the "Publish" button.
    </p>

    <p>
      The body of a blog entry is just a XHTML or HTML fragment, meaning that you can use any XHTML or HTML tags you like for markup and formatting. Common examples include using bold and italics. Also, you can include other elements such as hyperlinks and <a href="./help/images.html">images</a>.
    </p>

    <p>
      Switching on/off the rich text editor for the excerpt/body of the blog entry can be done through your <a href="editUserPreferences.secureaction">user preferences</a>. 
    </p>

    <h3>Excerpts</h3>
    <p>
    For long entries, you may not want the full content showing up on the summary (home, month or day view) pages, although still be visible via the permalinked page. For this reason, Pebble blog entries can be given excerpts. Typically these are shorter than blog entry bodies and, if populated, are displayed on the summary pages with a link to "Read more..." via the permalink. Excerpts are optional.
    </p>

    <h3>Edit and removing blog entries</h3>
    <p>
      Blog entries can be edited at any time after they have been saved, by clicking the "Edit" button next to the entry you want to edit. Likewise if you want to remove a blog entry.
    </p>

    <h3>Publishing/unpublishing blog entries</h3>
    <p>
      By default, all blog entries start out life as unpublished, or draft.
      To publish an entry, click the "Publish" button next to the entry you want to publish. This will take you to a page where
      you can choose the date/time that the entry will take on when it is published. "As-is" is the default, which retains the existing date/time of the blog entry. Once published, entries can also be unpublished.
      Take care when modifying the date/time associated with blog entries that have previously been published because doing so may alter the permalink. Only blog owners can publish/unpublish blog entries.
    </p>

    <h3>Aggregating content</h3>
    <p>
      If you also write content for an existing blog, you may want to publish/mirror that content on your Pebble blog, but still refer readers back to the original version. Pebble provides a simple way to achieve this by allowing you to store the original permalink alongside your blog content. By doing this, any content will show up on your Pebble blog, but the permalink will point always to the original content. When adding a new blog entry, simply enter the existing permalink into the "Original permalink" field.
      The value you enter in this field must be an absolute URL and will become the permalink for your blog entry. This means that when the entry shows up in a news aggregator, readers will be directed to the original version rather than the copy you have entered into Pebble.
    </p>

    <h3>Comments and TrackBacks</h3>
    <p>
      Responses can be disabled on a per blog entry basis, and this is useful should you want to direct responses to another location (e.g. an online forum) or simply not allow readers to leave comments for that entry.
    </p>

    <a name="attachments"></a><h3>Attachments</h3>
    <p>
    In a similar way to e-mail messages, you can "attach" a single file to a blog entry, and this can either be a file from your own blog
    or a file anywhere on the Internet. By attaching a file, readers of your blog will see an additional "Attachment" link from which
    they can download the file that you have referenced. Attached files are also represented in your RSS 2.0 feed through the enclosure element, making
    Pebble suitable for <a href="http://www.ipodder.org/whatIsPodcasting">podcasting</a>.
    </p>

    <p>
    To attach a file, enter a relative URL (e.g. <code>./files/somefile.mp3</code> to point to a <a href="./help/files.html">file</a> that you have already uploaded to your blog) or absolute URL for the file you'd like to attach to your blog entry. If the attachment size
    and type are left blank, they will be populated automatically when the blog entry is previewed or saved. See <a href="./help/files.html#mimeTypes">Files : MIME types</a> if the MIME type isn't automatically populated when you are trying to attach a file hosted from your blog.
    </p>
  </div>
</div>
