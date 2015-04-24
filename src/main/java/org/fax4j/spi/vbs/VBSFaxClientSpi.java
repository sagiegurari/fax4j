package org.fax4j.spi.vbs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.common.Logger;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.spi.process.ProcessOutputHandler;
import org.fax4j.spi.process.ProcessOutputValidator;
import org.fax4j.util.IOHelper;
import org.fax4j.util.ProcessExecutorHelper;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will proxy the requests to VB scripts that will invoke the windows fax API.<br>
 * <b>It is highly recommanded that the windows SPI will be used instead of this SPI.</b><br>
 * It is possible to see all faxes submitted from this SPI in the windows fax window available from the
 * Start&#8594;Settings&#8594;Printers and Faxes&#8594;Fax<br>
 * The executable used to invoke the VBS is defaulted to cscript. 
 * It is possible to set a different executable by setting the <b>org.fax4j.spi.vbs.exe.path property</b>.<br>
 * By default the windows 2000 vbs API will be used on all platforms.<br> 
 * In order for other platforms (other then windows 2000) to invoke the windows XP API, 
 * the <b>org.fax4j.spi.vbs.always.use.win2000.api</b> property must be set to false.<br>
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
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.vbs.server.name</td>
 * 		<td>The fax server host name.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.vbs.exe.path</td>
 * 		<td>The VBS executable.</td>
 * 		<td>cscript.exe</td>
 * 		<td>cscript.exe</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.vbs.always.use.win2000.api</td>
 * 		<td>By default the windows 2000 vbs API will be used on all platforms.<br> 
 * 			In order for other platforms (other then windows 2000) to invoke the windows XP API,
 * 			this property must be set to false.
 *		</td>
 * 		<td>true</td>
 * 		<td>true</td>
 * 		<td>false</td>
 * </tr>
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
 * 	<li>Required native executable: cscript.exe
 * 	<li>Windows OS (windows 2000 and up) - see http://msdn.microsoft.com/en-us/library/ms692855(VS.85).aspx for more info.
 * 	<li>Windows fax component installed.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.26
 * @since	0.1
 */
