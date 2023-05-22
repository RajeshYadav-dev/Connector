package com.snug.hcl.service;

import java.util.HashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.snug.hcl.parser.RequestTicketParser;
import com.snug.hcl.request.RequestTicketRequest;
import com.snug.hcl.utility.APIRequestUtility;

@Service
public class RequestTicketService {
	public static final Logger log = LoggerFactory.getLogger(RequestTicketService.class);

	@Value("${api.requestStatus.url}")
	private String apiRequestStatusUrl;

	@Autowired
	private APIRequestUtility apiRequestUtility;

	public HashMap<String, Object> requestTicketStatus(String request) {
		log.debug("*******inside the RequestTicketService  method**********");
		HashMap<String,Object> responsemap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestmap = new RequestTicketRequest().RequestTicketRequestURL(apiRequestStatusUrl,
					request);
			log.debug("Request Map --->" + requestmap);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating  request.");
			} else {
				HashMap<String, String> respMap = apiRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "GET",
						null);
				log.debug("respMap --->" + respMap);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("200")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();
					log.debug("raw response--->" + responseBody);

					HashMap<String, Object> data = RequestTicketParser.requestTicketParser(responseBody);
					responsemap.put("data", data);
				}
			}

		} catch (Exception e) {
			log.error("Error during request ticket status.!", e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());
		}
		log.info("Final API Response: " + responsemap);
		log.debug("*******outside the RequestTicketService  method**********");
		return responsemap;
	}

}
