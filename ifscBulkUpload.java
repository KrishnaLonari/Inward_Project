package com.api.remitGuru.component.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


import com.incesoft.tools.excel.xlsx.SimpleXLSXWorkbook;
import com.incesoft.tools.excel.xlsx.Cell;
import com.incesoft.tools.excel.xlsx.Sheet;
import com.incesoft.tools.excel.xlsx.Sheet.SheetRowReader;

import com.api.remitGuru.component.util.dao.SQLRepository;

public class ifscBulkUpload
{

	SQLRepository					repository			= null;
	// StructuralValidation validation = null;
	boolean							isSFTPENABLE		= false;
	String							threadName			= "";
	String							groupId				= "IDFCIN";
	String							daemonId			= "";
	String							daemonStartTime		= "";
	String							hostMailId			= "";
	String							lsDefaultBank		= "";
	String							gsTimeDiff			= "";

	// For File
	HashMap<String, String>			ifscCodes			= null;
	HashMap<String, String>			newIfscCodes		= null;
	HashMap<String, String>			ifscCodesFromFile	= null;
	ArrayList<ArrayList<String>>	errorData			= null;

	boolean							isFileValid			= true;
	String							fileName			= null;
	String							upload_type			= "SFTP";
	String							status				= "A";
	String							addedBy				= "SFTP";
	String							tableName			= "BankBranch";

	int								failRecords			= 0;
	int								insertCounter		= 0;
	int								updateCounter		= 0;
	int								totalCounter		= 0;

	public ifscBulkUpload()
	{
		repository 		= new SQLRepository();
		daemonStartTime = getISTTime();
	}

	public String getISTTime()
	{
		String lsTime = "";
		try
		{
			Date date = new Date();
			DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
			formatter.setTimeZone(TimeZone.getTimeZone("IST"));
			lsTime = formatter.format(date).toString();
		}
		catch (Exception e)
		{
			Logger.log("Error in getTime....", "ifscBulkUpload.java", "ifscBulkUpload", e, Logger.INFO);
		}

		return lsTime;
	}

	public String getFileID(String arName)
	{
		java.util.Random r = new Random();
		String newName = arName.replaceAll("\\.", "").replaceAll("\\s", "");
		return newName + r.nextInt();
	}

	public ArrayList<String> checkAllIFSCValidations(ArrayList<String> ifscData)
	{
		ArrayList<String> errorData = new ArrayList<String>();

		if (ifscData.size() >= 4)
		{
			if (!validateAlphaNumericWithoutSpace(ifscData.get(0), 11) || !validateIfsc(ifscData.get(0)))
				errorData.add("Invalid IFSC Code at Row Number : " + ifscData.get(12));

			if (!validateAlphaNumericWithoutSpace(ifscData.get(1), 4))
				errorData.add("Invalid Bank Code at Row Number : " + ifscData.get(12));
		}

		return errorData;
	}

	public boolean validateAlphaNumericWithoutSpace(String value, int maxSize)
	{
		value = checkInputValue(value);
		value = removeAllJunkCharacters(value);
		value = value.trim();
		if (value.length() > maxSize)
		{
			return false;
		}
		if (value.equals(""))
		{
			return false;
		}
		return true;
	}

	private static String checkInputValue(String value)
	{
		if (value == null || value.equals("null"))
			value = "";

		return value;
	}

	public static String removeAllJunkCharacters(String str)
	{
		// str = str.replaceAll("'","''");
		// str = str.replaceAll("[()?:!.,;/\\{}]+", " ");
		str = str == null ? "" : str.trim();
		str = str.replaceAll("[^\\-\\da-zA-Z0-9_ \\.,]", "");
		return str.trim();
	}

	public boolean validateIfsc(String value)
	{
		return value.matches("[A-Z|a-z]{4}[0][a-zA-Z0-9]{6}$");
	}

	public static long getCRC32Checksum(byte[] bytes)
	{
		Checksum crc32 = new CRC32();
		crc32.update(bytes, 0, bytes.length);
		long cksSum = crc32.getValue();
		return cksSum;
	}

