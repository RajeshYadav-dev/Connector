package com.snug.hcl.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.snug.hcl.parser.GetEmpIdParser;
import com.snug.hcl.request.ValidateUserRequest;
import com.snug.hcl.utility.APIRequestUtility;

@Service
public class APIRequestService {

	public static final Logger log = LoggerFactory.getLogger(APIRequestService.class);

	@Value("${api.validateUser.url}")
	private String apiValidateUserUrl;
	
	@Autowired
	private APIRequestUtility apiRequestUtility;
	
	public HashMap<String, Object> validateUser(String empId) {
		log.debug("*******inside the APIRequestService method**********");
		
		HashMap<String, Object> responsemap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestmap = new ValidateUserRequest().validateUserRequestURL(apiValidateUserUrl, empId);
			log.debug("Request Map --->" +requestmap);
			
			if(requestmap.get("REQUEST_URL").equals("ERROR")){
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating validateUser request."); 
			}else {
				HashMap<String, String> respMap = apiRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "GET", null);
				log.debug("respMap --->" +respMap);
 				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if(responsemap.get("statuscode").toString().equals("200")){
					String responseBody = respMap.get("RESPONSEBODY").toString();
					log.debug("raw response--->" +responseBody );
					
					HashMap<String, Object> data = GetEmpIdParser.getEmpIdParser(responseBody);					
					responsemap.put("data", data);
				}
			}
		} catch (Exception e) {
			log.error("Error during ValidateUserRequest..!",e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());
		}
		log.info("Final API Response: "+responsemap);
		log.debug("*******outside the APIRequestService**********");
		return responsemap;
	}
}
