package com.snug.hcl.parser;

import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncidentRequestParser {
	public static final Logger log = LoggerFactory.getLogger(GetEmpIdParser.class);

	public static HashMap<String, Object> incidentRequestParser(String responseBody) {
		log.debug("************************ INSIDE IncidentRequestParserMETHOD ************************");
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

						if (dataObj.optString("status").equalsIgnoreCase("1")) {
							responsemap.put("ticket_status_desc", "new");
						} else if (dataObj.optString("status").equalsIgnoreCase("2")) {
							responsemap.put("ticket_status_desc", "In progress");

						} else if (dataObj.optString("status").equalsIgnoreCase("20")) {
							responsemap.put("ticket_status_desc", "Assigned");

						} else if (dataObj.optString("status").equalsIgnoreCase("6")) {
							responsemap.put("ticket_status_desc", "Resloved");

						} else if (dataObj.optString("status").equalsIgnoreCase("15")) {
							responsemap.put("ticket_status_desc", "Pending");

						} else if (dataObj.optString("status").equalsIgnoreCase("7")) {
							responsemap.put("ticket_status_desc", "Closed");

						} else if (dataObj.optString("status").equalsIgnoreCase("11")) {
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
			log.error("Error parsing IncidentRequestParser response  :" + e);
		}
		log.debug("**********************outside IncidentRequestParser METHOD ************************");
		return responsemap;
	}
}
