/*
 * @(#)Sequence.java	22-Mar-2012
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenues*******.
 * Use is subject to license terms.
 *
 */
package com.api.remitGuru.component.util;

import java.util.ArrayList;

import com.api.remitGuru.component.util.dao.SQLRepository;

/**
 * This is a Info class for RGUser
 *
 *
 *
 * @author  *******
 * @version 	1.00, 05-Mar-2012
 */

public class RGSequence  implements java.io.Serializable {
	
	SQLRepository repository;
	
	public RGSequence(){

		RGUtil rgutil = new RGUtil();
		repository = (SQLRepository)(rgutil.getRepositoryObject());
	}

	public String getNextUserId(String groupId)
	{
		String strUserId	= "-1";
		int userId	= -1;

		try
		{
			String dbGroupId = EnvProperties.getPropertyValue("COMMON_UNIQUE_SEQUENCE") == null ? "" : EnvProperties.getPropertyValue("COMMON_UNIQUE_SEQUENCE");
			if(dbGroupId.contains(","+groupId.trim()+","))
				groupId = "RG";

			userId	= repository.GetNewSeqVal_SEQ_UserMaster_userId(groupId);	
			
			int START = 4001;
			int END = 9999;
			java.util.Random random = new java.util.Random();
			
			//get the range, casting to long to avoid overflow problems
			long range = (long)END - (long)START + 1;
			// compute a fraction of the range, 0 <= frac < range
			long fraction = (long)(range * random.nextDouble());
			int randomNumber =  (int)(fraction + START);
			
			strUserId = "" + randomNumber + "" + userId;
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextUserId", "RGSequence.java", "User", t, Logger.CRITICAL);
		   strUserId = "-1";
		}		

		return "" + strUserId;
	}	
	
	public String generateRGTN(String sendGroupId, String sendUserId, String sendLoginId)
	{
		String rgtn	= "0";
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query = new StringBuffer();
		ArrayList result = new ArrayList();	

		try
		{
			//Calendar now = Calendar.getInstance();
			//rgtn = now.getTimeInMillis();
			////rgtn	= repository.GetNewSeqVal_SEQ_Transactions_rgtn();	

			query.append(" SELECT					");  
			query.append("		ISNULL(SUBSTRING(CONVERT(VARCHAR,MAX(rgtn)),11,len(MAX(rgtn)))+1, 1) as txnNo		");  //0	
			query.append(" FROM 					"); 
			query.append(" 		Transactions		"); 
			query.append(" WHERE 					"); 
			query.append("		sendGroupId	= ?		"); 
			query.append(" AND	sendUserId	= ? 	");
			query.append(" AND	sendloginid = ? 	");
			query.append(" GROUP BY					");
			query.append("		sendUserId			");

			params.add(sendGroupId.toUpperCase());
			paramTypes.add(0);
			params.add(sendUserId);
			paramTypes.add(0);
			params.add(sendLoginId.toUpperCase());
			paramTypes.add(0);
			
			try
			{			
				Logger.log("generateRGTN query : " + query +  "  \n params:  " + params, "RGSequence.java", "Transaction", null, Logger.INFO);
				result = repository.getRepositoryItems(query, params, paramTypes,sendGroupId.toUpperCase());
			}
			catch(Throwable t)
			{
				Logger.log("Error in generateRGTN", "RGSequence.java", "Transaction", t, Logger.CRITICAL);
			}
			finally
			{
				params     = null;
				paramTypes = null;
				query = null;
				rgtn = "0";
			}

			if(result != null && result.size() > 0)
			{
				String [] temp	= (String [])result.get(0);

				rgtn	= temp[0];
				int len = -1;
				if(rgtn.length() < 4)
					len = 3 - rgtn.length();

				for(int x=0; x <= len; x++)
				{
					rgtn = "0" + rgtn;
				}

				rgtn = sendUserId + rgtn;
			}
			else
			{
				rgtn = sendUserId + "0001";
			}

			Logger.log("generateRGTN for sendGroupId : " + sendGroupId + "  sendUserId : " + sendUserId + " sendLoginId : " + sendLoginId +  "  rgtn :  " + rgtn, "RGSequence.java", "Transaction", null, Logger.INFO);

		}
		catch(Throwable t)
		{
		   Logger.log("Error in generateRTRN", "RGSequence.java", "Transaction", t, Logger.CRITICAL);
		   rgtn = "0";
		}		

		return "" + rgtn;
	}	
	
