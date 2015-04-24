package org.fax4j.bridge;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.fax4j.FaxClient;
import org.fax4j.FaxClientFactory;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxMonitorEventListener;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.ConfigurationHolderImpl;
import org.fax4j.common.Logger;
import org.fax4j.common.LoggerManager;
import org.fax4j.util.SpiUtil;

/**
 * This class partial implementation of the fax bridge.<br>
 * Specific implementations may provide additional, more transport based capabilities.
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
 * 		<td>org.fax4j.bridge.vendor.policy.class.name</td>
 * 		<td>The vendor policy class name.</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.05
 * @since	0.40.6
 */
public abstract class AbstractFaxBridge implements FaxBridge
{
	/**The initialized flag*/
	private boolean initialized;
	/**The fax bridge configuration*/
	private ConfigurationHolder bridgeConfiguration;
	/**The logger*/
	private Logger bridgeLogger;
	/**The fax client*/
	private FaxClient faxClient;
	/**The fax client*/
	private VendorPolicy vendorPolicy;

	/**
	 * This is the class constructor.
	 */
	public AbstractFaxBridge()
	{
		super();
		
		//set flag
		this.initialized=false;
	}

	/**
	 * This function initializes the fax bridge.
	 * 
	 * @param	type
	 * 			The fax client type (may be null for default type)
	 * @param	configuration
	 * 			The fax client configuration (may be null)
	 * @param 	flowOwner
	 * 			The flow owner (servlet, CLI main, ....) to be passed to the vendor policy
	 */
	public final synchronized void initialize(String type,Properties configuration,Object flowOwner)
	{
		if(this.initialized)
		{
			throw new FaxException("Fax bridge already initialized.");
		}
		
		//set flag
		this.initialized=true;

		//get configuration
		Map<String,String> map=new HashMap<String,String>();
		SpiUtil.copyPropertiesToMap(configuration,map);
		this.bridgeConfiguration=new ConfigurationHolderImpl(map);

		//create fax client
		this.faxClient=FaxClientFactory.createFaxClient(type,configuration);

		//get logger
		LoggerManager loggerManager=LoggerManager.getInstance();
		this.bridgeLogger=loggerManager.getLogger();

		//create vendor policy
		this.vendorPolicy=this.createVendorPolicy();
		if(this.vendorPolicy==null)
		{
			throw new FaxException("Unable to create vendor policy");
		}
		
		//initialize vendor policy
		this.vendorPolicy.initialize(flowOwner);
		if(this.vendorPolicy instanceof FaxMonitorEventListener)
		{
			this.faxClient.addFaxMonitorEventListener((FaxMonitorEventListener)this.vendorPolicy);
		}

		//log fax client SPI information
		this.bridgeLogger.logDebug(new Object[]{"Initializing fax bridge of type: ",this.getClass().getName(),"\nProvider Information:\n",this.getProvider(),"\nFax Bridge Configuration:\n",configuration,"\nVendor Policy Type: ",this.vendorPolicy.getClass().getName()},null);

		//invoke hook
		this.initializeImpl();
	}

	/**
	 * Returns the internal logger.
	 *  
	 * @return	The internal logger
	 */
	public final Logger getLogger()
	{
		return this.bridgeLogger;
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
	public String getPropertyPart()
	{
		return null;
	}

	/**
	 * Returns the configuration.
	 *  
	 * @return	The configuration
	 */
	public final Map<String,String> getConfiguration()
	{
		return this.bridgeConfiguration.getConfiguration();
	}

	/**
	 * Returns the value from the component configuration based on the provided 
	 * configuration key.
	 * The value will be trimmed.<br>
	 * If the trimmed configuration value is an empty string,
	 * null will be returned instead.
	 *  
	 * @param 	key
	 * 			The configuration key
	 * @return	The value
	 */
	public final String getConfigurationValue(String key)
	{
		//get value
		String value=this.bridgeConfiguration.getConfigurationValue(key);
		
		return value;
	}

	/**
	 * Returns the value from the component configuration based on the provided 
	 * configuration key.
	 * The value will be trimmed.<br>
	 * If the trimmed configuration value is an empty string,
	 * null will be returned instead.
	 *  
	 * @param 	key
	 * 			The configuration key (toString value will be used)
	 * @return	The value
	 */
	public final String getConfigurationValue(Enum<?> key)
	{
		//get value
		String value=this.bridgeConfiguration.getConfigurationValue(key);
		
		return value;
	}

	/**
	 * Returns the internal fax client. 
	 * 
	 * @return	The fax client
	 */
	public final FaxClient getFaxClient()
	{
		return this.faxClient;
	}
	
	/**
	 * This function returns the vendor policy.
	 * 
	 * @return	The vendor policy
	 */
	public final VendorPolicy getVendorPolicy()
	{
		return this.vendorPolicy;
	}

	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	public FaxJob createFaxJob()
	{
		if(!this.initialized)
		{
			throw new FaxException("Fax bridge not initialized.");
		}

		//create fax job object
		FaxJob faxJob=this.faxClient.createFaxJob();

		return faxJob;
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.<br>
	 * The file information in the provided fax job will be ignored by this method, 
	 * instead the file data will be taken from the FileInfo.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information (not including the file information)
	 * @param	fileInfo
	 * 			The file information of the requested fax
	 */
	public void submitFaxJob(FaxJob faxJob,FileInfo fileInfo)
	{
		if(!this.initialized)
		{
			throw new FaxException("Fax bridge not initialized.");
		}

		//validate input
		if(faxJob==null)
		{
			throw new FaxException("File job not provided.");
		}
		if(fileInfo==null)
		{
			throw new FaxException("File info not provided.");
		}

		//update fax job
		this.updateFaxJobWithFileInfo(faxJob,fileInfo);
		
		//send fax
		this.faxClient.submitFaxJob(faxJob);
	}

	/**
	 * This function initializes the fax bridge.
	 */
	protected void initializeImpl()
	{
		//empty (hook for extending classes)
	}

	/**
	 * This function returns the vendor policy.
	 * 
	 * @return	The vendor policy
	 */
	protected abstract VendorPolicy createVendorPolicy();
	
	/**
	 * This function stores the file in the local machine and updates
	 * the fax job with the new file data.
	 * 
	 * @param 	faxJob
	 * 			The fax job object to be updated
	 * @param	fileInfo
	 * 			The file information of the requested fax
	 */
	protected abstract void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo);
}