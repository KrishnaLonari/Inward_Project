package com.api.remitGuru.component.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.api.remitGuru.web.controller.FileTransferController;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.api.remitGuru.web.controller.SQLRepositoryController;

public class BSBDeamon 
{
	SQLRepositoryController repository = new SQLRepositoryController();
	static String errMsg = "";
	
	public String bsbProcess(String selectedGroupId,String sendCountry, String sendCurrency, String receiveCountry, String receiveCurrency, String txnStatus, String fileType)
	{
		String  receiveMode = "", fileName = "" , fsize = "", serviceProvider = "", spId = "",directory = "" ;
		System.out.println(selectedGroupId);
		String msg = "";
		
		java.text.SimpleDateFormat sdfWTime = new java.text.SimpleDateFormat("ddMMyyyyHHmm");
		String todayDateWTime = sdfWTime.format(new java.util.Date());
		fileName = "bsbStatusReport"+todayDateWTime+".csv";
		Logger.log("fileName: "+fileName, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		
		FileTransferController fileTransferCntrl = new FileTransferController();
		ArrayList txnInfo = new ArrayList();
		ArrayList txnData = new ArrayList();
		
		txnInfo.add(selectedGroupId);	//Group Id
		txnInfo.add(sendCountry); //Send Country
		txnInfo.add(sendCurrency); //Send Currency
		txnInfo.add(receiveCountry); //Receive Country
		txnInfo.add(receiveCurrency); //Receive Currency
		txnInfo.add(receiveMode); //Receive Mode Code
		txnInfo.add(txnStatus); //Txn Status	
		
		//to get the list of RGTN to be processed in the time slot
		txnData = getTxnAmtArrivedMakerRecords(txnInfo);
		
		if(txnData != null && txnData.size() > 0)
		{
			msg = "TransactionsFound";
			Logger.log("number of transactions found to be processed : "+txnData.size(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			ArrayList accData = new ArrayList();
			String rgtnAmtList = "";
			for(int i = 0; i < txnData.size(); i++)
			{
				String temp[] = (String [])txnData.get(i);
				String rgtn   = temp[0] == null ? "" : temp[0];
				String amount = temp[2] == null ? "" : temp[2];
				String bookingDate = temp[3] == null ? "" : temp[3];
				rgtnAmtList = rgtnAmtList + (rgtn+"~"+amount+"~"+bookingDate+",");
				accData.add(rgtn);
				accData.add(amount);
			}
			Logger.log("rgtnAmtList : "+rgtnAmtList+" \n accData size : "+accData.size(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			
			if(accData != null && accData.size() > 0)
			{
				String transferFile	= "";
				
				//to generate sr no
				int resultOfProcess = insertVerificationFileChecker(accData, selectedGroupId, sendCountry, sendCurrency);
				Logger.log("resultOfProcess (SR NO): "+resultOfProcess, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				
				if(resultOfProcess == -9)
				{
					msg = "SrNo_Failed : Duplicate";
					errMsg = "Error :Serial Number Creation Failed(Duplicate).";
					Logger.log("resultOfProcess duplicate: "+resultOfProcess, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				}
				else if(resultOfProcess == -1)
				{
					msg = "SrNo_Failed : InsertFailed";
					errMsg = "Error :Serial Number Creation Failed(Insert).";
					Logger.log("resultOfProcess failed: "+resultOfProcess, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				}
				else
				{
					Logger.log("SR No created : "+resultOfProcess, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
					msg = "SrNo_Created";
					
					ArrayList fileGenerateInfo = new ArrayList();
					fileGenerateInfo.add(selectedGroupId); //groupId
					fileGenerateInfo.add(sendCountry);//countryCode
					fileGenerateInfo.add(sendCurrency);//currencyCode
					fileGenerateInfo.add(String.valueOf(resultOfProcess));//srNo
					fileGenerateInfo.add("T");//fileType
					fileGenerateInfo.add(""); //transferType not required
					fileGenerateInfo.add(""); //bankId
					fileGenerateInfo.add(""); //rgtn not required
					
					//generate BSB file
					transferFile = downloadFile(fileGenerateInfo);
					Logger.log("bsbFile content : \n"+transferFile, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
					
					if(!transferFile.equals(""))
					{
						msg = "File_Content : Success";
						boolean fileCreateStatus = false;
						FileWriter fw = null;
						try
						{
							Logger.log("file creation started", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
							fw = new FileWriter(EnvProperties.getValue("ADMIN_DOWNLOAD_PATH") + fileName);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(transferFile);
							bw.close();
							fileCreateStatus = true;
							Logger.log("file creation ended", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
							
							//to get the file size
							try
							{
								File fileSize = new File(EnvProperties.getValue("ADMIN_DOWNLOAD_PATH") + fileName);
								if(fileSize.exists())
								{
									fsize = String.valueOf(fileSize.length());
								}
							}
							catch(Exception e)
							{
								Logger.log("Error in getting file size  : ", "BSBDeamon.java", "BSBDeamon", e, Logger.CRITICAL);
							}
							Logger.log("file size after creation of file : "+fsize, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
						}
						catch(Exception e)
						{
							Logger.log("Error in bsbDeamon : fileName : "+fileName, "BSBDeamon.java", "BSBDeamon", e, Logger.CRITICAL);
						}
						finally
						{
							if(fw != null)
							{
								try
								{
									fw.close();//prout.close();
								}
								catch(Throwable t)
								{
									Logger.log("Error while closing file : "+t, "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
								}
							}
							fw = null;//prout = null;
						}
						Logger.log("file creation status : "+fileCreateStatus, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
						
						if(fileCreateStatus == true)
						{
							msg = "BSB_File_Write : Success";
							ArrayList ftpFileData = new ArrayList();
							ftpFileData.add(selectedGroupId);//groupId
							ftpFileData.add(String.valueOf(resultOfProcess));//srNo
							ftpFileData.add(fileName);//fileName
							ftpFileData.add("P");
							ftpFileData.add("U");
							ftpFileData.add("T");
							ftpFileData.add(sendCountry);//countryCode
							ftpFileData.add(sendCurrency);//currencyCode
							ftpFileData.add("SYSTEM");//user_info.getLoginId().trim()
							if(selectedGroupId.equals("YR") && sendCountry.equals("AU"))
								ftpFileData.add("Westpac");
							else if(selectedGroupId.equals("RXM") && sendCountry.equals("AU"))
								ftpFileData.add("Westpac");
							else
								ftpFileData.add("PayLynx");
							ftpFileData.add(fsize);
							
							//to get ftpfileid for upload process
							int ftpInserResult = -1;
							ftpInserResult = insertFTPFileDetails(ftpFileData);
							Logger.log("ftp table insert result() (ftpfileid): "+ftpInserResult, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
							
							if(ftpInserResult != -1)
							{
								msg = "Ftp_record_inserted : Success";
								String adminDownloadPath = EnvProperties.getValue("ADMIN_DOWNLOAD_PATH");
								Logger.log("Folder path  : "+adminDownloadPath, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
								File in = new File(adminDownloadPath + fileName);
								if(in.exists())
								{
									ArrayList ftpSpData = new ArrayList();
									
									ftpSpData.add(selectedGroupId);//strGroupId
									ftpSpData.add(sendCountry);//countryCode
									ftpSpData.add(sendCurrency);//currencyCode
									ftpSpData.add("T");//fileType

									if(selectedGroupId.equals("YR") && sendCountry.equals("AU"))
										serviceProvider="Westpac"; 
									if(selectedGroupId.equals("RXM") && sendCountry.equals("AU"))
										serviceProvider="Westpac"; 
									//else
										//serviceProvider="PayLynx";

									ftpSpData.add(serviceProvider);
									ftpSpData.add("U");
									ArrayList ftpSpDbData = new ArrayList();
									try
									{
										ftpSpDbData = fileTransferCntrl.getFTPServiceProviderData(ftpSpData);
									}
									catch(Throwable t)
									{
										Logger.log("ERROR in bsbFileListAction while getFTPServiceProviderData List ", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
									}
									
									if(ftpSpDbData != null && ftpSpDbData.size() > 0)
									{
										String[] temp = (String [])ftpSpDbData.get(0);
										spId = temp[0] == null ? "" : temp[0];
										directory = temp[12] == null ? "" : temp[12];
									}
									Logger.log("spId : "+spId+" directory : "+directory, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
									
									//file encryption started
									String encryptOutput = "", encryptDir = "" ,encryptFileSize="",uploadOutput="",uploadFileSize="0",uploadDir="";
									int encryptResult = -1,uploadResult = -1;
									
									String encryptedFileName = createFIleName(fileName, "_Enc.csv");;
									String encryptedFilePath = EnvProperties.getValue(selectedGroupId+"_"+serviceProvider+"_ADMIN_FTP_UPLOAD_PATH")+"IN/";
									
									Logger.log("Orginal FileName : "+fileName+" \n Encrypted file name "+encryptedFileName +" \n Encrypted file path : "+encryptedFilePath, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
									try 
									{
										encryptOutput = encrypt(adminDownloadPath+fileName,encryptedFilePath+encryptedFileName,serviceProvider,selectedGroupId);
									}
									catch(Throwable t)
									{
										Logger.log("Exception in encryption." , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);		
									}
									Logger.log("Encryption output : "+encryptOutput , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
									
									if(encryptOutput.contains("~"))
									{
										try
										{
											encryptResult = Integer.parseInt(encryptOutput.split("~")[0]);
										}
										catch(Throwable t)
										{
											Logger.log("Error in parsing return result as integer." , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
										}
										encryptDir = encryptOutput.split("~")[1];
										encryptFileSize = encryptOutput.split("~")[2];
									}
									Logger.log("encryptResult : "+encryptResult , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
									//file encrpytion ended
									
									if(encryptResult <= 0)
									{
										msg = "BSb File encryption : Failed";
										errMsg = "Error :BSB file encryption failed.";
										Logger.log("Error in encryption." + adminDownloadPath + fileName , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);	
										ArrayList ftpFileDtls = new ArrayList();
										
										try
										{
											//int updateResult = fileTransferCntrl.updateFTPFileDetailsStatus(ftpFileId,"EF",user_info.getLoginId(),user_info.getGroupId());
											int updateResult = fileTransferCntrl.updateFTPFileDetailsStatus(ftpInserResult,"EF","SYSTEM",selectedGroupId);
										}
										catch(Throwable t)
										{
											Logger.log("Exception in updateFTPFileDetailsStatus while setting status as EF : Encryption Failed." , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
										}
									}
									else
									{
										msg = "BSb File encryption : Success";
										Logger.log("File encryption completed and file upload started" , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
										
										ArrayList ftpFileUpdateData = new ArrayList();
										ftpFileUpdateData.add(selectedGroupId);
										ftpFileUpdateData.add("SYSTEM");//user_info.getLoginId()
										ftpFileUpdateData.add(String.valueOf(ftpInserResult));//ftpFileId
										ftpFileUpdateData.add(encryptedFileName);
										ftpFileUpdateData.add(encryptedFilePath);

										ArrayList ftpFileUpdateData1 = new ArrayList();
										ftpFileUpdateData1.add(selectedGroupId);
										ftpFileUpdateData1.add("SYSTEM");//user_info.getLoginId()
										ftpFileUpdateData1.add(String.valueOf(ftpInserResult));//ftpFileId
										ftpFileUpdateData1.add(encryptDir);// path where file is encrypted
										ftpFileUpdateData1.add(encryptFileSize);
										
										try
										{
											//section commented as it does the task of updating encryption status and encrypted file path,dir,size ---> to Encryption success
											//int updateResult = fileTransferCntrl.updateFTPFileDetailsStatus(result,"ES",user_info.getLoginId(),user_info.getGroupId());
											int updateResult = fileTransferCntrl.updateFTPFileDetailsStatus(ftpInserResult,"ES","SYSTEM",selectedGroupId);
											updateResult = fileTransferCntrl.updateFTPFileDetailsFileName(ftpFileUpdateData);
											updateResult = fileTransferCntrl.updateFTPUploadedFilePath(ftpFileUpdateData1);//fileTransferCntrl		
										}
										catch(Throwable t)
										{
											Logger.log("Exception in updateFTPFileDetailsStatus while setting status as ES : Encryption Success." , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
											//msg = "fail";
										}
										Logger.log("Encrypted fileName : "+encryptedFileName +" after updating encrpytion status in table ES", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
										
										//to upload encrypted file to SFTP server starts
										
										uploadOutput = uploadSFTP(encryptedFileName,selectedGroupId, sendCountry, sendCurrency, serviceProvider, "T", directory);
										
										Logger.log("uploadOutput "+uploadOutput , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
										
										if(uploadOutput.contains("~"))
										{
											try
											{
												uploadResult = Integer.parseInt(uploadOutput.split("~")[0]);
											}
											catch(Throwable t)
											{
												Logger.log("Error in parsing return result as integer." , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
											}
											uploadDir = uploadOutput.split("~")[1];
											uploadFileSize = uploadOutput.split("~")[2];
										}
										Logger.log("uploadOutput : "+uploadOutput+" and uploadResult : "+uploadResult+" and uploadDir : "+uploadDir+" and uploadFileSize : "+uploadFileSize , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);	
										
										if(uploadResult <= 0)
										{
											msg = "FileUpload :Failed";
											errMsg = "Error :BSB file upload failed.";
										}
										else
										{
											msg = "FileUpload :Success";
										}
										
										ftpFileUpdateData = new ArrayList();
										ftpFileUpdateData.add(selectedGroupId);
										ftpFileUpdateData.add("SYSTEM");//user_info.getLoginId()
										ftpFileUpdateData.add(String.valueOf(ftpInserResult));//ftpFileId
										ftpFileUpdateData.add(uploadDir);// path where file is uploaded
										ftpFileUpdateData.add(uploadFileSize);//size of file uploaded
										
										try
										{
											//int updateResult = fileTransferCntrl.updateFTPFileDetailsStatus(result,uploadResult <= 0 ? "UF" : "US",user_info.getLoginId(),user_info.getGroupId());
											int updateResult = fileTransferCntrl.updateFTPFileDetailsStatus(ftpInserResult,uploadResult <= 0 ? "UF" : "US","SYSTEM",selectedGroupId);
											Logger.log("updateResult for US/UF : "+updateResult , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
											//if(updateResult < 0)
											if(updateResult > 0)
											{
												Logger.log("updating file path as upload is done successfully : " , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);	
												updateResult = fileTransferCntrl.updateFTPUploadedFilePath(ftpFileUpdateData);
											}
											Logger.log("last step updateResult : "+updateResult , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);	
										}
										catch(Throwable t)
										{
											Logger.log("Exception in updateFTPFileDetailsStatus." , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
											//msg = "fail";
										}
										
									}
								}
								else
								{
									msg = "BSB_FIle : Not found while enc and upload";
									errMsg = "Error :BSB file not found.";
									Logger.log("BSB file not found" , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
								}
							}
							else
							{
								msg = "Ftp_record_inserted : Failed";
								errMsg = "Error :While inserting data in FTP table.";
								Logger.log("some error occured while inserting data into ftp table : "+ftpInserResult, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
							}
							
							
						}
						else
						{
							msg = "BSB_File_Write : Failed";
							errMsg = "Error :While creating file.";
						}
						
					}
					else
					{
						Logger.log("some error occured while formulating bsbFile content : \n"+transferFile, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
						msg = "File_Content : Failed";
						errMsg = "Error :While generating file content.";
					}
				}
				
			}
			else
			{
				msg = "RgtnListError";
				errMsg = "Error :While forming RGTN list.";
				Logger.log("some problem occuered while creating RGTN list", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			}
			
		}
		else
		{
			Logger.log("No transactions found to be processed", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			msg = "ZeroTransactions";
			errMsg = "Error :No Transactions Found.";
		}
		
		return msg;
	}
	
	//to fetch RGTN list from
	public ArrayList getTxnAmtArrivedMakerRecords(ArrayList txnInfo)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query 				= new StringBuffer();
		ArrayList result 				= new ArrayList();		
		
		query.append(" SELECT									");
		query.append(" 		a.rgtn,								");//0
		query.append(" 		a.sendGroupId,						");//1
		query.append(" 		a.sendAmount, 						");//2
		query.append(" 		a.bookingDate, 						");//3
		query.append(" 		i.processcnt	 					");//4
		query.append(" FROM 									");
		query.append(" 		Transactions a						");
		query.append(" LEFT JOIN (select count(transferNo) processcnt, transferNo from  TransferFileGenerationDetails group by transferNo)  i ON a.rgtn = i.transferNo  , ");
		query.append(" 		TransactionStatusDetails b, 		");
		query.append(" 		Sender d,							");
		query.append(" 		Status e,							");
		query.append(" 		UserMaster h						");
		query.append(" WHERE 									");
		query.append(" 		a.rgtn = b.rgtn 					");
		query.append(" AND	a.sendGroupId = d.groupId			");
		query.append(" AND	a.sendUserId = d.userId				");
		query.append(" AND	a.sendLoginId  = d.loginId			");
		query.append(" AND  a.sendGroupId = h.groupId			");
		query.append(" AND	a.sendUserId = h.userId				");
		query.append(" AND	a.sendLoginId = h.loginId			");
		query.append(" AND	b.statusCode = e.statusCode			");	
		query.append(" AND	a.rgtn NOT IN						");		
		query.append("	(SELECT rgtn FROM TxnAmtCollectionStatusDetails) ");	

		if(!((String)txnInfo.get(6)).equals("All"))
		{
			query.append(" AND	b.statusCode  = ? 				");
			params.add((String)txnInfo.get(6));
			paramTypes.add(0);
		}
		else
		{
			query.append(" AND (b.statusCode  = 101 OR b.statusCode  = 152 OR b.statusCode  = 301 OR b.statusCode  = 401 OR b.statusCode  = 122)  	");
		}

		query.append(" AND	a.sendCountryCode	= ?			"); 
		params.add((String)txnInfo.get(1));
		paramTypes.add(0);
		
		query.append(" AND	a.sendGroupId		= ?				");		
		query.append(" AND	a.sendCurrencyCode	= ?				"); 
		query.append(" AND	a.recvCountryCode	= ?				");  
		query.append(" AND	a.recvCurrencyCode	= ?				"); 
		//query.append(" AND	a.sendModeCode		= ?				"); 
		query.append(" AND	b.recordStatus		= 'A'			"); 
		query.append(" AND	i.processcnt is  null			"); 
		query.append(" AND	a.sendRoleId IN						");
		query.append("		(SELECT roleId FROM role_mst		");
		query.append("		WHERE roleType = 'SEND')			");
		
		params.add((String)txnInfo.get(0));
		paramTypes.add(0);
		params.add((String)txnInfo.get(2));
		paramTypes.add(0);
		params.add((String)txnInfo.get(3));//recvCountry
		paramTypes.add(0);
		params.add((String)txnInfo.get(4));//recvCurrency
		paramTypes.add(0);
		//params.add((String)txnInfo.get(5));//recvModeCode
		//paramTypes.add(0);		

		query.append(" ORDER BY a.bookingDate DESC, a.rgtn, b.recordStatus");	
		Logger.log("Query********** : "+query, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,(String)txnInfo.get(0));
		}
		catch(Throwable t)
		{
			Logger.log("Error in getTxnAmtArrivedMakerRecords", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		finally
		{
			params     	= null;
			paramTypes 	= null;
			query 		= null;
		}

		return result;
	}
	
	public int insertVerificationFileChecker(ArrayList accData,String groupId,String countryCode,String currencyCode)
	{
		ArrayList quries	                         = new ArrayList();
		ArrayList<ArrayList<Object>> paramsList      = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<Integer>> paramTypesList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Object> params		             = null;
		ArrayList<Integer> paramTypes	             = null;
		StringBuffer updQry		                     = new StringBuffer();
		StringBuffer delQry		                     = new StringBuffer();
		int srNo									 = -1;
		int insertResult		                     = -1;
		
		try
		{
			for(int x=0; x < accData.size(); x=x+2)
			{
				if(x == 0)
				{			
					srNo = getMaxFileNumber(groupId,countryCode,currencyCode);
				}
				
				ArrayList verificationFileGenerationDtlQuery = new ArrayList();
				
				verificationFileGenerationDtlQuery = getVerificationFileGenerationDetailQueryJSP(srNo, (String)accData.get(x), (String)accData.get(x+1));//rgtn , amount

				if(verificationFileGenerationDtlQuery != null)
				{
					quries.add((String)verificationFileGenerationDtlQuery.get(0));

					paramsList.add((ArrayList)verificationFileGenerationDtlQuery.get(1));
					paramTypesList.add((ArrayList)verificationFileGenerationDtlQuery.get(2));
				}
				verificationFileGenerationDtlQuery = null;
			}
			
			try
			{
				insertResult = repository.executeBatch(quries, paramsList, paramTypesList,groupId);
				insertResult = srNo;
			}
			catch (Throwable t) 
			{
				Logger.log("Error in insertVerificationFileChecker", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
				insertResult = -1;
			}

			//deleting max entry if error
			if(insertResult == -1)
			{
				int revertResult = -1;
				
				quries			= new ArrayList();
				paramsList      = new ArrayList<ArrayList<Object>>();
				paramTypesList	= new ArrayList<ArrayList<Integer>>();

				delQry		= new StringBuffer();

				delQry.append(" DELETE FROM TransferFileGeneration							");
				delQry.append(" WHERE srno IN (												");
				delQry.append(" 		SELECT a.srno										");
				delQry.append("			FROM TransferFileGeneration a						");		
				delQry.append(" 		LEFT OUTER JOIN TransferFileGenerationDetails b		");
				delQry.append(" 		ON a.srno = b.srno									");	
				delQry.append(" 		WHERE b.srno  IS null)								");

				quries.add(delQry.toString());

				params		= new ArrayList<Object>();
				paramTypes	= new ArrayList<Integer>();
		
				paramsList.add(params);
				paramTypesList.add(paramTypes);	
					
				Logger.log("Failure Exception Query : "+ delQry.toString() +  "  \n  params : "  + params , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);

				try 
				{
					Logger.log("bsbDeamon Final Revert Queries : "+ quries +  "  \n  paramsList : "  + paramsList , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);

					revertResult = repository.executeBatch(quries, paramsList, paramTypesList,groupId);
				}
				catch (Throwable t) 
				{
					Logger.log("Error in updateAccountChecker", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
					revertResult = -1;
				}
			}
		}
		catch(Throwable t)
		{
				Logger.log("Error in updateACHAccountVerificationFileChecker", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
				insertResult = -1;
		}
		finally
		{
				delQry		   = null;
				updQry         = null;
				quries	       = null;
				params         = null;
				paramTypes     = null;
				paramsList     = null;
				paramTypesList = null;
		}
		return insertResult;
	}

	public ArrayList getVerificationFileGenerationDetailQueryJSP(int srNo, String rgtn, String amount)
	{
		Logger.log("getVerificationFileGenerationDetailQueryJSP "+srNo + " " +rgtn + " " + amount, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);

		ArrayList queries 				= new ArrayList();
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query 				= new StringBuffer();

		try
		{
			query.append(" INSERT INTO TransferFileGenerationDetails	");  
			query.append("		(srNo,		"); 
			query.append("		transferNo,	"); 
			query.append("		amount)		"); 
			query.append(" VALUES (			"); 
			query.append(" 		? ,	 "); 
			query.append(" 		? ,	 "); 
			query.append(" 		?)	 "); 

			queries.add(query.toString());

			params.add("" + srNo);
			paramTypes.add(0);
			params.add(rgtn);
			paramTypes.add(0);
			params.add(amount);
			paramTypes.add(0);
				
			queries.add(params);
			queries.add(paramTypes);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getVerificationFileGenerationDetailQueryJSP", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		finally
		{
			params     	= null;
			paramTypes 	= null;
			query 		= null;
		}

		return queries;
	}

	public int getMaxFileNumber(String groupId,String countryCode,String currencyCode)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query 				= new StringBuffer();
		ArrayList result 				= new ArrayList();	
		int fileNumber 					= 1;
		
		query.append(" SELECT							");  
		query.append("		max(fileNumber)				"); 
		query.append(" FROM 							"); 
		query.append(" 		TransferFileGeneration		"); 
		query.append(" WHERE 							"); 
		query.append("		groupId				= ?		"); 
		query.append(" AND	countryCode			= ?		");
		query.append(" AND	currencyCode		= ?		");
		query.append(" AND	fileType			= 'T'	");
				
		params.add(groupId);
		paramTypes.add(0);
		params.add(countryCode);
		paramTypes.add(0);
		params.add(currencyCode);
		paramTypes.add(0);

		try
		{
			result = repository.getRepositoryItems(query, params, paramTypes,groupId);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getMaxFileNumber ", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		finally
		{
			params     = null;
			paramTypes = null;
			query      = null;
		}
		if(result != null && result.size() > 0)
		{
			String [] temp	= (String [])result.get(0);
			if(temp[0] != null){
				fileNumber = Integer.parseInt("" + temp[0]) + 1;
			}else{
				fileNumber = 1;
			}
		}
			params			= new ArrayList<Object>();
			paramTypes		= new ArrayList<Integer>();
			query 			= new StringBuffer();
			int intResult 	= -1;	

			query.append(" INSERT INTO TransferFileGeneration	");  
			query.append("		(groupId,					"); 
			query.append("		countryCode,				"); 
			query.append("		currencyCode,				"); 
			query.append("		fileNumber,					"); 
			query.append("		fileType,					"); 
			query.append("		createdDate)				"); 
			query.append(" VALUES (							"); 
			query.append(" 		? ,	 "); 
			query.append(" 		? ,	 "); 
			query.append(" 		? ,	 "); 
			query.append(" 		? ,	 "); 
			query.append(" 		'T', "); 
			query.append(" 		GETDATE())	 "); 

			params.add(groupId);
			paramTypes.add(0);
			params.add(countryCode);
			paramTypes.add(0);
			params.add(currencyCode);
			paramTypes.add(0);
			params.add(""+fileNumber);
			paramTypes.add(0);

			try
			{
				intResult = repository.retrieveIdentity(query, params, paramTypes,groupId);
			}
			catch(Throwable t)
			{
				Logger.log("Error in getMaxFileNumber", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
			}
			finally
			{
				params     = null;
				paramTypes = null;
				query      = null;
			}	
		return intResult;
	}

	public String convertDate(String strDate)  
	{
		//String strDate="08-Sep-2014  10:10";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String strConvertedDate = "";
		try 
		{
			Date varDate=dateFormat.parse(strDate);
			dateFormat=new SimpleDateFormat("d/M/yyyy H:m:s");
			strConvertedDate = dateFormat.format(varDate);
		}
		catch(Throwable t)
		{
			Logger.log("Error in convertDate Method", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
			return strConvertedDate;
	}

	public String convertDate2(String strDate)  
	{
		//String strDate="08-Sep-2014  10:10";
		SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
		String strConvertedDate = "";
		try 
		{
			Date varDate=dateFormat.parse(strDate);
			dateFormat=new SimpleDateFormat("d/M/yyyy");
			strConvertedDate = dateFormat.format(varDate);
		}
		catch(Throwable t)
		{
			//System.out.println("eee : "+t);
			Logger.log("Error in convertDate Method", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		return strConvertedDate;
	}

	public String writeToFile(ArrayList data)throws Throwable 
	{
		Logger.log("Inside writeToFile : ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		int arrayLen = data.size();
		String fileData = "";
		for (int i = 0;i < arrayLen;i++ )
		{
			ArrayList recordData = (ArrayList)data.get(i);
			int recordDataSize = recordData.size();

			for (int j = 0;j < recordDataSize ;j++ )
			{
				if(j == 135)
				{
					fileData += "";
				}
				else
				{
					fileData += ""+recordData.get(j)+"";
				}
				if (j != recordDataSize - 1)
				{
					//fileData += "|";
					fileData += ",";
				}
				else
				{
					if (i != arrayLen - 1)
					{
						fileData += "\n";
						//fileData += "<br>";
					}
				}
			}
		} 
		return fileData;
	}

	public ArrayList getTransactionDebitFileData(ArrayList transferData, ArrayList fileGenerateInfo) throws Throwable 
	{
		Logger.log("inside  getTransactionDebitFileData ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		
		ArrayList fileData = new ArrayList();
		
		// header data for csv
		ArrayList userData = null;
		userData = new ArrayList();
		if(((String)fileGenerateInfo.get(0)).equals("YR"))
		{
			userData.add("REMITTERIDENTIFIER");
			userData.add("REMITTERFULLNAME");
			userData.add("REMITTERDATEOFBIRTH");
			userData.add("REMITTERAUSSTREETADDRESS");
			userData.add("REMITTERAUSTRALIANSUBURDCITY");
			userData.add("REMIITERAUSTRALIANSTATE");
			userData.add("REMIITERAUSTRALIANPOSTCODE");
			userData.add("REMITTERCOUNTRY");
			userData.add("REMITTERAUSTRALIANBSBNO");
			userData.add("REMITTERAUSTRALIANACCNO");
			userData.add("BENEFICIARYFULLNAME");
			userData.add("BENEFICIARYINDIANSTREETADDRESS");
			userData.add("BENEFICIARYINDIANCITY");
			userData.add("BENEFICIARYINDIANSTATE");
			userData.add("BENEFICIARYINDIANPOSTCODE");
			userData.add("BENEFICIARYBANKNAME");            //16
			userData.add("BENEFICIARYBANKBRANCH");          //17
			userData.add("BENEFICIARYBANKCODE");            //18
			userData.add("BENEFICIARY INDIAN ACCOUNT NO");
			userData.add("OFF SYSTEM BSB NUMBER");
			userData.add("OFF SYSTEM BSB ACCOUNT");
			userData.add("TRANSACTIONREFERENCE");
			userData.add("PAYMENTPURPOSE");
			userData.add("DATETIMEGENERATED");
		}
		else if(((String)fileGenerateInfo.get(0)).equals("RXM"))
		{
			userData.add("UNIQUE REMITTER IDENTIFIER");
			userData.add("REMITTER FULLNAME");
			userData.add("REMITTER DATEOFBIRTH");
			userData.add("REMITTER ADDRESS");
			userData.add("REMITTER SUBURB/CITY");
			userData.add("REMITTER STATE");
			userData.add("REMITTER POSTCODE");
			userData.add("REMITTER COUNTRY");
			userData.add("REMITTER AUSTRALIAN BSB");
			userData.add("REMITTER AUSTRALIAN ACCOUNT NUMBER");
			userData.add("BENEFICIARY FULL NAME");
			userData.add("BENEFICIARY ADDRESS");
			userData.add("BENEFICIARY CITY");
			userData.add("BENEFICIARY STATE");
			userData.add("BENEFICIARY POST CODE");
			userData.add("BENEFICIARY BANK NAME");            //16
			userData.add("BENEFICIARY BANK BRANCH");          //17
			userData.add("BENEFICIARY BANK CODE");            //18
			userData.add("BENEFICIARY IN COUNTRY ACCOUNT NUMBER");
			userData.add("OFF SYSTEM BSB NUMBER");
			userData.add("OSBSB ACCOUNT NUMBER");
			userData.add("TRANSACTION REFERENCE IDENTIFIER");
			userData.add("PAYMENT PURPOSE");
			userData.add("DATE TIME GENERATED");
		}
		fileData.add(userData);
		
		if(transferData != null && transferData.size() > 0)
		{
			Logger.log("data exists in transferData and size :  "+transferData.size(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			for(int x = 0; x < transferData.size(); x++)
			{
				String dateTimeGenerated = "";
				String countryName = "";
				String [] xlsData = new String [21];
				ArrayList rowData = null;
				rowData = new ArrayList();
				String[] temp = (String [])transferData.get(x);
				
				rowData.add(temp[0]!= null ? temp[0] : "");//REMITTER IDENTIFIER 0
				
				rowData.add((temp[1] + " " + temp[2] + " " + temp[3]).replaceAll("\\s+"," "));//REMITTERFULLNAME 1 2 3
				
				//rowData.add(temp[4]!= null ? temp[4] : "");//REMITTERDATEOFBIRTH
				String dateVal = (temp[4]!= null ? temp[4] : "");
				String dateValFormatted = convertDate2(dateVal);
				rowData.add(dateValFormatted);//REMITTERDATEOFBIRTH
				
				rowData.add((temp[5] + "~" + temp[6] + "~" + temp[7] + "~" + temp[8] ).replaceAll(","," ").replaceAll("\\s+"," "));//REMITTERAUSSTREETADDRESS
				
				rowData.add(temp[9]!= null ? temp[9].replaceAll(","," ") : "");//REMITTERAUSTRALIANSUBURDCITY
				
				rowData.add(temp[10]!= null ? temp[10].replaceAll(","," ") : "");//REMIITERAUSTRALIANSTATE
				
				rowData.add(temp[11]!= null ? temp[11] : "");//REMIITERAUSTRALIANPOSTCODE
				
				rowData.add(temp[12]!= null ? temp[12].replaceAll(","," ") : "");//REMITTERCOUNTRY --- hardcoded to australia
				
				rowData.add(temp[13]!= null ? temp[13] : "");//REMITTERAUSTRALIANBSBNO
				
				String encAccNo = temp[14]!= null ? temp[14] : "";
				Logger.log("accountNo from db : "+encAccNo, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				String decAccNo = "";
				try
				{
					EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
					decAccNo = encDec.decryptString(encAccNo.trim());
					Logger.log("decAccNo after decrpyt "+decAccNo, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				}
				catch(Throwable t)
				{
					decAccNo = encAccNo;
					Logger.log("Error while decrypting data ", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
				}
				Logger.log("decAccNo "+decAccNo, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				rowData.add(decAccNo.replaceAll(" ",""));//REMITTERAUSTRALIANACCNO
				
				rowData.add((temp[15] + " " + temp[16]).replaceAll("\\s+"," "));//BENEFICIARYFULLNAME
				
				rowData.add(temp[17].replaceAll(","," ").replaceAll("\\s+"," ").replace("\"", " "));//BENEFICIARYINDIANSTREETADDRESS
				
				rowData.add(temp[18]!= null ? temp[18].replaceAll(","," ") : "");//BENEFICIARYINDIANCITY
				
				rowData.add(temp[19]!= null ? temp[19].replaceAll(","," ") : "");//BENEFICIARYINDIANSTATE
				
				rowData.add(temp[20]!= null ? temp[20] : "");//BENEFICIARYINDIANPOSTCODE
				
				rowData.add(temp[27]!= null ? temp[27] : "");//BENEFICIARYBANKNAME
				rowData.add(temp[28]!= null ? temp[28].replaceAll(","," ") : "");//BENEFICIARYBANKBRANCH
				rowData.add(temp[29]!= null ? temp[29] : "");//BENEFICIARYBANKCODE
				
				String benfEncAccNo = temp[21]!= null ? temp[21] : "";
				Logger.log("accountNo from db : "+encAccNo, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				String benfDecAccNo = "";
				try
				{
					EncryptionDecryption encDec = EncryptionDecryption.getEncryptionDecryption();
					benfDecAccNo = encDec.decryptString(benfEncAccNo.trim());
					Logger.log("benfDecAccNo after decrpyt "+benfDecAccNo, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				}
				catch(Throwable t)
				{
					benfDecAccNo = benfEncAccNo;
					Logger.log("Error while decrypting data ", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
				}
				//rowData.add(temp[21]!= null ? temp[21] : "");//BENEFICIARY INDIAN ACCOUNT NO
				rowData.add(benfDecAccNo.replaceAll(" ",""));//BENEFICIARY INDIAN ACCOUNT NO
				
				if(((String)fileGenerateInfo.get(0)).equals("RXM"))
					rowData.add("037891");//OFF SYSTEM BSB NUMBER
				else if(((String)fileGenerateInfo.get(0)).equals("YR"))
					rowData.add("37884");//OFF SYSTEM BSB NUMBER
				else
					rowData.add(temp[22]!= null ? temp[22] : "");//OFF SYSTEM BSB NUMBER
				
				//OFF SYSTEM BSB ACCOUNT
				String sb = temp[23]!= null ? temp[23] : "";
				String bsbNo = "";
				if(sb.length() >= 9)
				{
					bsbNo = sb.substring(sb.length()-9, sb.length());
				}else{
					bsbNo = sb;
				}
				rowData.add(bsbNo);
				
				//rowData.add("'"+temp[24].replaceAll(","," ").replaceAll("\\s+"," "));//TRANSACTIONREFERENCE
				rowData.add(temp[24]!= null ? temp[24] : "");
				
				rowData.add(temp[25]!= null ? temp[25] : "");//PAYMENTPURPOSE
				
				//xlsData[20] = temp[26]!= null ? temp[26] : "";//DATETIMEGENERATED need to call convert date function
				String dateTimeVal = temp[26].replaceAll(" ", "-").replaceAll("@"," ");
				dateTimeGenerated = convertDate(dateTimeVal);
				//xlsData[20] = dateTimeGenerated;
				rowData.add(dateTimeGenerated);
				
				fileData.add(rowData);
			}
		}else
		{
			Logger.log("transferData is empty... no records", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		}	
		return fileData;		
	}

	public ArrayList getTransferFileData(ArrayList fileGenerateInfo)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query 				= new StringBuffer();
		ArrayList result				= new ArrayList();		

		if(((String)fileGenerateInfo.get(4)).equals("T"))
		{
			 query.append("	SELECT								");
			 query.append("		c.sendLoginId, 					");
			 query.append("		d.firstName,					");
			 query.append("		d.middleName,					");
			 query.append("		d.lastName,						");
			 query.append("		CONVERT(VARCHAR, i.dob, 103),	");
			 query.append("		d.address1,						");
			 query.append("		d.address2,						");
			 query.append("		d.address3,						");
			 query.append("		d.street,						");
			 query.append("		d.city,							");
			 query.append("		d.state,						");
			 query.append("		d.zip,							");
			 query.append("		l.country,						");
			 query.append("    CASE				   				");				                                                                                                            
			 //query.append("	WHEN c.sendModeCode='CIP' THEN e.bankBranch		");
			 query.append("	WHEN c.sendModeCode='CIP' THEN e.bsbCode		");
			 query.append("	WHEN c.sendModeCode='IBTR' THEN j.bsbCode	");                                                                                                    
			 query.append("	WHEN c.sendModeCode='WIRE' THEN k.bankBranch	");
			 query.append("	END 'bankName',									");

			 query.append("    CASE											");			                                                                                                            
			 query.append("	WHEN c.sendModeCode='CIP' THEN e.accountNo	    ");                                                                                            
			 query.append("	WHEN c.sendModeCode='IBTR' THEN j.accountNo	    ");                                                                                            
			 query.append("	WHEN c.sendModeCode='WIRE' THEN k.bankBranch    ");                                                                                        
			 query.append("	END 'accountNo',								");
			 
			 query.append("		c.recvFirstName,				");
			 query.append("		c.recvLastName,					");
			 query.append("		c.recvAddress,					");
			 query.append("		c.recvCity,						");
			 query.append("		c.recvState,					");
			 query.append("		c.recvPinCode,					");

			 query.append("     c.recvAccNumber,				");
			 
			 query.append("		37884,							");
			 query.append("		c.txnRefNo,						");
			 query.append("		c.txnRefNo,						");
			 query.append("		c.fircPurpose,					");
			 //query.append("		CONVERT(VARCHAR, c.bookingDate, 106) + '@' + CONVERT(VARCHAR(8), c.bookingDate, 114)		");
			 query.append("		CONVERT(VARCHAR, getDate(), 106) + '@' + CONVERT(VARCHAR(8), getDate(), 114),		");
			 query.append("		c.recvBankName,					");
			 query.append("		c.recvBankBranch,				");
			 query.append("		c.recvMicr						");
			 query.append("		FROM Transactions  c					");
			 query.append("		LEFT JOIN TxnCipDtls e ON c.rgtn = e.rgtn       ");                                                                                                
			 query.append("		LEFT JOIN TxnIbtrDtls j ON c.rgtn = j.rgtn      ");                                                                                              
			 query.append("		LEFT JOIN TxnWireDtls k ON c.rgtn = k.rgtn,		");
			 query.append("		     TransferFileGenerationDetails b,			");
			 query.append("		     TransferFileGeneration a,					");
			 query.append("		     Sender d,									");
			 query.append("		     TransactionStatusDetails f,				");
			 query.append("		     Country g,									");
			 query.append("		     ReceiveMode h,								");
			 query.append("		     UserMaster i,								");
			 query.append("		     Country l									");
			 query.append("		WHERE a.srNo = b.srNo							");
			 query.append("		  AND b.transferNo = c.rgtn						");
			 query.append("		  AND c.sendUserId = d.userId					");
			 query.append("		  AND c.sendLoginId = d.loginId					");
			 query.append("		  AND c.sendGroupId = d.groupId					");
			 query.append("		  AND c.rgtn = e.rgtn							");
			 query.append("		  AND c.rgtn = f.rgtn							");
			 query.append("		  AND c.recvCountry = g.countryCode				");
			 query.append("		  AND c.sendCountryCode=l.countryCode			");
			 query.append("		  AND c.receiveModeCode= h.receiveModeCode		");
			 query.append("		  AND c.sendLoginId = i.loginId					");
			 query.append("		  AND c.senduserId = i.userId					");
			 query.append("		  AND c.sendgroupId = i.groupId					");
			 query.append("		  AND f.recordStatus = 'A'						");
			 query.append("		  AND a.srNo = ?								");
			 query.append("		  AND a.groupId = ?								");
			 query.append("		  AND a.countryCode = ?							");
			 query.append("		  AND a.currencyCode = ?						");
			 query.append("		  AND a.fileType = ?							");
			
			params.add((String)fileGenerateInfo.get(3)); //Sr No
			paramTypes.add(0);
			params.add((String)fileGenerateInfo.get(0)); //GroupId
			paramTypes.add(0);
			params.add((String)fileGenerateInfo.get(1)); //Country Code
			paramTypes.add(0);
			params.add((String)fileGenerateInfo.get(2)); //Currency Code
			paramTypes.add(0);
			params.add((String)fileGenerateInfo.get(4)); //File Type
			paramTypes.add(0);

		}else{
			Logger.log("file type other than T , fileTYpe : "+fileGenerateInfo.get(4).toString() , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		}
		try
		{
			Logger.log("getTransferFileData query : "+query + " \n params : " +params , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			result = repository.getRepositoryItems(query, params, paramTypes,(String)fileGenerateInfo.get(0));
		}
		catch(Throwable t)
		{
			result = null;
			Logger.log("Error in getTransferFileData", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		finally
		{
			params 		= null;
			paramTypes 	= null;		
			query 		= null;
		}
		return result;
	}

	public String downloadFile(ArrayList fileGenerateInfo)
	{
		ArrayList transferData = new ArrayList();
		ArrayList data = new ArrayList();
		String fileString = "";
		Logger.log("inside  downloadFile : "+fileGenerateInfo.size(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		try
		{
			transferData = getTransferFileData(fileGenerateInfo);
			Logger.log("transferData size : "+transferData.size(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		}
		catch (Throwable t)
		{
			Logger.log("Error in downloadFile :: getTransferFileData()", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		try
		{
			if(transferData!= null && transferData.size() >0)
			{
				Logger.log("File Type :(Debit or credit) fileGenerateInfo.get(4)  : "+fileGenerateInfo.get(4).toString(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				if(((String)fileGenerateInfo.get(4)).equals("V")) //Verification File
				{
					if(((String)fileGenerateInfo.get(5)).equals("D")) //Debit File
					{
						//data = getAccountVerificationDebitFileData(transferData, fileGenerateInfo);
					}
					else //Credit File
					{
						//data = getAccountVerificationDebitFileData(transferData, fileGenerateInfo);	
					}
				}
				else //Transaction File
				{
					if(((String)fileGenerateInfo.get(5)).equals("D")) //Debit File
					{
						data = getTransactionDebitFileData(transferData, fileGenerateInfo);
					}
					else //Credit File
					{
						data = getTransactionDebitFileData(transferData, fileGenerateInfo);		
					}
				}

				fileString = writeToFile(data);
				Logger.log("file string value : "+fileString, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			}
			else
			{
				Logger.log("No Record Found for fileGenerateInfo : " + fileGenerateInfo, "BSBDeamon.java", "BSBDeamon", null, Logger.CRITICAL);
			}
		}
		catch (Throwable t)
		{
			Logger.log("Error in downloadFile :: process transferData", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}

		transferData = null;
		data = null;

		return fileString;	
	}

	//to insert file name in db after completing file creation
	public int insertFTPFileDetails(ArrayList ftpFileDetails)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer query		        = new StringBuffer();
		StringBuffer insUpdQry		    = new StringBuffer();
		int insUpdRes		            = -1;
		ArrayList result                = new ArrayList();
		String spId = "";
		String directory = "";
		String pgpDownloadPath = EnvProperties.getValue("ADMIN_DOWNLOAD_PATH");
		//to get ftp service provider data
		try
		{
			query.append(" SELECT										");
			query.append(" 		spId,									");
			query.append("		ftpDirectory							");
			query.append(" FROM											");
			query.append(" 		FTPServiceProvider 						");
			query.append(" WHERE										");
			query.append("	   spName = ?								");
			query.append(" AND groupId	= ?								");
			query.append(" AND countryCode = ?							");
			query.append(" AND currencyCode = ?							");
			query.append(" AND fileEvent = ?							");
			query.append(" AND fileType = ?								");
			query.append(" AND isActive = 'Y'							");
			
			params.add((String)ftpFileDetails.get(9));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(0));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(6));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(7));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(4));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(5));
			paramTypes.add(0);
			
			result = repository.getRepositoryItems(query, params, paramTypes,(String)ftpFileDetails.get(0));
		}
		catch(Throwable t)
		{
			Logger.log("Error in insertFTPFileDetails while retrieving service providerdetails", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		finally
		{
			params     = null;
			paramTypes = null;
			query      = null;
		}
		
		if(result != null && result.size() >0)
		{
			String[] temp = (String [])result.get(0);
			spId = temp[0] == null ? "" : temp[0];
			directory = temp[1] == null ? "" : temp[1];
		}
		ftpFileDetails.add(spId);
		ftpFileDetails.add(pgpDownloadPath);
		ftpFileDetails.add(directory);
		Logger.log("spid filepath ftpdir : "+spId+" "+pgpDownloadPath+" "+directory, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		try
		{
			insUpdQry.append(" INSERT INTO FTPFileDetails (			");
			insUpdQry.append(" 	    spId,					");
			insUpdQry.append(" 	    groupId,				");
			insUpdQry.append(" 	    srNo,				");
			insUpdQry.append(" 	    fileName,				");
			insUpdQry.append(" 	    fileStatus,				");
			insUpdQry.append(" 	    fileEvent,				");
			insUpdQry.append(" 	    fileType,				");
			insUpdQry.append(" 	    countryCode,			");
			insUpdQry.append(" 	    currencyCode,			");
			insUpdQry.append("	    createdDate,			");
			insUpdQry.append("	    createdBy,			");
			insUpdQry.append("	    filePath,			");
			insUpdQry.append("	    toFtpDirectory,			");
			insUpdQry.append("	    fileSize,			");
			insUpdQry.append("	    batchType)			");
			insUpdQry.append(" VALUES (					");
			insUpdQry.append("	    ?,					");
			insUpdQry.append("	    ?,					");
			insUpdQry.append(" 	    ?,					");
			insUpdQry.append(" 	    ?,					");
			insUpdQry.append(" 	    ?,					");   
			insUpdQry.append("	    ?,					");
			insUpdQry.append(" 	    ?,					");
			insUpdQry.append(" 	    ?,					");
			insUpdQry.append(" 	    ?,					"); 		
			insUpdQry.append("	    GETDATE(),			");
			insUpdQry.append(" 	    ?,					"); 
			insUpdQry.append(" 	    ?,					"); 
			insUpdQry.append(" 	    ?,					"); 
			insUpdQry.append(" 	    ?,					");
			insUpdQry.append(" 	    'BSB')				");		
			
			params		= new ArrayList<Object>();
			paramTypes	= new ArrayList<Integer>();

			params.add((String)ftpFileDetails.get(11));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(0));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(1));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(2));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(3));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(4));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(5));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(6));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(7));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(8));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(12));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(13));
			paramTypes.add(0);
			params.add((String)ftpFileDetails.get(10));
			paramTypes.add(0);
			
			try 
			{
				insUpdRes = repository.retrieveIdentity(insUpdQry, params, paramTypes,(String)ftpFileDetails.get(0));
			}
			catch (Throwable t) 
			{
				Logger.log("Error in insertFTPFileDetails", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
				insUpdRes = -1;
			}
			Logger.log("insertFTPFileDetails "+insUpdRes, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		}
		catch(Throwable t)
		{
			Logger.log("Error in insertFTPFileDetails", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
			insUpdRes = -1;
		}
		finally
		{
			params     = null;
			paramTypes = null;
			insUpdQry  = null;
		}
		Logger.log("after insert into ftpfile details : "+insUpdRes, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		return insUpdRes;
	}

	public String createFIleName(String name,String replaceString)
	{
		try
		{
			StringBuilder sb = new StringBuilder(name);
			int in = sb.indexOf(".");
			sb.replace(in, sb.length(), replaceString);
			return sb.toString();
		}
		catch(Throwable t)
		{
			Logger.log("Error in createFIleName methods", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		return name;
	}

	public static String encrypt(String originalFilePath,String encryptedFilePath,String serviceProvider,String groupId)
	{
		File in = new File(originalFilePath);
		String encryptedFileReturn = "";
		long encryptedFileSize = -1;
		String providerPublicKey = EnvProperties.getValue(groupId+"_"+serviceProvider+"_ADMIN_FTP_UPLOAD_PATH")+"Keys/"+serviceProvider+"_Prod_PGP_key.txt";
		String privateKey = EnvProperties.getValue(groupId+"_"+serviceProvider+"_ADMIN_FTP_UPLOAD_PATH")+"Keys/secring.gpg";
		String passPhrase = "avenues@1";
		boolean encFlag = false;
		Logger.log("Orginal File Path : "+originalFilePath+" \n Encrypted File Path : "+encryptedFilePath+" \n providerPublicKey : " + providerPublicKey + " \n Service Provider : "+serviceProvider+" \n GroupID : "+groupId, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		if(in.exists())
		{
			Logger.log("----------------START PGP Encryption------------------ ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			try
			{
				SignatureUtil.signAndEncryptFile(new FileOutputStream(encryptedFilePath),originalFilePath,SignatureUtil.readPublicKey(new FileInputStream(providerPublicKey)),SignatureUtil.readSecretKey(new FileInputStream(privateKey)),passPhrase, false, true);
				Logger.log("File encrypted successfully " + originalFilePath , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				encFlag = true;
			}
			catch (Throwable t) 
			{
				Logger.log("File encrypted failed. " + originalFilePath , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
				encFlag = false;
			}
			
			if(encFlag == true)
			{
				try
				{
					File out = new File(encryptedFilePath);
					encryptedFileSize = out.length();
					Logger.log("Encrypted file size : " + encryptedFileSize , "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);			
					Logger.log("----------------END PGP Encryption------------------ ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
					encryptedFileReturn = "1~" + encryptedFilePath + "~" + encryptedFileSize;
				}
				catch (Exception e) 
				{
					Logger.log("Error while geeting encrypted file size : " + encryptedFilePath , "BSBDeamon.java", "BSBDeamon", e, Logger.CRITICAL);
				}
			}	
		}
		else
		{
			Logger.log("file not found  : "+originalFilePath, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		}
		Logger.log("encryptedFileReturn : "+encryptedFileReturn, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		return encryptedFileReturn;
	}

	public String uploadSFTP(String ftpFileToUploadName,String selectedGroupId,String countryCode,String currencyCode,String selectedProvider,String fileType, String ftpFileToUploadPath)
	{
		String returnResult = "";
		int replyCode = -1;
		String ftpPath = EnvProperties.getValue(selectedGroupId+"_"+selectedProvider+"_ADMIN_FTP_UPLOAD_PATH")+"IN/";
		String prvoiderFtpPath = "";
		File in = new File(ftpPath + ftpFileToUploadName);
		String ftpServerName="",ftpServerUsername="",ftpServerPassword="";
		
		String ftpInputFilePath = EnvProperties.getValue(selectedGroupId+"_"+selectedProvider+"_ADMIN_FTP_UPLOAD_PATH")+"IN/"+ftpFileToUploadName;
		String ftpInputFileName = ftpFileToUploadName;
		String ftpOutputFilePath = ftpFileToUploadPath; //"/YesBankIncoming/";
		String ftpOutputFileName = ftpFileToUploadName;
		
		ArrayList ftpSPDtls=new ArrayList();
		ArrayList ftpSPData=new ArrayList();
		ftpSPData.add(selectedGroupId);
		ftpSPData.add(countryCode);
		ftpSPData.add(currencyCode);
		ftpSPData.add(fileType);// file type Transaction
		ftpSPData.add(selectedProvider);// service provider
		ftpSPData.add("U");//file event Download

		try
		{
			ftpSPDtls= new FileTransferController().getFTPServiceProviderData(ftpSPData);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getFTPServiceProviderData " , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);		
		}
		finally
		{
			ftpSPData  = null;
		}
		if(ftpSPDtls!=null && ftpSPDtls.size() > 0)
		{
			String [] ftpTemp = (String[]) ftpSPDtls.get(0);
			ftpServerName = ftpTemp[8];
			ftpServerUsername = ftpTemp[10];
			Logger.log("From DB : ftp serverName : "+ftpServerName+" ftp serverUserName : "+ftpServerUsername , "BSBDeamon.java", "BSBDeamon",null, Logger.INFO);
			if(in.exists())
			{
				//to write the sftp upload code here
				replyCode = uploadFile(ftpServerName, ftpServerUsername, ftpServerPassword, ftpInputFilePath, ftpInputFileName, ftpOutputFilePath, ftpOutputFileName,selectedProvider,selectedGroupId);
				if(replyCode != -1)
				{
					returnResult = "1~" + ftpOutputFilePath + "~"+ replyCode;
				}
			}
			else
			{
				Logger.log("File not found in uploadSFTP() : "+(ftpPath+ftpFileToUploadName), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			}
		}
		else
		{
			Logger.log("Service provider details not found : "+selectedProvider, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		}
		return returnResult;
	}

	public int uploadFile(String ftpServerName,String ftpServerUsername,String ftpServerPassword,String ftpInputFilePath,String ftpInputFileName,String ftpOutputFilePath,String ftpOutputFileName,String selectedProvider,String selectedGroupId)
	{
		int returnResult = -1;
		int port=22;
		
		Logger.log("-------START SFTP UPLOAD-------", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		Logger.log("Ftp server name : "+ftpServerName+" and ftp user name : "+ftpServerUsername
				+"\nFile to upload :" + ftpInputFilePath+" file name : "+ftpInputFileName
				+ "\nFile will be uploaded at :" + ftpOutputFilePath + "  File Name : " + ftpOutputFileName, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		String openSshPrivateKeyPath = EnvProperties.getValue(selectedGroupId+"_"+selectedProvider+"_ADMIN_FTP_UPLOAD_PATH")+"Keys/"+"avenues_openSSHKey_private_key";


		Logger.log("Ftp server name openSshPrivateKeyPath : "+openSshPrivateKeyPath, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);

		try
		{
			JSch sch = new JSch();
			sch.addIdentity(openSshPrivateKeyPath);
			Session session = sch.getSession(ftpServerUsername, ftpServerName, port);
			session.setConfig("StrictHostKeyChecking", "no");
			Logger.log("Establishing Connection... ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			
			session.connect();
			Logger.log("Connection established. ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp)channel;
			
			Logger.log("cur dir : "+channelSftp.pwd(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			
			byte[] bufr = new byte[(int) new File(ftpInputFilePath).length()];
			FileInputStream fis = new FileInputStream(new File(ftpInputFilePath));
			int fileSize = fis.available();
			Logger.log("Uploaded file size :  " + fileSize + " bytes", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			
			fis.read(bufr);
			ByteArrayInputStream fileStream = new ByteArrayInputStream(bufr);
			//channelSftp.put(fileStream, "/YesBankIncoming/" + ftpOutputFileName);
			channelSftp.put(fileStream, ftpOutputFilePath + ftpOutputFileName);
	        fileStream.close();
	        
	        Logger.log("Upload succeessful. ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
	        
	        session.disconnect();
	        Logger.log("session disconnected ", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
	        
	        returnResult = fileSize;
		}
		catch(Throwable t)
		{
			Logger.log("SFTP Failed. Error... ", "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
			returnResult = -1;
		}
		Logger.log(" returnResult from uploadFile : "+returnResult, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		return returnResult;
	}

	//to check for weekdays
	public boolean weekdayCheck()
	{
		boolean flag = false;
		try
		{
			SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			
			Date dat =  cal.getTime();
			String expDate =newDateFormat.format(dat); 
			
			Logger.log("expDate : " + expDate, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			Date MyDate = newDateFormat.parse(expDate);
			newDateFormat.applyPattern("EEEE");
			String day = newDateFormat.format(MyDate);
					
			Logger.log("day : " + day, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			if(day.equalsIgnoreCase("saturday") || day.equalsIgnoreCase("sunday"))
			{
				Logger.log("Saturday Sunday : " + day, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			}
			else
			{
				flag = true;
			}
		}
		catch(Throwable t)
		{
			System.out.println("some error while date calculate");
			Logger.log("Some error occured while calculating date : "+t , "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		return flag;
	}
	
	public static void main(String []args)
	{
		BSBDeamon bsb = new BSBDeamon();
		String selectedGroupId = "", sendCountry = "", sendCurrency = "", receiveCountry = "", receiveCurrency = "", txnStatus = "", fileType = "";
		
		boolean inpFlag = false, weekdayFlag = false;
		String result = "";
		String argument = "";

		if(args != null && args.length > 0)
		{
			Logger.log("args [] : " + args, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);

			try
			{
				argument = args[0];
				String val [] = argument.split("~");
				selectedGroupId = val[0];
				sendCountry = val[1];
				sendCurrency = val[2];
				receiveCountry = val[3];
				receiveCurrency = val[4];
				txnStatus = val[5];
				fileType = val[6];
				inpFlag = true;
			}
			catch(Throwable t)
			{
				Logger.log("Some error occured while creating input param : "+t, "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
			}
		}
		else
		{
			Logger.log("Please Provide Input Params", "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		}
		
		try
		{
			weekdayFlag = bsb.weekdayCheck();
		}
		catch(Throwable t)
		{
			Logger.log("Some error occured while checking for weekDay : "+t, "BSBDeamon.java", "BSBDeamon", t, Logger.CRITICAL);
		}
		Logger.log("arguments provided : "+argument+" inpFlag : "+inpFlag+" weekdayFlag : "+weekdayFlag, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		if(inpFlag == true && weekdayFlag == true)
		{
			result = bsb.bsbProcess(selectedGroupId, sendCountry, sendCurrency, receiveCountry, receiveCurrency, txnStatus, fileType);
			
			Logger.log("errMsg :"+errMsg, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			if(!errMsg.equals(""))
			{
				//send mail
				Logger.log("Sending BSB Failed Mail with reason :"+errMsg+" and the process state is :"+result, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
				EventHandler eh = new EventHandler();

				eh.setGroupId(selectedGroupId);
				eh.setSubject("Error :BSB File Upload Failed");		
				eh.setMailBody("Error : "+errMsg+"\nProcess Report : "+result);
				eh.setEventName("BSB_FILEUPLOAD_ALERT");
				eh.raiseEvent();
				
				Logger.log("BSB Error mailed send successfully on : "+new java.util.Date(), "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
			}
		}
		Logger.log("result : "+result+" inpFlag : "+inpFlag+" weekdayFlag : "+weekdayFlag+" argument : "+argument+" errMSg : "+errMsg, "BSBDeamon.java", "BSBDeamon", null, Logger.INFO);
		
		
	}
}