	public String getNextCurrRateId(String groupId)
    {
		String currRateId	= "1";
		String temp [] 	= null;
		StringBuffer selectQuery= new StringBuffer();	
		ArrayList result = null;
		
		selectQuery.append(" SELECT max(currRateId) FROM  GroupCurrencyRate  ");
		
		try
		{
			result	= repository.getRepositoryItems(selectQuery, groupId);	
			
			if(result != null && result.size() > 0 )
			{
				temp = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					currRateId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextCurrRateId", "RGSequence.java", "CurrencyRate", t, Logger.CRITICAL);
		   currRateId = null;
		}
		finally
		{
			selectQuery = null;
			result      = null;
		}
		
		return currRateId;
	}
	
	public String getNextGroupCustomerRiskLimit(String groupId)
    {
		String groupCustRiskLimitId	  = "1";
		String temp []        	  = null;
		StringBuffer selectQuery  = new StringBuffer();	
		ArrayList result = null;
		
		selectQuery.append("SELECT max(groupCustRiskLimitId) FROM  GroupCustomerRiskLimitRules");
		
		try
		{
			result	= repository.getRepositoryItems(selectQuery, groupId);	
			
			if(result != null && result.size() > 0 )
			{
				temp = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					groupCustRiskLimitId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextgroupCustomerRiskLimit", "RGSequence.java", "GroupCustomerRiskLimitRules", t, Logger.CRITICAL);
		   groupCustRiskLimitId = null;
		}
		finally
		{
			selectQuery = null;
			result      = null;
		}
		
		return groupCustRiskLimitId;
	}
	
	public String getNextFeeId(String groupId)
    {
		String feeId	          = "1";
		String temp []            = null;
		StringBuffer selectQuery  = new StringBuffer();	
		ArrayList result          = null;
		
		selectQuery.append(" SELECT max(feeId) FROM  GroupFeeBreakUp  ");
		
		try
		{
			result	= repository.getRepositoryItems(selectQuery, groupId);	
			
			if(result != null && result.size() > 0 )
			{
				temp = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					feeId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextFeeId", "RGSequence.java", "GroupFee", t, Logger.CRITICAL);
		   feeId = null;
		}
		finally
		{
			//temp[] 	= null;
			selectQuery = null;
			result = null;
		}
		
		return feeId;
	}
	
	public String getNextMerchChargesBreakUpId(String groupId)
    {
		String merchCurrRateId	 = "1";
		String temp []      	 = null;
		StringBuffer selectQuery = new StringBuffer();	
		ArrayList result         = null;
		
		selectQuery.append(" SELECT max(merchCurrRateId) FROM  GroupMerchantChargesBreakUp ");
		
		try
		{
			result	= repository.getRepositoryItems(selectQuery, groupId);	
			
			if(result != null && result.size() > 0 )
			{
				temp = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					merchCurrRateId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextMerchCurrRateId", "RGSequence.java", "GroupMerchantCurrencyRate", t, Logger.CRITICAL);
		   merchCurrRateId = null;
		}
		finally
		{
			selectQuery = null;
			result = null;
		}
		
		return merchCurrRateId;
	}
	
	public String getNextGroupRiskLimitId(String groupId)
    {
		String groupRiskLimitId	  = "1";
		String temp []        	  = null;
		StringBuffer selectQuery  = new StringBuffer();	
		ArrayList result = null;
		
		selectQuery.append(" SELECT max(groupRiskLimitId) FROM  GroupRiskLimitRules  ");
		
		try
		{
			result	= repository.getRepositoryItems(selectQuery, groupId);	
			
			if(result != null && result.size() > 0 )
			{
				temp = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					groupRiskLimitId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextGroupRiskLimitId", "RGSequence.java", "RiskLimit", t, Logger.CRITICAL);
		   groupRiskLimitId = null;
		}
		finally
		{
			//temp[] 	= null;
			selectQuery = null;
			result = null;
		}
		
		return groupRiskLimitId;
	}
	
	public String getNextGroupSvcId(String groupId)
    {
		String groupSvcId	= "1";
		String temp [] 	= null;
		StringBuffer selectQuery= new StringBuffer();	
		ArrayList result = null;
		
		selectQuery.append(" SELECT max(groupSvcId) FROM  GroupServiceChargeBreakUp ");
		
		try
		{
			result	= repository.getRepositoryItems(selectQuery, groupId);	
			
			if(result != null && result.size() > 0 )
			{
				temp = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					groupSvcId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextGroupSvcId", "RGSequence.java", " GroupServiceCharge", t, Logger.CRITICAL);
		   groupSvcId = null;
		}
		finally
		{
			selectQuery = null;
			result      = null;
		}
		
		return groupSvcId;
	}
	
	public String getNextGroupServTaxId(String groupId)
    {
		String groupServTaxId	= "1";
		String temp [] 	= null;
		StringBuffer selectQuery= new StringBuffer();	
		ArrayList result = null;
		
		selectQuery.append(" SELECT max(groupServTaxId) FROM  GroupServiceTaxBreakUp ");
		
		try
		{
			result	= repository.getRepositoryItems(selectQuery, groupId);	
			
			if(result != null && result.size() > 0 )
			{
				temp = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					groupServTaxId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getNextGroupServTaxId", "RGSequence.java", " GroupServiceTax", t, Logger.CRITICAL);
		   groupServTaxId = null;
		}
		finally
		{
			selectQuery = null;
			result      = null;
		}
		
		return groupServTaxId;
	}
	
	public String getNextRPSerialId(String rpId, String groupId)
    {
		String serialId					= "1";
		StringBuffer selectQuery		= new StringBuffer();	
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList result				= null;
		
		selectQuery.append(" SELECT ISNULL(MAX(serialId),0) FROM  RPTransactions where rpId = ? ");
		
		params.add(rpId);
		paramTypes.add(0);

		try
		{				
			result = repository.getRepositoryItems(selectQuery, params, paramTypes, groupId);

			if(result != null && result.size() > 0 )
			{
				String temp [] = (String[])result.get(0);
				
				if(temp[0] != null)
				{
					serialId = (Integer.parseInt(temp[0]) + 1) + "";                    
				}
			}
		}
		catch(Throwable t)
		{
			Logger.log("Error in getNextRPSerialId", "RGSequence.java", "RPTransactions", t, Logger.CRITICAL);
		}
		finally
		{
			params		= null;
			paramTypes	= null;
			selectQuery = null;	
			result		= null;
		}	
		
		return serialId;
	}
	
	public String getRandomGenId(String countryCode, String groupId)
	{
		int genId	= -1;
		String dbGroupId = EnvProperties.getPropertyValue("COMMON_UNIQUE_SEQUENCE") == null ? "" : EnvProperties.getPropertyValue("COMMON_UNIQUE_SEQUENCE");
	
		if(dbGroupId.contains(","+groupId.trim()+","))
			groupId = "RG";	
			
		try
		{
			genId	= repository.GetRandomGenId_Transactions_txnRefNo(countryCode, groupId);
		}
		catch(Throwable t)
		{
		   Logger.log("Error in getRandomGenId", "RGSequence.java", "Transaction", t, Logger.CRITICAL);		   
		}		

		return "" + genId;
	}
}
