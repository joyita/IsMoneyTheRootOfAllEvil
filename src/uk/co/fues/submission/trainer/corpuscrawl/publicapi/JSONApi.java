package uk.co.fues.submission.trainer.corpuscrawl.publicapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;

public class JSONApi {

	Logger log = LoggerFactory.getLogger(getClass());

	public String getJSON(String host, String address, String username,
			String password) {

		String json = "";
		HttpHost targetHost = new HttpHost(host, -1, "https");

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(),
							targetHost.getPort()),
					new UsernamePasswordCredentials("joyita", "krishna"));
			// Create AuthCache instance
			setParams(httpclient);
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			// Add AuthCache to the execution context
			BasicHttpContext localcontext = new BasicHttpContext();
			localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

			HttpGet httpget = new HttpGet(address);
			log.debug("executing request: " + httpget.getRequestLine());
			log.debug("to target: " + targetHost);
			String inputLine;
			for (int i = 0; i < 3; i++) {
				HttpResponse response = httpclient.execute(targetHost, httpget,
						localcontext);
				HttpEntity entity = response.getEntity();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						(InputStream) entity.getContent()));
				while ((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);
					json = json + inputLine;
				}
				in.close();
			}

		} catch (IOException e) {
			log.error("", e);
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
		return json;
	}

	public String getJSON(String address) {

		String json = "";

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {

			String inputLine;
			for (int i = 0; i < 3; i++) {
				setParams(httpclient);
				HttpGet request = new HttpGet(address);
				HttpResponse response = httpclient.execute(request);
				HttpEntity entity = response.getEntity();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						(InputStream) entity.getContent()));
				while ((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);
					json = json + inputLine;
				}
				in.close();
			}

		} catch (IOException e) {
			log.error("", e);
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
		return json;
	}
	
	private void setParams(DefaultHttpClient httpclient) {
		HttpParams params = new BasicHttpParams();
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
		params.setParameter(ClientPNames.MAX_REDIRECTS, 1);
		HttpClientParams.setRedirecting(params, false);
		
		httpclient.setParams(params);
		httpclient.setRedirectHandler((new RedirectHandler() {

			@Override
			public java.net.URI getLocationURI(HttpResponse arg0,
					HttpContext arg1) throws ProtocolException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isRedirectRequested(HttpResponse arg0,
					HttpContext arg1) {
				// TODO Auto-generated method stub
				return false;
			}
	    }));
	}

	@SuppressWarnings("unchecked")
	public void getResult(String json, String path, List<String> urls) {
		urls.addAll((Collection<? extends String>) JsonPath.read(json, path));
	}
}
