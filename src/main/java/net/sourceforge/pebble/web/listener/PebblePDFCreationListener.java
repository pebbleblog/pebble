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
package net.sourceforge.pebble.web.listener;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFCreationListener;

import java.util.Properties;
import java.util.Enumeration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfDictionary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Add ability to manipulate PDF output document before it's closed and opened.
 *  
 * Soltuion was adopted from: https://xhtmlrenderer.dev.java.net/servlets/ReadMsg?listName=users&msgNo=1908
 *
 * @author    Alexander Zagniotov
 */
public class PebblePDFCreationListener implements PDFCreationListener {

	/** the log used by this class */
	private static Log log = LogFactory.getLog(PebblePDFCreationListener.class);
	
	Properties headerTags = null;

	public PebblePDFCreationListener()  {
		headerTags = new Properties();
	}

	public void parseMetaTags(Document sourceXHTML) {
        
		try {

			NodeList e = sourceXHTML.getDocumentElement().getElementsByTagName("head");
			Element e1 = (Element) e.item(0);
			NodeList retVal = e1.getElementsByTagName("meta");

			for (int i = 0; i < retVal.getLength(); i++) {
					
					Element thisNode = (Element) retVal.item(i);
					String name = thisNode.getAttribute("name");
					String content = thisNode.getAttribute("content");
				 
					//<meta name="XXXXX" content="XXXXX" />
					if (name.length() != 0 && content.length() != 0) {
						headerTags.setProperty(name,content);
					}
				}
		}
    
		catch (Exception e)  {
				log.warn("Could not parse header meta tags: " + e);
		}
     }

  
    /**
	 * Called directly before calling open() on the Document. 
	 * That allows you to e.g. modify headers before the document is created.
	 *
	 */
	public void preOpen(ITextRenderer renderer) { 

		try {
			Enumeration e = headerTags.propertyNames();

			PdfWriter writer = renderer.getWriter(); 

			 if (writer == null) {
					 log.warn("PdfWriter is null, not able to set header meta tags to PDF document");
					 return;
			 }

			 /*
					Available versions:
					PdfWriter.VERSION_1_2,
					PdfWriter.VERSION_1_3,
					PdfWriter.VERSION_1_4,  //default
					PdfWriter.VERSION_1_5,
					PdfWriter.VERSION_1_6,
					PdfWriter.VERSION_1_7
				*/
			writer.setPdfVersion(PdfWriter.VERSION_1_5);

			//Full compression means that not only page streams are compressed, 
			//but some other objects as well, such as the cross-reference table. 
			//This is only possible since PDF-1.5
			writer.setFullCompression();

			while (e.hasMoreElements())  { 

				  String key = (String) e.nextElement() ;
				  PdfString val = new PdfString(headerTags.getProperty(key), PdfObject.TEXT_UNICODE);

				  PdfDictionary dictionary = writer.getInfo();
				  
				  if (key.equals("title" )) {
						dictionary.put(PdfName.TITLE, val);
				  }
				  else if (key.equals("author")) {
						dictionary.put(PdfName.AUTHOR, val);  
				  }
				  else if (key.equals("subject")) {
						dictionary.put(PdfName.SUBJECT, val);  
				  }
				  else if (key.equals("keywords")) {
						dictionary.put(PdfName.KEYWORDS, val);  
				  }
				  else if (key.equals("creator")) {
						dictionary.put(PdfName.CREATOR, val);  
				  }
				  else if (key.equals("producer")) {
						dictionary.put(PdfName.PRODUCER, val);  
				  }
				  else  {
						log.warn("Unexpected header meta tag: " + key + ", value: " + val);
				  }
				}
			}
			catch (Exception e)  {
				log.warn("Could not set header meta tags to PDF document: " + e);
			}
	}

    public void onClose(ITextRenderer renderer) { 

		PdfWriter writer = renderer.getWriter(); 

		if (writer == null) {
			 log.warn("PdfWriter is null, not able to set header meta tags to PDF document");
			 return;
		}

		//Display doc title in the title bar instead of filename,
		writer.setViewerPreferences(PdfWriter.DisplayDocTitle);

	}
}