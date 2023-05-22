package com.snug.hcl.parser;

import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAccountUnlockRequestParser {
	public static final Logger log = LoggerFactory.getLogger(CreateAccountUnlockRequestParser.class);

	public static HashMap<String, Object> createAccountUnlockRequetParser(String responseBody) {
		log.debug("************************ INSIDE CreateAccountUnlockRequetParser  METHOD ************************");
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
			log.error("Error parsing CreateAccountUnlockRequetParser response  :" + e);
		}
		log.debug("**********************outside CreateAccountUnlockRequetParser METHOD ************************");
		return responsemap;
	}
}
