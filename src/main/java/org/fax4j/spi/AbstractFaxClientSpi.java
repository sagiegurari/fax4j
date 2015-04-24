package org.fax4j.spi;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.fax4j.FaxClientActionEvent;
import org.fax4j.FaxClientActionEvent.FaxClientActionEventID;
import org.fax4j.FaxClientActionEventListener;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.FaxJobStatus;
import org.fax4j.FaxMonitorEvent;
import org.fax4j.FaxMonitorEvent.FaxMonitorEventID;
import org.fax4j.FaxMonitorEventListener;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.ConfigurationHolderImpl;
import org.fax4j.common.Logger;

/**
 * This class provides partial/common functionlity of the fax client service provider interface.<br>
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
 * @version 1.15
 * @since	0.1
 */
public abstract class AbstractFaxClientSpi implements FaxClientSpi
{
	/**The initialized flag*/
	private boolean initialized;
	/**The fax client SPI configuration*/
	private ConfigurationHolder spiConfiguration;
	/**The logger*/
	private Logger spiLogger;
	/**The fax job monitor*/
	private FaxJobMonitor spiFaxJobMonitor;
	/**The fax client action event listeners*/
	private Set<FaxClientActionEventListener> faxClientActionEventListeners;
	/**The fax monitor event listeners*/
	private Set<FaxMonitorEventListener> faxMonitorEventListeners;

	/**
	 * This class holds the fax job extended properties.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.38
	 */
	public enum FaxJobExtendedPropertyConstants
	{
		/**The linux property part default value*/
		TEXT_FILE_ENCODING_FAX_JOB_PROPERTY_KEY("text.file.encoding");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxJobExtendedPropertyConstants(String value)
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
	public AbstractFaxClientSpi()
	{
		super();
		
		//set flag
		this.initialized=false;
	}

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
	public final void initialize(Map<String,String> configuration,Logger logger,FaxJobMonitor faxJobMonitor)
	{
		if(this.initialized)
		{
			throw new FaxException("Fax client SPI already initialized.");
		}
		
		//set flag
		this.initialized=true;

		//get property part
		String propertyPart=this.getPropertyPart();

		//get configuration
		this.spiConfiguration=new ConfigurationHolderImpl(configuration,propertyPart);

		//get logger
		this.spiLogger=logger;

		//log fax client SPI information
		this.spiLogger.logDebug(new Object[]{"Initializing fax client SPI of type: ",this.getClass().getName(),"\nProvider Information:\n",this.getProvider(),"\nSPI Configuration:\n",configuration},null);

		//get fax job monitor
		this.spiFaxJobMonitor=faxJobMonitor;
		
		//init listeners data structures
		this.faxClientActionEventListeners=new HashSet<FaxClientActionEventListener>();
		this.faxMonitorEventListeners=new HashSet<FaxMonitorEventListener>();
		
		//initialize
		this.initializeImpl();
	}

	/**
	 * Returns the internal logger.
	 *  
	 * @return	The internal logger
	 */
	public final Logger getLogger()
	{
		return this.spiLogger;
	}

