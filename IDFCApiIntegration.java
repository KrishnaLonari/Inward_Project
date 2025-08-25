package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import com.api.remitGuru.component.util.Logger;

public class IDFCApiIntegration {
	static com.api.remitGuru.web.controller.SQLRepositoryController repository = new com.api.remitGuru.web.controller.SQLRepositoryController();
	final static String FILENAME = "IDFCApiIntegration.java";
	final static String LOGNAME = "IDFCApiIntegration";
	
	//final static String TXNBASEPATH	= "https://businessuat.singx.co/api";
	//final static String GET_OTP_URL 		= "https://api.aws-uat.idfcfirstbank.com/enterprise-communication-exp/api/v1/otp/generate";
	//final static String GET_OTP_VERF_URL = "https://api.aws-uat.idfcfirstbank.com/enterprise-communication-exp/api/v1/otp/verify";
	
	public int addOTPPool(ArrayList<String> otpData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();	
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();				
		StringBuffer insQry		        = new StringBuffer(); // param for Select query
		ArrayList recordData 			= new ArrayList();
		int insertResult 				= -1;		
		try 
		 {			
				insQry.append(" INSERT into idfcOtpPool (			");
				insQry.append("		groupId,						");  //0	
				insQry.append("		loginId,						");  //1
				insQry.append("		userId,							");	 //2
				insQry.append("		noOfAttempts,					");  //3
				insQry.append("		cnvId,							");	 //4
				insQry.append("		msgId,			  				");  //5
				insQry.append("		extRefId,						");  //6 
				insQry.append("		bizObjId,						");  //7
				insQry.append("		RefNb,							");  //8
				insQry.append("		mobile,							");  //9
				insQry.append("		genResStatusCode,				");  //10
				insQry.append("		apiName,						");  //11
				insQry.append("		eventFlag,						");  //12
				insQry.append("		status, 						");  //13
				insQry.append("		fullRequest, 					");  //14
				insQry.append("		fullResponse, 					");  //15
				insQry.append("		verifyNoOfAttempts, 			");  //16
				insQry.append("		createdBy,  					");  //17
				insQry.append("		createdDate,  					");  //18
				insQry.append("		updatedBy,  					");  //19
				insQry.append("		updatedDate )					");  //20
				insQry.append("		VALUES(							");
				insQry.append("		?,								");  //0
				insQry.append("		?,								");  //1
				insQry.append("		?,								");  //2
				insQry.append("		?,								");  //3
				insQry.append("		?,								");  //4
				insQry.append("		?,								");  //5
				insQry.append("		?,								");  //6 
				insQry.append("		?,								");  //7
				insQry.append("		?,								");  //8
				insQry.append("		?,								");  //9
				insQry.append("		?,								");  //10
				insQry.append("		?,								");  //11
				insQry.append("		?,								");  //12
				insQry.append("		?,								");  //13
				insQry.append("		?,								");  //14
				insQry.append("		?,								");  //15
				insQry.append("		?,								");  //16
				insQry.append("		?,								");  //17
				insQry.append("		getDate(),						");  //18
				insQry.append("		?,								");  //19
				insQry.append(" 	getDate() )						");  //20
				
				
				params.add((String)otpData.get(0));
				paramTypes.add(0);
				params.add((String)otpData.get(1));
				paramTypes.add(0);
				params.add((String)otpData.get(2));
				paramTypes.add(0);
				params.add((String)otpData.get(3));
				paramTypes.add(0);
				params.add((String)otpData.get(4));
				paramTypes.add(0);
				params.add((String)otpData.get(5));
				paramTypes.add(0);
				params.add((String)otpData.get(6));
				paramTypes.add(0);
				params.add((String)otpData.get(7));
				paramTypes.add(0);
				params.add((String)otpData.get(8));
				paramTypes.add(0);
				params.add((String)otpData.get(9));
				paramTypes.add(0);
				params.add((String)otpData.get(10));
				paramTypes.add(0);
				params.add((String)otpData.get(11));
				paramTypes.add(0);
				params.add((String)otpData.get(12));
				paramTypes.add(0);
				params.add((String)otpData.get(13));
				paramTypes.add(0);
				params.add((String)otpData.get(14));
				paramTypes.add(0);
				params.add((String)otpData.get(15));
				paramTypes.add(0);
				params.add((String)otpData.get(16));
				paramTypes.add(0);
				params.add((String)otpData.get(1));
				paramTypes.add(0);
				params.add((String)otpData.get(1));
				paramTypes.add(0);
				
				
				try
				{
					insertResult = repository.executeQuery(insQry, params, paramTypes, (String)otpData.get(0));
					Logger.log(" addOtpData Query Insert"+insQry+"\n params : "+params + "\n paramTypes : "+paramTypes + "\n insertResult : " +insertResult, FILENAME, LOGNAME, null, Logger.INFO);	
				}
				catch(Throwable t)
				{
					Logger.log(" addOtpData QueryInsert  Result  : "+t, FILENAME, LOGNAME, t, Logger.CRITICAL);
					insertResult = -1;
				}
		}
		catch (Throwable t) 
		{
			Logger.log("Error addOtpData arraylist "+t, FILENAME, LOGNAME, t,Logger.CRITICAL);
			insertResult = -1;
		}
		finally
		{
			insQry         = null;
			params         = null;
			paramTypes     = null;
		}
		return insertResult;
	}
	
