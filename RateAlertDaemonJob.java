package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.RateAlertDaemon;
import com.api.remitGuru.component.util.Logger;

public class RateAlertDaemonJob implements Job{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("RateAlertDaemonJob started at " + (new Date()), "RateAlertDaemonJob.java", "RateAlertDaemonJob", null, Logger.INFO);
			RateAlertDaemon rateAlertDaemon = new RateAlertDaemon();
			rateAlertDaemon.executeRateAlertDaemon();
			Logger.log("RateAlertDaemonJob stopped at " + (new Date()), "RateAlertDaemonJob.java", "RateAlertDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in GetRationalFXGetQuoteDtlsDaemonJob", "RateAlertDaemonJob.java", "RateAlertDaemonJob", e, Logger.CRITICAL);
		}
	}
}
