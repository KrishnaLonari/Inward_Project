package com.api.remitGuru.component.util;

import java.util.ArrayList;

import com.api.remitGuru.component.util.dao.SQLRepository;

public class RGMailer {
	
	SQLRepository repository;
	
	public RGMailer(){
	
		RGUtil rgutil = new RGUtil();
		repository = (SQLRepository)(rgutil.getRepositoryObject());
	}
	
	/*To address..*/
	public String to = null;

	/*From address..*/
	public String from = null;
	  
	/*cc address..*/
	public String cc = "";
	  
	/*Subject ..*/
	public String subject = "(no subject)";
	  
	/*Body of mail..*/
	public String body = null;

	/*Priority of mail..*/
	public int priority = 1;

	/*reply address..*/
	public String reply = null;

	/*eventId ..*/
	public String eventId = null;

	/*attachmentFlag ..*/
	public String attachmentFlag = null;
	
	/*attachmentContent ..*/
	public String attachmentContent = null;
	
	/*attachmentFileName ..*/
	public String attachmentFileName = null;	
	
	public int sendMail(String to, String subject, String mailContent, String[] data, String eventId, String attachmentFlag, String attachmentContent, String attachmentFileName)
	{	
		int res =-1;
		int send =-1;
		
		try
		{
			String groupId = "";
			groupId	= data[0];
			
			this.from		= EnvProperties.getValue(groupId+"_MAIL_FROM", groupId) == null ? "" : EnvProperties.getValue(groupId+"_MAIL_FROM", groupId);
			this.reply		= EnvProperties.getValue(groupId+"_MAIL_REPLY", groupId) == null ? "" : EnvProperties.getValue(groupId+"_MAIL_REPLY", groupId);
			this.to			= data[1] == null ? "" : data[1];
			this.cc			= "";
			this.subject	= subject == null ? "" : subject;			
			this.body		= mailContent == null ? "" : mailContent;
			this.eventId	= eventId == null ? "" : eventId;
			this.attachmentFlag		= attachmentFlag == null ? "N" : attachmentFlag;
			this.attachmentContent	= attachmentContent == null ? "" : attachmentContent;
			this.attachmentFileName	= attachmentFileName == null ? "" : attachmentFileName;

			if((this.from).equals(""))
				Logger.log("In sendMail(): Value not found in "+groupId+"remit.xml for "+ groupId+"_MAIL_FROM", "RGMailer.java", "MissingKeys", null, Logger.INFO);
			
			if((this.reply).equals(""))
				Logger.log("In sendMail(): Value not found in "+groupId+"remit.xml for "+ groupId+"_MAIL_REPLY", "RGMailer.java", "MissingKeys", null, Logger.INFO);
			
			send = this.send(data);
			if(send == -1)
			{
				res = -1;
			} 
			else
			{
				res = send;
			}			
		}
		catch(Throwable t)
		{
			res = -1;
			Logger.log("Error in sendFormatedMail", "RGMailer.java", "Mailer", t, Logger.CRITICAL);
		}

		return res;
	}

	/**
	 * <p>Method inserts email details into database</p> 
	 *
     * Method Name 						: send( )
     * 
	 *
     * @return							  The resultant int
	 */ 
	
	private int send(String[] data)
	{		  
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query = new StringBuffer();
		int result = -1;	

		query.append(" INSERT INTO mail_pool (	");
		query.append("		send_flag,			");
		query.append("		mail_to,			");
		query.append("		mail_from,			");
		query.append("		mail_reply,			");
		query.append("		cc,					");
		query.append("		msg,				");
		query.append("		subject,			");
		query.append("		eventId,			");
		query.append("		created_date,		");
		query.append("		groupId,			");
		query.append("		userId,				");
		
		query.append("		attachmentFlag,		");
		query.append("		attachmentContent,	");
		query.append("		attachmentFileName)	");		
		query.append(" VALUES(					");
		query.append(" 'N',	");
		query.append(" ? ,	");
		query.append(" ? ,	");
		query.append(" ? ,	");
		query.append(" ? ,	");
		query.append(" ? ,	");
		query.append(" ? ,	");
		query.append(" ? ,	");
		query.append(" GETDATE(), 	");
		query.append(" ? ,	");		
		query.append(" ? ,	");
		query.append(" ? ,	");
		query.append(" ? ,	");		
		query.append(" ? )	");
		
		params.add(this.to); 
		paramTypes.add(0);
		params.add(this.from); 
		paramTypes.add(0);
		params.add(this.reply); 
		paramTypes.add(0);
		params.add(this.cc); 
		paramTypes.add(0);
		params.add(this.body); 
		paramTypes.add(0);
		params.add(this.subject); 
		paramTypes.add(0);
		params.add(this.eventId); 
		paramTypes.add(0);
		params.add(data[0]); 
		paramTypes.add(0);
		params.add(data[3]); 
		paramTypes.add(0);
		
		params.add(this.attachmentFlag); 
		paramTypes.add(0);
		params.add(this.attachmentContent); 
		paramTypes.add(0);
		params.add(this.attachmentFileName); 
		paramTypes.add(0);

		try
		{
			Logger.log("send params : " + params, "RGMailer.java", "Mailer", null, Logger.INFO);
			result = repository.executeQuery(query, params, paramTypes, data[0]);
		}
		catch (Throwable t)
		{
			result = -1;
			Logger.log("Error in send", "RGMailer.java", "Mailer", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;		
		}
		
		return result;
	}
}
