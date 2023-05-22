package com.snug.hcl.service;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.snug.hcl.request.ValidateUserRequest;
import com.snug.hcl.parser.GetUserIdParser;
import com.snug.hcl.parser.ValidateFactorTokenParser;
import com.snug.hcl.parser.GetFactorIdParser;
import com.snug.hcl.request.FindUserIDRequest;
import com.snug.hcl.request.GetFactorsRequest;
import com.snug.hcl.request.ValidateTokenRequest;

import com.snug.hcl.utility.OktaRequestUtility;

@Service
public class APIGetUserIDService {
	public static final Logger log = LoggerFactory.getLogger(APIRequestService.class);

	@Value("${api.findUserID.url}")
	private String apiFindUserIDUrl;

	@Value("${api.listFactors.url}")
	private String apilistFactorsUrl;

	@Value("${api.verifyTockenFactor.url}")
	private String apiVerifyTockenFactorUrl;

	@Value("${api.verifyTockenFactor.body}")
	private String apiVerifyTockenFactorBody;

	@Autowired
	private OktaRequestUtility oktaRequestUtility;

	public HashMap<String, String> validateOKTAToken(String email, String passcode) {

		log.debug("*******inside the validateOKTAToken method**********");

		HashMap<String, String> responsemap = new HashMap<String, String>();

		log.info("Final email: " + email);
		log.info("Final passcode: " + passcode);

		String userId = findUserID(email);
	
		if (userId != null && userId != "") {
			
			String factorId = getFactorID(userId);
			
			if (factorId != null && factorId != "") {
				
				responsemap = validateFactorToken(userId, factorId, passcode);
			
			} else {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Invalid factorID");

			}

		} else {
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", "Invalid UserID");
		}
		log.info("Final API Response: " + responsemap);
		return responsemap;
	}

	public String findUserID(String email) {
		log.debug("*******inside the APIGetUserIDService:findUserID method**********");

		HashMap<String, String> responsemap = new HashMap<String, String>();
		String userId = "";

		try {
			HashMap<String, String> requestmap = new FindUserIDRequest().findUserIDRequestURL(apiFindUserIDUrl, email);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating get user id request.");
			} else {
				HashMap<String, String> respMap = oktaRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "GET",
						null);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("200")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();

					log.debug("raw response--->" + responseBody);

					userId = GetUserIdParser.getUserIDResponse(responseBody);

					log.debug("***userId*** " + userId);
				}
			}

		} catch (Exception e) {
			log.error("Error during ValidateUserRequest..!", e);
			userId = "";
		}

		log.info("Final userId: " + userId);
		log.debug("*******exiting the APIGetUserIDService:findUserID method**********");

		return userId;

	}

	public String getFactorID(String userId) {

		log.debug("*******inside the getFactorID method**********");

		HashMap<String, String> responsemap = new HashMap<String, String>();

		log.info("Final userId: " + userId);
		log.info("Final apilistFactorsUrl: " + apilistFactorsUrl);

		String factorId = "";

		try {
			HashMap<String, String> requestmap = new GetFactorsRequest().findFactorsRequestURL(apilistFactorsUrl,
					userId);
			log.info("Final requestmap: " + requestmap);

			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating get factors request.");
			} else {
				HashMap<String, String> respMap = oktaRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "GET",
						null);
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("200")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();

					log.debug("raw response--->" + responseBody);

					factorId = GetFactorIdParser.getFactorIDResponse(responseBody);

					log.debug("***factorId*** " + factorId);
				}
			}

		} catch (Exception e) {
			log.error("Error during getFactorID..!", e);
			factorId = "";
		}
		log.info("Final API Response for getFactorID: " + responsemap);
		log.info("Final factorId: " + factorId);
		return factorId;

	}

	public HashMap<String, String> validateFactorToken(String userId, String factorId, String passcode) {

		HashMap<String, String> responsemap = new HashMap<String, String>();
		log.info("Final factorId: " + factorId);
		log.info("Final apilistFactorsUrl: " + apilistFactorsUrl);

		try {
			HashMap<String, String> requestmap = new ValidateTokenRequest().validateTokenRequestURL(
					apiVerifyTockenFactorUrl, apiVerifyTockenFactorBody, userId, factorId, passcode);
			if (requestmap.get("REQUEST_URL").equals("ERROR")) {
				responsemap.put("statuscode", "500");
				responsemap.put("statusdesc", "Error creating validateFactorToken request.");
			} else {
				HashMap<String, String> respMap = oktaRequestUtility.sendRequest(requestmap.get("REQUEST_URL"), "POST",
						requestmap.get("REQUEST_BODY"));
				responsemap.put("statuscode", respMap.get("statuscode").toString());
				responsemap.put("statusdesc", respMap.get("statusdesc"));
				if (responsemap.get("statuscode").toString().equals("200")) {
					String responseBody = respMap.get("RESPONSEBODY").toString();
					log.debug("raw response--->" + responseBody);

					HashMap<String, String> validateTokenrespMap = ValidateFactorTokenParser
							.getValidateTokenResponse(responseBody);
					responsemap.put("factorResult", validateTokenrespMap.get("factorResult").toString());
					responsemap.put("errorCode", validateTokenrespMap.get("errorCode"));
					responsemap.put("errorSummary", validateTokenrespMap.get("errorSummary"));
					responsemap.put("status", validateTokenrespMap.get("status"));
					log.debug("***responsemap*** " + responsemap);
				}
			}

		} catch (Exception e) {
			log.error("Error during validateFactorToken..!", e);
			responsemap.put("status", "404");
			responsemap.put("statuscode", "500");
			responsemap.put("statusdesc", e.getMessage());
		}
		log.info("Final API Response:validateFactorToken " + responsemap);
		return responsemap;
	}

}