	public String generateOTPRequest(HashMap<String,String> genOTPRequest) 
	{
		SimpleDateFormat sdfAPI = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+05:30");
		String dateString = sdfAPI.format(Calendar.getInstance().getTime());
		String txtmessg= genOTPRequest.containsKey("otpMessage") && !genOTPRequest.get("otpMessage").isEmpty() ? genOTPRequest.get("otpMessage") : "{OTP} is your OTP to complete your net banking transaction. It is valid for 3 minutes. Please do not share it with anyone. Team IDFC FIRST Bank.";
		
		Logger.log("evantflag : "+genOTPRequest.get("eventFlag"), FILENAME, LOGNAME, null, Logger.INFO);
		
		
		String	otpGenRequestEntity = "{\r\n" + 
				"  \"callId\": \""+genOTPRequest.get("GENOTP_CALLID")+"\",\r\n" + 
				"  \"linkData\": \""+genOTPRequest.get("linkData")+"\",\r\n" + 
				"  \"referenceNumber\": \""+genOTPRequest.get("refNb")+"\",\r\n" + 
				"  \"sendEmail\": {\r\n" + 
				"    \"to\": \""+genOTPRequest.get("emailID")+"\",\r\n" + 
				"    \"subject\": \""+genOTPRequest.get("subject")+"\",\r\n" + 
				"    \"text\": \""+txtmessg+"\",\r\n" + 
				"    \"senderId\": \""+genOTPRequest.get("senderId")+"\"\r\n" + 
				"  }\r\n" + 
				"}"; 
									 
		return otpGenRequestEntity;							 
	}
	
	public String generateVerifyRequest(HashMap<String,String> valOTPRequest) 
	{		
		String otpValRequestEntity = "{\r\n" + 
				"  \"referenceNumber\": \""+valOTPRequest.get("refNb")+"\",\r\n" + 
				"  \"otpValue\": \""+valOTPRequest.get("otp")+"\",\r\n" + 
				"  \"linkData\": \""+valOTPRequest.get("linkData")+"\",\r\n" + 
				"  \"callId\": \""+valOTPRequest.get("GENOTP_CALLID")+"\"\r\n" + 
				"}";		

		return otpValRequestEntity;							 
	}
	
