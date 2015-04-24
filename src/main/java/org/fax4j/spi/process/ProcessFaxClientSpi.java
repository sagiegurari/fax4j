package org.fax4j.spi.process;

import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.util.ProcessExecutorHelper;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;
import org.fax4j.util.ReflectionHelper;
import org.fax4j.util.SpiUtil;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will invoke the requests by executing an external process (executable/script) that enables
 * to invoke the fax action.<br>
 * This class will use request templates to construct the process command.<br> 
 * The template parameters are updated with the values from the fax4j configuration or FaxJob data.<br>
 * All template parameters are defined as ${templatename} variables, for example: myexe.exe -target <b>${target.address}</b> -file <b>${file}</b><br>
 * <br>
 * Below table describes the template parameters.<br>
 * <b>Template Parameters:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Template Parameter</td>
 * 		<td>Description</td>
 * </tr>
 * 	<tr>
 * 		<td>file</td>
 * 		<td>The fax job file path</td>
 * </tr>
 * 	<tr>
 * 		<td>target.address</td>
 * 		<td>The fax job target address</td>
 * </tr>
 * 	<tr>
 * 		<td>target.name</td>
 * 		<td>The fax job target name</td>
 * </tr>
 * 	<tr>
 * 		<td>sender.name</td>
 * 		<td>The fax job sender name</td>
 * </tr>
 * 	<tr>
 * 		<td>sender.fax.number</td>
 * 		<td>The fax job sender fax number</td>
 * </tr>
 * 	<tr>
 * 		<td>sender.email</td>
 * 		<td>The fax job sender email</td>
 * </tr>
 * </table>
 * <br>
 * In case there is no template defined for a certain fax action, this class will throw an UnsupportedOperationException exception.<br>
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
 * 		<td>org.fax4j.spi.process.submit.template.command</td>
 * 		<td>The submit fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.suspend.template.command</td>
 * 		<td>The suspend fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.resume.template.command</td>
 * 		<td>The resume fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.cancel.template.command</td>
 * 		<td>The cancel fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.get.status.template.command</td>
 * 		<td>The get status fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.output.validator</td>
 * 		<td>The process output validator class name.</td>
 * 		<td>org.fax4j.spi.process.ExitCodeProcessOutputValidator</td>
 * 		<td>org.fax4j.spi.process.ExitCodeProcessOutputValidator</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.output.handler</td>
 * 		<td>The process output handler class name.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.use.windows.command.prefix</td>
 * 		<td>True to add a windows command prefix to the command template.</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.process.windows.command.prefix</td>
 * 		<td>True to add a windows command prefix to the command template.</td>
 * 		<td>rundll32 SHELL32.DLL,ShellExec_RunDLL</td>
 * 		<td>rundll32 SHELL32.DLL,ShellExec_RunDLL</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>External executable/script must be available
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.13
 * @since	0.21e
 */
