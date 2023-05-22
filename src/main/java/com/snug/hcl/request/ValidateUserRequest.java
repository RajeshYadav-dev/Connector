package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateUserRequest {

	public static final Logger log = LoggerFactory.getLogger(ValidateUserRequest.class);
	
	public HashMap<String, String> validateUserRequestURL(String requestURL, String empId){
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getvalidateUserURL="";
		try {
			getvalidateUserURL = requestURL.replace("{empId}", empId);
		}catch (Exception e) {
			log.error("Error while validatating User"+e);
			getvalidateUserURL = "ERROR";
		}
		requestmap.put("REQUEST_URL", getvalidateUserURL);
		log.info("Final validateUserRequestURL: "+getvalidateUserURL);
		return requestmap;
	}
}
