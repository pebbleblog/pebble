package net.sourceforge.pebble.dao.orient.model;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.State;

/**
 * An OrientDB Comment
 */
public class OrientComment extends OrientResponse {
  private String body;
  private String author;
  private String website;
  private String avatar;
  private String email;
  private long parent = -1;
  private String state = State.APPROVED.getName();
  private boolean authenticated = false;

  private OrientComment() {
  }

  public OrientComment(Comment comment) {
    super(comment);
    body = comment.getBody();
    author = comment.getAuthor();
    website = comment.getWebsite();
    avatar = comment.getAvatar();
    email = comment.getEmail();
    if (comment.getParent() != null) {
      parent = comment.getParent().getId();
    }
    state = comment.getState().getName();
    authenticated = comment.isAuthenticated();
  }

  public Comment toComment(BlogEntry blogEntry) {
    Comment comment = blogEntry.createComment(title, body, author, email, website, avatar, ipAddress, date, State.getState(state));
    if (parent != -1) {
      comment.setParent(blogEntry.getComment(parent));
    }
    comment.setAuthenticated(authenticated);
    return comment;
  }
}
