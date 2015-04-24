package org.fax4j.spi.windows;

import java.util.Properties;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.Logger;
import org.fax4j.spi.FaxClientSpiFactory;
import org.fax4j.spi.adapter.AbstractAdapterFaxClientSpi;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will proxy the requests to windows native fax API.<br>
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
 * 	<tr>
 * 		<td>org.fax4j.spi.windows.use.jni</td>
 * 		<td>True to use JNI, false to use the external fax4j.exe</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>This SPI will only work on Windows OS (windows 2000 and up).
 * 	<li>See windows fax/tapi limitations.
 * 	<li>JNI set to true only supports 32 bit JVMs (Windows OS can be 64 bit).
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
 * @version 1.14
 * @since	0.1
 */
public class WindowsFaxClientSpi extends AbstractAdapterFaxClientSpi
{
	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The fax server name property key*/
		FAX_SERVER_NAME_PROPERTY_KEY("org.fax4j.spi.windows.server.name"),
		/**The use JNI flag property key*/
		USE_JNI_PROPERTY_KEY("org.fax4j.spi.windows.use.jni");

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
	public WindowsFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeChildFaxClientSpi()
	{
		//get logger
		Logger logger=this.getLogger();

		//get use JNI flag
		boolean useJNI=false;
		String useJNIStr=this.getConfigurationValue(FaxClientSpiConfigurationConstants.USE_JNI_PROPERTY_KEY);
		if(useJNIStr!=null)
		{
			useJNI=Boolean.parseBoolean(useJNIStr);
		}
		
		//extract native resources
		WindowsFaxClientSpiHelper.extractNativeResources();
		
		if(useJNI)
		{
			//load native library
			WindowsFaxClientSpiHelper.loadNativeLibrary(logger);
			
			if(!WindowsFaxClientSpiHelper.isNativeLibraryLoaded())
			{
				//if failed to load native DLL, use executable
				useJNI=false;
			}
		}
		
		//get class name for child SPI
		String className=WindowsProcessFaxClientSpi.class.getName();
		if(useJNI)
		{
			className=WindowsJNIFaxClientSpi.class.getName();
		}
		
		//create child SPI configuration
		Properties childConfiguration=new Properties();
		childConfiguration.putAll(this.getConfiguration());
		childConfiguration.put(FaxClientSpiFactory.SPI_CLASS_NAME_TYPE_MAP_PROPERTY_KEY_PREFIX+"windows.child",className);
		this.faxClientSpi=FaxClientSpiFactory.createChildFaxClientSpi("windows.child",childConfiguration);
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
		//validate fax job ID
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJob);
		
		super.suspendFaxJobImpl(faxJob);
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
		//validate fax job ID
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJob);
		
		super.resumeFaxJobImpl(faxJob);
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
		//validate fax job ID
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJob);
		
		super.cancelFaxJobImpl(faxJob);
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
		//validate fax job ID
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJob);
		
		return super.getFaxJobStatusImpl(faxJob);
	}
}