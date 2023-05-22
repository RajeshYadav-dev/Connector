package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAccountUnlockRequest {
public static final Logger log = LoggerFactory.getLogger(CreateAccountUnlockRequest.class);
	
	public HashMap<String, String> createAccountUnlockTicket(String requestURL,String requestBody,String email){
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String AccountUnlockTicketBody="";
		try {
			AccountUnlockTicketBody = requestBody.replace("{email}", email);
		}catch (Exception e) {
			log.error("Error in fetching AccountUnlockTicketBody status "+e);
			AccountUnlockTicketBody = "ERROR";
		}
		requestmap.put("REQUEST_URL", requestURL);
		requestmap.put("REQUEST_BODY", AccountUnlockTicketBody);
		log.info("Final AccountUnlockTicketURL: "+requestURL);
		log.info("Final AccountUnlockTicketBody: "+requestBody);
		return requestmap;
	}


}
