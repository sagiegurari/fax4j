package org.fax4j.spi.http;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.fax4j.spi.http.MultiPartFaxJob2HTTPRequestConverter.FaxJob2HTTPRequestConverterConfigurationConstants;

/**
 * This class provides common base for HTTP based SPIs which submit multi part
 * HTTP request and expect a JSON output.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.9
 */
public abstract class AbstractMultiPart2JSONHTTPFaxClientSpi extends HTTPFaxClientSpi
{
	/**
	 * This is the default constructor.
	 */
	public AbstractMultiPart2JSONHTTPFaxClientSpi()
	{
		super();
	}

	/**
	 * This function creates and returns a new faxjob to HTTP request converter instance.
	 * 
	 * @return	A new faxjob to HTTP request converter instance
	 */
	@Override
	protected FaxJob2HTTPRequestConverter createFaxJob2HTTPRequestConverter()
	{
		//get property part
		String propertyPart=this.getPropertyPart();

		//get configuration
		Map<String,String> configuration=this.getConfiguration();

		//setup configuration
		configuration=new HashMap<String,String>(configuration);
		
		//add common parts
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.ADD_FILE_NAME_AS_PART_PROPERTY_KEY.toString(),propertyPart),"false");

		//add ignorable parts
		String ignoreValue=FaxJob2HTTPRequestConverterConfigurationConstants.IGNORE_PARAMETER_KEY.toString();
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_TARGET_NAME_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_SENDER_NAME_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_SENDER_FAX_NUMBER_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_SENDER_EMAIL_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUSPEND_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.RESUME_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.CANCEL_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.GET_FAX_JOB_STATUS_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),ignoreValue);

		//modify configuration
		this.updateFaxJob2HTTPRequestConverterConfiguration(configuration);

		//create converter
		FaxJob2HTTPRequestConverter converter=new MultiPartFaxJob2HTTPRequestConverter();
		converter.setPropertyPart(propertyPart);
		converter.initialize(configuration);
		
		return converter;
	}

	/**
	 * This function creates and returns a new HTTP response handler.
	 * 
	 * @return	A new HTTP response handler
	 */
	@Override
	protected HTTPResponseHandler createHTTPResponseHandler()
	{
		//get property part
		String propertyPart=this.getPropertyPart();
		
		//get configuration
		Map<String,String> configuration=this.getConfiguration();

		//setup configuration
		configuration=new HashMap<String,String>(configuration);

		//modify configuration
		this.updateHTTPResponseHandlerConfiguration(configuration);

		//create handler
		HTTPResponseHandler handler=new JSONHTTPResponseHandler();
		handler.setPropertyPart(propertyPart);
		handler.initialize(configuration);
		
		return handler;
	}
	
	/**
	 * Hook for extending classes. 
	 *
	 * @param	configuration
	 * 			The converter configuration
	 */
	protected abstract void updateFaxJob2HTTPRequestConverterConfiguration(Map<String,String> configuration);
	
	/**
	 * Hook for extending classes. 
	 *
	 * @param	configuration
	 * 			The response handler configuration
	 */
	protected abstract void updateHTTPResponseHandlerConfiguration(Map<String,String> configuration);
}