	public ArrayList<ArrayList<String>> getIFSCDataFromFile(String filePath)
	{
		ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
		int rowCnt = 1;
		ifscCodesFromFile = new HashMap<String, String>();

		try
		{
			Logger.log("filePath in getIFSCDataFromFile>> " + filePath, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			String ifscCode = "", bankName = "", micrBranchCode = "", micrCode = "", bankBranch = "", address = "", city = "", bankCode = "", trngFlag = "", district = "", state = "", lastUpdDate = "";
			SimpleXLSXWorkbook workbook = new SimpleXLSXWorkbook(new File(filePath));
			for (int ws = 0; ws < workbook.getSheetCount(); ws++)
			{
				Sheet sheetToRead = workbook.getSheet(ws, true);
				SheetRowReader reader = sheetToRead.newReader();

				Logger.log("Workbook obtained - " + ws, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
				Cell[] row;
				int rowPos = 0;
				List<String> list = null;

				int acceptedcolcount = 12;

				while ((row = reader.readRow()) != null)
				{
					if (rowPos++ == 0)
						continue;

					list = new ArrayList<String>();
					try
					{
						for (int i = 0; i < acceptedcolcount; i++)
						{
							Cell cell = row[i];
							if (cell != null && cell.getValue() != null)
								list.add(cell.getValue());
							else
								list.add("");
						}
					}
					catch (Exception e)
					{
						System.out.println("Cannot read row: " + rowPos);
						e.printStackTrace();
					}

					try
					{
						try
						{
							ifscCode = list.get(0);
							bankCode = list.get(1);
							micrBranchCode = list.get(2);
							trngFlag = list.get(3);
							bankName = list.get(4);
							bankBranch = list.get(5);
							city = list.get(6);
							address = list.get(7);
							micrCode = list.get(8);
							district = list.get(9);
							state = list.get(10);
							lastUpdDate = list.get(11);
							totalCounter++;

							ArrayList<String> ifscData = new ArrayList<String>();
							ifscData.add(ifscCode);// bankId//
							ifscData.add(bankCode);// ifscCode
							ifscData.add(micrBranchCode);// bankName
							ifscData.add(trngFlag); // isActive
							ifscData.add(bankName);// branchcode
							ifscData.add(bankBranch);// address
							ifscData.add(city);// city
							ifscData.add(address);// branch
							ifscData.add(micrCode);
							ifscData.add(district);
							ifscData.add(state);
							ifscData.add(lastUpdDate);
							ifscData.add(rowPos + "");

							ArrayList<String> errorDtls = checkAllIFSCValidations(ifscData);
							if (errorDtls != null && errorDtls.size() > 0)
							{
								errorData.add(errorDtls);
								failRecords++;
							}
							else
							{
								ifscCodesFromFile.put(ifscCode, ifscCode);
								records.add(ifscData);
							}

							ifscCode = "";
							bankName = "";
							micrBranchCode = "";
							micrCode = "";
							bankBranch = "";
							address = "";
							city = "";
							bankCode = "";
							trngFlag = "";
							district = "";
							state = "";
							lastUpdDate = "";
						}
						catch (Exception e)
						{
							e.printStackTrace();
							System.exit(0);
							Logger.log("Invalid File!!! 1", "ifscBulkUpload.java", "ifscBulkUpload", e, Logger.CRITICAL);
						}

						rowCnt++;
					}
					catch (Exception t)
					{
						Logger.log("Invalid File!!! 3", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Logger.log("Invalid File!!! 4", "ifscBulkUpload.java", "ifscBulkUpload", e, Logger.CRITICAL);
		}

		Logger.log("All IFSCMASTER FILE >> " + records.size(), "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);

		return records;
	}

	public int excecuteIfscBatchQueriesInsertData(String groupId)
	{
		ArrayList<Object> params = null;
		ArrayList<Integer> paramTypes = null;
		StringBuffer insQry = new StringBuffer();
		int result = -1;

		try
		{
			insQry = new StringBuffer();
			params = new ArrayList<Object>();
			paramTypes = new ArrayList<Integer>();

			insQry.append(" INSERT INTO BankBranch ( ");
			insQry.append(" 	ifscCode, 			"); 
			insQry.append(" 	bankId, 			"); 
			insQry.append(" 	micrBranchCode,		"); 
			insQry.append(" 	trngFlag, 			"); 
			insQry.append(" 	bankName, 			"); 
			insQry.append(" 	branchName, 		"); 
			insQry.append(" 	city, 				"); 
			insQry.append(" 	branchAddress,		"); 
			insQry.append(" 	micrCode, 			"); 
			insQry.append(" 	district, 			"); 
			insQry.append(" 	state, 				"); 
			insQry.append(" 	lastUpdDate, 		");
			insQry.append(" 	stateCode, 			");
			insQry.append(" 	cityCode, 			");
			insQry.append(" 	countryCode,		");
			insQry.append(" 	contact				");
			insQry.append(" ) 						");
			insQry.append(" SELECT 					");
			insQry.append(" 	ifscCode, 			"); 
			insQry.append(" 	bankId, 			"); 
			insQry.append(" 	micrBranchCode,		"); 
			insQry.append(" 	trngFlag, 			"); 
			insQry.append(" 	bankName, 			"); 
			insQry.append(" 	branchName, 		"); 
			insQry.append(" 	city, 				"); 
			insQry.append(" 	branchAddress,		"); 
			insQry.append(" 	micrCode, 			"); 
			insQry.append(" 	district, 			"); 
			insQry.append(" 	state, 				"); 
			insQry.append(" 	lastUpdDate, 		");
			insQry.append(" 	stateCode, 			");
			insQry.append(" 	cityCode, 			");
			insQry.append(" 	countryCode,		");
			insQry.append(" 	contact				");
			insQry.append(" FROM BankBranchTemp 	");

			try
			{
				result = repository.executeQuery(insQry, params, paramTypes, groupId);
			}
			catch (Throwable t)
			{
				Logger.log("Error in inner addIFSCData", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
				result = -1;
			}
		}
		catch (Throwable t)
		{
			Logger.log("Error in outer addIFSCData", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
			t.printStackTrace();
			result = -1;
		}
		return result;
	}

	public int excecuteIfscMaintBatchQueriesInsertData(ArrayList<ArrayList<String>> ifscData, String groupId, String filename, String createdBy)
	{
		ArrayList queries 						= new ArrayList();
		ArrayList<ArrayList<Object>> paramsList = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<Integer>> paramTypesList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Object> params 		= null;
		ArrayList<Integer> paramTypes 	= null;
		StringBuffer insQry 			= new StringBuffer();
		int result 						= -1;
		int dataArrayStart 				= 0;

		try
		{
			int recordCount = 0;
			insQry = new StringBuffer();
			insQry.append(" INSERT INTO BankBranchTemp 	");
			insQry.append(" (								");
			insQry.append(" 	ifscCode, 			"); 
			insQry.append(" 	bankId, 			"); 
			insQry.append(" 	micrBranchCode,		"); 
			insQry.append(" 	trngFlag, 			"); 
			insQry.append(" 	bankName, 			"); 
			insQry.append(" 	branchName, 		"); 
			insQry.append(" 	city, 				"); 
			insQry.append(" 	branchAddress,		"); 
			insQry.append(" 	micrCode, 			"); 
			insQry.append(" 	district, 			"); 
			insQry.append(" 	state, 				"); 
			insQry.append(" 	lastUpdDate, 		");
			insQry.append(" 	stateCode, 			");
			insQry.append(" 	cityCode, 			");
			insQry.append(" 	countryCode,		");
			insQry.append(" 	contact				");
			insQry.append("	 ) 						");
			insQry.append("	 VALUES 			    ");
			insQry.append("	 (			    		");
			insQry.append("	 	?, ?, ?, ?, ?, ?,	");
			insQry.append("	 	?, ?, ?, ?, ?, ?,   ");
			insQry.append("	 	?, ?, ?, ?		    ");
			insQry.append("	 )						");

			String insertQuery = insQry.toString();

			for (int x = dataArrayStart; x < ifscData.size(); x++)
			{
				ArrayList<String> oneRecord = ifscData.get(x);
				
				String ifscCode 	= (String)oneRecord.get(0);
				
				params = new ArrayList<Object>();
				paramTypes = new ArrayList<Integer>();

				params.add(ifscCode);// IDI_IFSC_CODE
				paramTypes.add(0);
				params.add((String) oneRecord.get(1));// IDI_BANK_CODE
				paramTypes.add(0);
				params.add((String) oneRecord.get(2));// IDI_BRANCH_CODE
				paramTypes.add(0);
				params.add((String) oneRecord.get(3));// IDI_TRNG_FLG
				paramTypes.add(0);
				params.add((String) oneRecord.get(4));// IDI_BANK_NAME
				paramTypes.add(0);
				params.add((String) oneRecord.get(5));// IDI_BRANCH_NAME
				paramTypes.add(0);
				params.add((String) oneRecord.get(6));// IDI_CITY_NAME
				paramTypes.add(0);
				params.add((String) oneRecord.get(7));// IDI_ADDRS
				paramTypes.add(0);
				params.add((String) oneRecord.get(8));// IDI_MICR_CODE
				paramTypes.add(0);
				params.add((String) oneRecord.get(9));// IDI_DISTRICT
				paramTypes.add(0);
				params.add((String) oneRecord.get(10));// IDI_STATE
				paramTypes.add(0);
				params.add((String) oneRecord.get(11));// DM_LSTUPDDT
				paramTypes.add(0);
				params.add((String) oneRecord.get(10));// stateCode
				paramTypes.add(0);
				params.add((String) oneRecord.get(6));// cityCode
				paramTypes.add(0);
				params.add("IN");					// countryCode
				paramTypes.add(0);
				params.add("");						 // contact
				paramTypes.add(0);

				paramsList.add(params);
				paramTypesList.add(paramTypes);
			}

			repository.executeBatchForSingleQuery(insertQuery, paramsList, paramTypesList, groupId, 10000);
		}
		catch (Throwable t)
		{
			Logger.log("Error in outer addIFSCData", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
			t.printStackTrace();
			result = -1;
		}
		return result;
	}

	//
	public int addBankData(ArrayList<String> rejectData, String groupId)
	{
		ArrayList quries = new ArrayList();
		ArrayList<ArrayList<Object>> paramsList = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<Integer>> paramTypesList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Object> params = null;
		ArrayList<Integer> paramTypes = null;
		StringBuffer query = new StringBuffer();
		int result = -1;
		int dataArrayStart = 0;

		try
		{

			for (int x = dataArrayStart; x < rejectData.size(); x = x + 6)
			{
				query = new StringBuffer();
				params = new ArrayList<Object>();
				paramTypes = new ArrayList<Integer>();

				query.append(" INSERT INTO BANK ( ");
				query.append(" 		BANKID, ");// 0
				query.append(" 		BANKNAME, ");// 1
				query.append(" 		IMPSFLAG, ");// 2
				query.append(" 		ISFIRFLAG, ");// 3
				query.append(" 		ISACTIVE ");// 4
				query.append(" ) ");
				query.append(" VALUES (?,?,?,?,?) ");

				params.add((String) rejectData.get(x));// BANKID
				paramTypes.add(0);
				params.add((String) rejectData.get(x + 1));// BANKNAME
				paramTypes.add(0);
				params.add((String) rejectData.get(x + 2));// IMPSFLAG
				paramTypes.add(0);
				params.add((String) rejectData.get(x + 3));// ISFIRFLAG
				paramTypes.add(0);
				params.add((String) rejectData.get(x + 4));// ISACTIVE
				paramTypes.add(0);
				params.add((String) rejectData.get(x + 5));// ISSAMEBANK
				paramTypes.add(0);

				quries.add(query.toString());

				paramsList.add(params);
				paramTypesList.add(paramTypes);
			}

			try
			{

				result = repository.executeBatch(quries, paramsList, paramTypesList,
						groupId);
			}
			catch (Throwable t)
			{
				Logger.log("Error in inner addBankData", "ifscBulkUpload.java",
						"ifscBulkUpload", t, Logger.CRITICAL);
				result = -1;
			}
		}
		catch (Throwable t)
		{
			Logger.log("Error in outer addBankData", "ifscBulkUpload.java",
					"ifscBulkUpload", t, Logger.CRITICAL);
			result = -1;
		}
		return result;
	}

	public ArrayList updateTable2(ArrayList<ArrayList<String>> fileData, String tableName, String groupId, String fileName, String createdBy)
	{
		System.out.println("In updateTable2 ");
		ArrayList insertQueryData = new ArrayList();
		ArrayList result = new ArrayList();

		HashMap<String, ArrayList<String>> bankMap = new HashMap<String, ArrayList<String>>();

		ArrayList bankData = new ArrayList();

		try
		{
			for (ArrayList<String> record : fileData)
			{
				insertQueryData.add(record.get(0));// 0
				insertQueryData.add(record.get(1));// 1
				insertQueryData.add(record.get(2));// 2
				insertQueryData.add(record.get(3));// 3
				insertQueryData.add(record.get(4));// 4
				insertQueryData.add(record.get(5));// 5
				insertQueryData.add(record.get(6));// 6
				insertQueryData.add(record.get(7));// 7
				insertQueryData.add(record.get(8));// 8
				insertQueryData.add(record.get(9));// 9
				insertQueryData.add(record.get(10));// 10
				insertQueryData.add(record.get(11));// 11
				insertCounter++;
			}

			System.out.println("********************************** Start - " + new Date());

			int finalResult = -1;
			if (insertQueryData.size() > 0)
			{
				// truncate BankBranchTemp
				String truncateIfscMaster = "truncate table BankBranchTemp";
				int res = manualExecuteQueryArgs(truncateIfscMaster, groupId);
				Logger.log("Truncate BankBranchTemp Table: " + res, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);

				// insert into BankBranchTemp
				finalResult = excecuteIfscMaintBatchQueriesInsertData(fileData, groupId, fileName, createdBy);
				Logger.log("IFSC Insert Into Maint Completed - " + finalResult, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);

				// truncate BankBranch
				truncateIfscMaster = "truncate table BankBranch";
				res = manualExecuteQueryArgs(truncateIfscMaster, groupId);
				Logger.log("Truncate IfscMaster Table: " + res, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);

				// insert into BankBranch
				res = excecuteIfscBatchQueriesInsertData(groupId);
				Logger.log("IFSC Insert Completed: " + res, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
				
				updateBankMasterData();
				updateGroupBankMasterData();
				updateStateMasterData();
				updateCityMasterData();
				
				Logger.log("All other Master Updated", "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			}

			System.out.println("********************************** End - " + new Date());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public int manualExecuteQueryArgs(String query, String groupId)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer insQry = new StringBuffer();
		int updateResult = -1;

		try
		{
			insQry.append(query);

			try
			{
				updateResult = repository.executeQuery(insQry, params, paramTypes, groupId);
			}
			catch (Throwable t)
			{
				Logger.log("Error in updateReportData ...", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
				updateResult = -1;
			}
		}
		catch (Throwable t)
		{
			Logger.log("Error in updateReportData ....", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
			updateResult = -1;
		}
		finally
		{
			params = null;
			paramTypes = null;
			insQry = null;
		}
		return updateResult;
	}

	public void executeIfscQueries(String completePath, String tableName, String status, boolean isFileValid,String fileName, String createdBy) throws Throwable
	{
		try
		{
			errorData = new ArrayList<ArrayList<String>>();

			ArrayList<ArrayList<String>> fileData = getIFSCDataFromFile(completePath);
			Logger.log("File read and data loaded in lists : " + fileData.size(), "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			System.out.println("isFileValid :" + isFileValid + "  fileData.size()--" + fileData.size());

			if(isFileValid)
			{
				updateTable2(fileData, tableName, groupId, fileName, createdBy);
			}
		}
		catch (Throwable e)
		{
			System.out.println("fileData.size()--In cathch");
			e.printStackTrace();
			Logger.log("Error Occurred:::" + e, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
		}
	}

	public void updateBankMasterData()
	{
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer insQry = new StringBuffer();		
		
		insQry.append(" INSERT INTO	Bank (						");
		insQry.append("		bankId,								");  
		insQry.append("		bankName,							");  
		insQry.append("		branchName,							");  
		insQry.append("		bankMICR,							");  
		insQry.append("		registrationDate,					");  
		insQry.append("		bankcontactName,					");  
		insQry.append("		designation,						");  
		insQry.append("		emailOfficial,						");  
		insQry.append("		emailPersonal,						"); 
		insQry.append("		bankAddress,						");  
		insQry.append("		telNo,								");                      
		insQry.append("		mobile,								"); 
		insQry.append("		fax	)								");
		insQry.append(" SELECT DISTINCT							");
		insQry.append("		bankId,								");  
		insQry.append("		bankName,							");  
		insQry.append("		'MUMBAI (DUMMY)',					");  
		insQry.append("		'100000001',						");  
		insQry.append("		GETDATE(),							");  
		insQry.append("		'Mr.Smith',							");  
		insQry.append("		'Branch Head',						");  
		insQry.append("		'test@test.com',					");  
		insQry.append("		'test@test.com',					"); 
		insQry.append("		'MUMBAI (DUMMY)',					");  
		insQry.append("		'1111111',							");                      
		insQry.append("		'2222222',							"); 
		insQry.append("		'3333333'							");
		insQry.append("		FROM BankBranch						");
		insQry.append(" WHERE BANKID NOT IN (SELECT BANKID FROM BANK)					");
	
		try
		{
			repository.executeQuery(insQry, params, paramTypes, groupId);
		}
		catch (Throwable t)
		{
			Logger.log("Error in updateBankMasterData ...", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
		}	
	}
	
	public void updateGroupBankMasterData()
	{
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer insQry = new StringBuffer();		
		
		insQry.append(" INSERT INTO GroupBanks  (					");
		insQry.append("		groupId,								");//1 groupId
		insQry.append("		countryCode,							"); //2 countryCode
		insQry.append("		currencyCode,							");//3 currencyCode
		insQry.append("		bankId,									");//4 bankId 
		insQry.append("		bankCode,								");//5 bankCode
		insQry.append("		bankType,								");//6 bankType
		insQry.append("		isSponserBank,							");//7isSponserBank
		insQry.append("		receiveModeCode,						");//8 receiveModeCode
		insQry.append("		isSameBank,								");//9 isSameBank
		insQry.append("		bankcontactName,						"); //10 bankcontactName
		insQry.append("		designation,					        ");//11 designation
		insQry.append("		emailOfficial,						    ");//12emailOfficial
		insQry.append("		emailPersonal,							");//13 emailPersonal
		insQry.append("		bankAddress,							");//14 bankAddress
		insQry.append("		telNo,									");//15 telNo
		insQry.append("		mobile,									");//16 mobile
		insQry.append("		fax,									");//17 fax
		insQry.append("		accountHolder,							");//18 accountHolder
		insQry.append("		routingNumber,							");//19 routingNumber
		insQry.append("		accountNo,								");//20 accountNo
		insQry.append("		accountType,							");//21 accountType
		insQry.append("		isActive,								");//22 isACtive
		insQry.append("		createdBy,								");//23createdBy        
		insQry.append("		createdDate,							");//24 createdDate
		insQry.append("		uniqueFieldId1,							");//25 uniqueFieldId1
		insQry.append("		uniqueFieldId2,							");//26 uniqueFieldId2
		insQry.append("		uniqueFieldId3 )						");//27 uniqueFieldId2
		insQry.append(" SELECT DISTINCT							");
		insQry.append("		'IDFCIN',							");  
		insQry.append("		countryCode,						");
		insQry.append("		'INR',								");
		insQry.append("		bankId,								");  
		insQry.append("		bankId,								");  
		insQry.append("		'D',								");  
		insQry.append("		'Y',								");  
		insQry.append("		'DC',								");  
		insQry.append("		'Y',								");  
		insQry.append("		'test',								");  
		insQry.append("		'Branch Head',						"); 
		insQry.append("		'test@test.com',					"); 
		insQry.append("		'test@test.com',					"); 
		insQry.append("		'MUMBAI (DUMMY)',					");  
		insQry.append("		'1111111',							");                      
		insQry.append("		'2222222',							"); 
		insQry.append("		'3333333',							");
		insQry.append("		'test',								");
		insQry.append("		'143245678',						");
		insQry.append("		'143245678',						");
		insQry.append("		'C',								");
		insQry.append("		'Y',								");
		insQry.append("		'SystemUpload',						");
		insQry.append("		getDate(),							");
		insQry.append("		'',									");
		insQry.append("		'',									");
		insQry.append("		''									");
		insQry.append("		FROM BankBranch						");
		insQry.append(" WHERE BANKID NOT IN (SELECT BANKID FROM GroupBanks)					");
	
		try
		{
			repository.executeQuery(insQry, params, paramTypes, groupId);
		}
		catch (Throwable t)
		{
			Logger.log("Error in updateGroupBankMasterData ...", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
		}	
	}
	
	public void updateStateMasterData()
	{
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer insQry = new StringBuffer();		
		
		insQry.append(" INSERT INTO	State (						");
		insQry.append("		stateCode,							");
		insQry.append("		countryCode,						");
		insQry.append("		state,								");
		insQry.append("		isActive	)						");
		insQry.append(" SELECT DISTINCT							");
		insQry.append("		stateCode,							");  
		insQry.append("		countryCode,						");  
		insQry.append("		state,								");  
		insQry.append("		'Y'									");  
		insQry.append("		FROM BankBranch						");
		insQry.append(" WHERE stateCode NOT IN (SELECT stateCode FROM State)					");
	
		try
		{
			repository.executeQuery(insQry, params, paramTypes, groupId);
		}
		catch (Throwable t)
		{
			Logger.log("Error in updateStateMasterData ...", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
		}	
	}
	
	public void updateCityMasterData()
	{
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer insQry = new StringBuffer();		
		
		insQry.append(" INSERT INTO City(			");
		insQry.append("		cityCode,				"); //0
		insQry.append("		countryCode,			"); //1
		insQry.append("		state,					"); //2
		insQry.append("		stateCode,				"); //3
		insQry.append("		city,					"); //4
		insQry.append("		isActive )				");
		insQry.append(" SELECT DISTINCT				");
		insQry.append("		cityCode,				");  
		insQry.append("		countryCode,			");  
		insQry.append("		state,					");  
		insQry.append("		stateCode,				");  
		insQry.append("		city,					");  
		insQry.append("		'Y'						");  
		insQry.append("		FROM BankBranch			");
		insQry.append(" WHERE cityCode NOT IN (SELECT cityCode FROM city)	");
	
		try
		{
			repository.executeQuery(insQry, params, paramTypes, groupId);
		}
		catch (Throwable t)
		{
			Logger.log("Error in updateCityMasterData ...", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
		}	
	}
	
	public int updateReportData(ArrayList<String> reportData, String fileId)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer insQry = new StringBuffer();
		int updateResult = -1;
		String remarks = reportData.get(5);

		remarks = remarks.replace("[", "");
		remarks = remarks.replace("]", "");
		remarks = remarks.replace(",", "~");

		try
		{
			insQry.append(" UPDATE ");
			insQry.append(" 	IFSC_BULK_DET SET ");
			insQry.append(" 	STATUS = ?, ");
			insQry.append(" 	REMARK = ?, ");
			insQry.append(" 	PROCESSENDTIME = ? ");
			insQry.append(" WHERE upper(FILE_ID) = ? ");
			insQry.append(" and FILE_NAME = ? ");

			params.add(reportData.get(4));
			paramTypes.add(0);
			params.add(remarks);
			paramTypes.add(0);
			params.add(getISTTime());
			paramTypes.add(0);
			params.add(fileId.toUpperCase());
			paramTypes.add(0);
			params.add(reportData.get(1));
			paramTypes.add(0);

			Logger.log("updateReportData ... insQry :: "+insQry+"\n params :: "+params.toString(), "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			try
			{
				updateResult = repository.executeQuery(insQry, params, paramTypes, groupId);
				Logger.log("updateReportData ... updateResult :: "+updateResult, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			}
			catch (Throwable t)
			{
				Logger.log("Error in updateReportData ...", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
				updateResult = -1;
			}
		}
		catch (Throwable t)
		{
			Logger.log("Error in updateReportData ....", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
			updateResult = -1;
		}
		finally
		{
			params 		= null;
			paramTypes 	= null;
			insQry 		= null;
		}
		return updateResult;
	}
	
	public int addReportData(ArrayList<String> reportData, String fileId)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer insQry = new StringBuffer();
		int insertResult = -1;

		try
		{
			insQry.append(" INSERT INTO IFSC_BULK_DET	  (");
			insQry.append("		FILE_ID,					");
			insQry.append("		UPLOAD_TYPE,				");
			insQry.append("		FILE_NAME,					");
			insQry.append("		STATUS,						");
			insQry.append("		PROCESSSTARTTIME,			");
			insQry.append("		CREATEDBY,					");
			insQry.append("		CREATEDDATE	) VALUES (		");
			insQry.append("		?, 		 					");
			insQry.append("		?, 		 					");
			insQry.append("		?, 		 					");
			insQry.append("		?, 		 					");
			insQry.append("		?, 		 					");
			insQry.append("		?, 		 					");
			insQry.append("		CURRENT_TIMESTAMP) 		 	");

			params.add(fileId.toUpperCase());
			paramTypes.add(0);
			params.add(reportData.get(3));
			paramTypes.add(0);
			params.add(reportData.get(1));
			paramTypes.add(0);
			params.add("PROCESSING");
			paramTypes.add(0);
			params.add(daemonStartTime);
			paramTypes.add(0);
			params.add(reportData.get(2));
			paramTypes.add(0);

			Logger.log("addReportData : insQry :: " + insQry.toString() + "\n params: " + params.toString(), "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			
			try
			{
				insertResult = repository.executeQuery(insQry, params, paramTypes, groupId);
				Logger.log("addReportData : insertResult : " + insertResult, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			}
			catch (Throwable t)
			{
				Logger.log("Error in addReportData ...", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
				insertResult = -1;
			}
		}
		catch (Throwable t)
		{
			Logger.log("Error in addReportData ....", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
			insertResult = -1;
		}
		finally
		{
			params 		= null;
			paramTypes 	= null;
			insQry 		= null;
		}
		return insertResult;
	}

	public String insertIFSCData(ArrayList<String> fileDtls)
	{
		tableName = "BankBranch";

		String filepath 	= fileDtls.get(0) == null ? "" : fileDtls.get(0);
		String fileName 	= fileDtls.get(1) == null ? "" : fileDtls.get(1);
		String loginId 		= fileDtls.get(2) == null ? "" : fileDtls.get(2);
		String completePath = filepath + fileName;
		
		String tmpFileName 	= fileName.toLowerCase();
		File upFile 		= new File(completePath);
		String fileId		= getFileID(fileName);
		
		fileDtls.add("BULK");// 3

		if ((tmpFileName.endsWith(".xlsx") || tmpFileName.endsWith(".xls")) && upFile.exists())
		{
			Logger.log("completePath:"+completePath, "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
			try
			{
				try
				{
					addReportData(fileDtls, fileId);
				}
				catch (Exception e)
				{
					Logger.log("Error in Update insertFileData", "ifscBulkUpload.java", "ifscBulkUpload", null, Logger.INFO);
				}
				
				try 
				{
					executeIfscQueries(completePath, tableName, status, isFileValid, fileName, loginId);
				} 
				catch (Throwable t) 
				{
					Logger.log("Error in executeIfscQueries", "ifscBulkUpload.java", "ifscBulkUpload", t, Logger.CRITICAL);
				}

				try
				{
					int updateReportData = -1;

					ArrayList<String> result = new ArrayList<String>();
					System.out.println("insertCounter :" + insertCounter);
					System.out.println("failRecords :" + failRecords);
					if (insertCounter > 0 && failRecords == 0)
					{
						status = "SUCCESS";
						result.add("Out of total " + totalCounter + " records: " + insertCounter
								+ " added, " + updateCounter + " updated, " + failRecords + " failed");
						errorData.add(result);
					}
					else if (insertCounter > 0 && failRecords != 0)
					{
						status = "PARTIALY PROCESSED";
						result.add("Out of total " + totalCounter + " records: " + insertCounter
								+ " added, " + updateCounter + " updated, " + failRecords + " failed");
						errorData.add(result);
					}
					else
						status = "FAIL";
					
					fileDtls.add(status);//4
					fileDtls.add(errorData.toString());//5

					updateReportData = updateReportData(fileDtls, fileId);
					
				}
				catch (Exception t)
				{
					Logger.log("Error in addReportData", "ifscBulkUpload.java",
							"ifscBulkUpload", t, Logger.CRITICAL);
				}

			}
			catch (Exception e)
			{
				Logger.log("Error in allinsQry...", "ifscBulkUpload.java",
						"ifscBulkUpload", e, Logger.CRITICAL);
			}
		}

		return status;
	}

	/*public static void main(String[] args) throws Throwable
	{
		// IDI_IFSC_CODE,IDI_BANK_CODE,IDI_BRANCH_CODE,IDI_TRNG_FLG,IDI_BANK_NAME,IDI_BRANCH_NAME,IDI_CITY_NAME,IDI_ADDRS,IDI_MICR_CODE,IDI_DISTRICT,IDI_STATE,DM_LSTUPDDT
		(new ifscBulkUpload()).insertIFSCData("F:\\IDFC_IFSC\\IFSCMasterFormat.xlsx");
	}*/

}
