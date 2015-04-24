package org.fax4j;

import org.fax4j.common.FaxClientActions;
import org.fax4j.common.Logger;
import org.fax4j.spi.FaxClientSpi;

/**
 * This is the fax client.<br>
 * The fax client enables to perform activities such as sending/suspending
 * fax jobs and so on.<br>
 * This is the class which will be used by external applications to invoke the actual
 * fax related activites.<br>
 * To create a new instance of the fax client, you will need to invoke the FaxClientFactory.createFaxClient
 * method.<br>
 * Here is a sample code that creates a new fax client and submits a new fax job:<br>
 * <pre>
 * //get new instance of a fax client (based on internal + external fax4j.properties file data)
 * FaxClient faxClient=FaxClientFactory.createFaxClient();
 * 
 * //create a new fax job
 * FaxJob faxJob=faxClient.createFaxJob();
 * 
 * //set fax job values
 * faxJob.setFile(new File("./my_document.txt"));
 * faxJob.setPriority(FaxJobPriority.HIGH_PRIORITY);
 * faxJob.setTargetAddress("555-555");
 * faxJob.setTargetName("YourName");
 * faxJob.setSenderEmail("myemail@mycompany.com");
 * faxJob.setSenderName("MyName");
 * 
 * //submit fax job
 * faxClient.submitFaxJob(faxJob);
 * 
 * //print submitted fax job ID (may not be supported by all SPIs)
 * System.out.println("Fax job submitted, ID: "+faxJob.getID());
 * </pre>
 * The engine behind the fax client is the fax client SPI which is used internally.<br>
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
 * Below table describes the configuration values relevant for this class,
 * for SPI specific configuration, see the relevant SPI class javadoc.<br>
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
 * 		<td>org.fax4j.spi.type.map.xxx</td>
 * 		<td>For each SPI type, there is a property that (with prefix: org.fax4j.spi.type.map. 
 * 			and type name for example: org.fax4j.spi.type.map.adapter) which defines the SPI class name
 * 			for the given SPI type.<br>
 * 			For example:<br>
 * 			org.fax4j.spi.type.map.adapter=org.fax4j.spi.adapter.AdapterFaxClientSpi<br>
 * 			Means that for SPI type adapter, the SPI class to be used is org.fax4j.spi.adapter.AdapterFaxClientSpi</td>
 * 		<td>
 * 			org.fax4j.spi.type.map.adapter=org.fax4j.spi.adapter.AdapterFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.windows=org.fax4j.spi.windows.WindowsFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.vbs=org.fax4j.spi.vbs.VBSFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.mail=org.fax4j.spi.email.MailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.http=org.fax4j.spi.http.HTTPFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.process=org.fax4j.spi.process.ProcessFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.linux=org.fax4j.spi.linux.LinuxFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.mac=org.fax4j.spi.mac.MacFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.hylafax=org.fax4j.spi.hylafax.HylaFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.comm=org.fax4j.spi.comm.CommFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.interfax=org.fax4j.spi.interfax.InterfaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.nextivafax=org.fax4j.spi.nextivafax.NextivafaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.send2fax=org.fax4j.spi.send2fax.Send2FaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.metrofax=org.fax4j.spi.metrofax.MetroFaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.faxage=org.fax4j.spi.faxage.FaxAgeMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.extremefax=org.fax4j.spi.extremefax.ExtremeFaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.efax=org.fax4j.spi.efax.EFaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.phaxio=org.fax4j.spi.phaxio.PhaxioFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.hoiio=org.fax4j.spi.hoiio.HoiioFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.rfax=org.fax4j.spi.java4less.RFaxFaxClientSpi
 * 		</td>
 * 		<td>NA</td>
 * 		<td>NA</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.default.type</td>
 * 		<td>In case the SPI type was not provided in the createFaxClientSpi method, the type will be taken from
 * 			this property value.</td>
 * 		<td>adapter</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.logger.class.name</td>
 * 		<td>Defines the internal fax4j logger class name.<br>
 * 			The logger must implement the org.fax4j.common.Logger interface and have an empty constructor.<br>
 * 			This property can only be set in the internal fax4j.properties and the fax4j.properties on the classpath,
 * 			providing these values as part of the java.util.Properties input of the createFaxClientSpi will have no affect.
 * 		</td>
 * 		<td>org.fax4j.common.SimpleLogger</td>
 * 		<td>org.fax4j.common.SimpleLogger</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.logger.log.level</td>
 * 		<td>Defines the internal fax4j logger log level.<br>
 * 			The possible values are: DEBUG, INFO and ERROR.<br>
 * 			This property can only be set in the internal fax4j.properties and the fax4j.properties on the classpath,
 * 			providing these values as part of the java.util.Properties input of the createFaxClientSpi will have no effect.
 * 		</td>
 * 		<td>ERROR</td>
 * 		<td>ERROR</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.client.class.name</td>
 * 		<td>Defines the fax client class name.<br>
 * 			Since the fax client redirects the operations to the SPI (the engine) classes, creating a
 * 			custom fax client is not required.
 * 		</td>
 * 		<td>org.fax4j.FaxClient</td>
 * 		<td>org.fax4j.FaxClient</td>
 * 		<td>false</td>
 * </tr>
 * </table>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.08
 * @since	0.1
 */
