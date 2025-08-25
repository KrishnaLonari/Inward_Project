package com.api.remitGuru.component.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.api.remitGuru.web.controller.FileTransferController;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class FTPUtil {

	public int uploadFTP(String ftpServerName,String ftpServerUsername,String ftpServerPassword,String ftpInputFilePath,String ftpOutputFilePath,String ftpOutputFileName)
	{
		int returnResult = -1;
    	FTPClient con = null;
    	Base64Coder base64 = new Base64Coder();

        try
        {
            con = new FTPClient();
            Logger.log("-------START FTP UPLOAD-------", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
            Logger.log("File to upload :" + ftpInputFilePath
						+ "\nFile will be uploaded at :" + ftpOutputFilePath + "  File Name : " + ftpOutputFileName, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			con.connect(ftpServerName);
			
			Logger.log("-------Connected to server " + ftpServerName + " -------", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			
            if (con.login(ftpServerUsername, base64.decodeString(ftpServerPassword)))
            {
				Logger.log("-------Login success!-------", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
                con.enterLocalPassiveMode();
                con.setFileType(FTP.BINARY_FILE_TYPE);
				con.changeWorkingDirectory(ftpOutputFilePath);
                
                FileInputStream in = new FileInputStream(new File(ftpInputFilePath));
				
				int fileSize = in.available();
				Logger.log("Uploaded file size :  " + fileSize + " bytes", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
                boolean result = con.storeFile(ftpOutputFileName, in);
                in.close();
                				
				Logger.log("Reply code : " + con.getReplyString(), "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
                if (result){ 
                	Logger.log("Upload succeessful. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
					returnResult = fileSize;
                }
				else{
					Logger.log("Upload failed. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
                	returnResult = -1;
				}
               
            }
            
        }
        catch (Throwable t)
        {
        	Logger.log("Upload failed. ", "FTPUtil.java", "PGPEncryption", t, Logger.CRITICAL);    
        	returnResult = -1;
        }
		finally{
			try{
				if (con.isConnected())
				{
					con.logout();
					con.disconnect();
					Logger.log("Logged out. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
				}
			}
			catch (IOException t) {
               Logger.log("Error in logout. ", "FTPUtil.java", "PGPEncryption", t, Logger.CRITICAL);
			}
		}
    	return returnResult;
    }
    
	public String downloadFTP(String ftpServerName,String ftpServerUsername,String ftpServerPassword,String ftpInputFilePath,String ftpOutputFilePath, String ftpDownloadedFileName){
        FTPClient con = null;
        int replyCode = -1;
		String returnString = "";
		int fileSize = 0;
		Base64Coder base64 = new Base64Coder();
		
        try
        {
            con = new FTPClient();
            Logger.log("-------START FTP DOWNLOAD-------", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			Logger.log("File to download :" + ftpInputFilePath
						+ "\nFile will be downloaded at :" + ftpOutputFilePath + ftpDownloadedFileName, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
            con.connect(ftpServerName);
            Logger.log("-------Connected to server " + ftpServerName + " -------", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			if (con.login(ftpServerUsername, base64.decodeString(ftpServerPassword)))
            {
				StringBuffer temp = new StringBuffer();
				Logger.log("-------Login success!-------", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
                con.enterLocalPassiveMode();
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = ftpOutputFilePath;
				
				Logger.log("Change Dir to :" + ftpInputFilePath, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
				con.changeWorkingDirectory(ftpInputFilePath);
				Logger.log("Remote Dir :" + data, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
				
				replyCode = con.getReplyCode();
				if (replyCode == 550) {
					Logger.log("Dir not present.", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
					
				}
				else
				{
					String [] filelist = ((String [])con.listNames());
							
					Logger.log("list Count>>" + filelist.length, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);

					for(int o = 0 ; o <filelist.length ; o++)
					{
						temp.append(filelist[o] + "\n");			
					}					
					Logger.log("list names>>" + temp, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);	
					
					InputStream inputStream = con.retrieveFileStream(ftpDownloadedFileName);
					replyCode = con.getReplyCode();
					
					if(inputStream!=null)
						fileSize = inputStream.available();

					Logger.log("Reply code : " + con.getReplyString() + "fileSize : " + fileSize, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);

					if (inputStream == null || replyCode == 550) {
						Logger.log("File not present." + ftpDownloadedFileName, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
						
					}
					else
					{
						byte[] bytesArray = new byte[4096];
						int bytesRead = -1;
						OutputStream out = new FileOutputStream(data + ftpDownloadedFileName);
						while ((bytesRead = inputStream.read(bytesArray)) != -1) {
							out.write(bytesArray, 0, bytesRead);
						}
			 
						
						if (con.completePendingCommand()) {
							Logger.log("Download succeessful. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
							returnString = "1~" + data + "~" + fileSize;
						}
						else
						{
							Logger.log("Download failed. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
							
						}
						out.close();
						inputStream.close();						
					}
				}              
            }
        }
        catch (Throwable t)
        {
        	Logger.log("Download failed. ", "FTPUtil.java", "PGPEncryption", t, Logger.CRITICAL);  	
        	returnString = "";
        }
		finally{
			try{
				if (con.isConnected())
				{
					con.logout();
					con.disconnect();
					Logger.log("Logged out. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
				}
			}
			catch (IOException t) {
               Logger.log("Error in logout. ", "FTPUtil.java", "PGPEncryption", t, Logger.CRITICAL);
			}
		}
       	return returnString;
    }

	public String uploadFTP(String ftpFileToUploadName,String selectedGroupId,String grpCntCur,String selectedProvider,String fileType){
		
		String returnResult = "";
		int replyCode = -1;
		FTPBean ftpBean = new FTPBean();
		ftpBean.setFtpPath(EnvProperties.getValue(selectedProvider + "_ADMIN_FTP_UPLOAD_PATH"));
		File in = new File(ftpBean.getFtpPath() + ftpFileToUploadName);
		if(in.exists())
		{
			ArrayList ftpSPData=new ArrayList();
			ArrayList ftpSPDtls=new ArrayList();

			ftpSPData.add(selectedGroupId);
			ftpSPData.add(grpCntCur.split("-")[0]);
			ftpSPData.add(grpCntCur.split("-")[1]);
			ftpSPData.add(fileType);// file type Transaction
			ftpSPData.add(selectedProvider);// service provider
			ftpSPData.add("U");//file event Download

			try
			{
				ftpSPDtls= new FileTransferController().getFTPServiceProviderData(ftpSPData);
			}
			catch(Throwable t)
			{
				Logger.log("Error in getFTPServiceProviderData " , "FTPUtil.jsp", "FTPUtil", t, Logger.CRITICAL);		
			}
			finally
			{
				ftpSPData  = null;
			}

			if(ftpSPDtls!=null && ftpSPDtls.size() > 0)
			{
				String [] ftpTemp = (String[]) ftpSPDtls.get(0);
				
				ftpBean.setFtpServerName(ftpTemp[8]);
				ftpBean.setFtpServerUsername(ftpTemp[10]);
				ftpBean.setFtpServerPassword(ftpTemp[11]);		
				ftpBean.setFtpUploadedFilePath(ftpTemp[12]);
				
				if(selectedProvider.equals("KKBK") || selectedProvider.equals("Westpac"))
				{
					replyCode = uploadSFTP(ftpBean.getFtpServerName(), ftpBean.getFtpServerUsername(), ftpBean.getFtpServerPassword(),ftpBean.getFtpPath() + ftpFileToUploadName, ftpBean.getFtpUploadedFilePath(), ftpFileToUploadName);
				}
				else
				{	
					replyCode = uploadFTP(ftpBean.getFtpServerName(), ftpBean.getFtpServerUsername(), ftpBean.getFtpServerPassword(),ftpBean.getFtpPath() + ftpFileToUploadName, ftpBean.getFtpUploadedFilePath(), ftpFileToUploadName);
				}
				
				if(replyCode != -1)
					 returnResult = "1~" + ftpBean.getFtpUploadedFilePath() + "~"+ replyCode;
			}			
		}
		
		return returnResult;
	}

public String downloadFTP(String inputFileName,String selectedGroupId,String grpCntCur,String selectedProvider){		
	

	String result = "";
	ArrayList ftpSPData=new ArrayList();
	ArrayList ftpSPDtls=new ArrayList();

	ftpSPData.add(selectedGroupId);
	ftpSPData.add(grpCntCur.split("-")[0]);
	ftpSPData.add(grpCntCur.split("-")[1]);
	ftpSPData.add("T");// file type Transaction
	ftpSPData.add(selectedProvider);// serive provider
	ftpSPData.add("D");//file event Download

	try
	{
			ftpSPDtls= new FileTransferController().getFTPServiceProviderData(ftpSPData);
	}
	catch(Throwable t)
	{
		Logger.log("Error in getFTPServiceProviderData " , "getFilesFTP.jsp", "getFilesFTP", t, Logger.CRITICAL);		
	}
	finally
	{
		ftpSPData  = null;
	}

	if(ftpSPDtls!=null && ftpSPDtls.size() > 0)
	{
		String [] ftpTemp = (String[]) ftpSPDtls.get(0);
		
		FTPBean ftpBean = new FTPBean();
		ftpBean.setFtpFileToDownloadPath(ftpTemp[12]);//ODAA10178	/home/apiftp/
		ftpBean.setFtpPath(EnvProperties.getValue(selectedProvider + "_ADMIN_FTP_DOWNLOAD_PATH"));
		ftpBean.setFtpServerName(ftpTemp[8]);
		ftpBean.setFtpServerUsername(ftpTemp[10]);
		ftpBean.setFtpServerPassword(ftpTemp[11]);			
		ftpBean.setFtpDownloadedFilePath(ftpBean.getFtpPath() + "download/");
		
		result = downloadFTP(ftpBean.getFtpServerName(), ftpBean.getFtpServerUsername(), ftpBean.getFtpServerPassword(), ftpBean.getFtpFileToDownloadPath(), ftpBean.getFtpDownloadedFilePath(), inputFileName);
		
		
	}
	
	return result;

}
	public ArrayList getFilesFTP(String provider,String ftpServerName,String ftpServerUsername,String ftpServerPassword,String folderName){
        ArrayList result = new ArrayList();
		FTPClient con = null;
		Base64Coder base64 = new Base64Coder();        
        try
        {
            con = new FTPClient();
            Logger.log("-------GET FILES LIST-------", "FTPUtil.java", "getFilesFTP", null, Logger.INFO);
			con.connect(ftpServerName);
            Logger.log("-------Connected to server " + ftpServerName + " -------", "FTPUtil.java", "getFilesFTP", null, Logger.INFO);
			if (con.login(ftpServerUsername, base64.decodeString(ftpServerPassword)))
            {
				String [] tempArray	= null;
				StringBuffer temp = new StringBuffer();
				Logger.log("-------Login success!-------", "FTPUtil.java", "getFilesFTP", null, Logger.INFO);
                con.enterLocalPassiveMode();
                con.setFileType(FTP.BINARY_FILE_TYPE);
                
				
				Logger.log("Change Dir to :" + folderName, "FTPUtil.java", "getFilesFTP", null, Logger.INFO);
				con.changeWorkingDirectory(folderName);	

				/*String [] filelist = ((String [])con.listNames());
							
				Logger.log("Total number of file : " + filelist.length, "FTPUtil.java", "getFilesFTP", null, Logger.INFO);
				
				for(int o = 0 ; o <filelist.length ; o++)
				{
					//temp.append(filelist[o] + "\n");			
					//result.add(filelist[o]);
					Logger.log("In if for loop :" + o + "\t" + filelist[o], "FTPUtil.java", "getFilesFTP", null, Logger.INFO);
				}*/
				
				//Logger.log("Working directory : " + con.printWorkingDirectory(), "FTPUtil.java", "getFilesFTP", null, Logger.INFO);
				
				DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				FTPFile[] ftpFiles =  con.listFiles("*.*");
				FileComparator[] comp = new FileComparator[ftpFiles.length];

				for (int i = 0; i < ftpFiles.length; i++)
	    			comp[i] = new FileComparator(ftpFiles[i]);
	      
				Arrays.sort(comp);
	      
				for (int i = 0; i < ftpFiles.length; i++)
					ftpFiles[i] = comp[i].filename;

				
				//Logger.log("ftpFiles length : " + ftpFiles.length, "FTPUtil.java", "getFilesFTP", null, Logger.INFO);

				if(ftpFiles!= null)
				{
					
					for (FTPFile ftpFile : ftpFiles) {
						if(ftpFile.isFile()){
							
							tempArray	= new String[3];
							tempArray[0] = ftpFile.getName();
							tempArray[1] = "" + ftpFile.getSize();
							tempArray[2] = "" + dateFormater.format(ftpFile.getTimestamp().getTime());

							temp.append(ftpFile.getName() + "\n");			
							result.add(tempArray);
							Logger.log("Files are :\n" + ftpFile.getName() + "  ||  " +  ftpFile.getSize() + " || " + ftpFile.getTimestamp(), "FTPUtil.java", "getFilesFTP", null, Logger.INFO);

						}
						
					}
				}
							
				Logger.log("Files out if are :\n" + temp, "FTPUtil.java", "getFilesFTP", null, Logger.INFO);	
               
            }
        }
        catch (Throwable t)
        {
        	Logger.log("Failed. ", "FTPUtil.java", "getFilesFTP", t, Logger.CRITICAL);  	
        	
        }
		finally{
			try{
				if (con.isConnected())
				{
					con.logout();
					con.disconnect();
					Logger.log("Logged out. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
				}
			}
			catch (IOException t) {
               Logger.log("Error in logout. ", "FTPUtil.java", "PGPEncryption", t, Logger.CRITICAL);
			}
		}
        return result;
    }
	
	public int uploadSFTP(String ftpServerName,String ftpServerUsername,String ftpServerPassword,String ftpInputFilePath,String ftpOutputFilePath,String ftpOutputFileName)
    {
		int returnResult = -1;
		Base64Coder base64 = new Base64Coder();

		String user = ftpServerUsername; 
		String password = base64.decodeString(ftpServerPassword);
		String host = ftpServerName;
		int port=22;
		String destPath = ftpOutputFilePath;
		
		Logger.log("-------START SFTP UPLOAD-------", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
		Logger.log("File to upload :" + ftpInputFilePath
						+ "\nFile will be uploaded at :" + destPath + "  File Name : " + ftpOutputFileName, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
		Logger.log("user : " + user + "\nftpServerName : " + host + "\nport : " +  port, "FTPUtil.java", "PGPEncryption", null, Logger.INFO);

		System.out.println("Connecting..." +  user + "@" + host + " " + port);

		try
		{
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
			Logger.log("Establishing Connection...", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			System.out.println("Establishing Connection...");
			session.connect();
				Logger.log("Connection established.", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
				System.out.println("Connection established.");
			Logger.log("Crating SFTP Channel.", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			System.out.println("Crating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			Logger.log("SFTP Channel created.", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			System.out.println("SFTP Channel created.");
			
			byte[] bufr = new byte[(int) new File(ftpInputFilePath).length()];
            FileInputStream fis = new FileInputStream(new File(ftpInputFilePath));

			int fileSize = fis.available();
			Logger.log("Uploaded file size :  " + fileSize + " bytes", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);

            fis.read(bufr);
            ByteArrayInputStream fileStream = new ByteArrayInputStream(bufr);
            sftpChannel.put(fileStream, destPath + "/" + ftpOutputFileName);
            fileStream.close();

			Logger.log("Upload succeessful. ", "FTPUtil.java", "PGPEncryption", null, Logger.INFO);
			returnResult = fileSize;
		}
		catch(Exception e){
			System.err.print(e);
			Logger.log("SFTP Failed. Error... ", "FTPUtil.java", "PGPEncryption", e, Logger.CRITICAL);
			returnResult = -1;
		}

		return returnResult;
    }

}

class FileComparator implements Comparable {
    public long time;
    public FTPFile filename;

    public FileComparator(FTPFile file) {
    	filename = file;
    	time = file.getTimestamp().getTimeInMillis();
    }

	public int compareTo(Object o) {
        long u = ((FileComparator) o).time;
        return time > u ? -1 : time == u ? 0 : 1;
    }
};
