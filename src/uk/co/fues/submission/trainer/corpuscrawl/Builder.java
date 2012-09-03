package uk.co.fues.submission.trainer.corpuscrawl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl.DeliciousAPI;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl.DiigoAPI;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl.GoogleAPI;
import uk.co.fues.submission.trainer.parse.WebPageParser;
import uk.co.fues.submission.util.constants.Directories;
import uk.co.fues.submission.vocabulary.Sins;

public class Builder {

	@Autowired
	DeliciousAPI delicious;
	
	@Autowired
	DiigoAPI diigo;
	
	@Autowired
	GoogleAPI google;
	
	@Autowired
	WebPageParser parser;
	
	public void collectTrainingData() throws IOException {
		for(Sins sin:Sins.values()) {
			List<String> urls = new ArrayList<String>();
			delicious.getUrl(sin.name(), urls);
			diigo.getUrl(sin.name(), urls);
			google.getUrl(sin.name(), urls);
			for(String url:urls) {
				String data = parser.parseURL(url);
				File file = new File(Directories.SIN_DIRECTORY.getLocation() + "/" + sin.name() + "/" + createFileNameFromURL(url));
				FileUtils.writeStringToFile(file, data);
			}
		}
	}
	
	private String createFileNameFromURL(String url) {
		url = url.replaceAll("/", "");
		url = url.replaceAll("http:", "");
		url = url.replaceAll("\\.", "");
		url = url + ".txt";
		return url;
	}
}
