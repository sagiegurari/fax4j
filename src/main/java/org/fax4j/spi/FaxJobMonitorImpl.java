package org.fax4j.spi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.FaxMonitorEvent.FaxMonitorEventID;

/**
 * This class provides the default implementation of the fax job monitor.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.40.2
 */
public class FaxJobMonitorImpl extends AbstractFaxJobMonitor
{
	/**The poller task interval between invocations*/
	protected long pollingInterval;
	/**The fixed polling interval settings*/
	protected boolean fixedPollingInterval;
	/**The poller thread priority*/
	protected int pollingThreadPriority;
	/**The poller task*/
	protected PollerTask pollerTask;
	/**The monitored fax jobs*/
	private Map<FaxClientSpi,Map<FaxJob,FaxJobStatus>> data;
	/**The lock object*/
	protected final Object LOCK;
	/**The fax job polling interval in millies property key*/
	public static final String POLLING_INTERVAL_IN_MILLIES_PROPERTY_KEY="org.fax4j.monitor.polling.interval";
	/**The fax job polling interval fixed property key*/
	public static final String FIXED_POLLING_INTERVAL_PROPERTY_KEY="org.fax4j.monitor.polling.interval.fixed";
	/**The fax job polling thread priority property key*/
	public static final String POLLING_THREAD_PRIORITY_PROPERTY_KEY="org.fax4j.monitor.polling.thread.priority";

	/**
	 * This is the class constructor.
	 */
	public FaxJobMonitorImpl()
	{
		super();
		
		//init lock
		this.LOCK=new Object();
	}

	/**
	 * This function initializes the fax job monitor.
	 */
	@Override
	protected void initializeImpl()
	{
		//init data structures
		this.data=new HashMap<FaxClientSpi,Map<FaxJob,FaxJobStatus>>(20);

		//get polling interval
		String value=this.getConfigurationValue(FaxJobMonitorImpl.POLLING_INTERVAL_IN_MILLIES_PROPERTY_KEY);
		this.pollingInterval=5000;
		if(value!=null)
		{
			this.pollingInterval=Long.parseLong(value);
			if(this.pollingInterval<=0)
			{
				throw new FaxException("Polling interval set to an invalid value: "+this.pollingInterval);
			}
		}
		
		//get fixed polling interval flag
		value=this.getConfigurationValue(FaxJobMonitorImpl.FIXED_POLLING_INTERVAL_PROPERTY_KEY);
		this.fixedPollingInterval=Boolean.parseBoolean(value);
		
		//get poller thread priority
		value=this.getConfigurationValue(FaxJobMonitorImpl.POLLING_THREAD_PRIORITY_PROPERTY_KEY);
		this.pollingThreadPriority=2;
		if(value!=null)
		{
			this.pollingThreadPriority=Integer.parseInt(value);
			if((this.pollingThreadPriority<Thread.MIN_PRIORITY)||(this.pollingThreadPriority>Thread.MAX_PRIORITY))
			{
				throw new FaxException("Polling thread priority set to an invalid value: "+this.pollingThreadPriority);
			}
		}
		
		//create poller task
		this.pollerTask=new PollerTask(this);
	}

	/**
	 * This function starts monitoring the requested fax job.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 * @param 	faxJob
	 * 			The fax job to monitor
	 */
	@Override
	public void monitorFaxJobImpl(FaxClientSpi faxClientSpi,FaxJob faxJob)
	{
		//get fax job status
		FaxJobStatus faxJobStatus=faxClientSpi.getFaxJobStatus(faxJob);
		if(faxJobStatus==null)
		{
			throw new FaxException("Unable to extract fax job status for fax job: "+faxJob.getID());
		}

		synchronized(this.LOCK)
		{
			Map<FaxJob,FaxJobStatus> faxJobMap=this.data.get(faxClientSpi);
			if(faxJobMap==null)
			{
				faxJobMap=new HashMap<FaxJob,FaxJobStatus>(500);
				this.data.put(faxClientSpi,faxJobMap);
			}
			
			//add new fax job
			faxJobMap.put(faxJob,faxJobStatus);
			
			if(!this.pollerTask.isRunning())
			{
				this.pollerTask.startTask();
			}
		}
	}

	/**
	 * This function will stop the monitoring of all fax jobs registered
	 * by the provided fax client SPI.<br>
	 * Other fax jobs registered by other SPIs will keep on being monitored.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 */
	@Override
	public void stopMonitoringAllFaxJobsImpl(FaxClientSpi faxClientSpi)
	{
		synchronized(this.LOCK)
		{
			//remove from monitored data
			this.data.remove(faxClientSpi);
		}
		
		//check if need to stop the monitor
		this.checkAndStopMonitor();
	}
	
