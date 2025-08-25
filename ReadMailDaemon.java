package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.Address;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

import com.api.remitGuru.component.environment.Constants;
import com.api.remitGuru.web.controller.RGUserController;
import com.api.remitGuru.web.controller.SQLRepositoryController;
import com.sun.mail.pop3.POP3Store;

public class ReadMailDaemon {
	
	/**
	 *	Default no parameter constructor
	 */

	SQLRepositoryController repository ;

	RGUserController rgUserCntrl;
	
	public ReadMailDaemon() {
		
		repository = new SQLRepositoryController();
		rgUserCntrl = new RGUserController();

	}
	
	boolean isFound = false;

	static boolean mailRead_liveStatus = true; ;
	
	static long mailRead_timeInterval = 900000L; // 15 mins interval default
	
	static boolean mailRead_updateCRMDataflag = false;
	
	String mimeType = "", mailContentHTML = "";
	
	String mailRead_imagePath = Constants.configFilePath;
	
	public  ArrayList<String []> receiveEmail() 
	{

		ArrayList<String []> mailInfoList = new ArrayList<String[]>();

		int previousMailCount = 0 ,msgCount = 0;
		int msgsPerGo =  0 ; // messages to be fetched
		
		long mailRead_timeInterval = 900000L; // 15 mins interval
		
		int unreadMsgs = 0;

		String mailRead_userName = "" , mailRead_password = "" , mailRead_readMailIds = "" , pop3Host = "" , storeType = "" , mailReadAttachment_forwardTo = "", mailReadAttachment_reply_mail_from = "" ,mailReadAttachment_reply_mail = "" ,mailReadAttachment_reply_mail_password = "" , senderName = "" , mailReadAttachment_uploadPath = "" , flowLog = "" , msgId = "" ,subject = "";

		boolean mailRead_liveStatus = false, mailReadAttachment_reply_mail_flag = false , mailRead_updateCRMDataflag = false ;
		
		File fileMailCount = new File(Constants.configFilePath + "mailRead.properties");

		FileInputStream inFileMailCount = null;

		FileOutputStream outFileMailCount = null;

		Properties propsFileMailCount = null;

		try
		{
			inFileMailCount = new FileInputStream(fileMailCount);

			propsFileMailCount = new Properties();

			propsFileMailCount.load(inFileMailCount);

			inFileMailCount.close();
			
			//  Mail Config
			
			previousMailCount = Integer.parseInt(propsFileMailCount.getProperty("mailRead_LastMailCount"));

			msgsPerGo = Integer.parseInt(propsFileMailCount.getProperty("mailRead_maxMsgsPerGo"));

			mailRead_liveStatus = Boolean.valueOf(propsFileMailCount.getProperty("mailRead_liveStatus"));

			mailRead_userName = propsFileMailCount.getProperty("mailRead_userName");

			mailRead_password = propsFileMailCount.getProperty("mailRead_password");

			mailRead_readMailIds = propsFileMailCount.getProperty("mailRead_readMailIds");
			
			mailReadAttachment_forwardTo = propsFileMailCount.getProperty("mailReadAttachment_forwardTo");
			
			mailReadAttachment_reply_mail_flag = Boolean.valueOf(propsFileMailCount.getProperty("mailReadAttachment_reply_mail_flag")); 
			
			mailReadAttachment_reply_mail_from = propsFileMailCount.getProperty("mailReadAttachment_reply_mail_from") ;
			
			mailReadAttachment_reply_mail = propsFileMailCount.getProperty("mailReadAttachment_reply_mail");

			mailReadAttachment_reply_mail_password = propsFileMailCount.getProperty("mailReadAttachment_reply_mail_password");

			mailReadAttachment_uploadPath = propsFileMailCount.getProperty("mailReadAttachment_uploadPath");
			
			mailRead_updateCRMDataflag = Boolean.valueOf(propsFileMailCount.getProperty("mailRead_updateCRMDataflag")); 
			
			mailRead_imagePath = mailRead_imagePath + propsFileMailCount.getProperty("mailRead_imagePath");
			
			try 
			{
				mailRead_timeInterval = (long)  (((Double.parseDouble(propsFileMailCount.getProperty("mailRead_timeIntervalMins")) * (1000))) * 60) ; 
			}
			catch(Throwable t)
			{
				Logger.log("Error in parsing mailRead_timeInterval", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
				mailRead_timeInterval = 900000L; // 15 mins inter val
			}
			pop3Host = EnvProperties.getValue("MAILERHOST");
			storeType = "pop3";
			
			//  Mail Config

			this.mailRead_liveStatus = mailRead_liveStatus ;
			this.mailRead_timeInterval = mailRead_timeInterval ;
			this.mailRead_updateCRMDataflag = mailRead_updateCRMDataflag ;
		}
		catch(Throwable t)
		{
			Logger.log("File error  ", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
		}
		finally
		{
			inFileMailCount = null;
		}
		
		/*Load Property File CRM*/
		
		String crmAttchPath = "";
		
		File crmFile = new File(Constants.configFilePath + "crm.properties");

		FileInputStream inCrmFile = null;

		Properties propsCrmFile = null;

		try
		{
			inCrmFile = new FileInputStream(crmFile);

			propsCrmFile = new Properties();

			propsCrmFile.load(inCrmFile);

			inCrmFile.close();
			
			crmAttchPath = propsCrmFile.getProperty("crm_default_attachment");
		}
		catch(Throwable t)
		{
			Logger.log("Error fetching crm_default_attachment", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
		}
		finally
		{
			inCrmFile = null;
			
			Logger.log("In fileMove () crm_default_attachment:"+crmAttchPath, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		
		/*Load Property File CRM*/
		
		
		Logger.log("previousMailCount "+previousMailCount+"\nmsgsPerGo "+msgsPerGo+"\nmailRead_liveStatus "+mailRead_liveStatus+"\nmailRead_userName  "+mailRead_userName+"\nmailRead_password "+mailRead_password+"\nmailRead_readMailIds "+mailRead_readMailIds+"\npop3Host "+pop3Host+"\ncrmAttchPath:"+crmAttchPath, "ReadMailDaemon.java", "ReadMailDaemon", null , Logger.INFO); 

		String mailFolder = "INBOX"; // INBOX Only the name "INBOX" is supported.
		
		Folder emailFolder = null;
		POP3Store emailStore = null;
		Session emailSession = null;
		Session sessionSMTP = null;
		Transport trSMTP = null;
		
		try 
		{ 
			/*POP3 Connection*/
			
			Properties properties = new Properties();  
			properties.put("mail.pop3.host", pop3Host);
			properties.put("mail.pop3.port", 110);
			properties.put("mail.pop3.keepmessagecontent", "true");
			emailSession = Session.getDefaultInstance(properties);  
			emailStore = (POP3Store) emailSession.getStore(storeType); 

			try
			{
				emailStore.connect(mailRead_userName, mailRead_password); 
			}
			catch(Throwable t)
			{
				Logger.log("emailStore connection error  ", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
				Logger.log("emailStore connection error  For previousMailCount:" + previousMailCount, "ReadMailDaemon.java", "ReadMailDaemonRaiseMail", t, Logger.CRITICAL);
			}
			
			/*POP3 Connection*/

			emailFolder = emailStore.getFolder(mailFolder);
			emailFolder.open(Folder.READ_WRITE);
			
			Logger.log("emailStore connected  "+emailStore.isConnected(), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
			
			Properties propertiesSMTP = null;
			
			try 
			{
				if(mailReadAttachment_reply_mail_from != null && !mailReadAttachment_reply_mail_from.equals(""))
				{
					/*SMTP*/
					
					propertiesSMTP = System.getProperties();
					propertiesSMTP.put("mail.smtp.host",pop3Host); // Same For smtp
					propertiesSMTP.put("mail.smtp.auth", "true"); 
					
					sessionSMTP = Session.getInstance(propertiesSMTP,null);
					
					trSMTP = sessionSMTP.getTransport("smtp");
					
					try
					{
						trSMTP.connect(pop3Host, mailReadAttachment_reply_mail_from, mailReadAttachment_reply_mail_password);
					}
					catch(Throwable t )
					{
						Logger.log("SMTP connection error  ", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
					}
					
					Logger.log("SMTP Session connected  "+trSMTP.isConnected(), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					
					/*SMTP*/
				}
				else
					Logger.log("SMTP Session connected  false", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				
			} 
			catch (Exception e) 
			{
				Logger.log("Error Initializing SMTP Session  ", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
			}
			
			/* Mail Count File*/
			
			unreadMsgs = emailFolder.getUnreadMessageCount();

			if((unreadMsgs - previousMailCount) > msgsPerGo)
				msgCount = previousMailCount + msgsPerGo ;
			else
				msgCount = unreadMsgs ;

			if(unreadMsgs != -1)
			{
				outFileMailCount = new FileOutputStream(fileMailCount);
				
				propsFileMailCount.setProperty("mailRead_LastMailCount", ""+msgCount);
				
				propsFileMailCount.store(outFileMailCount, null);
				
				outFileMailCount.close();
			}
			
			/* Mail Count File*/

			Logger.log("Unread Msgs "+unreadMsgs + "\npreviousMailCount "+previousMailCount + "\ncurrent msgCount "+msgCount, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
			
			
			for (int i = previousMailCount; i < msgCount && unreadMsgs != -1; i++)   // i = previousMailCount
			{  
				msgId = "" + (i+1);
				flowLog = "" ;
				flowLog = msgId;
				Message message = emailFolder.getMessage(i+1);
				this.mimeType = "" ;
				mailContentHTML = "";

				isFound = false ;
				
				String mailId = "" ;
				
				Address addArr [] = message.getFrom();
				
				if(addArr != null && addArr.length >= 1)
				{
					mailId = (addArr[0].toString().replace("<" , "").replace(">" , ""));
				
					Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nExtracted senderName From emailid:"+senderName + "\n original mailId:"+mailId + "message.getFrom() length:"+(addArr == null ? "null" : addArr.length), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}
				
				if((addArr != null && addArr.length >= 1) && (!addArr[0].toString().equals("")))
				{
					//imran khan <imrankhanit12@gmail.com>
					//Provide Support <transcript@providesupport.com>

					senderName = addArr[0].toString();

					senderName = MimeUtility.decodeText(senderName);

					if(senderName.lastIndexOf(" ") > -1)
					{
						senderName = senderName.substring(0 , senderName.lastIndexOf(" "));
					}

					Logger.log("final senderName:"+senderName, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}

				Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nExtracted senderName From emailid:"+senderName, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				mailId = (mailId.split(" "))[mailId.split(" ").length -1];

				String temp [] = new String [5];
				
				temp[0] = "Subject|"+(message.getSubject() == null ? "" : message.getSubject());
				
				subject = (message.getSubject() == null ? "" : message.getSubject());
				
				Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n From Address Array Length:"+(addArr == null ? "null" : addArr.length), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				
				if(addArr != null && addArr.length >= 1)
					temp[1] = "From|"+addArr[0];
				else
					temp[1] = "From|";
				
				Address addArrRecp [] = message.getRecipients(RecipientType.TO);
				
				Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n Recipients Address Array Length:"+(addArrRecp == null ? "null" : addArrRecp.length), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				
				if(addArrRecp != null && addArrRecp.length >= 1)
					temp[2] = "To|"+addArrRecp[0];
				else
					temp[2] = "To|";
					
				temp[3] = ""+(message.getSentDate() == null ? "" : message.getSentDate());
				
				if(!temp[3].equals(""))
					temp[3] =  ("SentDate|") + new SimpleDateFormat("dd-MM-yyy H:mm:ss").format(message.getSentDate());
				else
				{
					temp[3] =  "SentDate|";
					Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nSentDate is null " + mailId, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}
				
				temp[4] = ""+ (message.getReceivedDate() == null ? "" : message.getReceivedDate());
				
				if(!temp[4].equals(""))
					temp[4] = ("ReceivedDate|") + new SimpleDateFormat("dd-MM-yyy H:mm:ss").format(message.getReceivedDate());
				else
					temp[4] = "ReceivedDate|";

				Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nFrom|"+mailId + "\n"+temp[2] +"\n"+temp[0]+"\n"+temp[3]+"\n"+temp[4]+"\n"+temp[0]+"\nMail No|"+msgId, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				Logger.log("From|"+mailId + "\n"+temp[2] +"\n"+temp[0]+"\n"+temp[3]+"\n"+temp[4]+"\n"+temp[0]+"\nMail No|"+msgId, "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);

				if(mailRead_readMailIds.indexOf(mailId) > -1) // From CHAT source mail Id CHAT
				{
					flowLog = flowLog + " | CHAT Mail";
					Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nMail From Authorized Id", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

					Logger.log("Mail Type|CHAT" , "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);

					Object contentChat = message.getContent();

					String  mailContent = "" ;

					if(contentChat instanceof Multipart) 
					{
						Multipart mpChat = (Multipart) contentChat;

						flowLog = flowLog + " | Multipart";
						Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\ngetCount :"+mpChat.getCount(), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

						for (int n=0; n < mpChat.getCount(); n++) 
						{
							BodyPart bpChat = mpChat.getBodyPart(n);

							String mimeTypeChat = bpChat.getContentType();

							Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nBodyPart CHAT " + (n + 1) + " is of MimeType " + mimeTypeChat, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

							Object contentBodyPartChat = bpChat.getContent();

							if (contentBodyPartChat instanceof String) 
							{
								flowLog = flowLog + " | String";
								Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n**This is a String BodyPart CHAT**", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
								Logger.log((String)contentBodyPartChat, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

								mailContent = (String)contentBodyPartChat;

								if(mimeTypeChat.toUpperCase().indexOf("TEXT/PLAIN") > -1)
								{
									this.mimeType = mimeTypeChat ;
								}

								if(mimeTypeChat.toUpperCase().indexOf("TEXT/HTML") > -1)
								{
									this.mimeType = mimeTypeChat ;
								}

								if(mailContent.indexOf("<HTML") > -1 || mailContent.indexOf("<html") > -1)
									break;
							}
							else if (contentBodyPartChat instanceof Multipart) // HERE WE MAY EXTRACT HTML part of CHAT MAIL
							{
								flowLog = flowLog + " | Multipart";
								Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n**This BodyPart is a nested Multipart. CHAT", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
								
								Multipart mpChat1 = (Multipart)contentBodyPartChat;
								
								int countChat = mpChat1.getCount();

								Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nIt has " + countChat +" further BodyParts in it CHAT**", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

								/*Nested Body Parts*/
								
								for (int x=0; x < countChat; x++) 
								{
									BodyPart bpInner = mpChat1.getBodyPart(x);

									mimeTypeChat = bpInner.getContentType();

									Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nBodyPart Inner CHAT" + (x + 1) + " is of MimeType " + mimeTypeChat, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

									Object contentBodyPartInner = bpInner.getContent();

									if (contentBodyPartInner instanceof String) 
									{
										Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n**This is a String BodyPart** Inner CHAT", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
										Logger.log((String)contentBodyPartInner, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

										mailContent = (String)contentBodyPartInner;

										if(mimeTypeChat.toUpperCase().indexOf("TEXT/PLAIN") > -1)
										{
											this.mimeType = mimeTypeChat ;
										}

										if(mimeTypeChat.toUpperCase().indexOf("TEXT/HTML") > -1)
										{
											this.mimeType = mimeTypeChat ;
										}

										if(mailContent.indexOf("<HTML") > -1 || mailContent.indexOf("<html") > -1)
											break;
									}
									else if (contentBodyPartInner instanceof Multipart) 
									{
										Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n**This BodyPart is a nested Multipart. Inner CHAT", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
									}
										
								}
								/*Nested Body Parts*/
							}							
						}
					}
					else if (contentChat instanceof String) 
					{
						flowLog = flowLog + " | String";
						Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n**This is a String Message CHAT**:", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						Logger.log((String)contentChat, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						mailContent = (String)contentChat ;
						this.mimeType = message.getContentType() == null ? "" : message.getContentType();
					}

					if(mailContent.equals(""))
						mailContent = getMessageStream(message);

					Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nMail Content Final:"+mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

					ArrayList<String []>  mailInfoListTemp = new ArrayList<String []>();

					try
					{
						mailInfoListTemp = getChatDetails(mailContent);
					}
					catch(Throwable t)
					{
						Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nError Fetching Data getChatDetails", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
					}

					if(mailInfoListTemp != null && mailInfoListTemp.size() > 0)
					{
						if(mailInfoListTemp.get(0).length > 0)
						{
							int length = mailInfoListTemp.get(0).length + temp.length + 2; //  + x to length to add more columns to resultArray
							
							String resultArray [] = new String[length];
							
							System.arraycopy(temp, 0, resultArray, 0, temp.length);
							System.arraycopy(mailInfoListTemp.get(0), 0, resultArray, temp.length, mailInfoListTemp.get(0).length);
							resultArray[19] = "Attachment|";
							resultArray[20] = "mimeType|"+this.mimeType;
							mailInfoList.add(resultArray);
							Logger.log("mimeType chat:" + this.mimeType, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						}
					}	
				}
				else //for all other incomming email id's OTH
				{
					flowLog = flowLog + " | OTHER Mail";
					Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nMail From Unauthorized Id", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					
					Logger.log("Mail Type|Other" , "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);

					//String tempArr [] = new String [19];
					String tempArr [] = new String [21];
					String sender = "",disposition = "" , attachDt = "" ;
					
					int forwardCount = 0 , autoReplyCount = 0;
					
					boolean contentFound = false;
					
					//Get Sender Email id
					if(temp[1].indexOf(">") > -1 &&  temp[1].indexOf("<") > -1)
						sender =   temp[1].substring(temp[1].indexOf("<") + 1, temp[1].indexOf(">"));
					else
					{
						if(temp[1].indexOf("|") > -1 &&  temp[1].split("\\|").length > 1)
							sender = temp[1].split("\\|")[1];
					}					

					sender = sender.trim();
					//Get Sender Email id

					if(!sender.equals(""))
					{
						flowLog = flowLog + " | Sender not empty";
						String[] tempArr1 = null;
						
						tempArr1 = temp[0].split("\\|");
						
						if(tempArr1 != null && tempArr1.length > 1 && tempArr1[1].length() > 200)
						{
								tempArr[0] = "subject|"+validate("subject" ,200 ,tempArr1[1]);
								tempArr[18] = "Subject:" + tempArr1[1] + "\n";
								Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nOriginal Subject:" + tempArr1[1] + "\nTrimmed Subject:"+tempArr[18], "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						}
						else
							tempArr[0] = temp[0]; //subject
						
						tempArr[1] = ("From|")+ validate("sender" ,200 ,sender); //sender
						
						tempArr1 = temp[2].split("\\|");
						
						if(tempArr1 != null && tempArr1.length > 1)
						{
							tempArr[2] = "To|"+validate("To" ,200 ,tempArr1[1]);
						}
						else
							tempArr[2] = temp[2]; //Recipient
						
						tempArr1 = temp[2].split("\\|");
						
						if(tempArr1 != null && tempArr1.length > 1)
						{
							tempArr[2] = "To|"+validate("To" ,200 ,tempArr1[1]);
						}
						
						tempArr[3] = temp[3]; //SentDate
						tempArr[4] = temp[4]; //ReceivedDate // 5
						
						tempArr[5] = "Visitor|";
						tempArr[6] = "Operator|";
						tempArr[7] = "Company|";
						tempArr[8] = "conversationStarted|";
						tempArr[9] = "conversationFinished|";
						
						String pattern = "[\\/\\-\\?\\(\\)\\,\\\"\\+\\#\\&\\_\\[\\]\\@\\$\\%\\*\\:\\;\\\n\\\r]*";
						senderName = senderName.replaceAll(pattern,"");
						
						tempArr[10] = "Name|"+validate("senderName" ,60 ,senderName);
						tempArr[11] = "mobile|";         // 12

						tempArr[12] = ("email|")+ validate("email" ,200 ,sender); //email  //13

						tempArr[13] = "Country|";
						tempArr[14] = "Question|";
						tempArr[15] = "ipAddress|";
						tempArr[16]  = "Referrer|";
						tempArr[17] = "Browser|";		//18

						Object content = message.getContent();

						String mailContent = "" ;
									
						if(content instanceof Multipart) 
						{
							flowLog = flowLog + " | Multipart";
							Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n**This is a Multipart Message", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

							mailContent = writeMultiPart(content);
							
							// chat 360 details
							HashMap<String , String> hmChatInfo = new HashMap<String , String>();

							subject = subject.trim();

							if(subject.startsWith("Chat") && subject.indexOf("Email Transcript") > -1 && subject.indexOf("Ticket created") == -1) // 360 chat mail
							{
								Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n360Chat", "ReadMailDaemon.java", "jspReadMailDaemon", null, Logger.INFO);

								try
								{
									hmChatInfo =  getChatDetails360(mailContent);
								}
								catch(Throwable t)
								{
									Logger.log("mail Content after writeMultiPart" + mailContent, "ReadMailDaemon.java", "jspReadMailDaemon", t, Logger.CRITICAL);
								}

								String chatInfo = "";

								if(hmChatInfo != null && hmChatInfo.size() > 0)
								{
									//Requestor Name:
									chatInfo = hmChatInfo.get("Requestor Name:") == null ? "" : hmChatInfo.get("Requestor Name:");
									tempArr[5] = "Visitor|" + chatInfo;

									//agent
									chatInfo = hmChatInfo.get("agent") == null ? "" : hmChatInfo.get("agent");
									tempArr[6] = "Operator|"+chatInfo;

									//company
									tempArr[7] = "Company|Customer360";

									//conversationStarted
									tempArr[8] = "conversationStarted|";

									//conversationFinished
									tempArr[9] = "conversationFinished|";

									//Requestor Name:
									chatInfo = hmChatInfo.get("Requestor Name:") == null ? "" : hmChatInfo.get("Requestor Name:");
									tempArr[10] = "Name|"+validate("senderName" ,60 ,chatInfo);

									//Phone Number:
									chatInfo = hmChatInfo.get("Phone Number:") == null ? "" : hmChatInfo.get("Phone Number:");
									tempArr[11] = "mobile|"+chatInfo; 

									//Requestor Email:
									chatInfo = hmChatInfo.get("Requestor Email:") == null ? "" : hmChatInfo.get("Requestor Email:");
									tempArr[12] = ("email|")+ validate("email" ,200 ,chatInfo); //email  //13
									
									//Location:
									chatInfo = hmChatInfo.get("Location:") == null ? "" : hmChatInfo.get("Location:");

									String countryArr [] = chatInfo.split(",");
									
									if(countryArr.length>=2)
										tempArr[13] = "Country|"+ countryArr[1];
									else
										tempArr[13] = "Country|"+chatInfo;

									//Pre-Chat Message:
									chatInfo = hmChatInfo.get("Pre-Chat Message:") == null ? "" : hmChatInfo.get("Pre-Chat Message:");
									tempArr[14] = "Question|" + chatInfo;
								}	
		 
							}
							// chat 360 details

							Logger.log("mail Content after writeMultiPart" + mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

							Logger.log("mimeType final: " + this.mimeType, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

							Multipart mp = (Multipart) content;
							
							/*Attachment Code*/
							attachDt = "" ;
							for (int n=0; n < mp.getCount(); n++) 
							{
								flowLog = flowLog + " | Check ATTACHMENT";
								BodyPart bpAttach = mp.getBodyPart(n);
								
								disposition =  bpAttach.getDisposition();
								
								disposition = disposition == null ? "" : disposition;
								
								Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\ndisposition:"+disposition, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
								
								if(disposition.equalsIgnoreCase(BodyPart.ATTACHMENT) || disposition.equalsIgnoreCase(BodyPart.INLINE))
								{
									flowLog = flowLog + " | ATTACHMENT";
									Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nin side ATTACHMENT : n : "+ n, "ReadMailDaemon.java", "ReadMailDaemonAttachment", null, Logger.INFO);

									Logger.log("Attachment In mail" , "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);

									System.out.println(BodyPart.ATTACHMENT);
									
									DataHandler handler = bpAttach.getDataHandler(); 
									 
									/* To  Forward & Store Attachment Mail */
									 
									try 
									{
										String[] mailReadAttachment_forwardToArr = mailReadAttachment_forwardTo.split(",");
										 
										if(mailReadAttachment_forwardToArr != null && mailReadAttachment_forwardToArr.length > 0)
										{	 
											/*Store Attachment*/
													 
											if(mailReadAttachment_uploadPath != null && !mailReadAttachment_uploadPath.equals(""))
											{
												boolean  isFileStored = false;
												String filePath = "" ;
			
												try
												{
													String fileExt = bpAttach.getFileName() == null ? "" :  bpAttach.getFileName() ;

													if(!fileExt.equals(""))
													{
														String fileExtArr []   = fileExt.split("\\.");

														if(fileExtArr.length >= 2)
														{
															fileExt = "." + fileExtArr[fileExtArr.length-1]; 
														}
														else
														{
															fileExt = "";
														}

														filePath = mailReadAttachment_uploadPath + new Date().getTime() + fileExt ;

														FileOutputStream output = new FileOutputStream(filePath);

														InputStream input = bpAttach.getInputStream();

														byte[] buffer = new byte[4096];

														int byteRead;

														while ((byteRead = input.read(buffer)) != -1) 
														{
															output.write(buffer, 0, byteRead);
														}

														output.close();

														Logger.log("Attachment Stored" , "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);

														isFileStored = true;
													}
													else
													{
														isFileStored = false;
														Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog  + "\nFilename is blnak", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
													}
												}
												catch(Throwable t)
												{
													Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nError Storing attachment", "ReadMailDaemon.java", "ReadMailDaemonAttachment", t, Logger.CRITICAL);
													isFileStored = false;

													Logger.log("nError Storing attachment\n"+"msgId : " + msgId+"\nflowLog:"+flowLog ,"ReadMailDaemon.java", "ReadMailDaemonRaiseMail", t, Logger.CRITICAL);
												}

												if(isFileStored)
												{
													String fileName = "" ; 
													
													String fileNameArr [] = filePath.split("/");

													fileName = fileNameArr[fileNameArr.length - 1];

													String crmFilePath = crmAttchPath;

													flowLog = flowLog + " | ATTACHMENT Stored";
													attachDt += fileName + ":" + crmFilePath + ":" + bpAttach.getFileName() + ":" + "Y" + "?";
													
													//attachDt = fileName:fileName:bpAttach.getFileName():Y?param1:param2:param3:param4?
												}
											}
											else
											{
												flowLog = flowLog + " | ATTACHMENT not Stored";
												Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nAttachment Not Stored", "ReadMailDaemon.java", "ReadMailDaemonAttachment", null, Logger.INFO);
											}
											 
											/*Store Attachment*/

											/* To  Forward  Attachment Mail */

											try
											{
												if(forwardCount == 0)
												{
													Logger.log("Start Time : " + System.currentTimeMillis() , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

													flowLog = flowLog + " | ATTACHMENT Forwarded";
													Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nAttachment", "ReadMailDaemon.java", "ReadMailDaemonAttachment", null, Logger.INFO);
													 
													forwardCount ++ ;
													 
													Message forward = new MimeMessage(sessionSMTP);
													 
													String fromForwd = InternetAddress.toString(addArr);
													 
													forward.setSubject("Fwd :"+ message.getSubject());
													forward.setFrom(new InternetAddress(fromForwd));
													  
													for(int x = 0 ; x < mailReadAttachment_forwardToArr.length; x++)
														forward.addRecipient(RecipientType.TO, new InternetAddress(mailReadAttachment_forwardToArr[x]));
													 
													// Create your new message part
													MimeBodyPart messageBodyPart = new MimeBodyPart();
													messageBodyPart.setText("Oiginal message:\n\n");
													 
													// Create a multi-part to combine the parts
													Multipart multipartForwd = new MimeMultipart();
													multipartForwd.addBodyPart(messageBodyPart);
													 
													// Create and fill part for the forwarded content
													messageBodyPart = new MimeBodyPart();
													messageBodyPart.setDataHandler(message.getDataHandler());
													 
													// Add part to multi part
													multipartForwd.addBodyPart(messageBodyPart);
													 
													// Associate multi-part with message
													forward.setContent(multipartForwd);
													forward.saveChanges();
													 
													// Send message
													 
													//Transport.send(forward);
													trSMTP.sendMessage(forward, forward.getAllRecipients());
													 
													Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nAttachment Sent to:"+mailReadAttachment_forwardTo+"\nAttchment Sent From Customer:"+fromForwd, "ReadMailDaemon.java", "ReadMailDaemonAttachment", null, Logger.INFO);

													Logger.log("Attachment Forwarded" , "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);

													Logger.log("End Time : " + System.currentTimeMillis() , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
														 
												}
											}
											catch(Throwable t)
											{
												Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nError in Forwarding Mail", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
											}
											
											/* To  Forward  Attachment Mail */
										}
									} 
									catch (Exception e) 
									{
										Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nError in Forwarding Mail", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);

										Logger.log("Error in Forwarding Mail or storing attachment\n"+"msgId : " + msgId+"\nflowLog:"+flowLog, "ReadMailDaemon.java", "ReadMailDaemonRaiseMail", e, Logger.CRITICAL);
									}
									 
									 /* To  Forward Attachment Mail */
									 
									 /*Auto Reply Code*/
									 
									try 
									{
										if(mailReadAttachment_reply_mail_flag && mailReadAttachment_reply_mail_from != null && !mailReadAttachment_reply_mail_from.equals(""))
										{
											if(autoReplyCount == 0)
											{
												flowLog = flowLog + " | ATTACHMENT Auto reply";
												autoReplyCount ++;
												 
												Message autoReply = new MimeMessage(sessionSMTP);

												autoReply.setSubject("RE :"+ message.getSubject());
												autoReply.setFrom(new InternetAddress(mailReadAttachment_reply_mail_from));

												autoReply.addRecipients(RecipientType.TO,addArr);

												// Create your new message part
												BodyPart messageBodyPart = new MimeBodyPart();
												messageBodyPart.setText(mailReadAttachment_reply_mail);

												// Create a multi-part to combine the parts
												Multipart multipartAutoRep = new MimeMultipart();
												multipartAutoRep.addBodyPart(messageBodyPart);

												// Associate multi-part with message
												autoReply.setContent(multipartAutoRep);
												autoReply.saveChanges();

												// Send message

												//Transport.send(autoReply);
												trSMTP.sendMessage(autoReply, autoReply.getAllRecipients());

												Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nAutoReply Sent to:"+message.getFrom()+"\nAutoReply Sent From:"+mailReadAttachment_reply_mail_from, "ReadMailDaemon.java", "ReadMailDaemonAttachment", null, Logger.INFO);

												Logger.log("Reply Sent to Customer" , "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);
											}
										 }
									}
									catch (Exception e) 
									{
										Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nError in AutoReply Mail", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);

										Logger.log("Error in AutoReply Mail\n"+"msgId : " + msgId+"\nflowLog:"+flowLog ,"ReadMailDaemon.java", "ReadMailDaemonRaiseMail", e, Logger.CRITICAL);
									}
									 /*Auto Reply Code*/
								}
							}
							
							/*Attachment Code*/
							
							Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nAfter Attachment code", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						}
						else if (content instanceof String) 
						{
							Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\n**This is a String Message**:", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
							Logger.log((String)content, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
							mailContent = (String)content ;
							this.mimeType = message.getContentType() == null ? "" : message.getContentType();
						}
						
						if(mailContent.equals(""))
							mailContent = getMessageStream(message);
						
						Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nmailContent:"+mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

						tempArr[18] = (tempArr[18] == null ? "" : tempArr[18]) + mailContent ; 
						tempArr[18] = "User Chat Details|"+tempArr[18];
						tempArr[19] = "Attachment|"+attachDt;
						tempArr[20] = "mimeType|"+this.mimeType;
						Logger.log("msgId : " + msgId + "\nflowLog : " + flowLog + "\nattachDt:"+tempArr[19], "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						mailInfoList.add(tempArr);

						Logger.log("*********************************Final Each Data (OTHER) *******************************\nmsgId : " + msgId + "\nflowLog : " + flowLog + "\nmailInfoList : "+Arrays.toString(tempArr)  , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					}
				}

				if(mailInfoList != null && mailInfoList.size() > 0)
				{
					Logger.log("*********************************Final Data Start *******************************\nmsgId : " + msgId + "\nflowLog : " + flowLog, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

					for(int iLoop =0; iLoop < mailInfoList.size(); iLoop++)
					{
						Logger.log("mailInfoList : "+Arrays.toString((String[])mailInfoList.get(iLoop)) , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					}

					Logger.log("*********************************Final Data End *******************************", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}

				Logger.log("*********************************Mail Read Data Ends *******************************" , "ReadMailDaemon.java", "MailInfo", null, Logger.INFO);
			}
			
			emailFolder.close(false);  
			emailStore.close();
		} 
		catch (NoSuchProviderException e) 
		{
			Logger.log("Error NoSuchProviderException", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
			Logger.log("Error NoSuchProviderException\n"+"msgId : " + msgId+"\nflowLog:"+flowLog ,"ReadMailDaemon.java", "ReadMailDaemonRaiseMail", e, Logger.CRITICAL);
		}   
		catch (MessagingException e) 
		{
			Logger.log("Error MessagingException", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
			Logger.log("Error MessagingException\n"+"msgId : " + msgId+"\nflowLog:"+flowLog ,"ReadMailDaemon.java", "ReadMailDaemonRaiseMail", e, Logger.CRITICAL);
		}  
		catch (IOException e) 
		{
			Logger.log("Error IOException", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
			Logger.log("Error IOException\n"+"msgId : " + msgId+"\nflowLog:"+flowLog ,"ReadMailDaemon.java", "ReadMailDaemonRaiseMail", e, Logger.CRITICAL);
		}
		finally
		{
			emailSession = null;
			sessionSMTP = null;
			fileMailCount = null;
			outFileMailCount = null;
			trSMTP = null;
		}

		return mailInfoList ;
	}

	public String getMessageStream(Message message)
	{
		String messageTxt = "" ;

		InputStream is = null ; 
		BufferedReader reader = null; 

		try
		{
			is = message.getInputStream();

			reader = new BufferedReader(new InputStreamReader(is));

			StringBuilder out = new StringBuilder();

			String line = "";
			
			while ((line = reader.readLine()) != null) 
			{
				out.append(line);
			}

			messageTxt = out.toString();
		}
		catch(Throwable t)
		{
			Logger.log("Error Fetching Message Stream getMessageStream", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
			messageTxt = "" ;
		}
		finally
		{
			if(reader !=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					Logger.log("Error Closing BufferedReader Connection", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
				}
			}
		}

		return messageTxt ;
	}
	
	public ArrayList<String []> getChatDetails(String content) 
	{
		ArrayList<String []> mailInfoList = new ArrayList<String[]>();
		ArrayList<String> 	mailInfoListTemp = new ArrayList<String>();
		ArrayList<String> mailInfoHeader = new ArrayList<String>();

		String visitor = "" , Userchat ="" , nextHeader = "" ,nextHeaderTemp = "";
		int nextHeadIndex = 0;

		final String [] contentHeaderInfo1 = {"Visitor:","Operator:","Company:","Started:","Finished:"};
		final String [] contentHeaderInfo2 = {"Your Name:","Your Mobile No.:","Your Email Address:","Country:","Your Question:","IP Address:","Referrer:","Browser/OS:"};
		
		int headNotFoundChar = 500 ; // Read 500 Chars if Header is not found 
		try 
		{
			content = content.trim();

			if(content.indexOf("Chat Transcript") > -1 || content.indexOf("chat transcript") > -1 || content.indexOf("CHAT TRANSCRIPT") > -1)
			{
				Userchat = content;
				Userchat = Userchat.replace ("src=\"cid:" , "src=\" ../images/chat/" );
				//Userchat = Userchat.replace("<br />","");
				Userchat = Userchat .replace("Please select the department you would like to reach" , "department to reach");
				Userchat = Userchat .replace("<h1>" , "<h1 align='center'>");

				isFound = true ; 

				for(int index = 0 ; index < contentHeaderInfo1.length ; index++)
				{
					nextHeader = "";
					headNotFoundChar = 500;
					
					if(content.indexOf(contentHeaderInfo1[index]) > -1)
					{
						try
						{
							if(index != contentHeaderInfo1.length-1)
							{
								if(content.indexOf(contentHeaderInfo1[index+1] ,content.indexOf(contentHeaderInfo1[index])) == -1)
								{
									if((content.indexOf(contentHeaderInfo1[index]) + headNotFoundChar) > content.length())
										headNotFoundChar = content.length() - content.indexOf(contentHeaderInfo1[index]);
									
									nextHeaderTemp = content.substring(content.indexOf(contentHeaderInfo1[index]), (content.indexOf(contentHeaderInfo1[index])) + headNotFoundChar);
									
									nextHeadIndex = index + 2;
									
									while(nextHeadIndex < contentHeaderInfo1.length)
									{
										if(nextHeaderTemp.indexOf(contentHeaderInfo1[nextHeadIndex]) > -1)
										{
											nextHeader = contentHeaderInfo1[nextHeadIndex];
											break;
										}
										
										nextHeadIndex ++;
									}
								}
								else
									nextHeader = contentHeaderInfo1[index+1];
								
								if(!nextHeader.equals(""))
								{
									visitor  = content.substring(content.indexOf(contentHeaderInfo1[index]), content.indexOf(nextHeader, content.indexOf(contentHeaderInfo1[index])));
	
									if(content.indexOf("class=\"value-td\"" ,content.indexOf(contentHeaderInfo1[index])) > -1)
									{
										visitor  = (contentHeaderInfo1[index]) + (visitor.substring(visitor.indexOf("class=\"value-td\"") + ("class=\"value-td\"".length()) +1, visitor.lastIndexOf("</td>")));
									}
									else
										visitor = contentHeaderInfo1[index];
			
									if(contentHeaderInfo1[index].equals("Operator:"))
									{
										Pattern pattern = Pattern.compile("<a(.+?)</a>");
			
										Matcher matcher = pattern.matcher(visitor);
			
										visitor = "";
			
										while(matcher.find())
											visitor += matcher.group(1).substring(matcher.group(1).indexOf(">") + 1)  + ",";
			
										if(!visitor.equals(""))
											visitor = "Operator:" + (visitor.substring(0, visitor.length()-1));
										else
											visitor = "Operator:";	
									}
								}
								else
									visitor = contentHeaderInfo1[index];
							}
							else
							{
								if(content.indexOf("</table>" ,content.indexOf(contentHeaderInfo1[index])) > -1 && content.indexOf("class=\"value-td\"" ,content.indexOf(contentHeaderInfo1[index])) > -1) // last index Finished
								{
									visitor = content.substring(content.indexOf(contentHeaderInfo1[index]) , content.indexOf("</table>" , content.indexOf(contentHeaderInfo1[index]))); 
	
									visitor = (contentHeaderInfo1[index]) + (visitor.substring(visitor.indexOf("class=\"value-td\"") + ("class=\"value-td\"".length()) +1, visitor.lastIndexOf("</td>")));
								}
								else
									visitor = contentHeaderInfo1[index];
							}
						}
						catch(Throwable t)
						{
							Logger.log("Error in parsing " + contentHeaderInfo1[index], "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
							visitor = contentHeaderInfo1[index];
						}
					}
					else
					{
						visitor = contentHeaderInfo1[index];
					}

					visitor = visitor.replaceFirst(":", "|").replaceAll("[\\t\\n\\r]+", "");

					String tempArr[] = visitor.trim().split("\\|") ;

					String value  = (tempArr.length == 2) ?  tempArr[1]  : "";
					String header = (tempArr.length <= 2) ?  tempArr[0]  : "";

					if(contentHeaderInfo1[index].equals("Visitor:")) // max length  100
						visitor = (header+"|") + (validate(contentHeaderInfo1[index], 100, value)); 
					else if(contentHeaderInfo1[index].equals("Operator:")) // max length 100 
						visitor = (header+"|") + (validate(contentHeaderInfo1[index], 100, value));  
					else if(contentHeaderInfo1[index].equals("Company:")) // max length 200
						visitor = (header+"|") + (validate(contentHeaderInfo1[index], 200, value));  

					mailInfoListTemp.add(visitor.trim());
					mailInfoHeader.add(header+":");
				}
			}

			if(content.indexOf("Visitor Details") > -1 || content.indexOf("visitor details ") > -1 || content.indexOf("VISITOR DETAILS ") > -1)
			{
				isFound = true ; 

				l1 : for(int index = 0 ; index < contentHeaderInfo2.length ; index++)
				{
					nextHeader = "";
					headNotFoundChar = 500;
					
					if(content.indexOf(contentHeaderInfo2[index]) > -1 || contentHeaderInfo2[index].equals("Your Email Address:") || contentHeaderInfo2[index].equals("Your Name:"))
					{
						try
						{
							if(index != contentHeaderInfo2.length-1)
							{
								if(content.indexOf(contentHeaderInfo2[index+1] ,content.indexOf(contentHeaderInfo2[index])) == -1)
								{	
									
									if((content.indexOf(contentHeaderInfo2[index]) + headNotFoundChar) > content.length())
										headNotFoundChar = content.length() - content.indexOf(contentHeaderInfo2[index]);
										
									if(content.indexOf(contentHeaderInfo2[index]) > -1 && ((content.indexOf(contentHeaderInfo2[index])) + headNotFoundChar) > -1)
										nextHeaderTemp = content.substring(content.indexOf(contentHeaderInfo2[index]), (content.indexOf(contentHeaderInfo2[index])) + headNotFoundChar);
									
									nextHeadIndex = index + 2;
									
									while(nextHeadIndex < contentHeaderInfo2.length)
									{
										if(nextHeaderTemp.indexOf(contentHeaderInfo2[nextHeadIndex]) > -1)
										{
											nextHeader = contentHeaderInfo2[nextHeadIndex];
											break;
										}
										
										nextHeadIndex ++;
									}
								}
								else
								{
									if(contentHeaderInfo2[index].equals("Your Question:") && content.indexOf("Called From:" , content.indexOf("Your Question:")) > -1)
									{
										nextHeader = "";
									}
									else if(contentHeaderInfo2[index].equals("Your Question:") && content.indexOf("LoginId:" , content.indexOf("Your Question:")) > -1)
									{
										nextHeader = "LoginId:";
									}
									else
									{
										nextHeader = contentHeaderInfo2[index+1];
									}
								}
								
								if(content.indexOf(contentHeaderInfo2[index]) == -1 && contentHeaderInfo2[index].equals("Your Email Address:"))
								{
									contentHeaderInfo2[index] = "LoginId:";
									nextHeader = "Name:";
								}

								if(content.indexOf(contentHeaderInfo2[index]) == -1 && contentHeaderInfo2[index].equals("Your Name:"))
								{
									contentHeaderInfo2[index] = "Name:";
									nextHeader = "IP Address:";
								}
								
								if(!nextHeader.equals(""))
								{
									if(content.indexOf(contentHeaderInfo2[index]) == -1 && !contentHeaderInfo2[index].equals("Your Email Address:"))
									{
										mailInfoListTemp.add(contentHeaderInfo2[index] + "|");
										mailInfoHeader.add((contentHeaderInfo2[index]));
										continue l1 ;
									}
			
									if(content.indexOf("Your Email Address:") == -1 && contentHeaderInfo2[index].equals("Your Email Address:") )
									{
										visitor = content .substring(content.indexOf("LoginId:"), content.indexOf("</tr>", content.indexOf("LoginId:")));
	
										if(visitor.indexOf("class=\"value-td\"") > -1)
										{
											visitor = visitor.substring(visitor.indexOf("class=\"value-td\"") + ("class=\"value-td\"").length()+1, visitor.lastIndexOf("</td>"));	
											visitor = "Your Email Address:" + visitor;
										}
										else
											visitor = contentHeaderInfo2[index];
									}
									else
									{
										visitor  = content.substring(content.indexOf(contentHeaderInfo2[index]), content.indexOf(nextHeader, content.indexOf(contentHeaderInfo2[index])));
	
										if(visitor.indexOf("class=\"value-td\"") > -1)
										{
											visitor = (contentHeaderInfo2[index]) + (visitor.substring(visitor.indexOf("class=\"value-td\"") + ("class=\"value-td\"".length()) +1, visitor.lastIndexOf("</td>")));
										}
										else
											visitor = contentHeaderInfo2[index];
										
										if(contentHeaderInfo2[index].equals("Your Question:") && content.indexOf("Your Email Address:") == -1)
										{
											if(visitor.indexOf(">") > -1)
												visitor = (contentHeaderInfo2[index]) + (visitor.substring(0, visitor.indexOf(">")));
										}
									}
			
									if(contentHeaderInfo2[index].equals("IP Address:"))
									{
										String IPADDRESS_PATTERN = 
												"(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
			
										Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
										Matcher matcher = pattern.matcher(visitor);
										visitor = "";
										while (matcher.find()) 
										{
											visitor += matcher.group() + ","; 
										}
			
										if(!visitor.equals(""))
										{
											visitor = visitor.substring(0, visitor.length()-1);
											visitor = "IP Address:"+visitor;
										}
										else
											visitor = "IP Address:";
									}
								}
								else
									visitor = contentHeaderInfo2[index];
							}
							else
							{
								if(content.indexOf("This transcript email message" ,content.indexOf(contentHeaderInfo2[index])) > -1 && content.indexOf("class=\"value-td\"" ,content.indexOf(contentHeaderInfo2[index])) > -1) // last index Browser/OS:
								{
									visitor = content.substring(content.indexOf(contentHeaderInfo2[index]), content.indexOf("This transcript email message", content.indexOf(contentHeaderInfo2[index])));
									visitor = (contentHeaderInfo2[index]) + (visitor.substring(visitor.indexOf("class=\"value-td\"") + ("class=\"value-td\"".length()) +1, visitor.indexOf("</td>" ,visitor.indexOf("class=\"value-td\"") )));
								}
								else
									visitor = contentHeaderInfo2[index];
							}
						}
						catch(Throwable t)
						{
							Logger.log("Error in parsing " + contentHeaderInfo2[index], "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
							visitor = contentHeaderInfo2[index];
						}
					}
					else
					{
						visitor = contentHeaderInfo2[index];
					}

					visitor = visitor.replaceFirst(":", "|").replaceAll("[\\t\\n\\r]+", "");

					String tempArr[] = visitor.trim().split("\\|") ;

					String value  = (tempArr.length == 2) ?  tempArr[1]  : "";
					String header = (tempArr.length <= 2) ?  tempArr[0]  : "";

					if(contentHeaderInfo2[index].equals("Your Name:")) // max length  100
						visitor = (header+"|") + (validate(contentHeaderInfo2[index], 100, value)); 
					else if(contentHeaderInfo2[index].equals("Your Mobile No.:")) // max length  50
						visitor = (header+"|") + (validate(contentHeaderInfo2[index], 50, value)); 
					else if(contentHeaderInfo2[index].equals("Your Email Address:")) // max length  100
						visitor = (header+"|") + (validate(contentHeaderInfo2[index], 100, value));
					else if(contentHeaderInfo2[index].equals("Country:")) // max length  100
						visitor = (header+"|") + (validate(contentHeaderInfo2[index], 100, value));
					else if(contentHeaderInfo2[index].equals("IP Address:")) // max length  100
						visitor = (header+"|") + (validate(contentHeaderInfo2[index], 100, value)); 
					else if(contentHeaderInfo2[index].equals("Referrer:")) // max length  5000
						visitor = (header+"|") + (validate(contentHeaderInfo2[index], 5000, value));
					else if(contentHeaderInfo2[index].equals("Browser/OS:")) // max length  500
						visitor = (header+"|") + (validate(contentHeaderInfo2[index], 500, value));

					mailInfoListTemp.add(visitor.trim());
					mailInfoHeader.add(header+":");
				}
			}
		} 
		catch (Exception e) 
		{	
			Logger.log("Error in getChatDetails", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
		}	

		String arrInfo [] = Arrays.copyOf(mailInfoListTemp.toArray(), mailInfoListTemp.size(), String[].class );

		for(int i = arrInfo.length ;i <(contentHeaderInfo1.length + contentHeaderInfo2.length) ;i++)
			mailInfoHeader.add(i, "");

		if(arrInfo.length > 0)
		{
			if(arrInfo.length != (contentHeaderInfo1.length + contentHeaderInfo2.length ))
			{
				ArrayList<String> tempArrayList = new ArrayList<String>();
				ArrayList<String> tempArrInfo = new ArrayList<String>();

				tempArrayList.addAll(Arrays.asList(contentHeaderInfo1));
				tempArrayList.addAll(Arrays.asList(contentHeaderInfo2));
				tempArrInfo.addAll(Arrays.asList(arrInfo));

				int prevLength = arrInfo.length; 

				for(int j = 0; j < mailInfoHeader.size() ; j++)
				{
					if(!mailInfoHeader.contains(tempArrayList.get(j)))
						mailInfoListTemp.add(prevLength++, tempArrayList.get(j).replace(":", "|"));
				}
			}

			Userchat = Userchat.trim();

			if(Userchat.equals(""))
				mailInfoListTemp.add("User Chat Details|");
			else
				mailInfoListTemp.add("User Chat Details|"+Userchat);

			arrInfo = Arrays.copyOf(mailInfoListTemp.toArray(), mailInfoListTemp.size(), String[].class );
			mailInfoList.add(arrInfo); 
		}

		return mailInfoList ;
	}
	
	public String validate(String field , int maxLength ,String value) 
	{
		 value = value.trim();
		 
		if(!(value.equals("")) && value.length() > maxLength)
		{
			value = value.substring(0, maxLength);

			Logger.log("Field "+field + "  is exceeding"+ (maxLength), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
		}
		
		return value;
	}
	
	public String writeMultiPart(Object contentBodyPart)
	{
		String mailContent = "" , mimeType = "" ;

		Logger.log("IN writeMultiPart", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
		
		try
		{
			Multipart multipart = (Multipart)contentBodyPart;

			int mpCount = multipart.getCount();

			Logger.log("mpCount : " + mpCount, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

			for (int i=0; i < mpCount; i++) 
			{
				BodyPart bpInner = multipart.getBodyPart(i);
				
				mimeType = bpInner.getContentType();

				Logger.log("mimeType2 : " + mimeType, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				Object contentBodyPartInner = bpInner.getContent();

				if (contentBodyPartInner instanceof String) 
				{
					if(mimeType.toUpperCase().indexOf("TEXT/PLAIN") > -1)
					{
						mailContent = (String)contentBodyPartInner;
						this.mimeType = mimeType ;
					}
					
					if(mimeType.toUpperCase().indexOf("TEXT/HTML") > -1)
					{
						mailContentHTML += (String)contentBodyPartInner;
						this.mimeType = mimeType ;
					}
				}
				else if (contentBodyPartInner instanceof Multipart) 
				{
					mailContent = writeMultiPart(contentBodyPartInner);
				}

				Logger.log("fetching mail Content:" + mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				if(mimeType.contains("image/"))
				{
					/*if(bpInner.getFileName() !=null)
						mailContent = writeMailImage(mailContent , bpInner ,bpInner.getFileName());*/
					
					if(bpInner.getFileName() !=null)
						mailContentHTML = writeMailImage(mailContentHTML , bpInner ,bpInner.getFileName());
				}
			}
		}
		catch(Throwable t)
		{	
			Logger.log("Error writing multipart mail", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
		}

		return (!mailContentHTML.equals("") ? mailContentHTML :mailContent);
	}
	
	public String writeMailImage(String mailContent ,BodyPart bodypart , String imgName)
	{
		Logger.log("IN writeMailImage", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
		
		Logger.log("mail Content writeMailImage:" + mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

		try
		{
			Logger.log("content type:" + bodypart.getContentType(), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
			
			String imgPath = mailRead_imagePath + "image" + new Date().getTime() + ".jpg" ;
			//String imgPath = "/app/jboss-eap-6.4/DigiRemit/deployments/digiRemitWAR.war/secure/crm/mailImages/image" + new Date().getTime() + ".jpg" ;

			String imgPath1 = "";
			
			FileOutputStream output = new FileOutputStream(imgPath);

			InputStream input = bodypart.getInputStream();

			byte[] buffer = new byte[4096];

			int byteRead;

			while ((byteRead = input.read(buffer)) != -1) 
			{
				output.write(buffer, 0, byteRead);
			}

			output.close();

			if(mailContent.indexOf("<img") > -1)
			{
	 			String finalStrToRep = "" , strToRep1 = ""  , imgSrc = "" ,replace =  ""; 
				
				Logger.log("Before Replacement mailContent:\n" + mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				if(imgPath.indexOf("/mailImages") + 1 > -1)
				{
					imgPath1 = imgPath.substring(imgPath.indexOf("/mailImages") + 1, imgPath.length());
					Logger.log("imgPath1:" + imgPath1, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}

				String contentIdArr [] = bodypart.getHeader("Content-ID");

				mailContent = mailContent.replace("alt=\"","src=\"");

				if(contentIdArr !=null && contentIdArr.length >= 1)
				{
					replace = contentIdArr[0].replace("<","").replace(">","");
					Logger.log("contentId:" + contentIdArr[0], "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}

				Logger.log("replace:" + ("cid:" + replace), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				
				mailContent = mailContent.replace(("cid:" + replace), imgPath1);
				
				Logger.log("mailContent after replacing image path" + mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				/*{
					imgSrc  = "" ;
					strToRep1 = "" ;
					finalStrToRep = "" ;

					String strToRep= "" , endChar = "" ;

					//strToRep= "src=\"cid";

					if(mailContent.indexOf("src=\"cid") > -1)
					{
						strToRep = "src=\"cid";
						endChar = "\"";
						strToRep1 = "\"cid:";
					}
					
					if(mailContent.indexOf("src=cid") > -1)
					{
						strToRep = "src=cid";
						endChar = "\"";
						
						int index1 = 0 ; //  indexOf space 
						int index2 = 0 ; //  >
						
						index1 = mailContent.indexOf(" " , (mailContent.indexOf(strToRep) + strToRep.length() + 1)); 
						index2 = mailContent.indexOf(">" , (mailContent.indexOf(strToRep) + strToRep.length() + 1)); 
						
						System.out.println("index1:"+index1);
						System.out.println("index2:"+index2);

						Logger.log("index1:" + index1 + " index2:"+ index2 , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						
						if(index1 < index2)
							endChar = " ";
						else
							endChar = ">";
						
						strToRep1 = "cid:";
					}

					Logger.log("strToRep:"+strToRep , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					
					if(!strToRep.equals("") &&  (mailContent.indexOf(strToRep) + strToRep.length() + 1  > -1 && (mailContent.indexOf(endChar , mailContent.indexOf(strToRep)+ strToRep.length())) > -1))
					{	
						Logger.log("Inside image content" , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						
						imgSrc = mailContent.substring(mailContent.indexOf(strToRep) + strToRep.length() +1  , mailContent.indexOf(endChar , mailContent.indexOf(strToRep)+ strToRep.length()));

						Logger.log("imgSrc:" + imgSrc, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						
						if(imgPath.indexOf("/mailImages") + 1 > -1)
						{
							imgPath1 = imgPath.substring(imgPath.indexOf("/mailImages") + 1, imgPath.length());
							System.out.println(imgPath1);
						}

						if(mailContent.indexOf("src=cid") > -1)
						{
							finalStrToRep = (strToRep1 + imgSrc);
							imgPath1 = "\"" + imgPath1 + "\"";
						}
						
						if(mailContent.indexOf("src=\"cid") > -1)
						{
							finalStrToRep = (strToRep1 + imgSrc);
							imgPath1 = "\"" + imgPath1;
						}
						
						Logger.log("Image To Be Replaced:" + (strToRep1 + imgSrc)+"\nReplacing image:"+imgPath1, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						
						Logger.log("mailContent before replacing image path" + mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

						mailContent = mailContent.replaceFirst((strToRep1 + imgSrc), imgPath1);

						Logger.log("mailContent after replacing image path" + mailContent, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					}
				}*/
			}
		}	
		catch(Throwable t)
		{
			Logger.log("Error in stroing mail image ", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
		}

		return mailContent ; 
	}
	
	public HashMap<String, String> getChatDetails360(String content) 
	{
		HashMap<String, String>  hmChatInfo = new HashMap<String, String>();
		
		try 
		{
			final String chatHeaders [] = {"Website:" , "Location:" , "Requestor Name:" , "Requestor Email:" , "Phone Number:" , "Pre-Chat Message:" };
			
			String startIndexStr = "" , endIndexStr = "" , info = "";
			
			l1: for(int i = 0 ; i < chatHeaders.length; i++)
			{
				startIndexStr = chatHeaders[i];
				
				hmChatInfo.put(startIndexStr, null);
				
				for(int j = 0; j < chatHeaders.length; j++)
				{
					endIndexStr= "";
					
					if(!chatHeaders[i].equalsIgnoreCase(chatHeaders[j]))
					{
						if(content.indexOf(startIndexStr) > -1 && content.indexOf(chatHeaders[j], content.indexOf(startIndexStr)) > -1)
						{
							endIndexStr = chatHeaders[j];
							
							info  = content.substring(content.indexOf(startIndexStr) + startIndexStr.length() , content.indexOf(endIndexStr, content.indexOf(startIndexStr)));
							info = filterHTMLTags(info);
							
							hmChatInfo.put(startIndexStr, info);
							
							continue l1;
						}
					}
				}
			}
			
			
			Set<String>	hmChatInfoSet = hmChatInfo.keySet();
			
			startIndexStr = "" ; 
			endIndexStr = "Conversation:" ;
			info = "";
			
			for(String key :hmChatInfoSet)
			{
				if(hmChatInfo.get(key) == null)
					startIndexStr = key;
			}
			
			if(content.indexOf(startIndexStr) > -1 && content.indexOf(endIndexStr, content.indexOf(startIndexStr)) > -1)// pre chat message=question
			{
				info  = content.substring(content.indexOf(startIndexStr) + startIndexStr.length() , content.indexOf(endIndexStr, content.indexOf(startIndexStr)));
				info = filterHTMLTags(info);
				hmChatInfo.put(startIndexStr, info);
			}
			
			startIndexStr = "Conversation:"	;
			endIndexStr = "(Agent) has joined the Chat" ;
			
			if(content.indexOf(startIndexStr) > -1 && content.indexOf(endIndexStr, content.indexOf(startIndexStr)) > -1)// Agent=Operator
			{
				info  = content.substring(content.indexOf(startIndexStr) + startIndexStr.length() , content.indexOf(endIndexStr, content.indexOf(startIndexStr)));
				info = filterHTMLTags(info);
				hmChatInfo.put("agent", info);
			}
			
		} 
		catch (Exception e) 
		{
			Logger.log("Error in 360 chat details ", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
		}
		
		return hmChatInfo;

	}

	public String filterHTMLTags(String commentsVal)
	{
		String filterMsg = "";
		try
		{
			if(commentsVal!=null && (!commentsVal.equals("")))
			{
				commentsVal	= commentsVal.replaceAll("(?s)<!--.*?-->", "");
				commentsVal=commentsVal.replaceAll("<style.+</style>","");
				//commentsVal=commentsVal.replaceAll("<style type=\"text/css\".+</style>","");
				commentsVal	= commentsVal.replaceAll("\\<[^>]*>"," ");					
				commentsVal = commentsVal.replaceAll("&nbsp;"," ");
				filterMsg	= commentsVal.replaceAll("( )+", " ").trim();
				//filterMsg = commentsVal.replaceAll("(?i)<(?!(/?(li|p)))[^>]*>", "").trim();
			}			
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			filterMsg = "";
		}
		
		return filterMsg;
	}
	
	public int addMailDetails(ArrayList mailDetails)
	{
		//com.api.remitGuru.web.controller.SQLRepositoryController repository = new com.api.remitGuru.web.controller.SQLRepositoryController();

		ArrayList quries	                         = new ArrayList();
		ArrayList<ArrayList<Object>> paramsList      = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<Integer>> paramTypesList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Object> params		             = null;
		ArrayList<Integer> paramTypes	             = null;
		StringBuffer insQry		                     = new StringBuffer();
		int insertResult		                     = -1 ;
		boolean  userExists							= false;

		String registrationFlag = "P";

		try
		{
			for(int x = 0; x < mailDetails.size(); x = x+23)
			{
				insQry		                     = new StringBuffer();

				userExists = rgUserCntrl.isUserExists("RG" , (String)mailDetails.get(x+12));
				
				if(userExists)
					registrationFlag = "R";
				else
					registrationFlag = "P";
					

				insQry.append("	INSERT INTO UserChatHistory (	 ");

				insQry.append("		Subject		,				 ");// 0
				insQry.append("		sender		,				 ");// 1
				insQry.append("		Recipient		,			 ");// 2

				insQry.append("		SentDate		,			 ");// 3
				insQry.append("		ReceivedDate		,		 ");// 4

				insQry.append("		Visitor		,				 ");// 5
				insQry.append("		Operator		,			 ");// 6

				insQry.append("		Company		,				 ");// 7
				insQry.append("		conversationStarted		,	 ");// 8
				insQry.append("		conversationFinished		,");// 9

				insQry.append("		Name		,				 ");// 10
				insQry.append("		mobile 		,				 ");// 11
				insQry.append("		email	,					 ");// 12

				insQry.append("		Country 		,			 ");// 13
				insQry.append("		Question 		,			 ");// 14
				insQry.append("		ipAddress 		,			 ");// 15

				insQry.append("		Referrer 		,			 ");// 16
				insQry.append("		Browser 		,			 ");// 17
				insQry.append("		UserChatDetails ,			 ");// 18

				insQry.append("		createdBy 		,			 ");// 19
				insQry.append("		createdDate 	,		     ");// 
				insQry.append("		registrationFlag, 			 "); // 20 registrationFlag 
				insQry.append("		mimeType, 					 "); // 21 mimeType
				insQry.append("		iType 						 "); // 22 iType

				insQry.append("	) VALUES (						 ");// 

				insQry.append("	          ?,                     ");//0
				insQry.append("	          ?,                     ");//1
				insQry.append("	          ?,                     ");//2

				if(!((String)mailDetails.get(x+3)).equals(""))
					insQry.append("	CONVERT(datetime, ? ,5),");//3
				else
					insQry.append("	CONVERT(datetime, GETDATE() ,5),");//3
				
				if(!((String)mailDetails.get(x+4)).equals(""))			
					insQry.append("	CONVERT(datetime, ? ,5),");//4
				else
					insQry.append("	CONVERT(datetime, GETDATE() ,5),");//4

				insQry.append("	          ?,                     ");//5
				insQry.append("	          ?,                     ");//6
				insQry.append("	          ?,                     ");//7


				if(!((String)mailDetails.get(x+8)).equals("") || !((String)mailDetails.get(x+3)).equals(""))	//conversationStarted
					insQry.append("	CONVERT(datetime, ? ,5),");//8
				else
					insQry.append("	CONVERT(datetime, GETDATE() ,5),");//8
		
				if(!((String)mailDetails.get(x+9)).equals("") || !((String)mailDetails.get(x+3)).equals(""))	//conversationFinished
					insQry.append("	CONVERT(datetime, ? ,5),");//9
				else
					insQry.append("	CONVERT(datetime, GETDATE() ,5),");//9

				insQry.append("	          ?,                     ");//10
				insQry.append("	          ?,                     ");//11
				insQry.append("	          ?,                     ");//12

				insQry.append("	          ?,                     ");//13
				insQry.append("	          ?,                     ");//14
				insQry.append("	          ?,                     ");//15

				insQry.append("	          ?,                     ");//16
				insQry.append("	          ?,                     ");//17
				insQry.append("	          ?,                     ");//18

				insQry.append("	          ?,					 ");//19
				insQry.append("	          GETDATE()	,			 ");
				insQry.append("	          ?,					 ");//
				insQry.append("	          ?	,					 ");//20
				insQry.append("	          ?			)			 ");//21

				quries.add(insQry.toString());

				params		= new ArrayList<Object>();
				paramTypes	= new ArrayList<Integer>();

				params.add((String)mailDetails.get(x+0));  //Subject			, 
				paramTypes.add(0);						 			          
				params.add((String)mailDetails.get(x+1));	 //sender			, 
				paramTypes.add(0);						 			          
				params.add((String)mailDetails.get(x+2));	 //Recipient		,         
				paramTypes.add(0);

				if(!((String)mailDetails.get(x+3)).equals(""))
				{
					params.add((String)mailDetails.get(x+3));	 //SentDate		,         
					paramTypes.add(0);
				}
				
				if(!((String)mailDetails.get(x+4)).equals(""))
				{
					params.add((String)mailDetails.get(x+4));	 //ReceivedDate		,         
					paramTypes.add(0);
				}

				params.add((String)mailDetails.get(x+5));	 //Visitor			, 
				paramTypes.add(0);						 			          
				params.add((String)mailDetails.get(x+6));	 //Operator		,         
				paramTypes.add(0);						 		                  
				params.add((String)mailDetails.get(x+7));	 //Company			, 
				paramTypes.add(0);						 			          
				
				if(!((String)mailDetails.get(x+8)).equals(""))
				{
					params.add((String)mailDetails.get(x+8));	 //conversationStarted	,         
					paramTypes.add(0);
				}
				else if(!((String)mailDetails.get(x+3)).equals(""))
				{
					Logger.log("conversationStarted date is blank inserting sentDate", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					params.add((String)mailDetails.get(x+3));	 //conversationStarted	,    add  sentDate if  conversationStarted is blank   
					paramTypes.add(0);
				}
				
				if(!((String)mailDetails.get(x+9)).equals(""))
				{
					params.add((String)mailDetails.get(x+9));	 //conversationFinished	,	  
					paramTypes.add(0);
				}
				else if(!((String)mailDetails.get(x+3)).equals(""))
				{
					Logger.log("conversationFinished date is blank inserting sentDate", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					params.add((String)mailDetails.get(x+3));	 //conversationFinished	,  add  sentDate if  conversationFinished is blnk         
					paramTypes.add(0);
				}

				params.add((String)mailDetails.get(x+10)); //Name			,         
				paramTypes.add(0);						 			          
				params.add((String)mailDetails.get(x+11)); //mobile 			, 
				paramTypes.add(0);						 			          
				params.add((String)mailDetails.get(x+12)); //email			,         
				paramTypes.add(0);						 				  
				params.add((String)mailDetails.get(x+13)); //Country 		,         
				paramTypes.add(0);						 		                  
				params.add((String)mailDetails.get(x+14)); //Question 		,         
				paramTypes.add(0);						 		                  
				params.add((String)mailDetails.get(x+15)); //ipAddress 		,         
				paramTypes.add(0);						 		                  
				params.add((String)mailDetails.get(x+16)); //Referrer 		,         
				paramTypes.add(0);						 		                  
				params.add((String)mailDetails.get(x+17)); //Browser 		,         
				paramTypes.add(0);						 		                  
				params.add((String)mailDetails.get(x+18)); //UserChatDetails		, 
				paramTypes.add(0);						 		                  
				params.add((String)mailDetails.get(x+21)); //createdBy 		,         
				paramTypes.add(0);
				params.add(registrationFlag);//registrationFlag 		,         
				paramTypes.add(0);
				params.add((String)mailDetails.get(x+20)); //mimeType 		,         
				paramTypes.add(0);
				params.add((String)mailDetails.get(x+22)); //iType 		,         
				paramTypes.add(0);

				paramsList.add(params);					 		                  
				paramTypesList.add(paramTypes);					  
																						   
			}

			try
			{
				insertResult = repository.executeBatch(quries, paramsList, paramTypesList, "");
			}
			catch (Throwable t) 
			{
				Logger.log("Error in addMailDetails executing batch", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error in addMailDetails", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
			insertResult = -1;
		}

	 return insertResult ;
	}
	
	/*Methods For CRM starts */

	public ArrayList checkForUserExist(String strGroupId, String strEmailId)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer insQry		        = new StringBuffer();
		ArrayList recordData			= new ArrayList();		

		insQry.append(" SELECT								");
		insQry.append("		crmCustId,						");
		insQry.append("		emailId,						");
		insQry.append("		loginId,						");
		insQry.append("		isRegistered					");
		insQry.append(" FROM CRM_CustomerMasterData			");
		insQry.append("	WHERE 	groupId = ?					");
		insQry.append("	AND 	lower(emailId) = lower( ? )	");
		
		params.add(strGroupId);//groupId
		paramTypes.add(0);

		params.add(strEmailId);//emailId
		paramTypes.add(0);		

		try 
		{
			recordData = repository.getRepositoryItems(insQry, params, paramTypes,strGroupId);	
			
			Logger.log("Check for User Already Exist in CRM_CustomerMasterData: "+recordData.size(), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		catch (Throwable t) 
		{
			Logger.log("Error in checkForUserExist", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			recordData = null;
		}			
		finally
		{
			paramTypes = null;
			params     = null;
			insQry     = null;
		}
		return recordData;
	}	

	/*
		Method for Updating the No of Interaction
	*/
	public int updateInteractionCount(String strUpdatedBy,int ticketId, String groupId)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer insQry		        = new StringBuffer();
		int result						= -1;

		insQry.append(" UPDATE	CRM_TicketInfo  SET						");
		insQry.append("		noOfInteractions = noOfInteractions + 1,	"); 
		insQry.append("		updatedBy	= ?,							");	
		insQry.append("		updatedDate	= GETDATE()						");		
		insQry.append("	WHERE tcktId = ?								");	
		
		params.add(strUpdatedBy);//updatedBy
		paramTypes.add(0);	

		params.add(ticketId);//ticketId
		paramTypes.add(1);

		try 
		{
			result = repository.executeQuery(insQry, params, paramTypes, groupId);	
			
			Logger.log("Update Interaction Count in CRM_TicketInfo table : "+result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		catch (Throwable t) 
		{
			Logger.log("Error in updateInteractionCount", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			result = -1;
		}			
		finally
		{
			paramTypes = null;
			params     = null;
			insQry     = null;
		}
		return result;
	}

	/*
		Method for Checking Existing records with Subject + EmailId
	*/
	public ArrayList validateTicketInfoData(String strEmailId, String strSubject)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer insQry		        = new StringBuffer();
		ArrayList recordData			= new ArrayList();	

		insQry.append(" SELECT									");
		insQry.append("		 b.tcktId							");
		insQry.append(" FROM CRM_CustomerMasterData	a,			");
		insQry.append("		 CRM_TicketInfo b					");
		insQry.append("	WHERE a.crmCustId = b.crmCustId			");
		insQry.append("	AND b.subject like '%" +  strSubject + "%'");
		insQry.append("	AND a.emailId = ?						");
		
		params.add(strEmailId);//emailId
		paramTypes.add(0);	

		try 
		{
			recordData = repository.getRepositoryItems(insQry, params, paramTypes, "");	
			
			Logger.log("Validating Duplicates ticket in CRM_TicketInfo table : "+recordData.size(), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		catch (Throwable t) 
		{
			Logger.log("Error in validateTicketInfoData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			recordData = null;
		}			
		finally
		{
			paramTypes = null;
			params     = null;
			insQry     = null;
		}
		return recordData;
	}

	/*
		Method for Updating the No of Interaction
	*/
	public int updateNoOfTickets(String strUpdatedBy, String strGroupId, int custId)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer insQry		        = new StringBuffer();
		int result						= -1;		

		insQry.append(" UPDATE	CRM_CustomerMasterData  SET	");
		insQry.append("		noOfTickets = noOfTickets + 1,	"); 
		insQry.append("		updatedBy	= ?,				");	
		insQry.append("		updatedDate	= GETDATE()			");		
		insQry.append("	WHERE crmCustId = ?					");	
		insQry.append("	AND groupId = ?						");	
		
		params.add(strUpdatedBy);//updatedBy
		paramTypes.add(0);	
		
		params.add(custId);//crmCustId
		paramTypes.add(1);

		params.add(strGroupId);//groupId
		paramTypes.add(0);			

		try 
		{
			result = repository.executeQuery(insQry, params, paramTypes,strGroupId);	
			
			Logger.log("Update Interaction Count in CRM_TicketInfo table : "+result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		catch (Throwable t) 
		{
			Logger.log("Error in updateInteractionCount", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			result = -1;
		}			
		finally
		{
			paramTypes = null;
			params     = null;
			insQry     = null;
		}
		return result;
	}
	/*
		This method is used to copy the mail attachment file from server
	*/
	public static boolean fileMove(String strFileName,String uploadFlag)
	{
		
		/*Load Property File MailRead*/
		
		String sourceFilePath = "";
		
		File fileMailCount = new File(Constants.configFilePath + "mailRead.properties");

		FileInputStream inFileMailCount = null;

		Properties propsFileMailCount = null;

		try
		{
			inFileMailCount = new FileInputStream(fileMailCount);

			propsFileMailCount = new Properties();

			propsFileMailCount.load(inFileMailCount);

			inFileMailCount.close();
			
			sourceFilePath = propsFileMailCount.getProperty("mailReadAttachment_uploadPath");
			
			sourceFilePath += strFileName ;
		}
		catch(Throwable t)
		{
			Logger.log("Error fetching mailReadAttachment_uploadPath", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
		}
		finally
		{
			inFileMailCount = null;
			
			Logger.log("In fileMove () mailReadAttachment_uploadPath:"+sourceFilePath, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		
		/*Load Property File MailRead*/
		
		/*Load Property File CRM*/
		
		String destinationPath = "";
		
		File crmFile = new File(Constants.configFilePath + "crm.properties");

		FileInputStream inCrmFile = null;

		Properties propsCrmFile = null;

		try
		{
			inCrmFile = new FileInputStream(crmFile);

			propsCrmFile = new Properties();

			propsCrmFile.load(inCrmFile);

			inCrmFile.close();
			
			destinationPath = propsCrmFile.getProperty("crm_default_attachment");
			
			destinationPath += strFileName ;
		}
		catch(Throwable t)
		{
			Logger.log("Error fetching crm_default_attachment", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
		}
		finally
		{
			inCrmFile = null;
			
			Logger.log("In fileMove () crm_default_attachment:"+destinationPath, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		
		/*Load Property File CRM*/
		
		boolean fileAttachmentFlag = false;

		InputStream inStream = null;
		OutputStream outStream = null;
		
		try
		{
			File sourceFile =new File(sourceFilePath);
		    File destinationFile =new File(destinationPath);
			
			if(uploadFlag.equals("Y"))
			{				
				try
				{
					inStream = new FileInputStream(sourceFilePath);
					outStream = new FileOutputStream(destinationPath);

					byte[] buffer = new byte[1024];
					int length;

					//copy the file content in bytes 
					while ((length = inStream.read(buffer)) > 0)
					{
						outStream.write(buffer, 0, length);
					}
					inStream.close();
					outStream.close();

					sourceFile.delete(); 

					Logger.log("Mail Attachment File copied Successfully copied to Location : "+destinationPath, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

					Logger.log("sourceFile.exists() : "+sourceFile.exists(), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
					
					if(!sourceFile.exists())
					{
						Logger.log("Source file is still exist in the location : "+sourceFilePath, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
						fileAttachmentFlag = true;
					}
				}
				catch(Throwable t) 
				{
					fileAttachmentFlag = false;
					Logger.log("Error while Transfering Mail Attachment File", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
				}
			}
			else
			{
				fileAttachmentFlag = true;
			}
		}
		catch (Throwable t) 
		{
			fileAttachmentFlag = false;
			Logger.log("Error in Transfering File from server.", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
		}
		return fileAttachmentFlag;
	}
	/*
		Method for adding Records in the Database
	*/	
	public String addCustomerTicketData(ArrayList customerList,ArrayList fileuploadData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer insQry		        = new StringBuffer();

		ArrayList arUserExistinCRM		= new ArrayList();
		int crmCustId					= -1;
		int crmTicketId					= -1;	
		int interactionId				= -1;
		int attachmentId				= -1;
		String result					= "0";
		String strEmailId				= "";
		String strMobileNo				= "";
		String strLoginId				= "";
		//boolean chkForRegisterUser		= false;	
		ArrayList userExist				= new ArrayList();

		try 
		{
			arUserExistinCRM = checkForUserExist((String)customerList.get(0),(String)customerList.get(8));//groupId,LoginId
		}
		catch (Throwable t) 
		{
			Logger.log("Error while checking Existing User in checkForUserExist : " + customerList, "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);			
		}

		if(arUserExistinCRM != null && arUserExistinCRM.size() > 0)
		{
			crmCustId = Integer.parseInt(((String[]) arUserExistinCRM.get(0))[0]);
			result = "1"+"~"+crmCustId;

			Logger.log("result for existing crmCustId : "+ result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}

		/*if(arUserExistinCRM != null && arUserExistinCRM.size() > 0)
		{
			String[] temp = (String[]) arUserExistinCRM.get(0);
			crmCustId = Integer.parseInt(temp[0]);
			result = "1"+"~"+crmCustId;

			Logger.log("result for existing crmCustId : "+ result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}*/

		try
		{
			//chkForRegisterUser = rgUserCntrl.isUserExists((String)customerList.get(0),(String)customerList.get(8));//groupId,LoginId
			userExist = rgUserCntrl.getUserDetails((String)customerList.get(0),(String)customerList.get(8));//groupId,LoginId
			Logger.log("Check for Already Registered User : "+userExist.size(), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

			if(userExist != null && userExist.size() > 0)
			{
				strEmailId = ((String[]) userExist.get(0))[5];
				strMobileNo = ((String[]) userExist.get(0))[15];
				strLoginId = ((String[]) userExist.get(0))[3];
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error while checking User Exist : checkForUserExist", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			//chkForRegisterUser = false;
			userExist = null;
		}
		if(!customerList.get(2).equals(""))//Check for Ticket Id
		{
			customerList.set(3, (""+crmCustId));			
			result = updateTicketInfo(customerList,fileuploadData,strEmailId,strMobileNo,strLoginId);//passing emailId, MobileNo,loginId
		}
		else
		{
			if(crmCustId == -1)// Email Id is not exist in the Database
			{	
				insQry.append(" INSERT INTO CRM_CustomerMasterData (	");
				insQry.append("		groupId,							");
				insQry.append("		userId,								");
				insQry.append("		firstName,							");
				insQry.append("		lastName,							");
				insQry.append("		loginId,							");
				insQry.append("		mobile,								");
				insQry.append("		emailId,							");
				insQry.append("		isRegistered,						");
				insQry.append("		noOfTickets,						");
				insQry.append("		isActive,							");
				insQry.append("		createdBy,							");
				insQry.append("		createdDate,						");
				insQry.append("		updatedBy,							");
				insQry.append("		upDatedDate) VALUES (				");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		GETDATE(), 							");
				insQry.append("		? , 								");
				insQry.append("		GETDATE() ) 						");
				
				/*try
				{
					//chkForRegisterUser = rgUserCntrl.isUserExists((String)customerList.get(0),(String)customerList.get(8));//groupId,LoginId
					userExist = rgUserCntrl.getUserDetails((String)customerList.get(0),(String)customerList.get(8));//groupId,LoginId
					Logger.log("Check for Already Registered User : "+userExist.size(), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
				}
				catch (Throwable t) 
				{
					Logger.log("Error while checking User Exist : checkForUserExist", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
					//chkForRegisterUser = false;
					userExist = null;
				}*/	
				
				params.add((String)customerList.get(0));//groupId
				paramTypes.add(0);

				params.add((String)customerList.get(1));//userId
				paramTypes.add(0);

				params.add((String)customerList.get(6));//firstName
				paramTypes.add(0);

				params.add((String)customerList.get(7));//lastName
				paramTypes.add(0);

				params.add((String)customerList.get(4));//loginId
				paramTypes.add(0);

				params.add((String)customerList.get(9));//mobileNo
				paramTypes.add(0);

				params.add((String)customerList.get(8));//emailId
				paramTypes.add(0);

				//if(chkForRegisterUser)
				if(userExist != null && userExist.size() > 0)
				{
					params.add("Y");//isRegistered
					paramTypes.add(0);
				}
				else
				{
					params.add("N");
					paramTypes.add(0);
				}
				params.add(Integer.parseInt(customerList.get(17).toString()));//noOfTickets
				paramTypes.add(1);

				params.add((String)customerList.get(18));//isActive
				paramTypes.add(0);

				params.add((String)customerList.get(5));//createdBy
				paramTypes.add(0);

				params.add((String)customerList.get(5));//updatedBy
				paramTypes.add(0);
			
				try 
				{
					crmCustId = repository.retrieveIdentity(insQry, params, paramTypes,(String)customerList.get(0));
					result	= "1"+"~"+crmCustId;				
				}
				catch (Throwable t) 
				{
					Logger.log("Error while inserting records in CRM_CustomerMasterData : addCustomerTicketData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
					crmCustId	= -1;
					result		= "0"+"~"+crmCustId;
				}
				finally
				{	
					paramTypes = null;
					params     = null;
					insQry     = null;
				}
			}		

			Logger.log("result after CRM_CustomerMasterData : " + result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

			/*try 
			{
				//Checking for existing records with emailId + subject
				ArrayList arCrmTicketId = validateTicketInfoData((String)customerList.get(8),(String)customerList.get(10));			

				if(arCrmTicketId !=null && arCrmTicketId.size() > 0)
				{				
					String [] temp	= (String [])arCrmTicketId.get(0);	
					crmTicketId = Integer.parseInt(temp[0]);
				}
				else
				{
					crmTicketId = -1;
				}
			}
			catch (Throwable t) 
			{
				Logger.log("Error in validateTicketInfoData : " + crmTicketId, "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);	
				crmTicketId = -1;
			}*/

			if(crmCustId != -1 && crmTicketId == -1) // Record already exist in the CRM_CustomerMasterData Table == crmCustId not null and CRM_TicketInfo  not null
			{
				params		= new ArrayList<Object>();
				paramTypes	= new ArrayList<Integer>();
				insQry		= new StringBuffer();

				insQry.append(" INSERT INTO CRM_TicketInfo (	");
				insQry.append("		crmCustId,					");
				insQry.append("		assignedTo,					");
				insQry.append("		statusId,					");
				insQry.append("		comments,					");
				insQry.append("		priority,					");
				insQry.append("		categoryId,					");
				//insQry.append("		dueDate,					");
				insQry.append("		noOfInteractions,			");
				insQry.append("		subject,					");
				insQry.append("		description,				");
				insQry.append("		isActive,					");
				insQry.append("		createdBy,					");
				insQry.append("		createdDate,				");
				insQry.append("		updatedBy,					");
				insQry.append("		upDatedDate,				");
				insQry.append("		custUpdatedDate,			");
				insQry.append("		source,						");
				insQry.append("		mimeType,					");
				insQry.append("		sentDate) VALUES (			");
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				//insQry.append("		GETDATE() ,					");//dueDate
				insQry.append("		? ,							");//noOfInteractions
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				insQry.append("		? , 						");
				insQry.append("		GETDATE(), 					");
				insQry.append("		? , 						");
				insQry.append("		GETDATE(),	 				");
				insQry.append("		GETDATE(),	 				");
				insQry.append("		? , 						");		
				
				insQry.append("		?,  						");	//MIMEType

				if(!((String)customerList.get(25)).equals(""))
					insQry.append("	CONVERT(datetime, ? ,5) )");
				else
					insQry.append("	CONVERT(datetime, GETDATE() ,5) )");
				
				params.add(crmCustId);//cust Id
				paramTypes.add(1);
				try
				{
					params.add(Integer.parseInt(customerList.get(13).toString()));//assignTo
					paramTypes.add(1);
				}
				catch (Throwable t) 
				{
					Logger.log("Error while parsing assignTo value : addCustomerTicketData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
				}
				try
				{
					params.add(Integer.parseInt(customerList.get(14).toString()));//statusId
					paramTypes.add(1);
				}
				catch (Throwable t) 
				{
					Logger.log("Error while parsing statusId : addCustomerTicketData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
				}

				params.add((String)customerList.get(12));//solution/Comments field description in ticketInfo table
				paramTypes.add(0);

				params.add((String)customerList.get(15));//priority
				paramTypes.add(0);
				
				try
				{
					params.add(Integer.parseInt(customerList.get(16).toString()));//category
					paramTypes.add(1);	
				}
				catch (Throwable t) 
				{
					Logger.log("Error while parsing category : addCustomerTicketData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
				}
			
				//If no comments is entered then noOfInteraction is set to zero else set to one.
				/*if(customerList.get(12).equals(""))
				{
					params.add(0);//noOfInteractions
					paramTypes.add(1);
				}
				else
				{
					params.add(1);//noOfInteractions
					paramTypes.add(1);
				}*/

				params.add(1);//noOfInteractions
				paramTypes.add(1);

				params.add((String)customerList.get(10));//subject
				paramTypes.add(0);			

				params.add((String)customerList.get(11));//message/Description
				paramTypes.add(0);

				params.add((String)customerList.get(18));//isActive
				paramTypes.add(0);

				params.add((String)customerList.get(5));//createdBy
				paramTypes.add(0);

				params.add((String)customerList.get(5));//updatedBy
				paramTypes.add(0);

				params.add((String)customerList.get(19));//source
				paramTypes.add(0);

				params.add(((String)customerList.get(27)).toLowerCase());//mimeType
				paramTypes.add(0);

				if(!((String)customerList.get(25)).equals(""))
				{
					params.add((String)customerList.get(25));//sentDate
					paramTypes.add(0);
				}
									
				try 
				{				
					crmTicketId = repository.retrieveIdentity(insQry, params, paramTypes, (String)customerList.get(0));
					if(crmTicketId != -1)
					{
						updateNoOfTickets((String)customerList.get(5), (String)customerList.get(0), crmCustId);// Updating noOfTicket in Customer Master Table
					}
					result = result + "|" + "1" + "~" + crmTicketId;								
				}
				catch (Throwable t) 
				{
					Logger.log("Error while inserting records CRM_TicketInfo table:addCustomerTicketData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
					crmTicketId = -1;
					result = result + "|"  +  "0" + "~" + crmTicketId;				
				}
				finally
				{			
					paramTypes = null;
					params     = null;
					insQry     = null;
				}
			}
			else
			{
				updateInteractionCount((String)customerList.get(5),crmTicketId, (String)customerList.get(0));//updating CRM_TicketInfo noOfInteraction column
				result = result + "|"  +  "0" + "~" + crmTicketId;		
			}

			Logger.log("result after CRM_TicketInfo : " + result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

			//if(crmTicketId != -1 && (!customerList.get(12).toString().equals("")))//comments/solution field is not null
			if(crmTicketId != -1)
			{
				params		= new ArrayList<Object>();
				paramTypes	= new ArrayList<Integer>();
				insQry		= new StringBuffer();

				insQry.append(" INSERT INTO CRM_Interactions (			");
				insQry.append("		tcktId,								");
				insQry.append("		subject,							");
				insQry.append("		description,						");
				insQry.append("		statusId,							");
				insQry.append("		sendMailFlag,						");	
				insQry.append("		interactionType,					");	
				insQry.append("		mimeType,							");	
				insQry.append("		createdBy,							");
				insQry.append("		createdDate) VALUES (				");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				insQry.append("		? , 								");
				//insQry.append("		GETDATE())	 						");				
				if(!((String)customerList.get(25)).equals(""))
					insQry.append("	CONVERT(datetime, ? ,5) )");
				else
					insQry.append("	CONVERT(datetime, GETDATE() ,5) )");			
						
				params.add(crmTicketId);//cust Id
				paramTypes.add(1);

				params.add((String)customerList.get(10));//subject
				paramTypes.add(0);			

				params.add((String)customerList.get(11));//description
				paramTypes.add(0);

				try
				{
					params.add(Integer.parseInt(customerList.get(14).toString()));//status
					paramTypes.add(1);
				}
				catch (Throwable t) 
				{
					Logger.log("Error while parsing status value : addCustomerTicketData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
				}

				params.add((String)customerList.get(23));//sendMailFlag
				paramTypes.add(0);

				params.add((String)customerList.get(20));//inBound
				paramTypes.add(0);

				params.add(((String)customerList.get(27)).toLowerCase());//mimeType
				paramTypes.add(0);

				params.add((String)customerList.get(5));//createdBy
				paramTypes.add(0);	
				
				if(!((String)customerList.get(25)).equals(""))
				{
					params.add((String)customerList.get(25));//sentDate
					paramTypes.add(0);
				}
						
				try 
				{				
					interactionId = repository.retrieveIdentity(insQry, params, paramTypes, (String)customerList.get(0));				
					result = result + "|" + "1" + "~" + interactionId;			
				}
				catch (Throwable t) 
				{
					Logger.log("Error while inserting records in CRM_Interactions:addCustomerTicketData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);

					interactionId = -1;				
					result = result + "|" + "0" + "~" + interactionId;
				}
				finally
				{			
					paramTypes = null;
					params     = null;
					insQry     = null;
				}
			}
			else
			{
				result = result + "|" + "0" + "~" + interactionId;
			}

			Logger.log("result after CRM_Interactions : "+result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

			if(crmTicketId!= -1 && fileuploadData.size() > 0 )//comments/solution field is not null
			{
				Logger.log("fileuploadData.size() : "+fileuploadData.size(), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

				boolean fileUploadFlag = false;

				for(int k=0; k < fileuploadData.size(); k=k+4)
				{
					params		= new ArrayList<Object>();
					paramTypes	= new ArrayList<Integer>();
					insQry		= new StringBuffer();

					if(fileuploadData.get(k+3).toString().equals("Y"))
					{

						fileUploadFlag = fileMove(fileuploadData.get(k).toString(),fileuploadData.get(k+3).toString());

						Logger.log("inside for loop uploda flag : "+fileUploadFlag, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
					}
					else
					{
						fileUploadFlag = true;
					}

					if(fileUploadFlag)
					{

						insQry.append(" INSERT INTO CRM_Attachments (			");
						insQry.append("		tcktId,								");
						insQry.append("		interactionId,						");
						insQry.append("		fileName,							");
						insQry.append("		filePath,							");
						insQry.append("		customerFileName,					");
						insQry.append("		isActive,							");			
						insQry.append("		createdBy,							");
						insQry.append("		createdDate) VALUES (				");
						insQry.append("		? , 								");
						insQry.append("		? , 								");
						insQry.append("		? , 								");
						insQry.append("		? , 								");
						insQry.append("		? , 								");
						insQry.append("		? , 								");
						insQry.append("		? , 								");
						insQry.append("		GETDATE())	 						");	
						
						Logger.log("values : crmTicketId:"+crmTicketId+" interactionId: "+interactionId+" FileName:"+(String)fileuploadData.get(k)+" File Path : "+(String)fileuploadData.get(k+1)+"customer File Name : "+(String)fileuploadData.get(k+2), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
											
						params.add(crmTicketId);//ticket Id
						paramTypes.add(1);

						params.add(interactionId);//interaction Id
						paramTypes.add(1);
					
						params.add((String)fileuploadData.get(k));//file Name
						paramTypes.add(0);			

						params.add((String)fileuploadData.get(k+1));//file Path
						paramTypes.add(0);	
						
						params.add((String)fileuploadData.get(k+2));//customerFileName
						paramTypes.add(0);			

						params.add((String)customerList.get(18));//isActiveFlag
						paramTypes.add(0);	
											
						params.add((String)customerList.get(5));//createdBy
						paramTypes.add(0);	
											
						try 
						{
							attachmentId = repository.retrieveIdentity(insQry, params, paramTypes, (String)customerList.get(0));					
							
							result = result + "|" + "1" + "~" + attachmentId;					
						}
						catch (Throwable t) 
						{
							Logger.log("Error while inserting records in CRM_Attachments:insertAttachedFileData", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);

							attachmentId = -1;
							result = result + "|"  + "0"+"~" + attachmentId;
						}
						finally
						{			
							paramTypes = null;
							params     = null;
							insQry     = null;
						}
					}
					else
					{

					}
				}
			}
			else
			{
				result = result + "|"  + "0"+"~" + attachmentId;
			}

			Logger.log("result after CRM_Attachments : "+result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);			
		}
		return result;
	}

	/*
		Method for updating Records in the Database
	*/	
	public String updateTicketInfo(ArrayList customerList,ArrayList fileuploadData,String strEmailId, String strMobileNo,String loginId)//2 ticket,3 custId
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer insQry		        = new StringBuffer();
		int updateCustMaster			= -1;	
		int updateTranMaster			= -1;
		int updateInteractionMaster		= -1;
		int interactionId				= -1;
		int attachmentId				= -1;
		int crmTicketId					= -1;
		String result					= "0";

		Logger.log("Inside update Method ", "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);		

		insQry.append(" UPDATE						");
		insQry.append("		CRM_CustomerMasterData	");
		insQry.append(" SET							");
		insQry.append("		firstName = ?,			");
		insQry.append("		lastName = ?,			");
		insQry.append("		loginId = ?,			");
		insQry.append("		mobile = ?,				");
		insQry.append("		emailId = ?,			");		
		insQry.append("		updatedBy = ?,			");
		insQry.append("		updatedDate = getDate()	");
		insQry.append("	WHERE  crmCustId = ?		");
		insQry.append("	AND  groupId = 'RG'			");


		params.add((String)customerList.get(6));//firstName
		paramTypes.add(0);
		
		params.add((String)customerList.get(7));//lastName
		paramTypes.add(0);

		//If mobile No and emailId is not null in UserMaster table then update mobile No and Email Id from user Master Table else update data getting from Form
		
		if(!loginId.equals(""))
		{
			params.add(loginId);//loginId
			paramTypes.add(0);
		}
		else
		{
			params.add((String)customerList.get(4));//loginId
			paramTypes.add(0);
		}		
		if(!strEmailId.equals(""))
		{
			params.add(strMobileNo);//mobileNo
			paramTypes.add(0);
		}
		else
		{
			params.add((String)customerList.get(9));//mobileNo
			paramTypes.add(0);
		}
		if(!strMobileNo.equals(""))
		{
			params.add(strEmailId);//emailId
			paramTypes.add(0);
		}
		else
		{
			params.add((String)customerList.get(8));//emailId
			paramTypes.add(0);
		}

		params.add((String)customerList.get(5));//updatedBy
		paramTypes.add(0);
		
		try
		{
			params.add(Integer.parseInt(customerList.get(3).toString()));//customerId
			paramTypes.add(1);
		}
		catch (Throwable t) 
		{
			Logger.log("Error while parsing customerId:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
		}
		
		try 
		{
			updateCustMaster = repository.executeQuery(insQry, params, paramTypes, (String)customerList.get(0));	// Required RG
			result	= "1"+"~"+customerList.get(3).toString();
			
			Logger.log("Count after updating records in CRM_CustomerMasterData : "+updateCustMaster, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		}
		catch (Throwable t) 
		{
			Logger.log("Error while updating CRM_CustomerMasterData:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);

			updateCustMaster = -1;
			result		= "0"+"~"+customerList.get(3).toString();
		}			
		finally
		{			
			paramTypes = null;
			params     = null;
			insQry     = null;
		}

		/*try 
		{
			//Checking for existing records with emailId + subject
			ArrayList arCrmTicketId = validateTicketInfoData((String)customerList.get(8),(String)customerList.get(10));			

			if(arCrmTicketId !=null && arCrmTicketId.size() > 0)
			{				
				String [] temp	= (String [])arCrmTicketId.get(0);	
				crmTicketId = Integer.parseInt(temp[0]);
			}
			else
			{
				crmTicketId = -1;
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error in validateTicketInfoData : " + crmTicketId, "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);	
			crmTicketId = -1;
		}*/
		Logger.log("updateCustMaster : "+updateCustMaster+" crmTicketId : "+crmTicketId+"Flag Val : "+(String)customerList.get(24), "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

		if(updateCustMaster != -1 && ((String)customerList.get(24)).equals("Y"))  //Record updated successfully in the Customer Master Table
		{			
			params		= new ArrayList<Object>();
			paramTypes	= new ArrayList<Integer>();
			insQry		= new StringBuffer();
		
			insQry.append(" UPDATE						");
			insQry.append("		CRM_TicketInfo			");
			insQry.append(" SET							");
			insQry.append("		assignedTo = ?,			");
			insQry.append("		statusId = ?,			");
			insQry.append("		comments = ?,			");
			insQry.append("		priority = ?,			");
			insQry.append("		categoryId = ?,			");
			insQry.append("		dueDate = getDate(),	");
			insQry.append("		subject = ?,			");
			insQry.append("		description = ?,		");
			insQry.append("		source = ?,				");
			insQry.append("		updatedBy = ?,			");
			insQry.append("		updatedDate = getDate(),	");
			
			if(!((String)customerList.get(25)).equals(""))
					insQry.append("	sentDate = CONVERT(datetime, ? ,5)  ");
				else
					insQry.append("	sentDate =  CONVERT(datetime, GETDATE() ,5)  ");

			insQry.append("	WHERE  tcktId = ?			");		
			
			try
			{		
				params.add(Integer.parseInt(customerList.get(13).toString()));//assignedTo
				paramTypes.add(1);
			}
			catch (Throwable t) 
			{
				Logger.log("Error while parsing assignTo:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			}

			try
			{
				params.add(Integer.parseInt(customerList.get(14).toString()));//statusId
				paramTypes.add(1);
			}
			catch (Throwable t) 
			{
				Logger.log("Error while parsing statusId:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			}

			params.add((String)customerList.get(12));//solution
			paramTypes.add(0);

			params.add((String)customerList.get(15));//priority
			paramTypes.add(0);
			
			try
			{
				params.add(Integer.parseInt(customerList.get(16).toString()));//categoryId
				paramTypes.add(1);			
			}
			catch (Throwable t) 
			{
				Logger.log("Error while parsing categoryId:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			}
			
			params.add((String)customerList.get(10));//subject
			paramTypes.add(0);

			params.add((String)customerList.get(11));//message/description 
			paramTypes.add(0);

			params.add((String)customerList.get(19));//source
			paramTypes.add(0);

			params.add((String)customerList.get(5));//updatedBy
			paramTypes.add(0);

			if(!((String)customerList.get(25)).equals(""))
			{
				params.add((String)customerList.get(25));//sentDate
				paramTypes.add(0);
			}
			
			try
			{
				params.add(Integer.parseInt(customerList.get(2).toString()));//ticketId
				paramTypes.add(1);	
			}
			catch (Throwable t) 
			{
				Logger.log("Error while parsing ticketId:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			}
			try 
			{
				updateTranMaster = repository.executeQuery(insQry, params, paramTypes, (String)customerList.get(0));	
				result = result + "|" + "1" + "~" + customerList.get(2).toString();	
				
				Logger.log("Count After updating CRM_TicketInfo : "+updateTranMaster, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
			}
			catch (Throwable t) 
			{
				Logger.log("Error while updating CRM_TicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);

				updateTranMaster = -1;
				result = result + "|"  +  "0" + "~" + customerList.get(2).toString();
			}			
			finally
			{
				paramTypes = null;
				params     = null;
				insQry     = null;
			}			
		}
		else
		{
			updateTranMaster = updateInteractionCount((String)customerList.get(5),Integer.parseInt(customerList.get(2).toString()),(String)customerList.get(0));//updating CRM_TicketInfo noOfInteraction column
			result = result + "|"  +  "0" + "~" + customerList.get(2).toString();		
		}
		//if(updateTranMaster != -1 && (!customerList.get(12).toString().equals("")))//comments not null
		//if(updateTranMaster != -1 && (!((String)customerList.get(12)).equals("")))//comments not null
		if(updateTranMaster != -1)
		{
			params		= new ArrayList<Object>();
			paramTypes	= new ArrayList<Integer>();
			insQry		= new StringBuffer();

			insQry.append(" INSERT INTO CRM_Interactions (			");
			insQry.append("		tcktId,								");
			insQry.append("		subject,							");
			insQry.append("		description,						");
			insQry.append("		statusId,							");
			insQry.append("		sendMailFlag,						");	
			insQry.append("		interactionType,					");	
			insQry.append("		mimenType,							");	
			insQry.append("		createdBy,							");
			insQry.append("		createdDate) VALUES (				");
			insQry.append("		? , 								");
			insQry.append("		? , 								");
			insQry.append("		? , 								");
			insQry.append("		? , 								");
			insQry.append("		? , 								");
			insQry.append("		? , 								");
			insQry.append("		? , 								");
			//insQry.append("		GETDATE())	 						");						
			if(!((String)customerList.get(25)).equals(""))
					insQry.append("	CONVERT(datetime, ? ,5) )");
				else
					insQry.append("	CONVERT(datetime, GETDATE() ,5) )");
			
			try
			{
				params.add(Integer.parseInt(customerList.get(2).toString()));//ticketId
				paramTypes.add(1);
			}
			catch (Throwable t) 
			{
				Logger.log("Error while parsing ticketId:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
			}

			params.add((String)customerList.get(10));//subject
			paramTypes.add(0);			

			params.add((String)customerList.get(11));//message/description
			paramTypes.add(0);

			params.add(Integer.parseInt(customerList.get(14).toString()));//status
			paramTypes.add(1);

			params.add((String)customerList.get(23));//sendMailFlag
			paramTypes.add(0);

			params.add((String)customerList.get(20));//inBound
			paramTypes.add(0);

			params.add(((String)customerList.get(27)).toLowerCase());//mimeType
			paramTypes.add(0);

			params.add((String)customerList.get(5));//createdBy
			paramTypes.add(0);	
			
			if(!((String)customerList.get(25)).equals(""))
			{
				params.add((String)customerList.get(25));//sentDate
				paramTypes.add(0);
			}
						
			try 
			{				
				interactionId = repository.retrieveIdentity(insQry, params, paramTypes, (String)customerList.get(0));				
				result = result + "|" + "1" + "~" + interactionId;

				Logger.log("result in update method : " + result + " Count After inserting records in CRM_Interactions : "+interactionId, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
			}
			catch (Throwable t) 
			{
				Logger.log("Error while inserting records in  CRM_Interactions in update Condition", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);

				interactionId = -1;				
				result = result + "|" + "0" + "~" + interactionId;
			}
			finally
			{			
				paramTypes = null;
				params     = null;
				insQry     = null;
			}
		}
		else
		{
			result = result + "|" + "0" + "~" + interactionId;
		}		
		if(Integer.parseInt(customerList.get(2).toString()) != -1 && fileuploadData.size() > 0 )
		{
			boolean fileUploadFlag = false;

			for(int k=0; k < fileuploadData.size(); k=k+4)
			{
				params		= new ArrayList<Object>();
				paramTypes	= new ArrayList<Integer>();
				insQry		= new StringBuffer();

				//fileUploadFlag = fileMove(fileuploadData.get(k).toString(),fileuploadData.get(k+3).toString());

				//Logger.log("Inside updateTicketInfo method File Attachment upload flag : "+fileUploadFlag, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);

				if(fileuploadData.get(k+3).toString().equals("Y"))
				{
					fileUploadFlag = fileMove(fileuploadData.get(k).toString(),fileuploadData.get(k+3).toString());
					Logger.log("inside for loop uploda flag : "+fileUploadFlag, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
				}
				else
				{
					fileUploadFlag = true;
				}

				if(fileUploadFlag)
				{
					insQry.append(" INSERT INTO CRM_Attachments (	");
					insQry.append("		tcktId,						");
					insQry.append("		interactionId,				");
					insQry.append("		fileName,					");
					insQry.append("		filePath,					");
					insQry.append("		customerFileName,			");
					insQry.append("		isActive,					");			
					insQry.append("		createdBy,					");
					insQry.append("		createdDate) VALUES (		");
					insQry.append("		? , 						");
					insQry.append("		? , 						");
					insQry.append("		? , 						");
					insQry.append("		? , 						");
					insQry.append("		? , 						");
					insQry.append("		? , 						");
					insQry.append("		? , 						");
					insQry.append("		GETDATE())	 				");	
					
					try
					{
						params.add(Integer.parseInt(customerList.get(2).toString()));//ticketId
						paramTypes.add(1);
					}
					catch (Throwable t) 
					{
						Logger.log("Error while parsing ticketId:updateTicketInfo", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);
					}

					params.add(interactionId);//interaction Id
					paramTypes.add(1);

					params.add((String)fileuploadData.get(k));//file Name
					paramTypes.add(0);			

					params.add((String)fileuploadData.get(k+1));//file Path
					paramTypes.add(0);
					
					params.add((String)fileuploadData.get(k+2));//customer File Name
					paramTypes.add(0);		

					params.add((String)customerList.get(18));//isActiveFlag
					paramTypes.add(0);	
										
					params.add((String)customerList.get(5));//createdBy
					paramTypes.add(0);	
										
					try 
					{
						attachmentId = repository.retrieveIdentity(insQry, params, paramTypes, (String)customerList.get(0));					
						
						result = result + "|" + "1" + "~" + attachmentId;

						Logger.log("result in update Method : " + result + " Count After inserting records in CRM_Attachments : "+attachmentId, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
					}
					catch (Throwable t) 
					{
						Logger.log("Error while inserting records in CRM_Attachments table in update Condition", "crmTicketAction.jsp", "ReadMailDaemon", t, Logger.CRITICAL);

						attachmentId = -1;
						result = result + "|"  + "0"+"~" + attachmentId;
					}
					finally
					{			
						paramTypes = null;
						params     = null;
						insQry     = null;
					}
				}
				else
				{					
					result = result + "|"  + "0"+"~" + attachmentId;
				}
			}
		}
		else
		{
			result = result + "|"  + "0"+"~" + attachmentId;
		}		
		Logger.log("result in update method after CRM_Attachments : "+result, "crmTicketAction.jsp", "ReadMailDaemon", null, Logger.INFO);
		return result;
	}

	/*Methods For CRM ends */
	
	/**
	 * This main method is to start the daemon
	 *
	 */
	
	public static void main(String[] args) 
	{
		ReadMailDaemon readMailDaemon = new ReadMailDaemon();
		
		while(mailRead_liveStatus)
		{
			/*CRM Default Values*/
			
			Properties propDfltCrm = null;
			
			File fileCrm = new File(Constants.configFilePath + "crm.properties");

			FileInputStream inFileCrm = null;

			Properties propsFileCrm = null;
			
			String crmDefaultAssignTo = "" , crmDefaultStatusId = "", crmDefaultPriority = "", crmDefaultCategory = "", crmDefaultIsActive = "" , crmDefaultSource = "" , crmDefaultMailType = "" , crmDefaultSendMailFlag = "" , crmDefaultForceTicketFlag = "";
			
			try
			{
				inFileCrm = new FileInputStream(fileCrm);

				propsFileCrm = new Properties();

				propsFileCrm.load(inFileCrm);
				
				crmDefaultAssignTo = propsFileCrm.getProperty("crm_default_assignTo");
				crmDefaultStatusId = propsFileCrm.getProperty("crm_default_statusId");
				crmDefaultPriority = propsFileCrm.getProperty("crm_default_priority");
				crmDefaultCategory = propsFileCrm.getProperty("crm_default_category");
				crmDefaultIsActive = propsFileCrm.getProperty("crm_default_isActive");
				crmDefaultSource = propsFileCrm.getProperty("crm_default_source");
				crmDefaultMailType = propsFileCrm.getProperty("crm_default_mailType");
				crmDefaultSendMailFlag = propsFileCrm.getProperty("crm_default_sendMailFlag");
				crmDefaultForceTicketFlag = propsFileCrm.getProperty("crm_default_forceTicketFlag");
				
				inFileCrm.close();
			}
			catch(Throwable t)
			{
				Logger.log("Error Loading CRM Default Property File:", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);	
			}
			finally
			{
				propDfltCrm = null;
				inFileCrm =null;
			}
			
			/*CRM Default Values*/
			
			{
				Logger.log("MAIL READ STARTS", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				ArrayList<String []> mailInfoList = readMailDaemon.receiveEmail();  

				ArrayList mailInfoList1 = new ArrayList();

				String OLD_FORMAT = "dd-MMM-yyyy" , NEW_FORMAT = "dd-MM-yyy" , oldDateString  = "" , newDateString = "" , crmticketId = "" , crmticketIdSub = "" ,crmticketIdMail = ""  ;
				SimpleDateFormat sdf = null;
				
				final String tcktSerTermStart= "[Ticket#" ;
				final String tcktSerTermEnd= "]" ;
					
				ArrayList mailInfoListCRM = new ArrayList();

				Logger.log("mailInfoList size:"+mailInfoList.size(), "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				if(mailInfoList != null && mailInfoList.size() > 0)
				{
					for(int i = 0 ; i < mailInfoList.size() ; i++)
					{
						crmticketId = ""; 
						crmticketIdSub = "";
						crmticketIdMail = "";

						String temp [] = (String []) mailInfoList.get(i);
						
						String subjectTemp = "" , senderTemp = "" , crmStr = "";

						if(temp.length >= 2)
						{
							subjectTemp = (temp[0] == null ? "" : temp[0]);
							senderTemp =  (temp[1] == null ? "" : temp[1]);
						}

						Logger.log("subject:"+subjectTemp + "\nChat\n"+temp[18]+"sentDate temp[3]:"+temp[3], "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						
						if(senderTemp.indexOf("transcript@providesupport.com") == -1  && subjectTemp.indexOf("<No Subject>") == -1) // push data into CRM other than CHAT
						{	

							if((temp[18].indexOf(tcktSerTermStart) > -1 && temp[18].indexOf(tcktSerTermEnd , temp[18].indexOf(tcktSerTermStart)) > -1))// To extarct ticket id from mail
							{
								crmticketIdMail = temp[18].substring(temp[18].indexOf(tcktSerTermStart) , temp[18].indexOf(tcktSerTermEnd , temp[18].indexOf(tcktSerTermStart) + tcktSerTermStart .length()));
								
								crmticketIdMail = crmticketIdMail.substring(crmticketIdMail.indexOf(tcktSerTermEnd) + 1 + tcktSerTermStart.length());
							}

							if((subjectTemp.indexOf(tcktSerTermStart) > -1 && subjectTemp.indexOf(tcktSerTermEnd , subjectTemp.indexOf(tcktSerTermStart)) > -1))// To extarct ticket id from subject
							{
								crmticketIdSub = subjectTemp.substring(subjectTemp.indexOf(tcktSerTermStart) , subjectTemp.indexOf(tcktSerTermEnd , subjectTemp.indexOf(tcktSerTermStart) + tcktSerTermStart .length()));
								
								crmticketIdSub = crmticketIdSub.substring(crmticketIdSub.indexOf(tcktSerTermEnd) + 1 + tcktSerTermStart.length());
							}

							if(!crmticketIdMail.equals(""))
								crmticketId = crmticketIdMail;
							else if(!crmticketIdSub.equals(""))
								crmticketId = crmticketIdSub;
							
							if(!crmticketIdMail.equals(crmticketIdSub))
							{
								Logger.log("Ticket Id from mail is not matching with Ticket Id from subject"   , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
							}

							Logger.log("crmticketId:"+crmticketId + " crmticketIdMail:"+crmticketIdMail+" crmticketIdSub:"+crmticketIdSub   , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
							
							mailInfoListCRM.add("RG");//groupId	//0
							mailInfoListCRM.add("");//userId	//1

							mailInfoListCRM.add(crmticketId);//crmTicketId  2
							mailInfoListCRM.add("0");//crmCustId 3

							
							String crmStrArr [] = temp[12].split("\\|"); // login id //4
							
							if(crmStrArr.length >= 2)
							{
								crmStr = crmStrArr[1];
							}
							else
							{
								crmStr = "";
							}

							mailInfoListCRM.add(crmStr);
							
							mailInfoListCRM.add("SYSTEM");////created By + updatedBy	//5 to be checked loginid / SYSTEM
							
							crmStrArr = temp[10].split("\\|");  // 6 , 7 firstName , lastName
							
							if(crmStrArr.length >= 2)
							{
								crmStr = crmStrArr[1];
							}
							else
							{
								crmStr = "" ;
							}
							
							if(crmStr.indexOf(" ") > -1)
							{
								if(crmStr.split(" ").length >= 2)
								{
									mailInfoListCRM.add(crmStr.split(" ")[0]);  // firstname 6
									mailInfoListCRM.add(crmStr.split(" ")[1]);	// lastname 7
								}
								else
								{
									mailInfoListCRM.add(crmStr); // firstname 6
									mailInfoListCRM.add("");	//// lastname 7
								}
							}
							else
							{
								mailInfoListCRM.add(crmStr); // firstname 6
								mailInfoListCRM.add("");	//// lastname 7
							}
							
							crmStrArr = temp[12].split("\\|"); // email//8

							if(crmStrArr.length >= 2)
							{
								crmStr = crmStrArr[1];
							}
							else
							{
								crmStr = "" ;
							}
							
							mailInfoListCRM.add(crmStr); 
							
							mailInfoListCRM.add(""); //contactNo//9

							crmStrArr = temp[0].split("\\|"); //subject //10

							if(crmStrArr.length >= 2)
							{
								crmStr = crmStrArr[1];
							}
							else
							{
								crmStr = "(No Subject)" ;
							}

							mailInfoListCRM.add(crmStr);

							crmStrArr = temp[18].split("\\|");//description //11

							if(crmStrArr.length >= 2)
							{
								crmStr = temp[18].substring(temp[18].indexOf("|") + 1, temp[18].length());
							}
							else
							{
								crmStr = "" ;
							}
							
							mailInfoListCRM.add(crmStr);

							if(!crmticketId.equals(""))
								mailInfoListCRM.add(crmStr);
							else
								mailInfoListCRM.add("");//Message/Description comments //12
							
							/*CRM Default Values*/
							
							mailInfoListCRM.add(crmDefaultAssignTo);//assignTo //13
							mailInfoListCRM.add(crmDefaultStatusId);//statusId //14
							mailInfoListCRM.add(crmDefaultPriority);//priority //15
							mailInfoListCRM.add(crmDefaultCategory);//category //16
							mailInfoListCRM.add("0");//noOfTickets //17
							mailInfoListCRM.add(crmDefaultIsActive);//isActive //18
							mailInfoListCRM.add(crmDefaultSource);//source //19
							mailInfoListCRM.add(crmDefaultMailType);//inbound/Outbound  //20
							mailInfoListCRM.add("");//fileName // 21
							mailInfoListCRM.add("");//filePath //22
							mailInfoListCRM.add(crmDefaultSendMailFlag);//sendMailFlag // 23
							mailInfoListCRM.add(crmDefaultForceTicketFlag);//forceTicketFlag //24
							
							/*CRM Default Values*/

							/*mailInfoListCRM.add("133");//assignTo //13
							mailInfoListCRM.add("1016");//statusId //14
							mailInfoListCRM.add("LOW");//priority //15
							mailInfoListCRM.add("100");//category //16
							mailInfoListCRM.add("0");//noOfTickets //17
							mailInfoListCRM.add("Y");//isActive //18
							mailInfoListCRM.add("MAIL");//source //19
							mailInfoListCRM.add("I");//inbound/Outbound  //20
							mailInfoListCRM.add("");//fileName // 21
							mailInfoListCRM.add("");//filePath //22
							mailInfoListCRM.add("Y");//sendMailFlag // 23
							mailInfoListCRM.add("Y");//forceTicketFlag //24*/
											
							if(!temp[3].equals("") && temp[3].split("\\|").length > 1)
							{
								mailInfoListCRM.add(temp[3].split("\\|")[1]);//sentDate //25
							}
							else
								mailInfoListCRM.add("");

							mailInfoListCRM.add(temp[19]);// Attachment Data 26
							
							String mimeTypeCRM [] =  temp[20].split("\\|");

							if(mimeTypeCRM.length > 1)
								mailInfoListCRM.add(mimeTypeCRM[1]);// mimeType 27
							else
								mailInfoListCRM.add("");// mimeType 27
						}

						for(int j = 0; j < temp.length ; j++ )
						{				
							temp[j] = ( temp[j] == null ? "" : temp[j] );

							senderTemp = temp[1];
							subjectTemp = temp[0];
							
							if(j == 8  || j == 9)
							{
								if(!temp[j].equals("") && (temp[j].split("\\|").length != 1))
								{
									try 
									{
										Logger.log("oldDateString:"+temp[j] , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);	
										oldDateString  = temp[j].split(" ")[0];
										oldDateString =  oldDateString.split("\\|")[1];

										sdf = new SimpleDateFormat(OLD_FORMAT);
										Date d = sdf.parse(oldDateString);
										sdf.applyPattern(NEW_FORMAT);
										newDateString = sdf.format(d);

										oldDateString = " "+temp[j].split(" ")[1] +" "+ temp[j].split(" ")[2];

										newDateString += oldDateString; 
										mailInfoList1.add(newDateString);
									}
									catch(Throwable t)
									{
										Logger.log("Error in parsing  Date "+newDateString + (j), "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
									}
								}
								else
								{
									if(temp[j].split("\\|").length == 1)
										mailInfoList1.add("");
									else
										mailInfoList1.add(temp[j].split("\\|")[1]);		
								}
							}
							else
							{

								if(temp[j].split("\\|").length == 1)
									mailInfoList1.add("");
								else
								{
									if(temp[j].split("\\|")[0].indexOf("User Chat Details") > -1)
									{
										mailInfoList1.add(temp[j].substring(temp[j].indexOf("|") + 1, temp[j].length()));
									}
									else
										mailInfoList1.add(temp[j].split("\\|")[1]);		
								}
							}
						}

						mailInfoList1.add("SYSTEM");
						
						subjectTemp = subjectTemp.trim();

						String subjectTempArr  [] = subjectTemp.split("\\|");

						String subjectTemp1 = "";

						if(subjectTempArr.length >=2)
						{
							subjectTemp1 = subjectTempArr[1];
							subjectTemp1 = subjectTemp1.trim();
						}
						
						if(senderTemp.indexOf("transcript@providesupport.com") > -1 || subjectTemp.indexOf("<No Subject>") > -1 || (subjectTemp1.startsWith("Chat") && subjectTemp1.indexOf("Email Transcript") > -1 && subjectTemp1.indexOf("Ticket created") == -1)) //
							mailInfoList1.add("CHAT");
						else
							mailInfoList1.add("OTHER"); // add for iType 'CHAT' / 'MAIL' / 'PHONE' later

						Logger.log("User Data Length:" + temp.length, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
						
						String sss = "";
						for(String ss : temp)
							sss += ss + ",";

						Logger.log("User Data:" + sss, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					}
				}
				else
				{
					Logger.log("NO Mail To Read", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}

				int result = -1;
				String resultVal = "";

				if(mailInfoList != null && mailInfoList.size() > 0)
				{
					try 
					{
						result = readMailDaemon.addMailDetails(mailInfoList1);
					}
					catch(Throwable t)
					{
						Logger.log("Error in calling addMailDetails  ", "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
					}

					Logger.log("mailInfoListCRM size:"  + mailInfoListCRM.size() , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

					ArrayList mailInfoListCRMTemp = new ArrayList();

					int paramsReqd = 28;

					ArrayList fileUploadDt = new ArrayList();
					
					for(int i = 0 ; i < mailInfoListCRM.size() ; i++)
					{
						
						if(i % paramsReqd == 0 && i != 0  || (i == mailInfoListCRM.size() - 1))
						{
							if((i == mailInfoListCRM.size() - 1))
								mailInfoListCRMTemp.add(mailInfoListCRM.get(i));

							try 
							{
								Logger.log("Size:"+mailInfoListCRMTemp.size() + "Method Called "     , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);					
								fileUploadDt = new ArrayList();

								String attachDt = "" ;
								String attachDtArr [] = null;

								attachDt = (String)mailInfoListCRMTemp.get(mailInfoListCRMTemp.size()-2);// AttachmentData at mailInfoListCRMTemp.size()-2 (FOR CRM ONLY)

								//attachDt = param1:param2:param3:param4?param1:param2:param3:param4?   attachment1?attachment2?.....
								
								attachDtArr = attachDt.split("\\|");

								if(attachDtArr.length > 1)
								{
									attachDt = attachDtArr[1];

									attachDtArr = attachDt.split("\\?");

									for(String attch : attachDtArr)
									{
										String attachDtArr1 [] = attch.split("\\:");

										fileUploadDt.add(attachDtArr1[0]);
										fileUploadDt.add(attachDtArr1[1]);
										fileUploadDt.add(attachDtArr1[2]);
										fileUploadDt.add(attachDtArr1[3]);
									}
								}

								Logger.log("Method Called addCustomerTicketData:"+mailInfoListCRMTemp + " fileUploadDt:" +fileUploadDt    , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
								
								if(mailRead_updateCRMDataflag)
									resultVal = readMailDaemon.addCustomerTicketData(mailInfoListCRMTemp , fileUploadDt);
								
								Logger.log("Method Called addCustomerTicketData"   , "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
							}
							catch(Throwable t)
							{
								Logger.log("Error in calling addCustomerTicketData resultVal: "  + resultVal , "ReadMailDaemon.java", "ReadMailDaemon", t, Logger.CRITICAL);
							}
							finally
							{
								 mailInfoListCRMTemp = new ArrayList();
							}
						}

						mailInfoListCRMTemp.add(mailInfoListCRM.get(i));
					}
				}

				Logger.log("MAIL READ ENDS", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

				//code for delay
				try
				{
					Logger.log("inside delay code()", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
							
					Logger.log("inside delay code() mailRead_timeInterval : " + mailRead_timeInterval, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
					Logger.log("Thread Sleep", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

					Thread.sleep(mailRead_timeInterval);
					Logger.log("Thread Activated", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
				}
				catch(Exception e)
				{
					Logger.log("mailRead.jsp :  main() : Error Exception : ", "ReadMailDaemon.java", "ReadMailDaemon", e, Logger.CRITICAL);
				}
				//end code for delay	
			}
		}

		Logger.log("mailRead_liveStatus "+mailRead_liveStatus, "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);

		if(!mailRead_liveStatus)
			Logger.log("MAIL READ Daemon Stopped ", "ReadMailDaemon.java", "ReadMailDaemon", null, Logger.INFO);
			
	}
}
