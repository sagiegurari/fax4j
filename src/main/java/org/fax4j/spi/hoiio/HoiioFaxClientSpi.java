package org.fax4j.spi.hoiio;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.http.AbstractMultiPart2JSONHTTPFaxClientSpi;
import org.fax4j.spi.http.CommonHTTPClientConfiguration;
import org.fax4j.spi.http.HTTPClient.HTTPMethod;
import org.fax4j.spi.http.HTTPClientConfiguration;
import org.fax4j.spi.http.JSONHTTPResponseHandler.JSONHTTPResponseHandlerConfigurationConstants;
import org.fax4j.spi.http.MultiPartFaxJob2HTTPRequestConverter.FaxJob2HTTPRequestConverterConfigurationConstants;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will invoke the requests by sending HTTP requests to a Hoiio service.<br>
 * This implementation is not meant to promote the Hoiio commercial product but to serve
 * as a service provider interface for those using this product.<br>
 * <br>
 * The configuration of the fax4j framework is made up of 3 layers.<br>
 * The configuration is based on simple properties.<br>
 * Each layer overrides the lower layers by adding/changing the property values.<br>
 * The first layer is the internal fax4j.properties file located in the fax4j jar.<br>
 * This layer contains the preconfigured values for the fax4j framework and can be changed
 * by updating these properties in the higher layers.<br>
 * The second layer is the external fax4j.properties file that is located on the classpath.<br>
 * This file is optional and provides the ability to override the internal configuration for the
 * entire fax4j framework.<br>
 * The top most layer is the optional java.util.Properties object provided by the external classes
 * when creating a new fax client.<br>
 * These properties enable to override the configuration of the lower 2 layers.<br>
 * <br>
 * <b>SPI Status (Draft, Beta, Stable): </b>Stable<br>
 * <br>
 * Below table describes the configuration values relevant for this class.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hoiio.client.class.name</td>
 * 		<td>The HTTP client class name used to submit the HTTP requests</td>
 * 		<td>org.fax4j.spi.http.ApacheHTTPClient</td>
 * 		<td>org.fax4j.spi.http.ApacheHTTPClient</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hoiio.app.id</td>
 * 		<td>The hoiio Application ID</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hoiio.access.token</td>
 * 		<td>The hoiio access token</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * 	</tr>
 * </table>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>HTTP client required jars.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.9
 */
public class HoiioFaxClientSpi extends AbstractMultiPart2JSONHTTPFaxClientSpi
{
	/**The Hoiio property part*/
	public static final String HOIIO_PROPERTY_PART="hoiio";

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.42.9
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The Application ID property key*/
		APPLICATION_ID_PROPERTY_KEY("org.fax4j.spi.hoiio.app.id"),
		/**The access token property key*/
		ACCESS_TOKEN_PROPERTY_KEY("org.fax4j.spi.hoiio.access.token");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxClientSpiConfigurationConstants(String value)
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
	 * This is the default constructor.
	 */
	public HoiioFaxClientSpi()
	{
		super();
	}
	
	/**
	 * Returns the property part.<br>
	 * Property parts enables to replace the input request key with the part defined
	 * to enable to reuse services with different configuration blocks.<br>
	 * Property parts will be replaced with the {0} values in the key,
	 * for example org.fax4j.{0}.somekey with part abc will be replaced to org.fax4j.abc.somekey
	 *  
	 * @return	The property part
	 */
	@Override
	public String getPropertyPart()
	{
		return HoiioFaxClientSpi.HOIIO_PROPERTY_PART;
	}

	/**
	 * This function creates and returns the HTTP configuration object.
	 * 
	 * @return	The HTTP configuration object
	 */
	@Override
	protected HTTPClientConfiguration createHTTPClientConfiguration()
	{
		CommonHTTPClientConfiguration configuration=new CommonHTTPClientConfiguration();
		configuration.setHostName("secure.hoiio.com");
		configuration.setSSL(true);
		configuration.setMethod(FaxActionType.SUBMIT_FAX_JOB,HTTPMethod.POST);
		configuration.setMethod(FaxActionType.GET_FAX_JOB_STATUS,HTTPMethod.POST);

		return configuration;
	}
	
