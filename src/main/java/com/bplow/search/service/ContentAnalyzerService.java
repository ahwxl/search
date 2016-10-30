/**
 * www.bplow.com
 */
package com.bplow.search.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @desc  抽取超文本编辑语言中的纯文本内容，不含标签
 * @author wangxiaolei
 * @date 2016年10月29日 下午5:23:39
 */
@Service
public class ContentAnalyzerService {

	public String getHtmlContent(byte[] cnt) throws IOException, SAXException, TikaException {
		
		if(null == cnt || cnt.length < 1){
			return "";
		}

		InputStream input = new ByteArrayInputStream(cnt);
		ContentHandler text = new BodyContentHandler();// <co
		LinkContentHandler links = new LinkContentHandler();// <co
		ContentHandler handler = new TeeContentHandler(links, text);// <co
		Metadata metadata = new Metadata();// <co id="html.store"/>
		Parser parser = new HtmlParser();// <co id="html.parser"/>
		ParseContext context = new ParseContext();
		parser.parse(input, handler, metadata, context);// <co id="html.parse"/>
		//System.out.println("Title: " + metadata.get(Metadata.TITLE));
		//System.out.println("Body: " + text.toString());
		//System.out.println("Links: " + links.getLinks());
		String bodycnt = text.toString();
		if(null != bodycnt){
			bodycnt.replace("\n", "").replace("\t", "");
		}
		return bodycnt;
		
	}
	
	public void getPdf() throws IOException, SAXException, TikaException{
		InputStream input = new FileInputStream(new File("src/test/resources/pdfBox-sample.pdf")); 
	    ContentHandler textHandler = new BodyContentHandler();
	    Metadata metadata = new Metadata();
	    Parser parser = new AutoDetectParser();
	    ParseContext context = new ParseContext(); 
	    parser.parse(input, textHandler, metadata, context);
	    System.out.println("Title: " + metadata.get(Metadata.TITLE));
	    System.out.println("Body: " + textHandler.toString());
	}

}
