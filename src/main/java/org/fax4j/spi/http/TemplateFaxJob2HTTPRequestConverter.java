package org.fax4j.spi.http;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.common.FaxActionType;
import org.fax4j.util.IOHelper;
import org.fax4j.util.SpiUtil;

/**
 * This class implements the fax job to HTTP request converter interface.<br>
 * This class will use request templates to construct the HTTP requests.<br> 
 * Only the HTTP resource, URL parameters and HTTP content accept template parameters.<br>
 * The HTTP resource is the URL part after the domain, for example http://www.mydomain.com/<b>myresource</b>.<br>
 * The HTTP URL parameters is the URL part after the ?, for example http://www.mydomain.com/myresource?<b>param1=value1</b>.<br>
 * The HTTP content is not part of the URL and contains the POST information.<br>
 * The template parameters are than updated with the values from the fax4j configuration or FaxJob data.<br>
 * All template parameters are defined as ${templatename} variables, for example: http://www.mydomain.com/myresource?faxnum=<b>${target.address}</b><br>
 * <br>
 * Below table describes the template parameters.<br>
 * <b>Template Parameters:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Template Parameter</td>
 * 		<td>Description</td>
 * 	</tr>
 * 	<tr>
 * 		<td>file</td>
 * 		<td>The fax job file content (in case of template) or name (in case of resource or URL parameters)</td>
 * 	</tr>
 * 	<tr>
 * 		<td>target.address</td>
 * 		<td>The fax job target address</td>
 * 	</tr>
 * 	<tr>
 * 		<td>target.name</td>
 * 		<td>The fax job target name</td>
 * 	</tr>
 * 	<tr>
 * 		<td>sender.name</td>
 * 		<td>The fax job sender name</td>
 * 	</tr>
 * 	<tr>
 * 		<td>sender.fax.number</td>
 * 		<td>The fax job sender fax number</td>
 * 	</tr>
 * 	<tr>
 * 		<td>sender.email</td>
 * 		<td>The fax job sender email</td>
 * 	</tr>
 * 	<tr>
 * 		<td>fax.job.id</td>
 * 		<td>The fax job ID</td>
 * 	</tr>
 * </table>
 * <br>
 * In case there is no template defined for a certain fax action, this class will return null.
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
 * 		<td>org.fax4j.spi.http.template.encoding</td>
 * 		<td>The template encoding, if encoding is not defined, the default system encoding is used.</td>
 * 		<td>none</td>
 * 		<td>The default system encoding</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.submit.template.url</td>
 * 		<td>The URL pointing to the submit fax job template.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.suspend.template.url</td>
 * 		<td>The URL pointing to the suspend fax job template.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.resume.template.url</td>
 * 		<td>The URL pointing to the resume fax job template.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.cancel.template.url</td>
 * 		<td>The URL pointing to the cancel fax job template.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.get.status.template.url</td>
 * 		<td>The URL pointing to the get fax job status template.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.42.4
 */
public class TemplateFaxJob2HTTPRequestConverter extends AbstractFaxJob2HTTPRequestConverter
{
	/**The HTTP request template map*/
	private Map<String,String> httpRequestTemplateMap;

	/**
	 * This class holds the configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.42.4
	 */
	public enum FaxJob2HTTPRequestConverterConfigurationConstants
	{
		/**The template encoding property key*/
		TEMPLATE_ENCODING_PROPERTY_KEY("org.fax4j.spi.{0}.template.encoding"),
		/**The submit fax job template URL property key*/
		SUBMIT_FAX_JOB_TEMPLATE_PROPERTY_KEY("org.fax4j.spi.{0}.submit.template.url"),
		/**The suspend fax job template URL property key*/
		SUSPEND_FAX_JOB_TEMPLATE_PROPERTY_KEY("org.fax4j.spi.{0}.suspend.template.url"),
		/**The resume fax job template URL property key*/
		RESUME_FAX_JOB_TEMPLATE_PROPERTY_KEY("org.fax4j.spi.{0}.resume.template.url"),
		/**The cancel fax job template URL property key*/
		CANCEL_FAX_JOB_TEMPLATE_PROPERTY_KEY("org.fax4j.spi.{0}.cancel.template.url"),
		/**The get fax job status template URL property key*/
		GET_FAX_JOB_STATUS_TEMPLATE_PROPERTY_KEY("org.fax4j.spi.{0}.get.status.template.url");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxJob2HTTPRequestConverterConfigurationConstants(String value)
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
	public TemplateFaxJob2HTTPRequestConverter()
	{
		super(Fax4JProvider.FAX4J_PROVIDER);
	}

	/**
	 * This function initializes the component.
	 */
	@Override
	protected void initializeImpl()
	{
		//init templates map
		this.httpRequestTemplateMap=new HashMap<String,String>();
		
		String[] templateNames=new String[]
		{
			FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_FAX_JOB_TEMPLATE_PROPERTY_KEY.toString(),
			FaxJob2HTTPRequestConverterConfigurationConstants.SUSPEND_FAX_JOB_TEMPLATE_PROPERTY_KEY.toString(),
			FaxJob2HTTPRequestConverterConfigurationConstants.RESUME_FAX_JOB_TEMPLATE_PROPERTY_KEY.toString(),
			FaxJob2HTTPRequestConverterConfigurationConstants.CANCEL_FAX_JOB_TEMPLATE_PROPERTY_KEY.toString(),
			FaxJob2HTTPRequestConverterConfigurationConstants.GET_FAX_JOB_STATUS_TEMPLATE_PROPERTY_KEY.toString()
		};
		
		//get templates amount
		int amount=templateNames.length;
		
		//get template encoding value
		String encoding=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.TEMPLATE_ENCODING_PROPERTY_KEY);
		
