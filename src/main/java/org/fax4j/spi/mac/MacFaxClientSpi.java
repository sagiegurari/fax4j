package org.fax4j.spi.mac;

import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.spi.process.ExitCodeProcessOutputValidator;
import org.fax4j.spi.process.ProcessOutputValidator;
import org.fax4j.util.ProcessExecutorHelper;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;
import org.fax4j.util.SpiUtil;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will proxy the requests to mac native fax commands.<br>
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
 * <b>SPI Status (Draft, Beta, Stable): </b>Beta<br>
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
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mac.submit.command</td>
 * 		<td>The submit fax job command</td>
 * 		<td>lp</td>
 * 		<td>lp</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mac.print.queue.parameter</td>
 * 		<td>The print queue parameter</td>
 * 		<td>-d</td>
 * 		<td>-d</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mac.print.queue.name</td>
 * 		<td>The print queue name</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mac.general.parameters</td>
 * 		<td>The general parameters</td>
 * 		<td>-o</td>
 * 		<td>-o</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mac.phone.parameter</td>
 * 		<td>The phone parameter</td>
 * 		<td>phone</td>
 * 		<td>phone</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mac.fax.to.parameter</td>
 * 		<td>The fax to parameter</td>
 * 		<td>faxTo</td>
 * 		<td>faxTo</td>
 * 		<td>false</td>
 * </tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>This SPI will only work on Mac OS.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.07
 * @since	0.21e
 */
