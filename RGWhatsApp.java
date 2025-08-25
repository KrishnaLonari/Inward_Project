package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import com.api.remitGuru.component.environment.Constants;
import com.api.remitGuru.web.controller.transferWebServices.TransferRequestProcessorController;

public class RGWhatsApp {
		
	/**
	 *	Default no parameter constructor
	 */
	

	public RGWhatsApp(){
		
	}

	/*To address..*/
	public String to = null;

	/*Body of sms.*/
	public String body = null;

	/*eventId ..*/
	public String eventId = null;
	
	/*groupId ..*/
	public String groupId = null;

	/*userId ..*/
	public String userId = null;

	public String sendMessage(String smsMobile, String smsText, String[] data, String eventId)
	{
		String str1 = "", output="";

		try
		{
			this.groupId	= data[0] == null ? "" : data[0];			
			this.to			= data[2] == null ? "" : data[2];
			this.userId		= data[3] == null ? "" : data[3];
			this.body		= smsText == null ? "" : smsText;
			this.eventId	= eventId == null ? "" : eventId;
									
		}
		catch(Throwable t)
		{
			Logger.log("Error in sendSMS", "RGWhatsApp.java", "RGWhatsApp", t, Logger.CRITICAL);
		}

		try
		{
			this.body =  "'" + this.body.replaceAll("'","\"") + "'" ;
			String[] recvCommand = { "/bin/bash", "-c", "python yowsup-cli -c config.example -s "+ this.to.replaceAll("-","") +" "+this.body+" " };

			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.directory(new File(Constants.configFileRootPath + "whatsapp"));
			processBuilder.environment().put("PYTHONPATH", Constants.configFileRootPath + "whatsapp");
			processBuilder.command(recvCommand);
			Process process = processBuilder.start();
			process.waitFor();

			InputStream stream = process.getInputStream();
			BufferedReader recv_br = new BufferedReader(new InputStreamReader(stream));
			
			while ((str1 = recv_br.readLine()) != null) {
				output = output + str1 + "\n";
			}
		}
		catch (Exception e)
		{
			Logger.log("Error sending message in sendMessage()", "RGWhatsApp.java", "RGWhatsApp", e, Logger.CRITICAL);
			output = "Error";
		}
		
		if(output.indexOf("Sent message") > -1 || output.indexOf("Got sent receipt") > -1)
		{
			Logger.log("Message sending successful : " + output, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);
			return "success";
		}
		else
		{
			Logger.log("Message sending failed : " + output, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);
			return "fail";
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SimpleDateFormat bartDateFormat = new SimpleDateFormat("EEEE-MMMM-dd-yyyy HH:mm:ss");
		java.util.Date currdt = new java.util.Date();
	
		String datetime = bartDateFormat.format(currdt);
		
		try
		{	
			Logger.log("Daemon started at : " + datetime, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);

			if(args[0] != null && !args[0].equals(""))
			{
				String wam = args[0].split("\\^")[0];//"12225558889@s.whatsapp.net [14-08-2014 17:38]:YESGURU";//SENDGURU 50 CAD rishi
				String messageId = args[0].split("\\^")[1];//1409733035-24
				String pushName = args[0].split("\\^")[2];//user's name
				
				String error ="",wamText="",waFromMobile="",ipAddress = "127.0.0.1";

				Logger.log("WAM Data Received : " + wam + "\nmessageId : "+messageId + "\npushName : "+pushName, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);

				try
				{
					wamText = wam.split("]:")[1];
					waFromMobile = wam.split("@")[0];
				}
				catch(Exception e)
				{
					Logger.log("WAM Data wam : " + wam, "RGWhatsApp.java", "RGWhatsApp", e, Logger.CRITICAL);
					error = "1";
				}

				if(!error.equals(""))
				{
					Logger.log("Error in whatsApp Remittance Request : " + wamText +  " error :  " + error, "RGWhatsApp.java", "RGWhatsApp", null, Logger.CRITICAL);
				}
				else
				{
					Logger.log("Ready to process WAM Remittance Request : " + wamText +  " error :  " + error + "  waFromMobile : " + waFromMobile, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);

					String smsMobile	= "";
					String requestId = String.valueOf(Math.round(Math.random() * 100000000));	

					if(waFromMobile.length() > 10)
						smsMobile = waFromMobile.substring(0,waFromMobile.length()-10) + "-" + waFromMobile.substring(waFromMobile.length() - 10,waFromMobile.length());
					else if(waFromMobile.length() == 10)
						smsMobile = waFromMobile.substring(0,waFromMobile.length()-8) + "-" + waFromMobile.substring(waFromMobile.length() - 8,waFromMobile.length());
					
					Logger.log("smsMobile after split : " + smsMobile, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);

					if(smsMobile!=null && !smsMobile.equals(""))
					{
						String requestType = "";
						String temp [] = wamText.split(" ");

						if(temp[0].toUpperCase().equals("YESGURU"))
							requestType = "OTPCONFIRM";
						else if(temp[0].toUpperCase().equals("SENDGURU"))
							requestType = "SMSTRANSFER";
						else if(temp[0].toUpperCase().equals("GETSTATUS"))
							requestType = "LASTTXN";	
						
						if(!requestType.equals(""))
						{
							String data = "{\"requestId\":\"" + requestId + "\",\"requestType\":\"" + requestType + "\",\"channelId\":\"WHATSAPP\",\"clientId\":\"RG\",\"groupId\":\"RG\",\"smsMobile\":\"" + smsMobile + "\",\"smsMessage\":\"" + wamText + "\",\"ipAddress\":\"" + ipAddress + "\",\"key\":\"999999999999\"}";
	
							Logger.log("JSON object Data : " + data, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);
								
							ArrayList reqData = new ArrayList();
							String responseData = "";
	
							reqData.add(data);
							reqData.add("JSON");
							reqData.add(EnvProperties.getValue("WEB_NAME"));
							reqData.add(ipAddress);
							reqData.add("RG");
	
							try
							{
								com.api.remitGuru.web.controller.transferWebServices.TransferRequestHandler trh = new com.api.remitGuru.web.controller.transferWebServices.TransferRequestHandler();
								responseData = trh.transferRequestHandler(reqData);
							}
							catch(Exception e)
							{
								responseData = "Invalid Request";
								Logger.log("Error in transferRequestHandler : " + reqData, "RGWhatsApp.java", "RGWhatsApp", e, Logger.CRITICAL);
							}	
	
							Logger.log("responseData : " + responseData, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);
							
							String smsText = responseData;
							if(!responseData.equals("Invalid Request"))
							{
								try
								{
									com.api.remitGuru.web.controller.transferWebServices.TransferRequestJSONParser jsonParser = new com.api.remitGuru.web.controller.transferWebServices.TransferRequestJSONParser();		
	
									ArrayList reqData1 = new ArrayList();
									reqData1.add(responseData);
									Map responseHm = jsonParser.readJSONRequestData(reqData1);		
	
									smsText = (String)responseHm.get("smsMessage");
	
									Logger.log("responseData smsText : " + smsText, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);
								}
								catch(Exception e)
								{
									smsText = "Invalid Request";
									Logger.log("Error in readJSONRequestData : responseData : " + responseData, "RGWhatsApp.java", "RGWhatsApp", e, Logger.CRITICAL);
								}
							}
							
							String result1 = "", msg = "", userId = "";
							ArrayList mobileData = new ArrayList();	
	
							requestId = String.valueOf(Math.round(Math.random() * 100000000));
	
							Logger.log("Send response sms -> smsText : " + smsText +  " smsMobile :  " + smsMobile + " requestId : " + requestId, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);
	
							String str = "", output = "";
	
							if(smsText!=null && !smsText.equals(""))
							{
								TransferRequestProcessorController trnsfrReqProcCtrl = new TransferRequestProcessorController();
								mobileData.add("RG");
								mobileData.add(smsMobile);
								mobileData.add("R");
								mobileData.add("WHATSAPP");
								
								try
								{
									mobileData = trnsfrReqProcCtrl.getSMSRemitUserDetails(mobileData);
								}
								catch(Throwable t)
								{
									Logger.log("Error calling getSMSRemitUserDetails()", "RGWhatsApp.java", "RGWhatsApp", t, Logger.CRITICAL);
								}
								
								if(mobileData != null && mobileData.size() > 0)
								{
									userId = ((String [])mobileData.get(0))[2];
								}
								else
								{
									Logger.log("Mobile number not registered for SMS Remittance." + smsMobile, "RGWhatsApp.java", "RGWhatsApp", null, Logger.CRITICAL);
								}
								
								try
								{
									try
									{
										smsText = "'" + smsText.replaceAll("'","\"") + "'" ;
										String[] recvCommand = { "/bin/bash", "-c", "python yowsup-cli -c config.example -s "+ smsMobile.replaceAll("-","") +" "+smsText+" " };
	
										ProcessBuilder processBuilder = new ProcessBuilder();
										processBuilder.directory(new File(Constants.configFileRootPath + "whatsapp"));
										processBuilder.environment().put("PYTHONPATH", Constants.configFileRootPath + "whatsapp");
										processBuilder.command(recvCommand);
										Process process = processBuilder.start();
										process.waitFor();
	
										InputStream stream = process.getInputStream();
										BufferedReader recv_br = new BufferedReader(new InputStreamReader(stream));
										
										while ((str = recv_br.readLine()) != null) {
											output = output + str + "\n";
										}
									}
									catch (Exception e)
									{
										Logger.log("Error sending message", "RGWhatsApp.java", "RGWhatsApp", e, Logger.CRITICAL);
									}
									
									ArrayList smsData = new ArrayList();
									int smsResult = -1;	
	
									String sendFlag = "F";
									if(output.indexOf("Sent message") > -1 || output.indexOf("Got sent receipt") > -1)
										sendFlag = "Y";
									
									com.api.remitGuru.component.util.RGSMS rgSms = new com.api.remitGuru.component.util.RGSMS();
	
									/* Add in SMS Pool with send flag as Sent : Y or Fail : N. This is for audit purpose. */
									try
									{
										smsData.add("RG");//0 groupId           
										smsData.add(userId.equals("") ? "0" : userId);				//userId 										  
										smsData.add(sendFlag);//sendFlag    										  
										smsData.add(smsMobile);//smsTo       										  
										smsData.add("REMGUR");//smsFrom     										  
										smsData.add(smsText);//msg         										  
										smsData.add("");//eventId     										  
										smsData.add(output==null ? "" :output);//response    										  
										smsData.add("-1");//smsProviderId
	
										smsResult = rgSms.addSMSPool(smsData);
									}
									catch(Throwable t)
									{	
										Logger.log("Error calling addSMSPool", "RGWhatsApp.java", "RGWhatsApp", t, Logger.CRITICAL);
									}
									finally
									{
										sendFlag = null;
										smsData = null;			
									}
	
									Logger.log("Response for mobile no " + smsMobile + " : " + output+ "\nSms pool id : " + smsResult, "SMSPool.java", "SMSPool", null, Logger.INFO);
								}
								catch(Throwable t)
								{	
									Logger.log("Error calling sendInstantMessage", "RGWhatsApp.java", "RGWhatsApp", t, Logger.CRITICAL);
								}
								
								if(output.indexOf("Sent message") > -1 || output.indexOf("Got sent receipt") > -1)
								{
									Logger.log("Result of Message :" + output + "\nMessage : '" + smsText + "' sent successfully to " + smsMobile, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);				
								}
								else
								{
									Logger.log("Result of Message :" + output + "\nMessage failed to " + smsMobile, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);				
								}		
							}		
							else
							{
								Logger.log("Message Text Blank. " + smsText + " for mobile : " + smsMobile, "RGWhatsApp.java", "RGWhatsApp", null, Logger.CRITICAL);
							}
						}
						else
						{
							Logger.log("Chat Invalid request type " + smsMobile + " requestType : " + requestType + "  wamText : "  + wamText , "RGWhatsApp.java", "RGWhatsAppChat", null, Logger.INFO);
							//Whats App Chat
							
							try
							{
								File file = new File(Constants.configFileRootPath + "whatsapp/rg_whatsapp.txt");
								
								if(!file.exists())
									file.createNewFile();
								
								BufferedWriter br = new BufferedWriter(new FileWriter(file,true));
								br.append(args[0] + "\n");
								br.close();
								br = null;
							}
							catch (Exception e)
							{
								Logger.log("Error writing file for wam : " + args[0], "RGWhatsApp.java", "RGWhatsApp", e, Logger.CRITICAL);
							}
						}
					}
					else
					{
						Logger.log("Invalid mobile number" + smsMobile, "RGWhatsApp.java", "RGWhatsApp", null, Logger.CRITICAL);
					}	
				}
			}
			
			datetime = bartDateFormat.format(currdt);
		}
		catch(Throwable t)
		{
			Logger.log("Error in starting WhatsappRemit Daemon", "RGWhatsApp.java", "RGWhatsApp", t, Logger.CRITICAL);
		}
		
		Logger.log("Daemon stoped at : " + datetime, "RGWhatsApp.java", "RGWhatsApp", null, Logger.INFO);
	}
}
