package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.GetSingxTxnStatusDaemon;
import com.api.remitGuru.component.util.Logger;

public class GetSingxTxnStatusDaemonJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try
		{
			Logger.log("GetSingxTxnStatusDaemonJob started at " + (new Date()), "GetSingxTxnStatusDaemonJob.java", "GetSingxTxnStatusDaemonJob", null, Logger.INFO);
			GetSingxTxnStatusDaemon getSingxTxnStatusDaemon=new GetSingxTxnStatusDaemon();
			getSingxTxnStatusDaemon.executeGetSingxTxnStatusDaemon();
			Logger.log("GetSingxTxnStatusDaemonJob stopped at " + (new Date()), "GetSingxTxnStatusDaemonJob.java", "GetSingxTxnStatusDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in GetSingxTxnStatusDaemonJob", "GetSingxTxnStatusDaemonJob.java", "GetSingxTxnStatusDaemonJob", e, Logger.CRITICAL);
		}
	}

}
