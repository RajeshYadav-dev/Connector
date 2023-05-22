package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncidentTicketRequest {
public static final Logger log = LoggerFactory.getLogger(IncidentTicketRequest.class);
	
	public HashMap<String, String> IncidentTicketRequesttURL(String requestURL, String incident){
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getIncidentTicketRequest="";
		try {
			getIncidentTicketRequest = requestURL.replace("{incident}", incident);
		}catch (Exception e) {
			log.error("Error in fetching incident ticket status "+e);
			getIncidentTicketRequest = "ERROR";
		}
		requestmap.put("REQUEST_URL", getIncidentTicketRequest);
		log.info("Final IncidentTicketRequestURL: "+getIncidentTicketRequest);
		return requestmap;
	}

}
