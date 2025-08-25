/*
 * @(#)MailPool.java	24-APR-2012
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenues*******.
 * Use is subject to license terms.
 *
 */
package com.api.remitGuru.component.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.api.remitGuru.web.controller.GroupController;
import com.api.remitGuru.web.controller.RGUserController;

public class MailPool {

		
	/**
	 *	Default no parameter constructor
	 */
	public MailPool(){
		
	}
	
	
	/**
	 * This method is generate and send a mail in specified format
	 *
	 */
	
	public int generateMail(String groupId)
	{
		String mailTo = "", mailFrom = "", mailReply = "", mailCC = "", mailSubject = "", mailMatter = "", mailId = "",mailType="";
		String result = "";
		int output = 0 ;
	
		RGUserController rgUser = new RGUserController();

		ArrayList mailVec = null;
		try
		{
			mailVec = rgUser.getMailData(groupId);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getMailData() ", "MailPool.java", "MailPool", t, Logger.CRITICAL);
		}
		
		try
		{
			if(mailVec != null && mailVec.size() > 0)
			{
				Logger.log("MailPool.java :  generateMail() ->  ", "MailPool.java", "MailPool", null, Logger.INFO);

				for(int i=0; i<mailVec.size(); i++)
				{
					mailId = ((String [])mailVec.get(i))[0];
					mailTo = ((String [])mailVec.get(i))[1];
					mailFrom = ((String [])mailVec.get(i))[2];
					mailReply = ((String [])mailVec.get(i))[3];
					mailCC = ((String [])mailVec.get(i))[4];	
					mailSubject=((String [])mailVec.get(i))[5];
					mailMatter = ((String [])mailVec.get(i))[6];
					mailType = ((String [])mailVec.get(i))[7];
					
					if("Y".equals(((String[])mailVec.get(i))[9])) // mail with attachment
					{
						String [] fileName = new String[1];
						fileName[0] = ((String[])mailVec.get(i))[11];
						String pdfFilePath = convertToPDF(((String[])mailVec.get(i))[10], ((String[])mailVec.get(i))[11]);
						Logger.log("PDFFilePath ->  " + pdfFilePath, "MailPool.java", "MailPool", null, Logger.INFO);
						
						if(mailType.equalsIgnoreCase("MAIL HTML"))
						{
							result = simpleHTMLMailSendWithAttachment(mailTo,mailFrom,mailReply,mailCC,mailSubject,mailMatter,mailId,pdfFilePath,fileName,groupId);
						}
						else
						{							
							result = simpleMailSendWithAttachment(mailTo,mailFrom,mailReply,mailCC,mailSubject,mailMatter,mailId,pdfFilePath,fileName,groupId);
						}
						
						try
						{
							FileUtils.deleteQuietly(new File(pdfFilePath));
						}
						catch(Exception e)
						{
							Logger.log("PDFFilePath Deletion of temp file", "MailPool.java", "MailPool", e, Logger.CRITICAL);
						} 
					}
					else
					{						
						if(mailType.equalsIgnoreCase("MAIL HTML"))
						{
							result = simpleHTMLMailSend(mailTo,mailFrom,mailReply,mailCC,mailSubject,mailMatter,mailId,groupId);
						}
						else
						{
							result = simpleMailSend(mailTo,mailFrom,mailReply,mailCC,mailSubject,mailMatter,mailId,groupId);
						}
					}
					
					Logger.log("MailPool.java :  generateMail() ->  Query : result  : "+ result  +  "   mailId :  " + mailId + "  mailType : " + mailType, "MailPool.java", "MailPool", null, Logger.INFO);

					if(result.equalsIgnoreCase("true"))
					{
						try
						{	
							output = rgUser.updateMailPool(mailId,"Y",groupId);
						}
						catch(Throwable t)
						{
							output = -1;
							Logger.log("MailPool.java  -->  generateMail()  : unable Update the send flags to Y: mailId : " + mailId, "MailPool.java", "MailPool", t, Logger.CRITICAL);
						}	
					}
					else
					{
						
						try
						{	
							output = rgUser.updateMailPool(mailId,"F",groupId);
						}
						catch(Throwable t)
						{
							output = -1;
							Logger.log("MailPool.java  -->  generateMail()  : unable Update the send flags to F : mailId : " + mailId, "MailPool.java", "MailPool", t, Logger.CRITICAL);
						}							
						
					}
				}
			}
		}
		catch(Throwable t1)
		{
			Logger.log("MailPool.java  -->  Error in generateMail()  :  ", "MailPool.java", "MailPool", t1, Logger.CRITICAL);
		}

		Logger.log("MailPool.java ****** END OF generateMail() *******", "MailPool.java", "MailPool", null, Logger.INFO);
		return output;
	}
	