	/**
	 * Hook for extending classes. 
	 *
	 * @param	configuration
	 * 			The converter configuration
	 */
	@Override
	protected void updateFaxJob2HTTPRequestConverterConfiguration(Map<String,String> configuration)
	{
		//get ID and token
		String applicationID=this.getConfigurationValue(FaxClientSpiConfigurationConstants.APPLICATION_ID_PROPERTY_KEY);
		String accessToken=this.getConfigurationValue(FaxClientSpiConfigurationConstants.ACCESS_TOKEN_PROPERTY_KEY);
		
		//validate data
		if((applicationID==null)||(accessToken==null))
		{
			throw new FaxException("Missing hoiio Application ID/Access Token values.");
		}

		//get property part
		String propertyPart=this.getPropertyPart();

		//modify configuration
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_FILE_CONTENT_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),"file");
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_TARGET_ADDRESS_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),"dest");
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_SENDER_FAX_NUMBER_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),"caller_id");
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.GET_FAX_JOB_STATUS_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY.toString(),propertyPart),"txn_ref");
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.ADDITIONAL_PARAMETER_PROPERTY_KEY_PREFIX.toString()+"app_id",propertyPart),applicationID);
		configuration.put(MessageFormat.format(FaxJob2HTTPRequestConverterConfigurationConstants.ADDITIONAL_PARAMETER_PROPERTY_KEY_PREFIX.toString()+"access_token",propertyPart),accessToken);
	}

	/**
	 * Hook for extending classes. 
	 *
	 * @param	configuration
	 * 			The response handler configuration
	 */
	@Override
	protected void updateHTTPResponseHandlerConfiguration(Map<String,String> configuration)
	{
		//get property part
		String propertyPart=this.getPropertyPart();

		//modify configuration
		configuration.put(MessageFormat.format(JSONHTTPResponseHandlerConfigurationConstants.SUBMIT_JSON_OUTPUT_PROPERTY_KEY.toString(),propertyPart),"txn_ref");
		configuration.put(MessageFormat.format(JSONHTTPResponseHandlerConfigurationConstants.ERROR_DETECTION_PATH_PROPERTY_KEY.toString(),propertyPart),"status");
		configuration.put(MessageFormat.format(JSONHTTPResponseHandlerConfigurationConstants.ERROR_DETECTION_VALUE_PROPERTY_KEY.toString(),propertyPart),"error_");
		configuration.put(MessageFormat.format(JSONHTTPResponseHandlerConfigurationConstants.GET_STATUS_JSON_OUTPUT_PROPERTY_KEY.toString(),propertyPart),"fax_status");
		configuration.put(MessageFormat.format(JSONHTTPResponseHandlerConfigurationConstants.IN_PROGRESS_STATUS_MAPPING_PROPERTY_KEY.toString(),propertyPart),"ongoing");
		configuration.put(MessageFormat.format(JSONHTTPResponseHandlerConfigurationConstants.ERROR_STATUS_MAPPING_PROPERTY_KEY.toString(),propertyPart),"unanswered;failed;busy");
	}

	/**
	 * This function initializes the fax action type to resource mappings.
	 * 
	 * @return	The fax action type to resource mappings
	 */
	@Override
	protected Map<FaxActionType,String> initializeFaxActionType2ResourceMap()
	{
		//set resources
		Map<FaxActionType,String> map=new HashMap<FaxActionType,String>();
		map.put(FaxActionType.SUBMIT_FAX_JOB,"/open/fax/send");
		map.put(FaxActionType.GET_FAX_JOB_STATUS,"/open/fax/query_status");

		return map;
	}

	/**
	 * This function returns the HTTP URL parameters from the configuration.
	 * 
	 * @return	The HTTP URL parameters
	 */
	@Override
	protected String initializeURLParameters()
	{
		return null;
	}
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void suspendFaxJobImpl(FaxJob faxJob)
	{
		this.throwUnsupportedException();
	}
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void resumeFaxJobImpl(FaxJob faxJob)
	{
		this.throwUnsupportedException();
	}
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void cancelFaxJobImpl(FaxJob faxJob)
	{
		this.throwUnsupportedException();
	}
}