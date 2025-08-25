package com.api.remitGuru.component.schedularjob;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.ManageRiskLimitDaemon;
import com.api.remitGuru.component.util.Logger;
import com.api.remitGuru.component.util.MailPool;

public class ManageRiskLimitDaemonJob implements Job
{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("ManageRiskLimitDaemonJob started at " + (new Date()), "ManageRiskLimitDaemonJob.java", "ManageRiskLimitDaemonJob", null, Logger.INFO);
			ManageRiskLimitDaemon manageRiskLimitDaemon = new ManageRiskLimitDaemon();
			manageRiskLimitDaemon.executeManageRiskLimitDaemon();
			Logger.log("ManageRiskLimitDaemonJob stopped at " + (new Date()), "ManageRiskLimitDaemonJob.java", "ManageRiskLimitDaemonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in ManageRiskLimitDaemonJob", "ManageRiskLimitDaemonJob.java", "ManageRiskLimitDaemonJob", e, Logger.CRITICAL);
		}
	}
}