public class ProcessFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The process output validator*/
	private ProcessOutputValidator processOutputValidator;
	/**The process output handler*/
	private ProcessOutputHandler processOutputHandler;
	/**The use windows command prefix flag*/
	protected boolean useWindowsCommandPrefix;
	/**The windows command prefix value*/
	private String windowsCommandPrefix;
	/**The windows execute prefix*/
	protected static final String WINDOWS_EXECUTE_PREFIX_DEFAULT_VALUE="rundll32 SHELL32.DLL,ShellExec_RunDLL";
	
	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.21e
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The submit fax job template command property key*/
		SUBMIT_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY("org.fax4j.spi.{0}.submit.template.command"),
		/**The suspend fax job template command property key*/
		SUSPEND_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY("org.fax4j.spi.{0}.suspend.template.command"),
		/**The resume fax job template command property key*/
		RESUME_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY("org.fax4j.spi.{0}.resume.template.command"),
		/**The cancel fax job template command property key*/
		CANCEL_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY("org.fax4j.spi.{0}.cancel.template.command"),
		/**The get status fax job template command property key*/
		GET_STATUS_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY("org.fax4j.spi.{0}.get.status.template.command"),
		/**The process output validator property key*/
		PROCESS_OUTPUT_VALIDATOR_PRE_FORMAT_PROPERTY_KEY("org.fax4j.spi.{0}.output.validator"),
		/**The process output handler property key*/
		PROCESS_OUTPUT_HANDLER_PRE_FORMAT_PROPERTY_KEY("org.fax4j.spi.{0}.output.handler"),
		/**The use windows command prefix property key*/
		USE_WINDOWS_PREFIX_PROPERTY_KEY("org.fax4j.spi.process.use.windows.command.prefix"),
		/**The windows command prefix property key*/
		WINDOWS_PREFIX_PROPERTY_KEY("org.fax4j.spi.process.windows.command.prefix"),
		/**The property part default value*/
		PROPERTY_PART_DEFAULT_VALUE("process");

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
	 * This is the default constructor.
	 */
	public ProcessFaxClientSpi()
	{
		super();
	}

	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//get use windows command prefix flag
		String value=this.getConfigurationValue(FaxClientSpiConfigurationConstants.USE_WINDOWS_PREFIX_PROPERTY_KEY);
		this.useWindowsCommandPrefix=Boolean.parseBoolean(value);
		
		//get windows command prefix
		this.windowsCommandPrefix=this.getConfigurationValue(FaxClientSpiConfigurationConstants.WINDOWS_PREFIX_PROPERTY_KEY);
		if(this.windowsCommandPrefix==null)
		{
			this.windowsCommandPrefix=ProcessFaxClientSpi.WINDOWS_EXECUTE_PREFIX_DEFAULT_VALUE;
		}

		//create process output validator
		this.processOutputValidator=this.createProcessOutputValidator();
		if(this.processOutputValidator==null)
		{
			throw new FaxException("Unable to create a new process output validator.");
		}

		//create process output handler
		this.processOutputHandler=this.createProcessOutputHandler();
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
		return FaxClientSpiConfigurationConstants.PROPERTY_PART_DEFAULT_VALUE.toString();
	}
	
	/**
	 * This function creates and returns the process output validator.
	 * 
	 * @return	The process output validator
	 */
	protected ProcessOutputValidator createProcessOutputValidator()
	{
		//get process output validator
		String className=this.getConfigurationValue(FaxClientSpiConfigurationConstants.PROCESS_OUTPUT_VALIDATOR_PRE_FORMAT_PROPERTY_KEY);
		ProcessOutputValidator validator=null;
		if(className==null)
		{
			className=ExitCodeProcessOutputValidator.class.getName();
		}

		//create new instance
		validator=(ProcessOutputValidator)ReflectionHelper.createInstance(className);

		return validator;
	}
	
	/**
	 * This function creates and returns the process output handler.
	 * 
	 * @return	The process output handler
	 */
	protected ProcessOutputHandler createProcessOutputHandler()
	{
		//get process output handler
		String className=this.getConfigurationValue(FaxClientSpiConfigurationConstants.PROCESS_OUTPUT_HANDLER_PRE_FORMAT_PROPERTY_KEY);
		ProcessOutputHandler handler=null;
		if(className!=null)
		{
			//create new instance
			handler=(ProcessOutputHandler)ReflectionHelper.createInstance(className);
		}

		return handler;
	}
	
	/**
	 * This function returns the process output validator.
	 * 
	 * @return	The process output validator
	 */
	public final ProcessOutputValidator getProcessOutputValidator()
	{
		return this.processOutputValidator;
	}
	
	/**
	 * This function returns the process output handler.
	 * 
	 * @return	The process output handler
	 */
	protected final ProcessOutputHandler getProcessOutputHandler()
	{
		return this.processOutputHandler;
	}

	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void submitFaxJobImpl(FaxJob faxJob)
	{
		//create command
		String command=this.createSubmitFaxJobProcessCommand(faxJob);
		
		//execute command
		this.executeProcess(faxJob,command,FaxActionType.SUBMIT_FAX_JOB);
	}
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void suspendFaxJobImpl(FaxJob faxJob)
	{
		//create command
		String command=this.createSuspendFaxJobProcessCommand(faxJob);
		
		//execute command
		this.executeProcess(faxJob,command,FaxActionType.SUSPEND_FAX_JOB);
	}
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void resumeFaxJobImpl(FaxJob faxJob)
	{
		//create command
		String command=this.createResumeFaxJobProcessCommand(faxJob);
		
		//execute command
		this.executeProcess(faxJob,command,FaxActionType.RESUME_FAX_JOB);
	}
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void cancelFaxJobImpl(FaxJob faxJob)
	{
		//create command
		String command=this.createCancelFaxJobProcessCommand(faxJob);
		
		//execute command
		this.executeProcess(faxJob,command,FaxActionType.CANCEL_FAX_JOB);
	}
	
	/**
	 * This function returns the fax job status.<br>
	 * Not all SPIs support extraction of the fax job status.<br>
	 * In case the SPI is unable to extract or does not support extracting
	 * of the fax job status, it will return the UNKNOWN status.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @return	The fax job status
	 */
	@Override
	protected FaxJobStatus getFaxJobStatusImpl(FaxJob faxJob)
	{
		//create command
		String command=this.createGetStatusFaxJobProcessCommand(faxJob);

		FaxJobStatus faxJobStatus=FaxJobStatus.UNKNOWN;
		if(command!=null)
		{
			//execute command
			ProcessOutput processOutput=this.executeProcess(faxJob,command,FaxActionType.GET_FAX_JOB_STATUS);
			
			//get fax job status
			faxJobStatus=this.getFaxJobStatus(processOutput);
		}

		return faxJobStatus;
	}
	
	/**
	 * This function extracts the fax job status from the process output.
	 * 
	 * @param	processOutput
	 * 			The process output
	 * @return	The fax job status
	 */
	protected FaxJobStatus getFaxJobStatus(ProcessOutput processOutput)
	{
		FaxJobStatus faxJobStatus=null;
		if(this.processOutputHandler!=null)
		{
			//get fax job status
			faxJobStatus=this.processOutputHandler.getFaxJobStatus(this,processOutput);
		}
		
		//if unable to get the status, set to unknown
		if(faxJobStatus==null)
		{
			faxJobStatus=FaxJobStatus.UNKNOWN;
		}

		return faxJobStatus;
	}
	
	/**
	 * Executes the process and returns the output.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @param 	command
	 * 			The command to execute
	 * @param	faxActionType
	 * 			The fax action type
	 * @return	The process output
	 */
	protected ProcessOutput executeProcess(FaxJob faxJob,String command,FaxActionType faxActionType)
	{
		if(command==null)
		{
			this.throwUnsupportedException();
		}

		//update command
		String updatedCommand=command;
		if(this.useWindowsCommandPrefix)
		{
			//init buffer
			StringBuilder buffer=new StringBuilder(updatedCommand.length()+this.windowsCommandPrefix.length()+1);
			
			//update command
			buffer.append(this.windowsCommandPrefix);
			buffer.append(" ");
			buffer.append(updatedCommand);
			updatedCommand=buffer.toString();
		}

		//execute process
		ProcessOutput processOutput=ProcessExecutorHelper.executeProcess(this,updatedCommand);

		//validate output (if not valid, an exception should be thrown)
		this.validateProcessOutput(processOutput,faxActionType);
		
		//update fax job
		this.updateFaxJob(faxJob,processOutput,faxActionType);
		
		return processOutput;
	}
	
	/**
	 * This function formats the provided template.
	 * 
	 * @param 	template
	 * 			The template
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The formatted template
	 */
	protected String formatTemplate(String template,FaxJob faxJob)
	{
		return SpiUtil.formatTemplate(template,faxJob,null,false,true);
	}

	/**
	 * This function returns the template for the given template name.
	 * 
	 * @param	templateName
	 * 			The template name
	 * @return	The template
	 */
	protected String getTemplate(String templateName)
	{
		//get template
		String template=this.getConfigurationValue(templateName);

		return template;
	}
	
	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param	templateNameEnum
	 * 			The template name
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	protected String createProcessCommand(Enum<?> templateNameEnum,FaxJob faxJob)
	{
		//get template name
		String templateName=templateNameEnum.toString();

		//get template
		String template=this.getTemplate(templateName);
		String command=null;
		if(template!=null)
		{
			//format template
			command=this.formatTemplate(template,faxJob);
		}

		return command;
	}
	
	/**
	 * This function validates the process output for errors.<br>
	 * If not valid, an exception should be thrown.
	 * 
	 * @param	processOutput
	 * 			The process output to validate
	 * @param	faxActionType
	 * 			The fax action type
	 */
	protected void validateProcessOutput(ProcessOutput processOutput,FaxActionType faxActionType)
	{
		this.processOutputValidator.validateProcessOutput(this,processOutput,faxActionType);
	}

	/**
	 * Updates the fax job based on the data from the process output.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @param 	processOutput
	 * 			The process output
	 * @param	faxActionType
	 * 			The fax action type
	 */
	protected void updateFaxJob(FaxJob faxJob,ProcessOutput processOutput,FaxActionType faxActionType)
	{
		if(this.processOutputHandler!=null)
		{
			//update fax job
			this.processOutputHandler.updateFaxJob(this,faxJob,processOutput,faxActionType);
		}
	}
	
	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	protected String createSubmitFaxJobProcessCommand(FaxJob faxJob)
	{
		return this.createProcessCommand(FaxClientSpiConfigurationConstants.SUBMIT_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY,faxJob);
	}

	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	protected String createSuspendFaxJobProcessCommand(FaxJob faxJob)
	{
		return this.createProcessCommand(FaxClientSpiConfigurationConstants.SUSPEND_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY,faxJob);
	}

	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	protected String createResumeFaxJobProcessCommand(FaxJob faxJob)
	{
		return this.createProcessCommand(FaxClientSpiConfigurationConstants.RESUME_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY,faxJob);
	}
	
	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	protected String createCancelFaxJobProcessCommand(FaxJob faxJob)
	{
		return this.createProcessCommand(FaxClientSpiConfigurationConstants.CANCEL_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY,faxJob);
	}
	
	/**
	 * Creates the process command from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The process command to execute
	 */
	protected String createGetStatusFaxJobProcessCommand(FaxJob faxJob)
	{
		return this.createProcessCommand(FaxClientSpiConfigurationConstants.GET_STATUS_FAX_JOB_TEMPLATE_PRE_FORMAT_PROPERTY_KEY,faxJob);
	}
}