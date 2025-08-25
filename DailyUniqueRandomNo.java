package com.api.remitGuru.component.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import com.api.remitGuru.component.util.Logger;

import com.api.remitGuru.component.environment.Constants;

public class DailyUniqueRandomNo {

	private static DailyUniqueRandomNo dailyUniqueObject;
	private DailyUniqueRandomNo()
	{
		//
	}
	
	public static synchronized DailyUniqueRandomNo getDailyUniqueRandomNoObject()
	{
		if(dailyUniqueObject == null)
		{
			dailyUniqueObject = new DailyUniqueRandomNo();
		}
		return dailyUniqueObject;
	}

	////to generate random numbers
	public synchronized String getRandomNumber()
	{
		String randomNumber = "";
		String todaysDate = "";
		String oldDate = "";
        //ArrayList<Integer> numbermap = new ArrayList<Integer>();
        try
        {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy");
            todaysDate = sdf.format(new java.util.Date());
			Logger.log("getRandomNumber() today's date : "+todaysDate, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        }
        catch(Throwable e)
        {
            Logger.log("getRandomNumber() error while getting the todays date","DailyUniqueRandomNo.java", "DailyUniqueRandomNo", e, Logger.CRITICAL);
            //throw new NullPointerException();
        }
        Logger.log("todays date in getRandomNumber() : "+todaysDate,"DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        
        if(!todaysDate.equals(""))
        {
        	//processing starts
        	//to fetch the old date from property file
        	oldDate = getOldDate() == null ? "":getOldDate();
        	Logger.log("getRandomNumber() Oldate from prpoerty file : "+oldDate, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        	if(!oldDate.equals(""))
			{
				if(oldDate.equals(todaysDate))
				{
					//read random number
					randomNumber = getData(todaysDate);
				}
				else
				{
					//generate newdata
					generateRandomNumber(todaysDate);
					String oldDate2 = getOldDate();
					Logger.log("getRandomNumber() Oldate from property file after refreshing  data : "+oldDate2,"DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
					//fetch the random number
					randomNumber = getData(todaysDate);
				}
			}
        	else
			{
				Logger.log("getRandomNumber() some error occured while fetching old date from property file : ", "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.CRITICAL);
			}
        }
        else
        {
        	Logger.log("getRandomNumber() some error occured while getting todays's.. so processing failed", "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.CRITICAL);
        }
        
        Logger.log("random number : " + randomNumber, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        //to formulate 9 digit number
        if(randomNumber != null && !randomNumber.equals(""))
        {
        	String endDigit = dayandYear();
        	randomNumber = randomNumber +endDigit;
        }else{
        	Logger.log("getRandomNumber() randnumber not generated ", "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.CRITICAL);
        }
        
        return randomNumber;
    }
	
	//to get the random number from property file
	public String getData(String todaysDate)
	{
		String randomNo = null;
		FileInputStream in = null;
		String number = "";
		try
		{
			in = new FileInputStream(Constants.configFilePath+"randomNumber.properties");
			//in = new FileInputStream("D://"+"randomNumber.properties");
			Properties props = new Properties();
			props.load(in);

			randomNo = props.getProperty("randomNo_"+todaysDate);
			//Logger.log("randomNo list : "+randomNo, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
			
			String data[] = randomNo.split(",");
			if(data != null && data.length > 0)
			{
				number = data[0];
			}
			Logger.log("random number fetched : "+number, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
			
			//deleting the top element
			ArrayList<Integer> updatedList = new ArrayList<Integer>();
			for(int i=1;i<data.length;i++)
			{
				updatedList.add(Integer.valueOf(data[i]));
			}
			Logger.log("size of data[] : "+data.length, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
			Logger.log("size of updatedList : "+updatedList.size(), "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
			if(updatedList.size() > 0)
			{
				//updating data
				updateAllPropertyFile(updatedList, todaysDate, todaysDate);
			}else
			{
				Logger.log("daily count arraylist empty ","DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.CRITICAL);
			}
		}
		catch(Throwable t)
		{
			Logger.log("Error in getPropertyFileData catch Block ","DailyUniqueRandomNo.java", "DailyUniqueRandomNo", t, Logger.CRITICAL);
		}
		finally
		{
			try
			{
				if(in != null)
				{
					in.close();
				}
			}
			catch(Throwable t)
			{
				Logger.log("Error in getPropertyFileData finally Block : ", "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", t, Logger.CRITICAL);
				//Logger.log("Error in getPropertyFileData finally Block", "generateRandomNumber.java", "generateRandomNumber", t, Logger.CRITICAL);
			}
		}
		return number;
	}
	
	////to read old date from property file
	public String getOldDate()
	{
		String strPropVal = null;
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(Constants.configFilePath+"randomNumber.properties");
			//in = new FileInputStream("D://"+"randomNumber.properties");
			Properties props = new Properties();
			props.load(in);

			strPropVal = props.getProperty("fileDate") == null ? "":props.getProperty("fileDate");
			Logger.log("getOldDate() oldDate : "+strPropVal,"DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
		}
		catch(Throwable t)
		{
			Logger.log("Error in getOldDate() ","DailyUniqueRandomNoj.sp", "DailyUniqueRandomNo", t, Logger.CRITICAL);
		}
		finally
		{
			try
			{
				if(in != null)
				{
					in.close();
				}
			}
			catch(Throwable t)
			{
				Logger.log("Error in getOldDate() finally Block : ","DailyUniqueRandomNo.java", "DailyUniqueRandomNo", t, Logger.CRITICAL);
			}
		}
		return strPropVal;
	}
	
	////to generate random number
	public void generateRandomNumber(String todaysDate)
	{
		String st = EnvProperties.getPropertyValue("Daily_Random_Start_No") == null ? "1001" : EnvProperties.getPropertyValue("Daily_Random_Start_No");//1001;
		String en = EnvProperties.getPropertyValue("Daily_Random_End_No") == null ? "10000" : EnvProperties.getPropertyValue("Daily_Random_End_No");//10000;
		
		int starting = Integer.parseInt("" + st); 	//1001;
        int ending   = Integer.parseInt("" + en);	//10000;
        //String todaysDate = "";
        ArrayList<Integer> numbermap = new ArrayList<Integer>();
        Logger.log("read the starting and ending point : "+starting+" and "+ending, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        Logger.log("todaysDate in generateRandomNumber : "+todaysDate, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        try
        {
            for(int i=starting;i<ending;i++)
            {
                numbermap.add(i);
            }
            Collections.shuffle(numbermap);
            //writing the arraylist in file
            updateAllPropertyFile(numbermap, todaysDate, todaysDate);
        }
        catch(Throwable e)
        {
            Logger.log("exception while inserting data into arraylist : ","DailyUniqueRandomNo.java", "DailyUniqueRandomNo", e, Logger.CRITICAL);
        }
	}
	
	//to write data in property file
	public void updateAllPropertyFile(ArrayList randomNoList,String randomNo,String todaysDate)
	{
		FileOutputStream out = null;		
		String randNo = "";
		try
		{
			out = new FileOutputStream(Constants.configFilePath+"randomNumber.properties");
			//out = new FileOutputStream("D://"+"randomNumber.properties");
			Logger.log("inside updateAllPropertyFile()", "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
			Properties props = new Properties();
			props.setProperty("fileDate", todaysDate);
			props.setProperty("count_"+todaysDate, randomNo);
			
			for(int i=0; i < randomNoList.size();i++)
			{
				String strPropValue = randomNoList.get(i).toString();
				randNo = strPropValue + ","+ randNo;
				//props.setProperty("randomNo_"+todaysDate, randNo);
			}
			//Logger.log("randNo : "+randNo, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
			StringBuilder sb = new StringBuilder(randNo);
			sb.deleteCharAt(sb.lastIndexOf(","));
			randNo = sb.toString();
			
			props.setProperty("randomNo_"+todaysDate, randNo);
			props.store(out, null);	
			Logger.log("updateAllPropertyFile() data stored in property file successfully", "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
			//out.close();		
		}
		catch(Throwable t)
		{
			Logger.log("error at updateAllPropertyFile()","DailyUniqueRandomNo.java", "DailyUniqueRandomNo", t, Logger.CRITICAL);
		}
		finally
		{
			try
			{
				if(out != null)
				{
					out.close();
				}
			}
			catch(Throwable t)
			{
				Logger.log("error in finally block", "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", t, Logger.CRITICAL);
			}
		}
	}
	
	//to formulate day and year
	public String dayandYear()
	{
		String dayYear = "";
		Date now = new Date();
		 
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        //Logger.log(simpleDateformat.format(now), "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
 
        simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        //Logger.log(simpleDateformat.format(now), "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        //Logger.log(calendar.get(Calendar.DAY_OF_MONTH), "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);// the day of the week in numerical format
        String dy = String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
        String yy = String.valueOf(calendar.get(Calendar.YEAR));
        
       // Logger.log(calendar.get(Calendar.YEAR), "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        
        StringBuilder sb = new StringBuilder(yy);
        sb.deleteCharAt(0);sb.deleteCharAt(0);
        yy = sb.toString();
       
        //Logger.log("length of dy :"+dy.length(), "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        if(dy.length() == 1){
        	dy= "00"+dy;
        }else if(dy.length() ==2){
        	dy="0"+dy;
        }else{
        	//no need to add 0
        }
        //Logger.log(dy+yy);
        dayYear = dy+yy;
        Logger.log("dayYear : "+dayYear, "DailyUniqueRandomNo.java", "DailyUniqueRandomNo", null, Logger.INFO);
        return dayYear;
	}
	
	/*public static void main(String []args)
	{
		DailyUniqueRandomNo dm = new DailyUniqueRandomNo();
		System.out.println("random : "+dm.getRandomNumber());
	}*/
}
