package org.fax4j.util;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.common.ConfigurationHolder;

/**
 * This is an internal process execution utility class.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.40.6
 */
public final class ProcessExecutorHelper
{
	/**The process executers*/
	private static final Map<String,ProcessExecutor> PROCESS_EXECUTERS=new HashMap<String,ProcessExecutor>(20);

	/**
	 * This is the default constructor.
	 */
	private ProcessExecutorHelper()
	{
		super();
	}

	/**
	 * This function executes the given command and returns the process output.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder used when invoking the process
	 * @param	command
	 * 			The command to execute
	 * @return	The process output
	 */
	public static ProcessOutput executeProcess(ConfigurationHolder configurationHolder,String command)
	{
		//get process executer class name
		String className=configurationHolder.getConfigurationValue(ProcessExecutor.PROCESS_EXECUTER_CLASS_NAME_PROPERTY_KEY);
		if(className==null)
		{
			className=DefaultProcessExecutor.class.getName();
		}
		
		//get process executer from cache
		ProcessExecutor processExecutor=ProcessExecutorHelper.PROCESS_EXECUTERS.get(className);
		if(processExecutor==null)
		{
			//create new process executer (no need to synchronize as it won't impact anything if we create more
			//than 1 process executer of same type)
			processExecutor=(ProcessExecutor)ReflectionHelper.createInstance(className);
			
			//put in cache
			ProcessExecutorHelper.PROCESS_EXECUTERS.put(className,processExecutor);
		}

		ProcessOutput processOutput=null;
		try
		{
			//execute process
			processOutput=processExecutor.executeProcess(configurationHolder,command);
		}
		catch(RuntimeException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while invoking process, command: "+command,exception);
		}
		
		return processOutput;
	}

	/**
	 * This holds the process output.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.21e
	 */
	public static class ProcessOutput
	{
		/**The output text*/
		private final String OUTPUT_TEXT;
		/**The error text*/
		private final String ERROR_TEXT;
		/**The process exit code*/
		private final int EXIT_CODE;
		
		/**
		 * This is the class constructor.
		 * 
		 * @param 	outputText
		 * 			The output text
		 * @param 	errorText
		 * 			The error text
		 * @param	exitCode
		 * 			The process exit code
		 */
		public ProcessOutput(String outputText,String errorText,int exitCode)
		{
			super();
			
			//get values
			this.OUTPUT_TEXT=outputText;
			this.ERROR_TEXT=errorText;
			this.EXIT_CODE=exitCode;
		}
		
		/**
		 * This function returns the process output text.
		 * 
		 * @return	The process output text
		 */
		public String getOutputText()
		{
			return this.OUTPUT_TEXT;
		}
		
		/**
		 * This function returns the process error text.
		 * 
		 * @return	The process error text
		 */
		public String getErrorText()
		{
			return this.ERROR_TEXT;
		}
		
		/**
		 * This function returns the process exit code.
		 * 
		 * @return	The process exit code
		 */
		public int getExitCode()
		{
			return this.EXIT_CODE;
		}
	}
}