	/**
	 * Returns the fax job monitor.
	 *  
	 * @return	The fax job monitor
	 */
	public final FaxJobMonitor getFaxJobMonitor()
	{
		return this.spiFaxJobMonitor;
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
	public Map<String,String> getConfiguration()
	{
		return this.spiConfiguration.getConfiguration();
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
		String value=this.spiConfiguration.getConfigurationValue(key);
		
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
		String value=this.spiConfiguration.getConfigurationValue(key);
		
		return value;
	}

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
	public final void fireFaxMonitorEvent(FaxMonitorEventID id,FaxJob faxJob,FaxJobStatus faxJobStatus)
	{
		this.fireFaxEvent(id,faxJob,faxJobStatus);
	}

	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	public FaxJob createFaxJob()
	{
		//create new fax job
		FaxJob faxJob=this.createFaxJobImpl();
		
		//fire event
		this.fireFaxEvent(FaxClientActionEventID.CREATE_FAX_JOB,faxJob);
		
		return faxJob;
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
		//validate fax job
		this.invokeFaxJobNullValidation(faxJob);

		//validate fax job target address
		String targetAddress=faxJob.getTargetAddress();
		if((targetAddress==null)||(targetAddress.length()==0))
		{
			throw new FaxException("Fax job target address not provided.");
		}

		//validate fax job file
		File file=faxJob.getFile();
		if(file==null)
		{
			throw new FaxException("Fax job file not provided.");
		}
		else if(!file.exists())
		{
			throw new FaxException("Fax job file "+file.getPath()+" does not exist.");
		}
		else if(!file.isFile())
		{
			throw new FaxException("Fax job file "+file.getPath()+" is not a file.");
		}
		else if(file.length()==0)
		{
			throw new FaxException("Fax job file "+file.getPath()+" is empty.");
		}

		//invoke action
		this.submitFaxJobImpl(faxJob);
		
		//fire event
		this.fireFaxEvent(FaxClientActionEventID.SUBMIT_FAX_JOB,faxJob);

		//start monitoring fax job if and only if monitoring is supported and there are existing listeners
		if(this.isFaxMonitorEventsSupported())
		{
			synchronized(this.faxMonitorEventListeners)
			{
				if(!this.faxMonitorEventListeners.isEmpty())
				{
					//monitor the submitted fax job
					this.spiFaxJobMonitor.monitorFaxJob(this,faxJob);
				}
			}
		}
	}
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void suspendFaxJob(FaxJob faxJob)
	{
		//validate fax job ID
		this.invokeFaxJobIDValidation(faxJob);
		
		//invoke action
		this.suspendFaxJobImpl(faxJob);
		
		//fire event
		this.fireFaxEvent(FaxClientActionEventID.SUSPEND_FAX_JOB,faxJob);
	}
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void resumeFaxJob(FaxJob faxJob)
	{
		//validate fax job ID
		this.invokeFaxJobIDValidation(faxJob);
		
		//invoke action
		this.resumeFaxJobImpl(faxJob);
		
		//fire event
		this.fireFaxEvent(FaxClientActionEventID.RESUME_FAX_JOB,faxJob);
	}
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void cancelFaxJob(FaxJob faxJob)
	{
		//validate fax job ID
		this.invokeFaxJobIDValidation(faxJob);
		
		//invoke action
		this.cancelFaxJobImpl(faxJob);
		
		//fire event
		this.fireFaxEvent(FaxClientActionEventID.CANCEL_FAX_JOB,faxJob);
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
		//validate fax job ID
		this.invokeFaxJobIDValidation(faxJob);
		
		//invoke action
		FaxJobStatus faxJobStatus=this.getFaxJobStatusImpl(faxJob);
		
		return faxJobStatus;
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
		synchronized(this.faxClientActionEventListeners)
		{
			this.faxClientActionEventListeners.add(listener);
		}
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
		synchronized(this.faxClientActionEventListeners)
		{
			this.faxClientActionEventListeners.remove(listener);
		}
	}
	
	/**
	 * This function removes all fax client action event listeners from the internal fax
	 * event listeners data structure.
	 */
	public void removeAllFaxClientActionEventListeners()
	{
		synchronized(this.faxClientActionEventListeners)
		{
			this.faxClientActionEventListeners.clear();
		}
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
		boolean faxMonitorEventsSupported=this.isFaxMonitorEventsSupported();
		if(faxMonitorEventsSupported)
		{
			synchronized(this.faxMonitorEventListeners)
			{
				this.faxMonitorEventListeners.add(listener);
			}
		}
		else
		{
			this.throwUnsupportedException();
		}
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
		synchronized(this.faxMonitorEventListeners)
		{
			this.faxMonitorEventListeners.remove(listener);
			
			//stop fax job monitoring for this SPI
			this.checkAndStopMonitoringAllFaxJobs();
		}
	}
	
	/**
	 * This function removes all fax monitor event listeners from the internal fax
	 * event listeners data structure.<br>
	 * <b>Not all SPIs support monitoring events.</b>
	 */
	public void removeAllFaxMonitorEventListeners()
	{
		synchronized(this.faxMonitorEventListeners)
		{
			//clear data structure
			this.faxMonitorEventListeners.clear();

			//stop fax job monitoring for this SPI
			this.checkAndStopMonitoringAllFaxJobs();
		}
	}
	
	/**
	 * Checks and if needed stops monitoring fax jobs for this SPI.
	 */
	private synchronized void checkAndStopMonitoringAllFaxJobs()
	{
		//stop fax job monitoring for this SPI
		if(this.isFaxMonitorEventsSupported())
		{
			if(this.faxMonitorEventListeners.size()==0)
			{
				this.spiFaxJobMonitor.stopMonitoringAllFaxJobs(this);
			}
		}
	}

	/**
	 * This function fires a new fax event.
	 * 
	 * @param 	id
	 * 			The fax event ID
	 * @param 	faxJob
	 * 			The fax job
	 */
	protected void fireFaxEvent(FaxClientActionEventID id,FaxJob faxJob)
	{
		//create new fax event
		FaxClientActionEvent event=new FaxClientActionEvent(id,faxJob);

		//get listeners
		FaxClientActionEventListener[] listeners=null;
		synchronized(this.faxClientActionEventListeners)
		{
			listeners=this.faxClientActionEventListeners.toArray(new FaxClientActionEventListener[this.faxClientActionEventListeners.size()]);
		}
		
		int amount=listeners.length;
		FaxClientActionEventListener listener=null;
		for(int index=0;index<amount;index++)
		{
			//get next element
			listener=listeners[index];
			
			//fire event
			if(listener!=null)
			{
				switch(id)
				{
					case CREATE_FAX_JOB:
						listener.faxJobCreated(event);
						break;
					case SUBMIT_FAX_JOB:
						listener.faxJobSubmitted(event);
						break;
					case SUSPEND_FAX_JOB:
						listener.faxJobSuspended(event);
						break;
					case RESUME_FAX_JOB:
						listener.faxJobResumed(event);
						break;
					case CANCEL_FAX_JOB:
						listener.faxJobCancelled(event);
						break;
					default:
						throw new FaxException("Unable to support fax event, for event ID: "+id);
				}
			}
		}
	}

	/**
	 * This function fires a new fax event.
	 * 
	 * @param 	id
	 * 			The fax event ID
	 * @param 	faxJob
	 * 			The fax job
	 * @param	faxJobStatus
	 * 			The fax job status
	 */
	protected void fireFaxEvent(FaxMonitorEventID id,FaxJob faxJob,FaxJobStatus faxJobStatus)
	{
		//create new fax event
		FaxMonitorEvent event=new FaxMonitorEvent(id,faxJob,faxJobStatus);

		//get listeners
		FaxMonitorEventListener[] listeners=null;
		synchronized(this.faxClientActionEventListeners)
		{
			listeners=this.faxMonitorEventListeners.toArray(new FaxMonitorEventListener[this.faxMonitorEventListeners.size()]);
		}
		
		int amount=listeners.length;
		FaxMonitorEventListener listener=null;
		for(int index=0;index<amount;index++)
		{
			//get next element
			listener=listeners[index];
			
			//fire event
			if(listener!=null)
			{
				switch(id)
				{
					case FAX_JOB_STATUS_CHANGE:
						listener.faxJobStatusChanged(event);
						break;
					default:
						throw new FaxException("Unable to support fax event, for event ID: "+id);
				}
			}
		}
	}
	
	/**
	 * This function invokes the fax job null validation. 
	 * 
	 * @param	faxJob
	 * 			The fax job
	 */
	protected void invokeFaxJobNullValidation(FaxJob faxJob)
	{
		if(faxJob==null)
		{
			throw new FaxException("Fax job not provided.");
		}
	}
	
	/**
	 * This function invokes the fax job null validation. 
	 * 
	 * @param	faxJob
	 * 			The fax job
	 */
	protected void invokeFaxJobIDValidation(FaxJob faxJob)
	{
		//validate fax job is not null
		this.invokeFaxJobNullValidation(faxJob);

		//validate fax job ID
		String faxJobID=faxJob.getID();
		if((faxJobID==null)||(faxJobID.length()==0))
		{
			throw new FaxException("Fax job ID not provided.");
		}
	}
	
	/**
	 * This function throws unsupported operation exception.
	 */
	protected void throwUnsupportedException()
	{
		throw new FaxException("Operation not supported.");
	}

	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	protected FaxJob createFaxJobImpl()
	{
		//create new fax job
		FaxJob faxJob=new FaxJobImpl();
		
		//set defaults
		faxJob.setPriority(FaxJobPriority.MEDIUM_PRIORITY);
		
		return faxJob;
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	protected abstract void initializeImpl();

	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	protected abstract void submitFaxJobImpl(FaxJob faxJob);
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	protected abstract void suspendFaxJobImpl(FaxJob faxJob);
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	protected abstract void resumeFaxJobImpl(FaxJob faxJob);
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	protected abstract void cancelFaxJobImpl(FaxJob faxJob);

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
	protected abstract FaxJobStatus getFaxJobStatusImpl(FaxJob faxJob);
}