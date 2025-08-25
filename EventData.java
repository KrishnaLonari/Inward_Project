package com.api.remitGuru.component.util;

import java.util.ArrayList;

import com.api.remitGuru.component.util.Logger;
import com.api.remitGuru.component.util.dao.SQLRepository;
import com.api.remitGuru.component.util.EncryptionDecryption;
//********************************************
//		PLEASE NOTE
//********************************************
//This Java Class is assocoated with 

//secure/admin/addEventMaster.jsp 
//secure/admin/modifyEventMaster.jsp
//secure/admin/addGroupEventMap.jsp

//Any changes in method select parameter, needs to be map with the drop down values in this pages
//First element of the select query must be a Group ID for text mailer
//Second element of the select query must be a Email ID for mailer
//Third element of the select query must be a mobile no for mailer

public class EventData {
	
	SQLRepository repository;
	
	/**
	 *	Default no parameter constructor
	 */
	public EventData(){
	
		RGUtil rgutil = new RGUtil();
		repository = (SQLRepository)(rgutil.getRepositoryObject());
	}
	
	public ArrayList getSenderDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		groupId,			");//0
		query.append(" 		emailId,			");//1
		query.append(" 		mobile,				");//2
		query.append(" 		userId,				");//3
		query.append(" 		smsFlag,			");//4	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");//10
		query.append(" 		UPPER(SUBSTRING(firstName,1,1)) + LOWER(SUBSTRING(firstName,2,len(firstName))),		");		
		query.append(" 		UPPER(SUBSTRING(middleName,1,1)) + LOWER(SUBSTRING(middleName,2,len(middleName))),	");		
		query.append(" 		UPPER(SUBSTRING(lastname,1,1)) + LOWER(SUBSTRING(lastname,2,len(lastname))),		");	//13	
		query.append(" 		loginId				");//14
		query.append(" FROM						");
		query.append(" 		Sender				");
		query.append(" WHERE groupId	= ?		");
		query.append(" AND loginId		= ?		");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getSenderDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	public ArrayList getUserExchangeRatePreLoginDtls(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		//select loginId, a.groupId, exchangeRate, a.sendCurrencyCode, b.conversionRate from UserExchangeRate a, GroupCurrencyRate b 
		//where b.sendCountryCode=a.sendCountryCode and b.groupId=a.groupId and b.programCode='FER' and a.loginid='krish@gmail.com'

		query.append("	SELECT																						");
		query.append("		a.groupId,			                                                                	");//0
		query.append("		a.loginId,			                                                                	");//1
		query.append("		a.mobile,				                                                                ");//2
		query.append("		'' filler,			                                                                	");//3
		query.append("		'' filler,			                                                                	");//4
		query.append("		'' filler,			                                                                	");//5
		query.append("		'' filler,			                                                                	");//6
		query.append("		'' filler,			                                                                	");//7
		query.append("		UPPER(SUBSTRING(a.firstName,1,1)) + LOWER(SUBSTRING(a.firstName,2,len(a.firstName))),	");//8		
		query.append("		UPPER(SUBSTRING(a.lastname,1,1)) + LOWER(SUBSTRING(a.lastname,2,len(a.lastname))),   	");//9
		query.append("		a.sendCurrencyCode,        																");//10
		query.append("		a.exchangeRate,       																	");//11
		query.append("		b.conversionRate        																");//12
		query.append("	FROM						                                                            	");
		query.append("		UserExchangeRate a, GroupCurrencyRate b 	                                        	");
		query.append("	WHERE a.groupId	= ?		                                                        			");
		query.append("	AND a.loginId		= ?	                                                    				");
		query.append("	AND b.sendCountryCode=a.sendCountryCode	                                                    ");
		query.append("	AND b.groupId=a.groupId 	                                                    			");
		query.append("	AND b.programCode='FER'                                                  					");

		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getSenderDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}

