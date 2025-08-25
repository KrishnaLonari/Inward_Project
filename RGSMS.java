package com.api.remitGuru.component.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.api.remitGuru.component.util.dao.SQLRepository;
import com.api.remitGuru.web.controller.RGUserController;

public class RGSMS {
	
	SQLRepository repository;
	
	public RGSMS(){
	
		RGUtil rgutil = new RGUtil();
		repository = (SQLRepository)(rgutil.getRepositoryObject());
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

	
	public int sendSMS(String to, String smsContent, String[] data, String eventId)
	{	
		int send =-1;
		
		try
		{
			this.groupId	= data[0] == null ? "" : data[0];			
			this.to			= data[2] == null ? "" : data[2];
			this.userId		= data[3] == null ? "" : data[3];
			this.body		= smsContent == null ? "" : smsContent;
			this.eventId	= eventId == null ? "" : eventId;
					
			send = this.send();					
		}
		catch(Throwable t)
		{
			send = -1;
			Logger.log("Error in sendSMS", "RGSMS.java", "SMS", t, Logger.CRITICAL);
		}

		return send;
	}

	/**
	 * <p>Method inserts sms details into database</p> 
	 *
     * Method Name 						: send()
     * 
	 *
     * @return							  The resultant int
	 */ 
	
	private int send()
	{		  
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query = new StringBuffer();
		int result = -1;
		ArrayList smsProvider = new ArrayList();

		try
		{
			smsProvider = getSmsProvider();
		}
		catch(Throwable t)
		{
			Logger.log("Error in getting sms provider", "RGSMS.java", "SMS", t, Logger.CRITICAL);
		}
		
		if(smsProvider != null && smsProvider.size() > 0)
		{
			String strSmsProvider = ((String[])smsProvider.get(0))[0];
			String strSmsFrom = ((String[])smsProvider.get(0))[1];
			
			query.append(" INSERT INTO SMS_POOL (	");
			query.append("		groupId,			");
			query.append("		userId,				");
			query.append("		send_flag,			");
			query.append("		sms_to,				");
			query.append("		sms_from,			");		
			query.append("		msg,				");		
			query.append("		eventId,			");
			query.append("		created_date,		");
			query.append("		smsProviderId)		");			
			query.append(" VALUES(					");
			query.append(" ? ,						");
			query.append(" ? ,						");
			query.append(" 'N',						");
			query.append(" ? ,						");
			query.append(" ? ,						");
			query.append(" ? ,						");
			query.append(" ? ,						");			
			query.append(" GETDATE(),				");
			query.append(" ? )						");	
			
			params.add(this.groupId); 
			paramTypes.add(0);
			params.add(this.userId); 
			paramTypes.add(0);
			params.add(this.to); 
			paramTypes.add(0);
			params.add(strSmsFrom); 
			paramTypes.add(0);		
			params.add(this.body); 
			paramTypes.add(0);		
			params.add(this.eventId); 
			paramTypes.add(0);
			params.add(strSmsProvider); 
			paramTypes.add(0);
	
			try
			{
				result = repository.retrieveIdentity(query, params, paramTypes,this.groupId);
			}
			catch (Throwable t)
			{
				result = -1;
				Logger.log("Error in send", "RGSMS.java", "SMS", t, Logger.CRITICAL);
			}
			finally
			{
				query = null;
				params = null;
				paramTypes = null;		
			}
		}
		else
		{
			Logger.log("Could not get sms provider for group id : " + this.groupId + " and mobile number : " + this.to, "RGSMS.java", "SMS", null, Logger.INFO);
		}
		
		return result;
	}

	private ArrayList getSmsProvider() 
	{
		ArrayList smsProvider = null;
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query = new StringBuffer();

		query.append(" SELECT									");
		query.append("		smsProviderId,						");	
		query.append("		smsFrom								");	
		query.append(" FROM										");
		query.append("		GroupSMSServiceProvider				");
		query.append(" WHERE									");
		query.append("		groupId = ?							");
		query.append("		AND countryCode =					");
		query.append("		(SELECT TOP 1 countryCode 	 		");
		query.append("		FROM Country						");
		query.append("		WHERE countryPhoneCode = ?			");
		query.append("		AND isActive = 'Y' 					");
		query.append("		ORDER BY displayOrder)				");
		query.append("		AND isActive='Y'					");
		
		
		params.add(this.groupId); 
		paramTypes.add(0);
		params.add(this.to != null ? ((this.to + "-").split("-"))[0] : ""); 
		paramTypes.add(0);
		
		try
		{
			smsProvider = repository.getRepositoryItems(query, params, paramTypes,this.groupId);
			Logger.log("getSmsProvider query : " + query + "\n params : " + params + "\n smsProvider : " + smsProvider, "RGSMS.java", "SMS", null, Logger.INFO);
		}
		catch(Throwable t)
		{
			smsProvider = null;
			Logger.log("Error in getSmsProvider", "RGSMS.java", "SMS", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;
		}

		return smsProvider;	
	}

	public int createSmsResponse(Map responseMap, String groupId)
	{  		
		ArrayList<Object> params				= new ArrayList<Object>();
		ArrayList<Integer> paramTypes			= new ArrayList<Integer>();
		StringBuffer insQry						= new StringBuffer();		
		int insertResult = -1;
		String smsId="",smsStatus="",smsFailureReason="",deliveryDate="",hostAddress="";
		
		Set<String> keys = responseMap.keySet();
		Iterator<String> i=keys.iterator();
		while(i.hasNext())
		{
			String key = (String) i.next();
			if(key.equals("id"))
			{				    
				smsId = (String)responseMap.get(key); 
			}
			else if(key.equals("status"))
			{
				smsStatus = (String)responseMap.get(key) == null ? "" : (String)responseMap.get(key); 
			}
			else if(key.equals("res"))
			{
				smsFailureReason = (String)responseMap.get(key) == null ? "" : (String)responseMap.get(key); 
			}
			else if(key.equals("delivery_date"))
			{
				deliveryDate = (String)responseMap.get(key) == null ? "" : (String)responseMap.get(key); 	
			}
			else if(key.equals("hostAddress"))
			{
				hostAddress = (String)responseMap.get(key) == null ? "" : (String)responseMap.get(key); 	
			}
		}

		ArrayList smsData 			 = new ArrayList();
		smsData.add(smsId);
		smsData.add(smsStatus);
		smsData.add(deliveryDate);
		smsData.add(smsFailureReason);
		smsData.add(hostAddress);
		smsData.add(groupId);

		RGUserController rgUserCntrl = new RGUserController();	

		try
		{			
			insertResult = rgUserCntrl.createSmsResponseList(smsData);					
		}
		catch(Throwable t)
		{
			Logger.log("Error in createSmsResponse", "RGUtil.java", "RGUtil", t, Logger.CRITICAL);
			insertResult = -1;
		}
		finally
		{
			rgUserCntrl = null;
			smsData = null;
		}
		
		return insertResult;
	}
	
	public int addSMSPool(ArrayList data) 
    {
        ArrayList<Object> params = new ArrayList<Object>();
        ArrayList<Integer> paramTypes = new ArrayList<Integer>();
        StringBuffer query = new StringBuffer();
        int result = -1;       

        query.append(" INSERT INTO SMS_POOL (	");
        query.append("		groupId,			");//0
        query.append("		userId,				");//1
        query.append("		send_flag,			");//2
        query.append("		sms_to,				");//3
        query.append("		sms_from,			");//4
        query.append("		msg,				");//5
        query.append("		eventId,			");//6
        query.append("		created_date,		");
        query.append("		send_date,			");
        query.append("		response,			");//7
        query.append("		smsProviderId)		");//8
        query.append(" VALUES(					");
        query.append(" ? ,						");
        query.append(" ? ,						");
        query.append(" ?,						");
        query.append(" ? ,						");
        query.append(" ? ,						");
        query.append(" ? ,						");
        query.append(" ? ,						");        
        query.append(" GETDATE(),				");
        query.append(" GETDATE(),				");
		query.append(" ? ,						");
        query.append(" ? )						");

        params.add((String)data.get(0));//groupId
        paramTypes.add(0);
        params.add((String)data.get(1));//userId
        paramTypes.add(0);
        params.add((String)data.get(2));//sendFlag
        paramTypes.add(0);
        params.add((String)data.get(3));//smsTo
        paramTypes.add(0);
        params.add((String)data.get(4));//smsFrom
        paramTypes.add(0);
        params.add((String)data.get(5));//msg
        paramTypes.add(0);
        params.add((String)data.get(6));//eventId
        paramTypes.add(0);
        params.add((String)data.get(7));//response
        paramTypes.add(0);
        params.add((String)data.get(8));//smsProviderId
        paramTypes.add(0);

        try 
        {
            result = repository.retrieveIdentity(query, params, paramTypes,(String)data.get(0));
        }
        catch (Throwable t) 
        {
            result = -1;
            Logger.log("Error in addSMSPool", "RGSMS.java", "SMS", t, Logger.CRITICAL);
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
