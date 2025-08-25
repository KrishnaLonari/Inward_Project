package com.api.remitGuru.component.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;

import com.api.remitGuru.component.environment.Constants;
import com.api.remitGuru.component.util.dao.SQLRepository;

/*
 *	public String getDecryptedKey(String prpName, String groupId) 
 * 
 */

public class AttributeManager {
	
	SQLRepository repository;
	public static String propFileName = Constants.configFilePath + "seckeyring.properties";

	public static String dbKey 	 = ""; 
	public static String fileKey = ""; 

	
	public AttributeManager(){

		RGUtil rgutil = new RGUtil();
		repository = (SQLRepository)(rgutil.getRepositoryObject());
	}
	
	public ArrayList<String> getEncryptionKeys()
	{
		dbKey 	= getDatabaseKeyPart();
		fileKey = getFileKeyPart();
		
		ArrayList<String> keys = new ArrayList<String>();
		keys.add(dbKey + fileKey); //Password
		keys.add(fileKey + dbKey); //IV Vector
		
		dbKey 	= Base64.encodeBase64String(dbKey.getBytes());
		fileKey = Base64.encodeBase64String(fileKey.getBytes());
    	
		return keys;
	}
	
	public ArrayList<String> getDecryptionKeys(String prpName, String groupId)
	{
		ArrayList<Object> params		= new ArrayList<Object>();
		ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
		StringBuffer selQry 			= new StringBuffer();
		ArrayList selResult     		= new ArrayList() ;
		ArrayList<String> keys 			= new ArrayList<String>();
		
		try
		{
			selQry.append(" SELECT am.attributeValue , ");
			selQry.append(" km.keyValue	, am.secureFlag, am.attId		");
			selQry.append("	FROM AttributeMaster am, AttributeKeyMaster km ");
			selQry.append(" WHERE attributeName = ? ");
			selQry.append(" and am.attId = km.attId ");
			selQry.append(" and am.groupId = ? ");
		    
			params.add(prpName);
			paramTypes.add(0);
			params.add(groupId);
			paramTypes.add(0);
			
			selResult = repository.getRepositoryItems(selQry, params, paramTypes, groupId);
			if(selResult != null && selResult.size() > 0)
			{
				String [] temp	= (String [])selResult.get(0);
				
				String prpEncryptedVal 	=	temp[0]; 
				String dbSecureKey 		=	temp[1];
				String dbSecureFlag		=	temp[2];
				String dbAttID			=	temp[3];
				
				String fileSecuredKey 	= getKeyFromProp(dbAttID, groupId);
				byte[] fileDecodedByte 	= Base64.decodeBase64(fileSecuredKey);
				fileSecuredKey 			= new String(fileDecodedByte);
				
				byte[] dbDecodedByte 	= Base64.decodeBase64(dbSecureKey);
				dbSecureKey 			= new String(dbDecodedByte);
				
				keys.add(dbSecureKey + fileSecuredKey); //Password
				keys.add(fileSecuredKey + dbSecureKey); //IV Vector
				keys.add(prpEncryptedVal); 
				keys.add(dbSecureFlag);
				
				return keys;
			}
			
		}
		catch(Throwable t)
		{
			Logger.log("Error in getDecryptionKeys.. " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", t, Logger.CRITICAL);
		}
		finally
		{
			selQry	    = null;
			params      = null;
			paramTypes	= null;
		}
		
		return keys;
	}
	
	public String getKeyFromProp(String prpName, String groupId)
	{
		String propVal = "";
		try
		{
			propVal = EnvProperties.getPropertyValue(propFileName, prpName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Logger.log("Error getKeyFromProp " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", e, Logger.CRITICAL);
		}
		return propVal;
	}
	
	public String getDatabaseKeyPart()
	{
		String shortDBUUID = RandomStringUtils.random(8, UUID.randomUUID().toString());
		return shortDBUUID;
	}
	
	public String getFileKeyPart()
	{
		String shortFileUUID = RandomStringUtils.random(8, UUID.randomUUID().toString());
		return shortFileUUID;
	}
	
	public String encrypt(String groupId, String prpName, String prpValue, String secureFlag, String remarks, String isActive, String createdBy) 
	{
		String encryptedKey = "";
	    try 
	    {
	    	if(secureFlag.equalsIgnoreCase("y"))
	    	{
		    	ArrayList<String> keys = getEncryptionKeys();
		        IvParameterSpec iv = new IvParameterSpec(keys.get(0).getBytes("UTF-8"));
		        SecretKeySpec skeySpec = new SecretKeySpec(keys.get(1).getBytes("UTF-8"), "AES");
		
		        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		
		        byte[] encrypted = cipher.doFinal(prpValue.getBytes());
		        
		        encryptedKey =  Base64.encodeBase64String(encrypted);
	    	}
	    	else
	    		encryptedKey = prpValue;
	        
	        int insertResult = addKeysInMaster(groupId, prpName, encryptedKey, secureFlag, remarks, isActive, createdBy);
	        if(insertResult > 0 )
	        {
	        	int result = updateKeyMaster(prpName, isActive, createdBy, groupId);
	        	if(result > 0 )
	        	{
	        		updatePropFile(prpName, groupId);
	        	}
	        }
	    }
	    catch (Exception ex) 
	    {
	        ex.printStackTrace();
	        Logger.log("Error in encrypt " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", ex, Logger.CRITICAL);
	    }
	    
	    return encryptedKey;
	}
	
	public void updatePropFile(String prpName, String groupId)
	{
		try
		{
			String data = "";
			String dbAttID = "";
			if (!new File(propFileName).exists()) 
			{
				FileOutputStream out1 = new FileOutputStream(propFileName);
				out1.write(data.getBytes());
				out1.close();
			}
			
			ArrayList<Object> params		= new ArrayList<Object>();
			ArrayList<Integer> paramTypes	= new ArrayList<Integer>();
			StringBuffer selQry 			= new StringBuffer();
			ArrayList selResult     		= new ArrayList() ;
			
			try
			{
				selQry.append(" SELECT  				");
				selQry.append(" am.attId				");
				selQry.append("	FROM AttributeMaster am ");
				selQry.append(" WHERE attributeName = ? ");
				selQry.append(" AND groupId = ? ");
			    
				params.add(prpName);
				paramTypes.add(0);
				params.add(groupId);
				paramTypes.add(0);
				
				selResult = repository.getRepositoryItems(selQry, params, paramTypes, groupId);

				if(selResult != null && selResult.size() > 0)
				{
					String [] temp	=	(String [])selResult.get(0);
					dbAttID			=	temp[0];
				}
				
			}
			catch(Throwable t)
			{
				Logger.log("Error updatePropFile " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", t, Logger.CRITICAL);
			}
			finally
			{
				selQry	    = null;
				params      = null;
				paramTypes	= null;
			}
			
			if(!dbAttID.equals(""))
			{
				FileInputStream in = new FileInputStream(propFileName);
				Properties props = new Properties();
				props.load(in);
				in.close();
	
				FileOutputStream out = new FileOutputStream(propFileName);
				props.setProperty(dbAttID, fileKey);
				props.store(out, null);
				out.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger.log("Error updatePropFile 2 " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", e, Logger.CRITICAL);
		}
	}

	public int updateKeyMaster(String prpName, String isActive, String createdBy, String groupId) 
	{
		int insertResult = -1;

		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Integer> paramTypes = new ArrayList<Integer>();
		StringBuffer selQry = new StringBuffer();
		ArrayList selResult = new ArrayList();

		try 
		{
			selQry.append(" SELECT attId			");
			selQry.append("	FROM AttributeMaster	");
			selQry.append(" WHERE attributeName = ? ");
			selQry.append(" AND groupId = ? 		");

			params.add(prpName);
			paramTypes.add(0);
			params.add(groupId);
			paramTypes.add(0);

			selResult = repository.getRepositoryItems(selQry, params, paramTypes, groupId);
			if (selResult != null && selResult.size() > 0) {
				String[] temp = (String[]) selResult.get(0);

				String insertQuery = "insert into AttributeKeyMaster (attId, keyValue, isActive, createdBy, createdDate) values (";

				ArrayList<Object> insParams = new ArrayList<Object>();
				ArrayList<Integer> insParamTypes = new ArrayList<Integer>();
				StringBuffer insQry = new StringBuffer();

				try 
				{
					insQry.append(insertQuery);
					insQry.append("?, ?, ?, ?, getDate() ");
					insQry.append(" ) ");

					insParams.add(temp[0]);
					insParamTypes.add(0);
					insParams.add(dbKey);
					insParamTypes.add(0);
					insParams.add(isActive);
					insParamTypes.add(0);
					insParams.add(createdBy);
					insParamTypes.add(0);

					insertResult = repository.executeQuery(insQry, insParams, insParamTypes, groupId);
				} 
				catch (Throwable t) 
				{
					Logger.log("Error while inserting into AttributeKeyMaster .. " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", t, Logger.CRITICAL);
				} 
				finally 
				{
					insQry = null;
					insParams = null;
					insParamTypes = null;
				}
			}
		} 
		catch (Throwable t) 
		{
			Logger.log("Error in updateKeyMaster.. " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", t, Logger.CRITICAL);
		} 
		finally 
		{
			selQry = null;
			params = null;
			paramTypes = null;
		}
		return insertResult;
	}

	public int addKeysInMaster(String groupId, String prpName, String encryptedKey, String secureFlag, String remarks, String isActive, String createdBy) 
	{
		String insertQuery = "insert into AttributeMaster (groupId, attributeName, attributeValue, secureFlag, remarks, isActive, createdBy, createdDate) values (";

		ArrayList<Object> insParams 		= new ArrayList<Object>();
		ArrayList<Integer> insParamTypes 	= new ArrayList<Integer>();
		StringBuffer insQry 				= new StringBuffer();
		int insertResult 					= -1;

		try 
		{
			insQry.append(insertQuery);
			insQry.append("?, ?, ?, ?, ?, ?, ?, getDate()");
			insQry.append(" ) ");

			insParams.add(groupId);
			insParamTypes.add(0);
			insParams.add(prpName);
			insParamTypes.add(0);
			insParams.add(encryptedKey);
			insParamTypes.add(0);
			insParams.add(secureFlag);
			insParamTypes.add(0);
			insParams.add(remarks);
			insParamTypes.add(0);
			insParams.add(isActive);
			insParamTypes.add(0);
			insParams.add(createdBy);
			insParamTypes.add(0);

			insertResult = repository.executeQuery(insQry, insParams, insParamTypes, groupId);
		} 
		catch (Throwable t) 
		{
			Logger.log("Error while inserting in addKeysInMaster .. " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", t, Logger.CRITICAL);
		} 
		finally 
		{
			insQry = null;
			insParams = null;
			insParamTypes = null;
		}

		return insertResult;
	}

	public String getDecryptedKey(String prpName, String groupId) 
	{
		String decyptedVal = "";
		try 
		{
			ArrayList<String> keys = getDecryptionKeys(prpName, groupId);
			if(keys.get(3).trim().equalsIgnoreCase("Y"))
			{
				IvParameterSpec iv 		= new IvParameterSpec(keys.get(0).getBytes("UTF-8"));
				SecretKeySpec skeySpec 	= new SecretKeySpec(keys.get(1).getBytes(), "AES");
	
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	
				byte[] original = cipher.doFinal(Base64.decodeBase64(keys.get(2)));
	
				decyptedVal = new String(original);
			}
			else
				decyptedVal = keys.get(2);

		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Logger.log("Error in getDecryptedKey .. " + prpName + " - " + groupId, "AttributeManager.java", "AttributeManager", ex, Logger.CRITICAL);
		}

		return decyptedVal;
	}
}
