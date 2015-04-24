package org.fax4j.spi.http;

import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.ProviderImplementation;

/**
 * This interface defines the HTTP client used to submit HTTP requests.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.1
 */
public interface HTTPClient extends ProviderImplementation
{
	/**
	 * This enum holds the supported HTTP methods.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.42.5
	 */
	public static enum HTTPMethod
	{
		/**HTTP method*/
		POST,
		/**HTTP method*/
		GET,
		/**HTTP method*/
		PUT;
	}

	/**
	 * This function creates the HTTP client configuration to be
	 * used later on by this HTTP client type.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder
	 * @return	The HTTP client configuration
	 */
	public HTTPClientConfiguration createHTTPClientConfiguration(ConfigurationHolder configurationHolder);

	/**
	 * Submits the HTTP request and returns the HTTP response.
	 *  
	 * @param 	httpRequest
	 * 			The HTTP request to send
	 * @param 	httpClientConfiguration
	 * 			HTTP client configuration
	 * @param 	httpMethod
	 * 			The HTTP method to use
	 * @return	The HTTP response
	 */
	public HTTPResponse submitHTTPRequest(HTTPRequest httpRequest,HTTPClientConfiguration httpClientConfiguration,HTTPMethod httpMethod);
}