	public String excuteAPI(String jsonRequest,String URL, String method, String token, String source, String corrId, String tranId, HashMap<String,String> reqMap)
	{
		StringBuffer  jsonString = new StringBuffer();
		HttpsURLConnection connection = null;
		try 
		{				
			Logger.log("url : "+URL + "\n jsonRequest : "+jsonRequest+"\n method : "+method+"\n token : "+token+"\n source : "+source+"\n corrId : "+corrId+"\n  tranId : "+tranId, FILENAME, LOGNAME, null, Logger.INFO);
			URL	url = new URL(URL);				
			connection = (HttpsURLConnection) url.openConnection();
			
			if(!token.equals(""))
				connection.setRequestProperty("Authorization", "Basic " + token);
			if(!source.equals(""))
				connection.setRequestProperty("Source", source);//"PBLINE"
			if(!corrId.equals(""))
				connection.setRequestProperty("correlationId", corrId);//"12345678"
			if(!tranId.equals(""))
				connection.setRequestProperty("transactionId", tranId);//"12345678"
			
			connection.setRequestProperty("content-type", "application/json; charset=UTF-8");			
			connection.setRequestProperty("Accept", "*/*");
			connection.setDoOutput(true);
		
			
			// Send request
			if (method.equals("POST")) {
				connection.setDoOutput(true);
				connection.setRequestMethod(method);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(jsonRequest);
				wr.flush();
				wr.close();
			}
			
			int responseCode = connection.getResponseCode();	
			BufferedReader br = null ;
			
			Logger.log("responseCode : " + responseCode,FILENAME, LOGNAME,null,Logger.INFO);

			// Get Response
			InputStream is = null;
			if (connection.getResponseCode() >= 400) {
				is = connection.getErrorStream();
			} else {
				is = connection.getInputStream();
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = rd.readLine()) != null) {
				jsonString.append(line);
				jsonString.append("\r");
			}
			rd.close();
			
			ArrayList<String> otpData = new ArrayList<>();

			otpData.add(reqMap.get("groupId") != null ? reqMap.get("groupId").toString() : "");     // 0
			otpData.add(reqMap.get("loginId") != null ? reqMap.get("loginId").toString() : "");     // 1
			otpData.add("userId");     // 2 (no null check needed since it's hardcoded)
			otpData.add("");     // 3 (hardcoded empty string)
			otpData.add(reqMap.get("cnvId") != null ? reqMap.get("cnvId").toString() : "");         // 4
			otpData.add(reqMap.get("msgId") != null ? reqMap.get("msgId").toString() : "");         // 5
			otpData.add(reqMap.get("extRefId") != null ? reqMap.get("extRefId").toString() : "");   // 6
			otpData.add(reqMap.get("bizObjId") != null ? reqMap.get("bizObjId").toString() : "");   // 7
			otpData.add(reqMap.get("regAccountNo") != null ? reqMap.get("regAccountNo").toString() : ""); // 8
			otpData.add(reqMap.get("mobile") != null ? reqMap.get("mobile").toString() : "");       // 9
			otpData.add(""+responseCode);  // 10 (checking for null responseCode)
			otpData.add(reqMap.get("apiName") != null ? reqMap.get("apiName").toString() : "");     // 11
			otpData.add(reqMap.get("eventFlag") != null ? reqMap.get("eventFlag").toString() : ""); // 12
			if(responseCode >= 400)
			{
				otpData.add("Exception");  //13
			}
			else
			{
				otpData.add(""+responseCode);  //13
			}
			otpData.add(jsonRequest);					//14
			otpData.add(jsonString.toString());				    //15
			otpData.add("0");//16
			
			Logger.log("addOTPPool otpData : " + otpData,FILENAME, LOGNAME,null,Logger.INFO);

			int result2 = -1;
			try
			{
				result2 = addOTPPool(otpData);
				Logger.log("addOTPPool result : " + result2,FILENAME, LOGNAME,null,Logger.INFO);

			}
			catch(Throwable t)
			{	
				result2 = -1;
				Logger.log("Error addOTPPool otp_pool : "+t.getMessage(), FILENAME, LOGNAME, t, Logger.CRITICAL);
			}
		}
		catch(Throwable t)
		{
			Logger.log("Error in gettting excuteAPI : " + t.getMessage(),FILENAME, LOGNAME,t,Logger.CRITICAL);
			jsonString = new StringBuffer("");
		}
		finally {
			if (connection != null)
				connection.disconnect();
		}
		Logger.log("Final Response : " + jsonString.toString(),FILENAME, LOGNAME,null,Logger.INFO);
		return jsonString.toString();
	}
	
	
	
	public HashMap<String,String> callGenerateOTPAPI(HashMap<String,String> reqMap){
		
		String genOTPReqStr="", apiResStr="", tranId="";
		HashMap<String,String> resMap	=	new HashMap<String,String>();
		
		Logger.log("reqMap : " + reqMap,FILENAME, LOGNAME,null,Logger.INFO);
		
		
		String GET_OTP_URL = EnvProperties.getPropertyValue("IDFC_OTP_GEN_API_URL") == null ? "https://api.aws-uat.idfcfirstbank.com/enterprise-communication-exp/api/v1/otp/generate" : EnvProperties.getPropertyValue("IDFC_OTP_GEN_API_URL");
		String Client_ID = EnvProperties.getPropertyValue("DRI_UCIC_CLIENT_ID") == null ? "2bc1d89f-41e3-43c4-ab51-f18aa005f504" : EnvProperties.getPropertyValue("DRI_UCIC_CLIENT_ID");
		String Client_Secrete = EnvProperties.getPropertyValue("DRI_UCIC_CLIENT_SECRET") == null ? "ycEND1RRf1WNT5CTzdCmL~bQwZ" : EnvProperties.getPropertyValue("DRI_UCIC_CLIENT_SECRET");
		String Api_Source = EnvProperties.getPropertyValue("Api_Source") == null ? "REMIT" : EnvProperties.getPropertyValue("Api_Source");
		String Api_CorrId = EnvProperties.getPropertyValue("Api_CorrId") == null ? "e0c4a3c2-d29d-11eb-b8bc-0242ac130003" : EnvProperties.getPropertyValue("Api_CorrId");

		tranId	=	reqMap.containsKey("transactionId") && !reqMap.get("transactionId").isEmpty() ?	reqMap.get("transactionId") : "";
		
		String apiToken = "";
		if(!Client_ID.equals("") && !Client_Secrete.equals(""))
		{
		 String auth = Client_ID + ":" + Client_Secrete;
		 
		 byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
		 apiToken = new String(encodedAuth);
		}
		
		Logger.log("apiToken : " + apiToken,FILENAME, LOGNAME,null,Logger.INFO);

		
		genOTPReqStr = generateOTPRequest(reqMap);
		
		Logger.log("genOTPReqStr : " + genOTPReqStr,FILENAME, LOGNAME,null,Logger.INFO);

		if(!genOTPReqStr.isEmpty() && !apiToken.isEmpty())
			apiResStr = excuteAPI(genOTPReqStr, GET_OTP_URL, "POST", apiToken,Api_Source,Api_CorrId,tranId, reqMap);
		
		resMap.put("fullResponse", apiResStr);
		
		
		Logger.log("Final Response : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);

		return resMap;
	}

	public HashMap<String,String> callVerifyOTPAPI(HashMap<String,String> reqMap){
		
		String verifyOTPReqStr="", apiResStr="", tranId="";
		HashMap<String,String> resMap	=	new HashMap<String,String>();
		
		Logger.log("reqMap : " + reqMap,FILENAME, LOGNAME,null,Logger.INFO);
		
		
		String GET_OTP_VERF_URL = EnvProperties.getPropertyValue("IDFC_OTP_VERIFY_API_URL") == null ? "https://api.aws-uat.idfcfirstbank.com/enterprise-communication-exp/api/v1/otp/verify" : EnvProperties.getPropertyValue("IDFC_OTP_VERIFY_API_URL");
		String Client_ID = EnvProperties.getPropertyValue("Client_ID") == null ? "2bc1d89f-41e3-43c4-ab51-f18aa005f504" : EnvProperties.getPropertyValue("Client_ID");
		String Client_Secrete = EnvProperties.getPropertyValue("Client_Secrete") == null ? "ycEND1RRf1WNT5CTzdCmL~bQwZ" : EnvProperties.getPropertyValue("Client_Secrete");
		String Api_Source = EnvProperties.getPropertyValue("Api_Source") == null ? "REMIT" : EnvProperties.getPropertyValue("Api_Source");
		String Api_CorrId = EnvProperties.getPropertyValue("Api_CorrId") == null ? "e0c4a3c2-d29d-11eb-b8bc-0242ac130003" : EnvProperties.getPropertyValue("Api_CorrId");
		
		
		String apiToken = "";
		if(!Client_ID.equals("") && !Client_Secrete.equals(""))
		{
			String auth = Client_ID + ":" + Client_Secrete;
			
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
			apiToken = new String(encodedAuth);
		}
		
		Logger.log("apiToken : " + apiToken,FILENAME, LOGNAME,null,Logger.INFO);
		
		
		verifyOTPReqStr = generateVerifyRequest(reqMap);
		
		Logger.log("genOTPReqStr : " + verifyOTPReqStr,FILENAME, LOGNAME,null,Logger.INFO);
		
		if(!verifyOTPReqStr.isEmpty() && !apiToken.isEmpty())
			apiResStr = excuteAPI(verifyOTPReqStr, GET_OTP_VERF_URL, "POST", apiToken,Api_Source,Api_CorrId,tranId, reqMap);
		
		resMap.put("fullResponse", apiResStr);
		
		
		Logger.log("callVerifyOTPAPI :: Final Response : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);
		
		return resMap;
	}
	
public HashMap<String,String> callVerifySMSOTPAPI(HashMap<String,String> reqMap){
		
		String verifyOTPReqStr="", apiResStr="", tranId="";
		HashMap<String,String> resMap	=	new HashMap<String,String>();
		
		Logger.log("reqMap : " + reqMap,FILENAME, LOGNAME,null,Logger.INFO);
		
		
		String GET_OTP_VERF_URL = EnvProperties.getPropertyValue("GET_OTP_VERF_URL") == null ? "" : EnvProperties.getPropertyValue("GET_OTP_VERF_URL");
		String Client_ID = EnvProperties.getPropertyValue("Client_ID") == null ? "2bc1d89f-41e3-43c4-ab51-f18aa005f504" : EnvProperties.getPropertyValue("Client_ID");
		String Client_Secrete = EnvProperties.getPropertyValue("Client_Secrete") == null ? "ycEND1RRf1WNT5CTzdCmL~bQwZ" : EnvProperties.getPropertyValue("Client_Secrete");
		String Api_Source = EnvProperties.getPropertyValue("Api_Source") == null ? "REMIT" : EnvProperties.getPropertyValue("Api_Source");
		String Api_CorrId = EnvProperties.getPropertyValue("Api_CorrId") == null ? "e0c4a3c2-d29d-11eb-b8bc-0242ac130003" : EnvProperties.getPropertyValue("Api_CorrId");
		
		
		String apiToken = "";
		if(!Client_ID.equals("") && !Client_Secrete.equals(""))
		{
			String auth = Client_ID + ":" + Client_Secrete;
			
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
			apiToken = new String(encodedAuth);
		}
		
		Logger.log("apiToken : " + apiToken,FILENAME, LOGNAME,null,Logger.INFO);
		
		
		verifyOTPReqStr = generateVerifyRequest(reqMap);
		
		Logger.log("genOTPReqStr : " + verifyOTPReqStr,FILENAME, LOGNAME,null,Logger.INFO);
		
		if(!verifyOTPReqStr.isEmpty() && !apiToken.isEmpty())
			apiResStr = excuteAPI(verifyOTPReqStr, GET_OTP_VERF_URL, "POST", apiToken,Api_Source,Api_CorrId,tranId, reqMap);
		
		resMap.put("fullResponse", apiResStr);
		
		
		Logger.log("callVerifyOTPAPI :: Final Response : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);
		
		return resMap;
	}
	
public HashMap<String,String> callGenerateSMSOTPAPI(HashMap<String,String> reqMap){
	
	String genOTPReqStr="", apiResStr="", tranId="";
	HashMap<String,String> resMap	=	new HashMap<String,String>();
	
	Logger.log("reqMap : " + reqMap,FILENAME, LOGNAME,null,Logger.INFO);
	
	
	String GET_OTP_URL = EnvProperties.getPropertyValue("GET_OTP_URL") == null ? "" : EnvProperties.getPropertyValue("GET_OTP_URL");
	String Client_ID = EnvProperties.getPropertyValue("Client_ID") == null ? "2bc1d89f-41e3-43c4-ab51-f18aa005f504" : EnvProperties.getPropertyValue("Client_ID");
	String Client_Secrete = EnvProperties.getPropertyValue("Client_Secrete") == null ? "ycEND1RRf1WNT5CTzdCmL~bQwZ" : EnvProperties.getPropertyValue("Client_Secrete");
	String Api_Source = EnvProperties.getPropertyValue("Api_Source") == null ? "REMIT" : EnvProperties.getPropertyValue("Api_Source");
	String Api_CorrId = EnvProperties.getPropertyValue("Api_CorrId") == null ? "e0c4a3c2-d29d-11eb-b8bc-0242ac130003" : EnvProperties.getPropertyValue("Api_CorrId");
	
	tranId	=	reqMap.containsKey("transactionId") && !reqMap.get("transactionId").isEmpty() ?	reqMap.get("transactionId") : "";
	
	String apiToken = "";
	if(!Client_ID.equals("") && !Client_Secrete.equals(""))
	{
		String auth = Client_ID + ":" + Client_Secrete;
		
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
		apiToken = new String(encodedAuth);
	}
	
	Logger.log("apiToken : " + apiToken,FILENAME, LOGNAME,null,Logger.INFO);
	
	
	genOTPReqStr = generateSMSOTPRequest(reqMap);
	
	Logger.log("genOTPReqStr : " + genOTPReqStr,FILENAME, LOGNAME,null,Logger.INFO);
	
	if(!genOTPReqStr.isEmpty() && !apiToken.isEmpty())
		apiResStr = excuteAPI(genOTPReqStr, GET_OTP_URL, "POST", apiToken,Api_Source,Api_CorrId,tranId, reqMap);
	
	resMap.put("fullResponse", apiResStr);
	
	
	Logger.log("Final Response : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);
	
	return resMap;
}

public String generateSMSOTPRequest(HashMap<String,String> genOTPRequest) 
{
	SimpleDateFormat sdfAPI = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+05:30");
	String dateString = sdfAPI.format(Calendar.getInstance().getTime());
	String txtmessg= genOTPRequest.containsKey("otpMessage") && !genOTPRequest.get("otpMessage").isEmpty() ? genOTPRequest.get("otpMessage") : "{OTP} is your OTP to complete your net banking transaction. It is valid for 3 minutes. Please do not share it with anyone. Team IDFC FIRST Bank.";
	
	Logger.log("evantflag : "+genOTPRequest.get("eventFlag"), FILENAME, LOGNAME, null, Logger.INFO);
	
	
	String	otpGenRequestEntity = "{\r\n" + 
			"  \"callId\": \""+genOTPRequest.get("GENOTP_CALLID")+"\",\r\n" + 
			"  \"linkData\": \""+genOTPRequest.get("linkData")+"\",\r\n" + 
			"  \"referenceNumber\": \""+genOTPRequest.get("refNb")+"\",\r\n" + 
			"  \"sendSms\": {\r\n" + 
			"    \"mobileNumber\": "+genOTPRequest.get("cstMobile")+",\r\n" + 
			"    \"division\": \""+genOTPRequest.get("GENOTP_DIV")+"\",\r\n" + 
			"    \"subDivisionId\": \""+genOTPRequest.get("GENOTP_SUB_DIV")+"\",\r\n" + 
			"    \"text\": \""+txtmessg+"\"\r\n" + 
			"  }\r\n" + 
			"}"; 
	
	return otpGenRequestEntity;							 
}
	
	public static void main(String[] args) {
		
		String	GENOTP_DIV 		= "536";
		String	GENOTP_SUB_DIV 	= "OTPALERTS";
		String	GENOTP_CALLID 	= "DIGIREMIT_INWARD_UAT";
				
		HashMap<String,String> genOTPRequest = new HashMap<String,String>();

		java.security.SecureRandom digi = new java.security.SecureRandom();
		int uniID = digi.nextInt(100000);
		String cnvId = String.valueOf(uniID);
		String msgId = String.valueOf(uniID);
		String extRefId = String.valueOf(uniID);
		String bizObjId = String.valueOf(uniID);


		HashMap<String, String> reqMap 	= 	new HashMap<>();
		HashMap<String,String> resMap	=	new HashMap<String, String>();

		reqMap.put("groupId", "IDFCIN");
		reqMap.put("loginId", "");
		reqMap.put("cnvId", cnvId);
		reqMap.put("msgId",msgId);
		reqMap.put("extRefId", extRefId);
		reqMap.put("bizObjId", bizObjId);
		reqMap.put("regAccountNo", "");
		reqMap.put("emailID", "ram@yopmail.com");
		reqMap.put("apiName", "GENOTP");
		reqMap.put("eventFlag", "TXN");
		reqMap.put("GENOTP_CALLID", GENOTP_CALLID);
		reqMap.put("linkData", "1725534480421");
		reqMap.put("refNb", "1725534480421");
		reqMap.put("subject", "testing email OTP");
		//reqMap.put("senderId", "alerts@idfcbanktest.com"); 
		reqMap.put("senderId", "alerts@idfcfirstbank.com");//change given by Sidhant on 11Dec2024 on Email

		IDFCApiIntegration idfcapi= new IDFCApiIntegration();
		 
		resMap	= idfcapi.callGenerateOTPAPI(reqMap);
		 
		 //resMap	= callGenerateOTPAPI(reqMap);

		Logger.log("callGenerateOTPAPI :: resMap : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);

		if(false) {
//		Call Verify OTP-->
		reqMap 	= 	new HashMap<>();
		resMap	=	new HashMap<String, String>();
		
		reqMap.put("groupId", "IDFCIN");
		reqMap.put("loginId", "");
		reqMap.put("cnvId", cnvId);
		reqMap.put("msgId",msgId);
		reqMap.put("extRefId", extRefId);
		reqMap.put("bizObjId", bizObjId);
		reqMap.put("otp", "123456");// change OTP value
		reqMap.put("apiName", "VERIFYOTP");
		reqMap.put("eventFlag", "TXN");
		reqMap.put("GENOTP_CALLID", GENOTP_CALLID);
		reqMap.put("linkData", "1725534480421");
		reqMap.put("refNb", "1725534480421");
		
		resMap	= idfcapi.callVerifyOTPAPI(reqMap);
		Logger.log("callVerifyOTPAPI :: resMap : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);

		
//		Call Send SMS OTP API -->
		
		reqMap 	= 	new HashMap<>();
		resMap	=	new HashMap<String, String>();
		
		reqMap.put("groupId", "IDFCIN");
		reqMap.put("loginId", "");
		reqMap.put("cnvId", cnvId);
		reqMap.put("msgId",msgId);
		reqMap.put("extRefId", extRefId);
		reqMap.put("bizObjId", bizObjId);
		reqMap.put("regAccountNo", "");
		reqMap.put("mobile","441231231231");
		reqMap.put("cstMobile","441231231231");		
		reqMap.put("apiName", "GENSMSOTP");
		reqMap.put("eventFlag", "TXN");
		genOTPRequest.put("GENOTP_DIV",GENOTP_DIV);
		genOTPRequest.put("GENOTP_SUB_DIV",GENOTP_SUB_DIV);
		reqMap.put("GENOTP_CALLID", GENOTP_CALLID);
		reqMap.put("linkData", "1725534480421");
		reqMap.put("refNb", "1725534480421");

//		IDFCAPI idfcapi= new IDFCAPI();
		 
		resMap	= idfcapi.callGenerateSMSOTPAPI(reqMap);
		
		Logger.log("callGenerateSMSOTPAPI :: resMap : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);

//		Call Verify SMS OTP-->
		reqMap 	= 	new HashMap<>();
		resMap	=	new HashMap<String, String>();
		
		reqMap.put("groupId", "IDFCIN");
		reqMap.put("loginId", "");
		reqMap.put("cnvId", cnvId);
		reqMap.put("msgId",msgId);
		reqMap.put("extRefId", extRefId);
		reqMap.put("bizObjId", bizObjId);
		reqMap.put("otp", "123456");// change OTP value
		reqMap.put("apiName", "VERIFYOTP");
		reqMap.put("eventFlag", "TXN");
		reqMap.put("GENOTP_CALLID", GENOTP_CALLID);
		reqMap.put("linkData", "1725534480421");
		reqMap.put("refNb", "1725534480421");
		
		resMap	= idfcapi.callVerifySMSOTPAPI(reqMap);
		Logger.log("callVerifySMSOTPAPI :: resMap : " + resMap,FILENAME, LOGNAME,null,Logger.INFO);

	
	}
	
	}
	
}
