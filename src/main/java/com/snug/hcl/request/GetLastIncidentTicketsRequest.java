package com.snug.hcl.request;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GetLastIncidentTicketsRequest {
public static final Logger log = LoggerFactory.getLogger(GetLastIncidentTicketsRequest.class);
	
	public HashMap<String, String>getLastIncidentTicketsRequestURL(String requestURL, String empId){
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getLastIncidentsURL="";
		try {
			getLastIncidentsURL = requestURL.replace("{empId}", empId);
		}catch (Exception e) {
			log.error("Error while getLastIncidentsURL  "+e);
			getLastIncidentsURL = "ERROR";
		}
		requestmap.put("REQUEST_URL", getLastIncidentsURL);
		log.info("Final getLastIncidentsURL: "+getLastIncidentsURL);
		return requestmap;
	}
}
