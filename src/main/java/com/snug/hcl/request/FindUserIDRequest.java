package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindUserIDRequest {
	
	public static final Logger log = LoggerFactory.getLogger(FindUserIDRequest.class);
	
	public HashMap<String, String> findUserIDRequestURL(String requestURL, String email){
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getfindUserIDURL="";
		try {
			getfindUserIDURL = requestURL.replace("{email}", email);
		}catch (Exception e) {
			log.error("Error while find User ID "+e);
			getfindUserIDURL = "ERROR";
		}
		requestmap.put("REQUEST_URL", getfindUserIDURL);
		log.info("Final validateUserRequestURL: "+getfindUserIDURL);
		return requestmap;
	}

}