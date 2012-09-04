package uk.co.fues.submission.trainer.corpuscrawl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl.DeliciousAPI;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl.DiigoAPI;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl.GoogleAPI;
import uk.co.fues.submission.trainer.parse.DataCleansing;
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
	
	public void collectTrainingData() {
		for(Sins sin_:Sins.values()) {
			List<String> sins = sin_.getVocab();
			for(String sin:sins) {
			List<String> urls = new ArrayList<String>();
			delicious.getUrl(sin, urls);
			diigo.getUrl(sin, urls);
			//google.getUrl(sin.name(), urls);
			for(String url:urls) {
				if(checkUrls(url)) {
				String data = parser.parseURL(url);
				String cleansed = DataCleansing.clean(data);
				if(DataCleansing.include(cleansed)) {
					File file = new File(Directories.SIN_DIRECTORY.getLocation() + "/" + sin_.name() + "/" + createFileNameFromURL(url));
					try {
					FileUtils.writeStringToFile(file, cleansed);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			}
			}
		}
	}
	//make mmini page for each dic/th

	List<String> urls = Arrays.asList("livejournal", "youtube", "fanfic");
	private boolean checkUrls(String url) {
		System.out.println();
		for(String _url:urls) {
			if(url.contains(_url)) {
				return false;
			}
		}
		return true;
	}	
	private String createFileNameFromURL(String url) {
		url = url.replaceAll("/", "");
		url = url.replaceAll("http:", "");
		url = url.replaceAll("\\.", "");
		url = url + ".txt";
		return url;
	}
	

}
