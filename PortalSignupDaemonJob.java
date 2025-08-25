package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.PortalSignupDaemon;
import com.api.remitGuru.component.util.Logger;

public class PortalSignupDaemonJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("PortalSignupDaemonJob started at " + (new Date()), "PortalSignupDaemonJob.java", "PortalSignupDaemonJob", null, Logger.INFO);
			PortalSignupDaemon signupDaemon = new PortalSignupDaemon();
			signupDaemon.executePortalSignupDaemon();
			Logger.log("PortalSignupDaemonJob stopped at " + (new Date()), "PortalSignupDaemonJob.java", "PortalSignupDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in PortalSignupDaemonJob", "PortalSignupDaemonJob.java", "PortalSignupDaemonJob", e, Logger.CRITICAL);
		}
	}
}
