package com.api.remitGuru.component.util;

import java.nio.charset.Charset;
import java.util.Base64;

public class TokenGen {
	public static void main(String[] args) {
		String Client_ID="2bc1d89f-41e3-43c4-ab51-f18aa005f504";
		String Client_Secrete="ycEND1RRf1WNT5CTzdCmL~bQwZ";
		String apiToken="";
		 String auth = Client_ID + ":" + Client_Secrete;
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
	        apiToken = new String(encodedAuth);
	        System.out.println("apiToken :"+apiToken);
	}
}