public class VBSFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The fax server name*/
	protected String faxServerName;
	/**The VBS exe path*/
	protected String vbsExePath;
	/**The use windows 2000 API flag*/
	protected boolean useWin2kAPI;
	/**The process output validator*/
	private ProcessOutputValidator processOutputValidator;
	/**The process output handler*/
	private ProcessOutputHandler processOutputHandler;
	/**The fax VBS scripts*/
	private static final Map<String,String> VBS_SCRIPTS=new HashMap<String,String>();
	/**The fax vbs script*/
	private static final String VBS_SCRIPT="fax.vbs";
	/**The windows XP submit fax vbs script*/
	private static final String VBS_WIN_XP_SUBMIT_SCRIPT="winxp_submit_fax.vbs";
	/**The windows 2000 submit fax vbs script*/
	private static final String VBS_WIN_2K_SUBMIT_SCRIPT="win2k_submit_fax.vbs";
	/**The windows XP suspend fax vbs script*/
	private static final String VBS_WIN_XP_SUSPEND_SCRIPT="winxp_suspend_fax.vbs";
	/**The windows 2000 suspend fax vbs script*/
	private static final String VBS_WIN_2K_SUSPEND_SCRIPT="win2k_suspend_fax.vbs";
	/**The windows XP resume fax vbs script*/
	private static final String VBS_WIN_XP_RESUME_SCRIPT="winxp_resume_fax.vbs";
	/**The windows 2000 resume fax vbs script*/
	private static final String VBS_WIN_2K_RESUME_SCRIPT="win2k_resume_fax.vbs";
	/**The windows XP cancel fax vbs script*/
	private static final String VBS_WIN_XP_CANCEL_SCRIPT="winxp_cancel_fax.vbs";
	/**The windows 2000 cancel fax vbs script*/
	private static final String VBS_WIN_2K_CANCEL_SCRIPT="win2k_cancel_fax.vbs";
	/**The windows XP get fax job status vbs script*/
	private static final String VBS_WIN_XP_GET_FAX_JOB_STATUS_SCRIPT="winxp_get_fax_job_status.vbs";
	/**The windows 2000 get fax job status vbs script*/
	private static final String VBS_WIN_2K_GET_FAX_JOB_STATUS_SCRIPT="win2k_get_fax_job_status.vbs";
	/**The VBS script names*/
	private static final String[] VSB_SCRIPT_NAMES=new String[]{VBSFaxClientSpi.VBS_SCRIPT,
		VBSFaxClientSpi.VBS_WIN_XP_SUBMIT_SCRIPT,VBSFaxClientSpi.VBS_WIN_2K_SUBMIT_SCRIPT,
		VBSFaxClientSpi.VBS_WIN_XP_SUSPEND_SCRIPT,VBSFaxClientSpi.VBS_WIN_2K_SUSPEND_SCRIPT,
		VBSFaxClientSpi.VBS_WIN_XP_RESUME_SCRIPT,VBSFaxClientSpi.VBS_WIN_2K_RESUME_SCRIPT,
		VBSFaxClientSpi.VBS_WIN_XP_CANCEL_SCRIPT,VBSFaxClientSpi.VBS_WIN_2K_CANCEL_SCRIPT,
		VBSFaxClientSpi.VBS_WIN_XP_GET_FAX_JOB_STATUS_SCRIPT,VBSFaxClientSpi.VBS_WIN_2K_GET_FAX_JOB_STATUS_SCRIPT};
	/**The windows 2000 OS name*/
	public static final String WIN_2K_OS_NAME="Windows 2000";

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.03
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The fax server name property key*/
		FAX_SERVER_NAME_PROPERTY_KEY("org.fax4j.spi.vbs.server.name"),
		/**The VBS exe path property key*/
		VBS_EXE_PATH_PROPERTY_KEY("org.fax4j.spi.vbs.exe.path"),
		/**The always use win2k API property key*/
		VBS_ALWAYS_USE_WIN2K_API_PROPERTY_KEY("org.fax4j.spi.vbs.always.use.win2000.api"),
		/**The VBS exe path default value*/
		VBS_EXE_PATH_DEFAULT_VALUE("cscript");

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
	 * This class holds the fax job extended properties.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.1
	 */
	public enum FaxJobExtendedPropertyConstants
	{
		/**The document name fax job input*/
		DOCUMENT_NAME_FAX_JOB_PROPERTY_KEY("document.name");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxJobExtendedPropertyConstants(String value)
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

	static
	{
		//load scripts
		int amount=VBSFaxClientSpi.VSB_SCRIPT_NAMES.length;
		String name=null;
		InputStream inputStream=null;
		String content=null;
		for(int index=0;index<amount;index++)
		{
			//get next element
			name=VBSFaxClientSpi.VSB_SCRIPT_NAMES[index];
			
			//get input stream
			inputStream=VBSFaxClientSpi.class.getResourceAsStream(name);
			
			try
			{
				//read next script
				Reader reader=IOHelper.createReader(inputStream,null);
				content=IOHelper.readTextStream(reader);
				IOHelper.closeResource(inputStream);
			}
			catch(Exception exception)
			{
				IOHelper.closeResource(inputStream);
				throw new FaxException("Unable to read internal script: "+name,exception);
			}
			
			//put in map
			VBSFaxClientSpi.VBS_SCRIPTS.put(name,content);
		}
	}
	
	/**
	 * This is the default constructor.
	 */
	public VBSFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//get logger
		Logger logger=this.getLogger();
		
		//get fax server name
		this.faxServerName=this.getConfigurationValue(FaxClientSpiConfigurationConstants.FAX_SERVER_NAME_PROPERTY_KEY);
		if(this.faxServerName==null)
		{
			this.faxServerName="";
		}
		logger.logDebug(new Object[]{"Fax server name: ",this.faxServerName},null);
		
		//get vbs exe path
		this.vbsExePath=this.getConfigurationValue(FaxClientSpiConfigurationConstants.VBS_EXE_PATH_PROPERTY_KEY);
		if(this.vbsExePath==null)
		{
			this.vbsExePath=FaxClientSpiConfigurationConstants.VBS_EXE_PATH_DEFAULT_VALUE.toString();
		}
		logger.logDebug(new Object[]{"VBS exe path: ",this.vbsExePath},null);
		
		//get the win2k flag
		String valueStr=this.getConfigurationValue(FaxClientSpiConfigurationConstants.VBS_ALWAYS_USE_WIN2K_API_PROPERTY_KEY);
		if(valueStr==null)
		{
			valueStr=Boolean.toString(true);
		}
		boolean win2kAPI=Boolean.parseBoolean(valueStr);
		if(!win2kAPI)
		{
			//get OS name
			String osName=System.getProperty("os.name");
			
			if(osName.equalsIgnoreCase(VBSFaxClientSpi.WIN_2K_OS_NAME))
			{
				win2kAPI=true;
			}
		}
		this.useWin2kAPI=win2kAPI;
		logger.logDebug(new Object[]{"Use windows 2000 API: ",Boolean.toString(this.useWin2kAPI)},null);
		
		//create process handler and validator
		this.processOutputValidator=new VBSProcessOutputValidator();
		this.processOutputHandler=new VBSProcessOutputHandler();
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
	 * This function return the VBS exe path.
	 *  
	 * @return	The VBS exe path
	 */
	protected String getVBSExePath()
	{
		return this.vbsExePath;
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
		String name=VBSFaxClientSpi.VBS_WIN_XP_SUBMIT_SCRIPT;
		if(this.useWin2kAPI)
		{
			name=VBSFaxClientSpi.VBS_WIN_2K_SUBMIT_SCRIPT;
		}
		
		//setup input data
		Object[] input=setupSubmitFaxJobInput(faxJob);
		
		//invoke script
		this.invokeScript(faxJob,name,input,FaxActionType.SUBMIT_FAX_JOB);
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
		String name=VBSFaxClientSpi.VBS_WIN_XP_SUSPEND_SCRIPT;
		if(this.useWin2kAPI)
		{
			name=VBSFaxClientSpi.VBS_WIN_2K_SUSPEND_SCRIPT;
		}

		//invoke script
		this.invokeExistingFaxJobAction(name,faxJob,FaxActionType.SUSPEND_FAX_JOB);
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
		String name=VBSFaxClientSpi.VBS_WIN_XP_RESUME_SCRIPT;
		if(this.useWin2kAPI)
		{
			name=VBSFaxClientSpi.VBS_WIN_2K_RESUME_SCRIPT;
		}

		//invoke script
		this.invokeExistingFaxJobAction(name,faxJob,FaxActionType.RESUME_FAX_JOB);
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
		String name=VBSFaxClientSpi.VBS_WIN_XP_CANCEL_SCRIPT;
		if(this.useWin2kAPI)
		{
			name=VBSFaxClientSpi.VBS_WIN_2K_CANCEL_SCRIPT;
		}

		//invoke script
		this.invokeExistingFaxJobAction(name,faxJob,FaxActionType.CANCEL_FAX_JOB);
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
		String name=VBSFaxClientSpi.VBS_WIN_XP_GET_FAX_JOB_STATUS_SCRIPT;
		if(this.useWin2kAPI)
		{
			name=VBSFaxClientSpi.VBS_WIN_2K_GET_FAX_JOB_STATUS_SCRIPT;
		}

		//invoke script
		FaxJobStatus faxJobStatus=this.invokeExistingFaxJobAction(name,faxJob,FaxActionType.GET_FAX_JOB_STATUS);

		return faxJobStatus;
	}
	
	/**
	 * This function creates an input array with the needed info
	 * to submit a new fax job based on the provided data.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @return	The submit fax job script input
	 */
	protected Object[] setupSubmitFaxJobInput(FaxJob faxJob)
	{
		//init list
		List<Object> inputList=new LinkedList<Object>();
		
		//set fax server information
		inputList.add(this.faxServerName);

		//set fax values
		File file=faxJob.getFile();
		inputList.add(file);
		String documentName=faxJob.getProperty(FaxJobExtendedPropertyConstants.DOCUMENT_NAME_FAX_JOB_PROPERTY_KEY.toString(),null);
		if((documentName==null)||(documentName.length()==0))
		{
			documentName=file.getName();
		}
		inputList.add(documentName);

		if(this.useWin2kAPI)
		{
			//set target information
			inputList.add(faxJob.getTargetAddress());
			inputList.add(faxJob.getTargetName());

			//set sender information
			inputList.add(faxJob.getSenderName());
			inputList.add(faxJob.getSenderFaxNumber());
		}
		else
		{
			FaxJobPriority priority=faxJob.getPriority();
			String valueStr="fptNORMAL";
			if(priority!=null)
			{
				switch(priority)
				{
					case LOW_PRIORITY:
						valueStr="fptLOW";
						break;
					case MEDIUM_PRIORITY:
						valueStr="fptNORMAL";
						break;
					case HIGH_PRIORITY:
						valueStr="fptHIGH";
						break;
				}
			}
			inputList.add(valueStr);

			//set target information
			inputList.add(faxJob.getTargetAddress());
			inputList.add(faxJob.getTargetName());

			//set sender information
			inputList.add(faxJob.getSenderName());
			inputList.add(faxJob.getSenderFaxNumber());
			inputList.add(faxJob.getSenderEmail());
		}
		
		//convert to array
		int size=inputList.size();
		Object[] input=inputList.toArray(new Object[size]);
		
		return input;
	}

	/**
	 * This function formats the provided object to enable embedding
	 * in VBS code.
	 * 
	 * @param 	object
	 * 			The object to format
	 * @return	The formatted object
	 */
	protected Object formatObject(Object object)
	{
		Object formattedObject=object;
		if(object==null)
		{
			formattedObject="";
		}
		else if(object instanceof String)
		{
			//get string
			String string=(String)object;
			
			//remove characters
			string=string.replaceAll("\n","");
			string=string.replaceAll("\r","");
			string=string.replaceAll("\t","");
			string=string.replaceAll("\f","");
			string=string.replaceAll("\b","");
			string=string.replaceAll("'","");
			string=string.replaceAll("\"","");
			
			//get reference
			formattedObject=string;
		}
		else if(object instanceof File)
		{
			//get file
			File file=(File)object;

			String filePath=null;
			try
			{
				filePath=file.getCanonicalPath();
			}
			catch(IOException exception)
			{
				throw new FaxException("Unable to get file path.",exception);
			}
			filePath=filePath.replaceAll("\\\\","\\\\\\\\");
			
			//get reference
			formattedObject=filePath;
		}
		
		return formattedObject;
	}
	
	/**
	 * Invokes a basic fax action
	 *  
	 * @param 	scriptName
	 * 			The script name
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param	faxActionType
	 * 			The fax action type
	 * @return	The fax job status (only for get fax job status action, for others null will be returned)
	 */
	protected FaxJobStatus invokeExistingFaxJobAction(String scriptName,FaxJob faxJob,FaxActionType faxActionType)
	{
		//initialize array
		Object[] input=new String[2];

		//set fax server information
		input[0]=this.faxServerName;
		
		//set fax job ID
		input[1]=faxJob.getID();
		
		//invoke script
		return this.invokeScript(faxJob,scriptName,input,faxActionType);
	}
	
	/**
	 * Invokes the VB script and returns its output.
	 *  
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param 	name
	 * 			The script name
	 * @param	input
	 * 			The script input
	 * @param	faxActionType
	 * 			The fax action type
	 * @return	The fax job status (only for get fax job status action, for others null will be returned)
	 */
	protected FaxJobStatus invokeScript(FaxJob faxJob,String name,Object[] input,FaxActionType faxActionType)
	{
		//generate script
		String script=this.generateScript(name,input);
		
		//invoke script
		ProcessOutput processOutput=this.invokeScript(script);
		
		//validate output
		this.processOutputValidator.validateProcessOutput(this,processOutput,faxActionType);
		
		//handle output
		FaxJobStatus output=null;
		switch(faxActionType)
		{
			case SUBMIT_FAX_JOB:
				this.processOutputHandler.updateFaxJob(this,faxJob,processOutput,faxActionType);
				break;
			case GET_FAX_JOB_STATUS:
				output=this.processOutputHandler.getFaxJobStatus(this,processOutput);
				break;
			default:
				//do nothing
				break;
		}

		return output;
	}

	/**
	 * Invokes the VB script and returns the output.
	 *  
	 * @param	script
	 * 			The script to invoke
	 * @return	The script output
	 */
	protected ProcessOutput invokeScript(String script)
	{
		File file=null;
		try
		{
			//create temporary file
			file=File.createTempFile("fax4j_",".vbs");
		}
		catch(IOException exception)
		{
			throw new FaxException("Unable to create temporary vbscript file.",exception);
		}
		file.deleteOnExit();
		
		//generate command string
		StringBuilder buffer=new StringBuilder();
		buffer.append(this.getVBSExePath());
		buffer.append(" \"");
		buffer.append(file.getAbsolutePath());
		buffer.append("\"");
		String command=buffer.toString();

		try
		{
			//write script to file
			IOHelper.writeTextFile(script,file);
		}
		catch(IOException exception)
		{
			throw new FaxException("Unable to write vbscript to temporary file.",exception);
		}
		
		//get logger
		Logger logger=this.getLogger();
		logger.logDebug(new Object[]{"Invoking command: ",command," script:",Logger.SYSTEM_EOL,script},null);
		
		//execute command
		ProcessOutput vbsOutput=ProcessExecutorHelper.executeProcess(this,command);
		
		//get exit code
		int exitCode=vbsOutput.getExitCode();
		
		//delete temp file
		boolean fileDeleted=file.delete();
		logger.logDebug(new Object[]{"Temp script file deleted: ",String.valueOf(fileDeleted)},null);
		
		if(exitCode!=0)
		{
			throw new FaxException("Error while invoking script, exit code: "+exitCode+" script output:\n"+vbsOutput.getOutputText()+"\nScript error:\n"+vbsOutput.getErrorText());
		}
		
		return vbsOutput;
	}

	/**
	 * This function generates the script and returns it.
	 * 
	 * @param 	name
	 * 			The script name
	 * @param 	input
	 * 			The script input
	 * @return	The formatted script
	 */
	protected String generateScript(String name,Object[] input)
	{
		//get template
		String template=VBSFaxClientSpi.VBS_SCRIPTS.get(name);
		if((template==null)||(template.length()==0))
		{
			this.throwUnsupportedException();
		}
		
		//get common script
		String commonScript=VBSFaxClientSpi.VBS_SCRIPTS.get(VBSFaxClientSpi.VBS_SCRIPT);
		
		//format input
		Object[] formattedInput=null;
		if(input!=null)
		{
			//get size
			int size=input.length;
			
			//create array
			formattedInput=new Object[size];
			
			Object object=null;
			for(int index=0;index<size;index++)
			{
				//get next element
				object=input[index];
				
				//format object
				object=this.formatObject(object);
				
				//push to array
				formattedInput[index]=object;
			}
		}
		
		//push input to template
		String updatedScript=MessageFormat.format(template,formattedInput);
		
		//merge scripts
		StringBuilder buffer=new StringBuilder(commonScript.length()+updatedScript.length());
		buffer.append(commonScript);
		buffer.append(updatedScript);
		String script=buffer.toString();
		
		return script;
	}
}