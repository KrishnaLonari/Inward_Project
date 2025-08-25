package com.api.remitGuru.component.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.api.remitGuru.component.util.Logger;

/**
 * @author mansi.gandhi
 *
 */
public class SignatureGen {

	private static String path;
	private static String privateKeyName; 
	private static String publicKeyName ;
	private static String passPhrase ; 

	private static String encryptedFileName;
	private static String decryptedFileName ;


	/**
	 * @return the path
	 */
	public static String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public static void setPath(String path) {
		SignatureGen.path = path;
	}

	/**
	 * @return the privateKeyName
	 */
	public static String getPrivateKeyName() {
		return privateKeyName;
	}

	/**
	 * @param privateKeyName the privateKeyName to set
	 */
	public static void setPrivateKeyName(String privateKeyName) {
		SignatureGen.privateKeyName = privateKeyName;
	}

	/**
	 * @return the publicKeyName
	 */
	public static String getPublicKeyName() {
		return publicKeyName;
	}

	/**
	 * @param publicKeyName the publicKeyName to set
	 */
	public static void setPublicKeyName(String publicKeyName) {
		SignatureGen.publicKeyName = publicKeyName;
	}

	/**
	 * @return the passPhrase
	 */
	public static String getPassPhrase() {
		return passPhrase;
	}

	/**
	 * @param passPhrase the passPhrase to set
	 */
	public static void setPassPhrase(String passPhrase) {
		SignatureGen.passPhrase = passPhrase;
	}

	/**
	 * @return the encryptedFileName
	 */
	public static String getEncryptedFileName() {
		return encryptedFileName;
	}

	/**
	 * @param encryptedFileName the encryptedFileName to set
	 */
	public static void setEncryptedFileName(String encryptedFileName) {
		SignatureGen.encryptedFileName = encryptedFileName;
	}

	/**
	 * @return the decryptedFileName
	 */
	public static String getDecryptedFileName() {
		return decryptedFileName;
	}

	/**
	 * @param decryptedFileName the decryptedFileName to set
	 */
	public static void setDecryptedFileName(String decryptedFileName) {
		SignatureGen.decryptedFileName = decryptedFileName;
	}

	public static String encrypt(String originalFilePath,String encryptedFileName,String serviceProvider,String groupId)
	{
		
		File in = new File(originalFilePath);
		String encryptedFileReturn = "";
		long encryptedFileSize = -1;

		if(in.exists())
		{
			setPath(EnvProperties.getValue(serviceProvider + "_ADMIN_FTP_UPLOAD_PATH"));
			setPrivateKeyName(getPath() + "keys/" + groupId  + "_RemitGuru_Private_Prod_PGP_Key.asc");
			setPublicKeyName(getPath() + "keys/" + groupId + "_" + serviceProvider + "_Public_Production_PGP_Key.asc");// RemitGuru_Public_Prod_PGP_Key.asc //publicKey_test.txt
			setPassPhrase("prodavenues@0813");
			setEncryptedFileName(encryptedFileName);
			
			
			Logger.log("----------------START PGP Encryption------------------ ", "SignatureGen.java", "PGPEncryption", null, Logger.INFO);
			Logger.log("Parameters ::" 	+ "\nFile to be encrypted 			: " + getPath() + originalFilePath
					+ "\nEncrypted(output) File Path 	: " + getPath() + getEncryptedFileName()
					+ "\nPublic Key used for encryption : " + getPublicKeyName() 
					+ "\nPrivate Key used for signing : " + getPrivateKeyName(), "SignatureGen.java", "PGPEncryption", null, Logger.INFO);
			

			try {
				SignatureUtil.signAndEncryptFile(new FileOutputStream(getPath() + getEncryptedFileName()), (originalFilePath), SignatureUtil.readPublicKey(new FileInputStream(getPublicKeyName())), SignatureUtil.readSecretKey(new FileInputStream(getPrivateKeyName())), getPassPhrase(), false, true);
				Logger.log("File encrypted successfully " + originalFilePath , "SignatureGen.java", "PGPEncryption", null, Logger.INFO);
			} catch (Exception e) {
				Logger.log("File encrypted failed. " + originalFilePath , "SignatureGen.java", "PGPEncryption", e, Logger.CRITICAL);
			}
			File out = new File(getPath() + getEncryptedFileName());
			encryptedFileSize = out.length();
			Logger.log("Encrypted file size : " + encryptedFileSize , "SignatureGen.java", "PGPEncryption", null, Logger.INFO);			
			Logger.log("----------------END PGP Encryption------------------ ", "SignatureGen.java", "PGPEncryption", null, Logger.INFO);

			encryptedFileReturn = "1~" + getPath() + "~" + encryptedFileSize;
		}
		
		return encryptedFileReturn;
		
	}

	public static String decrypt(String originalEncfileName, String serviceProvider, String groupId) {

		String returnResult = "";
		setPath(EnvProperties.getValue(serviceProvider + "_ADMIN_FTP_UPLOAD_PATH"));
		File in = new File(getPath() + "download/" + originalEncfileName);
		long decryptedFileSize = 0;
		if(in.exists())
		{
			setPrivateKeyName(getPath() + "keys/" + groupId  + "_RemitGuru_Private_Prod_PGP_Key.asc");
			setPublicKeyName(getPath() + "keys/" + groupId + "_" + serviceProvider + "_Public_Production_PGP_Key.asc");// RemitGuru_Public_Prod_PGP_Key.asc //publicKey_test.txt
			setPassPhrase("prodavenues@0813");
			setEncryptedFileName(originalEncfileName);
			setDecryptedFileName(getPath()  + originalEncfileName.replace(".gpg",""));

			
			Logger.log("----------------START PGP Decryption------------------ ", "SignatureGen.java", "PGPEncryption", null, Logger.INFO);
			Logger.log("Parameters ::" 	+ "\nFile to be decrypted 			: " + getPath()  + "download/" +  getEncryptedFileName()
					+ "\nDecrypted(output) File Path 	: " +  getDecryptedFileName()
					+ "\nPrivate Key used for decryption : " + getPrivateKeyName() 
					+ "\nPublic Key used for verify sign : " + getPublicKeyName(), "SignatureGen.java", "PGPEncryption", null, Logger.INFO);

			try {
				SignatureUtil.decrypFileAndVerifySign(new FileInputStream(getPath()  + "download/" + getEncryptedFileName()), new FileInputStream(getPrivateKeyName()), getPassPhrase().toCharArray(), new FileOutputStream(getDecryptedFileName()), new FileInputStream(getPublicKeyName()));
				Logger.log("File decrypted successfully " + originalEncfileName , "SignatureGen.java", "PGPEncryption", null, Logger.INFO);
			} catch (Exception e) {
				Logger.log("File decrypted failed. " + originalEncfileName , "SignatureGen.java", "PGPEncryption", e, Logger.CRITICAL);
			}
			File out = new File(getDecryptedFileName());
			decryptedFileSize = out.length();
			Logger.log("Decrypted file size : " + decryptedFileSize , "SignatureGen.java", "PGPEncryption", null, Logger.INFO);			
			Logger.log("----------------END PGP Decryption------------------ ", "SignatureGen.java", "PGPEncryption", null, Logger.INFO);

			returnResult = "1~" +  getPath() +  "~" + decryptedFileSize;
		}

		return returnResult;
	}

}
