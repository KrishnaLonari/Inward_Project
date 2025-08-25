package com.api.remitGuru.component.schedularjob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.remitGuru.component.domain.GetRfxUserStatusDeamon;
import com.api.remitGuru.component.util.Logger;

public class GetRfxUserStatusDeamonJob implements Job{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			Logger.log("GetRfxUserStatusDeamonJob started at " + (new Date()), "GetRfxUserStatusDeamonJob.java", "GetRfxUserStatusDeamonJob", null, Logger.INFO);
			GetRfxUserStatusDeamon getRfxUserStatusDeamon = new GetRfxUserStatusDeamon();
			getRfxUserStatusDeamon.executeRfxStatusDaemon();
			Logger.log("GetRfxUserStatusDeamonJob stopped at " + (new Date()), "GetRfxUserStatusDeamonJob.java", "GetRfxUserStatusDeamonJob", null, Logger.INFO);
		}
		catch(Exception e)
		{
			Logger.log("Error in GetRfxUserStatusDeamonJob", "GetRfxUserStatusDeamonJob.java", "GetRfxUserStatusDeamonJob", e, Logger.CRITICAL);
		}
	}
}