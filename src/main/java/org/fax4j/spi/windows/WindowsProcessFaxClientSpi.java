package org.fax4j.spi.windows;

import java.io.File;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.process.ProcessFaxClientSpi;
import org.fax4j.spi.process.ProcessOutputHandler;
import org.fax4j.spi.process.ProcessOutputValidator;
import org.fax4j.util.IOHelper;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will proxy the requests to windows native fax API.<br>
 * This SPI should not be used directly and instead the WindowsFaxClientSpi should be used,
 * as it will proxy the requests to this internal SPI based on the configuration and machine setup.<br>
 * It is possible to see all faxes submitted from this SPI in the windows fax window available from the
 * Start&#8594;Settings&#8594;Printers and Faxes&#8594;Fax<br>
 * <br>
 * The configuration of the fax4j framework is made up of 3 layers.<br>
 * The configuration is based on simple properties.<br>
 * Each layer overrides the lower layers by adding/changing the property values.<br>
 * The first layer is the internal fax4j.properties file located in the fax4j jar.<br>
 * This layer contains the preconfigured values for the fax4j framework and can be changed
 * by updating these properties in the higher layers.<br>
 * The second layer is the external fax4j.properties file that is located on the classpath.<br>
 * This file is optional and provides the ability to override the internal configuration for the
 * entire fax4j framework.<br>
 * The top most layer is the optional java.util.Properties object provided by the external classes
 * when creating a new fax client.<br>
 * These properties enable to override the configuration of the lower 2 layers.<br>
 * <br>
 * <b>SPI Status (Draft, Beta, Stable): </b>Stable<br>
 * <br>
 * Below table describes the configuration values relevant for this class.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.windows.server.name</td>
 * 		<td>The fax server host name.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>This SPI will only work on Windows OS (windows 2000 and up).
 * 	<li>See windows fax/tapi limitations.
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>Required native libraries: winfax.dll
 * 	<li>Windows OS (windows 2000 and up) - see http://msdn.microsoft.com/en-us/library/ms692855(VS.85).aspx for more info.
 * 	<li>Windows fax component installed.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.41.5
 */
public class WindowsProcessFaxClientSpi extends ProcessFaxClientSpi
{
	/**The fax server name*/
	private String faxServerName;
	/**The fax4j executable file location*/
	private String fax4jExecutableFileLocation;

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.21e
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The windows property part default value*/
		WINDOWS_PROPERTY_PART_DEFAULT_VALUE("windows");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxClientSpiConfigurationConstants(String value)
		{
			this.value=value;
		}
		
