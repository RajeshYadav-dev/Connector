package com.snug.hcl.parser;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetFactorIdParser {
	public static final Logger log = LoggerFactory.getLogger(GetFactorIdParser.class);

	public static String getFactorIDResponse(String responseBody) {

		log.debug(
				"************************ INSIDE GetFactorIdParser:getFactorIDResponse METHOD ************************");

		String factorId = "";

		HashMap<String, String> responsemap = new HashMap<String, String>();
		try {
			if (responseBody != null) {

				JSONArray jArray = new JSONArray(responseBody);
				if (null != jArray && !jArray.isEmpty()) {
					for (int i = 0; i < jArray.length(); i++) {

						JSONObject Obj = jArray.getJSONObject(i);
						if (Obj != null) {
							if (Obj.optString("factorType").equalsIgnoreCase("token:software:totp")) {
								responsemap.put("factorId", Obj.optString("id"));
								factorId = Obj.optString("id");
								responsemap.put("factorType", Obj.optString("factorType"));
								responsemap.put("status", "200");
								break;
							}
						} else {
							responsemap.put("status", "404");
							factorId = "";
						}
					}
				}

				else {
					responsemap.put("status", "404");
					factorId = "";
				}

			} else {
				responsemap.put("status", "404");
				factorId = "";

			}

		} catch (Exception e) {
			log.error("Error parsing getFactorIDResponse response  :" + e);
		}
		log.info(
				"Final GetFactorIdParser:getFactorIDResponse responsemap : "+responsemap.toString());
		log.debug("**********************Exiting GetFactorIdParser:getFactorIDResponse METHOD ************************");
		return factorId;
	}
}
