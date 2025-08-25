package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.StatusInquiryDaemon;
import com.api.remitGuru.component.util.Logger;

public class StatusInquiryDaemonJob implements Job{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("StatusInquiryDaemonJob started at " + (new Date()), "StatusInquiryDaemonJob.java", "StatusInquiryDaemonJob", null, Logger.INFO);
			StatusInquiryDaemon statusInquiry = new StatusInquiryDaemon();
			statusInquiry.executeStatusInquiryDaemon();
			Logger.log("StatusInquiryDaemonJob stopped at " + (new Date()), "StatusInquiryDaemonJob.java", "StatusInquiryDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in StatusInquiryDaemonJob", "StatusInquiryDaemonJob.java", "StatusInquiryDaemonJob", e, Logger.CRITICAL);
		}
	}
}