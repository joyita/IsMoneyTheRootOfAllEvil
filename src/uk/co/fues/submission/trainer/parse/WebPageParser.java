package uk.co.fues.submission.trainer.parse;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


@Component
public class WebPageParser  {

	Logger logger = Logger.getLogger(getClass());

	public String parseURL(String url) {
		String content = "";
		try {
			URL _url = new URL(url);
			InputStream input = _url.openStream();
			LinkContentHandler linkHandler = new LinkContentHandler();
			ContentHandler textHandler = new BodyContentHandler();
			ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
			TeeContentHandler teeHandler = new TeeContentHandler(linkHandler,
					textHandler, toHTMLHandler);
			Metadata metadata = new Metadata();
			ParseContext parseContext = new ParseContext();
			HtmlParser parser = new HtmlParser();
			parser.parse(input, teeHandler, metadata, parseContext);

			content = (StringEscapeUtils.escapeHtml(textHandler.toString()));

			//String untagged = toHTMLHandler.toString();
			//String title = (metadata.get("title").replaceAll(".", "").replaceAll("/", ""));
			

		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (SAXException e) {
			logger.error(e);
		} catch (TikaException e) {
			logger.error(e);
		} 
		return content;
	}
}
