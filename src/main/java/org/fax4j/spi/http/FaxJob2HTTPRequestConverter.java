package org.fax4j.spi.http;

import org.fax4j.FaxJob;
import org.fax4j.common.FaxActionType;
import org.fax4j.common.Service;

/**
 * The fax job to HTTP request converter interface enables to convert fax jobs
 * to HTTP requests which will be submitted to invoke the fax action remotly.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.4
 */
public interface FaxJob2HTTPRequestConverter extends Service
{
	/**
	 * Creates the HTTP request from the fax job data.
	 * 
	 * @param	faxClientSpi
	 * 			The HTTP fax client SPI
	 * @param	faxActionType
	 * 			The fax action type
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The HTTP request to send
	 */
	public HTTPRequest createHTTPRequest(HTTPFaxClientSpi faxClientSpi,FaxActionType faxActionType,FaxJob faxJob);
}