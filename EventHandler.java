package com.api.remitGuru.component.util;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;

import com.api.remitGuru.component.util.Logger;
import com.api.remitGuru.component.util.dao.SQLRepository;

public class EventHandler {
	
	private  String eventName = null;
	private  String groupId = null;
	private  String userId = null;
	private  String loginId = null;
	private  String rgtn = null;
	private  String sendAccountId = null;
	private  String recvNickName = null;
	private  String sendPass = null;
	private  String referralId = null;
	private  String subject = null;
	private  String error = null;	
	private  String rpId = null;	
	private  String strId = null;	
	private  String mailBody = null;
	ArrayList<String[]> mailerData = new ArrayList<String[]>();
	
	public int raiseEvent() {
		Logger.log(" eventName : " + eventName + "\n groupId : " + groupId+ "\n userId : " + userId+ "\n loginId : " + loginId+ "\n rgtn : " + rgtn+ "\n sendAccountId : " + sendAccountId+ "\n recvNickName : " + recvNickName+ "\n subject : " + subject + "\n referralId : " + referralId + "\n rpId : " + rpId + "\n strId : " + strId + "\n mailBody : " + mailBody, "EventHandler.java", "EventHandler", null, Logger.INFO);

		if(eventName.equals(""))
		{
			Logger.log("Error in raiseEvent eventName is EMPTY", "EventHandler.java", "EventHandler", null, Logger.INFO);
			error = "Empty Event Name";
			clearEvent();
			return -1;
		}

		if(groupId.equals(""))
		{
			Logger.log("Error in raiseEvent groupId is EMPTY", "EventHandler.java", "EventHandler", null, Logger.INFO);
			error = "Empty Group Name";
			clearEvent();
			return -1;
		}
		
		ArrayList<String[]> eventDtls = new ArrayList<String[]>();
		ArrayList<String> eventData = new ArrayList<String>();
		ArrayList<String[]> eventArryList = new ArrayList<String[]>();
		String className = "EventData", methodName = "";

		try
		{
			eventDtls = getGroupEventDetails(groupId, eventName);
		}
		catch(Throwable t)
		{
			eventDtls = null;
		}

		if(eventDtls != null && eventDtls.size() > 0)	//Check if event or trigger is asign to that partner or not
		{
			methodName	= ((String[])eventDtls.get(0))[9];	//Getting methodName for a particular trigger
			
			Logger.log(" methodName : " + methodName, "EventHandler.java", "EventHandler", null, Logger.INFO);

			if(methodName != null && !methodName.equals(""))
			{
				try
				{
					eventData.add(groupId);
					eventData.add(userId);
					eventData.add(loginId);
					eventData.add(rgtn);
					eventData.add(sendAccountId);
					eventData.add(recvNickName);
					eventData.add(sendPass);
					eventData.add(referralId);
					eventData.add(rpId);
					eventData.add(strId);
					eventData.add(mailBody);
					
					if(methodName.indexOf(".") > -1)
					{
						className = methodName.split(".")[0];
						methodName= methodName.split(".")[0];
					}

					//Logger.log(" className : " + className + " methodName : " + methodName, "EventHandler.java", "EventHandler", null, Logger.INFO);
					//Code to Call trigger methods starts here
					
					if(methodName.equals("getMailerData"))
					{
						eventArryList = mailerData;
					}
					else
					{
						// Method argument list
						Class params[] = new Class[1];
						params[0] = ArrayList.class;
	
						// Method argument values
						Object paramsObj[] = new Object[1];
						paramsObj[0] = eventData; //Assigned the ArrayList passed to the method to the Specified Method called
	
						// Get an instance of a class
						EventData event = new EventData();
						
						Logger.log(" className : " + className + " methodName : " + methodName + "  params : " + params, "EventHandler.java", "EventHandler", null, Logger.INFO);
						// Get the method
						Method thisMethod = (EventData.class).getDeclaredMethod(methodName, params);
	
						// Call the method
						Object returnObj = thisMethod.invoke(event, paramsObj);
	
						eventArryList = (ArrayList)returnObj;		//Getting the SMSContentDetails
					}
					
					if(eventArryList != null &&  eventArryList.size() > 0)
					{
						String mailContent = "", to = "", attachmentContent = "", attachmentFileName = "";
						String [] data = (String[]) eventArryList.get(0);
						String eventType = "", eventRaiseTo = "", eventId = "";
						String contentPath = "", escallationIds = "";

						eventId = ((String[])eventDtls.get(0))[0];
						eventType = ((String[])eventDtls.get(0))[3];
						eventRaiseTo = ((String[])eventDtls.get(0))[4];
						escallationIds = ((String[])eventDtls.get(0))[5];

						//Logger.log(" eventType : " + eventType, "EventHandler.java", "EventHandler", null, Logger.INFO);
						//Logger.log(" contentType : " + ((String[])eventDtls.get(0))[6], "EventHandler.java", "EventHandler", null, Logger.INFO);

						if(((String[])eventDtls.get(0))[6].equals("T")) //contentType
						{
							String contentFileName = ((String[])eventDtls.get(0))[7];
							
							if(eventType.indexOf("MAIL") > -1)
								contentPath = EnvProperties.getValue("MAILPATH");
							else
								contentPath = EnvProperties.getValue("SMSPATH");

							contentFileName = contentPath + groupId + "_" + contentFileName;
							
							Logger.log(" contentFileName : " + contentFileName, "EventHandler.java", "EventHandler", null, Logger.INFO);

							File f = new File(contentFileName); 

							StringBuffer inBuf = new StringBuffer();
							String inLine = null;

							try{
								FileReader fr = new FileReader(f);
								BufferedReader bin = new BufferedReader(fr);
				
								while( (inLine = bin.readLine())!= null){
									inBuf.append(inLine);
									inBuf.append("\n");
								}

								mailContent = inBuf.toString();
								
								if("Y".equals(((String[])eventDtls.get(0))[11]))
								{
									attachmentContent = ((String[])eventDtls.get(0))[12];
									attachmentFileName = ((String[])eventDtls.get(0))[13];							
								}
							}
							catch (Exception e) {
								Logger.log("**************** No Text File Found *********************" + "\n Error in raiseEvent  No Text File Found for Group Id : " + groupId + "   Event Name : " + eventName + "  contentFileName : " + contentFileName, "EventHandler.java", "EventHandler", e, Logger.CRITICAL);
								error = "No Text File Found for Group Id : " + groupId + "   Event Name : " + eventName + "  contentFileName : " + contentFileName;
								clearEvent();
								return -1;
							}							
						}
						else if(((String[])eventDtls.get(0))[6].equals("D")) //Get the data from database
						{
							mailContent = ((String[])eventDtls.get(0))[8];
							
							if("Y".equals(((String[])eventDtls.get(0))[11]))
							{
								attachmentContent = ((String[])eventDtls.get(0))[12];
								attachmentFileName = ((String[])eventDtls.get(0))[13];							
							}
						}

						//Logger.log(" mailContent : " + mailContent, "EventHandler.java", "EventHandler", null, Logger.INFO);

						if(mailContent != null)
						{
							mailContent = TemplateFormatter.format(mailContent, data);
							
							Logger.log(" mailContent after fromat : " + mailContent, "EventHandler.java", "EventHandler", null, Logger.INFO);
							
							if(attachmentContent != null)
							{
								attachmentContent = TemplateFormatter.format(attachmentContent, data); 
								Logger.log(" attachmentContent after fromat : " + attachmentContent, "EventHandler.java", "EventHandler", null, Logger.INFO);
							}							
							
							if(eventRaiseTo.equals("E"))
							{
								to = escallationIds;
								data[1] = escallationIds;
							}

							int res = -1;

							if(eventType.equals("MAIL") || eventType.equals("MAIL HTML"))
							{
								if(subject == null || subject.equals("")){
									subject = ((String[])eventDtls.get(0))[10];
									subject = TemplateFormatter.format(subject, data);
								}
								
								if(data[1] == null || data[1].equals(""))
								{
									Logger.log("Blank EMail ID : subject : " + subject + " for eventData : " + eventData, "EventHandler.java", "EventHandler", null, Logger.INFO);
								}
								else
								{
									RGMailer rgMailer = new RGMailer();
									res = rgMailer.sendMail(to, subject, mailContent, data, eventId, ((String[])eventDtls.get(0))[11], attachmentContent, attachmentFileName);
								}
							}
							else if(eventType.equals("INSTANT MAIL") || eventType.equals("INSTANT MAIL HTML"))
							{
								if(subject == null || subject.equals("")){
									subject = ((String[])eventDtls.get(0))[10];
									subject = TemplateFormatter.format(subject, data);
								}
								
								MailPool mp = new MailPool();
								String mailFrom 	= EnvProperties.getValue(groupId+"_MAIL_FROM", groupId) == null ? "" : EnvProperties.getValue(groupId+"_MAIL_FROM", groupId);
								String mailReply	= EnvProperties.getValue(groupId+"_MAIL_REPLY", groupId) == null ? "" : EnvProperties.getValue(groupId+"_MAIL_REPLY", groupId);

								if(mailFrom.equals(""))
									Logger.log("In raiseEvent(): Value not found in "+groupId+"remit.xml for "+ groupId+"_MAIL_FROM", "EventHandler.java", "MissingKeys", null, Logger.INFO);
								
								if(mailReply.equals(""))
									Logger.log("In raiseEvent(): Value not found in "+groupId+"remit.xml for "+ groupId+"_MAIL_REPLY", "EventHandler.java", "MissingKeys", null, Logger.INFO);
								
								if(data[1] != null) //mailTo
								{
									if(eventType.equals("INSTANT MAIL HTML"))
									{
										mp.simpleHTMLMailSend(data[1], mailFrom, mailReply, "", subject, mailContent, "", groupId);
									}
									else
									{
										mp.simpleMailSend(data[1], mailFrom, mailReply, "", subject, mailContent, "", groupId);
									}
								}
								else
									Logger.log("Blank EMail ID in INSTANT MAIL : subject : " + subject + " for eventData : " + eventData + " groupId : " + groupId, "EventHandler.java", "EventHandler", null, Logger.INFO);
									
							}
							else if(eventType.equals("SMS"))
							{								
								RGSMS rgSMS = new RGSMS();
								
								if(eventRaiseTo.equals("E"))
								{
									String [] smsTo  = escallationIds.split(",");

									for(int iLoop = 0; iLoop < smsTo.length; iLoop++)
									{
										data[2] = smsTo[iLoop].trim();
										res = rgSMS.sendSMS(to, mailContent, data, eventId);
									}
								}
								else
								{
									if(data[4].indexOf(((String[])eventDtls.get(0))[4]) > -1)
									{
										res = rgSMS.sendSMS(to, mailContent, data, eventId);
									}
									else
									{
										Logger.log("SMS Allowed to : " + data[4] + " only not for : "+ ((String[])eventDtls.get(0))[4] + " for eventData : " + eventData, "EventHandler.java", "EventHandler", null, Logger.INFO);
									}
								}
							}
							else if(eventType.equals("INSTANT SMS"))
							{
								//RGSMS rgSMS = new RGSMS();
								//res = rgSMS.sendSMS(to, null, mailContent, data, eventId);
								SMSPool smsPool = new SMSPool();
								
								if(data[4].indexOf(((String[])eventDtls.get(0))[4]) > -1)
								{
									//data[0] - groupID
									//data[2] - to
									//data[3] - userID
									smsPool.sendInstantSms(data[2], mailContent, data[0], data[3]);
								} 
								else
								{
									Logger.log("SMS Allowed to : " + data[4] + " only not for : "+ ((String[])eventDtls.get(0))[4] + " for eventData : " + eventData, "EventHandler.java", "EventHandler", null, Logger.INFO);
								}
							}
							else if(eventType.equals("WHATSAPP"))
							{								
								RGWhatsApp rgWhatsApp = new RGWhatsApp();
								
								if(eventRaiseTo.equals("E"))
								{
									rgWhatsApp.sendMessage(escallationIds, mailContent, data, eventId);// Check for , in to and send Bulk Message
								}
								else
								{
									if(data[4].indexOf(((String[])eventDtls.get(0))[4]) > -1)// Check if register / subscribe for Whats App message
									{
										rgWhatsApp.sendMessage(to, mailContent, data, eventId);
									}
									else
									{
										Logger.log("Not Subscribe for WhatsApp Message. Flag : " + data[4] + " for : "+ ((String[])eventDtls.get(0))[4] + " for eventData : " + eventData, "EventHandler.java", "EventHandler", null, Logger.INFO);
									}
								}
							}
							else
							{
								Logger.log("eventType : " + eventType + " not implemented", "EventHandler.java", "EventHandler", null, Logger.INFO);
							}							
										
							Logger.log("res : " + res, "EventHandler.java", "EventHandler", null, Logger.INFO);
							Logger.log("**************** Success *********************", "EventHandler.java", "EventHandler", null, Logger.INFO);
							//success = this.setMailTemplateData(this.subject, mailContent, data);
						}
						else
						{
							Logger.log("**************** No Content Found *********************" + "\n Error in raiseEvent  No Content Found for Group Id : " + groupId + "   Event Name : " + eventName, "EventHandler.java", "EventHandler", null, Logger.INFO);
							error = "No Content Found for Group Id : " + groupId + "   Event Name : " + eventName;
							clearEvent();
							return -1;
						}

						//Code to Call trigger methods ends here 
					}
					else
					{
						Logger.log("**************** No Data Found *********************" + "\n Error in raiseEvent  No Data Found for Group Id : " + groupId + "   Event Name : " + eventName, "EventHandler.java", "EventHandler", null, Logger.INFO);
						error = "No Data Found for Group Id : " + groupId + "   Event Name : " + eventName;
						clearEvent();
						return -1;
					}
				}
				catch (Exception e)
				{
					eventArryList = null;
					Logger.log(e + "EventHandler.java raiseEvent() Exception while getting details from method: " + methodName , "EventHandler.java", "EventHandler", null, Logger.CRITICAL);
					clearEvent();
					return -1;
				}
			}
			else
			{
				Logger.log("**************** No Method Found *********************" + "\n Error in raiseEvent  No Method Found for Group Id : " + groupId + "   Event Name : " + eventName, "EventHandler.java", "EventHandler", null, Logger.INFO);
				error = "No Method Found for Group Id : " + groupId + "   Event Name : " + eventName;
				clearEvent();
				return -1;
			}
		}
		else
		{
			Logger.log("**************** No Such Event mapped to this Group *********************" + "\n Error in raiseEvent  No Such Active Event is found for Group Id : " + groupId + "   Event Name : " + eventName, "EventHandler.java", "EventHandler", null, Logger.INFO);
			error = "No Such Active Event is found for Group Id : " + groupId + "   Event Name : " + eventName;
			clearEvent();
			return -1;
		}
		
		clearEvent();
		return 1;
	}

