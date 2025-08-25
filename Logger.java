/*
 * @(#)Logger.java	01-Jan-2019
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenues*******.
 * Use is subject to license terms.
 *
 */

package com.api.remitGuru.component.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.api.remitGuru.component.environment.Constants;
import com.api.remitGuru.component.util.dao.SQLRepository;

/**
 * @author  *******
 */
public class Logger {
    public Logger() {  }
	
    /**
     * These fields denote the level/severity of message
     * Depending on these levels these logs can be turned off 
     */
    
    public final static String INFO      = "INFO";
    public final static String SQL   	 = "SQL";
    public final static String DEBUG     = "DEBUG";
    public final static String WARNING   = "WARNING";
    public final static String ERROR     = "ERROR";
    public final static String CRITICAL  = "CRITICAL";
    public final static String CONF  	 = "CONF";
    
    public static String logGroupId 	= "";
    public static String logUserId 		= "";
    public static String logLoginId 	= "";
    public static String logInfo 		= "";
    
    //protected static String filename;
    protected static PrintWriter write;
    //protected static String path = EnvProperties.getValue("LOGPATH");
    protected static double fileSizeLimit = EnvProperties.getValue("LOGFILESIZELIMIT") == null ? 0 : Double.parseDouble(EnvProperties.getValue("LOGFILESIZELIMIT"));
    
    protected static HashMap<String,String>groupLogMap = new HashMap<String, String>();
        
    static 
    {
    	try {
	    	System.out.println("inside Logger static initilzer");
	    	groupLogMap.put("Default", EnvProperties.getValue("LOGPATH"));
	    	System.out.println("Default Log Path Loaded : " + EnvProperties.getValue("LOGPATH"));
    	
	    	String activeGroupIds = EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST") == null ? "" : EnvProperties.getPropertyValue("ACTIVE_GROUP_LIST");
	    	System.out.println("\nactiveGroupIds : " + activeGroupIds);
	    	if(!activeGroupIds.equals(""))
	    	{
	    		String [] activeGroupIdsArr = activeGroupIds.split(",");	    		
	    		
				for(int i=1 ; i < activeGroupIdsArr.length; i++)
				{
					try {
						String grpId = (String)activeGroupIdsArr[i];
						String lpath = EnvProperties.getValue("LOGPATH",(String)activeGroupIdsArr[i]);
						groupLogMap.put(grpId, lpath);
						System.out.println("Logger Path Defined for GroupId : "+grpId+" :path: "+lpath);
					}
					catch(Exception e) {
						System.out.println("Error in Loading Logger Path for GroupId : "+ (String)activeGroupIdsArr[i]);
					}
				}
				System.out.println("************ Logger Path Loaded SUCCESSFULL ************\n\n");
				/*System.out.println("groupLogMap : "+groupLogMap.size());
				for(Map.Entry m : groupLogMap.entrySet())
		    	{
		    		System.out.println("key : "+m.getKey()+" : value :"+m.getValue());
		    	}*/
	    	}
	    	else
	    	{
	    		System.out.println("Logger Path Defined : No active groupId found");
	    	}
    	}
		catch (Exception e) {
			System.out.println("******************Error In Loading Logger Path**************" + e.getMessage());
		}
    }
    