		String templateName=null;
		String urlStr=null;
		URL url=null;
		String template=null;
		for(int index=0;index<amount;index++)
		{
			//get next element
			templateName=templateNames[index];
			
			//get URL string
			urlStr=this.getConfigurationValue(templateName);
			
			if(urlStr!=null)
			{
				try
				{
					//create URL
					url=new URL(urlStr);
					
					//read template
					InputStream stream=url.openStream();
					Reader reader=IOHelper.createReader(stream,encoding);
					template=IOHelper.readTextStream(reader);
					
					//cache template
					this.httpRequestTemplateMap.put(templateName,template);
				}
				catch(Throwable throwable)
				{
					throw new FaxException("Unable to load template for URL: "+urlStr,throwable);
				}
			}
		}
	}

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
	public HTTPRequest createHTTPRequest(HTTPFaxClientSpi faxClientSpi,FaxActionType faxActionType,FaxJob faxJob)
	{
		FaxJob2HTTPRequestConverterConfigurationConstants templateName=null;
		switch(faxActionType)
		{
			case SUBMIT_FAX_JOB:
				templateName=FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_FAX_JOB_TEMPLATE_PROPERTY_KEY;
				break;
			case SUSPEND_FAX_JOB:
				templateName=FaxJob2HTTPRequestConverterConfigurationConstants.SUSPEND_FAX_JOB_TEMPLATE_PROPERTY_KEY;
				break;
			case RESUME_FAX_JOB:
				templateName=FaxJob2HTTPRequestConverterConfigurationConstants.RESUME_FAX_JOB_TEMPLATE_PROPERTY_KEY;
				break;
			case CANCEL_FAX_JOB:
				templateName=FaxJob2HTTPRequestConverterConfigurationConstants.CANCEL_FAX_JOB_TEMPLATE_PROPERTY_KEY;
				break;
			case GET_FAX_JOB_STATUS:
				templateName=FaxJob2HTTPRequestConverterConfigurationConstants.GET_FAX_JOB_STATUS_TEMPLATE_PROPERTY_KEY;
				break;
			default:
				throw new FaxException("Fax action type: "+faxActionType+" not supported.");
		}

		//create HTTP request
		HTTPRequest httpRequest=this.createHTTPRequest(faxClientSpi,faxActionType,templateName,faxJob);
		
		return httpRequest;
	}
	
	/**
	 * This function formats the provided template.
	 * 
	 * @param 	template
	 * 			The template
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The formatted template
	 */
	protected String formatTemplate(String template,FaxJob faxJob)
	{
		return SpiUtil.formatTemplate(template,faxJob,null,true,false);
	}
	
	/**
	 * This function formats the HTTP resource.
	 * 
	 * @param	faxClientSpi
	 * 			The HTTP fax client SPI
	 * @param	faxActionType
	 * 			The fax action type
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The formatted HTTP resource
	 */
	protected String formatHTTPResource(HTTPFaxClientSpi faxClientSpi,FaxActionType faxActionType,FaxJob faxJob)
	{
		//get resource
		String resourceTemplate=faxClientSpi.getHTTPResource(faxActionType);
		
		//format resource
		String resource=SpiUtil.formatTemplate(resourceTemplate,faxJob,SpiUtil.URL_ENCODER,false,false);
		
		return resource;
	}

	/**
	 * This function formats the HTTP URL parameters.
	 * 
	 * @param	faxClientSpi
	 * 			The HTTP fax client SPI
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The formatted HTTP URL parameters
	 */
	protected String formatHTTPURLParameters(HTTPFaxClientSpi faxClientSpi,FaxJob faxJob)
	{
		//get URL parameters
		String urlParametersTemplate=faxClientSpi.getHTTPURLParameters();
		
		//format URL parameters
		String urlParameters=SpiUtil.formatTemplate(urlParametersTemplate,faxJob,SpiUtil.URL_ENCODER,false,false);
		
		return urlParameters;
	}
	
	/**
	 * Creates the HTTP request from the fax job data.
	 *  
	 * @param	faxClientSpi
	 * 			The HTTP fax client SPI
	 * @param	faxActionType
	 * 			The fax action type
	 * @param	templateNameEnum
	 * 			The template name
	 * @param 	faxJob
	 * 			The fax job object
	 * @return 	The HTTP request to send
	 */
	protected HTTPRequest createHTTPRequest(HTTPFaxClientSpi faxClientSpi,FaxActionType faxActionType,Enum<?> templateNameEnum,FaxJob faxJob)
	{
		//get template name string
		String templateName=templateNameEnum.toString();

		//get template
		String template=this.httpRequestTemplateMap.get(templateName);

		HTTPRequest httpRequest=null;
		if(template!=null)
		{
			//format template
			String content=this.formatTemplate(template,faxJob);

			//format resource
			String resource=this.formatHTTPResource(faxClientSpi,faxActionType,faxJob);
			
			//format URL parameters
			String parametersText=this.formatHTTPURLParameters(faxClientSpi,faxJob);
			
			//create HTTP request
			httpRequest=new HTTPRequest();
			
			//setup HTTP request
			httpRequest.setResource(resource);
			httpRequest.setContent(content);
			httpRequest.setParametersText(parametersText);
		}

		return httpRequest;
	}
}