	/**
	 * Runs the polling cycle to fetch and create monitor events.
	 */
	protected void runPollingCycle()
	{
		synchronized(this.LOCK)
		{
			Set<Entry<FaxClientSpi,Map<FaxJob,FaxJobStatus>>> entrySet=this.data.entrySet();
			Iterator<Entry<FaxClientSpi,Map<FaxJob,FaxJobStatus>>> faxClientSpiIterator=entrySet.iterator();
			Entry<FaxClientSpi,Map<FaxJob,FaxJobStatus>> faxClientSpiEntry=null;
			FaxClientSpi faxClientSpi=null;
			Map<FaxJob,FaxJobStatus> faxJobMap=null;
			Iterator<Entry<FaxJob,FaxJobStatus>> faxJobIterator=null;
			Entry<FaxJob,FaxJobStatus> faxJobEntry=null;
			FaxJob[] faxJobs=null;
			FaxJobStatus[] previousFaxJobStatuses=null;
			FaxJobStatus[] currentFaxJobStatuses=null;
			FaxJob faxJob=null;
			FaxJobStatus previousFaxJobStatus=null;
			FaxJobStatus currentFaxJobStatus=null;
			int counter=0;
			while(faxClientSpiIterator.hasNext())
			{
				//get next entry
				faxClientSpiEntry=faxClientSpiIterator.next();
				
				//get values
				faxClientSpi=faxClientSpiEntry.getKey();
				faxJobMap=faxClientSpiEntry.getValue();
				
				//get iterator
				faxJobIterator=faxJobMap.entrySet().iterator();
				
				int amount=faxJobMap.size();
				if(amount>0)
				{
					//init arrays
					faxJobs=new FaxJob[amount];
					previousFaxJobStatuses=new FaxJobStatus[amount];
					
					counter=0;
					while(faxJobIterator.hasNext())
					{
						//get next entry
						faxJobEntry=faxJobIterator.next();
					
						//get values
						faxJob=faxJobEntry.getKey();
						previousFaxJobStatus=faxJobEntry.getValue();
						
						//put in array
						faxJobs[counter]=faxJob;
						previousFaxJobStatuses[counter]=previousFaxJobStatus;
						counter++;
					}
					
					//poll for changes
					currentFaxJobStatuses=faxClientSpi.pollForFaxJobStatues(faxJobs);
					
					if((currentFaxJobStatuses!=null)&&(currentFaxJobStatuses.length==amount))
					{
						for(int index=0;index<amount;index++)
						{
							//get current fax job status
							currentFaxJobStatus=currentFaxJobStatuses[index];

							if(currentFaxJobStatus!=null)
							{
								//get values
								faxJob=faxJobs[index];
								previousFaxJobStatus=previousFaxJobStatuses[index];

								//if status was changed
								if(!previousFaxJobStatus.equals(currentFaxJobStatus))
								{
									//update data structure
									faxJobMap.put(faxJob,currentFaxJobStatus);

									//fire event
									faxClientSpi.fireFaxMonitorEvent(FaxMonitorEventID.FAX_JOB_STATUS_CHANGE,faxJob,currentFaxJobStatus);
								}

								//if status is unknown or error, stop monitoring fax job
								switch(currentFaxJobStatus)
								{
									case UNKNOWN:
									case ERROR:
										faxJobMap.remove(faxJob);
										break;
									case IN_PROGRESS:
									case PENDING:
									default:
										//do nothing
										break;
								}
							}
						}
					}
				}
				
				if(faxJobMap.isEmpty())
				{
					faxClientSpiIterator.remove();
				}
			}
		}
		
		//check if need to stop the monitor
		this.checkAndStopMonitor();
	}
	
	/**
	 * Checks and if needed stops the monitor in case there is no
	 * data to monitor.
	 */
	private void checkAndStopMonitor()
	{
		synchronized(this.LOCK)
		{
			//if no data, cancel poller task
			if(this.data.isEmpty())
			{
				this.pollerTask.setRunning(false);
			}
		}
	}

	/**
	 * This class used the start the polling cycle.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.40.2
	 */
	public static class PollerTask implements Runnable
	{
		/**The running flag*/
		private boolean running;
		/**The fax job monitor*/
		private final FaxJobMonitorImpl FAX_JOB_MONITOR;
		
		/**
		 * This is the class constructor.
		 * 
		 * @param	faxJobMonitor
		 * 			The fax job monitor
		 */
		public PollerTask(FaxJobMonitorImpl faxJobMonitor)
		{
			super();
			
			this.FAX_JOB_MONITOR=faxJobMonitor;
			this.setRunning(false);
		}

		/**
		 * This function sets the running flag.
		 * 
		 * @param 	running
		 * 			The running flag
		 */
		public final void setRunning(boolean running)
		{
			synchronized(this)
			{
				this.running=running;
				this.notifyAll();
			}
		}

		/**
		 * This function returns the running flag.
		 *
		 * @return	The running flag
		 */
		public final boolean isRunning()
		{
			return this.running;
		}
		
		/**
		 * Starts the task.
		 */
		public final void startTask()
		{
			if(!this.isRunning())
			{
				synchronized(this)
				{
					//set flag
					this.setRunning(true);
					
					//start thread
					Thread thread=new Thread(this,"Fax Monitor");
					thread.setPriority(this.FAX_JOB_MONITOR.pollingThreadPriority);
					thread.setDaemon(true);
					thread.start();
				}
			}
		}

		/**
		 * Invoked by the timer to start the polling cycle.
		 */
		public final void run()
		{
			//run loop
			long startTime=-1;
			long waitTime=0;
			long cycleTime=this.FAX_JOB_MONITOR.pollingInterval;
			while(this.isRunning())
			{
				//get start time
				startTime=System.currentTimeMillis();
				
				//run internal runnable
				this.FAX_JOB_MONITOR.runPollingCycle();

				//get wait time
				waitTime=cycleTime;
				if(!this.FAX_JOB_MONITOR.fixedPollingInterval)
				{
					waitTime=waitTime-(System.currentTimeMillis()-startTime);
				}
				
				if(waitTime>0)
				{
					synchronized(this)
					{
						try
						{
							//wait for next cycle
							this.wait(waitTime);
						}
						catch(Throwable throwable)
						{
							//ignore
						}
					}
				}
			}
		}
	}
}