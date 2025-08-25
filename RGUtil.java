package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.Security;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.zip.Adler32;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.api.remitGuru.component.util.dao.SQLRepository;
import com.api.remitGuru.web.controller.RGUserController;

public class RGUtil {
	
	SQLRepository repository;
	
	public RGUtil(){

		repository = new SQLRepository();
	}
	
	/**********************************************************************************
	*  Method Description:	This method is used to check the IP Details.
	*  Method List:
	*  List of JSPs associated with this class:	
	*  Updated Date:	18-May-2012
	*  Version:	1.00
	*  Author	:	
	***********************************************************************************/	
	public ArrayList getIPCountryStateCityZipData(ArrayList ipData) {

		ArrayList result 			 = new ArrayList();	
		RGUserController rgUserCntrl = new RGUserController();	
		try
		{			
			result = rgUserCntrl.getIPCountryStateCityZipData(ipData);					
		}
		catch(Throwable t)
		{
			Logger.log("Error in getIPCountryStateCityZipData for ipData : " + ipData, "RGUtil.java", "RGUtil", t, Logger.CRITICAL);
		}
		finally
		{
			rgUserCntrl = null;
		}
		
		return result;
	}
	
	public ArrayList checkForIPMismatch(ArrayList ipData)
	{
		ArrayList ipResult = new ArrayList();
		RGUserController rgUserCntrl = new RGUserController();
		
		try
		{
			ipResult = rgUserCntrl.checkForIPMismatch(ipData);		
		}
		catch(Throwable t)
		{
			Logger.log("Error in checkForIPMismatch for ipData : " +ipData, "RGUtil.java", "RGUtil", t, Logger.CRITICAL);
		}
		finally
		{
			rgUserCntrl = null;
		}
		
		return ipResult;
	}
	
	public boolean isGroupExists(String groupId)
	{
		RGUserController rgUserCntrl = new RGUserController();
		boolean isGroupIdProper = false;
		
		try
		{
			isGroupIdProper = rgUserCntrl.isGroupExists(groupId);
		}
		catch(Throwable t)
		{
			isGroupIdProper	= false;
		}
		
		return isGroupIdProper;
	}
	
	public int  checkValidDomain(String userEmailId)
	{
		String hostName="",Ip="",hostname="";
		try
		{
			String[] emailId= userEmailId.split("@");
			hostName= emailId[1];
			InetAddress addr = InetAddress.getByName(hostName);// Gets the fully qualified domain name for this IP address.
			Ip = addr.getHostAddress();
			hostname = addr.getHostName();
		}
		catch (Exception e)
		{
			try
			{
				InetAddress addr1 = InetAddress.getByName("www."+hostName);
				Ip = addr1.getHostAddress();
				hostname = addr1.getHostName();
			}
			catch(Exception ie)
			{
				try 
				{				
					Hashtable env = new Hashtable();
					env.put("java.naming.factory.initial","com.sun.jndi.dns.DnsContextFactory");
				    DirContext ictx = new InitialDirContext( env );
				    Attributes attrs = ictx.getAttributes( hostName, new String[] { "MX" });
				    Attribute attr = attrs.get( "MX" );
				    if( attr == null ) return( 0 );
				    return( attr.size() );
				}
				catch(Exception m)
				{
					Logger.log("invalid Domain : " + userEmailId, "RGUtil.java", "RGUtil", m, Logger.CRITICAL);
					return -1;
				}
			}		
		}	
		finally
		{
			hostName = null;
			Ip = null;
			hostname = null;
		}
		return 1;
	}
	
	public String calculateTimeZone(String sourceDate, String dateFormat, String fromTimeZone, String timeZone)
	{
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		Date date;

		try 
		{
			date = formatter.parse(sourceDate);
			cal.setTime(date);	
			
			fromTimeZone = fromTimeZone.replaceAll("\\+","");
			
			if(fromTimeZone.indexOf("-") > -1)
			{
				fromTimeZone = fromTimeZone.replaceAll("\\-","");
				cal.add(Calendar.HOUR,Integer.parseInt(""+fromTimeZone.split(":")[0]));
				cal.add(Calendar.MINUTE,Integer.parseInt(""+fromTimeZone.split(":")[1]));
			}
			else
			{
				cal.add(Calendar.HOUR,Integer.parseInt("-"+fromTimeZone.split(":")[0]));
				cal.add(Calendar.MINUTE,Integer.parseInt("-"+fromTimeZone.split(":")[1]));
			}
			
			//GMT for India is +05:30
			//cal.add(Calendar.HOUR,Integer.parseInt(("-5")));
			//cal.add(Calendar.MINUTE,Integer.parseInt(("-30")));

			timeZone = timeZone.replaceAll("\\+","");
		
			cal.add(Calendar.HOUR,Integer.parseInt(""+timeZone.split(":")[0]));
			cal.add(Calendar.MINUTE,Integer.parseInt(""+timeZone.split(":")[1]));
		} 
		catch (Throwable t) 
		{
			Logger.log("Error in calculateTimeZone  for : sourceDate : " + sourceDate + " dateFormat : " + dateFormat  + " fromTimeZone : " + fromTimeZone + "  timeZone : " + timeZone + " DATE : " +cal.getTime(), "RGUtil.java", "RGUtil", t, Logger.CRITICAL);
		}

		return formatter.format(cal.getTime());	
	}
	
