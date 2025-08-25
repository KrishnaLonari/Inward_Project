package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class KongBearerTokenManager {

	static com.api.remitGuru.web.controller.SQLRepositoryController repository = new com.api.remitGuru.web.controller.SQLRepositoryController();

	static String API_URL 			= EnvProperties.getPropertyValue("KONG_BEARER_API_URL") == null ? "" : EnvProperties.getPropertyValue("KONG_BEARER_API_URL");
	static String CLIENT_ID		= EnvProperties.getPropertyValue("KONG_CLIENT_ID") == null ? "" : EnvProperties.getPropertyValue("KONG_CLIENT_ID");                          
	static String CLIENT_SECRET 	= EnvProperties.getPropertyValue("KONG_CLIENT_SECRET") == null ? "" : EnvProperties.getPropertyValue("KONG_CLIENT_SECRET");              

	final static String FILENAME 	= KongBearerTokenManager.class.getSimpleName()+".java";
	final static String LOGNAME 	= KongBearerTokenManager.class.getSimpleName();
	
	private static String bearerToken;
    private static long lastFetchedTime;
    private static final Object lock = new Object();
    private static Map<String, Long> tokenMap = new HashMap<>();
    private static final String TOKEN_KEY = "token";
    
    public static String getBearerToken() {
        

        synchronized (lock) {

        	try {
	            long EXPIRY_DURATION = EnvProperties.getPropertyValue("KONG_BEARER_EXPIRY_DURATION") == null ? 20 * 60 * 1000 : Long.parseLong(EnvProperties.getPropertyValue("KONG_BEARER_EXPIRY_DURATION")); // 20 minutes in milliseconds
	
	            long currentTime = System.currentTimeMillis();

	            if (false && tokenMap.containsKey(TOKEN_KEY)) {
	    			Logger.log("tokenMap contains token ", FILENAME, LOGNAME,null,Logger.INFO);

	                long fetchedTime = tokenMap.get(TOKEN_KEY);
	                if (currentTime - fetchedTime < EXPIRY_DURATION) {
		    			Logger.log("token not expired, using it ", FILENAME, LOGNAME,null,Logger.INFO);

	                    return bearerToken; 
	                } 
	                else {
		    			Logger.log("token expired!", FILENAME, LOGNAME,null,Logger.INFO);

	                    tokenMap.remove(TOKEN_KEY); 
	                }
	            }

    			Logger.log("Fetching new one", FILENAME, LOGNAME,null,Logger.INFO);

	            // Token either doesn't exist or is expired
	            bearerToken = fetchBearerTokenFromApi();
	            tokenMap.put(TOKEN_KEY, currentTime);
	            return bearerToken;
	            
            }catch (Exception e) {
                log("Failed to fetch bearerToken. Using old TOKEN as fallback. Reason: " + e.getMessage());
    			Logger.log("API failure in fetching bearerToken: " + e.getMessage(), FILENAME, LOGNAME,e, Logger.CRITICAL);

            }
        }

        return bearerToken;
    }
    
    

	private static String fetchBearerTokenFromApi() throws Exception {
		try {
			String boundary = UUID.randomUUID().toString();
			String lineEnd = "\r\n";
			String twoHyphens = "--";

			URL url = new URL(API_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			conn.setRequestProperty("Cache-Control", "no-cache");

			DataOutputStream request = new DataOutputStream(conn.getOutputStream());

			addFormField(request, boundary, "client_id", CLIENT_ID);
			addFormField(request, boundary, "client_secret", CLIENT_SECRET);
			addFormField(request, boundary, "grant_type", "client_credentials");
			addFormField(request, boundary, "scope", "remit-singx-services");

			request.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			request.flush();
			request.close();

			int responseCode = conn.getResponseCode();
			Logger.log("Response Code: " + responseCode, FILENAME, LOGNAME,null,Logger.INFO);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine).append("\n");
			}
			in.close();

			Logger.log("apiReponse : "+response, FILENAME, LOGNAME,null,Logger.INFO);
			String bearerToken="";
			
			bearerToken = extractSecretKey(response.toString());
			return bearerToken;

		} catch (Exception e) {
			Logger.log("Exception during KMSExport API Calling : "+e.getMessage(), FILENAME, LOGNAME, e,Logger.CRITICAL);

			return "Error";
		}
    }
    
	private static void addFormField(DataOutputStream out, String boundary, String name, String value) throws IOException {
		String lineEnd = "\r\n";
		String twoHyphens = "--";

		out.writeBytes(twoHyphens + boundary + lineEnd);
		out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + lineEnd);
		out.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
		out.writeBytes(lineEnd);
		out.writeBytes(value + lineEnd);
	}
	
    private static String extractSecretKey(String jsonResponse) {
        try {
            JSONObject root = new JSONObject(jsonResponse);

            if (root.has("access_token")) {
            	return root.optString("access_token", "");
            }
            else
            	return "";
            	
        } catch (Exception e) {
			Logger.log("Failed to parse bearer token from response: " + e.getMessage(), FILENAME, LOGNAME,e, Logger.CRITICAL);

        }

        return "";
    }

    private static void log(String message) {
		Logger.log("[BearerTokenManager] " + message, FILENAME, LOGNAME,null,Logger.INFO);
    }

    private static void alert(String message) {
		Logger.log("[Alert] " + message, FILENAME, LOGNAME,null,Logger.WARNING);
    }
}
