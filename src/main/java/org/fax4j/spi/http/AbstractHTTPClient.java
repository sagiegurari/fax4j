package org.fax4j.spi.http;

import org.fax4j.FaxException;
import org.fax4j.Provider;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.http.CommonHTTPClientConfiguration.HTTPClientConfigurationConstants;

/**
 * This abstract HTTP client provides partial implementation for common functionality.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.34
 */
public abstract class AbstractHTTPClient implements HTTPClient
{
	/**
	 * This is the default constructor.
	 */
	public AbstractHTTPClient()
	{
		super();
	}

	/**
	 * This function returns the provider.
	 * 
	 * @return	The provider
	 */
	public final Provider getProvider()
	{
		return Fax4JProvider.FAX4J_PROVIDER;
	}

	/**
	 * This function creates the HTTP client configuration to be
	 * used later on by this HTTP client type.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder
	 * @return	The HTTP client configuration
	 */
	public HTTPClientConfiguration createHTTPClientConfiguration(ConfigurationHolder configurationHolder)
	{
		//get server values
		String hostName=configurationHolder.getConfigurationValue(HTTPClientConfigurationConstants.HOST_NAME_PROPERTY_KEY);
		if(hostName==null)
		{
			throw new FaxException("Host name not defined in property: "+HTTPClientConfigurationConstants.HOST_NAME_PROPERTY_KEY);
		}
		String value=configurationHolder.getConfigurationValue(HTTPClientConfigurationConstants.PORT_PROPERTY_KEY);
		if(value==null)
		{
			value=String.valueOf(-1);
		}
		int port=Integer.parseInt(value);
		boolean ssl=Boolean.parseBoolean(configurationHolder.getConfigurationValue(HTTPClientConfigurationConstants.SSL_PROPERTY_KEY));
		
		//create configuration
		CommonHTTPClientConfiguration configuration=new CommonHTTPClientConfiguration();
		
		//set values
		configuration.setHostName(hostName);
		configuration.setPort(port);
		configuration.setSSL(ssl);

		//set methods
		Enum<?>[] methodProperties=new Enum<?>[]{HTTPClientConfigurationConstants.SUBMIT_HTTP_METHOD_PROPERTY_KEY,
												 HTTPClientConfigurationConstants.SUSPEND_HTTP_METHOD_PROPERTY_KEY,
												 HTTPClientConfigurationConstants.RESUME_HTTP_METHOD_PROPERTY_KEY,
												 HTTPClientConfigurationConstants.CANCEL_HTTP_METHOD_PROPERTY_KEY,
												 HTTPClientConfigurationConstants.GET_STATUS_HTTP_METHOD_PROPERTY_KEY};
		FaxActionType[] faxActionTypes=new FaxActionType[]{FaxActionType.SUBMIT_FAX_JOB,
														   FaxActionType.SUSPEND_FAX_JOB,
														   FaxActionType.RESUME_FAX_JOB,
														   FaxActionType.CANCEL_FAX_JOB,
														   FaxActionType.GET_FAX_JOB_STATUS};
		HTTPMethod httpMethod=null;
		for(int index=0;index<methodProperties.length;index++)
		{
			//set next method
			value=configurationHolder.getConfigurationValue(methodProperties[index]);
			httpMethod=HTTPMethod.POST;
			if(value!=null)
			{
				httpMethod=HTTPMethod.valueOf(value);
			}
			configuration.setMethod(faxActionTypes[index],httpMethod);
		}

		return configuration;
	}
}