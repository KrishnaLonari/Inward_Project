package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.InitiatePaymentDaemon;
import com.api.remitGuru.component.util.Logger;

public class InitiatePaymentDaemonJob implements Job{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("InitiatePaymentDaemonJob started at " + (new Date()), "InitiatePaymentDaemonJob.java", "InitiatePaymentDaemonJob", null, Logger.INFO);
			InitiatePaymentDaemon initiatePayment = new InitiatePaymentDaemon();
			initiatePayment.executeInitiatePaymentDaemon();
			Logger.log("InitiatePaymentDaemonJob stopped at " + (new Date()), "InitiatePaymentDaemonJob.java", "InitiatePaymentDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in InitiatePaymentDaemonJob", "InitiatePaymentDaemonJob.java", "InitiatePaymentDaemonJob", e, Logger.CRITICAL);
		}
	}
}