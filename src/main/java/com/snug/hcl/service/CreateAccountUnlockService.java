package com.snug.hcl.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.snug.hcl.parser.CreateAccountUnlockRequestParser;

import com.snug.hcl.request.CreateAccountUnlockRequest;
import com.snug.hcl.utility.APIRequestUtility;

@Service
public class CreateAccountUnlockService {
	public static final Logger log = LoggerFactory.getLogger(CreateAccountUnlockService.class);

	@Value("${api.accountUnlock.url}")
	private String apiAccountUnlockUrl;

	@Value("${api.accountUnlock.body}")
	private String apiAccountUnlockBody;

	@Autowired
	private APIRequestUtility apiRequestUtility;

	public HashMap<String, Object> createAccountUnlockService(String email) {
		log.debug("*******inside the CreateAccountUnlockService method**********");
		HashMap<String, Object> responsemap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestmap = new CreateAccountUnlockRequest()
					.createAccountUnlockTicket(apiAccountUnlockUrl, apiAccountUnlockBody, email);
			log.debug("Request Map --->" + requestmap);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating  request.");
			} else {
				HashMap<String, String> respMap = apiRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "POST",
						requestmap.get("REQUEST_BODY"));
				log.debug("respMap --->" + respMap);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("201")) {

					String responseBody = respMap.get("RESPONSEBODY").toString();
					log.debug("raw response--->" + responseBody);
					HashMap<String, Object> data = CreateAccountUnlockRequestParser.createAccountUnlockRequetParser(responseBody);
					responsemap.put("data", data);
				}
			}
		} catch (Exception e) {
			log.error("Error during create account unlock ticket!", e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());
		}
		log.info("Final API Response: " + responsemap);
		log.debug("*******outside the CreateAccountUnlockService method**********");
		return responsemap;
	}
}
