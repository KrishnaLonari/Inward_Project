package com.api.remitGuru.component.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class SingXSecretKeyManager {
	static com.api.remitGuru.web.controller.SQLRepositoryController repository = new com.api.remitGuru.web.controller.SQLRepositoryController();

	final static String FILENAME 	= SingXSecretKeyManager.class.getSimpleName()+".java";
	final static String LOGNAME 	= SingXSecretKeyManager.class.getSimpleName();
	
	private static String secretKey;
    private static long lastFetchedTime;
    private static final Object lock = new Object();

    public static String getSecretKey() {
        long now = System.currentTimeMillis();

        synchronized (lock) {

            if (secretKey == null || secretKey.isEmpty()) {
                try {
                    long startTime = System.currentTimeMillis();
                    String fetchedKey = fetchSecretKeyFromApi();

                    if (fetchedKey != null && !fetchedKey.isEmpty()) {
                        secretKey = fetchedKey;
                        lastFetchedTime = now;

                    } else {
                        log("Fetched key is null or empty. Using old key as fallback.");
                        alert("Fetched key is empty. Using fallback secret key.");
                        secretKey = getFromDB("SINGX_API_SECRET");
                    }

                } catch (Exception e) {
                    log("Failed to fetch secret key. Using old key as fallback. Reason: " + e.getMessage());
        			Logger.log("API failure in fetching secret key: " + e.getMessage(), FILENAME, LOGNAME,e, Logger.CRITICAL);

                }
            }
        }

        return secretKey;
    }

    private static String getFromDB(String attrName) {
    	ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		String key="";
		query.append(" SELECT					");
		query.append(" 		attributeValue			");//0
		query.append(" FROM						");
		query.append(" 		AttributeMaster				");
		query.append(" WHERE attributeName	= ?		");
		query.append(" AND isActive		= 'Y'		");
		
		params.add(attrName);
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,"IDFCIN");
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getFromDB query : " + query, FILENAME,LOGNAME, t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}
		
		if(result!=null && result.size()>0) {
			String temp[] = (String[])result.get(0);
			key = temp[0];
		}

		return key;
	}

	private static String fetchSecretKeyFromApi() throws Exception {
    	String apiReponse="";
		String secretKey="";
		try {
			String API_URL 			= EnvProperties.getPropertyValue("KMS_EXPORT_PROPERTY_API_URL") == null ? "" : EnvProperties.getPropertyValue("KMS_EXPORT_PROPERTY_API_URL");
			String CLIENT_ID		= EnvProperties.getPropertyValue("KMS_CLIENT_ID") == null ? "" : EnvProperties.getPropertyValue("KMS_CLIENT_ID");                          
			String CLIENT_SECRET 	= EnvProperties.getPropertyValue("KMS_CLIENT_SECRET") == null ? "" : EnvProperties.getPropertyValue("KMS_CLIENT_SECRET");              
			
			if(API_URL.isEmpty() || CLIENT_ID.isEmpty() || CLIENT_SECRET.isEmpty()) {
				Logger.log("API_URL OR CLIENT_ID OR CLIENT_SECRET is Empty!", FILENAME, LOGNAME,null,Logger.WARNING);

				return "";
			}
			
			String auth = CLIENT_ID + ":" + CLIENT_SECRET;
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
	        String idfCToken = new String(encodedAuth);
	        
	        if(null==idfCToken || idfCToken.isEmpty() ) {
				Logger.log("idfCToken is Null OR Empty!", FILENAME, LOGNAME,null,Logger.WARNING);

				return "";
			}
	        
			KMSExportRequest exportRequest=new KMSExportRequest("SINGX_API_SECRET");
			
			ExternalAPICallerUtil apiCallerUtil=new ExternalAPICallerUtil();
			
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Authorization", "Basic " + idfCToken);
			//According to Ayush, below values are for reference only, and it will be same for PROD
			headers.put("correlationId", "e0c4a3c2-d29d-11eb-b8bc");
			headers.put("source", "internal");
			headers.put("transactionId", "1234-trans-abcd");
			apiReponse = apiCallerUtil.callPostApi(API_URL, exportRequest, headers);
			
			secretKey = extractSecretKey(apiReponse);
			
			Logger.log("secretKey : "+ secretKey, FILENAME, LOGNAME, null, Logger.INFO);

		} catch (Throwable t) {
			Logger.log("Exception during KMSExport API Calling : "+t.getMessage(), FILENAME, LOGNAME, t,Logger.CRITICAL);
		}
		
		return secretKey;
    }
    
    private static String extractSecretKey(String jsonResponse) {
        try {
            JSONObject root = new JSONObject(jsonResponse);

            if (root.has("metadata")) {
                JSONObject metadata = root.getJSONObject("metadata");
                String status = metadata.optString("status", "");

                if ("SUCCESS".equalsIgnoreCase(status)) {
                    JSONArray resourceData = root.optJSONArray("resource_data");
                    if (resourceData != null && resourceData.length() > 0) {
                        JSONObject firstItem = resourceData.getJSONObject(0);
                        return firstItem.optString("content", ""); 
                    }
                    else
                    	return "";
                }
                else
                	return "";
            }
            else
            	return "";
            	
        } catch (Exception e) {
			Logger.log("Failed to parse secret key from response: " + e.getMessage(), FILENAME, LOGNAME,e, Logger.CRITICAL);

        }

        return "";
    }

    private static void log(String message) {
		Logger.log("[SecretKeyManager] " + message, FILENAME, LOGNAME,null,Logger.INFO);
    }

    private static void alert(String message) {
		Logger.log("[Alert] " + message, FILENAME, LOGNAME,null,Logger.WARNING);
    }
}
