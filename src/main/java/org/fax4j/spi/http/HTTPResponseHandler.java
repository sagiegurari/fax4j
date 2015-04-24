package org.fax4j.spi.http;

import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.common.Service;

/**
 * This interface defines the HTTP response handler which is
 * used to update the fax job based on the HTTP response data.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.4
 */
public interface HTTPResponseHandler extends Service
{
	/**
	 * Updates the fax job based on the data from the HTTP response data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @param	httpResponse
	 * 			The HTTP response
	 * @param	faxActionType
	 * 			The fax action type
	 */
	public void updateFaxJob(FaxJob faxJob,HTTPResponse httpResponse,FaxActionType faxActionType);
	
	/**
	 * This function extracts the fax job status from the HTTP response data.
	 * 
	 * @param	httpResponse
	 * 			The HTTP response
	 * @return	The fax job status
	 */
	public FaxJobStatus getFaxJobStatus(HTTPResponse httpResponse);
}