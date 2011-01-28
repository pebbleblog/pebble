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
package net.sourceforge.pebble.web.view;

import java.io.ByteArrayInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FilenameFilter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.listener.PebblePDFCreationListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.TrueTypeUtil;


/**
 * Represents a binary view component and prepares the model for display.
 *
 * @author    Alexander Zagniotov
 */
public class PdfView extends BinaryView {


  private static Log log = LogFactory.getLog(PdfView.class);

  private static final String SEP = "/";
  private static final String FONTS_PATH = "fonts";
  private static final String THEMES_PATH = "themes";
  private static final String DEFAULT_ENCODING = "UTF-8";
  private static final String PDF_CSS = "pdf.css";
  private static final String SYSTEM_THEME_PATH = HtmlView.SYSTEM_THEME;

  private String filename = "default.pdf";
  private BlogEntry entry;
  private long length = 0;

  public PdfView(BlogEntry entry, String filename) {
	this.entry = entry;
    this.filename = filename;
  }

  /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
  public String getContentType() {
    return "application/pdf";
  }

  public long getContentLength() {
    return length;
  }

  /**
   * Dispatches this view.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @param context
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException {

			try {
				ITextRenderer renderer = new ITextRenderer();

				//This will be an attachment
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				String author = entry.getUser().getName();
				String title = entry.getTitle();
				String subtitle = entry.getSubtitle();
				String body = entry.getBody();
				String blogName = entry.getBlog().getName();
				String entryPermalink = entry.getPermalink();
				String entryDescription = entry.getBlog().getDescription();

				//Some of the HTML entities need to be escaped to Unicode notation \\uXXXX for XHTML markup to validate
				title = StringUtils.transformHTML(title);
				subtitle = StringUtils.transformHTML(subtitle);
				body = StringUtils.unescapeHTMLEntities(body);
				
				//Build absolute path to: <pebble_root>/themes/_pebble/fonts/ 
				String webApplicationRoot = PebbleContext.getInstance().getWebApplicationRoot() + SEP + THEMES_PATH;

				//<pebble_root> + / + themes + / + _pebble + / + fonts 
				String fontDirAbsolutePath = webApplicationRoot + SEP + SYSTEM_THEME_PATH + SEP + FONTS_PATH;
				File fontDir = new File(fontDirAbsolutePath);


				//Get blog entry tags for PDF metadata 'keywords'
				StringBuffer tags = new StringBuffer();
				Iterator<Tag> currentEntryTags = entry.getAllTags().iterator();
				
				//Build a string out of blog entry tags and seperate them by comma
				while (currentEntryTags.hasNext()) {
					Tag currentTag = currentEntryTags.next();
					if (currentTag.getName() != null && !currentTag.getName().equals("")) {
						 tags.append(currentTag.getName());
						 if (currentEntryTags.hasNext()) {
							tags.append(",");
						}
					}
				}

				//Build valid XHTML source from blog entry for parsing 
				StringBuffer buf = new StringBuffer();
				buf.append("<html>");
				buf.append("<head>");
				buf.append("<meta name=\"title\" content=\"" + title + " - " + blogName + "\"/>");
				buf.append("<meta name=\"subject\" content=\"" + title + "\"/>");
				buf.append("<meta name=\"keywords\" content=\"" + tags.toString().trim() + "\"/>");
				buf.append("<meta name=\"author\" content=\"" + author + "\"/>");
				buf.append("<meta name=\"creator\" content=\"Pebble (by pebble.sourceforge.net)\"/>");
				buf.append("<meta name=\"producer\" content=\"Flying Saucer (by xhtmlrenderer.dev.java.net)\"/>");
				buf.append("<link rel='stylesheet' type='text/css' href='" + entry.getBlog().getUrl() + 
																			THEMES_PATH + SEP + 
																			SYSTEM_THEME_PATH + SEP + 
																			PDF_CSS + "' media='print' />");
				buf.append("</head>");
				buf.append("<body>");
				buf.append("<div id=\"header\" style=\"\">" + blogName + " - " + entryDescription + "</div>");
				buf.append("<p>");

				//Gets TTF or OTF font file from the font directory in the system theme folder
				 if (fontDir.isDirectory()) {
						File[] files = fontDir.listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								String lower = name.toLowerCase();

								//Load TTF or OTF files
								return lower.endsWith(".otf") || lower.endsWith(".ttf");
							}
						});
						
						if (files.length > 0) {
							String fontFamilyName = "";
							//You should always embed TrueType fonts.
							renderer.getFontResolver().addFont(files[0].getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
							log.info("Added font: " + files[0].getAbsolutePath());

							//Get font family name from the BaseFont object. All this work just to get font family name
							BaseFont font = BaseFont.createFont(files[0].getAbsolutePath(), BaseFont.IDENTITY_H , BaseFont.NOT_EMBEDDED);
							fontFamilyName = TrueTypeUtil.getFamilyName(font);
							
							if (!fontFamilyName.equals("")) {
								//Wrap DIV with font family name around the content of the blog entry
								author = "<div style=\"font-family: " + fontFamilyName + ";\">" + author + "</div>";
								title = "<div style=\"font-family: " + fontFamilyName + ";\">" + title + "</div>";
								subtitle = "<div style=\"font-family: " + fontFamilyName + ";\">" + subtitle + "</div>";
								body = "<div style=\"font-family: " + fontFamilyName + ";\">" + body + "</div>";
								log.info("PDFGenerator - Added font family: '" + fontFamilyName + "' to PDF content");
							}		
						}
				 }

				buf.append("<h1>" + title  + "</h1>");
				buf.append("<h2>" + subtitle + "</h2>");
				buf.append("</p>");
				buf.append("<p>" + body + "</p>");
				buf.append("<p><br /><br /><br />");
				buf.append("<i>Published by " + author + "</i><br />");
				buf.append("<i>" + entry.getDate().toString() + "</i><br />");
				buf.append("<i><a href=\"" + entryPermalink + "\" title=\"" + entryPermalink + "\">" + entryPermalink + "</a></i>");
				buf.append("</p>");
				buf.append("</body>");
				buf.append("</html>");

				byte[] bytes = buf.toString().getBytes(DEFAULT_ENCODING);

				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource(bais);
				Document doc = builder.parse(is);

				//Listener that will parse HTML header meta tags, and will set them to PDF document as meta data
				PebblePDFCreationListener pdfListener = new PebblePDFCreationListener();
				pdfListener.parseMetaTags(doc);
				renderer.setListener(pdfListener);
				renderer.setDocument(doc, null);
				renderer.layout();

				BufferedOutputStream bufferedOutput = new BufferedOutputStream(response.getOutputStream());
				
				renderer.createPDF(bufferedOutput);
				bufferedOutput.flush();
				bufferedOutput.close();

				log.info("Successfully generated PDF document: " + filename);
			}

			catch (ParserConfigurationException e)  {
				log.error("Could not create PDF, could not get new instance of DocumentBuilder: " + e);
			} 

			catch (SAXException e)  {
				log.error("Could not create PDF, could not parse InputSource: " + e);
			}

			catch (IOException e)  {
				log.error("Could not create PDF: " + e);
			}

			catch (DocumentException e)  {
				log.error("iText could not create PDF document: " + e);
			}

			catch (Exception e)  {
				log.error("Could not create PDF: " + e);
			}
		}
	}