	public boolean compareBetweenDate(String startDate, String endDate, String compareDate, String format)
	{		
    	try
    	{
    		SimpleDateFormat sdf = new SimpleDateFormat( format);
        	Date date1 = sdf.parse(startDate);
        	Date date2 = sdf.parse(endDate);
        	Date date3 = sdf.parse(compareDate);
 
        	if((date3.after(date1)|| date3.equals(date1)) && (date3.before(date2)|| date3.equals(date2))){
        		return true;	
        	}
    	}
    	catch(ParseException ex)
    	{
    		Logger.log("Error in compareBetweenDate  for : startDate : " + startDate + " endDate : " + endDate  + " compareDate : " + compareDate + "  format : " + format, "RGUtil.java", "RGUtil", ex, Logger.CRITICAL);
    	}
		
		return false;
	}
	
	public int calculateDaysDifference(String startDate, String endDate, String format)
	{
		int result = -1;
    	try
    	{
    		SimpleDateFormat sdf = new SimpleDateFormat( format);
        	Date date1 = sdf.parse(startDate);
        	Date date2 = sdf.parse(endDate);
			
			String diff =""+((date2.getTime() - date1.getTime())/(1000*60*60*24));
			result = Integer.parseInt(diff);

    	}
    	catch(Exception ex)
    	{
    		Logger.log("Error in calculateDaysDifference  for : startDate : " + startDate + " endDate : " + endDate  + "  format : " + format, "RGUtil.java", "RGUtil", ex, Logger.CRITICAL);
    	}
		
		return result;
	}
	
	public String[] splitInParts(String s, int partLength)
	{
	    int len = s.length();

	    // Number of parts
	    int nparts = (len + partLength - 1) / partLength;
	    String parts[] = new String[nparts];

	    // Break into parts
	    int offset= 0;
	    int i = 0;
	    while (i < nparts)
	    {
	        parts[i] = s.substring(offset, Math.min(offset + partLength, len));
	        offset += partLength;
	        i++;
	    }   
	    
	    return parts;
	}

	public String checkMaxLength(String data, int len)
	{
		return data.length() > len ? data.substring(0,len) : data;
	}
	
