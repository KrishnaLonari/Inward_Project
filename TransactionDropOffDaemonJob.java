package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.TransactionDropoffDaemon;
import com.api.remitGuru.component.util.Logger;

public class TransactionDropOffDaemonJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("TransactionDropOffDaemonJob started at " + (new Date()), "TransactionDropOffDaemonJob.java", "TransactionDropOffDaemonJob", null, Logger.INFO);
			TransactionDropoffDaemon dropoffDaemon = new TransactionDropoffDaemon();
			dropoffDaemon.executeTransactionDropoffDaemon();
			Logger.log("TransactionDropOffDaemonJob stopped at " + (new Date()), "TransactionDropOffDaemonJob.java", "TransactionDropOffDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in TransactionDropOffDaemonJob", "TransactionDropOffDaemonJob.java", "TransactionDropOffDaemonJob", e, Logger.CRITICAL);
		}
	}
}
