/*
 * @(#)SMSPool.java	21-JUN-2012
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenues*******.
 * Use is subject to license terms.
 *
 */
package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONObject;

import com.api.remitGuru.web.controller.RGUserController;

public class SMSPool {



	/**
	 *	Default no parameter constructor
	 */
	public SMSPool(){

	}



	/**
	 * This method is to generate the sms in specified format
	 * and update the SMS_POOL table for the send_flag
	 *
	 */

	public int generateSMS(String groupId)
	{
		String smsId="";
		String smsTo="";
		String smsFrom="";
		String smsText="";
		String smsProvider="";
		String result="";
		int output = 0 ;
		RGUserController rgUser = new RGUserController();

		ArrayList smsData=null;
		try
		{
			smsData= rgUser.getSMSData(groupId);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getting data from getSMSData. ", "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
		}

		if(smsData != null && smsData.size() > 0)
		{
			for(int i=0; i<smsData.size(); i++)
			{
				smsId = ((String [])smsData.get(i))[1];
				smsTo = (((String [])smsData.get(i))[2]).replaceAll("-","");
				smsFrom = ((String [])smsData.get(i))[3];
				smsText = ((String [])smsData.get(i))[4];
				smsProvider = ((String [])smsData.get(i))[5];

				result = smsSend(smsId,smsTo,smsFrom,smsText,smsProvider,groupId);


            	if(result!= null && (result.contains("Platform Accepted") || result.contains("Sent.") || result.contains("SUCCESS")))
				{
					try
					{
						output = rgUser.updateSmsPool(result,smsId,"Y",groupId);
					}
					catch(Throwable t)
					{
						output = -1;
						Logger.log("SMSPool.java  -->  generateSMS()  : unable Update the send flags to Y : smsId : " + smsId, "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
					}

				}
				else
				{
					try
					{
						output = rgUser.updateSmsPool(result,smsId,"F",groupId);
					}
					catch(Throwable t)
					{
						output = -1;
						Logger.log("SMSPool.java  -->  generateSMS()  : unable Update the send flags to F: smsId : " + smsId, "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
					}

				}

			}
		}
		return output;
	}


	/*
	* This method will actually send the SMS corresponding to the service provider.
	*/
	public String smsSend(String smsId,String smsTo,String smsFrom,String smsText,String smsProvider, String groupId)
	{
		String URLString = null;
		ArrayList smsProviderDtls = new ArrayList();
		RGUserController rgUser = new RGUserController();
		int responseCode  = 0;
		String responseMsg = "",responseHdr = "";
		String responseStr = "";

		try
		{
			smsProviderDtls = rgUser.getSmsProviderDtls(smsProvider, "", groupId);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getting sms provider details for sms provider id " + smsProvider, "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
		}

		if(smsProviderDtls != null && smsProviderDtls.size() > 0)
		{
			String smsPostingURL = ((String[])smsProviderDtls.get(0))[3];
			String smsUserId = ((String[])smsProviderDtls.get(0))[1];
			String smsUserPassword = ((String[])smsProviderDtls.get(0))[2];
			String smsResponseURL = ((String[])smsProviderDtls.get(0))[5];
			String smsProviderName = ((String[])smsProviderDtls.get(0))[0];
			String smsUdh = "0";
			String dlr_mask = "19";
			String dlr_url = smsResponseURL+smsId;
			
			String smsApiSrc = EnvProperties.getPropertyValue("smsApiSrc") == null ? "" :	EnvProperties.getPropertyValue("smsApiSrc"); // IDFCBK
			String smsApiSubDvsnId = EnvProperties.getPropertyValue("smsApiSubDvsnId") == null ? "" : EnvProperties.getPropertyValue("smsApiSubDvsnId"); //CRMNEXT
			String smsApiDvsn = EnvProperties.getPropertyValue("smsApiDvsn") == null ? "" : EnvProperties.getPropertyValue("smsApiDvsn"); //2943
			
			if(smsTo.equals("448280839093"))
			{
				smsTo = "918280839093";
			}
			
			try
			{
				//URLString = "&to=" + URLEncoder.encode(smsTo) + "&udh=" + smsUdh + "&from=" + URLEncoder.encode(smsFrom) + "&text=" + URLEncoder.encode(smsText);
				
				URLString="{\r\n" + 
						"  \"sendSMSReq\":{\r\n" + 
						"    \"msgHdr\":{\r\n" + 
						"      \"msgId\":\"121256\",\r\n" + 
						"      \"cnvId\":\"12345\",\r\n" + 
						"      \"extRefId\":\"12345\",\r\n" + 
						"      \"bizObjId\":\"121\",\r\n" + 
						"      \"appId\":\"REM\",\r\n" + 
						"      \"timestamp\":\"2019-OCT-03:18:58:00\"\r\n" + 
						"    },\r\n" + 
						"    \"msgBdy\":{\r\n" + 
						"      \"mbNb\":\""+smsTo+"\",\r\n" + 
						"      \"txt\":\""+smsText+"\",\r\n" + 
						"      \"src\":\""+smsApiSrc+"\",\r\n" + 
						"      \"typ\":\"Alert\",\r\n" + 
						"      \"dvsn\":\""+smsApiDvsn+"\",\r\n" + 
						"      \"subDvsnId\":\""+smsApiSubDvsnId+"\"\r\n" + 
						"     \r\n" + 
						"    }\r\n" + 
						"  }\r\n" + 
						"}";
				
				
				
				
				Logger.log("postURLSMS postURL : " + smsPostingURL + " URLString : " + URLString, "SMSPool.java", "SMSPool", null, Logger.INFO);
				URL url = new URL(smsPostingURL);
				 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				 connection.setRequestProperty("content-type", "application/json; charset=UTF-8");
				 connection.setDoOutput(true);
			     connection.setRequestProperty("Accept", "*/*");
			     
			     OutputStream writer = connection.getOutputStream();
			     writer.write(URLString.getBytes());
			     writer.flush();
			     writer.close();

	            StringBuffer sbResponse = new StringBuffer();
	            responseCode = connection.getResponseCode();
	            BufferedReader	input = null;
	            Logger.log("SMS Response Code : " + responseCode, "SMSPool.java", "SMSPool", null, Logger.INFO);
	            if (responseCode == HttpURLConnection.HTTP_OK) // success
	            {
	                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String inputLine;

	                while ((inputLine = input.readLine()) != null)
	                {
	                    sbResponse.append(inputLine);
	                }
	                responseStr = input.readLine();
	                input.close();
	            }
	            
	            responseMsg = connection.getResponseMessage();
	            responseStr = sbResponse.toString();
	            JSONObject object = new JSONObject(responseStr);
	            responseHdr = object.getJSONObject("sendSMSRes").getJSONObject("msgHdr").getString("rslt");
	            if(responseHdr.equalsIgnoreCase("OK"))
	            {
	            	responseMsg = object.getJSONObject("sendSMSRes").getJSONObject("msgBdy").getString("sts");
	            }
	            Logger.log("SMS Api Response : " + responseStr +"SMS Response Hdr : " + responseHdr + "\n Response msg" + responseMsg, "SMSPool.java", "SMSPool", null, Logger.INFO);
				
			}
			catch (Exception e)
			{
				// TODO: handle exception
				Logger.log("Exception at SMS : " + e, "SMSPool.java", "SMSPool", e, Logger.INFO);
			}
		}
		else
		{
			Logger.log("Error No sms provider details found for sms provider id " + smsProvider, "SMSPool.java", "SMSPool", null, Logger.CRITICAL);
		}

		return responseMsg;
	}

	public String[] postURLSMS(String postURL, String URLString)
	{
		String responseStrArray[] = new String[3];
		int responseCode  = 0;
		String responseMsg = "";
		String responseStr = "";

		try
		{
			Logger.log("postURLSMS postURL : " + postURL + " URLString : " + URLString, "SMSPool.java", "SMSPool", null, Logger.INFO);

			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			OutputStream printout = null;
	        BufferedReader	input = null;
	        URL url = new URL(postURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        //System.out.println("connection opened::");
	        //conn.setRequestMethod("GET"); //GET is set by default, but using this method we can change it to POST.

            StringBuffer sbResponse = new StringBuffer();
            conn.setRequestMethod("POST");

			// Sets the value of the doOutput field for this URLConnection to the specified value. Invoking the method setDoOutput(true) on the URLConnection, will always use the POST method.
            conn.setDoOutput(true);
			// No caching. Sets the value of the useCaches field of this URLConnection to the specified value.
			conn.setUseCaches(false);
			// Specify the content type. Sets the general request property
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            printout = conn.getOutputStream();
            printout.write(URLString.getBytes());
            printout.flush();
            printout.close();

            responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) // success
            {
                input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;

                while ((inputLine = input.readLine()) != null)
                {
                    sbResponse.append(inputLine);
                }
                responseStr = input.readLine();
                input.close();
            }
            responseMsg = conn.getResponseMessage();
            responseStr = sbResponse.toString();
            // System.out.println(sbResponse.toString());
			responseStrArray[0] = responseCode + "";
			responseStrArray[1] = responseMsg;
			responseStrArray[2] = responseStr;

			Logger.log("postURLSMS " + responseStrArray[0]+"\t"+responseStrArray[1]+"\t"+responseStrArray[2], "SMSPool.java", "SMSPool", null, Logger.INFO);
			System.out.println("SMS : " + responseStrArray[0]+"\t"+responseStrArray[1]+"\t"+responseStrArray[2]);
		}
		catch(Throwable t)
		{
			Logger.log("Error in postURLSMS." , "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
		}
		return responseStrArray;
	}
	

	public String postSMS(String smsPostingURL,String smsTo, String smsText)
	{
		String URLString = "";
		int responseCode  = 0;
		String responseMsg = "",responseHdr = "";
		String responseStr = "";
		
		String smsApiSrc = EnvProperties.getPropertyValue("smsApiSrc") == null ? "" :	EnvProperties.getPropertyValue("smsApiSrc"); // IDFCBK
		String smsApiSubDvsnId = EnvProperties.getPropertyValue("smsApiSubDvsnId") == null ? "" : EnvProperties.getPropertyValue("smsApiSubDvsnId"); //CRMNEXT
		String smsApiDvsn = EnvProperties.getPropertyValue("smsApiDvsn") == null ? "" : EnvProperties.getPropertyValue("smsApiDvsn"); //2943
		
		if(smsTo.equals("448280839093"))
		{
			smsTo = "918280839093";
		}
		
		try
		{
			URLString="{\r\n" + 
					"  \"sendSMSReq\":{\r\n" + 
					"    \"msgHdr\":{\r\n" + 
					"      \"msgId\":\"121256\",\r\n" + 
					"      \"cnvId\":\"12345\",\r\n" + 
					"      \"extRefId\":\"12345\",\r\n" + 
					"      \"bizObjId\":\"121\",\r\n" + 
					"      \"appId\":\"REM\",\r\n" + 
					"      \"timestamp\":\"2019-OCT-03:18:58:00\"\r\n" + 
					"    },\r\n" + 
					"    \"msgBdy\":{\r\n" + 
					"      \"mbNb\":\""+smsTo+"\",\r\n" + 
					"      \"txt\":\""+smsText+"\",\r\n" + 
					"      \"src\":\""+smsApiSrc+"\",\r\n" + 
					"      \"typ\":\"Alert\",\r\n" + 
					"      \"dvsn\":\""+smsApiDvsn+"\",\r\n" + 
					"      \"subDvsnId\":\""+smsApiSubDvsnId+"\"\r\n" + 
					"     \r\n" + 
					"    }\r\n" + 
					"  }\r\n" + 
					"}";
			
			Logger.log("postURLSMS postURL : " + smsPostingURL + " URLString : " + URLString, "SMSPool.java", "SMSPool", null, Logger.INFO);
			URL url = new URL(smsPostingURL);
			 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			 connection.setRequestProperty("content-type", "application/json; charset=UTF-8");
			 connection.setDoOutput(true);
		     connection.setRequestProperty("Accept", "*/*");
		     
		     OutputStream writer = connection.getOutputStream();
		     writer.write(URLString.getBytes());
		     writer.flush();
		     writer.close();

            StringBuffer sbResponse = new StringBuffer();
            responseCode = connection.getResponseCode();
            BufferedReader	input = null;
            Logger.log("SMS Response Code : " + responseCode, "SMSPool.java", "SMSPool", null, Logger.INFO);
            if (responseCode == HttpURLConnection.HTTP_OK) // success
            {
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = input.readLine()) != null)
                {
                    sbResponse.append(inputLine);
                }
                responseStr = input.readLine();
                input.close();
            }
            
            responseMsg = connection.getResponseMessage();
            responseStr = sbResponse.toString();
            JSONObject object = new JSONObject(responseStr);
            responseHdr = object.getJSONObject("sendSMSRes").getJSONObject("msgHdr").getString("rslt");
            if(responseHdr.equalsIgnoreCase("OK"))
            {
            	responseMsg = object.getJSONObject("sendSMSRes").getJSONObject("msgBdy").getString("sts");
            }
            Logger.log("SMS Api Response : " + responseStr +"SMS Response Hdr : " + responseHdr + "\n Response msg" + responseMsg, "SMSPool.java", "SMSPool", null, Logger.INFO);
			
		}
		catch (Exception e)
		{
			// TODO: handle exception
			Logger.log("Exception at SMS : " + e, "SMSPool.java", "SMSPool", e, Logger.INFO);
		}
		return responseMsg;
	}

	
	public int sendInstantSms(String smsTo, String smsText,String groupId,String userId)// change params to arraylist
	{
		
		ArrayList smsProviderDtls = new ArrayList();
		RGUserController rgUser = new RGUserController();
		String smsProviderId = "";
		String smsFrom = "IDFCIn";
		smsTo = smsTo.replaceAll("-","");

		
		RGSMS rgSms = new RGSMS();
		String responseMsg = "";
		String sendFlag="F";
		ArrayList smsData = new ArrayList();
		int smsResult = -1;

		try
		{
			smsProviderDtls = rgUser.getSmsProviderDtls("", "", groupId);//rgUser
		}
		catch(Throwable t)
		{
			Logger.log("Error in getting sms provider details for sms provider id " + smsProviderId, "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
		}

		if(smsProviderDtls != null && smsProviderDtls.size() > 0)
		{
			String smsPostingURL = ((String[])smsProviderDtls.get(0))[3];
			String smsUserId = ((String[])smsProviderDtls.get(0))[1];
			String smsUserPassword = ((String[])smsProviderDtls.get(0))[2];
			String smsResponseURL = ((String[])smsProviderDtls.get(0))[5];
			smsProviderId = ((String[])smsProviderDtls.get(0))[6];

			Logger.log( "smsProviderId :" + smsProviderId, "SMSPool.java", "SMSPool", null, Logger.INFO);

			try
			{
				responseMsg = postSMS(smsPostingURL, smsTo, smsText);
			}
			catch(Throwable t)
			{
				Logger.log("Error occurred in postURLSMS", "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
			}
		}
		else
		{
			Logger.log("No sms provider details found for sms provider id " + smsProviderId , "SMSPool.java", "SMSPool", null, Logger.CRITICAL);
		}

		
	 	if(responseMsg!= null && (responseMsg.contains("Platform Accepted") || responseMsg.contains("Sent.") || responseMsg.contains("SUCCESS")))
		{
			sendFlag = "Y";
		}
		else
		{
			sendFlag = "F";
		}

		/* Add in SMS Pool with send flag as Sent : Y or Fail : N. This is for audit purpose. */
		try
		{
			smsData.add(groupId);				//0 groupId
			smsData.add(userId);				//userId
			smsData.add(sendFlag);				//sendFlag
			smsData.add(smsTo);					//smsTo
			smsData.add(smsFrom);				//smsFrom
			smsData.add(smsText);				//msg
			smsData.add("");					//eventId
			smsData.add(responseMsg);		//response
			smsData.add(smsProviderId);			//smsProviderId

			smsResult = rgSms.addSMSPool(smsData);
		}
		catch(Throwable t)
		{
			Logger.log("Error calling addSMSPool", "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
		}
		finally
		{
			sendFlag = null;
			smsData = null;
		}

		Logger.log("Response for mobile no " + smsTo + " : " + responseMsg + "\nSms pool id : " + smsResult, "SMSPool.java", "SMSPool", null, Logger.INFO);

		/* Sms_pool add End*/
		return smsResult;
	}
	
	public void executeSMSPool()
	{
		try
		{
			 String stopSMSDaemon = EnvProperties.getPropertyValue("SMS_DAEMON_ACTIVE") == null ? "" : EnvProperties.getPropertyValue("SMS_DAEMON_ACTIVE");
			 Logger.log("SMS_DAEMON_ACTIVE: " + stopSMSDaemon, "MailPool.java", "MailPool", null, Logger.INFO);
			 if(stopSMSDaemon.equalsIgnoreCase("Y"))
			 {
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("EEEE-MMMM-dd-yyyy HH:mm:ss");
				java.util.Date currdt = new java.util.Date();
				String datetime = bartDateFormat.format(currdt);
				Logger.log("Daemon started at : " + datetime, "SMSPool.java", "SMSPool", null, Logger.INFO);
				SMSPool sms = new SMSPool();
				String activeGroupIds = EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST") == null ? "" : EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST");//",RG,ONECARD,YR,AR,RXM,";
				
				if(!activeGroupIds.equals(""))
				{
					String [] grpArr = activeGroupIds.split(",");
					for(int i=1 ; i < grpArr.length; i++)
					{
						Logger.log(i + " : Daemon Started for Group Id : " + (String)grpArr[i], "SMSPool.java", "SMSPool", null, Logger.INFO);
						sms.generateSMS((String)grpArr[i]);
						Logger.log(i + " : Daemon End for Group Id : " + (String)grpArr[i], "SMSPool.java", "SMSPool", null, Logger.INFO);
					}
				}
			 }
		}
		catch(Throwable t)
		{
			Logger.log("Error in starting SMS Daemon ", "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
		}
	}


	/**
	 * This main method is to start the daemon
	 *
	 */
	public static void main(String [] args)
	{
		while(true)
		{
			try
			{
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("EEEE-MMMM-dd-yyyy HH:mm:ss");
				java.util.Date currdt = new java.util.Date();

				String datetime = bartDateFormat.format(currdt);

				Logger.log("Daemon started at : " + datetime, "SMSPool.java", "SMSPool", null, Logger.INFO);

				SMSPool sms = new SMSPool();
				//GroupController group = new GroupController();

				//ArrayList groupList = group.getActiveGroupList(args[0]);

				//if(groupList != null && groupList.size() > 0)
				//{
					//for(int i=0; i<groupList.size(); i++)
					//{
						//String[] grpArr = (String[])groupList.get(i);

				String activeGroupIds = EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST") == null ? "" : EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST");//",RG,ONECARD,YR,AR,RXM,";

				if(!activeGroupIds.equals(""))
				{
					String [] grpArr = activeGroupIds.split(",");
					for(int i=1 ; i < grpArr.length; i++)
					{
						Logger.log(i + " : Daemon Started for Group Id : " + (String)grpArr[i], "SMSPool.java", "SMSPool", null, Logger.INFO);
						sms.generateSMS((String)grpArr[i]);
						Logger.log(i + " : Daemon End for Group Id : " + (String)grpArr[i], "SMSPool.java", "SMSPool", null, Logger.INFO);
					}
				}
			}
			catch(Throwable t)
			{
				Logger.log("Error in starting SMS Daemon ", "SMSPool.java", "SMSPool", t, Logger.CRITICAL);
			}

			//code for delay
			try
			{
				Logger.log("inside delay code()", "SMSPool.java", "SMSPool", null, Logger.INFO);
				int interval = Integer.parseInt(EnvProperties.getValue("SMS_DELAY"));
				Logger.log("inside delay code() interval : " + interval, "SMSPool.java", "SMSPool", null, Logger.INFO);
				Logger.log("Thread Sleep", "SMSPool.java", "SMSPool", null, Logger.INFO);

				Thread.sleep(1000*interval);
				Logger.log("Thread Activated", "SMSPool.java", "SMSPool", null, Logger.INFO);
			}
			catch(Exception e)
			{
				Logger.log("SMSPool.java :  main() : Error Exception : ", "SMSPool.java", "SMSPool", e, Logger.CRITICAL);
			}
			//end code for delay
		}// end of while
	}
	
	public String sendInstantSms(String smsId,String smsTo,String smsFrom,String smsText,String smsProviderId,String smsProviderName,String groupId,String userId)// change params to arraylist
	{
		return "";
	}
}
