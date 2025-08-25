/*
 * @(#)UserVerificationPool.java	11-DEC-2012
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenues*******.
 * Use is subject to license terms.
 *
 */
package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.api.remitGuru.component.environment.Constants;
import com.api.remitGuru.web.controller.GroupCustomerRiskLimitController;

public class UserVerificationPool {

	
	
	/**
	 *	Default no parameter constructor
	 */
	public UserVerificationPool(){
		
	}
	
	public String postURL(String xmlPostingURL, String URLXML)
	{		
		String responseXML		= "";
		
		PrintWriter pw = null;
		BufferedWriter out = null;
		FileReader fr = null;

		try 
		{
			try
			{
				// Create file 
				FileWriter fstream = new FileWriter(Constants.configFilePath + "LendProtectReq.xml");
				out = new BufferedWriter(fstream);
				out.write(URLXML);
				//Close the output stream
				out.close();
			}
			catch (Exception e)
			{//Catch exception if any
				Logger.log("Error Writing: ", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
			}
			
			URL url = new URL(xmlPostingURL);
			fr = new FileReader(Constants.configFilePath + "LendProtectReq.xml");
			char[] buffer = new char[1024*10];
			int bytes_read = 0;

			if ((bytes_read = fr.read(buffer)) != -1)
			{
				URLConnection urlc = url.openConnection();
				urlc.setRequestProperty("Content-Type","text/xml");
				urlc.setDoOutput(true);
				urlc.setDoInput(true);
				pw = new PrintWriter(urlc.getOutputStream());
				// send xml to jsp
				pw.write(buffer, 0, bytes_read);
	            pw.close();
		        BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			    String inputLine;
				while ((inputLine = in.readLine()) != null)
				{
					System.out.println(inputLine);
					responseXML = responseXML + inputLine;
				}
            
				in.close();
            }
			
			fr.close();
		}
        catch (Exception e) 
		{
			Logger.log("error in postURL : ", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
		}
		finally 
		{
			pw = null;
			out = null;
			fr  = null;
		}

		return responseXML;
    }
	
	public int sendLendDetails(String country, String rgtn, String groupId)
	{
		//Daemon to read all pending records
		//Update flag to w 
		//Get Service Provider Details
		//Generate XML
		//Post XML 
		//Store Response
		//Read Response
		//Add response result
		
		int intResult = -1;
		ArrayList uvDetails = new ArrayList();
		ArrayList spDetails = new ArrayList();
		String requestIds = "", reqIdUnprocessed = "";
		String[] spDetailsArray = null;
		String xmlResponse = "";
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		HashMap<Integer, String> map1 = new HashMap<Integer, String>();
		RGUtil rgutil = new RGUtil();
		
		if (country.equals("")) {
			Logger.log("country is empty", "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
			return intResult;
		}
		GroupCustomerRiskLimitController grpCustRskLimitCntrl = new GroupCustomerRiskLimitController();
		
		try
		{
			Logger.log("Processing for " + country, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
			
			if(!rgtn.equals(""))
			{
				try 
				{
					uvDetails = grpCustRskLimitCntrl.getUserVerificationRequest(country, "P", rgtn, groupId);
				}
				catch (Exception e)
				{
					Logger.log("Error in calling getUserVerificationRequest", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
				}
			}
			else
			{
				try 
				{
					uvDetails = grpCustRskLimitCntrl.getLendProtectRGTN(country,groupId);
				} 
				catch (Exception e)
				{
					Logger.log("Error in calling getLendProtectRGTN", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
				}
			}

			if (uvDetails != null && uvDetails.size() > 0)
			{
				for (int i = 0; i < uvDetails.size(); i++)
				{
					String[] temp = (String[])uvDetails.get(i);
					String userId = rgutil.getUserIdFromRGTN((String)temp[4],country);
					
					if(!map.containsValue(userId))
					{
						map.put(i, userId);
						requestIds = requestIds + temp[0] + ",";
					}
					else
					{
						reqIdUnprocessed = reqIdUnprocessed + temp[0] + ",";
					}
				}

				Logger.log("No of Request for " + country + " : " + map.size()+"\nNo of Request not Processed for " + country + " : " + (uvDetails.size() - map.size()), "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);

				requestIds = requestIds + "0";
				reqIdUnprocessed = reqIdUnprocessed + "0";
				
				try
				{
					intResult = grpCustRskLimitCntrl.updateUserVerificationRequestFlag(requestIds,country, "P", "W", "", groupId);
				} 
				catch (Exception e)
				{
					Logger.log("Error in calling updateUserVerificationRequestFlag", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
				}
				
				try
				{
					intResult = grpCustRskLimitCntrl.updateUserVerificationRequestFlag(reqIdUnprocessed,country, "P", "D", "", groupId);
				} 
				catch (Exception e)
				{
					Logger.log("Error in calling updateUserVerificationRequestFlag", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
				}

				Logger.log("Records Updated to W : " + requestIds + "\nRecords Updated to D : " + reqIdUnprocessed, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);

				try 
				{
					spDetails = grpCustRskLimitCntrl.getServiceProviderDetails(country,groupId);
				} 
				catch (Exception e)
				{
					Logger.log("Error in calling getServiceProviderDetails", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
				}

				if (spDetails != null && spDetails.size() > 0)
				{
					spDetailsArray = (String[])spDetails.get(0);

					for (int i = 0; i < uvDetails.size(); i++)
					{
						xmlResponse = "";
						String xmlRequest = "";

						String[] temp = (String[])uvDetails.get(i);
						String userId = rgutil.getUserIdFromRGTN((String)temp[4],country);
						
						if(!map1.containsValue(userId))
						{
							map1.put(i, userId);
							
							if(temp[22].equals("CA"))
							{
								xmlRequest = grpCustRskLimitCntrl.generateXMLRequestCA(spDetailsArray, temp);
							}
							else
							{
								xmlRequest = grpCustRskLimitCntrl.generateXMLRequest(spDetailsArray, temp);
							}
	
							Logger.log("requestId : " + temp[0] + " xmlRequest : " + xmlRequest, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
	
							try
							{
								xmlResponse =  postURL(spDetailsArray[6], xmlRequest); //spDetailsArray[5] //postURL
							}
							catch(Throwable t)
							{
								Logger.log("Error in posting url : " +  spDetailsArray[5] + "\nxml : " + xmlRequest, "UserVerificationPool.java", "UserVerificationPool", t, Logger.CRITICAL);
							}
	
							Logger.log("applicationId : " + temp[0] + " xmlResponse : " + xmlResponse, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
							
							int storeResult = -1;
							try
							{
								storeResult = grpCustRskLimitCntrl.storeResponse(temp, xmlResponse);
							} 
							catch (Exception e)
							{
								Logger.log("Error in calling storeResponse", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
							}
							
							intResult = -1;
							try
							{
								intResult = grpCustRskLimitCntrl.updateUserVerificationRequestFlag(temp[0],country, "W", (storeResult > 0 ? "C" : "F"), xmlRequest, groupId);
							} 
							catch (Exception e)
							{
								Logger.log("Error in calling updateUserVerificationRequestFlag", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
							}
	
							Logger.log("status updated to C applicationId : " + temp[0], "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
							
							if(storeResult > 0)
							{
								intResult = -1;
								try
								{
									intResult = grpCustRskLimitCntrl.readResponse(temp, xmlResponse, "" + storeResult);
								} 
								catch (Exception e)
								{
									Logger.log("Error in calling readResponse", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
								}
							}
							else
							{
								Logger.log("No Response for applicationId : " + temp[0], "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
								intResult = -1;
							}
						}
						else
						{
							Logger.log("Duplicate Login ID in Batch applicationId : " + temp[0] +  " RGTN : " + temp[4] +  " Login ID : " + temp[3], "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
						}
					}
				}
				else
				{
					Logger.log("Service Provider not found for " + country, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
					intResult = -1;
				}
			}
			else
			{
				Logger.log("No of Request for " + country + " : 0", "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
				intResult = -1;
			}

			Logger.log("End Processing for " + country, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
		}
		catch(Throwable t)
		{
			Logger.log("Error in starting SMS Daemon ", "UserVerificationPool.java", "UserVerificationPool", t, Logger.CRITICAL);
			intResult = -1;
		}
		
		return intResult;
	}
	
	/**
	 * This main method is to start the daemon
	 *
	 */
	public static void main(String [] args)
	{
		while(true) 
		{
			String daemon = "0";
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("EEEE-MMMM-dd-yyyy HH:mm:ss");
			java.util.Date currdt = new java.util.Date();	
			
			String datetime = bartDateFormat.format(currdt);
			
			Logger.log("Daemon started at : " + datetime, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);

			UserVerificationPool usrVerifctnPool = new UserVerificationPool(); 
			
			String activeGroupIds = EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST") == null ? "" : EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST");//",RG,ONECARD,YR,AR,RXM,";
			
			if(!activeGroupIds.equals(""))
			{
				String [] grpArr = activeGroupIds.split(",");
				for(int i=1 ; i < grpArr.length; i++)
				{
					Logger.log(i + " : Daemon Started for Group Id : " + (String)grpArr[i], "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
					daemon = "" + i;
					usrVerifctnPool.sendLendDetails("GB", "", (String)grpArr[i]);
					Logger.log(i + " : Daemon End for Group Id : " + (String)grpArr[i], "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
				}
			}
			
			//code for delay
			try
			{
				int interval = Integer.parseInt(EnvProperties.getValue("USERVERIFICATION_DELAY"));
				
				datetime = bartDateFormat.format(currdt);
				Logger.log("inside delay code() interval : " + interval + " datetime : "+datetime, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);

				Thread.sleep(1000*interval);
				
				datetime = bartDateFormat.format(currdt);
				
				Logger.log("Thread Activated datetime : "+ datetime, "UserVerificationPool.java", "UserVerificationPool", null, Logger.INFO);
			}
			catch(Exception e)
			{
				Logger.log("UserVerificationPool.java :  main() : Error Exception : ", "UserVerificationPool.java", "UserVerificationPool", e, Logger.CRITICAL);
			}
			//end code for delay		
		}// end of while

	}
}

