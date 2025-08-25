package com.api.remitGuru.component.schedularjob;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.util.Logger;
import com.api.remitGuru.component.util.MailPool;

public class MailPoolJob implements Job
{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("Mail Pool Daemon started at " + (new Date()), "SchedulerJob.java", "SchedulerJob", null, Logger.INFO);
			MailPool mailPool = new MailPool();
			mailPool.executeMailPool();
			Logger.log("Mail Pool Daemon stopped at " + (new Date()), "SchedulerJob.java", "SchedulerJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in MailPoolJob", "SchedulerJob.java", "SchedulerJob", e, Logger.CRITICAL);
		}
	}
}