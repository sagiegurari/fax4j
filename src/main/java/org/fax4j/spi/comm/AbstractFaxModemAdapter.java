package org.fax4j.spi.comm;

import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;
import org.fax4j.spi.FaxClientSpi;

/**
 * This class provides partial/common functionlity of the fax modem adapter interface.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.38
 */
public abstract class AbstractFaxModemAdapter implements FaxModemAdapter
{
	/**The initialized flag*/
	private boolean initialized;

	/**
	 * This is the class constructor.
	 */
	public AbstractFaxModemAdapter()
	{
		super();
		
		//set flag
		this.initialized=false;
	}

	/**
	 * This function initializes the fax modem adapter.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 */
	public void initialize(FaxClientSpi faxClientSpi)
	{
		if(this.initialized)
		{
			throw new FaxException("Fax Modem Adapter already initialized.");
		}
		
		//set flag
		this.initialized=true;

		//get logger
		Logger logger=faxClientSpi.getLogger();

		//log fax client SPI information
		logger.logDebug(new Object[]{"Initializing fax modem adapter of type: ",this.getClass().getName(),"\nProvider Information:\n",this.getProvider()},null);

		//initialize
		this.initializeImpl(faxClientSpi);
	}

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
	public void submitFaxJob(FaxJob faxJob,CommPortAdapter adapter)
	{
		if(faxJob==null)
		{
			throw new FaxException("Fax job not provided.");
		}

		if(adapter==null)
		{
			throw new FaxException("COMM port adapter not provided.");
		}

		//submit fax job
		this.submitFaxJobImpl(faxJob,adapter);
	}

	/**
	 * This function initializes the fax modem adapter.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 */
	protected abstract void initializeImpl(FaxClientSpi faxClientSpi);

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
	protected abstract void submitFaxJobImpl(FaxJob faxJob,CommPortAdapter adapter);
}