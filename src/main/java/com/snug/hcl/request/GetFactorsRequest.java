package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GetFactorsRequest {
	
public static final Logger log = LoggerFactory.getLogger(GetFactorsRequest.class);
	
	public HashMap<String, String> findFactorsRequestURL(String requestURL, String userId){
		
		log.info("input findFactorsRequestURL: "+requestURL);
		
		log.info("input userId: "+userId);
		
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getfindFactorsURL="";
		try {
			getfindFactorsURL = requestURL.replace("{userID}", userId);
			log.info("Final validateUserRequestURL: "+getfindFactorsURL);
		}catch (Exception e) {
			log.error("Error while find factors "+e);
			getfindFactorsURL = "ERROR";
		}
		requestmap.put("REQUEST_URL", getfindFactorsURL);
		log.info("Final validateUserRequestURL: "+getfindFactorsURL);
		return requestmap;
	}

}
