package org.fax4j.spi.http;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.http.HTTPClient.HTTPMethod;

/**
 * This is the common HTTP client configuration.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.04
 * @since	0.20c
 */
public class CommonHTTPClientConfiguration implements HTTPClientConfiguration
{
	/**The host name*/
	private String hostName;
	/**The port*/
	private int port;
	/**True to use SSL (HTTPS), false for simple HTTP*/
	private boolean ssl;
	/**The fax action to HTTP method map*/
	private Map<FaxActionType,HTTPMethod> methodMap;

	/**
	 * This class holds the configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.20c
	 */
	public enum HTTPClientConfigurationConstants
	{
		/**The host name property key*/
		HOST_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.host.name"),
		/**The port property key*/
		PORT_PROPERTY_KEY("org.fax4j.spi.{0}.port"),
		/**The SSL property key*/
		SSL_PROPERTY_KEY("org.fax4j.spi.{0}.ssl"),
		/**HTTP method property key*/
		SUBMIT_HTTP_METHOD_PROPERTY_KEY("org.fax4j.spi.{0}.submit.method"),
		/**HTTP method property key*/
		SUSPEND_HTTP_METHOD_PROPERTY_KEY("org.fax4j.spi.{0}.suspend.method"),
		/**HTTP method property key*/
		RESUME_HTTP_METHOD_PROPERTY_KEY("org.fax4j.spi.{0}.resume.method"),
		/**HTTP method property key*/
		CANCEL_HTTP_METHOD_PROPERTY_KEY("org.fax4j.spi.{0}.cancel.method"),
		/**HTTP method property key*/
		GET_STATUS_HTTP_METHOD_PROPERTY_KEY("org.fax4j.spi.{0}.get.status.method");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private HTTPClientConfigurationConstants(String value)
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
	 * This is the class constructor.
	 */
	public CommonHTTPClientConfiguration()
	{
		super();
		
		//init map
		this.methodMap=new HashMap<FaxActionType,HTTPClient.HTTPMethod>();
		
		//set defaults
		this.setPort(80);
		this.setSSL(false);
		FaxActionType[] faxActionTypes=FaxActionType.values();
		FaxActionType faxActionType=null;
		int amount=faxActionTypes.length;
		for(int index=0;index<amount;index++)
		{
			//get next fax action type
			faxActionType=faxActionTypes[index];
			
			//set default
			this.setMethod(faxActionType,HTTPMethod.POST);
		}
	}

	/**
	 * This function returns the host name.
	 * 
	 * @return 	The host name
	 */
	public final String getHostName()
	{
		return this.hostName;
	}

	/**
	 * This function sets the host name.
	 * 
	 * @param 	hostName
	 * 			The new value for the host name
	 */
	public final void setHostName(String hostName)
	{
		this.hostName=hostName;
	}

	/**
	 * This function returns the port.
	 * 
	 * @return 	The port
	 */
	public final int getPort()
	{
		return this.port;
	}

	/**
	 * This function sets the port.
	 * 
	 * @param 	port
	 * 			The new value for the port
	 */
	public final void setPort(int port)
	{
		this.port=port;
	}

	/**
	 * This function returns the SSL flag value.
	 * 
	 * @return 	The SSL flag value
	 */
	public final boolean isSSL()
	{
		return this.ssl;
	}

	/**
	 * This function sets the SSL flag value.
	 * 
	 * @param 	ssl
	 * 			The new value for the SSL flag value
	 */
	public final void setSSL(boolean ssl)
	{
		this.ssl=ssl;
	}

	/**
	 * This function returns the HTTP method based on the provided
	 * fax action type.
	 * 
	 * @param	faxActionType
	 * 			The fax action type
	 * @return 	The HTTP method
	 */
	public final HTTPMethod getMethod(FaxActionType faxActionType)
	{
		return this.methodMap.get(faxActionType);
	}

	/**
	 * This function sets the HTTP method based on the provided
	 * fax action type.
	 * 
	 * @param	faxActionType
	 * 			The fax action type
	 * @param 	httpMethod
	 * 			The HTTP method
	 */
	public final void setMethod(FaxActionType faxActionType,HTTPMethod httpMethod)
	{
		this.methodMap.put(faxActionType,httpMethod);
	}
}