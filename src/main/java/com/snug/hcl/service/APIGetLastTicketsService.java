package com.snug.hcl.service;


import java.util.HashMap;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.snug.hcl.parser.GetLastThreeTicketsParser;
import com.snug.hcl.request.GetLastIncidentTicketsRequest;
import com.snug.hcl.request.GetLastRequestTicketsRequest;
import com.snug.hcl.utility.APIRequestUtility;

@Service
public class APIGetLastTicketsService {
	public static final Logger log = LoggerFactory.getLogger(APIGetLastTicketsService.class);

	@Value("${api.lastincident.url}")
	private String apiLastincidentUrl;

	@Value("${api.lastrequest.url}")
	private String apiLastrequestUrl;

	@Autowired
	private APIRequestUtility apiRequestUtility;

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getLastThreeTickets(String empId) {
		HashMap<String, Object> responsemap = new HashMap<String, Object>();
		HashMap<String, String> responsemapINC = new HashMap<String, String>();
		HashMap<String, String> responsemapREQ = new HashMap<String, String>();

		JSONArray arrayTickets = new JSONArray();
		JSONArray arrayREQ = new JSONArray();

		JSONObject jsonObjectTickets = new JSONObject();
		JSONObject jsonObjectSortedTickets = new JSONObject();
		
		String responseBodyINC="";
		String responseBodyREQ="";

		try {
			responsemapINC = getLastIncidents(empId);

			responsemapREQ = getLastRequests(empId);

			if (responsemapINC.get("statuscode").toString().equals("200")
					&& null != (responsemapINC.get("INCIDENTS"))) {
				responseBodyINC = responsemapINC.get("INCIDENTS").toString();
				log.info("INCIDENTS Tickets..! : ", responseBodyINC);
			}

			if (responsemapREQ.get("statuscode").toString().equals("200") && null != (responsemapREQ.get("REQUESTS"))) {
				responseBodyREQ = responsemapREQ.get("REQUESTS").toString();
				log.info("REQUESTS Tickets..! : ", responseBodyREQ);
			}
			
			responsemap = GetLastThreeTicketsParser.getLastTicketsResponse(responseBodyINC, responseBodyREQ);
				

		} catch (Exception e) {
			log.error("Error during getLastThreeTickets..!", e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());
		}
		return responsemap;
	}

	public HashMap<String, String> getLastIncidents(String empId) {
		HashMap<String, String> responsemap = new HashMap<String, String>();

		try {
			HashMap<String, String> requestmap = new GetLastIncidentTicketsRequest()
					.getLastIncidentTicketsRequestURL(apiLastincidentUrl, empId);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating apiLastincidentUrl request.");
			} else {
				HashMap<String, String> respMap = apiRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "GET",
						null);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("200")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();
					org.json.JSONObject jsonObject = new org.json.JSONObject(responseBody);
					JSONArray jArray = new JSONArray(jsonObject.getJSONArray("result"));
					if (null != jArray && !jArray.isEmpty()) {

						responsemap.put("INCIDENTS", responseBody);
						responsemap.put("status", "200");
						responsemap.put("statuscode", "200");
						responsemap.put("statusdesc", "OK");
						

					} else {
						responsemap.put("status", "404");
						responsemap.put("statusdesc", "No Tickets");

					}
				}
			}
		} catch (Exception e) {
			log.error("Error during getLastIncidents..!", e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());

		}
		log.info("Final API Response: " + responsemap);
		return responsemap;
	}

	public HashMap<String, String> getLastRequests(String empId) {
		HashMap<String, String> responsemap = new HashMap<String, String>();

		try {
			HashMap<String, String> requestmap = new GetLastRequestTicketsRequest()
					.getLastRequestTicketsRequestURL(apiLastrequestUrl, empId);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating apiLastincidentUrl request.");
			} else {
				HashMap<String, String> respMap = apiRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "GET",
						null);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("200")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();
					org.json.JSONObject jsonObject = new org.json.JSONObject(responseBody);
					JSONArray jArray = new JSONArray(jsonObject.getJSONArray("result"));
					if (null != jArray && !jArray.isEmpty()) {

						responsemap.put("REQUESTS", responseBody);
						responsemap.put("status", "200");
						responsemap.put("statuscode", "200");
						responsemap.put("statusdesc", "OK");
						

					} else {
						responsemap.put("status", "404");
						responsemap.put("statusdesc", "No Tickets");

					}
				}
			}
		} catch (Exception e) {
			log.error("Error during getLastRequests..!", e);
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());

		}
		log.info("Final API Response: " + responsemap);
		return responsemap;
	}
}
