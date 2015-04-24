package org.fax4j.spi.windows;

import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.process.ProcessOutputHandler;
import org.fax4j.spi.windows.WindowsProcessFaxClientSpi.Fax4jExeConstants;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This class implements the windows process output handler which is
 * used to update the fax job based on the process output information.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.41.5
 */
public class WindowsProcessOutputHandler implements ProcessOutputHandler
{
	/**
	 * This is the class constructor.
	 */
	public WindowsProcessOutputHandler()
	{
		super();
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
		String output=WindowsFaxClientSpiHelper.getOutputPart(processOutput,Fax4jExeConstants.FAX_JOB_ID_OUTPUT_PREFIX.toString());

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
		String output=WindowsFaxClientSpiHelper.getOutputPart(processOutput,Fax4jExeConstants.FAX_JOB_STATUS_OUTPUT_PREFIX.toString());

		FaxJobStatus faxJobStatus=null;
		if(output!=null)
		{
			//get fax job status
			faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString(output);
		}

		return faxJobStatus;
	}
}