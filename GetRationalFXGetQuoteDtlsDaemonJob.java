package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.GetRationalFXGetQuoteDtlsDaemon;
import com.api.remitGuru.component.util.Logger;

public class GetRationalFXGetQuoteDtlsDaemonJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("GetRationalFXGetQuoteDtlsDaemonJob started at " + (new Date()), "GetRationalFXGetQuoteDtlsDaemonJob.java", "GetRationalFXGetQuoteDtlsDaemonJob", null, Logger.INFO);
			GetRationalFXGetQuoteDtlsDaemon rationalFXGetQuote = new GetRationalFXGetQuoteDtlsDaemon();
			rationalFXGetQuote.executeGetRationalFXGetQuoteDtlsDaemon();
			Logger.log("GetRationalFXGetQuoteDtlsDaemonJob stopped at " + (new Date()), "GetRationalFXGetQuoteDtlsDaemonJob.java", "GetRationalFXGetQuoteDtlsDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in GetRationalFXGetQuoteDtlsDaemonJob", "GetRationalFXGetQuoteDtlsDaemonJob.java", "GetRationalFXGetQuoteDtlsDaemonJob", e, Logger.CRITICAL);
		}
	}
}
