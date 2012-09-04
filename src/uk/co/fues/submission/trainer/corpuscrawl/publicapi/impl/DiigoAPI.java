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
		
		String call = "secure.diigo.com";
		String address = "/api/v2/bookmarks?tags=" + tag + "&count=100";
		String username = "*";
		String password = "*";
		String json = getJSON(call, address, username, password);
	    getResult(json, "$..url", urls);    
	}
}
