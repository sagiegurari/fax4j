package org.fax4j.spi;

import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.Provider;
import org.fax4j.common.Fax4JProvider;

/**
 * This class provides partial/common functionlity of the internal fax4j fax client service provider interface.<br>
 * This class should be used only by <b>internal</b> fax4j SPIs, for SPIs written by vendors outside the
 * fax4j, see the org.fax4j.spi.AbstractFaxClientSpi class.<br>
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
 * @version 1.03
 * @since	0.17
 */
public abstract class AbstractFax4JClientSpi extends AbstractFaxClientSpi
{
	/**
	 * This is the default constructor.
	 */
	public AbstractFax4JClientSpi()
	{
		super();
	}

	/**
	 * This function returns the provider.
	 * 
	 * @return	The provider
	 */
	public final Provider getProvider()
	{
		return Fax4JProvider.FAX4J_PROVIDER;
	}
	
	/**
	 * This function returns true if the fax monitor events are supported by this SPI.
	 * 
	 * @return	True if the fax monitor events are supported by this SPI
	 */
	public boolean isFaxMonitorEventsSupported()
	{
		return false;
	}

	/**
	 * This function polls the new statues for the provided fax jobs.
	 * 
	 * @param 	faxJobs
	 * 			The fax jobs to poll
	 * @return	The fax job statues
	 */
	public final FaxJobStatus[] pollForFaxJobStatues(FaxJob[] faxJobs)
	{
		FaxJobStatus[] faxJobStatuses=null;
		if((faxJobs!=null)&&(faxJobs.length>0)&&(this.isFaxMonitorEventsSupported()))
		{
			faxJobStatuses=this.pollForFaxJobStatuesImpl(faxJobs);
		}

		return faxJobStatuses;
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
		this.throwUnsupportedException();
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
		this.throwUnsupportedException();
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
		this.throwUnsupportedException();
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
		this.throwUnsupportedException();
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
		return FaxJobStatus.UNKNOWN;
	}

	/**
	 * This function polls the new statues for the provided fax jobs.
	 * 
	 * @param 	faxJobs
	 * 			The fax jobs to poll
	 * @return	The fax job statues
	 */
	protected FaxJobStatus[] pollForFaxJobStatuesImpl(FaxJob[] faxJobs)
	{
		//get amount
		int amount=faxJobs.length;
		
		//init array
		FaxJobStatus[] faxJobStatuses=new FaxJobStatus[amount];

		FaxJob faxJob=null;
		FaxJobStatus faxJobStatus=null;
		for(int index=0;index<amount;index++)
		{
			//get next fax job
			faxJob=faxJobs[index];
			
			//set default
			faxJobStatus=FaxJobStatus.UNKNOWN;

			if(faxJob!=null)
			{
				try
				{
					faxJobStatus=this.getFaxJobStatus(faxJob);
				}
				catch(RuntimeException exception)
				{
					//ignore errors as this method should not throw any exception
				}
			}

			//set default
			if(faxJobStatus==null)
			{
				faxJobStatus=FaxJobStatus.UNKNOWN;
			}
			
			//put in output array
			faxJobStatuses[index]=faxJobStatus;
		}
		
		return faxJobStatuses;
	}
}