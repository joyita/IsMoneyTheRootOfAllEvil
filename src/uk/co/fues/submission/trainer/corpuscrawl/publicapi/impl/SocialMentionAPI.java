package uk.co.fues.submission.trainer.corpuscrawl.publicapi.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.fues.submission.trainer.corpuscrawl.publicapi.JSONApi;
import uk.co.fues.submission.trainer.corpuscrawl.publicapi.TagAPI;

public class SocialMentionAPI extends JSONApi implements TagAPI {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void getUrl(String tag, List<String> urls) {
		String call = "http://api2.socialmention.com/search?q=" + tag + "&t[]=blogs&t[]=bookmarks&f=json&lang=en";
		
		String json;
		try {
			json = getJSON(call);
			if(json!=null&&!json.equalsIgnoreCase("")) {
				getResult(dirtyJSONStrip(json), "link", urls);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		
	}
	
	// super ugly, not proud
	public String dirtyJSONStrip(String json) {
		Pattern pattern = Pattern.compile("^(.*?)\\[");
		Matcher matcher = pattern.matcher(json);
		String ret = "";
		if (matcher.find()) {
			ret = matcher.group(1);
		}
		String re = json.substring(ret.length(), json.length()-3);
		return StringUtils.unEscapeString(re);

	}
	
}
