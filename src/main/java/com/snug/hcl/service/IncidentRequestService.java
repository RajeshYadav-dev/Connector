package com.snug.hcl.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.snug.hcl.parser.IncidentRequestParser;
import com.snug.hcl.request.IncidentTicketRequest;

import com.snug.hcl.utility.APIRequestUtility;

@Service
public class IncidentRequestService {
	public static final Logger log = LoggerFactory.getLogger(IncidentRequestService.class);

	@Value("${api.incidentStatus.url}")
	private String apiIncidentStatusUrl;

	@Autowired
	private APIRequestUtility apiRequestUtility;

	public HashMap<String, Object> incidentTicketStatus(String incident) {
		log.debug("*******inside the IncidentRequestService method**********");
		HashMap<String,  Object> responsemap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestmap = new IncidentTicketRequest().IncidentTicketRequesttURL(apiIncidentStatusUrl, incident);
			log.debug("Request Map --->" +requestmap);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating  request.");
			} else {
				HashMap<String, String> respMap = apiRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "GET",
						null);
				log.debug("respMap --->" +respMap);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("200")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();
					log.debug("raw response--->" +responseBody );
					HashMap<String, Object> data = IncidentRequestParser.incidentRequestParser(responseBody);
					responsemap.put("data", data);
				}
			}
				
		
		} catch (Exception e) {
			log.error("Error during incident ticket status.!", e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());
		}
		log.info("Final API Response: " + responsemap);
		log.debug("*******outside the IncidentRequestService method**********");
		return responsemap;
	}

}
