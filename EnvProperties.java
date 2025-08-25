/*
 * @(#)EnvProperties.java	01-Jan-2012
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenue.
 * Use is subject to license terms.
 *
 */

package com.api.remitGuru.component.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.api.remitGuru.component.environment.Constants;

/**
 * <BR> Used for retrieving the common properties for the project.
 * <BR> Retrieves data from an properties file -- remit.properties.
 * <BR>
 * <BR> This keeps the classes decoupled from the environment settings,
 *      for porting accross environments.
 * <BR>
 * @author		********
 * @version 	1.00, 01-Jan-2012
 */

public class EnvProperties {

	/*
     * Constructors
     */
	private EnvProperties () {
    }
		
	static Document doc;
	static Document XMLdocument;
	static String reloadGroupIdFlag ;
	static String reloadGroupId;
	
	static String reloadLocale;
	static ResourceBundle rb;		
	
	static HashMap<String, Document> map = new HashMap<String, Document>();
	
	static HashMap<String, ResourceBundle> locale_map = new HashMap<String, ResourceBundle>();
	
	static HashMap<String, String> propertiesMapFileWise = new HashMap<String, String>(); 
	static long lastModTimeFileWise = 0;
	static HashMap<String, String> propertiesMap = new HashMap<String, String>(); 
	static long lastModTime = 0;
	static HashMap<String, Long> timestampMapFileWise = new HashMap<String, Long>();

	static {
		try {
			System.out.println("******************In Env Properties**************");
		   	System.out.println("***** configFilePath : " + Constants.configFilePath);
	    	System.out.println("***** enabledLogs : " + Constants.enabledLogs);
	 
			FileInputStream file = new FileInputStream(Constants.configFilePath + "globalremit.xml");
			InputSource source = new InputSource(file);
			
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder parser=dbf.newDocumentBuilder();
			doc = parser.parse(source);
			
			parser = null;
			dbf = null;
			source = null;
			file.close();
			file = null;
		}
		catch (Exception e) {
			System.out.println("******************Error In Fetch Properties**************" + e.getMessage());
		}
	}
	
	/************************************************
	 * <br> Function Name: getProperties(String groupId)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from xml file
	 ******************************************/

