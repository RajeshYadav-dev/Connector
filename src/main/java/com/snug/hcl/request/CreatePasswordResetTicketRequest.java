
package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePasswordResetTicketRequest {
public static final Logger log = LoggerFactory.getLogger(CreatePasswordResetTicketRequest.class);
	
	public HashMap<String, String> createPasswordResetTicket(String requestURL,String requestBody, String email){
		HashMap<String, String> requestmap = new HashMap<String, String>();	
		String getPasswordResetTicketBody="";
		try {
			getPasswordResetTicketBody = requestBody.replace("{email}", email);
		}catch (Exception e) {
			log.error("Error in fetching getPasswordResetTicketBody status "+e);
			getPasswordResetTicketBody = "ERROR";
		}
		requestmap.put("REQUEST_URL", requestURL);
		requestmap.put("REQUEST_BODY", getPasswordResetTicketBody);
		log.info("Final getPasswordResetTicketURL: "+requestURL);
		log.info("Final getPasswordResetTicketBody: "+getPasswordResetTicketBody);
		return requestmap;
	}

}
