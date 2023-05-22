package com.snug.hcl.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class OktaRequestUtility {

	public static final Logger log = LoggerFactory.getLogger(OktaRequestUtility.class);

	@Value("${api.timeout}")
	private String apiTimeout;

	@Value("${api.okta.key}")
	private String oktaAuthKey;

	public HashMap<String, String> sendRequest(String hosturl, String method, String requestBody)
			throws UnknownHostException {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		StringBuilder finalOutput = new StringBuilder();
		log.info("HostURL =" + hosturl);
		log.info("method =" + method);
		log.info("requestBody =" + requestBody);
		log.info("oktaAuthKey =" + oktaAuthKey);
		DataOutputStream out = null;
		BufferedReader br = null;
		try {
			String httpsURL = hosturl;
			URL myUrl = new URL(httpsURL);
			HttpURLConnection conn = null;
			log.info(method + ": Request came for Request URL :" + hosturl);
			conn = (HttpURLConnection) myUrl.openConnection();
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Connection", "Keep-Alive");
			if ((oktaAuthKey != null) && (!oktaAuthKey.isEmpty())) {

				conn.setRequestProperty("Authorization", "SSWS " + oktaAuthKey);
			}

			conn.setReadTimeout(1000 * Integer.parseInt(apiTimeout));
			conn.setConnectTimeout(1000 * Integer.parseInt(apiTimeout));
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (null != requestBody) {
				conn.setDoOutput(true);
				out = new DataOutputStream(conn.getOutputStream());
				out.write(requestBody.getBytes());
				// out.write(requestBody.getBytes());
				out.flush();
				out.close();
			}
			log.info("RESPONSE code :" + conn.getResponseCode());
			log.info("RESPONSE message " + conn.getResponseMessage());
			log.info("RESPONSE message " + conn.getErrorStream());
			if (conn.getResponseCode() == 200) {
				if (null != conn.getInputStream()) {
					log.info(">>>>>>>>>>:  conn.getInputStream() : not null");
					br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line = "";
					log.info("line :" + line);
					while ((line = br.readLine()) != null) {
						finalOutput.append(line);
					}
				}
					log.info(">>>>>>>>>>: Response from SOI :" + finalOutput.toString());
					log.info("RESPONSE code :" + conn.getResponseCode());
					log.info("RESPONSE message " + conn.getResponseMessage());
					log.info("RESPONSE message " + conn.getErrorStream());
					responseMap.put("RESPONSEBODY", finalOutput.toString());
				}
			
			responseMap.put("statuscode", String.valueOf(conn.getResponseCode()));
			responseMap.put("statusdesc", conn.getResponseMessage());
		} catch (SocketTimeoutException e) {
			log.error("SocketTimeoutException :", e);
			responseMap.put("statuscode", "71");
			responseMap.put("statusdesc", e.getMessage());
		} catch (FileNotFoundException e) {
			log.error("Data not Available1: " + e.getMessage());
			responseMap.put("statuscode", "404");
			responseMap.put("statusdesc", e.getMessage());
		} catch (IOException e) {
			log.error("Data not Available2: " + e.getMessage() + " " + e.toString());
			responseMap.put("statuscode", "403");
			responseMap.put("statusdesc", e.getMessage());
		} catch (Exception e) {
			log.error("Error during " + method + " host request..!", e);
			responseMap.put("statuscode", "75");
			responseMap.put("statusdesc", e.getMessage());
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("Exception while closing OutputStreamWriter... " + e.getMessage());
				}
			}
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("Exception while closing Buffered Reader... " + e.getMessage());
				}
			}
		}
		log.debug("Response :" + responseMap);
		return responseMap;
	}

}