package net.sourceforge.pebble.util;

import net.sourceforge.pebble.domain.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Ranker for tags
 */
public class TagRanker {

  /**
   * Calculate the rankings for each of the tags, and return a new list.
   *
   * @param tags The tags to calculate the rankings for
   * @return The new list of tags
   */
  public static Collection<Tag> calculateTagRankings(Collection<? extends Tag> tags) {
    if (tags.size() > 0) {
      // find the maximum
      int maxBlogEntries = 0;
      for (Tag tag : tags) {
        if (tag.getNumberOfBlogEntries() > maxBlogEntries) {
          maxBlogEntries = tag.getNumberOfBlogEntries();
        }
      }

      int[] thresholds = new int[10];
      for (int i = 0; i < 10; i++) {
        thresholds[i] = (int) Math.round((maxBlogEntries / 10.0) * (i + 1));
      }

      List<Tag> orderedTags = new ArrayList<Tag>();

      // now rank the tags
      for (Tag tag : tags) {
        int rank = calculateRank(thresholds, tag.getNumberOfBlogEntries());

        if (tag.getNumberOfBlogEntries() > 0) {
          orderedTags.add(new Tag(tag.getName(), tag.getBlog(), rank, tag.getNumberOfBlogEntries()));
        }
      }
      Collections.sort(orderedTags);
      return Collections.unmodifiableCollection(orderedTags);
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * Calculates the rank for a tag
   *
   * @param thresholds The ranking thresholds
   * @param numberOfBlogEntries The number of blog entries to calculate for
   * @return The rank
   */
  private static int calculateRank(int[] thresholds, int numberOfBlogEntries) {
    for (int i = 0; i < thresholds.length; i++) {
      if (numberOfBlogEntries <= thresholds[i]) {
        return i + 1;
      }
    }
    return thresholds[thresholds.length - 1];
  }


}
