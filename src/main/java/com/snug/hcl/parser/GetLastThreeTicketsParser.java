package com.snug.hcl.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import org.json.*;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetLastThreeTicketsParser {
	public static final Logger log = LoggerFactory.getLogger(GetLastThreeTicketsParser.class);
	public static List sortTickets(List allTickets)
	{
		log.debug("************************ INSIDE GetLastThreeTicketsParser:sortTickets METHOD ************************");
		log.debug("allTickets Unsorted: ", allTickets.toString());
		Collections.sort(allTickets, new Comparator() {
			
			public int compare(JSONObject a, JSONObject b) {
							return 0;
			}

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				String str1 = o1.toString();
				String str2 = o2.toString();
				JSONObject a = new JSONObject(str1);
				JSONObject b = new JSONObject(str2);
				 String stra = new String();
		         String strb = new String();
		        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss aa");
				Date date1 = null,date2 = null;
				
				try {
					 stra = (String) a.get("opened_at");
					 strb = (String) b.get("opened_at");
					 date1 =sdf.parse(stra);
					 date2 =sdf.parse(strb);
					
				} catch (JSONException | ParseException e) {
					e.printStackTrace();
				}
				
				return date2.compareTo(date1);
				
			}
			});
		
		log.debug("allTickets Sorted: ", allTickets.toString());	
		log.debug("************************ Exiting GetLastThreeTicketsParser:sortTickets METHOD ************************");
		
		return allTickets;
	}
	public static HashMap<String, Object> getLastTicketsResponse(String responseBodyINC, String responseBodyREQ) {

		log.debug(
				"************************ INSIDE GetLastThreeTicketsParser:getLastTicketsResponse METHOD ************************");
		HashMap<String, Object> responsemap = new HashMap<String, Object>();
		JSONArray arrayTickets = new JSONArray();
		JSONObject jsonObjectTickets = new JSONObject();
		JSONObject jsonObjectSortedTickets  = new JSONObject();
		try {
		
			/*** Parsing INC Tickets**/
			if (responseBodyINC != null && responseBodyINC != "") {

				JSONObject jsonObject = new JSONObject(responseBodyINC);

				if (jsonObject != null) {
					JSONArray jArray = new JSONArray(jsonObject.getJSONArray("result"));
					if (null != jArray && !jArray.isEmpty()) {
						for (int i = 0; i < jArray.length(); i++) {
							org.json.JSONObject jsonObjectINC = new org.json.JSONObject();
							org.json.JSONObject Obj = jArray.getJSONObject(i);
							if (Obj != null) {
								jsonObjectINC.put("number", Obj.optString("number"));
								jsonObjectINC.put("ticketStatus", Obj.optString("status"));
								jsonObjectINC.put("opened_at", Obj.optString("opened_at"));
								jsonObjectINC.put("sla_status", Obj.optString("sla_status"));
								jsonObjectINC.put("sla_time", Obj.optString("sla_time"));
								arrayTickets.put(jsonObjectINC);
							}
						}

					} else {
						responsemap.put("statusdescINC", "No IncidentTickets");

					}
				} else {
					responsemap.put("statusdescINC", "No IncidentTickets");
				}
			} else {
				responsemap.put("statusdescINC", "No IncidentTickets");
			}
/*** Parsing Request Tickets**/
			if (responseBodyREQ != null && responseBodyREQ !="") {

				JSONObject jsonObject = new JSONObject(responseBodyREQ);

				if (jsonObject != null) {
					JSONArray jArray = new JSONArray(jsonObject.getJSONArray("result"));
					if (null != jArray && !jArray.isEmpty()) {
						for (int i = 0; i < jArray.length(); i++) {
							org.json.JSONObject jsonObjectREQ = new org.json.JSONObject();
							org.json.JSONObject Obj = jArray.getJSONObject(i);
							if (Obj != null) {
								jsonObjectREQ.put("number", Obj.optString("number"));
								jsonObjectREQ.put("opened_at", Obj.optString("opened_at"));
								jsonObjectREQ.put("ticketStatus", Obj.optString("status"));
								jsonObjectREQ.put("sla_status", Obj.optString("sla_status"));
								jsonObjectREQ.put("sla_time", Obj.optString("sla_time"));
								arrayTickets.put(jsonObjectREQ);
							}
						}

					} else {
						responsemap.put("statusdescREQ", "No Request Tickets");

					}
				} else {
					responsemap.put("statusdescREQ", "No Request Tickets");
				}
			} else {
				responsemap.put("statusdescREQ", "No Request Tickets");
			}
			
		
	/**Sorting starts***/
			JSONArray sortedJsonArray = new JSONArray();
			List list = new ArrayList();
			for (int i = 0; i < arrayTickets.length(); i++) {
				list.add(arrayTickets.getJSONObject(i));
			}

			List sortedList = sortTickets(list);
			

			if ( arrayTickets.length()>=3) {
			for (int i = 0; i < 3; i++) {
				sortedJsonArray.put(sortedList.get(i));
			}}
			else
			{
				for (int i = 0; i < arrayTickets.length(); i++) {
					sortedJsonArray.put(sortedList.get(i));
				}
			}
				
			jsonObjectSortedTickets.put("result", sortedJsonArray);
			responsemap.put("LastTickets", jsonObjectSortedTickets.toString());
			responsemap.put("statuscode", "200");
			responsemap.put("statusdesc", "OK");
			
			
		
		} catch (Exception e) {
			responsemap.put("status", "404");
			log.error("Error parsing responseBodyINC response  :" + e);
		}
		log.info("Final GetLastThreeTicketsParser:getLastTicketsResponse responsemap : " + responsemap.toString());
		log.debug(
				"**********************Exiting GetLastThreeTicketsParser:getLastTicketsResponse METHOD ************************");
		return responsemap;
	}

}
