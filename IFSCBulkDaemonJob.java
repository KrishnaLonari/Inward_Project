package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.IFSCBulkDaemon;
import com.api.remitGuru.component.util.Logger;

public class IFSCBulkDaemonJob implements Job{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("IFSCBulkDaemonJob started at " + (new Date()), "IFSCBulkDaemonJob.java", "IFSCBulkDaemonJob", null, Logger.INFO);
			IFSCBulkDaemon ifscBulkDaemon = new IFSCBulkDaemon();
			ifscBulkDaemon.executeIFSCBulkDaemon();
			Logger.log("IFSCBulkDaemonJob stopped at " + (new Date()), "IFSCBulkDaemonJob.java", "IFSCBulkDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in IFSCBulkDaemonJob", "IFSCBulkDaemonJob.java", "IFSCBulkDaemonJob", e, Logger.CRITICAL);
		}
	}
}