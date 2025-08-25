package com.api.remitGuru.component.schedularjob;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.util.EnvProperties;
import com.api.remitGuru.component.util.Logger;
import com.api.remitGuru.component.util.dao.SQLRepository;

public class docUploadStatus implements Job{

	SQLRepository repository = new SQLRepository();
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{
	
		ArrayList<Object> DocUploadData = new ArrayList<>();
		try 
		{
			DocUploadData = getDocUploadData();
			Logger.log("DocUploadData : " + DocUploadData.size(), "docUploadStatus.java", "docUploadStatus", null, Logger.INFO);
		}
		catch (Throwable e) 
		{
			Logger.log("Error in getting data::: ", "docUploadStatus.java", "docUploadStatus", e, Logger.CRITICAL);
		}
		
		if(DocUploadData.size() > 0)
		{
			for(int i=0;i<DocUploadData.size();i++)
			{
				String docstatus="";
				int updateDocStatus=0;
				String [] temp	= (String [])DocUploadData.get(i);
				String txnRefNo = temp[0] == null ? "" : temp[0];
				
				String docuploadstatus = docUploadStatus(txnRefNo);
				Logger.log("docuploadstatus :" + docuploadstatus, "docUploadStatus.java", "docUploadStatus", null, Logger.INFO);
				if(!docuploadstatus.trim().equals(""))
				{
					try
					{												
						JSONObject object = new JSONObject(docuploadstatus);
						docstatus = object.getString("response");
						Logger.log("response : " + docstatus,"docUploadStatus.java", "docUploadStatus", null, Logger.INFO);
					}
					catch (Throwable t)
					{
						Logger.log("Error in getting response from docstatus api ::: ", "docUploadStatus.java", "docUploadStatus", t, Logger.CRITICAL);
					}

						updateDocStatus = updateTransactionStatus(txnRefNo,docstatus);
						Logger.log("updateTxnResult :" + updateDocStatus, "docUploadStatus.java", "docUploadStatus", null, Logger.INFO);
				}

			}
		}
		
		
	}

	private int updateTransactionStatus(String txnRefNo, String docstatus) {
		ArrayList<Object> params						= new ArrayList<Object>();
		ArrayList<Integer> paramTypes					= new ArrayList<Integer>();
		StringBuffer updQry			 					= new StringBuffer();
		int updateResult		 						= -1;
		
		if(docstatus.equalsIgnoreCase("Success"))
			docstatus="Y";
		else
			docstatus="N";
		
		updQry.append(" UPDATE									");
		updQry.append("    txnAdditionalDataRequests            ");
		updQry.append(" SET                                     ");
		updQry.append("    docUploadStatus= ? ,		            ");
		updQry.append("    updatedBy = 'DAEMON' ,               ");
		updQry.append("    updatedDate = GETDATE()              ");
		updQry.append(" WHERE                                   ");
		updQry.append("		 groupId  = 'IDFC'					");
		updQry.append(" AND  txnRefNo = ?						");
		updQry.append(" AND  recordStatus =  'S'				");
		updQry.append(" AND  docUploadStatus =  'N'				");
				
		
		params.add(docstatus);//docInfo
		paramTypes.add(0);
		params.add(txnRefNo);//docInfo
		paramTypes.add(0);
		
		try
		{
			updateResult = repository.executeQuery(updQry, params, paramTypes, "IDFC");
			Logger.log("queries :"+updQry+ "\n paramsList:"+params+"\n updateResult : "+updateResult, "docUploadStatus.java", "docUploadStatus", null, Logger.INFO);
		
		}
		catch(Throwable t)
		{
			updateResult = -1;
			Logger.log("Error in update txnAdditionalDataRequests ", "docUploadStatus.java", "docUploadStatus", t, Logger.CRITICAL);
		}
		finally
		{
			updQry		   = null;
			params         = null;
			paramTypes     = null;
			
		}

		return updateResult;
	}

	private String docUploadStatus(String txnRefNo) {
		String docStatus= "";
		StringBuffer  jsonString = new StringBuffer();
	    try {
	        URL url = new URL(EnvProperties.getPropertyValue("DOCUPLOADSTATUS") == null ? "" : EnvProperties.getPropertyValue("DOCUPLOADSTATUS"));
	        docStatus="{\r\n" + 
					"	\"customerRefNumber\": \""+txnRefNo+"\",\r\n" +
					"	\"documentUpload\": \"Success\"\r\n" +
					"}\r\n" + 
					"";
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			Logger.log("docStatus>>>>>> " + docStatus,"docUploadStatus.java", "docUploadStatus",null,Logger.INFO);
			
			connection.setRequestProperty("content-type", "application/json; charset=UTF-8");
			
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept", "*/*");
			
			//OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			OutputStream writer = connection.getOutputStream();
			writer.write(docStatus.getBytes());
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();

			BufferedReader br = null ;
			
			if(responseCode <= 201)
					br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			else
				br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

			Logger.log("BufferedReader : " + br,"docUploadStatus.java", "docUploadStatus",null,Logger.INFO);
		  
			String line;
			while ((line = br.readLine()) != null)
			{
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
			Logger.log("Do Transfer	: " + jsonString.toString(),"docUploadStatus.java", "docUploadStatus",null,Logger.INFO);

		}
		catch (Exception e)
		{
			Logger.log("Error:2002 while calling Docstatus Webservice : " + docStatus+"JSON "+jsonString.toString(),"docUploadStatus.java", "docUploadStatus",e,Logger.CRITICAL);
			//throw new RuntimeException(e.getMessage());
			jsonString.append("");
		}
		return jsonString.toString();

	}

	private ArrayList<Object> getDocUploadData() 
	{
		ArrayList<Object> params						= new ArrayList<Object>();
		ArrayList<Integer> paramTypes					= new ArrayList<Integer>();
		StringBuffer selQry			 					= new StringBuffer();
		ArrayList result 								= new ArrayList();
		
		selQry.append(" SELECT								");
		selQry.append("		  txnRefNo						");
		selQry.append(" FROM								");
		selQry.append(" 	 txnAdditionalDataRequests		");
		selQry.append(" WHERE								");
		selQry.append("		 groupId  = 'IDFC'				");
		selQry.append(" AND  docUploadStatus =  'N'			");
		selQry.append(" AND  recordStatus =  'S'			");
		selQry.append(" AND  createdDate > (GETDATE()-10) 	"); 
			
		try
		{
			result = repository.getRepositoryItems(selQry, params, paramTypes,"IDFC");
			Logger.log("select recordData Qry: " + selQry + "\n paramsList : " + params+"\n result:"+result.size(), "docUploadStatus.java", "docUploadStatus", null, Logger.INFO);
		}
		catch(Throwable t)
		{
			Logger.log("Error in txnAdditionalDataRequests select query", "docUploadStatus.java", "docUploadStatus", t, Logger.CRITICAL);
		}
		finally
		{
			selQry		   = null;
			params         = null;
			paramTypes     = null;
		}

		return result;

	}

}
