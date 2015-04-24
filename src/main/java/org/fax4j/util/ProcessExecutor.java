package org.fax4j.util;

import java.io.IOException;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * The process executer enables to execute native processes.<br>
 * Implementing classes must be statless and thread safe.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.40.1
 */
public interface ProcessExecutor
{
	/**The process executer class name property key*/
	public static final String PROCESS_EXECUTER_CLASS_NAME_PROPERTY_KEY="org.fax4j.process.executer.class.name";

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
	public ProcessOutput executeProcess(ConfigurationHolder configurationHolder,String command) throws IOException,InterruptedException;
}