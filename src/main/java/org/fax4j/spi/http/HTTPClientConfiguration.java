package org.fax4j.spi.http;

import org.fax4j.common.FaxActionType;
import org.fax4j.spi.http.HTTPClient.HTTPMethod;

/**
 * This interface holds the HTTP client configuration.<br>
 * Each HTTP client will have different configuration requirements
 * and therefore this interface is just a flagging interface.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.20c
 */
public interface HTTPClientConfiguration
{
	/**
	 * This function returns the host name.
	 * 
	 * @return 	The host name
	 */
	public String getHostName();

	/**
	 * This function returns the port.
	 * 
	 * @return 	The port
	 */
	public int getPort();

	/**
	 * This function returns the SSL flag value.
	 * 
	 * @return 	The SSL flag value
	 */
	public boolean isSSL();

	/**
	 * This function returns the HTTP method based on the provided
	 * fax action type.
	 * 
	 * @param	faxActionType
	 * 			The fax action type
	 * @return 	The HTTP method
	 */
	public HTTPMethod getMethod(FaxActionType faxActionType);
}