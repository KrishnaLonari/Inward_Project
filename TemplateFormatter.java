package com.api.remitGuru.component.util;

import java.util.ArrayList;
import java.util.Map;

public class TemplateFormatter {
	
	public TemplateFormatter() {
	}

	/**
	 * <p>Method replaces mail body with related details</p> 
	 *
     * Method Name 						: format( )
     * @param							  inStr String
	 * @param							  userData String Array
	 * 
	 *
     * @return							  The resultant String
	 */ 

	public static String format (String inStr, String[] userData)  
	{
		StringBuffer sb = new StringBuffer(inStr);
			
		String varStr = "";

	    for(int i=0; i<userData.length ; i++)
	    {
			int loc = 0;
            while(loc!=-1)
            {
				varStr = "{"+i+"}";
				loc = inStr.indexOf(varStr);
                if (loc !=-1)
                {
					sb.delete(loc,loc+varStr.length());
					sb.insert(loc,(String)userData[i]);
				}
                inStr =sb.toString();
             }
	    }
	    
	    if(sb!=null && sb.toString().indexOf("#if") > -1)
	    {
	    	sb = new StringBuffer(formatCondition(sb.toString()));
	    }
		
		return sb.toString();
	} 
	
	public static String formatCondition (String inStr ) 
	{		
		StringBuffer sb = new StringBuffer(inStr);
		String pcr = new String();

		int loc = 0,loc1=0;
		String varStr = "";
		String varStr1 = "";                
		
		 while(loc!=-1 && loc1!=-1 )
		 {			
			varStr = "#if";
			loc = inStr.indexOf(varStr);
			if (loc != -1)
			{
				varStr1 = "#end";
				loc1 = inStr.indexOf(varStr1);
				if (loc1 !=-1)
				{
					pcr = new String();					
					pcr = parseCondition(sb.substring(loc,loc1+varStr1.length()));
					sb = new StringBuffer(sb.substring(0,loc)    + pcr.trim() + sb.substring(loc1+varStr1.length(),sb.length()));
					inStr =sb.toString(); 
				}	
				
				inStr =sb.toString(); 						
			}

			loc = inStr.indexOf(varStr);
			loc1 = inStr.indexOf(varStr1);		
		 }		
		
		return sb.toString();
	}
	 
	private static String parseCondition(String expression) 
	{		
		String extractedExpression= expression; // expressionOg

		String x = "" , y = "";

		int ifCount = 0 , elseCount = 0 , elseIfCount = 0 ;  

		ArrayList<String> extractedExpressionList = new ArrayList<String>();

		while (expression.indexOf("#" ,expression.indexOf("#") + 1) > -1)
		{
			extractedExpression = expression.substring(expression.indexOf("#") , expression.indexOf("#" ,expression.indexOf("#") + 1) );
			expression = expression.substring(extractedExpression.length() , expression.length());
						
			extractedExpressionList.add(extractedExpression);
						
			if(extractedExpression.indexOf("#if") > -1)
				ifCount++;
			if(extractedExpression.indexOf("#else") > -1 && extractedExpression.indexOf("#elseif") == -1 )
				elseCount++;
			if(extractedExpression.indexOf("#elseif") > -1)
				elseIfCount++;	
		}	

		StringBuffer mailContent = new StringBuffer("");

		String op = "";

		if(ifCount != 1 || elseCount > 1) // more than one if / else if
		{
			mailContent.append("");
			return "";
		}

		for(String expTemp : extractedExpressionList)
		{				
			if(expTemp.indexOf("==") > -1)
			{
				x = expTemp.substring(expTemp.indexOf("(") + 1, expTemp.indexOf("==")).trim();
				y = expTemp.substring(expTemp.indexOf("==") + 2, expTemp.indexOf(")")).trim();
				 
				op = expTemp.substring(expTemp.indexOf(")") + 1, expTemp.length());
				
				if(x.equals(y))
				{
					mailContent.append(op);
					break ;
				}
			}
			else if(expTemp.indexOf("!=") > -1)
			{
				x = expTemp.substring(expTemp.indexOf("(") + 1, expTemp.indexOf("!=")).trim();
				y = expTemp.substring(expTemp.indexOf("!=") + 2, expTemp.indexOf(")")).trim();
				
				op = expTemp.substring(expTemp.indexOf(")") + 1, expTemp.length());
				
				if(!x.equals(y))
				{
					mailContent.append(op);
					break ;
				}
			}
			else
			{
				op = expTemp.substring(expTemp.indexOf("#else") + "#else".length() , expTemp.length());
				mailContent.append(op);
				break ;
			}
		}		

		return mailContent.toString();
	}
	 public static String renderMessage(String message, Map<String, ?> messageValues)
	 {
		message = message.replaceAll("&#123;", "{").replaceAll("&#125;", "}");	
			
	 	if (message.indexOf('{') == -1) return message;

	 	if ((messageValues != null) && (messageValues.size() > 0))
	 	{
	 		for (Map.Entry entry : messageValues.entrySet())
	 			message = message.replaceAll("\\{" + (String)entry.getKey() + "\\}", (String)entry.getValue());
	 	}

	 	return message;
	 }

	 public  static String renderMessage(String message, String messageValueName, String messageValue)
	 {
		message = message.replaceAll("&#123;", "{").replaceAll("&#125;", "}");

	 	if (message.indexOf('{') == -1) return message;

	 	message = message.replaceAll("\\{" + messageValueName + "\\}", messageValue);

	 	return message;
	 }
	 
}
