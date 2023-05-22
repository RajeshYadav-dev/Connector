package com.snug.hcl.parser;

import java.util.HashMap;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateFactorTokenParser {
  public static final Logger log = LoggerFactory.getLogger(ValidateFactorTokenParser.class);

  public static HashMap < String, String > getValidateTokenResponse(String responseBody) {

    log.debug(
      "************************ INSIDE ValidateFactorTokenParser:getValidateTokenResponse METHOD ************************");
    HashMap < String, String > responsemap = new HashMap < String, String > ();
    try {
      if (responseBody != null) {

        JSONObject jsonObject = new JSONObject(responseBody);

        if (jsonObject != null) {
          responsemap.put("factorResult", jsonObject.optString("factorResult"));
          responsemap.put("errorCode", jsonObject.optString("errorCode"));
          responsemap.put("errorSummary", jsonObject.optString("errorSummary"));
          responsemap.put("status", "200");
        }
      } else {
        responsemap.put("status", "404");

      }

    } catch (Exception e) {
      responsemap.put("status", "404");
      log.error("Error parsing getValidateTokenResponse response  :" + e);
    }
    log.info("Final ValidateFactorTokenParser:getValidateTokenResponse responsemap : " + responsemap.toString());
    log.debug(
      "**********************Exiting ValidateFactorTokenParser:getValidateTokenResponse METHOD ************************");
    return responsemap;
  }

}