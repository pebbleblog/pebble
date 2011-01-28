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

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.domain.Blog;

/**
 * Converts &lt;thumbnail&gt; tags into image thumbnails.
 * @author Alan Burlison
 */
public class ThumbnailDecorator extends ContentDecoratorSupport {

    /**
     * Decorates the specified blog entry.
     *
     * @param context   the context in which the decoration is running
     * @param blogEntry the blog entry to be decorated
     */
    @Override
    public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
        Blog blog = blogEntry.getBlog();
        blogEntry.setBody(replaceTags(blogEntry.getBody(), blog));
        blogEntry.setExcerpt(replaceTags(blogEntry.getExcerpt(), blog));

    }

    /**
     * Decorates the specified static page.
     *
     * @param context    the context in which the decoration is running
     * @param staticPage the static page to be decorated
     */
    @Override
    public void decorate(ContentDecoratorContext context,
      StaticPage staticPage) {
        staticPage.setBody(replaceTags(staticPage.getContent(),
          staticPage.getBlog()));
    }

    /**
     * Find all the thumbnail tags and replace them with the equivalent HTML.
     * @param content the page content
     * @param blog the current blog.
     * @return the decorated content.
     */
    private String replaceTags(String content, Blog blog) {

        // Check there is something to do.
        if (content == null || content.length() == 0) {
            return "";
        }

        // Establish default sizes.
        int defaultThumbSize = 200;
        int defaultPopupSize = 640;
        PluginProperties props = blog.getPluginProperties();
        if (props.hasProperty(thumbSizeProp)) {
            try {
                defaultThumbSize =
                  Integer.parseInt(props.getProperty(thumbSizeProp));
            } catch (NumberFormatException nfe) {
                // Ignore.
            }
        }
        if (props.hasProperty(popupSizeProp)) {
            try {
                defaultPopupSize =
                  Integer.parseInt(props.getProperty(popupSizeProp));
            } catch (NumberFormatException nfe) {
                // Ignore.
            }
        }

        // Find all the thumbnail tags.
        Matcher tagM = tagRE.matcher(content);
        StringBuffer sb = new StringBuffer(content.length());
        while (tagM.find()) {
            StringBuilder repl = new StringBuilder();
            Matcher attrM = attrRE.matcher(tagM.group(1));
            String src = null;
            String alt = "";
            int thumbSize = defaultThumbSize;
            int popupSize = defaultPopupSize;

            // Parse the attributes of the tag.
            while (attrM.find()) {
                String an = attrM.group(1);
                String av = attrM.group(2);
                if (an.equalsIgnoreCase("src")) {
                    src = av;
                } else if (an.equalsIgnoreCase("alt")) {
                    alt = av;
                } else if (an.equalsIgnoreCase("thumbSize")) {
                    try {
                        thumbSize = Integer.parseInt(av);
                    } catch (NumberFormatException e) {
                        tagM.appendReplacement(sb, String.format(
                          "<!-- ERROR: invalid thumbnail thumbSize \"%s\" -->",
                          av));
                    }
                } else if (an.equalsIgnoreCase("popupSize")) {
                    try {
                        popupSize = Integer.parseInt(av);
                    } catch (NumberFormatException e) {
                        tagM.appendReplacement(sb, String.format(
                          "<!-- ERROR: invalid thumbnail popupSize \"%s\" -->",
                          av));
                    }
                }
            }

            // Replace the tag with the equivalent HTML.
            if (src == null) {
                tagM.appendReplacement(sb, String.format(
                    "<!-- ERROR: missing thumbnail src -->"));
            } else {
                tagM.appendReplacement(sb,
                  renderTag(new File(src), thumbSize, popupSize, alt, blog));
            }
        }
        tagM.appendTail(sb);
        return sb.toString();
    }

    /**
     * Render a thumbnail tag into the equivalent HTML, also creating and
     * caching the thumbnail as appropriate.
     * @param src image source.
     * @param thumbSize required thumbmail size, maximum dimension.
     * @param popupSize required popup size, maximum dimension.
     * @param alt alternate text for link.
     * @param blog the current blog.
     * @return the equivalent HTML.
     */
    private String renderTag(File src, int thumbSize, int popupSize,
      String alt, Blog blog) {

        // Check the image exists.
        File img = new File(blog.getRoot(), src.getPath());
        if (! img.isFile()) {
            return String.format(
              "<!-- ERROR: invalid thumbnail src \"%s\" -->", src);
        }

        // Check the thumbnail directory exists.
        File thumbDir = new File(img.getParent(), "thumbnails");
        if (!thumbDir.isDirectory()) {
            if (!thumbDir.mkdir()) {
                return String.format(
                  "<!-- ERROR: can't create thumbnail directory -->",
                  thumbDir.toString());
            }
        }

        // Check that an up-to-date thumbnail exists.
        File thumb = new File(thumbDir, img.getName());
        if (!thumb.isFile() || thumb.lastModified() < img.lastModified()) {
            if (! createThumbnail(img, thumb, thumbSize)) {
                return String.format(
                  "<!-- ERROR: can't create thumbnail \"%s\" -->", thumb);
            }
        }

        // Build the replacement HTML & return it.
        File tsrc = new File(new File(src.getParent(), "thumbnails"),
          src.getName());
        return String.format("<a href=\"%1$s\" onclick=\"window.open(" +
          "'%1$s','popup','width=%4$d,height=%4$d,toolbar=no,directories=no," +
          "location=no,menubar=no,status=no'); return false\" " +
          "class=\"thumbnailLink\"><img src=\"%2$s\" alt=\"%3$s\" " +
          "class=\"thumbnailImage\"/></a>",
          src, tsrc, alt, popupSize);
    }

    /**
     * Create a new thumbnail from an image.
     * @param imgFile the image to thumbnail.
     * @param thumbFile the thumbnail to create.
     * @param thumbSize the maximum dimension of the thumbnail.
     * @return true if the thumbnail was created successfully.
     */
    public boolean createThumbnail(File imgFile, File thumbFile,
      int thumbSize) {
        // Work out the image file suffix.
        String suffix = imgFile.getName();
        int dot = suffix.lastIndexOf('.');
        if (dot < 1) {
            return false;
        }
        suffix = suffix.substring(dot + 1);

        try {
            // Read in the image.
            BufferedImage img = ImageIO.read(imgFile);
            if (img == null) {
                return false;
            }

            // Calcuate the scaling.
            int ih = img.getHeight();
            int iw = img.getWidth();
            int thumbH, thumbW;
            float scale;
            if (iw > ih) {
                scale = (float) thumbSize / (float) iw;
                thumbW = thumbSize;
                thumbH = Math.round(ih * scale);
            } else {
                scale = (float) thumbSize / (float) ih;
                thumbH = thumbSize;
                thumbW = Math.round(iw * scale);
            }

            // Scale the image.
            BufferedImage thumb =
              new BufferedImage(thumbW, thumbH, img.getType());
            AffineTransform at =
              AffineTransform.getScaleInstance(scale, scale);
            AffineTransformOp ato = new AffineTransformOp(at, renderHints);
            ato.filter(img, thumb);

            // Save the image.
            Iterator<ImageWriter> iwi =
              ImageIO.getImageWritersBySuffix(suffix);
            if (! iwi.hasNext()) {
                return false;
            }
            ImageWriter thumbWriter = iwi.next();
            ImageWriteParam iwp = thumbWriter.getDefaultWriteParam();
            if (iwp.canWriteCompressed()) {
                String ct[] = iwp.getCompressionTypes();
                if (ct != null) {
                    iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    iwp.setCompressionType(ct[0]);
                    iwp.setCompressionQuality(0.8F);
                }
            }
            FileImageOutputStream thumbOut =
              new FileImageOutputStream(thumbFile);
            thumbWriter.setOutput(thumbOut);
            thumbWriter.write(null, new IIOImage(thumb, null, null), iwp);
            thumbWriter.dispose();
            thumbOut.close();
            
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    /** Default thumbnail size property name. */
    private static final String thumbSizeProp = "Thumbnail.thumbnailSize";
    /** Default popup window size property name. */
    private static final String popupSizeProp = "Thumbnail.popupSize";
    /** RE for matching thumbnail tags. */
    private static Pattern tagRE = Pattern.compile(
      "<thumbnail\\s+(.+?)\\s*/>",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    /** RE for matching tag attributes. */
    private static Pattern attrRE = Pattern.compile(
      "([\\w_-]+)\\s*=\\s*\"([^\"]+)\"",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    /** Image transform hints. */
    private static final RenderingHints renderHints;

    // Initialise rendering hints.
    static {
        Map<RenderingHints.Key, Object> hintMap =
          new HashMap<RenderingHints.Key, Object>();
        hintMap.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
          RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hintMap.put(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
        hintMap.put(RenderingHints.KEY_COLOR_RENDERING,
          RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hintMap.put(RenderingHints.KEY_DITHERING,
          RenderingHints.VALUE_DITHER_DISABLE);
        hintMap.put(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hintMap.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
          RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hintMap.put(RenderingHints.KEY_COLOR_RENDERING,
          RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        renderHints = new RenderingHints(hintMap);
    }
}
