package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Blog;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.WikiRenderEngine;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseInitialRenderContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Decorates blog entries and comments by rendering them with Radeox, internal
 * links pointing to static pages within the blog.
 *
 * @author Simon Brown
 */
public class RadeoxDecorator extends BlogEntryDecoratorSupport {

  private static final String WIKI_START_TAG = "<wiki>";
  private static final String WIKI_END_TAG = "</wiki>";

  /**
   * Executes the logic associated with this decorator.
   *
   * @param chain   the chain of BlogEntryDecorators to apply
   * @param context the context in which the decoration is running
   * @throws BlogEntryDecoratorException if something goes wrong when running the decorator
   */
  public void decorate(BlogEntryDecoratorChain chain, BlogEntryDecoratorContext context)
      throws BlogEntryDecoratorException {

    BlogEntry blogEntry = context.getBlogEntry();
    InitialRenderContext initialContext = new BaseInitialRenderContext();
    initialContext.set(RenderContext.INPUT_LOCALE, getBlog().getLocale());
    RenderEngine engineWithContext = new RadeoxWikiRenderEngine(initialContext, blogEntry);

    blogEntry.setExcerpt(wikify(blogEntry.getExcerpt(), engineWithContext, initialContext));
    blogEntry.setBody(wikify(blogEntry.getBody(), engineWithContext, initialContext));

    chain.decorate(context);
  }

  private String wikify(String content, RenderEngine renderEngine, InitialRenderContext renderContext) {
    // is there work to do?
    if (content == null || content.length() == 0) {
      return "";
    }

    // this pattern says "take the shortest match you can find where there are
    // one or more characters between escape tags"
    //  - the match is case insensitive and DOTALL means that newlines are
    //  - considered as a character match
    Pattern p = Pattern.compile(WIKI_START_TAG + ".+?" + WIKI_END_TAG,
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher m = p.matcher(content);

    // while there are blocks to be escaped
    while (m.find()) {
      int start = m.start();
      int end = m.end();

      // grab the text, strip off the escape tags and transform it
      String textToWikify = content.substring(start, end);
      textToWikify = textToWikify.substring(WIKI_START_TAG.length(), textToWikify.length() - WIKI_END_TAG.length());
      textToWikify = renderEngine.render(textToWikify, renderContext);

      // now add it back into the original text
      content = content.substring(0, start) + textToWikify + content.substring(end, content.length());
      m = p.matcher(content);
    }

    return content;
  }

}

class RadeoxWikiRenderEngine extends BaseRenderEngine implements WikiRenderEngine {

  private BlogEntry blogEntry;

  public RadeoxWikiRenderEngine(InitialRenderContext context, BlogEntry blogEntry) {
    super(context);
    context.setRenderEngine(this);
    this.blogEntry = blogEntry;
  }

  public boolean exists(String name) {
    Blog blog = blogEntry.getBlog();
    BlogEntry staticPage = blog.getStaticPageIndex().getStaticPage(name);

    return (staticPage != null);
  }

  public boolean showCreate() {
    return true;
  }

  public void appendLink(StringBuffer buffer, String name, String view) {
    appendLink(buffer, name, view, null);
  }

  public void appendLink(StringBuffer buffer, String name, String view, String anchor) {
    buffer.append("<a href=\"");
    buffer.append(blogEntry.getBlog().getUrl() + "pages/" + name + ".html");
    if (anchor != null && anchor.trim().length() > 0) {
      buffer.append("#");
      buffer.append(anchor);
    }
    buffer.append("\">");
    buffer.append(view);
    buffer.append("</a>");
  }

  public void appendCreateLink(StringBuffer buffer, String name, String view) {
    buffer.append("<a href=\"addStaticPage.secureaction?staticName=");
    buffer.append(name);
    buffer.append("\">");
    buffer.append(view);
    buffer.append("</a><sup>?</sup>");
  }
}