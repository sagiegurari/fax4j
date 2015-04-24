package org.fax4j.spi;

import java.util.Map;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.FaxMonitorEvent.FaxMonitorEventID;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.FaxClientActions;
import org.fax4j.common.Logger;

/**
 * This interface defines the fax client service provider interface.<br>
 * This is the actual engine behind the fax4j framework.<br>
 * It handles the fax client activities such as sending/suspending fax jobs and so on.<br>
 * The SPI is an internal class that should be used internally only.<br>
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
 * For SPI specific configuration, see the relevant SPI class javadoc.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.11
 * @since	0.1
 */
public interface FaxClientSpi extends FaxClientActions,ConfigurationHolder
{
	/**
	 * This function initializes the fax client SPI.<br>
	 * This method is called by the FaxClientSpiFactory.
	 * 
	 * @param	configuration
	 * 			The fax client configuration
	 * @param	logger
	 * 			The internal logger
	 * @param	faxJobMonitor
	 * 			The fax job monitor
	 */
	public void initialize(Map<String,String> configuration,Logger logger,FaxJobMonitor faxJobMonitor);
	
	/**
	 * Returns the internal logger.
	 *  
	 * @return	The internal logger
	 */
	public Logger getLogger();

	/**
	 * Returns the fax job monitor.
	 *  
	 * @return	The fax job monitor
	 */
	public FaxJobMonitor getFaxJobMonitor();

	/**
	 * This function returns true if the fax monitor events are supported by this SPI.
	 * 
	 * @return	True if the fax monitor events are supported by this SPI
	 */
	public boolean isFaxMonitorEventsSupported();
	
	/**
	 * This function polls the new statues for the provided fax jobs.
	 * 
	 * @param 	faxJobs
	 * 			The fax jobs to poll
	 * @return	The fax job statues
	 */
	public FaxJobStatus[] pollForFaxJobStatues(FaxJob[] faxJobs);

	/**
	 * This function fires a new fax monitor event.
	 * 
	 * @param 	id
	 * 			The fax monitor event ID
	 * @param 	faxJob
	 * 			The fax job
	 * @param	faxJobStatus
	 * 			The fax job status
	 */
	public void fireFaxMonitorEvent(FaxMonitorEventID id,FaxJob faxJob,FaxJobStatus faxJobStatus);
}