	/*********************************************************************************************************************
	*  Method Description:	This method is responsible fetching  data for event details for a particular group & event
	*  List of JSPs associated with this class:
	*  Updated Date	:	3/19/2012
	*  Author		:	
	*  Version		:	1.00
	*  Related File :
	**********************************************************************************************************************/
	private ArrayList getGroupEventDetails(String groupId, String eventName)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query = new StringBuffer();
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		RGUtil rgutil = new RGUtil();
		SQLRepository repository = (SQLRepository)(rgutil.getRepositoryObject());

		query.append(" SELECT						");
		query.append(" 		a.eventId,				");		
		query.append(" 		a.eventName,			");	
		query.append(" 		a.description,			");	
		query.append(" 		a.eventType,			");	
		query.append(" 		a.eventRaiseTo,			");	
		query.append(" 		b.escallationIds,		");	
		query.append(" 		a.contentType,			");	
		query.append(" 		a.contentFileName,		");	
		query.append(" 		b.content,				");	
		query.append(" 		b.methodName,			");	
		query.append(" 		b.subject,				");			
		query.append(" 		b.attachmentFlag,		"); //11	
		query.append(" 		b.attachmentContent,	");	//12
		query.append(" 		b.attachmentFileName	");	//13		
		query.append(" FROM							");
		query.append(" 		EventMaster a,			");
		query.append(" 		GroupEventMap b			");
		query.append(" WHERE						");
		query.append("		a.eventId = b.eventId	");
		query.append(" AND	b.groupId	= ?			");
		query.append(" AND	a.eventName = ?			");
		query.append(" AND	b.isActive	= 'Y'		");
		query.append(" AND	a.isActive	= 'Y'		");
		
