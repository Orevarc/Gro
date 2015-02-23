package com.groteam4450.gro;

public class HttpResponseParser {

	public HttpResponseParser() {}
	
	public boolean parseLoginResponse(String response) {
		if (response.contains("Unauthorized")){
			return false;
		}
		else if (response.contains("Response code : 200")) {
			return true;
		}
		return false;
	}
}
