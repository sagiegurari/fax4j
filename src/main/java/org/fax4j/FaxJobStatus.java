package org.fax4j;

/**
 * This is the fax job status enum which holds the possible fax 
 * status values.<br>
 * Not all SPIs support extraction of the fax job status.<br>
 * In case the SPI is unable to extract or does not support extracting
 * of the fax job status, it will return the UNKNOWN status.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.21b
 */
public enum FaxJobStatus
{
	/**The 'Unknown' status*/
	UNKNOWN,
	/**
	 * The 'Pending' status - in case fax job has not been submitted
	 * yet by the fax server but it is still in the queue.<br>
	 * Suspended fax jobs are also considered as pending.
	 */
	PENDING,
	/**
	 * The 'In Progress' status - in case the fax server is currently
	 * handling the fax job
	 */
	IN_PROGRESS,
	/**
	 * The 'Error' status - in case there was an error during the fax
	 * server attempt to submit the fax job.
	 */
	ERROR
}