	public String simpleMailSend(String to, String from, String reply, String cc, String subject, String matter, String mailId, String groupId)
	{
		//Logger.log("indide simpleMailSend()", "MailPool.java", "MailPool", null, Logger.INFO);
		String resultMail = "false" , password = "" , username = "" ,userId = "",host = "", port ="25", ssl="";

		try
		{
			String usernamePassword =  EnvProperties.getValue("MAIL_AUTHENTICATIONS","RG"); ; //  user~passwd
			
			if(usernamePassword != null && !usernamePassword.equals(""))
			{
				String usernamePasswordArr [] = usernamePassword.split("\\|");
				
				if( usernamePasswordArr.length > 0 )
				{
					for(String temp : usernamePasswordArr)
					{
						String tempArr [] = temp.split("~"); 

						if(tempArr != null && tempArr.length > 0 )
						{
							username = tempArr[0];

							if(from.equalsIgnoreCase(username))
							{
								userId = tempArr[1];
								password = tempArr[2];	
								break;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			password = "";
			username = "";
			userId = "";
			Logger.log("Error in retrieving email id or password", "MailPool.java", "MailPool", e, Logger.CRITICAL);
		}
		
		Logger.log("Login cred from : "+from+" username : "+username+" userId : "+userId+" password : "+password + " : ", "MailPool.java", "MailPool", null, Logger.INFO);

		if((to != null) && !(to.equals("")))
		{
			if(password != null && !password.equals("") && userId != null && !userId.equals(""))
			{
				try
				{
					resultMail = simpleMailSendAuthenticated(to, from, reply, cc, subject, matter, mailId, password, userId, groupId);
				}
				catch(Exception e)
				{	
					Logger.log("Error in  simpleMailSendAuthenticated mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+ cc , "MailPool.java", "MailPool", e, Logger.CRITICAL);
					resultMail = "false";
				}
			}

			if(password == null || password.equals("") ||  resultMail.equals("false"))
			{
				Logger.log("Without Authentication resultMail : "+resultMail + " mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc + " groupId :" + groupId , "MailPool.java", "MailPool", null, Logger.INFO);

				if(!groupId.equals(""))
				{
					host = EnvProperties.getValue("MAILERHOST", groupId); //"smtp.******.com";
					port = EnvProperties.getValue("MAILERPORT", groupId); //"smtp.******.com";
					ssl = EnvProperties.getValue("MAILERSSL", groupId);
				}
				else
				{
					host = EnvProperties.getValue("MAILERHOST");
					port = EnvProperties.getValue("MAILERPORT");
					ssl = EnvProperties.getValue("MAILERSSL");					
				}
					
				Logger.log("indide simpleMailSend() host : " +host + " groupId : "+ groupId, "MailPool.java", "MailPool", null, Logger.INFO);

				try
				{
					System.clearProperty("mail.smtp.socketFactory.port");
					System.clearProperty("mail.smtp.socketFactory.class");
					
					//Properties properties = System.getProperties();
					Properties properties = new Properties();
					properties.put("mail.smtp.host",host);
					if(ssl.equals("1"))
					{
						properties.put("mail.smtp.socketFactory.port", port);
						properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
					}
					properties.put("mail.smtp.auth", "false"); 
					properties.put("mail.smtp.port",port);
					Session s = Session.getInstance(properties,null);
					MimeMessage message = new MimeMessage(s);
					InternetAddress[] toAddresses, ccAddress;
					message.setFrom(new InternetAddress(from));
					toAddresses = InternetAddress.parse(to);

					if(reply != null && !reply.equals(""))
						message.setReplyTo(new InternetAddress[]{new InternetAddress(reply)});
					
					message.setRecipients(Message.RecipientType.TO, toAddresses);

					if(cc != null && !cc.equals(""))
					{
						ccAddress = InternetAddress.parse(cc);
						message.addRecipients(Message.RecipientType.CC, ccAddress);
					}
					
					message.setSubject(subject);
					message.setText(matter);
					message.saveChanges() ;
					
					//Logging content -->
					try {
				        ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        message.writeTo(baos);
				        String messageContent = baos.toString("UTF-8");
						Logger.log("MimeMessage Content: \n" + messageContent, "MailPool.java", "MailPool", null, Logger.INFO);

				    } catch (MessagingException | IOException e) {
				        e.printStackTrace();
						Logger.log("SendFailedException in logging." , "MailPool.java", "MailPool", e, Logger.CRITICAL);

				    }
					
					Transport.send(message);

					resultMail = "true";
				}
				catch (SendFailedException e)
				{
					resultMail="false";
					e.printStackTrace();
					Logger.log("MailPool.java  simpleMailSend () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", null, Logger.INFO);
				}
				catch (Exception e)
				{
					resultMail = "false";
					Logger.log("MailPool.java  simpleMailSend () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", null, Logger.INFO);
				}
				catch(Throwable t)
				{
					resultMail = "false";
					Logger.log("MailPool.java  simpleMailSend () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", null, Logger.INFO);
				}
			}
		}
		else
		{
			resultMail = "false";
		}

		return resultMail;
	}
	
	public String simpleHTMLMailSend(String to, String from, String reply, String cc, String subject, String matter, String mailId, String groupId)
	{
		//Logger.log("indide simpleMailSend()", "MailPool.java", "MailPool", null, Logger.INFO);
		String resultMail = "false" , password = "" , username = "" , userId =  "", host = "", port = "25", ssl = "";
		
		try
		{
			String usernamePassword =  EnvProperties.getValue("MAIL_AUTHENTICATIONS","RG"); ; //  user~passwd
			
			if(usernamePassword != null && !usernamePassword.equals(""))
			{
				String usernamePasswordArr [] = usernamePassword.split("\\|");
				
				if(usernamePasswordArr.length > 0)
				{
					for(String temp : usernamePasswordArr)
					{
						String tempArr [] = temp.split("~"); 

						if(tempArr != null && tempArr.length > 0 )
						{
							username = tempArr[0];

							if(from.equalsIgnoreCase(username))
							{
								userId = tempArr[1];
								password = tempArr[2];	
								break;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			password = "";
			username = "";
			userId = "" ;
			Logger.log("Error in retrieving email id or password", "MailPool.java", "MailPool", e, Logger.CRITICAL);
		}
		
		if((to != null) && !(to.equals("")))
		{
			if(password != null && !password.equals("") && userId != null && !userId.equals(""))
			{
				try
				{
					resultMail = simpleHTMLMailSendAuthenticated(to, from, reply, cc, subject, matter, mailId, password, userId, groupId);
				}
				catch(Exception e)
				{	
					Logger.log("Error in  simpleHTMLMailSendAuthenticated mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+ cc , "MailPool.java", "MailPool", e, Logger.CRITICAL);
					resultMail = "false";
				}
			}

			if(password == null || password.equals("") ||  resultMail.equals("false"))
			{
				Logger.log("Without Authentication resultMail : "+resultMail + " mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc + " groupId : "+ groupId , "MailPool.java", "MailPool", null, Logger.INFO);

				if(!groupId.equals(""))
				{
					host = EnvProperties.getValue("MAILERHOST", groupId); //"smtp.******.com";
					port = EnvProperties.getValue("MAILERPORT", groupId); //"smtp.******.com";
					ssl = EnvProperties.getValue("MAILERSSL", groupId);
				}
				else
				{
					host = EnvProperties.getValue("MAILERHOST"); //"smtp.******.com";
					port = EnvProperties.getValue("MAILERPORT");
					ssl = EnvProperties.getValue("MAILERSSL");
				}
				
				Logger.log("indide simpleMailSend() host : " +host + " port " + port + " ssl: " + ssl + "  groupId : "+ groupId, "MailPool.java", "MailPool", null, Logger.INFO);

				try
				{
					System.clearProperty("mail.smtp.socketFactory.port");
					System.clearProperty("mail.smtp.socketFactory.class");
					
					Properties properties = System.getProperties();
					properties.put("mail.smtp.host",host);
					if(ssl.equals("1"))
					{
						properties.put("mail.smtp.socketFactory.port", port);
						properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
					}
					properties.put("mail.smtp.auth", "false"); 
					properties.put("mail.smtp.port",port);
					Session s = Session.getInstance(properties,null);
					MimeMessage message = new MimeMessage(s);
					InternetAddress[] toAddresses, ccAddress;
					message.setFrom(new InternetAddress(from));
					toAddresses = InternetAddress.parse(to);
					message.setRecipients(Message.RecipientType.TO, toAddresses);
					
					if(cc != null && !cc.equals(""))
					{
						ccAddress 	= InternetAddress.parse(cc);
						message.addRecipients(Message.RecipientType.CC, ccAddress);
					}
					
					if(reply != null && !reply.equals(""))
						message.setReplyTo(new InternetAddress[]{new InternetAddress(reply)});
					
					message.setSubject(subject);
					MimeBodyPart mbp1 = new MimeBodyPart();
					mbp1.setText(matter) ;
					mbp1.setContent(matter, "text/HTML") ;
					message.setText(matter) ;
					Multipart mp = new MimeMultipart();
					mp.addBodyPart(mbp1);
					message.setContent(mp);
					message.saveChanges() ;
					Transport.send(message);

					resultMail = "true";
				}
				catch (SendFailedException e)
				{
					resultMail="false";
					Logger.log("SendFailedException MailPool.java  simpleHTMLMailSend () SendFailedException mailId : " + mailId + " to : " + to, "MailPool.java", "MailPool", e, Logger.CRITICAL);
				}
				catch (Exception e)
				{
					resultMail = "false";
					Logger.log("Exception MailPool.java  simpleHTMLMailSend () SendFailedException mailId : " + mailId + " to : " + to, "MailPool.java", "MailPool", e, Logger.CRITICAL);
				}
				catch(Throwable t)
				{
					resultMail = "false";
					Logger.log("Throwable MailPool.java  simpleHTMLMailSend () SendFailedException mailId : " + mailId + " to : " + to, "MailPool.java", "MailPool", t, Logger.CRITICAL);
				}
			}
		}
		else
		{
			resultMail = "false";
		}

		return resultMail;
	}
	
	public String simpleMailSendAuthenticated(String to, String from, String reply, String cc, String subject, String matter, String mailId , String password, String userId, String groupId)
	{	
		String resultMail = "true",host = "", port = "25", ssl = "";
		Logger.log("indide simpleMailSendAuthenticated() resultMail : "+resultMail + " mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc + " groupId :" + groupId , "MailPool.java", "MailPool", null, Logger.INFO);
		
		if(!groupId.equals(""))
		{
			host = EnvProperties.getValue("MAILERHOST", groupId); //"smtp.******.com";
			port = EnvProperties.getValue("MAILERPORT", groupId); //"smtp.******.com";
			ssl = EnvProperties.getValue("MAILERSSL", groupId);
		}
		else
		{
			host = EnvProperties.getValue("MAILERHOST"); //"smtp.******.com";
			port = EnvProperties.getValue("MAILERPORT");
			ssl = EnvProperties.getValue("MAILERSSL");
		}
		
		Logger.log("indide simpleMailSendAuthenticated() host : " +host + " port : " + port + " ssl : " + ssl + " groupId : "+ groupId, "MailPool.java", "MailPool", null, Logger.INFO);
		
		if((to != null) && !(to.equals("")))
		{
			try
			{
				System.clearProperty("mail.smtp.socketFactory.port");
				System.clearProperty("mail.smtp.socketFactory.class");
				
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host",host);
				if(ssl.equals("1"))
				{
					properties.put("mail.smtp.socketFactory.port", port);
					properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				}
				properties.put("mail.smtp.auth", "true");
				properties.put("mail.smtp.port",port);
				Session s = Session.getInstance(properties,null);
				MimeMessage message = new MimeMessage(s);
				InternetAddress[] toAddresses, ccAddress;
				message.setFrom(new InternetAddress(from));
				toAddresses = InternetAddress.parse(to);

				Transport tr = s.getTransport("smtp");
				
				try
				{
					Logger.log("indide simpleMailSendAuthenticated() host : " +host + " userId : " + userId + " password : " + password + " groupId : "+ groupId, "MailPool.java", "MailPool", null, Logger.INFO);
					
					tr.connect(host, userId, password);
				}
				catch(Exception e )
				{
					Logger.log("connection error mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", e, Logger.CRITICAL);
					resultMail="false";
				}
				
				if(reply != null && !reply.equals(""))
					message.setReplyTo(new InternetAddress[]{new InternetAddress(reply)});
				
				message.setRecipients(Message.RecipientType.TO, toAddresses);

				if(cc != null && !cc.equals(""))
				{
					ccAddress = InternetAddress.parse(cc);
					message.addRecipients(Message.RecipientType.CC, ccAddress);
				}
				
				message.setSubject(subject);
				message.setText(matter);
				message.saveChanges() ;
				
				tr.sendMessage(message, message.getAllRecipients());
				
				resultMail = "true";
			
			}
			catch (SendFailedException e)
			{
				resultMail="false";
				Logger.log("SendFailedException MailPool.java  simpleMailSendAuthenticated () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", e, Logger.CRITICAL);
			}
			catch (Exception e)
			{
				resultMail = "false";
				Logger.log("Exception MailPool.java  simpleMailSendAuthenticated () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", e, Logger.CRITICAL);
			}
			catch(Throwable t)
			{
				resultMail = "false";
				Logger.log("Throwable MailPool.java  simpleMailSendAuthenticated () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", t, Logger.CRITICAL);
			}
		}
		else
		{
			resultMail = "false"; 	
		}

		return resultMail;
	}
	
	public String simpleHTMLMailSendAuthenticated(String to, String from, String reply, String cc, String subject, String matter, String mailId , String password, String userId, String groupId)
	{	
		String resultMail = "true",host = "", port = "25", ssl = "";
		Logger.log("indide simpleHTMLMailSendAuthenticated() resultMail : "+resultMail + " mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc + " groupId :" + groupId , "MailPool.java", "MailPool", null, Logger.INFO);
	
		if(!groupId.equals(""))
		{
			host = EnvProperties.getValue("MAILERHOST", groupId); //"smtp.******.com";
			port = EnvProperties.getValue("MAILERPORT", groupId); //"smtp.******.com";
			ssl = EnvProperties.getValue("MAILERSSL", groupId);
		}
		else
		{
			host = EnvProperties.getValue("MAILERHOST"); //"smtp.******.com";
			port = EnvProperties.getValue("MAILERPORT");
			ssl = EnvProperties.getValue("MAILERSSL");
		}
			
		Logger.log("indide simpleMailSendAuthenticated() host : " +host + " groupId : " + groupId, "MailPool.java", "MailPool", null, Logger.INFO);
		
		if((to != null) && !(to.equals("")))
		{
			try
			{
				System.clearProperty("mail.smtp.socketFactory.port");
				System.clearProperty("mail.smtp.socketFactory.class");
				
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host",host);
				if(ssl.equals("1"))
				{
					properties.put("mail.smtp.socketFactory.port", port);
					properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				}
				properties.put("mail.smtp.auth", "true"); 
				properties.put("mail.smtp.port",port);
				Session s = Session.getInstance(properties,null);
				MimeMessage message = new MimeMessage(s);
				InternetAddress[] toAddresses, ccAddress;
				message.setFrom(new InternetAddress(from));
				toAddresses = InternetAddress.parse(to);

				Transport tr = s.getTransport("smtp");
				
				try
				{
					tr.connect(host, userId, password);
				}
				catch(Exception e )
				{
					Logger.log("connection error mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", null, Logger.INFO);
					resultMail="false";
				}
				
				if(reply != null && !reply.equals(""))
					message.setReplyTo(new InternetAddress[]{new InternetAddress(reply)});
				
				message.setRecipients(Message.RecipientType.TO, toAddresses);

				if(cc != null && !cc.equals(""))
				{
					ccAddress = InternetAddress.parse(cc);
					message.addRecipients(Message.RecipientType.CC, ccAddress);
				}
				
				message.setSubject(subject);
				MimeBodyPart mbp1 = new MimeBodyPart();
				mbp1.setText(matter) ;
				mbp1.setContent(matter, "text/HTML") ;
				message.setText(matter) ;
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(mbp1);
				message.setContent(mp);
				message.saveChanges() ;
				
				tr.sendMessage(message, message.getAllRecipients());
				
				resultMail = "true";
			
			}
			catch (SendFailedException e)
			{
				resultMail="false";
				Logger.log("MailPool.java  simpleHTMLMailSendAuthenticated () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", e, Logger.CRITICAL);
			}
			catch (Exception e)
			{
				resultMail = "false";
				Logger.log("MailPool.java  simpleHTMLMailSendAuthenticated () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", e, Logger.CRITICAL);
			}
			catch(Throwable t)
			{
				resultMail = "false";
				Logger.log("MailPool.java  simpleHTMLMailSendAuthenticated () SendFailedException mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", t, Logger.CRITICAL);
			}
		}
		else
		{
			resultMail = "false"; 	
		}

		return resultMail;
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
				
				Logger.log("Daemon started at : " + datetime, "MailPool.java", "MailPool", null, Logger.INFO);

				MailPool mail = new MailPool();				
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
						Logger.log(i + " : Daemon Started for Group Id : " + (String)grpArr[i], "MailPool.java", "MailPool", null, Logger.INFO);
						mail.generateMail((String)grpArr[i]);
						Logger.log(i + " : Daemon End for Group Id : " + (String)grpArr[i], "MailPool.java", "MailPool", null, Logger.INFO);
					}
				}
			}
			catch(Throwable t)
			{
				Logger.log("Error in starting Mail Daemon ", "MailPool.java", "MailPool", t, Logger.CRITICAL);
			}
			
			//code for delay
			try
			{
				Logger.log("inside delay code()", "MailPool.java", "MailPool", null, Logger.INFO);
				int interval = Integer.parseInt(EnvProperties.getValue("MAIL_DELAY"));
				Logger.log("inside delay code() interval : " + interval, "MailPool.java", "MailPool", null, Logger.INFO);
				Logger.log("Thread Sleep", "MailPool.java", "MailPool", null, Logger.INFO);

				Thread.sleep(1000*interval);
				Logger.log("Thread Activated", "MailPool.java", "MailPool", null, Logger.INFO);
			}
			catch(Exception e)
			{
				Logger.log("MailPool.java :  main() : Error Exception : ", "MailPool.java", "MailPool", e, Logger.CRITICAL);
			}
			//end code for delay		
		}// end of while

	}
	
	public String simpleMailSendWithAttachment(String to, String from, String reply, String cc, String subject, String matter, String mailId, String path, String [] fileName,String groupId)
	{
		Logger.log("inside simpleMailSendWithAttachment()", "MailPool.jsp", "MailPool", null, Logger.INFO);
		String resultMail = "false", password = "" , username = "" ,userId = "";

		try
		{
			String usernamePassword =  EnvProperties.getValue("MAIL_AUTHENTICATIONS","RG"); ; //  user~passwd
			
			if(usernamePassword != null && !usernamePassword.equals(""))
			{
				String usernamePasswordArr [] = usernamePassword.split("\\|");
				
				if( usernamePasswordArr.length > 0 )
				{
					for(String temp : usernamePasswordArr)
					{
						String tempArr [] = temp.split("~"); 

						if(tempArr != null && tempArr.length > 0 )
						{
							username = tempArr[0];

							if(from.equalsIgnoreCase(username))
							{
								userId = tempArr[1];
								password = tempArr[2];	
								break;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			password = "";
			username = "";
			userId = "";
			Logger.log("Error in retrieving email id or password", "MailPool.jsp", "MailPool", e, Logger.CRITICAL);
		}
			
		if((to != null) && !(to.equals("")))
		{
			Logger.log("Without Authentication resultMail : " + resultMail + " mailId : " + mailId + " to : " + to + " from : " + from + " cc : " + cc , "MailPool.jsp", "MailPool", null, Logger.INFO);
			Logger.log("Attchment path : " + path , "MailPool.jsp", "MailPool", null, Logger.INFO);
			
			String host = "", port = "25", ssl = "";
			
			if(!groupId.equals(""))
			{
				host = EnvProperties.getValue("MAILERHOST", groupId); //"smtp.******.com";
				port = EnvProperties.getValue("MAILERPORT", groupId); //"smtp.******.com";
				ssl = EnvProperties.getValue("MAILERSSL", groupId);
			}
			else
			{
				host = EnvProperties.getValue("MAILERHOST"); //"smtp.******.com";
				port = EnvProperties.getValue("MAILERPORT");
				ssl = EnvProperties.getValue("MAILERSSL");
			}
			
			Logger.log("mail sent : " +host+" port : "+port, "MailPool.jsp", "MailPool", null, Logger.INFO);

			try
			{
				System.clearProperty("mail.smtp.socketFactory.port");
				System.clearProperty("mail.smtp.socketFactory.class");
				
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host",host);
				
				if(ssl.equals("1"))
				{
					properties.put("mail.smtp.socketFactory.port", port);
					properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				}
				
				properties.put("mail.smtp.port",port);
				
				if(password != null && !password.equals("") && userId != null && !userId.equals(""))
					properties.put("mail.smtp.auth","true");
				else
					properties.put("mail.smtp.auth","false");
					
				Session s = Session.getInstance(properties,null);
				MimeMessage message = new MimeMessage(s);
				InternetAddress[] toAddresses, ccAddress;
				message.setFrom(new InternetAddress(from));
				toAddresses = InternetAddress.parse(to);
				
				Transport tr = s.getTransport("smtp");
				
				if(password != null && !password.equals("") && userId != null && !userId.equals(""))
				{
					try
					{
						Logger.log("indide simpleMailSendAuthenticated() host : " +host + " userId : " + userId + " password : " + password + " groupId : "+ groupId, "MailPool.java", "MailPool", null, Logger.INFO);
						
						tr.connect(host, userId, password);
					}
					catch(Exception e )
					{
						Logger.log("connection error mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", e, Logger.CRITICAL);
						resultMail="false";
					}
				}

				if(reply != null && !reply.equals(""))
					message.setReplyTo(new InternetAddress[]{new InternetAddress(reply)});

				message.setRecipients(Message.RecipientType.TO, toAddresses);

				if(cc != null && !cc.equals(""))
				{
					ccAddress = InternetAddress.parse(cc);
					message.addRecipients(Message.RecipientType.CC, ccAddress);
				}

				//Create Multipart for attachment
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();

				// Now set the actual message
				messageBodyPart.setText(matter);

				
				
				// Create a multipar message
				//Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				// Part two is attachment
				
				for(int i = 0; i < fileName.length; i++)
				{	
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(path); // + fileName[i]);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(fileName[i]);
					multipart.addBodyPart(messageBodyPart);
				}
				
				message.setSubject(subject);
				message.setContent(multipart);
				//message.setText(matter);
				message.saveChanges() ;
				Logger.log("....................before ", "MailPool.jsp", "MailPool", null, Logger.INFO);
				
				if(password != null && !password.equals("") && userId != null && !userId.equals(""))
					tr.sendMessage(message, message.getAllRecipients());
				else
					Transport.send(message);

				Logger.log("....................after ", "MailPool.jsp", "MailPool", null, Logger.INFO);
				resultMail = "true";
			}
			catch (SendFailedException se)
			{
				resultMail="false";
				Logger.log("Email.java  Email () SendFailedException mailId : " + mailId + " to : " + to + " from : "+ from + " cc : "+ cc, "MailPool.jsp", "MailPool", se, Logger.CRITICAL);
			}
			catch (Exception e)
			{
				resultMail = "false";
				Logger.log("Email.java  Email () SendFailedException mailId : " + mailId + " to : " + to + " from : "+ from + " cc : "+ cc, "MailPool.jsp", "MailPool", e, Logger.CRITICAL);
			}
			catch(Throwable t)
			{
				resultMail = "false";
				Logger.log("Email.java  Email () SendFailedException mailId : " + mailId + " to : " + to + " from : "+ from + " cc : "+ cc, "MailPool.jsp", "MailPool", t, Logger.CRITICAL);
			}
		}
		else
		{
			resultMail = "false";
		}

		return resultMail;
	}
	
	public String simpleHTMLMailSendWithAttachment(String to, String from, String reply, String cc, String subject, String matter, String mailId, String path, String [] fileName,String groupId)
	{
		Logger.log("inside simpleHTMLMailSendWithAttachment()", "MailPool.jsp", "MailPool", null, Logger.INFO);
		String resultMail = "false", password = "" , username = "" ,userId = "";

		try
		{
			String usernamePassword =  EnvProperties.getValue("MAIL_AUTHENTICATIONS","RG"); ; //  user~passwd
			
			if(usernamePassword != null && !usernamePassword.equals(""))
			{
				String usernamePasswordArr [] = usernamePassword.split("\\|");
				
				if( usernamePasswordArr.length > 0 )
				{
					for(String temp : usernamePasswordArr)
					{
						String tempArr [] = temp.split("~"); 

						if(tempArr != null && tempArr.length > 0 )
						{
							username = tempArr[0];

							if(from.equalsIgnoreCase(username))
							{
								userId = tempArr[1];
								password = tempArr[2];	
								break;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			password = "";
			username = "";
			userId = "";
			Logger.log("Error in retrieving email id or password", "MailPool.jsp", "MailPool", e, Logger.CRITICAL);
		}
			
		if((to != null) && !(to.equals("")))
		{
			Logger.log("Without Authentication resultMail : " + resultMail + " mailId : " + mailId + " to : " + to + " from : " + from + " cc : " + cc , "MailPool.jsp", "MailPool", null, Logger.INFO);
			Logger.log("Attchment  path : " + path , "MailPool.jsp", "MailPool", null, Logger.INFO);

			String host = "", port = "25", ssl = "";
			
			if(!groupId.equals(""))
			{
				host = EnvProperties.getValue("MAILERHOST", groupId); //"smtp.******.com";
				port = EnvProperties.getValue("MAILERPORT", groupId); //"smtp.******.com";
				ssl = EnvProperties.getValue("MAILERSSL", groupId);
			}
			else
			{
				host = EnvProperties.getValue("MAILERHOST"); //"smtp.******.com";
				port = EnvProperties.getValue("MAILERPORT");
				ssl = EnvProperties.getValue("MAILERSSL");
			}
			Logger.log("mail sent : " +host+" port : "+port + " ssl : " + ssl, "MailPool.jsp", "MailPool", null, Logger.INFO);

			try
			{
				System.clearProperty("mail.smtp.socketFactory.port");
				System.clearProperty("mail.smtp.socketFactory.class");
				
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host",host);
				if(ssl.equals("1"))
				{
					properties.put("mail.smtp.socketFactory.port", port);
					properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				}
				properties.put("mail.smtp.port",port);
				
				if(password != null && !password.equals("") && userId != null && !userId.equals(""))
					properties.put("mail.smtp.auth","true");
				else
					properties.put("mail.smtp.auth","false");
					
				Session s = Session.getInstance(properties,null);
				MimeMessage message = new MimeMessage(s);
				InternetAddress[] toAddresses, ccAddress;
				message.setFrom(new InternetAddress(from));
				toAddresses = InternetAddress.parse(to);
				
				Transport tr = s.getTransport("smtp");
				
				if(password != null && !password.equals("") && userId != null && !userId.equals(""))
				{
					try
					{
						Logger.log("indide simpleMailSendAuthenticated() host : " +host + " userId : " + userId + " password : " + password + " groupId : "+ groupId, "MailPool.java", "MailPool", null, Logger.INFO);
						
						tr.connect(host, userId, password);
					}
					catch(Exception e )
					{
						Logger.log("connection error mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc, "MailPool.java", "MailPool", e, Logger.CRITICAL);
						resultMail="false";
					}
				}
				
				if(reply != null && !reply.equals(""))
					message.setReplyTo(new InternetAddress[]{new InternetAddress(reply)});

				message.setRecipients(Message.RecipientType.TO, toAddresses);

				if(cc != null && !cc.equals(""))
				{
					ccAddress = InternetAddress.parse(cc);
					message.addRecipients(Message.RecipientType.CC, ccAddress);
				}

				//Create Multipart for attachment
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();

				// Now set the actual message
				messageBodyPart.setText(matter);

				
				
				// Create a multipar message
				//Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				// Part two is attachment
				
				for(int i = 0; i < fileName.length; i++)
				{	
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(path);// + fileName[i]);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(fileName[i]);
					multipart.addBodyPart(messageBodyPart);
				}
				
				message.setSubject(subject);
				
				MimeBodyPart mbp1 = new MimeBodyPart();
				mbp1.setText(matter) ;
				mbp1.setContent(matter, "text/HTML") ;
				
				message.setText(matter) ;
				
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(mbp1);
				message.setContent(mp);
				
				message.setContent(multipart);
				//message.setText(matter);
				message.saveChanges() ;
				Logger.log("....................before ", "MailPool.jsp", "MailPool", null, Logger.INFO);
				
				if(password != null && !password.equals("") && userId != null && !userId.equals(""))
					tr.sendMessage(message, message.getAllRecipients());
				else
					Transport.send(message);

				Logger.log("....................after ", "MailPool.jsp", "MailPool", null, Logger.INFO);
				resultMail = "true";
			}
			catch (SendFailedException se)
			{
				resultMail="false";
				Logger.log("Email.java  Email () SendFailedException mailId : " + mailId + " to : " + to + " from : "+ from + " cc : "+ cc, "MailPool.jsp", "MailPool", se, Logger.CRITICAL);
			}
			catch (Exception e)
			{
				resultMail = "false";
				Logger.log("Email.java  Email () SendFailedException mailId : " + mailId + " to : " + to + " from : "+ from + " cc : "+ cc, "MailPool.jsp", "MailPool", e, Logger.CRITICAL);
			}
			catch(Throwable t)
			{
				resultMail = "false";
				Logger.log("Email.java  Email () SendFailedException mailId : " + mailId + " to : " + to + " from : "+ from + " cc : "+ cc, "MailPool.jsp", "MailPool", t, Logger.CRITICAL);
			}
		}
		else
		{
			resultMail = "false";
		}

		return resultMail;
	}
	
	public String convertToPDF(String attachmentContent, String attachmentFileName)
	{
		Logger.log("convertToPDF", "MailPool.jsp", "MailPool", null, Logger.INFO);
		String path   =  EnvProperties.getPropertyValue("MAIL_ATTACH_PATH") == null ? "" : EnvProperties.getPropertyValue("MAIL_ATTACH_PATH");
		
		String lsDestPDFDir = path;
		if(!new File(lsDestPDFDir).exists())
			new File(lsDestPDFDir).mkdir();
		
		String FileNameWithPath = path + attachmentFileName;
		
		try
		{
			String lsDestDir = path + "Temp/";
			if(!new File(lsDestDir).exists())
				new File(lsDestDir).mkdir();
			
			String lsDestFile = lsDestDir + UUID.randomUUID() + System.currentTimeMillis() +  ".html";
			FileWriter fw = new FileWriter(lsDestFile);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(attachmentContent);
			pw.close();
			fw.close();
			
			Logger.log("convertToPDF inside try", "MailPool.jsp", "MailPool", null, Logger.INFO);
			String cssContent = 
	       		 "  .details { border-collapse: collapse;   padding:2} .detailsamt{border: 1px solid black;}"
	       		+ " table { font-size:12px; border-collapse: collapse; height: 400px; } "
	       		+ " table tr td {  padding: 0; white-space: -o-pre-wrap;   word-wrap: break-word;   white-space: pre-wrap;   white-space: -moz-pre-wrap;   white-space: -pre-wrap;  } "
	       		+ " .label { font-weight:bold; text-decoration:underline; text-align: center; } .aa{ text-align: center; } .bb{ text-align: left; } .marginTest { margin-left:20px;}" ;

			Document document = new Document();
	        // step 2
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FileNameWithPath));
	        // step 3
	        document.open();
	        // step 4

	        // CSS
	        CSSResolver cssResolver =
	                XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
	       // FileRetrieve retrieve = new FileRetrieveImpl();
	        cssResolver.addCss(cssContent, true);


	        // HTML
	        HtmlPipelineContext htmlContext = new HtmlPipelineContext();
	        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
	       

	        // Pipelines
	        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
	        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
	        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
	        
	        Logger.log("convertToPDF inside html : " + lsDestFile, "MailPool.jsp", "MailPool", null, Logger.INFO);
	        Logger.log("convertToPDF inside pdf : " + FileNameWithPath, "MailPool.jsp", "MailPool", null, Logger.INFO);
	        
	        // XML Worker
	        XMLWorker worker = new XMLWorker(css, true);
	        XMLParser p = new XMLParser(worker);
	        p.parse(new FileInputStream(lsDestFile));

	        // step 5
	        document.close();
	        
	        try
			{
				FileUtils.deleteQuietly(new File(lsDestFile));
			}
			catch(Exception e)
			{
				Logger.log("Error in convertToPDF inside Deletion of temp file", "MailPool.jsp", "MailPool", e, Logger.CRITICAL);
			}  
		}
		catch(Exception e)
		{
			Logger.log("Error in convertToPDF inside catch", "MailPool.jsp", "MailPool", e, Logger.CRITICAL);
			e.printStackTrace();
		}
		return FileNameWithPath;
	}
	
	//temp methods 
	public String simpleMailSend(String to, String from, String reply, String cc, String subject, String matter, String mailId)
	{
		return simpleMailSend(to, from, reply, cc, subject, matter, mailId, "");
	}

	public String simpleHTMLMailSend(String to, String from, String reply, String cc, String subject, String matter, String mailId)
	{
		return simpleHTMLMailSend(to, from, reply, cc, subject, matter, mailId, "");
	}

	public String simpleMailSendAuthenticated(String to, String from, String reply, String cc, String subject, String matter, String mailId , String password, String userId)
	{
		return simpleMailSendAuthenticated(to, from, reply, cc, subject, matter, mailId , password, userId, "");
	}

	public String simpleHTMLMailSendAuthenticated(String to, String from, String reply, String cc, String subject, String matter, String mailId , String password, String userId)
	{
		return simpleHTMLMailSendAuthenticated(to, from, reply, cc, subject, matter, mailId , password, userId, "");
	}
	
	public void executeMailPool()
	{
		try
		{
			 String stopMailDaemon = EnvProperties.getPropertyValue("MAIL_DAEMON_ACTIVE") == null ? "" : EnvProperties.getPropertyValue("MAIL_DAEMON_ACTIVE");
			 Logger.log("MAIL_DAEMON_ACTIVE: " + stopMailDaemon, "MailPool.java", "MailPool", null, Logger.INFO);
							 
			 if(stopMailDaemon.equalsIgnoreCase("Y"))
			 {
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("EEEE-MMMM-dd-yyyy HH:mm:ss");
				java.util.Date currdt = new java.util.Date();
	
				String datetime = bartDateFormat.format(currdt);
	
				Logger.log("Daemon started at : " + datetime, "MailPool.java", "MailPool", null, Logger.INFO);
	
				MailPool mail = new MailPool();
	
				String activeGroupIds = EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST") == null ? ""
						: EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST");//",RG,ONECARD,YR,AR,RXM,";
	
				if (!activeGroupIds.equals(""))
				{
					String[] grpArr = activeGroupIds.split(",");
					for (int i = 1; i < grpArr.length; i++)
					{
						Logger.log(i + " : Daemon Started for Group Id : " + (String) grpArr[i], "MailPool.java",
								"MailPool", null, Logger.INFO);
						mail.generateMail((String) grpArr[i]);
						Logger.log(i + " : Daemon End for Group Id : " + (String) grpArr[i], "MailPool.java", "MailPool",
								null, Logger.INFO);
					}
				}
			 }
		}
		catch (Throwable t)
		{
			Logger.log("Error in starting Mail Daemon ", "MailPool.java", "MailPool", t, Logger.CRITICAL);
		}
	}

}
