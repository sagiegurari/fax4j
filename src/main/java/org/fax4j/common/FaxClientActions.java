package org.fax4j.common;

import org.fax4j.FaxClientActionEventListener;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.FaxMonitorEventListener;

/**
 * Defines all the fax client actions and capabilities.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.33
 */
public interface FaxClientActions extends ProviderImplementation
{
	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	public FaxJob createFaxJob();
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void submitFaxJob(FaxJob faxJob);
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void suspendFaxJob(FaxJob faxJob);
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void resumeFaxJob(FaxJob faxJob);
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	public void cancelFaxJob(FaxJob faxJob);
	
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
	public FaxJobStatus getFaxJobStatus(FaxJob faxJob);
	
	/**
	 * This function adds the fax client action event listener to the internal fax
	 * event listeners data structure.
	 * 
	 * @param 	listener
	 * 			The fax client action event listener
	 */
	public void addFaxClientActionEventListener(FaxClientActionEventListener listener);
	
	/**
	 * This function removes the fax client action event listener from the internal fax
	 * event listeners data structure.
	 * 
	 * @param 	listener
	 * 			The fax client action event listener
	 */
	public void removeFaxClientActionEventListener(FaxClientActionEventListener listener);
	
	/**
	 * This function removes all fax client action event listeners from the internal fax
	 * event listeners data structure.
	 */
	public void removeAllFaxClientActionEventListeners();
	
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
	public void addFaxMonitorEventListener(FaxMonitorEventListener listener);
	
	/**
	 * This function removes the fax monitor event listener from the internal fax
	 * event listeners data structure.<br>
	 * <b>Not all SPIs support monitoring events.</b>
	 * 
	 * @param 	listener
	 * 			The fax monitor event listener
	 */
	public void removeFaxMonitorEventListener(FaxMonitorEventListener listener);
	
	/**
	 * This function removes all fax monitor event listeners from the internal fax
	 * event listeners data structure.<br>
	 * <b>Not all SPIs support monitoring events.</b>
	 */
	public void removeAllFaxMonitorEventListeners();
}