	public String[] postURL(String postURL, String URLString)
	{	
		String responseStrArray[] = new String[3];
		int responseCode  = 0;
		String responseMsg = "";
		String responseStr = "";

		try
		{
			Logger.log("postURL : " + postURL + " URLString : " + URLString, "RGUtil.java", "RGUtil", null, Logger.INFO);
			
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());


			DataOutputStream printout = null;
			BufferedReader	input = null;
			URL url = new URL(postURL);		
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//System.out.println("connection opened::");
			//conn.setRequestMethod("GET"); //GET is set by default, but using this method we can change it to POST.

			//Sets the value of the doInput field for this URLConnection to the specified value. 
			conn.setDoInput(true);
			
			// Sets the value of the doOutput field for this URLConnection to the specified value. Invoking the method setDoOutput(true) on the URLConnection, will always use the POST method.
			conn.setDoOutput(true);
			
			// No caching. Sets the value of the useCaches field of this URLConnection to the specified value.
			conn.setUseCaches(false);
			// Specify the content type. Sets the general request property
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			// Send POST output.
			printout = new DataOutputStream(conn.getOutputStream());
			String content = URLString;
			printout.writeBytes(content);
			printout.flush();
			printout.close();

			//Get response data.
			input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			responseStr = input.readLine();

			Logger.log("postURL responseStr : " + responseStr, "RGUtil.java", "RGUtil", null, Logger.INFO);
			
			conn.connect();
			responseCode = conn.getResponseCode();
			responseMsg = conn.getResponseMessage();

			responseStrArray[0] = responseCode + "";
			responseStrArray[1] = responseMsg;
			responseStrArray[2] = responseStr;

			Logger.log("postURL " + responseStrArray[0]+"\t"+responseStrArray[1]+"\t"+responseStrArray[2], "RGUtil.java", "RGUtil", null, Logger.INFO);
			//System.out.println("postURL : " + responseStrArray[0]+"\t"+responseStrArray[1]+"\t"+responseStrArray[2]);
		}
		catch(Throwable t)
		{
			Logger.log("Error in postURL." , "RGUtil.java", "RGUtil", t, Logger.CRITICAL);			
		}
		return responseStrArray;
	}
	
	public int pageFlowTracker(String url,String uri,String sessionId,String previousPgUrl,String QString,String ipAddress, String groupId)
	{
		RGUserController rgUserCntrl = new RGUserController();
		int result = -1;
		
		try
		{
			result = rgUserCntrl.pageFlowTracker(url,uri,sessionId,previousPgUrl,QString,ipAddress, groupId);
		}
		catch(Throwable t)
		{
			Logger.log("Error in pageFlowTracker. url :" + url + " uri :" + uri + " sessionId :"  + sessionId +  " previousPgUrl :" + previousPgUrl + " QString :" + QString + " ipAddress :"  + ipAddress, "RGUtil.java", "RGUtil", t, Logger.CRITICAL);	
		}
		finally
		{
			rgUserCntrl = null;
		}
		
		return result;
	}
	
	public int pageFlowTracker(ArrayList pageInfo)
	{
		RGUserController rgUserCntrl = new RGUserController();
		int result = -1;
		
		try
		{
			result = rgUserCntrl.pageFlowTracker(pageInfo);
		}
		catch(Throwable t)
		{
			Logger.log("Error in pageFlowTracker :", "RGUtil.java", "RGUtil", t, Logger.CRITICAL);	
		}
		finally
		{
			rgUserCntrl = null;
		}
		
		return result;		
	}

	public int pageFlowTrackerSecure(ArrayList pageInfo)
	{
		RGUserController rgUserCntrl = new RGUserController();
		int result = -1;
		
		try
		{
			result = rgUserCntrl.pageFlowTrackerSecure(pageInfo);
		}
		catch(Throwable t)
		{
			Logger.log("Error in pageFlowTrackerSecure :", "RGUtil.java", "RGUtil", t, Logger.CRITICAL);	
		}
		finally
		{
			rgUserCntrl = null;
		}
		
		return result;		
	}
	
	public ArrayList getPageFlowData(String sessionId, String isSecure, String groupId)
	{
		RGUserController rgUserCntrl = new RGUserController();
		ArrayList pfResult = new ArrayList();
		
		try
		{
			pfResult = rgUserCntrl.getPageFlowData(sessionId, isSecure, groupId);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getPageFlowData :", "RGUtil.java", "RGUtil", t, Logger.CRITICAL);	
		}
		finally
		{
			rgUserCntrl = null;
		}
		
		return pfResult;		
	}

	public int pageFlowTrackerUpdate(String sessionId,String bannerId,String pageId, String groupId)
	{
		RGUserController rgUserCntrl = new RGUserController();
		int result = -1;
		
		try
		{
			result = rgUserCntrl.updatePageFlowTracker( sessionId, bannerId, pageId, groupId) ;
		}
		catch(Throwable t)
		{
			Logger.log("Error in pageFlowTrackerUpdate. sessionId :"  + sessionId , "RGUtil.java", "RGUtil", t, Logger.CRITICAL);	
		}
		finally
		{
			rgUserCntrl = null;
		}
		
		return result;
	}

	public String calcChecksum(String checksumString) throws Exception 	
	{	
		Adler32 adl = new Adler32();                 
		adl.update(checksumString.getBytes());		

		String newChecksum = String.valueOf(adl.getValue());

		return newChecksum;
	}

	public String encryptRes(String checksumString, String WorkingKey, String decRequest)
	{
		String checkSum = "";
		try
		{
			checkSum = calcChecksum(checksumString + "|" + WorkingKey);
		}
		catch(Exception e)
		{
			Logger.log("error in calling calcChecksum ", "RGUtil.java", "RGUtil", e, Logger.CRITICAL);
		}

		decRequest += checkSum;
		
		Logger.log("URL to be Encrypted: "+decRequest, "RGUtil.java", "RGUtil", null, Logger.INFO);

		AesCryptUtil aesUtil=new AesCryptUtil(WorkingKey);
		String enRequest=aesUtil.encrypt(decRequest);

		Logger.log("Encrypted URL: "+enRequest, "RGUtil.java", "RGUtil", null, Logger.INFO);

		return enRequest;
	}
	
	public String calculateProcessingDate(String date, int n, boolean includeSunday, boolean includeSaturday, ArrayList holidays)
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		try
		{
			cal.setTime(df.parse(date));

			if(n > 0)
			{
				l1: while(n > 0)
				{
					cal.add(Calendar.DATE, 1);
		
					String actualDate = df.format(cal.getTime());
					if(holidays != null && holidays.size() > 0)
					{
						for(int k=0; k<holidays.size(); k++)
						{
							if(!(n > 1 && ((String[])holidays.get(k))[1].equals("IN"))) //only 4th day holiday in INDIA will be considered in processing.
							{
								if(actualDate.equals(((String[])holidays.get(k))[0]))
								{
									continue l1;
								}
							}
						}
					}
		
					if(includeSunday && !includeSaturday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
						n--;
					}
					else if(includeSaturday && !includeSunday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n--;
					}
					else if(includeSaturday && includeSunday)
					{
						n--;
					}
					else
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n--;
					}
				}
			}
			else if(n < 0)
			{			
				l1: while(n < 0)
				{
					cal.add(Calendar.DATE, -1);
		
					String actualDate = df.format(cal.getTime());
					if(holidays != null && holidays.size() > 0)
					{
						for(int k=0; k<holidays.size(); k++)
						{
							//if(!(n < 1 && ((String[])holidays.get(k))[1].equals("IN"))) //only 4th day holiday in INDIA will be considered in processing.
							//{
								if(actualDate.equals(((String[])holidays.get(k))[0]))
								{
									continue l1;
								}
							//}
						}
					}
		
					if(includeSunday && !includeSaturday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
						n++;
					}
					else if(includeSaturday && !includeSunday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n++;
					}
					else if(includeSaturday && includeSunday)
					{
						n++;
					}
					else
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n++;
					}
				}
			}
		}
		catch(Throwable t)
		{
			Logger.log("Error in calculateProcessingDate", "RGUtil.java", "RGUtil", t, Logger.CRITICAL);
		}

		return df.format(cal.getTime());
	}
	
	public String calculateProcessingDate(String date, int n, int h, boolean includeSunday, boolean includeSaturday, ArrayList holidays)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		String actualDate ="";

		Calendar cal=Calendar.getInstance();
		try
		{
			cal.setTime(sdf.parse(date));
			if(n > 0)
			{
				l1: while(n > 0)
				{
					cal.add(Calendar.DATE, 1);
		
					actualDate = sdf.format(cal.getTime());
					actualDate = actualDate.split(" ")[0];
					Logger.log(" actualDate : "+actualDate, "getTrackerandTransfer.jsp", "getTrackerandTransfer", null, Logger.INFO);
					if(holidays != null && holidays.size() > 0)
					{
						for(int k=0; k<holidays.size(); k++)
						{
							if(!(n > 1 && ((String[])holidays.get(k))[1].equals("IN"))) //only 4th day holiday in INDIA will be considered in processing.
							{
								if(actualDate.equals(((String[])holidays.get(k))[0]))
								{
									continue l1;
								}
							}
						}
					}
		
					if(includeSunday && !includeSaturday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
						n--;
					}
					else if(includeSaturday && !includeSunday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n--;
					}
					else if(includeSaturday && includeSunday)
					{
						n--;
					}
					else
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n--;
					}
				}
			}
			else if(n < 0)
			{			
				l1: while(n < 0)
				{
					cal.add(Calendar.DATE, -1);
		
					actualDate = sdf.format(cal.getTime());
					actualDate = actualDate.split(" ")[0];
					Logger.log(" actualDate : "+actualDate, "getTrackerandTransfer.jsp", "getTrackerandTransfer", null, Logger.INFO);
					if(holidays != null && holidays.size() > 0)
					{
						for(int k=0; k<holidays.size(); k++)
						{
							//if(!(n < 1 && ((String[])holidays.get(k))[1].equals("IN"))) //only 4th day holiday in INDIA will be considered in processing.
							//{
								if(actualDate.equals(((String[])holidays.get(k))[0]))
								{
									continue l1;
								}
							//}
						}
					}
		
					if(includeSunday && !includeSaturday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
						n++;
					}
					else if(includeSaturday && !includeSunday)
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n++;
					}
					else if(includeSaturday && includeSunday)
					{
						n++;
					}
					else
					{
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						n++;
					}
				}
			}
			
			cal.add(Calendar.HOUR,h);
		}
		catch(Throwable t)
		{
			Logger.log("Error in calculateProcessingDate", "RGUtil.java", "RGUtil", t, Logger.CRITICAL);
		}
		return sdf.format(cal.getTime());
	}
	
	public String getUserIdFromRGTN(String rgtn, String countryCode)
	{
		return rgtn.substring(0, 10);
	}
	
	public String getSrNoFromRGTN(String rgtn, String countryCode)
	{
		return rgtn.substring(10, 14);
	}
	
	public boolean validateUserId(String userId)
	{
		if(userId.length() == 10)
		{
			try
			{
				long u = Long.parseLong(userId);
			}
			catch(Throwable t)
			{
				Logger.log("UserId is not numeric", "RGUtil.java", "RGUtil", t, Logger.CRITICAL);
				return false;
			}
			
			// select query Sender
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String convertRupeesToWordsConst(int n, String ch)
	{
		String one[] = {" "," One"," Two"," Three"," Four"," Five"," Six"," Seven"," Eight"," Nine"," Ten"," Eleven"," Twelve"," Thirteen"," Fourteen","Fifteen"," Sixteen"," Seventeen"," Eighteen"," Nineteen"};
		String ten[] = {" "," "," Twenty"," Thirty"," Forty"," Fifty"," Sixty"," Seventy"," Eighty"," Ninety"};
		String str = "";

		if(n>19)
			str += ten[n/10]+one[n%10];
		else
			str += one[n];

		if(n>0)
			return str + ch + (ch.equals(" Hundred") ? " and" : "");
		else
			return "";
	}

	public String convertRupeesToWords(int n)
	{
		return (convertRupeesToWordsConst((n/1000000000)," Hundred")+" "+convertRupeesToWordsConst((n/10000000)%100," Crore")+" "+convertRupeesToWordsConst(((n/100000)%100)," Lakh")+" "+convertRupeesToWordsConst(((n/1000)%100)," Thousand")+" "+convertRupeesToWordsConst(((n/100)%10)," Hundred")+" "+convertRupeesToWordsConst((n%100)," "));
	}
	
	public SQLRepository getRepositoryObject()
	{
		 SQLRepository repository = null;
		
		 try
		 {
			repository = (SQLRepository)getClassInstance("com.api.remitGuru.web.controller.SQLRepositoryController");
			
			Logger.log("WEB SQLRepositoryController Call", "RGUtil.java", "RGUtil", null, Logger.INFO);
		 }
		 catch(Throwable ex)
		 {	
			//ex.printStackTrace();
			
			Logger.log("Error : Failed WEB SQLRepositoryController Call", "RGUtil.java", "RGUtil", ex, Logger.INFO);
			
			try
			{
				repository = (SQLRepository)getClassInstance("com.api.remitGuru.component.util.dao.SQLRepository");
				
				Logger.log("APP SQLRepository Call", "RGUtil.java", "RGUtil", null, Logger.INFO);
			}
			catch(Throwable e)
			{			
				e.printStackTrace();
				
				Logger.log("Error : Failed WEB/APP SQLRepository Call ", "RGUtil.java", "RGUtil", e, Logger.CRITICAL);
			}
		 }
		 
		 return repository;
	}
	
	public Object getClassInstance(String strClassPath) throws Throwable
	{
		Object obj = null;
			  
		Class c = Class.forName(strClassPath);			
				
		obj = c.newInstance();
		 
		return obj;
	}
	
	public SQLRepository getLogErrorInDBObject()
	{
		 SQLRepository repository = null;
		
		 try
		 {
			repository = (SQLRepository)getClassInstance("com.api.remitGuru.web.controller.SQLRepositoryController");
			
			System.out.println("WEB SQLRepositoryController Call : getLogErrorInDBObject");
		 }
		 catch(Throwable ex)
		 {
			 repository = null;
		 }
		 
		 return repository;
	}
	
	public String getFinancialYear()
	{
		String dateRange = "";

		Calendar todaysDate = Calendar.getInstance();
		int intTxnYear = todaysDate.get(Calendar.YEAR);	

		if((todaysDate.MONTH+1) > 3)
			dateRange = ("01-04-"+intTxnYear+ "~"+ "31-03-"+(intTxnYear+1));
		else
			dateRange = ("01-04-"+(intTxnYear - 1)+ "~"+ "31-03-"+intTxnYear);

		return dateRange;
	}
}
