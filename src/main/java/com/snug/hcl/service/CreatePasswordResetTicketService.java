package com.snug.hcl.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.snug.hcl.parser.CreatePasswordResetTicketParser;
import com.snug.hcl.request.CreatePasswordResetTicketRequest;
import com.snug.hcl.utility.APIRequestUtility;

@Service
public class CreatePasswordResetTicketService {

	public static final Logger log = LoggerFactory.getLogger(CreatePasswordResetTicketService.class);

	@Value("${api.passwordReset.url}")
	private String apiPasswordResetUrl;

	@Value("${api.passwordReset.body}")
	private String apiPasswordResetBody;

	@Autowired
	private APIRequestUtility apiRequestUtility;
	public HashMap<String, Object> createPasswordResetTicket(String email) {
		log.debug("*******inside the  CreatePasswordResetTicketService method**********");
		HashMap<String,  Object> responsemap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestmap = new CreatePasswordResetTicketRequest().createPasswordResetTicket(apiPasswordResetUrl,apiPasswordResetBody,email);
			log.debug("Request Map --->" +requestmap);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating  request.");
			} else {
				HashMap<String, String> respMap = apiRequestUtility.sendRequest(requestmap.get("REQUEST_URL"),"POST",requestmap.get("REQUEST_BODY"));
				log.debug("respMap --->" +respMap);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("201")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();
					log.debug("raw response--->" + responseBody);
					HashMap<String, Object> data = CreatePasswordResetTicketParser.createPasswordResetTicketParser(responseBody);
					responsemap.put("data", data);
				}
			}
		} catch (Exception e) {
			log.error("Error during create password reset ticket!", e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());
		}
		log.info("Final API Response: " + responsemap);
		log.debug("*******outside the CreatePasswordResetTicketService method**********");
		return responsemap;
	}
}
