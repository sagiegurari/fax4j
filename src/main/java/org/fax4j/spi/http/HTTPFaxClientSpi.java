package org.fax4j.spi.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.common.ServiceFactory;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.spi.http.HTTPClient.HTTPMethod;
import org.fax4j.util.ReflectionHelper;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will invoke the requests by sending HTTP requests to a web server that supports
 * sending fax messages.<br>
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
 * 		<td>org.fax4j.spi.http.client.class.name</td>
 * 		<td>The HTTP client class name used to submit the HTTP requests</td>
 * 		<td>org.fax4j.spi.http.ApacheHTTPClient</td>
 * 		<td>org.fax4j.spi.http.ApacheHTTPClient</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.faxjob2request.converter.class.name</td>
 * 		<td>The fax job to HTTP request converter class name used to convert the fax job data into the relevant HTTP request.</td>
 * 		<td>org.fax4j.spi.http.MultiPartFaxJob2HTTPRequestConverter</td>
 * 		<td>org.fax4j.spi.http.MultiPartFaxJob2HTTPRequestConverter</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.response.handler.class.name</td>
 * 		<td>The HTTP response handler class name used to update the fax job data based on the HTTP response.</td>
 * 		<td>org.fax4j.spi.http.JSONHTTPResponseHandler</td>
 * 		<td>org.fax4j.spi.http.JSONHTTPResponseHandler</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.host.name</td>
 * 		<td>The target (service provider) host name.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.port</td>
 * 		<td>The target port number.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.ssl</td>
 * 		<td>True to use SSL (HTTPS), false for simple HTTP</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.submit.resource</td>
 * 		<td>The default HTTP resource of the submit fax job request</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.suspend.resource</td>
 * 		<td>The default HTTP resource of the suspend fax job request</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.resume.resource</td>
 * 		<td>The default HTTP resource of the resume fax job request</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.cancel.resource</td>
 * 		<td>The default HTTP resource of the cancel fax job request</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.get.status.resource</td>
 * 		<td>The default HTTP resource of the get fax job status request</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.url.parameters</td>
 * 		<td>The default HTTP URL parameters of the request</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.submit.method</td>
 * 		<td>The HTTP method for the submit action</td>
 * 		<td>POST</td>
 * 		<td>POST</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.suspend.method</td>
 * 		<td>The HTTP method for the suspend action</td>
 * 		<td>POST</td>
 * 		<td>POST</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.resume.method</td>
 * 		<td>The HTTP method for the resume action</td>
 * 		<td>POST</td>
 * 		<td>POST</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.cancel.method</td>
 * 		<td>The HTTP method for the cancel action</td>
 * 		<td>POST</td>
 * 		<td>POST</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.get.status.method</td>
 * 		<td>The HTTP method for the get fax job status action</td>
 * 		<td>POST</td>
 * 		<td>POST</td>
 * 		<td>false</td>
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
 * @version 1.14
 * @since	0.1
 */
