package com.snug.hcl.parser;

import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestTicketParser {
	public static final Logger log = LoggerFactory.getLogger(GetEmpIdParser.class);

	public static HashMap<String, Object> requestTicketParser(String responseBody) {
		log.debug("************************ INSIDE RequestTicketParser METHOD ************************");
		HashMap<String, Object> responsemap = new HashMap<String, Object>();
		try {
			if (responseBody != null) {
				JSONObject jsonObject = new JSONObject(responseBody);

				if (jsonObject != null) {

					JSONObject dataObj = jsonObject.getJSONObject("result");
					if (dataObj != null) {
						responsemap.put("number", dataObj.optString("number"));
						responsemap.put("employee_number", dataObj.optString("employee_number"));
						responsemap.put("sla_status", dataObj.optString("sla_status"));
						responsemap.put("sla_time", dataObj.optString("sla_time"));
						responsemap.put("status", dataObj.optString("status"));

						if (dataObj.optString("status").equalsIgnoreCase("4")) {
							responsemap.put("ticket_status_desc", "closed Incomplete");
						} else if (dataObj.optString("status").equalsIgnoreCase("1")) {
							responsemap.put("ticket_status_desc", "open");

						} else if (dataObj.optString("status").equalsIgnoreCase("2")) {
							responsemap.put("ticket_status_desc", "work in progress");

						} else if (dataObj.optString("status").equalsIgnoreCase("-5")) {
							responsemap.put("ticket_status_desc", "Pending");

						} else if (dataObj.optString("status").equalsIgnoreCase("3")) {
							responsemap.put("ticket_status_desc", "closed complete");

						} else if (dataObj.optString("status").equalsIgnoreCase("7")) {
							responsemap.put("ticket_status_desc", "Closed skipped");

						} else if (dataObj.optString("status").equalsIgnoreCase("30")) {
							responsemap.put("ticket_status_desc", "Cancelled");


					} else {
						responsemap.put("ticket_status_desc", " ");
					}
					} else {
						responsemap.put("status", "404");
					}
				}

			}
		} catch (Exception e) {
			log.error("Error parsing RequestTicketParser response  :" + e);
		}
		log.debug("**********************outside RequestTicketParser METHOD ************************");
		return responsemap;
	}
}