		/**
		 * This function returns the string value.
		 * 
		 * @return	The string value
		 */
		@Override
		public final String toString()
		{
			return this.value;
		}
	}

	/**
	 * This class holds the fax4j exe constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.22
	 */
	public enum Fax4jExeConstants
	{
		/**The fax4j.exe*/
		FAX4J_EXE("fax4j.exe"),
		/**The space string*/
		SPACE_STR(" "),
		/**The value wrapper string*/
		VALUE_WRAPPER("\""),
		/**fax4j.exe command line argument constant*/
		SUBMIT_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE("submit"),
		/**fax4j.exe command line argument constant*/
		SUSPEND_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE("suspend"),
		/**fax4j.exe command line argument constant*/
		RESUME_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE("resume"),
		/**fax4j.exe command line argument constant*/
		CANCEL_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE("cancel"),
		/**fax4j.exe command line argument constant*/
		GET_STATUS_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE("getstatus"),
		/**fax4j.exe command line argument constant*/
		ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT("-action"),
		/**fax4j.exe command line argument constant*/
		SERVER_FAX4J_EXE_COMMAND_LINE_ARGUMENT("-server"),
		/**fax4j.exe command line argument constant*/
		FAX_JOB_ID_FAX4J_EXE_COMMAND_LINE_ARGUMENT("-job"),
		/**fax4j.exe command line argument constant*/
		TARGET_ADDRESS_FAX4J_EXE_COMMAND_LINE_ARGUMENT("-target_address"),
		/**fax4j.exe command line argument constant*/
		TARGET_NAME_FAX4J_EXE_COMMAND_LINE_ARGUMENT("-target_name"),
		/**fax4j.exe command line argument constant*/
		SENDER_NAME_FAX4J_EXE_COMMAND_LINE_ARGUMENT("-sender_name"),
		/**fax4j.exe command line argument constant*/
		FILE_NAME_FAX4J_EXE_COMMAND_LINE_ARGUMENT("-file"),
		/**The fax operation done text*/
		OPERATION_OUTPUT_DONE("fax.operation.done=true"),
		/**The fax job ID output prefix*/
		FAX_JOB_ID_OUTPUT_PREFIX("fax.job.id="),
		/**The fax job status output prefix*/
		FAX_JOB_STATUS_OUTPUT_PREFIX("fax.job.status="),
		/**The fax error output prefix*/
		FAX_ERROR_OUTPUT_PREFIX("fax.error=");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private Fax4jExeConstants(String value)
		{
			this.value=value;
		}
		
		/**
		 * This function returns the string value.
		 * 
		 * @return	The string value
		 */
		@Override
		public final String toString()
		{
			return this.value;
		}
	}

	/**
	 * This is the default constructor.
	 */
	public WindowsProcessFaxClientSpi()
	{
		super();
	}

	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//initialize
		super.initializeImpl();

		//get temporary directory
		File directory=IOHelper.getFax4jInternalTemporaryDirectory();

		//get exe path
		String fax4jExe=Fax4jExeConstants.FAX4J_EXE.toString();
		File exeFile=new File(directory,fax4jExe);
		if(exeFile.exists())
		{
			this.fax4jExecutableFileLocation=exeFile.getPath();
		}
		else
		{
			exeFile=IOHelper.getFileFromNativePath(fax4jExe);
			if(exeFile!=null)
			{
				this.fax4jExecutableFileLocation=exeFile.getPath();
			}
		}

		//get fax server name
		this.faxServerName=WindowsFaxClientSpiHelper.getServerNameFromConfiguration(this);

		//set defaults
		this.useWindowsCommandPrefix=false;
	}
	
	/**
	 * This function returns true if the fax monitor events are supported by this SPI.
	 * 
	 * @return	True if the fax monitor events are supported by this SPI
	 */
	@Override
	public boolean isFaxMonitorEventsSupported()
	{
		return true;
	}

	/**
	 * Returns the property part.<br>
	 * Property parts enables to replace the input request key with the part defined
	 * to enable to reuse services with different configuration blocks.<br>
	 * Property parts will be replaced with the {0} values in the key,
	 * for example org.fax4j.{0}.somekey with part abc will be replaced to org.fax4j.abc.somekey
	 *  
	 * @return	The property part
	 */
	@Override
	public String getPropertyPart()
	{
		return FaxClientSpiConfigurationConstants.WINDOWS_PROPERTY_PART_DEFAULT_VALUE.toString();
	}
	
	/**
	 * This function creates and returns the process output validator.
	 * 
	 * @return	The process output validator
	 */
	@Override
	protected ProcessOutputValidator createProcessOutputValidator()
	{
		//create new instance
		ProcessOutputValidator validator=new WindowsProcessOutputValidator();
		
		return validator;
	}
	
	/**
	 * This function creates and returns the process output handler.
	 * 
	 * @return	The process output handler
	 */
	@Override
	protected ProcessOutputHandler createProcessOutputHandler()
	{
		//create new instance
		ProcessOutputHandler handler=new WindowsProcessOutputHandler();
		
		return handler;
	}
	
	/**
	 * This function adds the given command line argument to the buffer.
	 * 
	 * @param 	buffer
	 * 			The buffer
	 * @param 	argument
	 * 			The argument
	 * @param 	value
	 * 			The argument value
	 */
	protected void addCommandLineArgument(StringBuilder buffer,String argument,String value)
	{
		if((value!=null)&&(value.length()>0))
		{
			buffer.append(argument);
			buffer.append(Fax4jExeConstants.SPACE_STR);
			buffer.append(Fax4jExeConstants.VALUE_WRAPPER);
			buffer.append(value);
			buffer.append(Fax4jExeConstants.VALUE_WRAPPER);
			buffer.append(Fax4jExeConstants.SPACE_STR);
		}
	}
	
	/**
	 * This function creates and returns the fax4j.exe command.
	 * 
	 * @param	commandArguments
	 * 			The command line arguments
	 * @return	The fax4j.exe command
	 */
	protected String createProcessCommand(String commandArguments)
	{
		//create command
		StringBuilder buffer=new StringBuilder(500);
		buffer.append("\"");
		buffer.append(this.fax4jExecutableFileLocation);
		buffer.append("\"");
		buffer.append(Fax4jExeConstants.SPACE_STR);
		buffer.append(commandArguments);
		String command=buffer.toString();

		return command;
	}
	
	/**
	 * This function creates and returns the command line arguments for the fax4j
	 * external exe when running the submit fax job action.
	 * 
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The full command line arguments line
	 */
	protected String createProcessCommandArgumentsForSubmitFaxJob(FaxJob faxJob)
	{
		//get values from fax job
		String targetAddress=faxJob.getTargetAddress();
		String targetName=faxJob.getTargetName();
		String senderName=faxJob.getSenderName();
		File file=faxJob.getFile();
		String fileName=null;
		try
		{
			fileName=file.getCanonicalPath();
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to extract canonical path from file: "+file,exception);
		}
		
		//init buffer
		StringBuilder buffer=new StringBuilder();
		
		//create command line arguments
		this.addCommandLineArgument(buffer,Fax4jExeConstants.ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),Fax4jExeConstants.SUBMIT_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE.toString());
		this.addCommandLineArgument(buffer,Fax4jExeConstants.SERVER_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),this.faxServerName);
		this.addCommandLineArgument(buffer,Fax4jExeConstants.TARGET_ADDRESS_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),targetAddress);
		this.addCommandLineArgument(buffer,Fax4jExeConstants.TARGET_NAME_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),targetName);
		this.addCommandLineArgument(buffer,Fax4jExeConstants.SENDER_NAME_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),senderName);
		this.addCommandLineArgument(buffer,Fax4jExeConstants.FILE_NAME_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),fileName);
		
		//get text
		String commandArguments=buffer.toString();
		
		return commandArguments;
	}
	
	/**
	 * This function creates and returns the command line arguments for the fax4j
	 * external exe when running an action on an existing fax job.
	 * 
	 * @param	faxActionTypeArgument
	 * 			The fax action type argument
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The full command line arguments line
	 */
	protected String createProcessCommandArgumentsForExistingFaxJob(String faxActionTypeArgument,FaxJob faxJob)
	{
		//get values from fax job
		String faxJobID=faxJob.getID();
		
		//init buffer
		StringBuilder buffer=new StringBuilder();
		
		//create command line arguments
		this.addCommandLineArgument(buffer,Fax4jExeConstants.ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),faxActionTypeArgument);
		this.addCommandLineArgument(buffer,Fax4jExeConstants.SERVER_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),this.faxServerName);
		this.addCommandLineArgument(buffer,Fax4jExeConstants.FAX_JOB_ID_FAX4J_EXE_COMMAND_LINE_ARGUMENT.toString(),String.valueOf(faxJobID));
		
		//get text
		String commandArguments=buffer.toString();
		
		return commandArguments;
	}
	
	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	@Override
	protected String createSubmitFaxJobProcessCommand(FaxJob faxJob)
	{
		//get arguments
		String commandArguments=this.createProcessCommandArgumentsForSubmitFaxJob(faxJob);
		
		//create command
		String command=this.createProcessCommand(commandArguments);
		
		return command;
	}

	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	@Override
	protected String createSuspendFaxJobProcessCommand(FaxJob faxJob)
	{
		//get arguments
		String commandArguments=this.createProcessCommandArgumentsForExistingFaxJob(Fax4jExeConstants.SUSPEND_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE.toString(),faxJob);
		
		//create command
		String command=this.createProcessCommand(commandArguments);
		
		return command;
	}

	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	@Override
	protected String createResumeFaxJobProcessCommand(FaxJob faxJob)
	{
		//get arguments
		String commandArguments=this.createProcessCommandArgumentsForExistingFaxJob(Fax4jExeConstants.RESUME_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE.toString(),faxJob);
		
		//create command
		String command=this.createProcessCommand(commandArguments);
		
		return command;
	}
	
	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	@Override
	protected String createCancelFaxJobProcessCommand(FaxJob faxJob)
	{
		//get arguments
		String commandArguments=this.createProcessCommandArgumentsForExistingFaxJob(Fax4jExeConstants.CANCEL_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE.toString(),faxJob);
		
		//create command
		String command=this.createProcessCommand(commandArguments);
		
		return command;
	}
	
	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	@Override
	protected String createGetStatusFaxJobProcessCommand(FaxJob faxJob)
	{
		//get arguments
		String commandArguments=this.createProcessCommandArgumentsForExistingFaxJob(Fax4jExeConstants.GET_STATUS_ACTION_FAX4J_EXE_COMMAND_LINE_ARGUMENT_VALUE.toString(),faxJob);
		
		//create command
		String command=this.createProcessCommand(commandArguments);
		
		return command;
	}
}