		params.add(groupId);
		paramTypes.add(0);
		params.add(eventName);
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,groupId);
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getGroupEventDetails query : " + query, "EventHandler.java", "EventHandler", t, Logger.CRITICAL);
		}
		finally
		{
			params = null;
			paramTypes = null;
			query = null;	
			repository = null;
		}

		return result;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public  void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupId
	 */
	public  String getGroupId() {
		return groupId;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public  void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the eventName
	 */
	public  String getEventName() {
		return eventName;
	}

	/**
	 * @param userId the userId to set
	 */
	public  void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public  String getUserId() {
		return userId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public  void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the loginId
	 */
	public  String getLoginId() {
		return loginId;
	}

	/**
	 * @param sendAccountId the sendAccountId to set
	 */
	public  void setSendAccountId(String sendAccountId) {
		this.sendAccountId = sendAccountId;
	}

	/**
	 * @return the sendAccountId
	 */
	public  String getSendAccountId() {
		return sendAccountId;
	}

	/**
	 * @param rgtn the rgtn to set
	 */
	public  void setRgtn(String rgtn) {
		this.rgtn = rgtn;
	}

	/**
	 * @return the rgtn
	 */
	public  String getRgtn() {
		return rgtn;
	}

	/**
	 * @param recvNickName the recvNickName to set
	 */
	public  void setRecvNickName(String recvNickName) {
		this.recvNickName = recvNickName;
	}

	/**
	 * @return the recvNickName
	 */
	public  String getRecvNickName() {
		return recvNickName;
	}

	/**
	 * @param subject the subject to set
	 */
	public  void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the subject
	 */
	public  String getSubject() {
		return subject;
	}	
	
	/**
	 * @param error the error to set
	 */
	public  void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the recvNickName
	 */
	public  String getError() {
		return error;
	}

	/**
	 * @param sendPass the sendPass to set
	 */
	public void setSendPass(String sendPass) {
		this.sendPass = sendPass;
	}

	/**
	 * @return the sendPass
	 */
	public String getSendPass() {
		return sendPass;
	}

	/**
	 * @return the referralId
	 */
	public String getReferralId() {
		return referralId;
	}

	/**
	 * @param referralId the referralId to set
	 */
	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	/**
	 * @return the rpId
	 */
	public String getRpId() {
		return rpId;
	}

	/**
	 * @param rpId the rpId to set
	 */
	public void setRpId(String rpId) {
		this.rpId = rpId;
	}

	/**
	 * @return the strId
	 */
	public String getStrId() {
		return strId;
	}

	/**
	 * @param strId the strId to set
	 */
	public void setStrId(String strId) {
		this.strId = strId;
	}
	
	/**
	 * @return the mailBody
	 */
	public String getMailBody() {
		return mailBody;
	}

	/**
	 * @param mailBody the mailBody to set
	 */
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
	
	/**
	 * @return the mailerData
	 */
	public ArrayList<String[]> getMailerData() {
		return mailerData;
	}

	/**
	 * @param mailerData the mailerData to set
	 */
	public void setMailerData(ArrayList<String[]> mailerData) {
		this.mailerData = mailerData;
	}
	

	public void clearEvent(){
		
		subject = null;
		error = null;	
	}
}