	public static void getProperties(String groupId)
	{
		try 
		{
			String str 				= Constants.configFilePath + groupId+"remit.xml";
			FileInputStream file 	= new FileInputStream(str);
			InputSource source 		= new InputSource(file);
			
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder parser=dbf.newDocumentBuilder();
			
			XMLdocument 			= parser.parse(source);
			
			parser = null;
			dbf = null;
			source = null;
			file.close();
			file = null;
			str = null;
		}
		catch (Throwable t) 
		{
			Logger.log("Error in reading xml file method: getProperties(groupId) for groupId : " + groupId, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			XMLdocument = null;
		}
	}
	
	/************************************************
	 * <br> Function Name: getValue(String strName,String groupId)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from xml file
	 ******************************************/
	
	public static String getValue(String strName, String groupId) 
	{
		if(groupId==null || groupId.equalsIgnoreCase("null") || groupId.equals(""))
		{
			Logger.log("INFO EnvProperties.java groupId is NULL NodeName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getValue(strName);			
		}
		else
		{
			if(checkGroupId(groupId))
				groupId = groupId.toLowerCase();
			else
			{
				Logger.log("Error EnvProperties.java Not a valid groupId = " + groupId + " for strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return null;
			}
		}
	
		if(map.containsKey(groupId))
		{
			if(reloadGroupId != null && reloadGroupId.toLowerCase().equals(groupId))
			{
				getProperties(groupId);
				if(XMLdocument != null)
					map.put(groupId, XMLdocument);

				reloadGroupId = "";
			}
			else
			{
				XMLdocument	= (Document)map.get(groupId);
			}
		}
		else
		{
			getProperties(groupId);

			if(XMLdocument != null)
				map.put(groupId, XMLdocument);
			else
				Logger.log("INFO EnvProperties.java map containsKey not matching groupId  :  " + groupId + "   XMLdocument: "+XMLdocument, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		if(XMLdocument != null)
		{
			try
			{
				NodeList list = XMLdocument.getElementsByTagName(strName);
				Node node=list.item(0);
				NodeList children = node.getChildNodes();
				node = children.item(0);
				str = (node.getNodeValue()).trim();
				
				children = null;
				node = null;
				list = null;
			}
			catch (Exception e)
			{
				str = null;	
				Logger.log("In getValue(groupId): Node not found in " +groupId + "remit.xml for "+strName, "EnvProperties.java", "MissingNode", e, Logger.CRITICAL);
			}
		}
		else
		{
			Logger.log("INFO EnvProperties.java XML or TAG not Found  "+groupId+"remit.xml , NodeName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}
		
		return str;
	}

	/************************************************
	 * <br> Function Name: getValue
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from xml file
	 ******************************************/

	public static String getValue(String strName) {
		// Only one element will be returned.
		String str = null;
		try
		{
			NodeList list = doc.getElementsByTagName(strName);
			Node node = list.item(0);
			NodeList children = node.getChildNodes();
			node = children.item(0);
			str = node.getNodeValue();

			children = null;
			node = null;
			list = null;				
		}
		catch (Exception e)
		{
			str = null;	
			Logger.log("In getValue(): Node not found in globalremit.xml for "+strName, "EnvProperties.java", "MissingNode", e, Logger.CRITICAL);
		}

		return (str != null ? str.trim() : str);
	}

	/**********************************************************************************************
	 * Method Description: This method reads the property file and return the value of
	 * the given property.
	 * Method List: Properties#getProperty(java.lang.String)
	 * Created Date: 01-Jan-2012
	 * Created By:  
	 * Version:	1.00
	 *********************************************************************************************/
	
	public static String getPropertyValue(String propName)
	{
		Properties props = new Properties();
		String propValue = null;

		try
		{
			File propertyFile = new File(Constants.configFilePath + "remit.properties");
			long modTime = propertyFile.lastModified();
		
			if(modTime != lastModTime)
			{
				lastModTime = modTime;
				propertiesMap.clear();
			}
			if(propertiesMap.get(propName) != null)
				return propertiesMap.get(propName);
			
			FileInputStream fis = new FileInputStream(propertyFile);
			props.load(fis);

			if(props.getProperty(propName) != null)
			{
				propValue = props.getProperty(propName);
				propertiesMap.put(propName, propValue);
			}
			else
				propertiesMap.put(propName, null);
			
			fis.close();			
			fis = null;
		}
		catch(IOException ioe)
		{
		   ioe.printStackTrace();
		   return null;
		}
		
		props = null;
		
		return propValue;
	}
	
	/**********************************************************************************************
	 * Method Description: This method reads the given property file and return the value of
	 * the given property.
	 * Method List: Properties#getProperty(java.lang.String, java.lang.String)
	 * Created Date: 01-Jan-2012
	 * Created By:  
	 * Version:	1.00
	 *********************************************************************************************/
	
	public static String getPropertyValue(String fileName, String propName)
	{
		Properties props = new Properties();
		String propValue = null;

		try
		{
			File propertyFile = new File(fileName);
			String strFileName = propertyFile.getName();
			long modTime = propertyFile.lastModified();
			long lastModTimeFileWise = 0;
			if(timestampMapFileWise.get(strFileName+"~lastModified") != null)
				lastModTimeFileWise = timestampMapFileWise.get(strFileName+"~lastModified");
			
			if(modTime != lastModTimeFileWise)
			{
				timestampMapFileWise.put(strFileName+"~lastModified", modTime);
				propertiesMapFileWise.clear();
			}
			if(propertiesMapFileWise.get(strFileName+"~"+propName) != null)
				return propertiesMapFileWise.get(strFileName+"~"+propName);
			
			FileInputStream fis = new FileInputStream(propertyFile);
			props.load(fis);

			if(props.getProperty(propName) != null)
			{
				propValue = props.getProperty(propName);
				propertiesMapFileWise.put(strFileName+"~"+propName, propValue);
			}
			else
				propertiesMapFileWise.put(strFileName+"~"+propName, null);
			
			fis.close();			
			fis = null;
		}
		catch(IOException ioe)
		{
		   ioe.printStackTrace();
		   return null;
		}
		
		props = null;
		
		return propValue;
	}
	
	public static void reloadreloadGroupIdFlag(String reloadGroupIdFlag, String reloadGroupId) 
	{
		EnvProperties.reloadGroupIdFlag	= reloadGroupId; 
		
		if(reloadGroupIdFlag != null && reloadGroupIdFlag.equals("reloadpartner"))
			EnvProperties.reloadGroupId 	= reloadGroupId; 
		
		System.out.println("reloadGroupIdFlag : " + reloadGroupIdFlag + " reloadGroupId : " + reloadGroupId) ;
	}
	
	/*
     * This is used for validation of group Id
     *
     */
	public static boolean checkGroupId(String value) {
		
		boolean isGroupIdProper = true;
		
		String validStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int strLen = value.length();		
		char charArray[] = value.toCharArray();
		
		if(value.length() > 12)
		{
			return isGroupIdProper = false;
		}
		
		for(int i=0;i<strLen;i++) {
			
			if((validStr.indexOf(charArray[i]))== -1) {
								
				return isGroupIdProper = false;
			}
		}
		
		validStr = null;
	
		return isGroupIdProper;
	}

	//Code For Internationalization
	/************************************************
	 * <br> Function Name: getProperties(String groupId)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from xml file
	 * 
	 * Methods :
	 * public static String getFieldName(String strName, String locale); 
	 * public static String getPageHeader(String strName, String locale); 
	 * public static String getMessage(String strName, String locale);
     * public static String getMenuPanelMessage(String strName, String locale);
     * 
     * public static String getErrorMessage(String strName, String locale); 
     * public static String getErrorMessage(String strName, String paramName, String paramValue, String locale); 
	 * public static String getErrorMessage(String strName, Map<String, ?> paramValues, String locale);
	 ******************************************/

	public static void getLocaleProperties(String fileName, Locale locale)
	{
		try 
		{	
			Logger.log("in getLocaleProperties Reading locale file : " + fileName + " locale : " + locale, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			File file = new File(Constants.configFilePath + "messages");   
			URL[] urls = {file.toURI().toURL()};   
			ClassLoader loader = new URLClassLoader(urls);   
			rb = ResourceBundle.getBundle(fileName, locale, loader);  

			//rb		= ResourceBundle.getBundle(Constants.configFilePath + "messages\\" + fileName, locale);
		}
		catch (Throwable t) 
		{
			Logger.log("Error in reading locale file method: getLocaleProperties(fileName, locale) for fileName : " + fileName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			rb = null;
		}
	}
	
	/************************************************
	 * <br> Function Name: getFieldName(String strName,String locale)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from FieldNames locale
	 ******************************************/
	
	public static String getFieldName(String strName, String locale) 
	{
		if(locale == null || locale.equalsIgnoreCase("null") || locale.equals(""))
		{
			Logger.log("INFO getFieldName EnvProperties.java locale is NULL KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getFieldName(strName,"en");			
		}
		else if(strName.indexOf("label.") == -1)
		{
			Logger.log("INFO getFieldName KEY Value without label. locale is : " + locale + " KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return strName;
		}
		else
		{
			if(checkLocale(locale))
				locale = locale.toLowerCase();
			else
			{
				Logger.log("Error getFieldName EnvProperties.java Not a valid locale = " + locale + " for KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return "";
			}
		}
		
		if(locale_map.containsKey("FieldNames_" + locale))
		{
			if(reloadLocale != null && reloadLocale.equals("FieldNames_" + locale))
			{				
				getLocaleProperties("FieldNames",new Locale(locale));
				if(rb != null)
					locale_map.put("FieldNames_" + locale, rb);

				reloadLocale = "";
			}
			else
			{
				rb	= (ResourceBundle)locale_map.get("FieldNames_" + locale);
			}
		}
		else
		{	
			getLocaleProperties("FieldNames",new Locale(locale));

			if(rb != null)
				locale_map.put("FieldNames_" + locale, rb);
			else
				Logger.log("INFO getFieldName EnvProperties.java locale_map containsKey not matching locale  :  " + locale + "   rb : " + rb, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		
		try
		{
			if(rb != null)
			{
				str = rb.getString(strName);
			}
			else
			{
				str = strName;
				Logger.log("INFO getFieldName EnvProperties.java KEY not Found  "+ locale + ", strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error getFieldName in reading FieldNames_ key value for strName : " + strName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			str = strName;
		}
		
		if(str == null)
			str = strName;
		
		if(str == null)
			str = "";
		
		return str.trim();
	}	
	
	/************************************************
	 * <br> Function Name: getPageHeader(String strName,String locale)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from PageHeaders locale
	 ******************************************/
	
	public static String getPageHeader(String strName, String locale) 
	{
		if(locale == null || locale.equalsIgnoreCase("null") || locale.equals(""))
		{
			Logger.log("INFO getPageHeader EnvProperties.java locale is NULL KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getPageHeader(strName,"en");			
		}
		else
		{
			if(checkLocale(locale))
				locale = locale.toLowerCase();
			else
			{
				Logger.log("Error getPageHeader EnvProperties.java Not a valid locale = " + locale + " for KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return "";
			}
		}
	
		if(locale_map.containsKey("PageHeaders_" + locale))
		{
			if(reloadLocale != null && reloadLocale.equals("PageHeaders_" + locale))
			{
				getLocaleProperties("PageHeaders",new Locale(locale));
				if(rb != null)
					locale_map.put("PageHeaders_" + locale, rb);

				reloadLocale = "";
			}
			else
			{
				rb	= (ResourceBundle)locale_map.get("PageHeaders_" + locale);
			}
		}
		else
		{
			getLocaleProperties("PageHeaders",new Locale(locale));

			if(rb != null)
				locale_map.put("PageHeaders_" + locale, rb);
			else
				Logger.log("INFO getPageHeader EnvProperties.java locale_map containsKey not matching locale  :  " + locale + "   rb : " + rb, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		
		try
		{
			if(rb != null)
			{
				str = rb.getString(strName);
			}
			else
			{
				str = strName;
				Logger.log("INFO getPageHeader EnvProperties.java KEY not Found  "+ locale + ", strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error getPageHeader in reading PageHeaders_ key value for strName : " + strName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			str = strName;
		}
		
		if(str == null)
			str = strName;
		
		if(str == null)
			str = "";
		
		return str.trim();
	}
	
	/************************************************
	 * <br> Function Name: getMessage(String strName,String locale)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from Messages locale
	 ******************************************/
	
	public static String getMessage(String strName, String locale) 
	{
		if(locale == null || locale.equalsIgnoreCase("null") || locale.equals(""))
		{
			Logger.log("INFO getMessage EnvProperties.java locale is NULL KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getMessage(strName,"en");			
		}
		else
		{
			if(checkLocale(locale))
				locale = locale.toLowerCase();
			else
			{
				Logger.log("Error getMessage EnvProperties.java Not a valid locale = " + locale + " for KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return "";
			}
		}
	
		if(locale_map.containsKey("Messages_" + locale))
		{
			if(reloadLocale != null && reloadLocale.equals("Messages_" + locale))
			{
				getLocaleProperties("Messages",new Locale(locale));
				if(rb != null)
					locale_map.put("Messages_" + locale, rb);

				reloadLocale = "";
			}
			else
			{
				rb	= (ResourceBundle)locale_map.get("Messages_" + locale);
			}
		}
		else
		{
			getLocaleProperties("Messages",new Locale(locale));

			if(rb != null)
				locale_map.put("Messages_" + locale, rb);
			else
				Logger.log("INFO getMessage EnvProperties.java locale_map containsKey not matching locale  :  " + locale + "   rb : " + rb, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		
		try
		{
			if(rb != null)
			{
				str = rb.getString(strName);
			}
			else
			{
				str = strName;
				Logger.log("INFO getMessage EnvProperties.java KEY not Found  "+ locale + ", strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error getMessage in reading Messages_ key value for strName : " + strName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			str = strName;
		}
		
		if(str == null)
			str = strName;
		
		if(str == null)
			str = "";
		
		return str.trim();
	}
	
	/************************************************
	 * <br> Function Name: getMenuPanelMessage(String strName,String locale)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from Messages locale
	 ******************************************/
	
	public static String getMenuPanelMessage(String strName, String locale) 
	{
		if(locale == null || locale.equalsIgnoreCase("null") || locale.equals(""))
		{
			Logger.log("INFO getMenuPanelMessage EnvProperties.java locale is NULL KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getMenuPanelMessage(strName,"en");			
		}
		else
		{
			if(checkLocale(locale))
				locale = locale.toLowerCase();
			else
			{
				Logger.log("Error getMenuPanelMessage EnvProperties.java Not a valid locale = " + locale + " for KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return "";
			}
		}
	
		if(locale_map.containsKey("MenuPanelMessages_" + locale))
		{
			if(reloadLocale != null && reloadLocale.equals("MenuPanelMessages_" + locale))
			{
				getLocaleProperties("MenuPanelMessages",new Locale(locale));
				if(rb != null)
					locale_map.put("MenuPanelMessages_" + locale, rb);

				reloadLocale = "";
			}
			else
			{
				rb	= (ResourceBundle)locale_map.get("MenuPanelMessages_" + locale);
			}
		}
		else
		{
			getLocaleProperties("MenuPanelMessages",new Locale(locale));

			if(rb != null)
				locale_map.put("MenuPanelMessages_" + locale, rb);
			else
				Logger.log("INFO getMenuPanelMessage EnvProperties.java locale_map containsKey not matching locale  :  " + locale + "   rb : " + rb, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		
		try
		{
			if(rb != null)
			{
				str = rb.getString(strName);
			}
			else
			{
				str = strName;
				Logger.log("INFO getMenuPanelMessage EnvProperties.java KEY not Found  "+ locale + ", strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error getMenuPanelMessage in reading Messages_ key value for strName : " + strName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			str = strName;
		}
		
		if(str == null)
			str = strName;
		
		if(str == null)
			str = "";
		
		return str.trim();
	}
	
	
	/************************************************
	 * <br> Function Name: getErrorMessage(String strName,String locale)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from ErrorMessages locale
	 ******************************************/
	
	public static String getErrorMessage(String strName, String locale) 
	{
		if(locale == null || locale.equalsIgnoreCase("null") || locale.equals(""))
		{
			Logger.log("INFO getErrorMessage2 EnvProperties.java locale is NULL KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getErrorMessage(strName,"en");			
		}
		else
		{
			if(checkLocale(locale))
				locale = locale.toLowerCase();
			else
			{
				Logger.log("Error getErrorMessage2 EnvProperties.java Not a valid locale = " + locale + " for KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return "";
			}
		}
	
		if(locale_map.containsKey("ErrorMessages_" + locale))
		{
			if(reloadLocale != null && reloadLocale.equals("ErrorMessages_" + locale))
			{
				getLocaleProperties("ErrorMessages",new Locale(locale));
				if(rb != null)
					locale_map.put("ErrorMessages_" + locale, rb);

				reloadLocale = "";
			}
			else
			{
				rb	= (ResourceBundle)locale_map.get("ErrorMessages_" + locale);
			}
		}
		else
		{
			getLocaleProperties("ErrorMessages",new Locale(locale));

			if(rb != null)
				locale_map.put("ErrorMessages_" + locale, rb);
			else
				Logger.log("INFO getErrorMessage2 EnvProperties.java locale_map containsKey not matching locale  :  " + locale + "   rb : " + rb, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		
		try
		{
			if(rb != null)
			{
				str = rb.getString(strName);
			}
			else
			{
				str = strName;
				Logger.log("INFO getErrorMessage2 EnvProperties.java KEY not Found  "+ locale + ", strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error getErrorMessage2 in reading ErrorMessages_ key value for strName : " + strName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			str = strName;
		}
		
		if(str == null)
			str = strName;
		
		if(str == null)
			str = "";
		
		return str.trim();
	}	
	
	/************************************************
	 * <br> Function Name: getErrorMessage(String strName, String paramName, String paramValue, String locale)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from ErrorMessages locale
	 ******************************************/
	
	public static String getErrorMessage(String strName, String paramName, String paramValue, String locale) 
	{
		if(locale == null || locale.equalsIgnoreCase("null") || locale.equals(""))
		{
			Logger.log("INFO getErrorMessage4 EnvProperties.java locale is NULL KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getErrorMessage(strName,paramName,paramValue,"en");			
		}
		else
		{
			if(checkLocale(locale))
				locale = locale.toLowerCase();
			else
			{
				Logger.log("Error getErrorMessage4 EnvProperties.java Not a valid locale = " + locale + " for KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return "";
			}
		}
	
		if(locale_map.containsKey("ErrorMessages_" + locale))
		{
			if(reloadLocale != null && reloadLocale.equals("ErrorMessages_" +locale))
			{
				getLocaleProperties("ErrorMessages",new Locale(locale));
				if(rb != null)
					locale_map.put("ErrorMessages_" + locale, rb);

				reloadLocale = "";
			}
			else
			{
				rb	= (ResourceBundle)locale_map.get("ErrorMessages_" + locale);
			}
		}
		else
		{
			getLocaleProperties("ErrorMessages",new Locale(locale));

			if(rb != null)
				locale_map.put("ErrorMessages_" + locale, rb);
			else
				Logger.log("INFO getErrorMessage4 EnvProperties.java locale_map containsKey not matching locale  :  " + locale + "   rb : " + rb, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		
		try
		{
			if(rb != null)
			{
				str = rb.getString(strName);

				str = TemplateFormatter.renderMessage(str, paramName, paramValue);
			}
			else
			{
				str = strName;
				Logger.log("INFO getErrorMessage4 EnvProperties.java KEY not Found  "+ locale + ", strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error getErrorMessage4 in reading ErrorMessages_ key value for strName : " + strName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			str = strName;
		}
		
		if(str == null)
			str = strName;
		
		if(str == null)
			str = "";
		
		return str.trim();
	}
	
	/************************************************
	 * <br> Function Name: getErrorMessage(String strName, Map<String, ?> paramValues, String locale)
	 * <br> param  : strName
	 * <br> return : String
	 * <br> Description: gets the value for given attribute from ErrorMessages locale
	 ******************************************/
	
	public static String getErrorMessage(String strName, Map<String, ?> paramValues, String locale) 
	{
		if(locale == null || locale.equalsIgnoreCase("null") || locale.equals(""))
		{
			Logger.log("INFO getErrorMessage3 EnvProperties.java locale is NULL KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			return getErrorMessage(strName,paramValues,"en");			
		}
		else
		{
			if(checkLocale(locale))
				locale = locale.toLowerCase();
			else
			{
				Logger.log("Error getErrorMessage3 EnvProperties.java Not a valid locale = " + locale + " for KeyName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.CRITICAL);
				return "";
			}
		}
	
		if(locale_map.containsKey("ErrorMessages_" + locale))
		{
			if(reloadLocale != null && reloadLocale.equals("ErrorMessages_" +locale))
			{
				getLocaleProperties("ErrorMessages",new Locale(locale));
				if(rb != null)
					locale_map.put("ErrorMessages_" + locale, rb);

				reloadLocale = "";
			}
			else
			{
				rb	= (ResourceBundle)locale_map.get("ErrorMessages_" + locale);
			}
		}
		else
		{
			getLocaleProperties("ErrorMessages",new Locale(locale));

			if(rb != null)
				locale_map.put("ErrorMessages_" + locale, rb);
			else
				Logger.log("INFO getErrorMessage3 EnvProperties.java locale_map containsKey not matching locale  :  " + locale + "   rb : " + rb, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
		}

		String str = null;
		
		try
		{
			if(rb != null)
			{
				str = rb.getString(strName);

				str = TemplateFormatter.renderMessage(str, paramValues);
			}
			else
			{
				str = strName;
				Logger.log("INFO getErrorMessage3 EnvProperties.java KEY not Found  "+ locale + ", strName = " + strName, "EnvProperties.java", "EnvProperties", null, Logger.INFO);
			}
		}
		catch (Throwable t) 
		{
			Logger.log("Error getErrorMessage3 in reading ErrorMessages_ key value for strName : " + strName + " locale : " + locale, "EnvProperties.java", "EnvProperties", t, Logger.ERROR);
			str = strName;
		}
		
		if(str == null)
			str = strName;
		
		if(str == null)
			str = "";
		
		return str.trim();
	}
	
	/*
     * This is used for validation of group Id
     *
     */
	public static boolean checkLocale(String locale) {
		
		boolean isLocaleProper = true;
		
		String validStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
		int strLen = locale.length();	
		
		if(locale.length() > 12)
		{
			return isLocaleProper = false;
		}
		
		char charArray[] = locale.toCharArray();

		for(int i=0;i<strLen;i++) {
			
			if((validStr.indexOf(charArray[i]))== -1) {
								
				return isLocaleProper = false;
			}
		}
		
		validStr = null;
	
		return isLocaleProper;
	}
	
	public static void reloadreloadLocaleFlag(String reloadGroupIdFlag, String reloadLocale) 
	{
		if(reloadGroupIdFlag != null && reloadGroupIdFlag.equals("reloadpartner"))
			EnvProperties.reloadLocale 	= reloadLocale; 
		
		System.out.println("reloadGroupIdFlag : " + reloadGroupIdFlag + " reloadLocale : " + reloadLocale) ;
	}
	//End Code For Internationalization
	
	
	/*public static Properties appEnvProperties;

    static {

        try {

			appEnvProperties = new Properties();
            InputStream configStream = Class.forName("com.api.remitGuru.component.util.EnvProperties").getResourceAsStream("/config/remit.properties");
            appEnvProperties.load(configStream);
        }
        catch (Exception e) {
            System.out.println("Exception .. "+e);
        }
    }*/

    /**
     * Returns the value for the property from the xml property file.
     *
     */
    /*public static String getValue(String prop) {

		return appEnvProperties.getProperty(prop);
    }*/


    /**
     * Used to print the String representation of this object.
     *
     */
    public String toString () {

        return "This is EnvProperties File -- remit.properties";
    }


    /*
     * This is used for unit testing of this class.
     *
     */

	public static void main(String args[]) {

		try{
			String str = EnvProperties.getPropertyValue("project-name");
			System.out.println(str.trim());
			//str = EnvProperties.getValue("BaseURL");
			//System.out.println(str.trim());
			
			EnvProperties.reloadreloadLocaleFlag("reloadpartner", "FieldNames_en") ;
			
			str = EnvProperties.getFieldName("test1","en");
			System.out.println(str.trim());
			
			System.out.println(EnvProperties.getFieldName("test", "en"));
			
			System.out.println(getErrorMessage("constraint.NotBlank.violated","en"));
			
			/*JBossManagedConnectionPoolStatistics js = new JBossManagedConnectionPoolStatistics();
			System.out.println(" js.getName()   "  +  js.getName());
			
			JBossManagedConnectionPool js1 =  new JBossManagedConnectionPool();
			js1.flush();*/
		}
		catch(Exception e)
		{
			System.out.println("Exception e :" +e);
		}		
	}

}