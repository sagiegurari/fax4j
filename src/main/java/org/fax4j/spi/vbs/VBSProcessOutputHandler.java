package org.fax4j.spi.vbs;

import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.process.ProcessOutputHandler;
import org.fax4j.spi.windows.WindowsFaxClientSpiHelper;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This class implements the VBS process output handler which is
 * used to update the fax job based on the process output information.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.3
 */
public class VBSProcessOutputHandler implements ProcessOutputHandler
{
	/**The fax job ID output prefix*/
	private static final String FAX_JOB_ID_OUTPUT_PREFIX="fax.job.id=";
	/**The fax job status output prefix*/
	private static final String FAX_JOB_STATUS_OUTPUT_PREFIX="fax.job.status=";

	/**
	 * This is the class constructor.
	 */
	public VBSProcessOutputHandler()
	{
		super();
	}

	/**
	 * This function returns the fax job status based on the windows
	 * fax job status string value.
	 * 
	 * @param 	faxJobStatusStr
	 * 			The fax job status string value
	 * @return	The fax job status
	 */
	protected FaxJobStatus getFaxJobStatusFromWindowsFaxJobStatusString(String faxJobStatusStr)
	{
		FaxJobStatus faxJobStatus=FaxJobStatus.UNKNOWN;
		if((faxJobStatusStr!=null)&&(faxJobStatusStr.length()>0))
		{
			if((faxJobStatusStr.equalsIgnoreCase("Pending"))||(faxJobStatusStr.equalsIgnoreCase("Paused"))||(faxJobStatusStr.equalsIgnoreCase("Retrying")))
			{
				faxJobStatus=FaxJobStatus.PENDING;
			}
			else if(faxJobStatusStr.equalsIgnoreCase("In Progress"))
			{
				faxJobStatus=FaxJobStatus.IN_PROGRESS;
			}
			else if((faxJobStatusStr.equalsIgnoreCase("Failed"))||(faxJobStatusStr.equalsIgnoreCase("No Line"))||(faxJobStatusStr.equalsIgnoreCase("Retries Exceeded")))
			{
				faxJobStatus=FaxJobStatus.ERROR;
			}
		}
		
		return faxJobStatus;
	}

	/**
	 * Updates the fax job based on the data from the process output.
	 *  
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @param 	faxJob
	 * 			The fax job object
	 * @param 	processOutput
	 * 			The process output
	 * @param	faxActionType
	 * 			The fax action type
	 */
	public void updateFaxJob(FaxClientSpi faxClientSpi,FaxJob faxJob,ProcessOutput processOutput,FaxActionType faxActionType)
	{
		//get output
		String output=WindowsFaxClientSpiHelper.getOutputPart(processOutput,VBSProcessOutputHandler.FAX_JOB_ID_OUTPUT_PREFIX);

		if(output!=null)
		{
			//validate fax job ID
			WindowsFaxClientSpiHelper.validateFaxJobID(output);
			
			//set fax job ID
			faxJob.setID(output);
		}
	}
	
	/**
	 * This function extracts the fax job status from the process output.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @param	processOutput
	 * 			The process output
	 * @return	The fax job status
	 */
	public FaxJobStatus getFaxJobStatus(FaxClientSpi faxClientSpi,ProcessOutput processOutput)
	{
		//get output
		String output=WindowsFaxClientSpiHelper.getOutputPart(processOutput,VBSProcessOutputHandler.FAX_JOB_STATUS_OUTPUT_PREFIX);

		FaxJobStatus faxJobStatus=null;
		if(output!=null)
		{
			//get fax job status
			faxJobStatus=this.getFaxJobStatusFromWindowsFaxJobStatusString(output);
		}

		return faxJobStatus;
	}
}