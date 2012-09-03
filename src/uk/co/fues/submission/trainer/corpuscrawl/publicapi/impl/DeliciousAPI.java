package uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.fues.submission.trainer.corpuscrawl.publicapi.JSONApi;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.TagAPI;


public class DeliciousAPI extends JSONApi implements TagAPI {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void getUrl(String tag, List<String> urls) {
		
		String call = "http://feeds.delicious.com/v2/json/tag/" + tag + "?count=1000";
		String json = getJSON(call);
        getResult(json, "$..u", urls);
		
	}
	
	public void getAssociatedTags() {
		
	}


}
