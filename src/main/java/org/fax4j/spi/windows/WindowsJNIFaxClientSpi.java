package org.fax4j.spi.windows;

import java.io.File;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.LogLevel;
import org.fax4j.common.Logger;
import org.fax4j.spi.AbstractFax4JClientSpi;

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
 * 	<li>JNI only supported on 32 bit JVMs (Windows OS can be 64 bit).
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
 * @version 1.0
 * @since	0.41.5
 */
public class WindowsJNIFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The fax server name*/
	private String faxServerName;

	/**
	 * This is the default constructor.
	 */
	public WindowsJNIFaxClientSpi()
	{
		super();
	}

	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//get fax server name
		this.faxServerName=WindowsFaxClientSpiHelper.getServerNameFromConfiguration(this);
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
	 * This function is invoked before any native call to set the
	 * native layer debug mode.
	 */
	protected void preNativeCall()
	{
		//get logger
		Logger logger=this.getLogger();
		
		//get log level
		LogLevel logLevel=logger.getLogLevel();
		
		boolean debugMode=false;
		if(logLevel.equals(LogLevel.DEBUG))
		{
			debugMode=true;
		}
		
		//set debug mode
		WindowsJNIFaxClientSpi.setDebugModeNative(debugMode);
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
		//get fax job values
		String targetAddress=faxJob.getTargetAddress();
		String targetName=faxJob.getTargetName();
		if(targetName==null)
		{
			targetName="";
		}
		String senderName=faxJob.getSenderName();
		if(senderName==null)
		{
			senderName="";
		}
		File file=faxJob.getFile();
		String filePath=null;
		try
		{
			filePath=file.getCanonicalPath();
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to extract canonical path from file: "+file,exception);
		}
		
		//invoke fax action
		int faxJobIDInt=this.winSubmitFaxJob(this.faxServerName,targetAddress,targetName,senderName,filePath);
		
		//validate fax job ID
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJobIDInt);
		
		//set fax job ID
		String faxJobID=String.valueOf(faxJobIDInt);
		faxJob.setID(faxJobID);
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
		//get fax job ID
		int faxJobIDInt=WindowsFaxClientSpiHelper.getFaxJobID(faxJob);

		//invoke fax action
		this.winSuspendFaxJob(this.faxServerName,faxJobIDInt);
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
		//get fax job ID
		int faxJobIDInt=WindowsFaxClientSpiHelper.getFaxJobID(faxJob);

		//invoke fax action
		this.winResumeFaxJob(this.faxServerName,faxJobIDInt);
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
		//get fax job ID
		int faxJobIDInt=WindowsFaxClientSpiHelper.getFaxJobID(faxJob);

		//invoke fax action
		this.winCancelFaxJob(this.faxServerName,faxJobIDInt);
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
		//get fax job ID
		int faxJobIDInt=WindowsFaxClientSpiHelper.getFaxJobID(faxJob);

		//invoke fax action
		String faxJobStatusStr=this.winGetFaxJobStatus(this.faxServerName,faxJobIDInt);

		//get fax job status
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString(faxJobStatusStr);

		return faxJobStatus;
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID will be returned by this method.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param	targetAddress
	 * 			The fax job target address
	 * @param	targetName
	 * 			The fax job target name
	 * @param	senderName
	 * 			The fax job sender name
	 * @param	fileName
	 * 			The file to fax
	 * @return	The fax job ID (null in case of an error)
	 */
	private int winSubmitFaxJob(String serverName,String targetAddress,String targetName,String senderName,String fileName)
	{
		int faxJobID=0;
		synchronized(WindowsFaxClientSpiHelper.NATIVE_LOCK)
		{
			//pre native call
			this.preNativeCall();
			
			//invoke native
			faxJobID=WindowsJNIFaxClientSpi.submitFaxJobNative(serverName,targetAddress,targetName,senderName,fileName);
		}
		
		return faxJobID;
	}
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 */
	private void winSuspendFaxJob(String serverName,int faxJobID)
	{
		synchronized(WindowsFaxClientSpiHelper.NATIVE_LOCK)
		{
			//pre native call
			this.preNativeCall();
			
			//invoke native
			WindowsJNIFaxClientSpi.suspendFaxJobNative(serverName,faxJobID);
		}
	}
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 */
	private void winResumeFaxJob(String serverName,int faxJobID)
	{
		synchronized(WindowsFaxClientSpiHelper.NATIVE_LOCK)
		{
			//pre native call
			this.preNativeCall();
			
			//invoke native
			WindowsJNIFaxClientSpi.resumeFaxJobNative(serverName,faxJobID);
		}
	}
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 */
	private void winCancelFaxJob(String serverName,int faxJobID)
	{
		synchronized(WindowsFaxClientSpiHelper.NATIVE_LOCK)
		{
			//pre native call
			this.preNativeCall();
			
			//invoke native
			WindowsJNIFaxClientSpi.cancelFaxJobNative(serverName,faxJobID);
		}
	}

	/**
	 * This function returns the fax job status.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 * @return	The fax job status
	 */
	private String winGetFaxJobStatus(String serverName,int faxJobID)
	{
		String status=null;
		synchronized(WindowsFaxClientSpiHelper.NATIVE_LOCK)
		{
			//pre native call
			this.preNativeCall();
			
			//invoke native
			status=WindowsJNIFaxClientSpi.getFaxJobStatusNative(serverName,faxJobID);
		}
		
		return status;
	}
	
	/**
	 * This function sets the debug mode of the native layer.
	 * 
	 * @param 	debug
	 * 			True to print debug messages, otherwise false
	 */
	private static native void setDebugModeNative(boolean debug);
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID will be returned by this method.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param	targetAddress
	 * 			The fax job target address
	 * @param	targetName
	 * 			The fax job target name
	 * @param	senderName
	 * 			The fax job sender name
	 * @param	fileName
	 * 			The file to fax
	 * @return	The fax job ID (null in case of an error)
	 */
	private static native int submitFaxJobNative(String serverName,String targetAddress,String targetName,String senderName,String fileName);
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 */
	private static native void suspendFaxJobNative(String serverName,int faxJobID);
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 */
	private static native void resumeFaxJobNative(String serverName,int faxJobID);
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 */
	private static native void cancelFaxJobNative(String serverName,int faxJobID);

	/**
	 * This function returns the fax job status.
	 * 
	 * @param 	serverName
	 * 			The fax server name
	 * @param 	faxJobID
	 * 			The fax job ID
	 * @return	The fax job status
	 */
	private static native String getFaxJobStatusNative(String serverName,int faxJobID);
}