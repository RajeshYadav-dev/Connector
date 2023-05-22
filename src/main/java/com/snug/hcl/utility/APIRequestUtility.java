package com.snug.hcl.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class APIRequestUtility {

	public static final Logger log = LoggerFactory.getLogger(APIRequestUtility.class);

	@Value("${api.timeout}")
	private String apiTimeout;
	
	@Value("${basic.auth.username}")
	private String basicAuthUsername;
	
	@Value("${basic.auth.pwd}")
	private String basicAuthPwd;
	
	public HashMap<String, String> sendRequest(String hosturl, String method, String request) throws UnknownHostException{
		HashMap<String, String> responseMap = new HashMap<String, String>();
		StringBuilder finalOutput = new StringBuilder();
		log.info("HostURL ="+hosturl);
		OutputStreamWriter out = null;
		BufferedReader br = null;
		try {
			String httpsURL = hosturl;
			URL myUrl = new URL(httpsURL);
			HttpURLConnection conn = null;
			log.info(method+": Request came for Request URL :"+hosturl);
			conn = (HttpURLConnection)myUrl.openConnection();
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Connection", "Keep-Alive");
			if((basicAuthUsername != null && basicAuthPwd != null)
					&& (!basicAuthUsername.isEmpty() && !basicAuthPwd.isEmpty())) {
				String basicAuthCredentials = basicAuthUsername+ ":" + basicAuthPwd;
				String encodedCredentials = Base64.encodeBase64String(basicAuthCredentials.getBytes());
				conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);
			}
			conn.setReadTimeout(1000*Integer.parseInt(apiTimeout));
			conn.setConnectTimeout(1000*Integer.parseInt(apiTimeout));
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			if(null != request) {
				out = new OutputStreamWriter(conn.getOutputStream());
				out.write(request);
				out.flush();
			}
			log.info("RESPONSE code :"+conn.getResponseCode());
			log.info("RESPONSE message "+conn.getResponseMessage());
			if(null != conn.getInputStream()) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ( (line = br.readLine()) != null){
					finalOutput.append(line);
				}
				log.trace(">>>>>>>>>>: Response from SOI :" +finalOutput.toString());
				responseMap.put("RESPONSEBODY", finalOutput.toString());
			}
			responseMap.put("statuscode", String.valueOf(conn.getResponseCode()));
			responseMap.put("statusdesc", conn.getResponseMessage());
		} catch (SocketTimeoutException e) {
	    	log.error("SocketTimeoutException :", e);
	    	responseMap.put("statuscode", "71");
			responseMap.put("statusdesc", e.getMessage());
	    } catch (FileNotFoundException e) {
	    	log.error("Data not Available: "+ e.getMessage());
	    	responseMap.put("statuscode", "404");
			responseMap.put("statusdesc", e.getMessage());
	    } catch (IOException e) {
			log.error("Data not Available: "+ e.getMessage());
			responseMap.put("statuscode", "400");
			responseMap.put("statusdesc", e.getMessage());
		} catch (Exception e) {
			log.error("Error during "+method+" host request..!", e);
			responseMap.put("statuscode", "75");
			responseMap.put("statusdesc", e.getMessage());
		}finally {
			if(null != out) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("Exception while closing OutputStreamWriter... "+ e.getMessage() );
				}
			}
			if(null != br) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("Exception while closing Buffered Reader... "+ e.getMessage() );
				}
			}
		}
		log.debug("Response :"+responseMap);
		return responseMap;
	}
}
