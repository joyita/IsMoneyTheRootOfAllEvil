package uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.fues.submission.trainer.corpuscrawl.publicapi.JSONApi;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.TagAPI;

public class DiigoAPI  extends JSONApi implements TagAPI {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void getUrl(String tag, List<String> urls) {
		
		String call = "https://secure.diigo.com/api/v2/bookmarks?tags=" + tag + "&count=100";
		String json = getJSON(call);
	    getResult(json, "$..url", urls);    
	}
}
