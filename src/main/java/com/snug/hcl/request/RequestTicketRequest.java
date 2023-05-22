package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestTicketRequest {
public static final Logger log = LoggerFactory.getLogger(RequestTicketRequest.class);
	
	public HashMap<String, String> RequestTicketRequestURL(String requestURL, String request){
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getRequestTicketRequest="";
		try {
			getRequestTicketRequest = requestURL.replace("{request}", request);
		}catch (Exception e) {
			log.error("Error in fetching request ticket staatus "+e);
			getRequestTicketRequest = "ERROR";
		}
		requestmap.put("REQUEST_URL", getRequestTicketRequest);
		log.info("Final requestTicketRequestURL: "+getRequestTicketRequest);
		return requestmap;
	}

}