	public ArrayList getNonLiveLeadDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		groupId,			");//0
		query.append(" 		emailId,			");//1
		query.append(" 		mobile,				");//2
		query.append(" 		mobileCountryCode,	");//3
		query.append(" 		sendCountry,		");//4
		query.append(" 		'' filler,			");//5
		query.append(" 		'' filler,			");//6
		query.append(" 		'' filler,			");//7
		query.append(" 		UPPER(SUBSTRING(firstName,1,1)) + LOWER(SUBSTRING(firstName,2,len(firstName))),		");	//8	
		query.append(" 		UPPER(SUBSTRING(lastname,1,1)) + LOWER(SUBSTRING(lastname,2,len(lastname)))		");	//9
		query.append(" FROM						");
		query.append(" 		leadNonliveCountriesDtls				");
		query.append(" WHERE groupId	= ?		");
		query.append(" AND emailId		= ?		");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getSenderDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}

	public ArrayList getUserDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		groupId,			");//0
		query.append(" 		emailId,			");//1
		query.append(" 		mobile,				");//2
		query.append(" 		userId,				");//3
		query.append(" 		'' filler,			");//4	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");//10
		query.append(" 		UPPER(SUBSTRING(firstName,1,1)) + LOWER(SUBSTRING(firstName,2,len(firstName))),		");		
		query.append(" 		UPPER(SUBSTRING(middleName,1,1)) + LOWER(SUBSTRING(middleName,2,len(middleName))),	");		
		query.append(" 		UPPER(SUBSTRING(lastname,1,1)) + LOWER(SUBSTRING(lastname,2,len(lastname))),		");	//13	
		query.append(" 		loginId				");//14
		query.append(" FROM						");
		query.append(" 		UserMaster			");
		query.append(" WHERE groupId	= ?		");
		query.append(" AND loginId		= ?		");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getUserDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}

	public ArrayList getBranchTellerDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT	TOP 1																			");
		query.append(" 	b.groupId,																				");
		query.append(" 	b.emailId,		                                                                        ");
		query.append(" 	b.mobile,			                                                                    ");
		query.append(" 	b.userId,			                                                                    ");
		query.append(" 	'S' SMSflag,		                                                                    ");
		query.append(" 	'"	+ (String)eventData.get(10) + "' mailBody,		                                    ");
		query.append(" 	'' filler,			                                                                    ");
		query.append(" 	'' filler,			                                                                    ");
		query.append(" 	'' filler,			                                                                    ");
		query.append(" 	'' filler,			                                                                    ");
		query.append(" 	'' filler,			                                                                    ");
		query.append(" 	UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),	");	
		query.append(" 	UPPER(SUBSTRING(b.middleName,1,1)) + LOWER(SUBSTRING(middleName,2,len(b.middleName))),	");
		query.append(" 	UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),		");
		query.append(" 	b.loginId,                                                                              ");
		query.append(" 	a.txnrefno ,                                                                            ");
		query.append(" 	COALESCE(b.emailId, ' ') AS TellerEmailId,                                              ");
		query.append(" 	COALESCE(c.remarks, ' ') AS Documents						                            ");
		query.append(" FROM                                                                                     ");
		query.append(" 	Transactions a                                                                          ");
		query.append(" 	LEFT JOIN Usermaster as b ON b.loginId= a.tellerLoginId                                 ");
		query.append(" 	LEFT JOIN txnAdditionalDataRequests as c ON c.txnRefNo= a.txnrefno	                    ");
		query.append(" WHERE                                                                                    ");
		query.append(" 	a.sendgroupId	= ?                                                                     ");
		query.append(" 	and a.rgtn = ?	                                                                        ");
		query.append(" 	and a.channel='BRANCH'                                                                  ");
		query.append(" 	order by c.createdDate desc                                                             ");

		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(3));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getBranchTellerDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}

	public ArrayList getACHAccountDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();

		query.append("	SELECT							");
		query.append("		a.groupId,					");
		query.append("		b.emailId,					");
		query.append(" 		b.mobile,					");//2
		query.append(" 		b.userId,					");//3
		query.append(" 		b.smsFlag,					");//4
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");//10
		query.append("		a.achAccId,					");
		query.append(" 		UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),	");		
		query.append(" 		UPPER(SUBSTRING(b.middleName,1,1)) + LOWER(SUBSTRING(b.middleName,2,len(b.middleName))),");		
		query.append(" 		UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),		");
		query.append("		a.countryCode,				");//15
		query.append("		a.currencyCode,				");
		query.append("		a.accountHolder,			");
		query.append("		a.bankName,					");
		query.append("		'*****' + substring(a.routingNumber,LEN(a.routingNumber) -3 ,4) routingNumber,			");
		//query.append("		LEFT(REPLICATE('*', LEN(substring(a.accountNo,0 ,LEN(a.accountNo)-3))), LEN(substring(a.accountNo,0 ,LEN(a.accountNo)-3)))+substring(a.accountNo,LEN(a.accountNo) -3 ,5) accountNo, ");
		query.append("		a.accountNo,				");//20
		query.append("		a.accountType,				");//21
		query.append("		a.processType				");//22
		query.append("	FROM							");
		query.append("		ACHAccount a,				");
		query.append("		Sender b					");
		query.append("	WHERE							");
		query.append("		a.groupId	= b.groupId		");
		query.append("	AND	a.userId	= b.userId		");
		query.append("	AND	a.loginId	= b.loginId		");
		query.append("	AND a.groupId	= ?				");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);		
		
		if(((String)eventData.get(4) != null && !((String)eventData.get(4)).equals("")))
		{
			query.append("	AND a.achAccId	= ?			");
			params.add((String)eventData.get(4));
			paramTypes.add(0);
		}
		else
		{
			query.append(" AND a.loginId		= ?			");
			params.add((String)eventData.get(2));
			paramTypes.add(0);
			
			query.append(" ORDER BY  a.createdDate DESC 	");
		}

		try
		{
			//Logger.log("In getACHAccountDetail query : " + query + "\n Param : " + params + "\n eventData : " + eventData, "EventData.java", "EventData", null, Logger.INFO);
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getACHAccountDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}
		
		try
		{
			if(result != null && result.size() > 0)
			{
				for(int i=0; i<result.size(); i++)
				{
					String temp[] = result.get(i);
					String encAcchAccNo = temp[20] == null ? "" : temp[20];
					Logger.log("temp[20] before : "+temp[20], "EventData.java", "EventData", null, Logger.INFO);
					String decAcchAccNo = "";
					try
					{
						EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
						decAcchAccNo = encDec.decryptString(encAcchAccNo.trim());
						//Logger.log("Decrypting data : "+encAcchAccNo+" after dec : "+decAcchAccNo, "EventData.java", "EventData", null, Logger.INFO);
					}
					catch(Throwable t)
					{
						decAcchAccNo = encAcchAccNo;
						Logger.log("Error while decrypting data : "+encAcchAccNo, "EventData.java", "EventData", t, Logger.CRITICAL);
					}
					temp[20] = decAcchAccNo.replaceAll("\\w(?=\\w{4})","*");
					Logger.log("temp[20] after : "+temp[20], "EventData.java", "EventData", null, Logger.INFO);
					result.set(i, temp);
				}
			}
		}
		catch(Throwable t)
		{
			Logger.log("Error in getACHAccountDetail while decrypting account number : " + t, "EventData.java", "EventData", t, Logger.CRITICAL);
		}

		return result;
	}

	public ArrayList getReceiverDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();

		query.append("	SELECT							");
		query.append("		a.groupId,					");
		query.append("		b.emailId,					");
		query.append(" 		b.mobile,					");//2
		query.append(" 		b.userId,					");//3
		query.append(" 		b.smsFlag,					");	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");//10
		query.append(" 		COALESCE(UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),''),	");		
		query.append(" 		COALESCE(UPPER(SUBSTRING(b.middleName,1,1)) + LOWER(SUBSTRING(b.middleName,2,len(b.middleName))),''), ");		
		query.append(" 		COALESCE(UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),''),		");
		query.append("		CONVERT(VARCHAR(20),a.registrationDate, 105),			"); 	
		query.append("		COALESCE(a.firstName,''),				");//15
		query.append("		COALESCE(a.middleName,''),				");
		query.append("		COALESCE(a.lastName,''),					");
		query.append("		a.countryCode,				");
		query.append("		a.state,					");
		query.append("		a.city,						");//20
		query.append("		a.pinCode,					");
		query.append("		a.emailId,					");
		query.append("		a.mobile,					");
		query.append("		concat('******',substring(convert(varchar,a.accNumber),len(a.accNumber)-3, len(a.accNumber))) as maskAccNumber,				");
		query.append("		a.accType,					");//25
		query.append("		a.micr,						");
		query.append("		a.bankName,					");
		query.append("		a.bankBranch,				");
		query.append("		a.bankCity,					");
		query.append("		a.bankState,				");//30
		query.append("		a.nickName,					");//31
		query.append("		a.bankDetails,				");//32
		query.append("		a.accNumber					");//33
		query.append("	FROM							");
		query.append("		Receiver a,					");
		query.append("		Sender b					");
		query.append("	WHERE							");
		query.append("		a.groupId	= b.groupId		");
		query.append("	AND	a.userId	= b.userId		");
		query.append("	AND	a.loginId	= b.loginId		");
		query.append("	AND a.groupId	= ?				");
		query.append("	AND a.userId	= ?				");
		query.append("	AND a.loginid	= ?				");
		query.append("	AND a.nickName	= ?				");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(1));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		params.add((String)eventData.get(5));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getReceiverDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}
		
		if(result != null && result.size() > 0)
		{
			String temp[] = result.get(0);
			String AcchAccNo = temp[24] == null ? "" : temp[24];
			Logger.log("temp[24] before : "+temp[24], "EventData.java", "EventData", null, Logger.INFO);
			String decAcchAccNo = "";
			try
			{
				EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
				decAcchAccNo = encDec.decryptString(AcchAccNo.trim());
				//Logger.log("Decrypting data : "+encAcchAccNo+" after dec : "+decAcchAccNo, "EventData.java", "EventData", null, Logger.INFO);
			}
			catch(Throwable t)
			{
				decAcchAccNo = AcchAccNo;
				Logger.log("Error while decrypting data : "+AcchAccNo, "EventData.java", "EventData", t, Logger.CRITICAL);
			}
			
			temp[24] = decAcchAccNo.replaceAll("\\w(?=\\w{4})","*");
			String IbanNo ="",bankDetails="";
			if(decAcchAccNo.equals(""))
			{
				bankDetails = temp[32] == null ? "" : temp[32];
				if(bankDetails.contains("~"))
				{
					IbanNo = bankDetails.split("~")[3].replaceAll("-", "");
					temp[24] = IbanNo.replaceAll("\\w(?=\\w{4})","*");
				}
			}
			
			
			result.set(0, temp);
			
			
		}

		return result;
	}

	public ArrayList getTransactionDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();

		query.append("	SELECT							");
		query.append("		a.sendGroupId,				");
		query.append("		b.emailId,					");
		query.append(" 		b.mobile,					");//2
		query.append(" 		b.userId,					");//3
		query.append(" 		b.smsFlag,					");	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");//10
		query.append("		a.rgtn,						");
		query.append(" 		COALESCE(UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),''),	");		
		query.append(" 		COALESCE(UPPER(SUBSTRING(b.middleName,1,1)) + LOWER(SUBSTRING(b.middleName,2,len(b.middleName))),''),");		
		query.append(" 		COALESCE(UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),''),		");
		query.append("		CONVERT(VARCHAR(20),a.bookingDate, 105),		"); //15 
		query.append("		a.sendAmount,				"); 
		query.append("		a.recvNickName,				");
		query.append(" 		COALESCE(UPPER(SUBSTRING(a.recvFirstName,1,1)) + LOWER(SUBSTRING(a.recvFirstName,2,len(a.recvFirstName))),''),	");		
		query.append(" 		COALESCE(UPPER(SUBSTRING(a.recvMiddleName,1,1)) + LOWER(SUBSTRING(a.recvMiddleName,2,len(a.recvMiddleName))),''),");
		query.append(" 		COALESCE(UPPER(SUBSTRING(a.recvLastName,1,1)) + LOWER(SUBSTRING(a.recvLastName,2,len(a.recvLastName))),''),		");
		query.append("		a.sendCurrencyCode,			");
		query.append("		c.payAmount,				");
		query.append("		a.recvCurrencyCode,			");
		query.append("		ISNULL(d.userDescription , '') subStatus,		"); //24
		query.append("		b.address1+' '+b.address2+' '+b.address3,		"); //25
		query.append("		e.country,										"); //26
		query.append("		b.state,										"); //27
		query.append("		b.city,											"); //28
		query.append("		b.zip,											"); //29
		query.append("		recvBankName,									"); //30
		query.append("		recvBankBranch,									"); //31
		query.append("		a.groupTxnFee + a.custTxnFee AS Fee,			"); //32	
		query.append("		a.txnRefNo,										"); //33
		query.append("		c.conversionRate,								"); //34
		query.append("		c.totalspread,									"); //35
		query.append("		c.serviceTaxAmt,								"); //36
		query.append("		c.eduCessTaxAmt,								"); //37
		query.append("		c.highEduCessTaxAmt,							"); //38
		query.append("		CAST(ROUND(c.conversionRate-(c.conversionrate * c.totalspread),2,1) AS DECIMAL(10,2)),	"); //39
		query.append("		ROUND(c.serviceTaxAmt,0) + ROUND((c.eduCessTaxAmt+c.highEduCessTaxAmt),0),				"); //40
		query.append("		a.sendModecode,				"); //41
		query.append("		a.programCode,				"); //42
		query.append("		a.recvAmount,				"); //43
		query.append("		a.conversionRate,			"); //44
		query.append("		a.totalspread,				"); //45
		query.append("		CAST(ROUND(a.conversionRate-(a.conversionrate * a.totalspread),2,1) AS DECIMAL(10,2)),	"); //46
		query.append("		CONVERT(VARCHAR(20),a.expDeliveryDate, 105),	"); //47
		query.append("		replace(txnRefNo,'RXM',''),	"); //48
		query.append("		CONVERT(VARCHAR(20),GETDATE(), 105)							"); //49
		query.append("	FROM							");
		query.append("		Transactions a,				");
		query.append("		Sender b,					");
		query.append("		TransactionStatusDetails c	");
		query.append("		LEFT JOIN SubStatus d ON d.subStatusCode = c.subStatusCode,   ");
		query.append("		Country e				   ");
		query.append("	WHERE							");
		query.append("		a.rgtn			= c.rgtn	");
		query.append("	AND	c.recordStatus	= 'A'		");
		query.append("	AND	a.sendGroupId	= b.groupId	");
		query.append("	AND	a.sendUserId	= b.userId	");
		query.append("	AND	a.sendLoginId	= b.loginId	");
		query.append("	AND b.countryCode	= e.countryCode	");
		query.append("	AND a.sendGroupId	= ?			");
		query.append("	AND a.rgtn			= ?			");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(3));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getTransactionDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	
	public ArrayList getTransactionReceiverDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();

		query.append("	SELECT							");
		query.append("		a.sendGroupId,				");
		query.append("		a.recvEmailId,				");
		query.append(" 		a.recvMobileNo,				");//2
		query.append(" 		b.userId,					");//3
		query.append(" 		b.smsFlag,					");	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");//10
		query.append("		a.rgtn,						");
		query.append(" 		UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),	");		
		query.append(" 		UPPER(SUBSTRING(b.middleName,1,1)) + LOWER(SUBSTRING(b.middleName,2,len(b.middleName))),");		
		query.append(" 		UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),		");
		query.append("		CONVERT(VARCHAR(20),a.bookingDate, 105),		"); //15 
		query.append("		a.sendAmount,				"); 
		query.append("		a.recvNickName,				");
		query.append(" 		UPPER(SUBSTRING(a.recvFirstName,1,1)) + LOWER(SUBSTRING(a.recvFirstName,2,len(a.recvFirstName))),");		
		query.append(" 		UPPER(SUBSTRING(a.recvMiddleName,1,1)) + LOWER(SUBSTRING(a.recvMiddleName,2,len(a.recvMiddleName))),");
		query.append(" 		UPPER(SUBSTRING(a.recvLastName,1,1)) + LOWER(SUBSTRING(a.recvLastName,2,len(a.recvLastName))),");//20
		query.append("		a.sendCurrencyCode,			");
		query.append("		c.payAmount,				");
		query.append("		a.recvCurrencyCode,			");
		query.append("		ISNULL(d.userDescription , '') subStatus,		"); //24
		query.append("		recvBankName,				"); //25
		query.append("		recvBankBranch,				"); //26
		query.append("		a.personalMsg,				"); //27
		query.append("		a.groupTxnFee + a.custTxnFee AS Fee,				");//28
		query.append("		a.txnRefNo,					"); //29
		query.append("		c.conversionRate,								"); //30
		query.append("		c.totalspread,									"); //31
		query.append("		c.serviceTaxAmt,								"); //32
		query.append("		c.eduCessTaxAmt,								"); //33
		query.append("		c.highEduCessTaxAmt,							"); //34
		query.append("		CAST(ROUND(c.conversionRate-(c.conversionrate * c.totalspread),2,1) AS DECIMAL(10,2)),	"); //35
		query.append("		ROUND(c.serviceTaxAmt,0) + ROUND((c.eduCessTaxAmt+c.highEduCessTaxAmt),0),				"); //36
		query.append("		a.recvAmount				"); //37
		query.append("	FROM							");
		query.append("		Transactions a,				");
		query.append("		Sender b,					");
		query.append("		TransactionStatusDetails c	");
		query.append("		LEFT JOIN SubStatus d ON d.subStatusCode = c.subStatusCode   ");
		query.append("	WHERE							");
		query.append("		a.rgtn			= c.rgtn	");
		query.append("	AND	c.recordStatus	= 'A'		");
		query.append("	AND	a.sendGroupId	= b.groupId	");
		query.append("	AND	a.sendUserId	= b.userId	");
		query.append("	AND	a.sendLoginId	= b.loginId	");
		query.append("	AND a.sendGroupId	= ?			");
		query.append("	AND a.rgtn			= ?			");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(3));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getTransactionDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	public ArrayList getPasswordDetail(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		groupId,			");//0
		query.append(" 		emailId,			");//1
		query.append(" 		mobile,				");//2
		query.append(" 		userId,				");//3
		query.append(" 		'S' smsFlag,		");	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");//10
		query.append(" 		UPPER(SUBSTRING(firstName,1,1)) + LOWER(SUBSTRING(firstName,2,len(firstName))),		");		
		query.append(" 		UPPER(SUBSTRING(middleName,1,1)) + LOWER(SUBSTRING(middleName,2,len(middleName))),	");		
		query.append(" 		UPPER(SUBSTRING(lastname,1,1)) + LOWER(SUBSTRING(lastname,2,len(lastname))),		");	
		query.append(" 		'" + (String)eventData.get(6) + "',					");		
		query.append("		CONVERT(VARCHAR(20),lastPasswordChangeDate, 105)	"); //15
		query.append(" FROM						");
		query.append(" 		UserMaster			");
		query.append(" WHERE groupId	= ?		");
		query.append(" AND loginId		= ?		");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		
		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getPasswordDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}	
	
	public ArrayList getReferralDetails(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		a.groupId,	");//0
		query.append(" 		a.refEmailId,	");//1
		query.append(" 		concat(mobileCountryCode, a.Mobile),	");//2
		query.append(" 		b.userId,			");//3		
		query.append(" 		b.smsFlag,			");
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");//10
		query.append(" 		a.refEmailId,		");//11
		query.append(" 		UPPER(SUBSTRING(a.FirstName,1,1)) + LOWER(SUBSTRING(a.FirstName,2,len(a.FirstName))),	");//12		
		query.append(" 		UPPER(SUBSTRING(a.LastName,1,1)) + LOWER(SUBSTRING(a.LastName,2,len(a.LastName))),		");//13
		query.append(" 		'',					");//14
		query.append(" 		UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),	");//15		
		query.append(" 		UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),		");//16
		query.append(" 		'" + EnvProperties.getValue("httpsecure", (String)eventData.get(0)) +"' URL				");//17
		query.append(" FROM						");
		query.append(" 		ReferalDtls	a,		");
		query.append(" 		Sender b			");		
		query.append(" WHERE a.groupId	= b.groupId		");
		query.append(" AND a.emailId	= b.loginId		");
		query.append(" AND b.groupId	= ?		");
		query.append(" AND a.refEmailId	= ?		");
				
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(7));
		paramTypes.add(0);
	
		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getReferralDetails query : " + query + " params : " + params, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}
	
		return result;
	}


	public ArrayList getReferrerDetails(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		a.referrerGroupId,	");//0
		query.append(" 		b.emailId,			");//1
		query.append(" 		b.mobile,			");//2
		query.append(" 		b.userId,			");//3		
		query.append(" 		b.smsFlag,			");
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");//10
		query.append(" 		UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),		");		
		query.append(" 		UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),			");
		query.append(" 		UPPER(SUBSTRING(a.referralFirstName,1,1)) + LOWER(SUBSTRING(a.referralFirstName,2,len(a.referralFirstName))),	");//13		
		query.append(" 		UPPER(SUBSTRING(a.referralLastName,1,1)) + LOWER(SUBSTRING(a.referralLastName,2,len(a.referralLastName))),		");//14
		query.append(" 		a.referralEmailId,	");//15
		query.append(" 		a.referralId		");//16
		query.append(" FROM						");
		query.append(" 		ReferralDetails	a,	");
		query.append(" 		Sender b			");		
		query.append(" WHERE a.referrerGroupId	= b.groupId		");
		query.append(" AND a.referrerUserId		= b.userId		");
		query.append(" AND a.referrerLoginId	= b.loginId		");
		query.append(" AND b.groupId	= ?		");
		query.append(" AND a.referralId	= ?		");
			
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(7));
		paramTypes.add(0);
	
		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getReferralDetails query : " + query + " params : " + params, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}
	
		return result;
	}

	public ArrayList getRPSenderDetails(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		groupId,			");//0
		query.append(" 		emailId,			");//1
		query.append(" 		mobile,				");//2
		query.append(" 		a.userId,			");//3
		query.append(" 		a.smsFlag,			");	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");//10
		query.append(" 		UPPER(SUBSTRING(firstName,1,1)) + LOWER(SUBSTRING(firstName,2,len(firstName))),		");		
		query.append(" 		UPPER(SUBSTRING(middleName,1,1)) + LOWER(SUBSTRING(middleName,2,len(middleName))),	");		
		query.append(" 		UPPER(SUBSTRING(lastname,1,1)) + LOWER(SUBSTRING(lastname,2,len(lastname))),		");//13
		query.append(" 		rpId,				");		
		query.append(" 		amount,				");		
		query.append(" 		rpBookingDate,		");		
		query.append(" 		recvNickName,		");	//17
		query.append(" 		frequency,			");	//18
		query.append(" 		CASE				");	//19 Recurring period
		query.append(" 		WHEN frequency=30 THEN 'Monthly'				");
		query.append(" 		WHEN frequency=90 THEN 'Quaterly'				");
		query.append(" 		WHEN frequency=180 THEN 'Half Yearly'			");
		query.append(" 		WHEN frequency=365 THEN 'Yearly'				");
		query.append(" 		ELSE ''	END AS RecurringPeriod,					"); 
		query.append(" 		b.sendCurrencyCode								"); //20
		query.append(" FROM						");
		query.append(" 		Sender a,			");
		query.append(" 		RecurringPaymentsDetail b ");
		query.append(" WHERE						");
		query.append(" a.groupId = b.sendGroupId	");
		query.append(" AND b.sendLoginId = a.loginId ");
		query.append(" AND b.sendUserId = a.userId	");
		query.append(" AND groupId		= ?			");
		query.append(" AND loginId		= ?			");
		query.append(" AND rpId			= ?			");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		params.add((String)eventData.get(8));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getRPSenderDetails query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	public ArrayList getSchTxnSenderDetails(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT					");
		query.append(" 		groupId,			");//0
		query.append(" 		emailId,			");//1
		query.append(" 		mobile,				");//2
		query.append(" 		a.userId,			");//3
		query.append(" 		a.smsFlag,			");	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");//10
		query.append(" 		UPPER(SUBSTRING(firstName,1,1)) + LOWER(SUBSTRING(firstName,2,len(firstName))),		");		
		query.append(" 		UPPER(SUBSTRING(middleName,1,1)) + LOWER(SUBSTRING(middleName,2,len(middleName))),	");		
		query.append(" 		UPPER(SUBSTRING(lastname,1,1)) + LOWER(SUBSTRING(lastname,2,len(lastname))),		");
		query.append(" 		strId,				");			
		query.append(" 		amount,				");		
		query.append(" 		srBookingDate,		");
		query.append(" 		recvNickName,		");	//17
		query.append(" 		frequency,			");	//18
		query.append(" 		CASE				");	//19 Recurring period
		query.append(" 		WHEN frequency=30 THEN 'Monthly'				");
		query.append(" 		WHEN frequency=90 THEN 'Quaterly'				");
		query.append(" 		WHEN frequency=180 THEN 'Half Yearly'			");
		query.append(" 		WHEN frequency=365 THEN 'Yearly'				");
		query.append(" 		ELSE ''	END AS RecurringPeriod,					");
		query.append(" 		b.sendCurrencyCode								"); //20
		query.append(" FROM						");
		query.append(" 		Sender a,			");
		query.append(" 		ScheduleTxnReminderDetail b			");
		query.append(" WHERE						");
		query.append(" a.groupId = b.sendGroupId	");
		query.append(" AND b.sendLoginId = a.loginId	");
		query.append(" AND b.sendUserId = a.userId	");
		query.append(" AND groupId		= ?			");
		query.append(" AND loginId		= ?			");
		query.append(" AND strId		= ?			");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		params.add((String)eventData.get(9));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getSchTxnSenderDetails query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	public ArrayList getEscalltionMailContentDetails(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();
		String loginId = (String)eventData.get(2);
			
		query.append(" SELECT					");
		query.append(" 		'" + (String)eventData.get(0) + "' filler, ");//0
		query.append(" 		'' filler,			");//1
		query.append(" 		'' filler,			");//2
		query.append(" 		'' filler,			");//3
		query.append(" 		'' filler,			");	
		query.append("		'"	+ (String)eventData.get(10) + "' mailBody, ");//5	
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler,			");
		query.append(" 		'' filler			");//10
				
		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes, (String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getEscalltionMailContentDetails query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}
		
		return result;
	}	
	
	public ArrayList getRPSenderDetails1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETRPSENDERDETAILS1";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		params.add((String)eventData.get(8));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getRPSenderDetails1 query : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	public ArrayList getSenderDetail1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETSENDERDETAIL1";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getSenderDetail1 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}

	public ArrayList getReceiverDetail1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETRECEIVERDETAIL1";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(1));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		params.add((String)eventData.get(5));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getReceiverDetail1 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}
		
		if(result != null && result.size() > 0)
		{
			String temp[] = result.get(0);
			String encAcchAccNo = temp[24] == null ? "" : temp[24];
			Logger.log("temp[24] before : "+temp[24], "EventData.java", "EventData", null, Logger.INFO);
			String decAcchAccNo = "";
			try
			{
				EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
				decAcchAccNo = encDec.decryptString(encAcchAccNo.trim());
				//Logger.log("Decrypting data : "+encAcchAccNo+" after dec : "+decAcchAccNo, "EventData.java", "EventData", null, Logger.INFO);
			}
			catch(Throwable t)
			{
				decAcchAccNo = encAcchAccNo;
				Logger.log("Error while decrypting data : "+encAcchAccNo, "EventData.java", "EventData", t, Logger.CRITICAL);
			}
			temp[24] = decAcchAccNo.replaceAll("\\w(?=\\w{4})","*");
			//Logger.log("temp[24] after : "+temp[24], "EventData.java", "EventData", null, Logger.INFO);
			result.set(0, temp);
		}

		return result;
	}

	public ArrayList getTransactionDetail1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETTRANSACTIONDETAIL1";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(3));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getTransactionDetail1 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}
		
		if(result != null && result.size() > 0)
		{
			String temp[] = result.get(0);
			String encAcchAccNo = temp[51] == null ? "" : temp[51];
			Logger.log("temp[51] before : "+temp[51], "EventData.java", "EventData", null, Logger.INFO);
			String decAcchAccNo = "";
			try
			{
				EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
				decAcchAccNo = encDec.decryptString(encAcchAccNo.trim());
				//Logger.log("Decrypting data : "+encAcchAccNo+" after dec : "+decAcchAccNo, "EventData.java", "EventData", null, Logger.INFO);
			}
			catch(Throwable t)
			{
				decAcchAccNo = encAcchAccNo;
				Logger.log("Error while decrypting data : "+encAcchAccNo, "EventData.java", "EventData", t, Logger.CRITICAL);
			}
			temp[51] = decAcchAccNo.replaceAll("\\w(?=\\w{4})","*");
			//Logger.log("temp[51] after : "+temp[51], "EventData.java", "EventData", null, Logger.INFO);
			result.set(0, temp);
		}

		return result;
	}
	
	public ArrayList getTransactionReceiverDetail1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETTRANSACTIONRECEIVERDETAIL1";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(3));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getTransactionReceiverDetail1 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	public ArrayList getPasswordDetail1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETPASSWORDDETAIL1";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		
		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getPasswordDetail1 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}	
	
	public ArrayList getReferralDetails1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETREFERRALDETAILS1";
				
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(7));
		paramTypes.add(0);
	
		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getReferralDetails1 queryName : " + queryName + " params : " + params, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}
	
		return result;
	}


	public ArrayList getReferrerDetails1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETREFERRERDETAILS1";
			
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(7));
		paramTypes.add(0);
	
		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getReferrerDetails1 queryName : " + queryName + " params : " + params, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}
	
		return result;
	}

	public ArrayList getSchTxnSenderDetails1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETSCHTXNSENDERDETAILS1";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(2));
		paramTypes.add(0);
		params.add((String)eventData.get(9));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getSchTxnSenderDetails1 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}

		return result;
	}
	
	public ArrayList getACHAccountDetail1(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETACHACCOUNTDETAIL1";

		params.add((String)eventData.get(0));
		paramTypes.add(0);		
		
		params.add((String)eventData.get(4));
		paramTypes.add(0);

		try
		{
			//Logger.log("In getACHAccountDetail queryName : " + queryName + "\n Param : " + params + "\n eventData : " + eventData, "EventData.java", "EventData", null, Logger.INFO);
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getACHAccountDetail1 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}
		
		try
		{
			if(result != null && result.size() > 0)
			{
				for(int i=0; i<result.size(); i++)
				{
					String temp[] = result.get(i);
					String encAcchAccNo = temp[20] == null ? "" : temp[20];
					Logger.log("temp[20] before : "+temp[20], "EventData.java", "EventData", null, Logger.INFO);
					String decAcchAccNo = "";
					try
					{
						EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
						decAcchAccNo = encDec.decryptString(encAcchAccNo.trim());
						//Logger.log("Decrypting data : "+encAcchAccNo+" after dec : "+decAcchAccNo, "EventData.java", "EventData", null, Logger.INFO);
					}
					catch(Throwable t)
					{
						decAcchAccNo = encAcchAccNo;
						Logger.log("Error while decrypting data : "+encAcchAccNo, "EventData.java", "EventData", t, Logger.CRITICAL);
					}
					temp[20] = decAcchAccNo.replaceAll("\\w(?=\\w{4})","*");
					//Logger.log("temp[20] after : "+temp[20], "EventData.java", "EventData", null, Logger.INFO);
					result.set(i, temp);
				}
			}
		}
		catch(Throwable t)
		{
			Logger.log("Error in getACHAccountDetail while decrypting account number : " + t, "EventData.java", "EventData", t, Logger.CRITICAL);
		}

		return result;
	}

	public ArrayList getACHAccountDetail2(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String queryName = "GETACHACCOUNTDETAIL2";
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);		
		
		params.add((String)eventData.get(2));
		paramTypes.add(0);

		try
		{
			//Logger.log("In getACHAccountDetail queryName : " + queryName + "\n Param : " + params + "\n eventData : " + eventData, "EventData.java", "EventData", null, Logger.INFO);
			result = repository.getRepositoryItemsFromFileWithoutHeader(queryName, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getACHAccountDetail2 queryName : " + queryName, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			queryName = null;
			params = null;
			paramTypes = null;			
		}
		
		try
		{
			if(result != null && result.size() > 0)
			{
				for(int i=0; i<result.size(); i++)
				{
					String temp[] = result.get(i);
					String encAcchAccNo = temp[20] == null ? "" : temp[20];
					Logger.log("temp[20] before : "+temp[20], "EventData.java", "EventData", null, Logger.INFO);
					String decAcchAccNo = "";
					try
					{
						EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
						decAcchAccNo = encDec.decryptString(encAcchAccNo.trim());
						//Logger.log("Decrypting data : "+encAcchAccNo+" after dec : "+decAcchAccNo, "EventData.java", "EventData", null, Logger.INFO);
					}
					catch(Throwable t)
					{
						decAcchAccNo = encAcchAccNo;
						Logger.log("Error while decrypting data : "+encAcchAccNo, "EventData.java", "EventData", t, Logger.CRITICAL);
					}
					temp[20] = decAcchAccNo.replaceAll("\\w(?=\\w{4})","*");
					//Logger.log("temp[20] after : "+temp[20], "EventData.java", "EventData", null, Logger.INFO);
					result.set(i, temp);
				}
			}
		}
		catch(Throwable t)
		{
			Logger.log("Error in getACHAccountDetail while decrypting account number : " + t, "EventData.java", "EventData", t, Logger.CRITICAL);
		}

		return result;
	}
	
	public ArrayList getTransactionDetailWithPoints(ArrayList eventData)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer query = new StringBuffer();

		query.append("	SELECT							");
		query.append("		a.sendGroupId,				");
		query.append("		b.emailId,					");
		query.append(" 		b.mobile,					");//2
		query.append(" 		b.userId,					");//3
		query.append(" 		b.smsFlag,					");	
		query.append(" 		'"	+ (String)eventData.get(10) + "' mailBody,			");//5	
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");
		query.append(" 		'' filler,					");//10
		query.append("		a.rgtn,						");
		query.append(" 		UPPER(SUBSTRING(b.firstName,1,1)) + LOWER(SUBSTRING(b.firstName,2,len(b.firstName))),	");		
		query.append(" 		UPPER(SUBSTRING(b.middleName,1,1)) + LOWER(SUBSTRING(b.middleName,2,len(b.middleName))),");		
		query.append(" 		UPPER(SUBSTRING(b.lastname,1,1)) + LOWER(SUBSTRING(b.lastname,2,len(b.lastname))),		");
		query.append("		CONVERT(VARCHAR(20),a.bookingDate, 105),		"); //15 
		query.append("		a.sendAmount,				"); 
		query.append("		a.recvNickName,				");
		query.append(" 		UPPER(SUBSTRING(a.recvFirstName,1,1)) + LOWER(SUBSTRING(a.recvFirstName,2,len(a.recvFirstName))),	");		
		query.append(" 		UPPER(SUBSTRING(a.recvMiddleName,1,1)) + LOWER(SUBSTRING(a.recvMiddleName,2,len(a.recvMiddleName))),");
		query.append(" 		UPPER(SUBSTRING(a.recvLastName,1,1)) + LOWER(SUBSTRING(a.recvLastName,2,len(a.recvLastName))),		");
		query.append("		a.sendCurrencyCode,			");
		query.append("		c.payAmount,				");
		query.append("		a.recvCurrencyCode,			");
		query.append("		ISNULL(d.userDescription , '') subStatus,		"); //24
		query.append("		b.address1+' '+b.address2+' '+b.address3,		"); //25
		query.append("		e.country,										"); //26
		query.append("		b.state,										"); //27
		query.append("		b.city,											"); //28
		query.append("		b.zip,											"); //29
		query.append("		recvBankName,									"); //30
		query.append("		recvBankBranch,									"); //31
		query.append("		a.groupTxnFee + a.custTxnFee AS Fee,			"); //32	
		query.append("		a.txnRefNo,										"); //33
		query.append("		c.conversionRate,								"); //34
		query.append("		c.totalspread,									"); //35
		query.append("		c.serviceTaxAmt,								"); //36
		query.append("		c.eduCessTaxAmt,								"); //37
		query.append("		c.highEduCessTaxAmt,							"); //38
		query.append("		CAST(ROUND(c.conversionRate-(c.conversionrate * c.totalspread),2,1) AS DECIMAL(10,2)),	"); //39
		query.append("		ROUND(c.serviceTaxAmt,0) + ROUND((c.eduCessTaxAmt+c.highEduCessTaxAmt),0),				"); //40
		query.append("		a.sendModecode,				"); //41
		query.append("		a.programCode,				"); //42
		query.append("		a.recvAmount,				"); //43
		query.append("		a.conversionRate,			"); //44
		query.append("		a.totalspread,				"); //45
		query.append("		CAST(ROUND(a.conversionRate-(a.conversionrate * a.totalspread),2,1) AS DECIMAL(10,2)),	"); //46
		query.append("		CONVERT(VARCHAR(20),a.expDeliveryDate, 105),	"); //47
		query.append("		replace(txnRefNo,'RXM',''),	"); //48
		query.append("	CASE WHEN a.sendModeCode='CIP'	THEN g.accHolderName 							");
		query.append("		 WHEN a.sendModeCode='IBTR' THEN b.firstName + '' + b.lastName 					");
		query.append("		 WHEN a.sendModeCode='WIRE' THEN b.firstName + '' + b.lastName 					");
		query.append("		 WHEN a.sendModeCode='ACH' OR a.sendModeCode='PAD'  THEN j.accountHolder END 'accountHolder',	"); //49
		query.append("	CASE WHEN a.sendModeCode='CIP'	THEN g.bankName 							");
		query.append("		 WHEN a.sendModeCode='IBTR' THEN h.bankName 							");
		query.append("		 WHEN a.sendModeCode='WIRE' THEN i.bankName 							");
		query.append("		 WHEN a.sendModeCode='ACH' OR a.sendModeCode='PAD'  THEN j.bankName END 'bankName',		"); //50
		query.append("	CASE WHEN a.sendModeCode='CIP'	THEN g.accountNo 							");
		query.append("		 WHEN a.sendModeCode='IBTR' THEN h.accountNo 							");
		query.append("		 WHEN a.sendModeCode='WIRE' THEN i.bankBranch							");
		query.append("		 WHEN a.sendModeCode='ACH' OR a.sendModeCode='PAD'  THEN j.accountNo END,			"); //51
		query.append("	CONVERT(VARCHAR(20),GETDATE(), 105),						"); //52
		query.append("	k.loyaltyPts,			"); //53
		query.append("	k.exchangeRate,			"); //54
		query.append("	k.serviceTaxAmt,		"); //55
		query.append("	k.sendAmount,			"); //56
		query.append("	k.recvAmount,			"); //57
		query.append("	k.fcyAmtSplit,			"); //58
		query.append("	k.destAmtSplit,			"); //59
		query.append("	k.claimType,			"); //60
		query.append("	k.custCategory,			"); //61
		query.append("	k.promoType,			"); //62
		query.append("	k.promoValue,			"); //63
		query.append("	k.promoCode,			"); //64
		query.append("	k.loyaltyType,			"); //65
		query.append("	k.exRateWithoutPromo	"); //66
		query.append("	FROM							");
		query.append("		Transactions a				");
		query.append("			LEFT JOIN TxnCipDtls g  ON a.rgtn = g.rgtn	");
		query.append("			LEFT JOIN TxnIbtrDtls h ON a.rgtn = h.rgtn	");
		query.append("			LEFT JOIN TxnWireDtls i ON a.rgtn = i.rgtn	");
		query.append("			LEFT JOIN TxnAchDtls j  ON a.rgtn = j.rgtn,	");
		query.append("		Sender b,					");
		query.append("		TransactionStatusDetails c	");
		query.append("		LEFT JOIN SubStatus d ON d.subStatusCode = c.subStatusCode,   ");
		query.append("		Country e,				   ");
		query.append("		TxnDtls k				   ");
		query.append("	WHERE							");
		query.append("		a.rgtn			= c.rgtn	");
		query.append("	AND	c.recordStatus	= 'A'		");
		query.append("	AND	a.sendGroupId	= b.groupId	");
		query.append("	AND	a.sendUserId	= b.userId	");
		query.append("	AND	a.sendLoginId	= b.loginId	");
		query.append("	AND b.countryCode	= e.countryCode	");
		query.append("	AND	a.rgtn			= k.rgtn	");
		query.append("	AND a.sendGroupId	= ?			");
		query.append("	AND a.rgtn			= ?			");
		
		params.add((String)eventData.get(0));
		paramTypes.add(0);
		params.add((String)eventData.get(3));
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)eventData.get(0));
		}
		catch (Throwable t)
		{
			result = null;
			Logger.log("Error in getTransactionDetail query : " + query, "EventData.java", "EventData", t, Logger.CRITICAL);
		}
		finally
		{
			query = null;
			params = null;
			paramTypes = null;			
		}
		
		if(result != null && result.size() > 0)
		{
			String temp[] = result.get(0);
			String encAcchAccNo = temp[51] == null ? "" : temp[51];
			Logger.log("temp[51] before : "+temp[51], "EventData.java", "EventData", null, Logger.INFO);
			String decAcchAccNo = "";
			try
			{
				EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
				decAcchAccNo = encDec.decryptString(encAcchAccNo.trim());
				//Logger.log("Decrypting data : "+encAcchAccNo+" after dec : "+decAcchAccNo, "EventData.java", "EventData", null, Logger.INFO);
			}
			catch(Throwable t)
			{
				decAcchAccNo = encAcchAccNo;
				Logger.log("Error while decrypting data : "+encAcchAccNo, "EventData.java", "EventData", t, Logger.CRITICAL);
			}
			temp[51] = decAcchAccNo.replaceAll("\\w(?=\\w{4})","*");
			//Logger.log("temp[51] after : "+temp[51], "EventData.java", "EventData", null, Logger.INFO);
			result.set(0, temp);
		}

		return result;
	}
}
