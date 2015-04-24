package org.fax4j.spi;

import java.util.Map;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;

/**
 * This interface defines the fax job monitor capabilities.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.40.2
 */
public interface FaxJobMonitor
{
	/**
	 * This function initializes the fax job monitor.<br>
	 * This method is called by the FaxClientSpiFactory.
	 * 
	 * @param	configuration
	 * 			The fax job monitor configuration
	 * @param	logger
	 * 			The internal logger
	 */
	public void initialize(Map<String,String> configuration,Logger logger);

	/**
	 * This function starts monitoring the requested fax job.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 * @param 	faxJob
	 * 			The fax job to monitor
	 */
	public void monitorFaxJob(FaxClientSpi faxClientSpi,FaxJob faxJob);

	/**
	 * This function will stop the monitoring of all fax jobs registered
	 * by the provided fax client SPI.<br>
	 * Other fax jobs registered by other SPIs will keep on being monitored.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 */
	public void stopMonitoringAllFaxJobs(FaxClientSpi faxClientSpi);
}