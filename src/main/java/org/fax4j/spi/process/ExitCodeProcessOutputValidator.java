package org.fax4j.spi.process;

import org.fax4j.FaxException;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This process output validator simply checks the exit code is 0.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.26
 */
public class ExitCodeProcessOutputValidator implements ProcessOutputValidator
{
	/**
	 * This is the class constructor.
	 */
	public ExitCodeProcessOutputValidator()
	{
		super();
	}

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
	public void validateProcessOutput(FaxClientSpi faxClientSpi,ProcessOutput processOutput,FaxActionType faxActionType)
	{
		//validate exit code
		int exitCode=processOutput.getExitCode();
		if(exitCode!=0)
		{
			throw new FaxException("Error while invoking fax action process, exit code: "+exitCode+" process output:\n"+processOutput.getOutputText()+"\nprocess error:\n"+processOutput.getErrorText());
		}
	}
}