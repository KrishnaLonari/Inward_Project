package com.api.remitGuru.component.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptionDecryption2 {
	private String key;
	private String ivVal;
	private SecretKey secKey;
	private static EncryptionDecryption2 encryptionDecryption = null;
	private static IvParameterSpec iv = null;

	private EncryptionDecryption2() {

	}

	public static synchronized EncryptionDecryption2 getEncryptionDecryption(String groupId) throws Throwable {
		if (encryptionDecryption == null) {
			encryptionDecryption = new EncryptionDecryption2();
			AttributeManager am = new AttributeManager();
			encryptionDecryption.key = am.getDecryptedKey("DataEncKey2", "IDFCIN");
			encryptionDecryption.ivVal = am.getDecryptedKey("DataEncIV2", "IDFCIN");
			Logger.log("got key : " + groupId, "EncryptionDecryption2.java", "ED", null, Logger.INFO);
			encryptionDecryption.secKey = new SecretKeySpec(encryptionDecryption.key.getBytes(), "AES");
			encryptionDecryption.iv = new IvParameterSpec(encryptionDecryption.ivVal.getBytes("UTF-8"));
		}
		return encryptionDecryption;
	}

	public static synchronized EncryptionDecryption2 getEncryptionDecryption() throws Throwable {
		if (encryptionDecryption == null) {
			encryptionDecryption = new EncryptionDecryption2();
			AttributeManager am = new AttributeManager();
			encryptionDecryption.key = am.getDecryptedKey("DataEncKey2", "IDFCIN");
			encryptionDecryption.ivVal = am.getDecryptedKey("DataEncIV2", "IDFCIN");
			Logger.log("Method without param ,got key RG", "EncryptionDecryption2.java", "ED", null, Logger.INFO);
			encryptionDecryption.secKey = new SecretKeySpec(encryptionDecryption.key.getBytes(), "AES");
			encryptionDecryption.iv = new IvParameterSpec(encryptionDecryption.ivVal.getBytes("UTF-8"));
		}
		return encryptionDecryption;
	}

	public String encryptString(String plainText) throws Throwable {
		String val = "";

		if (!plainText.equals("")) {
			byte[] cipherText = encryptText(plainText, secKey, iv);
			val = bytesToHex(cipherText);
		} else {
			val = "";
		}

		return val;
	}

	public String decryptString(String encodedText) throws Throwable {
		String val = "";

		if (!encodedText.equals("")) {
			byte[] encByte = hexToBYte(encodedText);
			val = decryptText(encByte, secKey, iv);
		} else {
			val = "";
		}

		return val;
	}

	private static byte[] encryptText(String plainText, SecretKey secKey, IvParameterSpec iv) throws Throwable {
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		aesCipher.init(Cipher.ENCRYPT_MODE, secKey, iv);
		byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
		return byteCipherText;
	}

	private static String decryptText(byte[] byteCipherText, SecretKey secKey, IvParameterSpec iv) throws Throwable {
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		aesCipher.init(Cipher.DECRYPT_MODE, secKey, iv);
		byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
		return new String(bytePlainText);
	}

	private static String bytesToHex(byte[] hash) throws Throwable {
		return DatatypeConverter.printHexBinary(hash);
	}

	private static byte[] hexToBYte(String hash) throws Throwable {
		return DatatypeConverter.parseHexBinary(hash);
	}
	
	public String encryptE(String data) {
	
		String encData = "";
		try
		{
			encData = encryptString(data);
		}
		catch(Throwable t)
		{
			encData = data;
			Logger.log("Error while encrypting data : "+data, "EncryptionDecryption2.java", "EncryptionDecryption2", t, Logger.CRITICAL);
		}
		
		return encData;
	}
	
	public String encryptDE(String data) {
		
		String decData = "";
		try
		{
			decData = decryptString(data);
		}
		catch(Throwable t)
		{
			decData = data;
			Logger.log("Error while decrypting data : "+data, "EncryptionDecryption2.java", "EncryptionDecryption2", t, Logger.CRITICAL);
		}

		String encData = "";
		try
		{
			encData = encryptString(decData);
		}
		catch(Throwable t)
		{
			encData = decData;
			Logger.log("Error while encrypting encFirstName : "+data, "RGUser.java", "User", t, Logger.CRITICAL);
		}
		
		return encData;
	}
	
}
