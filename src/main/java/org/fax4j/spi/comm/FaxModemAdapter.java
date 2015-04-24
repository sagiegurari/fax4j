package org.fax4j.spi.comm;

import org.fax4j.FaxJob;
import org.fax4j.common.ProviderImplementation;
import org.fax4j.spi.FaxClientSpi;

/**
 * Defines a local fax modem adapter.<br>
 * This adapter can only work with fax modems that are directly connected to
 * the machine via COMM port.<br>
 * The fax modem is not thread safe and it is up to the caller class to synchronize
 * any non thread safe resource, such as the COMM adapter.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.34
 */
public interface FaxModemAdapter extends ProviderImplementation
{
	/**
	 * This function initializes the fax modem adapter.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 */
	public void initialize(FaxClientSpi faxClientSpi);

	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param	adapter
	 * 			The COMM port adapter
	 */
	public void submitFaxJob(FaxJob faxJob,CommPortAdapter adapter);
}