public class FaxClient implements FaxClientActions
{
	/**The fax client SPI*/
	private final FaxClientSpi FAX_CLIENT_SPI;

	/**
	 * This is the default constructor.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 */
	public FaxClient(FaxClientSpi faxClientSpi)
	{
		super();
		
		//get SPI
		this.FAX_CLIENT_SPI=faxClientSpi;
		if(this.FAX_CLIENT_SPI==null)
		{
			throw new FaxException("Fax client SPI is null.");
		}
		
		//get logger
		Logger logger=this.FAX_CLIENT_SPI.getLogger();
		if(logger==null)
		{
			throw new FaxException("Fax client logger is null.");
		}
	}
	
	/**
	 * This function returns the fax client SPI.
	 *  
	 * @return	The fax client SPI
	 */
	protected final FaxClientSpi getFaxClientSpi()
	{
		return this.FAX_CLIENT_SPI;
	}

	/**
	 * This function returns the provider.
	 * 
	 * @return	The provider
	 */
	public final Provider getProvider()
	{
		return this.FAX_CLIENT_SPI.getProvider();
	}

	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	public FaxJob createFaxJob()
	{
		return this.FAX_CLIENT_SPI.createFaxJob();
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void submitFaxJob(FaxJob faxJob)
	{
		this.FAX_CLIENT_SPI.submitFaxJob(faxJob);
	}
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void suspendFaxJob(FaxJob faxJob)
	{
		this.FAX_CLIENT_SPI.suspendFaxJob(faxJob);
	}

	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void resumeFaxJob(FaxJob faxJob)
	{
		this.FAX_CLIENT_SPI.resumeFaxJob(faxJob);
	}
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void cancelFaxJob(FaxJob faxJob)
	{
		this.FAX_CLIENT_SPI.cancelFaxJob(faxJob);
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
	public FaxJobStatus getFaxJobStatus(FaxJob faxJob)
	{
		return this.FAX_CLIENT_SPI.getFaxJobStatus(faxJob);
	}
	
	/**
	 * This function adds the fax client action event listener to the internal fax
	 * event listeners data structure.
	 * 
	 * @param 	listener
	 * 			The fax client action event listener
	 */
	public void addFaxClientActionEventListener(FaxClientActionEventListener listener)
	{
		this.FAX_CLIENT_SPI.addFaxClientActionEventListener(listener);
	}
	
	/**
	 * This function removes the fax client action event listener from the internal fax
	 * event listeners data structure.
	 * 
	 * @param 	listener
	 * 			The fax client action event listener
	 */
	public void removeFaxClientActionEventListener(FaxClientActionEventListener listener)
	{
		this.FAX_CLIENT_SPI.removeFaxClientActionEventListener(listener);
	}
	
	/**
	 * This function removes all fax client action event listeners from the internal fax
	 * event listeners data structure.
	 */
	public void removeAllFaxClientActionEventListeners()
	{
		this.FAX_CLIENT_SPI.removeAllFaxClientActionEventListeners();
	}
	
	/**
	 * This function adds the fax monitor event listener to the internal fax
	 * event listeners data structure.<br>
	 * Fax jobs will be monitored only if there are active listeners registered.<br>
	 * If the listeners are added after a fob job was submitted, that fax job would not be monitored.<br>
	 * <b>Not all SPIs support monitoring events, in which case this method will throw an exception.</b>
	 * 
	 * @param 	listener
	 * 			The fax monitor event listener
	 */
	public void addFaxMonitorEventListener(FaxMonitorEventListener listener)
	{
		this.FAX_CLIENT_SPI.addFaxMonitorEventListener(listener);
	}
	
	/**
	 * This function removes the fax monitor event listener from the internal fax
	 * event listeners data structure.<br>
	 * <b>Not all SPIs support monitoring events.</b>
	 * 
	 * @param 	listener
	 * 			The fax monitor event listener
	 */
	public void removeFaxMonitorEventListener(FaxMonitorEventListener listener)
	{
		this.FAX_CLIENT_SPI.removeFaxMonitorEventListener(listener);
	}
	
	/**
	 * This function removes all fax monitor event listeners from the internal fax
	 * event listeners data structure.<br>
	 * <b>Not all SPIs support monitoring events.</b>
	 */
	public void removeAllFaxMonitorEventListeners()
	{
		this.FAX_CLIENT_SPI.removeAllFaxMonitorEventListeners();
	}
}