public class HTTPFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The HTTP client used to submit the HTTP requests*/
	private HTTPClient httpClient;
	/**The HTTP client configuration*/
	private HTTPClientConfiguration httpClientConfiguration;
	/**The fax job to HTTP request converter*/
	private FaxJob2HTTPRequestConverter faxJob2HTTPRequestConverter;
	/**The HTTP response handler*/
	private HTTPResponseHandler httpResponseHandler;
	/**The fax action type to HTTP resource mapping*/
	private Map<FaxActionType,String> faxActionType2ResourceMap;
	/**The default HTTP URL parameters*/
	private String urlParameters;
	/**The HTTP property part*/
	public static final String HTTP_PROPERTY_PART="http";

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.03
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The HTTP client class name property key*/
		HTTP_CLIENT_CLASS_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.client.class.name"),
		/**The fax job to HTTP request converter class name property key*/
		FAX_JOB2HTTP_REQUEST_CONVERTER_CLASS_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.faxjob2request.converter.class.name"),
		/**The HTTP response handler class name property key*/
		HTTP_RESPONSE_HANDLER_CLASS_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.response.handler.class.name"),
		/**The HTTP resource property key*/
		SUBMIT_HTTP_RESOURCE_PROPERTY_KEY("org.fax4j.spi.{0}.submit.resource"),
		/**The HTTP resource property key*/
		SUSPEND_HTTP_RESOURCE_PROPERTY_KEY("org.fax4j.spi.{0}.suspend.resource"),
		/**The HTTP resource property key*/
		RESUME_HTTP_RESOURCE_PROPERTY_KEY("org.fax4j.spi.{0}.resume.resource"),
		/**The HTTP resource property key*/
		CANCEL_HTTP_RESOURCE_PROPERTY_KEY("org.fax4j.spi.{0}.cancel.resource"),
		/**The HTTP resource property key*/
		GET_STATUS_HTTP_RESOURCE_PROPERTY_KEY("org.fax4j.spi.{0}.get.status.resource"),
		/**The HTTP URL parameters property key*/
		HTTP_URL_PARAMETERS_PROPERTY_KEY("org.fax4j.spi.{0}.url.parameters");

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
	public HTTPFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		String value=this.getConfigurationValue(FaxClientSpiConfigurationConstants.HTTP_CLIENT_CLASS_NAME_PROPERTY_KEY);
		if(value==null)
		{
			value=ApacheHTTPClient.class.getName();
		}

		//create HTTP client
		this.httpClient=(HTTPClient)ReflectionHelper.createInstance(value);
		
		//create configuration
		this.httpClientConfiguration=this.createHTTPClientConfiguration();

		//create fax job to HTTP request converter
		this.faxJob2HTTPRequestConverter=this.createFaxJob2HTTPRequestConverter();

		//create HTTP response handler
		this.httpResponseHandler=this.createHTTPResponseHandler();

		//get resources
		Map<FaxActionType,String> map=this.initializeFaxActionType2ResourceMap();
		this.faxActionType2ResourceMap=new HashMap<FaxActionType,String>(map);
		
		//get URL parameters
		this.urlParameters=this.initializeURLParameters();
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
		return HTTPFaxClientSpi.HTTP_PROPERTY_PART;
	}

	/**
	 * This function creates and returns the HTTP configuration object.
	 * 
	 * @return	The HTTP configuration object
	 */
	protected HTTPClientConfiguration createHTTPClientConfiguration()
	{
		//create configuration
		HTTPClientConfiguration configuration=this.httpClient.createHTTPClientConfiguration(this);
		
		return configuration;
	}

	/**
	 * This function creates and returns a new faxjob to HTTP request converter instance.
	 * 
	 * @return	A new faxjob to HTTP request converter instance
	 */
	protected FaxJob2HTTPRequestConverter createFaxJob2HTTPRequestConverter()
	{
		//get property part
		String propertyPart=this.getPropertyPart();

		//create converter
		FaxJob2HTTPRequestConverter converter=(FaxJob2HTTPRequestConverter)ServiceFactory.createService(FaxClientSpiConfigurationConstants.FAX_JOB2HTTP_REQUEST_CONVERTER_CLASS_NAME_PROPERTY_KEY,MultiPartFaxJob2HTTPRequestConverter.class.getName(),this,propertyPart);
		
		return converter;
	}

	/**
	 * This function creates and returns a new HTTP response handler.
	 * 
	 * @return	A new HTTP response handler
	 */
	protected HTTPResponseHandler createHTTPResponseHandler()
	{
		//get property part
		String propertyPart=this.getPropertyPart();

		//create handler
		HTTPResponseHandler handler=(HTTPResponseHandler)ServiceFactory.createService(FaxClientSpiConfigurationConstants.HTTP_RESPONSE_HANDLER_CLASS_NAME_PROPERTY_KEY,JSONHTTPResponseHandler.class.getName(),this,propertyPart);
		
		return handler;
	}
	
	/**
	 * This function initializes the fax action type to resource mappings.
	 * 
	 * @return	The fax action type to resource mappings
	 */
	protected Map<FaxActionType,String> initializeFaxActionType2ResourceMap()
	{
		//get resources
		Map<FaxActionType,String> map=new HashMap<FaxActionType,String>();
		FaxActionType[] faxActionTypes=new FaxActionType[]{FaxActionType.SUBMIT_FAX_JOB,
														   FaxActionType.SUSPEND_FAX_JOB,
														   FaxActionType.RESUME_FAX_JOB,
														   FaxActionType.CANCEL_FAX_JOB,
														   FaxActionType.GET_FAX_JOB_STATUS};
		Enum<?>[] keys=new Enum<?>[]{FaxClientSpiConfigurationConstants.SUBMIT_HTTP_RESOURCE_PROPERTY_KEY,
									 FaxClientSpiConfigurationConstants.SUSPEND_HTTP_RESOURCE_PROPERTY_KEY,
									 FaxClientSpiConfigurationConstants.RESUME_HTTP_RESOURCE_PROPERTY_KEY,
									 FaxClientSpiConfigurationConstants.CANCEL_HTTP_RESOURCE_PROPERTY_KEY,
									 FaxClientSpiConfigurationConstants.GET_STATUS_HTTP_RESOURCE_PROPERTY_KEY};
		String resource=null;
		for(int index=0;index<faxActionTypes.length;index++)
		{
			//get next resource
			resource=this.getConfigurationValue(keys[index]);
			if(resource!=null)
			{
				map.put(faxActionTypes[index],resource);
			}
		}
		
		return map;
	}

	/**
	 * This function returns the HTTP URL parameters from the configuration.
	 * 
	 * @return	The HTTP URL parameters
	 */
	protected String initializeURLParameters()
	{
		return this.getConfigurationValue(FaxClientSpiConfigurationConstants.HTTP_URL_PARAMETERS_PROPERTY_KEY);
	}

	/**
	 * Returns the HTTP client.
	 *  
	 * @return	The HTTP client
	 */
	protected final HTTPClient getHTTPClient()
	{
		return this.httpClient;
	}

	/**
	 * Returns the HTTP client configuration.
	 *  
	 * @return	The HTTP client configuration
	 */
	protected final HTTPClientConfiguration getHTTPClientConfiguration()
	{
		return this.httpClientConfiguration;
	}

	/**
	 * Returns the fax job to HTTP request converter.
	 *  
	 * @return	The fax job to HTTP request converter
	 */
	protected final FaxJob2HTTPRequestConverter getFaxJob2HTTPRequestConverter()
	{
		return this.faxJob2HTTPRequestConverter;
	}

	/**
	 * Returns the HTTP response handler.
	 *  
	 * @return	The HTTP response handler
	 */
	protected final HTTPResponseHandler getHTTPResponseHandler()
	{
		return this.httpResponseHandler;
	}

	/**
	 * This function returns the HTTP resource.
	 * 
	 * @param	faxActionType
	 * 			The fax action type
	 * @return	The HTTP resource
	 */
	protected final String getHTTPResource(FaxActionType faxActionType)
	{
		return this.faxActionType2ResourceMap.get(faxActionType);
	}
	
	/**
	 * This function returns the HTTP URL parameters.
	 * 
	 * @return	The HTTP URL parameters
	 */
	protected final String getHTTPURLParameters()
	{
		return this.urlParameters;
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void submitFaxJobImpl(FaxJob faxJob)
	{
		//create HTTP request
		HTTPRequest httpRequest=this.createSubmitFaxJobHTTPRequest(faxJob);
		
		//submit HTTP request
		this.submitHTTPRequest(faxJob,httpRequest,FaxActionType.SUBMIT_FAX_JOB);
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
		//create HTTP request
		HTTPRequest httpRequest=this.createSuspendFaxJobHTTPRequest(faxJob);
		
		//submit HTTP request
		this.submitHTTPRequest(faxJob,httpRequest,FaxActionType.SUSPEND_FAX_JOB);
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
		//create HTTP request
		HTTPRequest httpRequest=this.createResumeFaxJobHTTPRequest(faxJob);
		
		//submit HTTP request
		this.submitHTTPRequest(faxJob,httpRequest,FaxActionType.RESUME_FAX_JOB);
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
		//create HTTP request
		HTTPRequest httpRequest=this.createCancelFaxJobHTTPRequest(faxJob);
		
		//submit HTTP request
		this.submitHTTPRequest(faxJob,httpRequest,FaxActionType.CANCEL_FAX_JOB);
	}
	
	/**
	 * This function returns the fax job status.<br>
	 * Not all SPIs support extraction of the fax job status.<br>
	 * In case the SPI is unable to extract or does not support extracting
	 * of the fax job status, it will return the UNKNOWN status.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @return	The fax job status
	 */
	@Override
	protected FaxJobStatus getFaxJobStatusImpl(FaxJob faxJob)
	{
		//set default output
		FaxJobStatus faxJobStatus=FaxJobStatus.UNKNOWN;

		//create HTTP request
		HTTPRequest httpRequest=this.createGetFaxJobStatusHTTPRequest(faxJob);
		
		if(httpRequest!=null)
		{
			//submit HTTP request
			HTTPResponse httpResponse=this.submitHTTPRequest(faxJob,httpRequest,FaxActionType.GET_FAX_JOB_STATUS);
			
			//check HTTP response for fax job status
			if(httpResponse!=null)
			{
				faxJobStatus=this.httpResponseHandler.getFaxJobStatus(httpResponse);
				if(faxJobStatus==null)
				{
					faxJobStatus=FaxJobStatus.UNKNOWN;
				}
			}
		}
		
		return faxJobStatus;
	}
	
	/**
	 * Submits the HTTP request and returns the HTTP response.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @param 	httpRequest
	 * 			The HTTP request to send
	 * @param	faxActionType
	 * 			The fax action type
	 * @return	The HTTP response
	 */
	protected HTTPResponse submitHTTPRequest(FaxJob faxJob,HTTPRequest httpRequest,FaxActionType faxActionType)
	{
		HTTPResponse httpResponse=null;
		if(httpRequest==null)
		{
			this.throwUnsupportedException();
		}
		else
		{
			//setup default header properties
			Properties headerProperties=httpRequest.getHeaderProperties();
			if(headerProperties==null)
			{
				headerProperties=new Properties();
				httpRequest.setHeaderProperties(headerProperties);
			}
			
			//setup resource
			if(httpRequest.getResource()==null)
			{
				String resource=this.getHTTPResource(faxActionType);
				httpRequest.setResource(resource);
			}
	
			//setup URL parameters
			if(httpRequest.getParametersText()==null)
			{
				httpRequest.setParametersText(this.urlParameters);
			}
			
			//get HTTP method
			HTTPMethod httpMethod=this.httpClientConfiguration.getMethod(faxActionType);
			
			//submit HTTP request
			httpResponse=this.submitHTTPRequestImpl(httpRequest,httpMethod);
	
			//validate response status code
			int statusCode=httpResponse.getStatusCode();
			if(statusCode>=400)
			{
				throw new FaxException("Error while invoking HTTP request, return status code: "+statusCode);
			}
			
			//update fax job
			this.updateFaxJob(faxJob,httpResponse,faxActionType);
		}
		
		return httpResponse;
	}
	
	/**
	 * Submits the HTTP request and returns the HTTP response.
	 *  
	 * @param 	httpRequest
	 * 			The HTTP request to send
	 * @param 	httpMethod
	 * 			The HTTP method to use
	 * @return	The HTTP response
	 */
	protected HTTPResponse submitHTTPRequestImpl(HTTPRequest httpRequest,HTTPMethod httpMethod)
	{
		return this.httpClient.submitHTTPRequest(httpRequest,this.httpClientConfiguration,httpMethod);
	}
	
	/**
	 * Updates the fax job based on the data from the HTTP response.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @param 	httpResponse
	 * 			The HTTP response
	 * @param	faxActionType
	 * 			The fax action type
	 */
	protected void updateFaxJob(FaxJob faxJob,HTTPResponse httpResponse,FaxActionType faxActionType)
	{
		this.httpResponseHandler.updateFaxJob(faxJob,httpResponse,faxActionType);
	}
	
	/**
	 * Creates the HTTP request from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The HTTP request to send
	 */
	protected HTTPRequest createSubmitFaxJobHTTPRequest(FaxJob faxJob)
	{
		return this.faxJob2HTTPRequestConverter.createHTTPRequest(this,FaxActionType.SUBMIT_FAX_JOB,faxJob);
	}
	
	/**
	 * Creates the HTTP request from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The HTTP request to send
	 */
	protected HTTPRequest createSuspendFaxJobHTTPRequest(FaxJob faxJob)
	{
		return this.faxJob2HTTPRequestConverter.createHTTPRequest(this,FaxActionType.SUSPEND_FAX_JOB,faxJob);
	}
	
	/**
	 * Creates the HTTP request from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The HTTP request to send
	 */
	protected HTTPRequest createResumeFaxJobHTTPRequest(FaxJob faxJob)
	{
		return this.faxJob2HTTPRequestConverter.createHTTPRequest(this,FaxActionType.RESUME_FAX_JOB,faxJob);
	}
	
	/**
	 * Creates the HTTP request from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The HTTP request to send
	 */
	protected HTTPRequest createCancelFaxJobHTTPRequest(FaxJob faxJob)
	{
		return this.faxJob2HTTPRequestConverter.createHTTPRequest(this,FaxActionType.CANCEL_FAX_JOB,faxJob);
	}
	
	/**
	 * Creates the HTTP request from the fax job data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The HTTP request to send
	 */
	protected HTTPRequest createGetFaxJobStatusHTTPRequest(FaxJob faxJob)
	{
		return this.faxJob2HTTPRequestConverter.createHTTPRequest(this,FaxActionType.GET_FAX_JOB_STATUS,faxJob);
	}
}