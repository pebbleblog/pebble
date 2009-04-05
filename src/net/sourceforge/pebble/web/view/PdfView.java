/*
 * Copyright (c) 2003-2006, Simon Brown
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

import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.DocumentException;

import org.xhtmlrenderer.pdf.ITextRenderer;





/**
 * Represents a binary view component and prepares the model for display.
 *
 * @author    Alexander Zagniotov
 */
public class PdfView extends BinaryView {


  private static Log log = LogFactory.getLog(PdfView.class);

  private String filename;
  private BlogEntry entry;

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
    return 0;
  }

  /**
   * Dispatches this view.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @param context
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException {

			//this will be an attachment
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);

			String body = entry.getBody();

			//Some of the HTML entities need to be unescaped for XHTML markup to validate
			body = StringUtils.unescapeHTMLEntities(body);

			//org.xml.sax.SAXParseException: 
			//An invalid XML character (Unicode: 0x19) was found in the element content of the document
			body = body.replaceAll("&#25;", "");
			body = body.replaceAll("\u0019", ""); //yeah, lets do it again

			StringBuffer buf = new StringBuffer();
			StringBuffer tags = new StringBuffer();

			//Get blog entry tags and set them as PDF metadata 'keywords'
			Iterator<Tag> currentEntryTags = entry.getAllTags().iterator();

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
			buf.append("<html>");
			buf.append("<head>");
			buf.append("<meta name=\"title\" content=\"" + entry.getBlog().getName() + "\"/>");
			buf.append("<meta name=\"subject\" content=\"" + entry.getTitle() + "\"/>");
			buf.append("<meta name=\"keywords\" content=\"" + tags.toString().trim() + "\"/>");
			buf.append("<meta name=\"author\" content=\"" + entry.getUser().getName() + "\"/>");
			buf.append("<meta name=\"creator\" content=\"Pebble (by pebble.sourceforge.net)\"/>");
			buf.append("<meta name=\"producer\" content=\"Flying Saucer (by xhtmlrenderer.dev.java.net)\"/>");
			buf.append("<link rel='stylesheet' type='text/css' href='" + entry.getBlog().getUrl()+ "themes/" + HtmlView.SYSTEM_THEME + "/pdf.css' media='print' />");
			buf.append("</head>");
			buf.append("<body>");
			buf.append("<div id=\"header\" style=\"\">" + entry.getBlog().getName() + " - " + entry.getBlog().getDescription() + "</div>");
			buf.append("<p>");
			buf.append("<h1>" + entry.getTitle()  + "</h1>");
			buf.append("<h2>" + entry.getSubtitle() + "</h2>");
			buf.append("</p>");
			buf.append("<p>" + body + "</p>");
			buf.append("<p><br /><br /><br />");
			buf.append("<i>Published by " + entry.getUser().getName() + "</i><br />");
			buf.append("<i>" + entry.getDate().toString() + "</i><br />");
			buf.append("<i><a href=\"" + entry.getPermalink() + "\" title=\"" + entry.getPermalink() + "\">" + entry.getPermalink() + "</a></i>");
			buf.append("</p>");
			buf.append("</body>");
			buf.append("</html>");

			//A PdfString-class is the PDF-equivalent of a JAVA-String-object
			PdfString unicodeXHTML = new PdfString(buf.toString(), PdfObject.TEXT_UNICODE);

			try {
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource(new BufferedReader(new StringReader(unicodeXHTML.toUnicodeString())));
				Document doc = builder.parse(is);

				ITextRenderer renderer = new ITextRenderer();

				//listener that will parse header meta tags, and will set them to PDF document
				PebblePDFCreationListener pdfListener = new PebblePDFCreationListener();
				pdfListener.parseMetaTags(doc);
				renderer.setListener(pdfListener);
				
				/*
					Available versions:
					PdfWriter.VERSION_1_2,
					PdfWriter.VERSION_1_3,
					PdfWriter.VERSION_1_4,  //default
					PdfWriter.VERSION_1_5,
					PdfWriter.VERSION_1_6,
					PdfWriter.VERSION_1_7
				*/
				renderer.setPDFVersion(PdfWriter.VERSION_1_5);
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