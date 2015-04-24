package org.fax4j.util;

import java.io.IOException;
import org.fax4j.FaxException;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.Logger;
import org.fax4j.common.LoggerManager;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * The process executer enables to execute native processes.<br>
 * This class provides only partial implementation of the executer.<br>
 * Implementing classes must be statless and thread safe.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.40.1
 */
public abstract class AbstractProcessExecutor implements ProcessExecutor
{
	/**The logger*/
	protected final Logger LOGGER;

	/**
	 * This is the class constructor.
	 */
	public AbstractProcessExecutor()
	{
		super();

		//get logger
		LoggerManager loggerManager=LoggerManager.getInstance();
		this.LOGGER=loggerManager.getLogger();
	}

	/**
	 * This function executes the given command and returns the process output.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder used when invoking the process
	 * @param	command
	 * 			The command to execute
	 * @return	The process output
	 * @throws	IOException
	 * 			Any IO exception
	 * @throws	InterruptedException
	 * 			If thread interrupted during waitFor for the process
	 */
	public ProcessOutput executeProcess(ConfigurationHolder configurationHolder,String command) throws IOException,InterruptedException
	{
		//validate command provided
		if((command==null)||(command.length()==0))
		{
			throw new FaxException("Command not provided.");
		}

		//trim command and validate
		String updatedCommand=command.trim();
		if(updatedCommand.length()==0)
		{
			throw new FaxException("Command not provided.");
		}

		//validate configuration holder provided
		if(configurationHolder==null)
		{
			throw new FaxException("Configuration holder not provided.");
		}
		
		this.LOGGER.logDebug(new Object[]{"Invoking command: ",updatedCommand},null);

		//execute process
		ProcessOutput processOutput=this.executeProcessImpl(configurationHolder,updatedCommand);

		int exitCode=processOutput.getExitCode();
		String outputText=processOutput.getOutputText();
		String errorText=processOutput.getErrorText();

		this.LOGGER.logDebug(new Object[]{"Invoked command: ",updatedCommand," Exit Code: ",String.valueOf(exitCode),Logger.SYSTEM_EOL,"Output Text:",Logger.SYSTEM_EOL,outputText,Logger.SYSTEM_EOL,"Error Text:",Logger.SYSTEM_EOL,errorText},null);
		
		return processOutput;
	}

	/**
	 * This function executes the given command and returns the process output.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder used when invoking the process
	 * @param	command
	 * 			The command to execute
	 * @return	The process output
	 * @throws	IOException
	 * 			Any IO exception
	 * @throws	InterruptedException
	 * 			If thread interrupted during waitFor for the process
	 */
	protected abstract ProcessOutput executeProcessImpl(ConfigurationHolder configurationHolder,String command) throws IOException,InterruptedException;
}