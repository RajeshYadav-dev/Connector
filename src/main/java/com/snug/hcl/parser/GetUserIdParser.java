package com.snug.hcl.parser;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUserIdParser {
	public static final Logger log = LoggerFactory.getLogger(GetUserIdParser.class);
	
	public static String getUserIDResponse(String responseBody) {
		
		log.debug("************************ INSIDE GetUserIdParser:getUserIDParser METHOD ************************");
		
		String userId ="";
		
		HashMap<String, String> responsemap = new HashMap<String, String>();
		try {
			if (responseBody != null) {
			
				JSONArray jArray = new JSONArray(responseBody);
				if(null != jArray && !jArray.isEmpty()) {
				JSONObject Obj = jArray.getJSONObject(0);
				
				if (Obj != null) {
					responsemap.put("id", Obj.optString("id"));
					responsemap.put("vip", Obj.optString("vip"));
					responsemap.put("status", "200");
					userId = Obj.optString("id");
					log.debug("***userId*** "+userId);
				}						
			}else {
				responsemap.put("status", "404");
				userId = "";
			}

				
			} else {
				responsemap.put("status", "404");
				userId = "";

			}

		} catch (Exception e) {
			log.error("Error parsing getUserIDParser response  :" + e);
		}
		log.info(
				"Final GetUserIdParser:getUserIDResponse responsemap : "+responsemap.toString());
		log.debug("**********************Exiting getUserIDParser METHOD ************************");
		return userId;
	}

}
