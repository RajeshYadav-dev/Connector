package com.snug.hcl.request;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValidateTokenRequest {
	
	public static final Logger log = LoggerFactory.getLogger(ValidateTokenRequest.class);

	public HashMap<String, String> validateTokenRequestURL(String requestURL,String requestBody, String userId, String factorId, String passcode){
		
		log.info("input validateTokenRequestURL: "+requestURL);
		log.info("input validateTokenRequestBody: "+requestBody);
		
		log.info("input factorId: "+factorId);
		log.info("input userID: "+userId);
		
		HashMap<String, String> requestmap = new HashMap<String, String>();
		String getValidateTokenURL="";
		String getValidateTokenBody="";
		if (requestURL.indexOf("{userID}")!= 0)
		{
			log.info("{userID} index "+requestURL.indexOf("{userID}"));
		}
		try {
			requestURL = requestURL.replace("{userID}", userId);
			
			getValidateTokenURL = requestURL.replace("{factorID}", factorId);
			
			getValidateTokenBody = requestBody.replace("{passCode}", passcode);
			
			log.info("Final validateUserRequestURL: "+getValidateTokenURL);
			log.info("Final getValidateTokenBody: "+getValidateTokenBody);
		}catch (Exception e) {
			
			log.error("Error while validate Token request "+e);
			getValidateTokenURL = "ERROR";
			getValidateTokenBody = "ERROR";
		}
		requestmap.put("REQUEST_URL", getValidateTokenURL);
		requestmap.put("REQUEST_BODY", getValidateTokenBody);
		log.info("Final getValidateTokenURL: "+getValidateTokenURL);
		log.info("Final getValidateTokenBody: "+getValidateTokenBody);
		return requestmap;
	}


}