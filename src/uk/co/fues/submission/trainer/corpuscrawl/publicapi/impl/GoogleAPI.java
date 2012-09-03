package uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.fues.submission.trainer.corpuscrawl.publicapi.JSONApi;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.TagAPI;


public class GoogleAPI extends JSONApi implements TagAPI {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void getUrl(String tag, List<String> urls) {
		
		String call = "https://www.googleapis.com/customsearch/v1?\n" + 
				"key=AIzaSyBwvGX_aInnPhITMD4X3E63lgyYXgWZKTQ&cx=013036536707430787589:_pqjad5hr1a&q=" + tag + "&alt=json";
		String json = getJSON(call);
        getResult(json, "$..u", urls);
		
	}
	
	public void getAssociatedTags() {
		
	}


}
