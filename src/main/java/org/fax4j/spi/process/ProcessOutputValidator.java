package org.fax4j.spi.process;

import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This interface defines the process output validator which is
 * used to validate if the process ended correctly.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.26
 */
public interface ProcessOutputValidator
{
	/**
	 * This function validates the process output for errors.<br>
	 * If not valid, an exception should be thrown.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @param	processOutput
	 * 			The process output to validate
	 * @param	faxActionType
	 * 			The fax action type
	 */
	public void validateProcessOutput(FaxClientSpi faxClientSpi,ProcessOutput processOutput,FaxActionType faxActionType);
}