package com.groteam4450.gro;

import org.apache.http.Header;

import com.loopj.android.http.*;

public class HTTPRestClient {

	private static final String baseURL = "http://groappvm.cloudapp.net:1336/";
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void postAuth(String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.post(null, getAbsoluteUrl(url), headers, params, null, responseHandler);
	  }
	  
	  public static void getAuth(String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.get(null, getAbsoluteUrl(url), headers, params, responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return baseURL + relativeUrl;
	  }
}
