package org.fax4j.spi.process;

import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This interface defines the process output handler which is
 * used to update the fax job based on the process output information.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.25
 */
public interface ProcessOutputHandler
{
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
	public void updateFaxJob(FaxClientSpi faxClientSpi,FaxJob faxJob,ProcessOutput processOutput,FaxActionType faxActionType);
	
	/**
	 * This function extracts the fax job status from the process output.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @param	processOutput
	 * 			The process output
	 * @return	The fax job status
	 */
	public FaxJobStatus getFaxJobStatus(FaxClientSpi faxClientSpi,ProcessOutput processOutput);
}