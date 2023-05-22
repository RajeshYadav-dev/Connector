package com.snug.hcl.parser;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetEmpIdParser {

	public static final Logger log = LoggerFactory.getLogger(GetEmpIdParser.class);

	public static HashMap<String, Object> getEmpIdParser(String responseBody) {
		log.debug("************************ INSIDE GetEmpIdParser METHOD ************************");
		HashMap<String, Object> responsemap = new HashMap<String, Object>();
		try {
			if (responseBody != null) {
				JSONObject jsonObject = new JSONObject(responseBody);

				if (jsonObject != null) {

					JSONArray jsonArray = jsonObject.getJSONArray("result");
					if (jsonArray != null) {
						JSONArray jArray = new JSONArray(jsonArray);

						if (null != jArray && !jArray.isEmpty()) {
							JSONObject Obj = jArray.getJSONObject(0);
							if (Obj != null) {

								responsemap.put("active", Obj.optString("active"));
								responsemap.put("vip", Obj.optString("vip"));
								responsemap.put("email", Obj.optString("email"));
								responsemap.put("status", "200");
							}
						}
					}
				}
			} else {
				responsemap.put("status", "404");

			}

		} catch (Exception e) {
			log.error("Error parsing ChangeWalletStatus response  :" + e);
		}
		log.debug("**********************outside GetEmpIdParser METHOD ************************");
		return responsemap;
	}
}
