package com.snug.hcl.parser;

import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePasswordResetTicketParser {
	public static final Logger log = LoggerFactory.getLogger(CreatePasswordResetTicketParser.class);

	public static HashMap<String, Object> createPasswordResetTicketParser(String responseBody) {
		log.debug("************************ INSIDE CreatePasswordResetTicketParser METHOD ************************");
		HashMap<String, Object> responsemap = new HashMap<String, Object>();
		try {
			if (responseBody != null) {
				JSONObject jsonObject = new JSONObject(responseBody);

				if (jsonObject != null) {

					JSONObject dataObj = jsonObject.getJSONObject("result");
					if (dataObj != null) {

						responsemap.put("number", dataObj.optString("number"));
						responsemap.put("state", dataObj.optString("state"));
						responsemap.put("u_type", dataObj.optString("u_type"));

					} else {
						responsemap.put("status", "404");
					}
				}
			}
		} catch (Exception e) {
			log.error("Error parsing CreatePasswordResetTicketParserresponse  :" + e);
		}
		log.debug("**********************outside CreatePasswordResetTicketParser METHOD ************************");
		return responsemap;
	}
}