public class MacFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The submit fax job command*/
	private String submitCommand;
	/**The print queue parameter*/
	private String printQueueParameter;
	/**The print queue name*/
	private String printQueueName;
	/**The general parameters*/
	private String generalParameters;
	/**The phone parameter*/
	private String phoneParameter;
	/**The fax to parameter*/
	private String faxToParameter;
	/**The process output validator*/
	private ProcessOutputValidator processOutputValidator;
	
	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.21e
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The submit fax job command property key*/
		SUBMIT_FAX_JOB_COMMAND_PROPERTY_KEY("org.fax4j.spi.mac.submit.command"),
		/**The print queue parameter property key*/
		PRINT_QUEUE_PARAMETER_PROPERTY_KEY("org.fax4j.spi.mac.print.queue.parameter"),
		/**The print queue name property key*/
		PRINT_QUEUE_NAME_PROPERTY_KEY("org.fax4j.spi.mac.print.queue.name"),
		/**The general parameters property key*/
		GENERAL_PARAMETERS_PROPERTY_KEY("org.fax4j.spi.mac.general.parameters"),
		/**The phone parameter property key*/
		PHONE_PARAMETER_PROPERTY_KEY("org.fax4j.spi.mac.phone.parameter"),
		/**The fax to parameter property key*/
		FAX_TO_PARAMETER_PROPERTY_KEY("org.fax4j.spi.mac.fax.to.parameter"),
		/**The submit fax job command default value*/
		SUBMIT_FAX_JOB_COMMAND_DEFAULT_VALUE("lp"),
		/**The print queue parameter default value*/
		PRINT_QUEUE_PARAMETER_DEFAULT_VALUE("-d"),
		/**The general parameters default value*/
		GENERAL_PARAMETERS_DEFAULT_VALUE("-o"),
		/**The phone parameter default value*/
		PHONE_PARAMETER_DEFAULT_VALUE("phone"),
		/**The fax to parameter default value*/
		FAX_TO_PARAMETER_DEFAULT_VALUE("faxTo");

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
	public MacFaxClientSpi()
	{
		super();
	}

	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//get values
		this.submitCommand=this.getConfigurationValue(FaxClientSpiConfigurationConstants.SUBMIT_FAX_JOB_COMMAND_PROPERTY_KEY);
		if(this.submitCommand==null)
		{
			this.submitCommand=FaxClientSpiConfigurationConstants.SUBMIT_FAX_JOB_COMMAND_DEFAULT_VALUE.toString();
		}
		this.printQueueParameter=this.getConfigurationValue(FaxClientSpiConfigurationConstants.PRINT_QUEUE_PARAMETER_PROPERTY_KEY);
		if(this.printQueueParameter==null)
		{
			this.printQueueParameter=FaxClientSpiConfigurationConstants.PRINT_QUEUE_PARAMETER_DEFAULT_VALUE.toString();
		}
		this.printQueueName=this.getConfigurationValue(FaxClientSpiConfigurationConstants.PRINT_QUEUE_NAME_PROPERTY_KEY);
		if(this.printQueueName==null)
		{
			throw new FaxException("Print queue name not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.PRINT_QUEUE_NAME_PROPERTY_KEY);
		}
		this.generalParameters=this.getConfigurationValue(FaxClientSpiConfigurationConstants.GENERAL_PARAMETERS_PROPERTY_KEY);
		if(this.generalParameters==null)
		{
			this.generalParameters=FaxClientSpiConfigurationConstants.GENERAL_PARAMETERS_DEFAULT_VALUE.toString();
		}
		this.phoneParameter=this.getConfigurationValue(FaxClientSpiConfigurationConstants.PHONE_PARAMETER_PROPERTY_KEY);
		if(this.phoneParameter==null)
		{
			this.phoneParameter=FaxClientSpiConfigurationConstants.PHONE_PARAMETER_DEFAULT_VALUE.toString();
		}
		this.faxToParameter=this.getConfigurationValue(FaxClientSpiConfigurationConstants.FAX_TO_PARAMETER_PROPERTY_KEY);
		if(this.faxToParameter==null)
		{
			this.faxToParameter=FaxClientSpiConfigurationConstants.FAX_TO_PARAMETER_DEFAULT_VALUE.toString();
		}
		
		//init process output validator
		this.processOutputValidator=this.createMacProcessOutputValidator();
	}
	
	/**
	 * This function creates and returns the process output validator.
	 * 
	 * @return	The process output validator
	 */
	protected ProcessOutputValidator createMacProcessOutputValidator()
	{
		ProcessOutputValidator validator=new ExitCodeProcessOutputValidator();

		return validator;
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
		String command=this.createSubmitFaxCommand(faxJob);
		
		//execute process
		this.executeProcess(command,FaxActionType.SUBMIT_FAX_JOB);
	}
	
	/**
	 * Creates and returns the submit fax command.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @return	The command
	 */
	protected String createSubmitFaxCommand(FaxJob faxJob)
	{
		//init buffer
		StringBuilder buffer=new StringBuilder();
		
		//create command
		buffer.append(this.submitCommand);
		buffer.append(" ");
		buffer.append(this.printQueueParameter);
		buffer.append(" ");
		buffer.append(this.printQueueName);
		buffer.append(" ");
		buffer.append(this.generalParameters);
		buffer.append(" ");
		buffer.append(this.phoneParameter);
		buffer.append("=");
		buffer.append(faxJob.getTargetAddress());
		String targetName=faxJob.getTargetName();
		if((targetName!=null)&&(targetName.length()>0))
		{
			buffer.append(" ");
			buffer.append(this.faxToParameter);
			buffer.append("=\"");
			targetName=SpiUtil.urlEncode(targetName);
			buffer.append(targetName);
			buffer.append("\"");
		}
		buffer.append(" \"");
		buffer.append(faxJob.getFilePath());
		buffer.append("\"");

		//get command
		String command=buffer.toString();
		
		return command;
	}
	
	/**
	 * This function executes the external command to send the fax.
	 * 
	 * @param 	command
	 * 			The command to execute
	 * @param	faxActionType
	 * 			The fax action type
	 * @return	The process output
	 */
	protected ProcessOutput executeProcess(String command,FaxActionType faxActionType)
	{
		//execute process
		ProcessOutput processOutput=ProcessExecutorHelper.executeProcess(this,command);

		//validate process output
		this.processOutputValidator.validateProcessOutput(this,processOutput,faxActionType);
		
		return processOutput;
	}
}