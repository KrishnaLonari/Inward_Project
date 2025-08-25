package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.RFXDocumentUploadDaemon;
import com.api.remitGuru.component.util.Logger;

public class RFXDocumentUploadDaemonJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("RFXDocumentUploadDaemonJob started at " + (new Date()), "RFXDocumentUploadDaemonJob.java", "RFXDocumentUploadDaemonJob", null, Logger.INFO);
			RFXDocumentUploadDaemon docUploadRFX = new RFXDocumentUploadDaemon();
			docUploadRFX.executeRFXDocumentUpload();		
			Logger.log("RFXDocumentUploadDaemonJob stopped at " + (new Date()), "RFXDocumentUploadDaemonJob.java", "RFXDocumentUploadDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in RFXDocumentUploadDaemonJob", "RFXDocumentUploadDaemonJob.java", "RFXDocumentUploadDaemonJob", e, Logger.CRITICAL);
		}
	}
}