package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

public class SingXAPICaller {
	String FILENAME = "SingXAPICaller.java";
	String LOGNAME = "SingXAPICaller";
	
	String tokenSingx 	= "";
	String secret 		= "";
	
	SingXAPICaller(){
		
		try {
			tokenSingx 	= EnvProperties.getPropertyValue("SingXToken");
			secret 		= EnvProperties.getPropertyValue("SingXEncryptionSecret");
		}catch(Throwable t) {
			Logger.log("Error getting environment values SingXToken and SingXEncryptionSecret", FILENAME, LOGNAME, t, Logger.CRITICAL);

		}
		
		Logger.log("In SingXAPICaller() : tokenSingx : "+tokenSingx+"\n secret : "+secret, FILENAME, LOGNAME, null, Logger.INFO);

	}
	
	public String excuteSingXEncryptedRequest(String mainURL, String targetURL, String requestEntity, String method, String ContentType) {
		
		String singxBaseURL = "https://external-api-uat.singx.co";
		//if(targetURL.equals("/central/external-onboarding/checkexistingcustomer"))
		
		if (targetURL.endsWith("/")) {
		    targetURL = targetURL.substring(0, targetURL.length() - 1);
		}
		
		mainURL = singxBaseURL+targetURL;
		
		return excuteSingXEncryptedRequest(mainURL, targetURL, requestEntity, method, ContentType,"true");	
	}

	private String excuteSingXEncryptedRequest(String mainURL, String targetURL, String requestEntity, String method, String ContentType, String isDev) {
		
		Logger.log("In excuteEncryptedRequest : ", FILENAME, LOGNAME, null, Logger.INFO);
		Logger.log("mainURL : "+mainURL, FILENAME, LOGNAME, null, Logger.INFO);
		Logger.log("targetURL : "+targetURL, FILENAME, LOGNAME, null, Logger.INFO);
		Logger.log("Request before encryption : "+requestEntity, FILENAME, LOGNAME, null, Logger.INFO);

		URL url;
		HttpURLConnection connection = null;
		StringBuffer response = new StringBuffer();
		String token="";
		//String tokenSingx = EnvProperties.getPropertyValue("SingXToken");

		//String secret = EnvProperties.getPropertyValue("SingXEncryptionSecret");
		
		String signatureSalt ="", signature="",decryptedPayload = ""; 
		long timestamp=0l;
		JSONObject payload = new JSONObject();
		
		
			try {
				SecureRandom random = new SecureRandom();
				byte[] saltBytes = new byte[16];
				
				random.nextBytes(saltBytes);
				signatureSalt = Base64.getEncoder().encodeToString(saltBytes);
				
				timestamp = (System.currentTimeMillis() / 1000);		

				String toSign = method.toLowerCase() + targetURL + signatureSalt + timestamp + secret;
				
			   	Logger.log("toSign : "+toSign, FILENAME, LOGNAME, null, Logger.INFO);


			   Mac mac = Mac.getInstance("HmacSHA256");
				SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
				mac.init(secretKeySpec);
				byte[] hmacSha256 = mac.doFinal(toSign.getBytes(StandardCharsets.UTF_8));
				signature = Base64.getEncoder().encodeToString(hmacSha256);

				SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(secret), "AES");
				
				
				IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(signatureSalt));
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				
				
				cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
				if (method.equals("POST")) {
					byte[] encryptedBytes = cipher.doFinal(requestEntity.getBytes(StandardCharsets.UTF_8));
					String encryptedPayload = Base64.getEncoder().encodeToString(encryptedBytes);
			
					payload.put("data", encryptedPayload);
					System.out.println("payload >> " + payload);
				}
			
			}catch (Exception t) {
				t.printStackTrace();
			}
		
		Logger.log("Encryption headers : \n method : "+method.toLowerCase() 
									+"\n signatureURL : "+targetURL
									+"\n signatureSalt : "+signatureSalt
									+"\n timestamp : "+timestamp
									+"\n secret : "+secret
									+"\n requestEntity : "+requestEntity
									+"\n payload : "+payload, FILENAME, LOGNAME, null, Logger.INFO);

		try {
			
			
			// Create connection
			url = new URL(mainURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			
			
			
			connection.setRequestProperty("Content-Type", ContentType);
			connection.setRequestProperty("Content-Length", Integer.toString(payload.toString().getBytes().length));
			connection.setRequestProperty("Accept", "application/json");
			
			connection.setConnectTimeout(50000);
			connection.setReadTimeout(50000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			
			Logger.log("Request Headers: ", FILENAME, LOGNAME, null, Logger.INFO);
			Logger.log("Basic - idfCToken: "+token+", \n signature : "+signature+",\n signatureSalt : "+signatureSalt+"\n tokenSingx : "+tokenSingx+", timestamp :"+timestamp+",\n signatureSalt : "+signatureSalt, FILENAME, LOGNAME, null, Logger.INFO);
		
				
			//Set headers for encryption request-->
			connection.setRequestProperty("x-signature", signature);
			connection.setRequestProperty("x-salt", signatureSalt);
			connection.setRequestProperty("x-api-key", tokenSingx);
			connection.setRequestProperty("x-timestamp", ""+timestamp);
			connection.setRequestProperty("x-api-iv", signatureSalt);
			connection.setRequestProperty("source", "DIGIREMIT_INWARD");
			
			connection.setDoOutput(true);
			connection.setRequestMethod(method);
			if (method.equals("POST")) {
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(payload.toString());
				wr.flush();
				wr.close();
			}
//				Logging all headers of connection -->


			// Get Response
			InputStream is = null;
			
			if (connection.getResponseCode() >= 400) {
				is = connection.getErrorStream();
			} else {
				is = connection.getInputStream();
			}
			Logger.log("Response headers : ", FILENAME, LOGNAME, null, Logger.INFO);

			for (Map.Entry<String, java.util.List<String>> header : connection.getHeaderFields().entrySet()) {
    			Logger.log(header.getKey() + ": " + header.getValue(), FILENAME, LOGNAME, null, Logger.INFO);

            }

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append("\r");
			}
			rd.close();
			
			Logger.log(" response : " + response, FILENAME, LOGNAME, null, Logger.INFO);

			JSONObject jsonObject = new JSONObject(response.toString());
			System.out.println("response >> "+ response);
			// Decode the secret key and IV from Base64
			SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(secret), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(signatureSalt));

			// Decode the encrypted payload from Base64
			byte[] encryptedBytes = Base64.getDecoder().decode(jsonObject.getString("body"));

			// Initialize the cipher for AES decryption with CBC mode and PKCS5 padding
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			// Perform the decryption
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			// Convert the decrypted bytes into a string (assuming UTF-8 encoding)
			decryptedPayload = new String(decryptedBytes, StandardCharsets.UTF_8);
			
			Logger.log("decryptedPayload : " + decryptedPayload, FILENAME, LOGNAME, null, Logger.INFO);

		} catch (Exception t) {
			t.printStackTrace();
			//Logger.log("Connection Error: ", FILENAME, LOGNAME, t, Logger.CRITICAL);
			response.append("");
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return decryptedPayload;
	} 

	public String getDecryptedResponse(String encryptedPayload, String secret, String signatureSalt) {
		String decryptedPayload = "";
	    try {
	        // Decode the secret key and IV from Base64
	        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(secret), "AES");
	        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(signatureSalt));

	        // Decode the encrypted payload from Base64
	        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPayload);

	        // Initialize the cipher for AES decryption with CBC mode and PKCS5 padding
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

	        // Perform the decryption
	        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

	        // Convert the decrypted bytes into a string (assuming UTF-8 encoding)
	        decryptedPayload = new String(decryptedBytes, StandardCharsets.UTF_8);

	        // Print the decrypted payload
	        System.out.println("Decrypted Payload: " + decryptedPayload);

	    } catch (Exception e) {
	        e.printStackTrace();  // Print the stack trace for debugging
	        System.out.println("An error occurred during decryption: " + e.getMessage());
	    }
	    return decryptedPayload;
	}
	
}
