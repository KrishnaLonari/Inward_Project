package com.api.remitGuru.component.util;

import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.ArrayList;
//import org.wildfly.security.auth.*;
//import org.wildfly.naming.client.WildFlyInitialContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import com.api.remitGuru.component.controller.*;
import com.api.remitGuru.component.util.*;

public class DaemonTest {
	/*org.wildfly.security.auth.client.AuthenticationConfiguration ejbConfig = org.wildfly.security.auth.client.AuthenticationConfiguration.empty().useName("ejbadmin").usePassword("Pass@1234");
	 
	// create your authentication context
	//org.wildfly.security.auth.client.AuthenticationContext context = org.wildfly.security.auth.client.AuthenticationContext.empty().with(org.wildfly.security.auth.client.MatchRule.ALL.matchHost("192.168.14.96"), ejbConfig);
	org.wildfly.security.auth.client.AuthenticationContext context = org.wildfly.security.auth.client.AuthenticationContext.empty();
	context = context.with(MatchRule.ALL.matchHost("127.0.0.1"), ejbConfig);
	
	try {


		// create a callable that invokes an EJB
		java.util.concurrent.Callable<Void> callable = () -> {
		 
		    // create an InitialContext
		    Properties properties = new Properties();
		    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		    properties.put(Context.PROVIDER_URL, "remote+http://192.168.14.96:8380");
		    InitialContext ctx = new InitialContext(properties);
		 
		    // look up an EJB and invoke one of its methods (same as before)
		String myURL = "appControllerJAR/CountryHandlerBean!com.api.remitGuru.component.controller.CountryHandler";
			CountryHandler 	 ch	    = (CountryHandler) ctx.lookup("ejb:/"+myURL);	

		//ArrayList a1;
		 ArrayList al = new ArrayList();

		try
		{
		al = ch.getCountryList("RG");
		System.out.println("<br><br><br>Country List  : "  + al );

		}
		catch(Exception ep4)
		{
			ep4.printStackTrace();
			System.out.println("ep2: <br>"  + ep4 );
		}
		//out.println("<br><br><br>Country List  : "  + al );
		   // RemoteCalculator statelessRemoteCalculator = (RemoteCalculator) context.lookup("ejb:/ejb-remote-server-side//CalculatorBean!" + RemoteCalculator.class.getName());
		   // int sum = statelessRemoteCalculator.add(101, 202);
		    return null;
		};
		 
		// use your authentication context to run your callable
		context.runCallable(callable);


		}
		catch(Exception t) {
			t.printStackTrace();
			System.out.println("t:<br> <br>"  + t );
			//Logger.log("Exception in doLookUp", "CountryController.java", "Controller", t, Logger.ERROR);
		}


		ArrayList al2 = new ArrayList();

		try
		{
			//al = ch.getCountryList("RG");
		System.out.println(" Try Country List ::------------"+al2);
		}
		catch(Exception ep3)
		{
			ep3.printStackTrace();
			System.out.println("ep2: <br>"  + ep3 );
		}*/


}
