package org.fax4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;

/**
 * This class provides partial implementation of the fax job monitor capabilities.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.40.2
 */
public abstract class AbstractFaxJobMonitor implements FaxJobMonitor
{
	/**The initialized flag*/
	private boolean initialized;
	/**The fax job monitor configuration*/
	private Map<String,String> monitorConfiguration;
	/**The logger*/
	private Logger monitorLogger;

	/**
	 * This is the class constructor.
	 */
	public AbstractFaxJobMonitor()
	{
		super();
	}
	
	/**
	 * This function initializes the fax job monitor.<br>
	 * This method is called by the FaxClientSpiFactory.
	 * 
	 * @param	configuration
	 * 			The fax job monitor configuration
	 * @param	logger
	 * 			The internal logger
	 */
	public void initialize(Map<String,String> configuration,Logger logger)
	{
		if(this.initialized)
		{
			throw new FaxException("Fax job monitor already initialized.");
		}
		
		//set flag
		this.initialized=true;
		
		//get configuration
		this.monitorConfiguration=new HashMap<String,String>(configuration);
		this.monitorConfiguration=Collections.unmodifiableMap(this.monitorConfiguration);
		
		//get logger
		this.monitorLogger=logger;

		//log fax job monitor information
		this.monitorLogger.logDebug(new Object[]{"Initializing fax job monitor of type: ",this.getClass().getName(),"\nMonitor Configuration:\n",configuration},null);

		//initialize
		this.initializeImpl();
	}
	
	/**
	 * Returns the internal logger.
	 *  
	 * @return	The internal logger
	 */
	protected final Logger getLogger()
	{
		return this.monitorLogger;
	}
	
	/**
	 * Returns the value from the monitor configuration based on the provided 
	 * configuration key.
	 *  
	 * @param 	key
	 * 			The configuration key
	 * @return	The value
	 */
	protected final String getConfigurationValue(String key)
	{
		//get value
		String value=this.monitorConfiguration.get(key);
		
		//trim value
		if(value!=null)
		{
			value=value.trim();
			
			//set as null if empty string
			if(value.length()==0)
			{
				value=null;
			}
		}
		
		this.monitorLogger.logDebug(new Object[]{"Extracted configuration for key: ",key," value: ",value},null);
		
		return value;
	}

	/**
	 * This function starts monitoring the requested fax job.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 * @param 	faxJob
	 * 			The fax job to monitor
	 */
	public void monitorFaxJob(FaxClientSpi faxClientSpi,FaxJob faxJob)
	{
		if(faxClientSpi==null)
		{
			throw new FaxException("Fax client SPI not provided.");
		}
		if(faxJob==null)
		{
			throw new FaxException("Fax job not provided.");
		}
		String id=faxJob.getID();
		if((id==null)||(id.length()==0))
		{
			throw new FaxException("Missing fax job ID in provided fax job.");
		}
		
		//monitor fax job
		this.monitorFaxJobImpl(faxClientSpi,faxJob);
		
	}

	/**
	 * This function will stop the monitoring of all fax jobs registered
	 * by the provided fax client SPI.<br>
	 * Other fax jobs registered by other SPIs will keep on being monitored.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 */
	public void stopMonitoringAllFaxJobs(FaxClientSpi faxClientSpi)
	{
		if(faxClientSpi==null)
		{
			throw new FaxException("Fax client SPI not provided.");
		}
		
		//stop monitoring fax jobs for provided SPI
		this.stopMonitoringAllFaxJobsImpl(faxClientSpi);
		
	}

	/**
	 * This function initializes the fax job monitor.
	 */
	protected abstract void initializeImpl();

	/**
	 * This function starts monitoring the requested fax job.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 * @param 	faxJob
	 * 			The fax job to monitor
	 */
	public abstract void monitorFaxJobImpl(FaxClientSpi faxClientSpi,FaxJob faxJob);

	/**
	 * This function will stop the monitoring of all fax jobs registered
	 * by the provided fax client SPI.<br>
	 * Other fax jobs registered by other SPIs will keep on being monitored.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 */
	public abstract void stopMonitoringAllFaxJobsImpl(FaxClientSpi faxClientSpi);
}