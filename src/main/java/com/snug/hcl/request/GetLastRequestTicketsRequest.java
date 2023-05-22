package com.snug.hcl.request;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GetLastRequestTicketsRequest {
public static final Logger log = LoggerFactory.getLogger(GetLastRequestTicketsRequest.class);
	
	public HashMap<String, String> getLastRequestTicketsRequestURL(String requestURL, String empId){
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getLastRequestsURL="";
		try {
			getLastRequestsURL = requestURL.replace("{empId}", empId);
		}catch (Exception e) {
			log.error("Error while getLastRequestsURL "+e);
			getLastRequestsURL = "ERROR";
		}
		requestmap.put("REQUEST_URL", getLastRequestsURL);
		log.info("Final getLastRequestsURL: "+getLastRequestsURL);
		return requestmap;
	}
}