	public static synchronized void checkFileSize(String filename, String path)
	{
		String strFileNameWithoutExtn = null;
		String strExistingFileName = null;		
		File logFile = new File(path+filename);

		try
		{
			if(logFile.exists() || logFile.isFile())
			{
				double fileSize = logFile.length();	            
				double dataFileSizeInMB = fileSize / 1048576L;

				if(dataFileSizeInMB > fileSizeLimit)
				{
					strFileNameWithoutExtn = filename.substring(0,filename.length()-4); 
					
					for(int i=1;i < 100;i++)
					{
						strExistingFileName = strFileNameWithoutExtn+"_"+i+".txt";
						File existingFileCheck = new File(path+strExistingFileName);
						if (!existingFileCheck.exists() || !existingFileCheck.isFile())
						{
							logFile.renameTo(new File(path+strExistingFileName));							
							break;
						}	            			
					}				
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("Error in Logger:checkFileSize " + ex);
		}
		finally
		{
			strFileNameWithoutExtn = null;
			strExistingFileName = null;		
			logFile = null;
		}
	}
	
    public static synchronized void open(String filename) {
        try {
        	String path = "";
        	
        	if(logGroupId != null  && !logGroupId.equals(""))
        		path = groupLogMap.get(logGroupId) == null ? groupLogMap.get("Default") : groupLogMap.get(logGroupId);
        	else
        		path = groupLogMap.get("Default");
        			
            Calendar calendar = new GregorianCalendar();
            filename = filename + calendar.get(Calendar.DATE) + "-" + (calendar.get(Calendar.MONTH)+1) +"-" + (calendar.get(Calendar.YEAR))+".txt";
            
            if(fileSizeLimit > 0)
				checkFileSize(filename, path);
            
            write = new PrintWriter(new FileWriter(path+filename,true));
            calendar = null;
        }
        catch(Exception ioe){
        	System.out.println("Error in Logger:open method " + ioe);
        }
    }

    public static synchronized void log(String text, String callerFileName, String logFileName, Throwable t, String msgLevel) {
        if((Constants.enabledLogs).indexOf("ALL") != -1 || (Constants.enabledLogs).indexOf(msgLevel) != -1){
        	try
            {
	            Logger.open(logFileName);
	            write.println(msgLevel + " : "+ new java.util.Date()+" : Occured at : "+ callerFileName+ " GroupId : " + logGroupId + "\n");
	            write.println(text);
	            if(msgLevel.equals(ERROR) || msgLevel.equals(CRITICAL)) {
	                write.println(" The Exception is \t" +  t);
	            } 
	            write.println("=============================================================================");
	            write.flush();
            }
            finally
            {
                Logger.close();
            }
            
            if(msgLevel.equals(ERROR) || msgLevel.equals(CRITICAL)) {
            	try
	            {
	            	if(logFileName.endsWith("_Scan"))
	            		Logger.open("error_Scan");
	            	else
	            		Logger.open("error");
	            	
					write.println(msgLevel + " : "+ new java.util.Date()+" : Occured at : "+ callerFileName+ " GroupId : " + logGroupId +"\n");
					write.println(text);
					write.println(" The Exception is \t" +  t);
					write.println("=============================================================================");
					write.flush();
	            }
	            finally
	            {
					Logger.close();            	
	            }
			}
            
            //===
            if(msgLevel.equals(ERROR) || msgLevel.equals(CRITICAL)) {
            	String detailedLogFlag = EnvProperties.getPropertyValue("DETAILED_LOG") == null ? "" : EnvProperties.getPropertyValue("DETAILED_LOG");
			
				if(detailedLogFlag.equals("TRUE"))
				{
					try
		            {
						Logger.open("error_detailed");
						write.println(msgLevel + " : "+ new java.util.Date()+" : Occured at : "+ callerFileName+ " GroupId : " + logGroupId +"\n");
						write.println(text);
						write.println(" The Exception is \t" +  t);					
						
						if(t != null)
						{
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							t.printStackTrace(pw);
							
							write.println("\n Stack Trace \t" +  sw);
						
							try {
								sw.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							pw.close();					
						}
						
						write.println("=============================================================================");
						write.flush();
		            }
					finally
		            {
						Logger.close();            	
		            }
				}		
            }
			
            if(msgLevel.equals(ERROR) || msgLevel.equals(CRITICAL)) {
            	String logMailFlag = EnvProperties.getPropertyValue("LOG_MAIL") == null ? "" : EnvProperties.getPropertyValue("LOG_MAIL");
            	String logDBFlag = EnvProperties.getPropertyValue("LOG_DB") == null ? "" : EnvProperties.getPropertyValue("LOG_DB");
            	
				if(logMailFlag.equals("TRUE") || logDBFlag.equals("TRUE"))
				{
					String emailBody = "";
					emailBody = (msgLevel + " : "+ new java.util.Date()+" : Occured at : "+ callerFileName+ " GroupId : " + logGroupId +"\n");
					emailBody = emailBody + (text);
					emailBody = emailBody + (" The Exception is \t" +  t);
					
					if(t != null)
					{
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						t.printStackTrace(pw);	
						emailBody = emailBody + ("\n Stack Trace \t" +  sw);
						try {
							sw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						pw.close();
					}
					
					emailBody = emailBody + ("=============================================================================");
					
					try {
						if(logMailFlag.equals("TRUE"))
							errorMailSend("alert@remit.in", "alert@remit.in", "", "", "Error - Syetem Error", "System Error : \n\n" + emailBody + "\n\n", "", "RG");
						if(logDBFlag.equals("TRUE"))
							logErrorInDB(""+ t, "System Error : \n\n" + emailBody + "\n\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			//==================
        }
    }

    public static synchronized void close(){
    	if(write != null)
    		write.close();
    	
        write=null;
    }
    
    public static void logErrorInDB(String logType, String logText)
    {
    	Connection connLog	= null;
    	PreparedStatement pstmtLog = null;
    	int rs 				= -1;
    	
    	RGUtil	 ru = new RGUtil();
    	SQLRepository repository = ru.getLogErrorInDBObject();
    	
    	if(repository != null)
    	{
    		try 
	    	{
    			rs = repository.logErrorInDB(logType, logText, "RG");
	    	}
	    	catch(Throwable sqle)
	    	{
	    		System.out.println("Error in ResultSet :" + sqle);
	    		rs = -1;
	    	}
    	}
    	else
	    {
	    	try 
	    	{
	    		InitialContext ictx 		= null;
	    		java.util.Hashtable props 	= new java.util.Hashtable();
	    		ictx 						= new InitialContext(props);
	    		DataSource dataSource 		= (DataSource) ictx.lookup(EnvProperties.getValue("DATASOURCE"));
	    		connLog 					= dataSource.getConnection();
	    		
	    		//System.out.println("Got jdbc conn...."+connLog);
	    		
	    		pstmtLog 		= connLog.prepareStatement("INSERT INTO ErrorLog (logType, logText, server) values(? , ?, ?)");
	    		pstmtLog.setString(1, logType);
	    		pstmtLog.setString(2, logText);  
	    		pstmtLog.setString(3, "APP");  
	    		
	    		try
	    		{	
	    			rs	 = pstmtLog.executeUpdate();
	    			System.out.println("ResultSet :" + rs);
	    		}
	    		catch(Throwable t)
	    		{
	    			System.out.println("Error in ResultSet :" + t);
	    		}
	    	}
	    	catch(Throwable sqle)
	    	{
	    		rs = -1;
	    	}
	    	finally
			{
				try 
				{
					if(pstmtLog != null) pstmtLog.close();
					if(connLog != null) connLog.close();		
				}
				catch (Throwable sqle) 
				{
					System.out.println("Error in setRepositoryConnection :" + sqle);
				}
			}
	    }
    	
    	System.out.println("Final ResultSet :" + rs);  
    }
    
    public static void errorMailSend(String to, String from, String reply, String cc, String subject, String matter, String mailId, String groupId)
    {
    	System.out.println("inside simpleMailSend()");
    	    	
    	String resultMail = "false";
    	
    	if((to != null) && !(to.equals("")))
    	{
			System.out.println("Without Authentication resultMail : "+resultMail + " mailId : " + mailId + " to : " + to + " from : "+from + " cc : "+cc);

			String host = EnvProperties.getValue("MAILERHOST");
			String port = EnvProperties.getValue("MAILERPORT");
			System.out.println("inside errorMailSend() host : " +host+" port : "+port);
			
			try
			{
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host",host);
				properties.put("mail.smtp.port",port);
				properties.put("mail.smtp.auth","false"); 
				Session s = Session.getInstance(properties,null);
				MimeMessage message = new MimeMessage(s);
				InternetAddress[] toAddresses, ccAddress;
				message.setFrom(new InternetAddress(from));
				toAddresses = InternetAddress.parse(to);

				if(reply != null && !reply.equals(""))
					message.setReplyTo(new InternetAddress[]{new InternetAddress(reply)});
				
				message.setRecipients(Message.RecipientType.TO, toAddresses);
				
				Transport tr = s.getTransport("smtp");
				tr.connect(host, "auth@remitguru.com", "Wel@Remit@)@)");
				
				if(cc != null && !cc.equals(""))
				{
					ccAddress = InternetAddress.parse(cc);
					message.addRecipients(Message.RecipientType.CC, ccAddress);
				}
				
				message.setSubject(subject);
				message.setText(matter);
				message.saveChanges() ;
				Transport.send(message);
				resultMail = "true";
			}
			catch (SendFailedException e)
			{
				resultMail="false";
				System.out.println("errorMailSend SendFailedException ==== "+e+ " SendFailedException to : " + to + " from : "+from + " cc : "+cc);
			}
			catch (Exception e)
			{
				resultMail = "false";
				System.out.println("errorMailSend Exception === "+e+ " to : " + to + " from : "+from + " cc : "+cc);
			}
			catch(Throwable t)
			{
				resultMail = "false";
				System.out.println("errorMailSend Throwable === "+t+ " to : " + to + " from : "+from + " cc : "+cc);
			}
    	}
    	else
    	{
    		resultMail = "false";
    	}

    	System.out.println("Final resultMail : " + resultMail);
    }
    
    public static String getLogGroupId() {
		return logGroupId;
	}

	public static void setLogGroupId(String logGroupId) {
		Logger.logGroupId = logGroupId;
	}

	public static String getLogUserId() {
		return logUserId;
	}

	public static void setLogUserId(String logUserId) {
		Logger.logUserId = logUserId;
	}

	public static String getLogLoginId() {
		return logLoginId;
	}

	public static void setLogLoginId(String logLoginId) {
		Logger.logLoginId = logLoginId;
	}

	public static String getLogInfo() {
		return logInfo;
	}

	public static void setLogInfo(String logInfo) {
		Logger.logInfo = logInfo;
	}
    //public static void main(String[] args) {  }
	
}
