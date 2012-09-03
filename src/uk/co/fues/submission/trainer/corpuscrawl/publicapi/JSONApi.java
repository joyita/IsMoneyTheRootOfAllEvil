package uk.co.fues.submission.trainer.corpuscrawl.publicapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;

public class JSONApi {

	Logger log = LoggerFactory.getLogger(getClass());

	public String getJSON(String call) {
		URL url;
		try {
			url = new URL(call);
	        String inputLine;
	        String json = "";
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(url.openStream()));
	        while ((inputLine = in.readLine()) != null) {
	            System.out.println(inputLine);
	            json = json + inputLine;
	        }
	        in.close();
	        return json;

		} catch (MalformedURLException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void getResult(String json, String path, List<String> urls) {
	        urls.addAll((Collection<? extends String>) JsonPath.read(json, path));
	}
}
