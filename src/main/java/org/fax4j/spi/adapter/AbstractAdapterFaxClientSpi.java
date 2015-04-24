package org.fax4j.spi.adapter;

import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.Logger;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.spi.FaxClientSpi;

/**
 * This class provides partial implementation of an adapter fax client service provider interface.<br>
 * This implementation will proxy the requests to another internal SPI.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.41.5
 */
public abstract class AbstractAdapterFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The fax client SPI*/
	protected FaxClientSpi faxClientSpi;
	
	/**
	 * This is the class constructor.
	 */
	public AbstractAdapterFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//setup the child SPI
		this.initializeChildFaxClientSpi();
		if(this.faxClientSpi==null)
		{
			throw new FaxException("Child fax client SPI not available.");
		}
		
		//get logger
		Logger logger=this.getLogger();
		
		logger.logDebug(new Object[]{"Created child fax client SPI: ",this.faxClientSpi.getClass().getName()},null);
	}
	
	/**
	 * This function returns the internal fax client SPI.
	 * 
	 * @return	The internal fax client SPI
	 */
	protected FaxClientSpi getInternalFaxClientSpi()
	{
		return this.faxClientSpi;
	}
	
	/**
	 * This function returns true if the fax monitor events are supported by this SPI.
	 * 
	 * @return	True if the fax monitor events are supported by this SPI
	 */
	@Override
	public boolean isFaxMonitorEventsSupported()
	{
		return this.faxClientSpi.isFaxMonitorEventsSupported();
	}

	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	@Override
	protected FaxJob createFaxJobImpl()
	{
		return this.faxClientSpi.createFaxJob();
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
		this.faxClientSpi.submitFaxJob(faxJob);
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
		this.faxClientSpi.suspendFaxJob(faxJob);
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
		this.faxClientSpi.resumeFaxJob(faxJob);
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
		this.faxClientSpi.cancelFaxJob(faxJob);
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
		return this.faxClientSpi.getFaxJobStatus(faxJob);
	}

	/**
	 * This function polls the new statues for the provided fax jobs.
	 * 
	 * @param 	faxJobs
	 * 			The fax jobs to poll
	 * @return	The fax job statues
	 */
	@Override
	protected FaxJobStatus[] pollForFaxJobStatuesImpl(FaxJob[] faxJobs)
	{
		return this.faxClientSpi.pollForFaxJobStatues(faxJobs);
	}
	
	/**
	 * This function initializes the child fax client SPI.
	 */
	protected abstract void initializeChildFaxClientSpi();
}