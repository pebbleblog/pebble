package net.sourceforge.pebble.plugin.decorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a chain of blog entry decorators..
 * 
 * @author Simon Brown
 */
public class BlogEntryDecoratorChain {

  /** the iterator used by this chain */
  private Iterator iterator;

  /**
   * Creates a new chain based upon the list of decorators.
   *
   * @param decorators    a List of BlogEntryDecorator instances
   */
  public BlogEntryDecoratorChain(List decorators) {
    if (decorators == null) {
      decorators = new ArrayList(0);
    }
    this.iterator = decorators.iterator();
  }

  /**
   * Executes the remaining decorators associated with this chain.
   *
   * @param context     the context in which the decoration is running
   * @throws BlogEntryDecoratorException
   *          if something goes wrong when running the decorator
   */
  public void decorate(BlogEntryDecoratorContext context)
      throws BlogEntryDecoratorException {

    if (iterator.hasNext()) {
      BlogEntryDecorator decorator = (BlogEntryDecorator)iterator.next();
      decorator.decorate(this, context);
    }

  }

}
