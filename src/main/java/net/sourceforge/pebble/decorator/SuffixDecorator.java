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

package net.sourceforge.pebble.decorator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.util.I18n;

/**
 * Adds user-defined suffix to each blog post. Suffix can contain
 * strings, eg ${blogEntry.title} or ${blogEntry.permaLink.encoded}
 * which will be substituted (naively).
 * 
 * @author Mike Bremford
 */
public class SuffixDecorator extends ContentDecoratorSupport {

    /**
     * The name of the configuration property which specifies the suffix
     */
    public String SUFFIX = "SuffixDecorator.suffix";

    /**
     * Decorates the specified blog entry.
     * 
     * @param context the context in which the decoration is running
     * @param blogEntry the blog entry to be decorated
     */
    public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
        Blog blog = blogEntry.getBlog();
        PluginProperties props = blog.getPluginProperties();
        String suffix;
        synchronized(props) {
            suffix = props.getProperty(SUFFIX);
        }

        if (suffix != null && suffix.trim().length() > 0) {
            Map<String,String> map = new HashMap();
            map.put("blogEntry.title", blogEntry.getTitle());
            map.put("blogEntry.permalink", blogEntry.getPermalink());
            map.put("blogEntry.excert", blogEntry.getExcerpt());
            map.put("blogEntry.author", blogEntry.getAuthor());
            map.put("blogEntry.id", blogEntry.getId());
            map.put("blogEntry.subtitle", blogEntry.getSubtitle());
            map.put("blogEntry.tags", blogEntry.getTags());

            // Scan over suffix matching for ${blogEntry.title} or ${blogEntry.title.encoded}
            // and replace the string with the appropriate property

            for (Iterator<Map.Entry<String,String>> i = map.entrySet().iterator();i.hasNext();) {
                try {
                    Map.Entry<String,String> e = i.next();
                    String val = e.getValue();
                    if (val == null) {
                        val = "";
                    }

                    String key = "${"+e.getKey()+"}";
                    for (int j=suffix.indexOf(key);j>=0;j=suffix.indexOf(key, j)) {
                        suffix = suffix.substring(0, j) + val + suffix.substring(j + key.length());
                        j += val.length() - key.length();
                    }

                    key = "${"+e.getKey()+".encoded}";
                    for (int j=suffix.indexOf(key);j>=0;j=suffix.indexOf(key, j)) {
                        String eval = URLEncoder.encode(val, "UTF-8");
                        suffix = suffix.substring(0, j) + eval + suffix.substring(j + key.length());
                        j += eval.length() - key.length();
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);      // Can't happem, UTF-8 is always there
                }
            }

            String body = blogEntry.getBody();
            if (body != null && body.trim().length() > 0) {
                blogEntry.setBody(body + suffix);
            }
        }
    }

}
