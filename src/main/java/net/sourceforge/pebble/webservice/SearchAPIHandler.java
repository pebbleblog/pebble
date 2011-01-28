
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

package net.sourceforge.pebble.webservice;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.search.SearchHit;
import net.sourceforge.pebble.search.SearchResults;
import net.sourceforge.pebble.util.Pageable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Extension to allow search and query of Blog posting via XML-RPC.
 *
 * @author tcp@favoritemedium.com
 */
public class SearchAPIHandler extends AbstractAPIHandler {

    static final String URL = "url";
    static final String BLOG_ID = "blogid";
    static final String BLOG_NAME = "blogName";
    static final String DATE_CREATED = "dateCreated";
    static final String USER_ID = "userId";
    static final String POST_ID = "postid";
    static final String CONTENT = "content";

    static final String TITLE_START_DELIMITER = "<title>";
    static final String TITLE_END_DELIMITER = "</title>";
    static final String CATEGORY_START_DELIMITER = "<category>";
    static final String CATEGORY_END_DELIMITER = "</category>";
    static final char BLOG_ID_SEPARATOR = '/';

    static final int PAGE_SIZE = 20;


    /** the log used by this class */
    private static Log log = LogFactory.getLog(SearchAPIHandler.class);


    /**
     * Search blog for specific string.
     * 
     * @param blogid    the ID of the blog (ignored)
     * @param username  the username used for logging in via XML-RPC
     * @param password  the password used for logging in via XML-RPC
     */
    public Vector search(String blogid, String username, String password, 
                        String searchString, String sortBy) {
        log.debug("search.search(" +
            blogid + ", " +
            username + ", xxxxxx, \"" +
            searchString + "," + 
            sortBy + "\")");

        Vector posts = new Vector();
        try {

            Blog blog = getBlogWithBlogId(blogid);
            SearchResults result = blog.getSearchIndex().search( searchString );

            if ( sortBy != null && sortBy.equalsIgnoreCase("date") ) {
                result.sortByDateDescending();
            } else {
                result.sortByScoreDescending();
            }

            List<SearchHit> hits = result.getHits();
            BlogService service = new BlogService();

            for (SearchHit hit : hits) {
                BlogEntry entry = service.getBlogEntry(hit.getBlog(), hit.getId());
                adaptBlogEntry(entry);
                posts.add(adaptBlogEntry(entry));
            }
            posts.add( searchResultSummary(hits, sortBy, searchString, 0, 0) );

        } catch (Exception ex) {
            log.error(ex);
        }
        return posts;
    }

    /**
     * Search blog for specific string with pagable parameters
     * 
     * @param blogid    the ID of the blog (ignored)
     * @param username  the username used for logging in via XML-RPC
     * @param password  the password used for logging in via XML-RPC
     */
    public Vector search(String blogid, String username, String password, 
                        String searchString, String sortBy, int pageSize, int offset) {
        log.debug("search.search(" +
            blogid + ", " +
            username + ", xxxxxx, \"" +
            searchString + "," + 
            sortBy + "\")");


        Vector posts = new Vector();
        try {

            Blog blog = getBlogWithBlogId(blogid);
            SearchResults result = blog.getSearchIndex().search( searchString );
            
            if ( sortBy != null && sortBy.equalsIgnoreCase("date") ) {
                result.sortByDateDescending();
            } else {
                result.sortByScoreDescending();
            }

            if ( pageSize <= 0 ) 
                pageSize = PAGE_SIZE;

            List<SearchHit> hits = result.getHits();
            Pageable pageable = new Pageable(hits);
            pageable.setPageSize(pageSize);
            pageable.setPage(offset);
            List<SearchHit> subList = pageable.getListForPage();

            BlogService service = new BlogService();

            for (SearchHit hit : subList ) {
                BlogEntry entry = service.getBlogEntry(hit.getBlog(), hit.getId());
                adaptBlogEntry(entry);
                posts.add(adaptBlogEntry(entry));
            }
            posts.add( searchResultSummary(subList, sortBy, searchString, pageSize, offset) );

        } catch (Exception ex) {
            log.error(ex);
        }
        return posts;
    }

    /**
     * Helper method to adapt a blog entry into an XML-RPC compatible struct.
     * Since the Blogger API doesn't support titles, the title is wrapped in
     * &lt;title&gt;&lt;/title&gt; tags.
     *
     * @param entry   the BlogEntry to adapt
     * @return  a Hashtable representing the major properties of the entry
     */
    private Hashtable adaptBlogEntry(BlogEntry entry) {
        Hashtable post = new Hashtable();
        String categories = "";
        Iterator it = entry.getCategories().iterator();
        while (it.hasNext()) {
            Category category = (Category)it.next();
            categories += category.getId();
            if (it.hasNext()) {
                categories += ",";
            }
        }
        post.put(DATE_CREATED, entry.getDate());
        post.put(USER_ID, entry.getAuthor());
        post.put(POST_ID, formatPostId(entry.getBlog().getId(), entry.getId()));
        post.put(CONTENT, TITLE_START_DELIMITER + entry.getTitle() + TITLE_END_DELIMITER
                + CATEGORY_START_DELIMITER + categories + CATEGORY_END_DELIMITER + entry.getBody());

        return post;
    }

    /**
     * Auto create a Map of search result summary.
     * @param result of the search hits.
     */
    private Map searchResultSummary(List<SearchHit> result, String sortBy,
                                        String query, int pageIndex, int offset) {
        Map data = new Hashtable();
        data.put("size", result.size() );
        data.put("sortBy", sortBy);
        data.put("index", pageIndex);
        data.put("offset", offset);
        data.put("query", query);

        return data;
    }

}
