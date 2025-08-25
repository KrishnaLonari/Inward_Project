package com.api.remitGuru.component.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptionDecryption {
	private String key;
	private SecretKey secKey;
	private static EncryptionDecryption encryptionDecryption = null;

	private EncryptionDecryption() {

	}

	public static synchronized EncryptionDecryption getEncryptionDecryption(String groupId) throws Throwable {
		if (encryptionDecryption == null) {
			encryptionDecryption = new EncryptionDecryption();
			AttributeManager am = new AttributeManager();
			encryptionDecryption.key = am.getDecryptedKey("DataEncKey", groupId);
			Logger.log("got key : " + groupId, "EncryptionDecryption.java", "ED", null, Logger.INFO);
			encryptionDecryption.secKey = new SecretKeySpec(encryptionDecryption.key.getBytes(), "AES");
		}
		return encryptionDecryption;
	}

	public static synchronized EncryptionDecryption getEncryptionDecryption() throws Throwable {
		if (encryptionDecryption == null) {
			encryptionDecryption = new EncryptionDecryption();
			AttributeManager am = new AttributeManager();
			encryptionDecryption.key = am.getDecryptedKey("DataEncKey", "RG");
			Logger.log("got key 'RG'", "EncryptionDecryption.java", "ED", null, Logger.INFO);
			encryptionDecryption.secKey = new SecretKeySpec(encryptionDecryption.key.getBytes(), "AES");
		}
		return encryptionDecryption;
	}

	public String encryptString(String plainText) throws Throwable {
		String val = "";

		if (!plainText.equals("")) {
			byte[] cipherText = encryptText(plainText, secKey);
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
			val = decryptText(encByte, secKey);
		} else {
			val = "";
		}

		return val;
	}

	private static byte[] encryptText(String plainText, SecretKey secKey) throws Throwable {
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
		byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
		return byteCipherText;
	}

	private static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Throwable {
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.DECRYPT_MODE, secKey);
		byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
		return new String(bytePlainText);
	}

	private static String bytesToHex(byte[] hash) throws Throwable {
		return DatatypeConverter.printHexBinary(hash);
	}

	private static byte[] hexToBYte(String hash) throws Throwable {
		return DatatypeConverter.parseHexBinary(hash);
	}
}
