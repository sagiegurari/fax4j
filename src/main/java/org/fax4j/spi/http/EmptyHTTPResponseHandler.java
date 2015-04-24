package org.fax4j.spi.http;

import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.common.FaxActionType;

/**
 * This class provides empty implementation of the HTTP response handler interface.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.4
 */
public class EmptyHTTPResponseHandler extends AbstractHTTPResponseHandler
{
	/**
	 * This is the default constructor.
	 */
	public EmptyHTTPResponseHandler()
	{
		super(Fax4JProvider.FAX4J_PROVIDER);
	}

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
	public void updateFaxJob(FaxJob faxJob,HTTPResponse httpResponse,FaxActionType faxActionType)
	{
		//empty implementation
	}
	
	/**
	 * This function extracts the fax job status from the HTTP response data.
	 * 
	 * @param	httpResponse
	 * 			The HTTP response
	 * @return	The fax job status
	 */
	public FaxJobStatus getFaxJobStatus(HTTPResponse httpResponse)
	{
		return FaxJobStatus.UNKNOWN;
	}
}