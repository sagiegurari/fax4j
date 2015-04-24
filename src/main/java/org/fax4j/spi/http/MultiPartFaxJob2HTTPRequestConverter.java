package org.fax4j.spi.http;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.spi.http.HTTPRequest.ContentPartType;

/**
 * This class implements the fax job to HTTP request converter interface.<br>
 * This class will construct a multi part HTTP requests.<br> 
 * The parameter names are set in the fax4j configuration and their values are extracted from the fax job.
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
 * 		<td>org.fax4j.spi.http.multi.part.submit.file.content.parameter</td>
 * 		<td>The submit fax action, file content parameter name.</td>
 * 		<td>file</td>
 * 		<td>file</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.submit.file.name.parameter</td>
 * 		<td>The submit fax action, file name parameter name.</td>
 * 		<td>filename</td>
 * 		<td>filename</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.submit.target.address.parameter</td>
 * 		<td>The submit fax action, target address parameter name.</td>
 * 		<td>targetaddress</td>
 * 		<td>targetaddress</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.submit.target.name.parameter</td>
 * 		<td>The submit fax action, target name parameter name.</td>
 * 		<td>targetname</td>
 * 		<td>targetname</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.submit.sender.name.parameter</td>
 * 		<td>The submit fax action, sender name parameter name.</td>
 * 		<td>sendername</td>
 * 		<td>sendername</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.submit.sender.fax.number.parameter</td>
 * 		<td>The submit fax action, sender fax number parameter name.</td>
 * 		<td>senderfaxnumber</td>
 * 		<td>senderfaxnumber</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.submit.sender.email.parameter</td>
 * 		<td>The submit fax action, sender email parameter name.</td>
 * 		<td>senderemail</td>
 * 		<td>senderemail</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.suspend.fax.job.id.parameter</td>
 * 		<td>The suspend fax action, fax job ID parameter name.</td>
 * 		<td>faxjobid</td>
 * 		<td>faxjobid</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.resume.fax.job.id.parameter</td>
 * 		<td>The resume fax action, fax job ID parameter name.</td>
 * 		<td>faxjobid</td>
 * 		<td>faxjobid</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.cancel.fax.job.id.parameter</td>
 * 		<td>The cancel fax action, fax job ID parameter name.</td>
 * 		<td>faxjobid</td>
 * 		<td>faxjobid</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.get.status.fax.job.id.parameter</td>
 * 		<td>The get fax job status fax action, fax job ID parameter name.</td>
 * 		<td>faxjobid</td>
 * 		<td>faxjobid</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.parameter.key.X</td>
 * 		<td>The additional parameter to submit where X is the parameter key and the property value is the parameter value,<br>
 * 			For example org.fax4j.spi.http.multi.part.parameter.key.my.property=my_value<br>
 * 			This converter will also send parameter my.property with value my_value
 * 		</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.multi.part.add.file.name.as.part</td>
 * 		<td>The add the file name as a separate part property key.</td>
 * 		<td>true</td>
 * 		<td>true</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.42.7
 */
public class MultiPartFaxJob2HTTPRequestConverter extends AbstractFaxJob2HTTPRequestConverter
{
	/**The submit fax action, file content parameter name*/
	protected String submitFileContentParameter;
	/**The submit fax action, file name parameter name*/
	protected String submitFileNameParameter;
	/**The submit fax action, target address parameter name*/
	protected String submitTargetAddressParameter;
	/**The submit fax action, target name parameter name*/
	protected String submitTargetNameParameter;
	/**The submit fax action, sender name parameter name*/
	protected String submitSenderNameParameter;
	/**The submit fax action, sender fax number parameter name*/
	protected String submitSenderFaxNumberParameter;
	/**The submit fax action, sender email parameter name*/
	protected String submitSenderEMailParameter;
	/**The suspend fax action, fax job ID parameter name*/
	protected String suspendFaxJobIDParameter;
	/**The resume fax action, fax job ID parameter name*/
	protected String resumeFaxJobIDParameter;
	/**The cancel fax action, fax job ID parameter name*/
	protected String cancelFaxJobIDParameter;
	/**The get fax job status fax action, fax job ID parameter name*/
	protected String getStatusFaxJobIDParameter;
	/**Additional parameters*/
	protected Map<String,String> additionalParameters;
	/**True to add the file name as an additional part*/
	protected boolean addFileNamePart;

	/**
	 * This class holds the configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.42.4
	 */
	public enum FaxJob2HTTPRequestConverterConfigurationConstants
	{
		/**The file content parameter name for the submit action property key*/
		SUBMIT_ACTION_FILE_CONTENT_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.submit.file.content.parameter"),
		/**The file name parameter name for the submit action property key*/
		SUBMIT_ACTION_FILE_NAME_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.submit.file.name.parameter"),
		/**The target address parameter name for the submit action property key*/
		SUBMIT_ACTION_TARGET_ADDRESS_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.submit.target.address.parameter"),
		/**The target name parameter name for the submit action property key*/
		SUBMIT_ACTION_TARGET_NAME_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.submit.target.name.parameter"),
		/**The sender name parameter name for the submit action property key*/
		SUBMIT_ACTION_SENDER_NAME_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.submit.sender.name.parameter"),
		/**The sender fax number parameter name for the submit action property key*/
		SUBMIT_ACTION_SENDER_FAX_NUMBER_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.submit.sender.fax.number.parameter"),
		/**The sender email parameter name for the submit action property key*/
		SUBMIT_ACTION_SENDER_EMAIL_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.submit.sender.email.parameter"),
		/**The fax job ID parameter name for the suspend action property key*/
		SUSPEND_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.suspend.fax.job.id.parameter"),
		/**The fax job ID parameter name for the resume action property key*/
		RESUME_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.resume.fax.job.id.parameter"),
		/**The fax job ID parameter name for the cancel action property key*/
		CANCEL_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.cancel.fax.job.id.parameter"),
		/**The fax job ID parameter name for the get fax job status action property key*/
		GET_FAX_JOB_STATUS_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.get.status.fax.job.id.parameter"),
		/**The additional parameter property key prefix*/
		ADDITIONAL_PARAMETER_PROPERTY_KEY_PREFIX("org.fax4j.spi.{0}.multi.part.parameter.key."),
		/**The add the file name as a separate part property key*/
		ADD_FILE_NAME_AS_PART_PROPERTY_KEY("org.fax4j.spi.{0}.multi.part.add.file.name.as.part"),
		/**The add the file name as a separate part property key*/
		IGNORE_PARAMETER_KEY("_IGNORE_");

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
	public MultiPartFaxJob2HTTPRequestConverter()
	{
		super(Fax4JProvider.FAX4J_PROVIDER);
	}

	/**
	 * This function initializes the component.
	 */
	@Override
	protected void initializeImpl()
	{
		this.submitFileContentParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_FILE_CONTENT_PARAMETER_NAME_PROPERTY_KEY);
		if(this.submitFileContentParameter==null)
		{
			this.submitFileContentParameter="file";
		}
		this.submitFileNameParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_FILE_NAME_PARAMETER_NAME_PROPERTY_KEY);
		if(this.submitFileNameParameter==null)
		{
			this.submitFileNameParameter="filename";
		}
		this.submitTargetAddressParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_TARGET_ADDRESS_PARAMETER_NAME_PROPERTY_KEY);
		if(this.submitTargetAddressParameter==null)
		{
			this.submitTargetAddressParameter="targetaddress";
		}
		this.submitTargetNameParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_TARGET_NAME_PARAMETER_NAME_PROPERTY_KEY);
		if(this.submitTargetNameParameter==null)
		{
			this.submitTargetNameParameter="targetname";
		}
		this.submitSenderNameParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_SENDER_NAME_PARAMETER_NAME_PROPERTY_KEY);
		if(this.submitSenderNameParameter==null)
		{
			this.submitSenderNameParameter="sendername";
		}
		this.submitSenderFaxNumberParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_SENDER_FAX_NUMBER_PARAMETER_NAME_PROPERTY_KEY);
		if(this.submitSenderFaxNumberParameter==null)
		{
			this.submitSenderFaxNumberParameter="senderfaxnumber";
		}
		this.submitSenderEMailParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_ACTION_SENDER_EMAIL_PARAMETER_NAME_PROPERTY_KEY);
		if(this.submitSenderEMailParameter==null)
		{
			this.submitSenderEMailParameter="senderemail";
		}
		this.suspendFaxJobIDParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.SUSPEND_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY);
		if(this.suspendFaxJobIDParameter==null)
		{
			this.suspendFaxJobIDParameter="faxjobid";
		}
		this.resumeFaxJobIDParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.RESUME_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY);
		if(this.resumeFaxJobIDParameter==null)
		{
			this.resumeFaxJobIDParameter="faxjobid";
		}
		this.cancelFaxJobIDParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.CANCEL_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY);
		if(this.cancelFaxJobIDParameter==null)
		{
			this.cancelFaxJobIDParameter="faxjobid";
		}
		this.getStatusFaxJobIDParameter=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.GET_FAX_JOB_STATUS_ACTION_FAX_JOB_ID_PARAMETER_NAME_PROPERTY_KEY);
		if(this.getStatusFaxJobIDParameter==null)
		{
			this.getStatusFaxJobIDParameter="faxjobid";
		}
		this.addFileNamePart=true;
		String value=this.getConfigurationValue(FaxJob2HTTPRequestConverterConfigurationConstants.ADD_FILE_NAME_AS_PART_PROPERTY_KEY);
		if(value!=null)
		{
			this.addFileNamePart=Boolean.parseBoolean(value);
		}
		
		//initialize additional parameters
		this.additionalParameters=this.initializeAdditionalParameters();
		if(this.additionalParameters!=null)
		{
			//normalize for best performance
			this.additionalParameters=new HashMap<String,String>(this.additionalParameters);
			
			//protect from modification
			this.additionalParameters=Collections.unmodifiableMap(this.additionalParameters);
		}
	}
	
	/**
	 * This function builds and returns the additional parameters map.
	 * 
	 * @return	The additional parameters
	 */
	protected Map<String,String> initializeAdditionalParameters()
	{
		//get configuration
		Map<String,String> configuration=this.getConfiguration();

		//get property part
		String propertyPart=this.getPropertyPart();
		
		//get prefix
		String prefix=FaxJob2HTTPRequestConverterConfigurationConstants.ADDITIONAL_PARAMETER_PROPERTY_KEY_PREFIX.toString();
		prefix=MessageFormat.format(prefix,propertyPart);

		//init output
		Map<String,String> additionalParametersMap=new HashMap<String,String>();
		
		Iterator<Entry<String,String>> iterator=configuration.entrySet().iterator();
		Entry<String,String> entry=null;
		String key=null;
		String value=null;
		while(iterator.hasNext())
		{
			//get next entry
			entry=iterator.next();
			
			//get next key
			key=entry.getKey();
			
			if(key!=null)
			{
				//format key
				key=MessageFormat.format(key,propertyPart);
				key=key.trim();
				
				if(key.length()>0)
				{
					//check if additional parameter key
					if(key.startsWith(prefix))
					{
						//get value
						value=entry.getValue();
						
						if(value!=null)
						{
							value=value.trim();
							if(value.length()>0)
							{
								//get updated key without prefix
								key=key.substring(prefix.length());
								
								//put in map
								additionalParametersMap.put(key,value);
							}
						}
					}
				}
			}
		}
		
		return additionalParametersMap;
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
		//setup common request data
		HTTPRequest httpRequest=this.createCommonHTTPRequest(faxClientSpi,faxActionType);
		
		//create content
		List<ContentPart<?>> contentList=new LinkedList<HTTPRequest.ContentPart<?>>();
		switch(faxActionType)
		{
			case SUBMIT_FAX_JOB:
				String value=faxJob.getTargetAddress();
				this.addContentPart(contentList,this.submitTargetAddressParameter,value);
				value=faxJob.getTargetName();
				this.addContentPart(contentList,this.submitTargetNameParameter,value);
				value=faxJob.getSenderName();
				this.addContentPart(contentList,this.submitSenderNameParameter,value);
				value=faxJob.getSenderFaxNumber();
				this.addContentPart(contentList,this.submitSenderFaxNumberParameter,value);
				value=faxJob.getSenderEmail();
				this.addContentPart(contentList,this.submitSenderEMailParameter,value);
				File file=faxJob.getFile();
				contentList.add(new ContentPart<File>(this.submitFileContentParameter,file,ContentPartType.FILE));
				if(this.shouldAddFileNamePart())
				{
					value=file.getName();
					this.addContentPart(contentList,this.submitFileNameParameter,value);
				}
				break;
			case SUSPEND_FAX_JOB:
				this.addContentPart(contentList,this.suspendFaxJobIDParameter,faxJob.getID());
				break;
			case RESUME_FAX_JOB:
				this.addContentPart(contentList,this.resumeFaxJobIDParameter,faxJob.getID());
				break;
			case CANCEL_FAX_JOB:
				this.addContentPart(contentList,this.cancelFaxJobIDParameter,faxJob.getID());
				break;
			case GET_FAX_JOB_STATUS:
				this.addContentPart(contentList,this.getStatusFaxJobIDParameter,faxJob.getID());
				break;
			default:
				throw new FaxException("Fax action type: "+faxActionType+" not supported.");
		}
		
		//add additional parameters
		this.addAdditionalParameters(contentList);
		
		//add additional parts
		this.addAdditionalContentParts(faxClientSpi,faxActionType,faxJob,contentList);
		
		//convert to array
		ContentPart<?>[] content=contentList.toArray(new ContentPart<?>[contentList.size()]);
		
		//set content
		httpRequest.setContent(content);

		return httpRequest;
	}
	
	/**
	 * This function returns true if the converter should add a file name content part.
	 * 
	 * @return	True if to add a file name content part
	 */
	protected boolean shouldAddFileNamePart()
	{
		return this.addFileNamePart;
	}

	/**
	 * Creates a HTTP request with the common data.
	 *  
	 * @param	faxClientSpi
	 * 			The HTTP fax client SPI
	 * @param	faxActionType
	 * 			The fax action type
	 * @return 	The HTTP request to send
	 */
	protected HTTPRequest createCommonHTTPRequest(HTTPFaxClientSpi faxClientSpi,FaxActionType faxActionType)
	{
		//setup common request data
		HTTPRequest httpRequest=new HTTPRequest();
		String resource=faxClientSpi.getHTTPResource(faxActionType);
		httpRequest.setResource(resource);
		String urlParameters=faxClientSpi.getHTTPURLParameters();
		httpRequest.setParametersText(urlParameters);

		return httpRequest;
	}
	
	/**
	 * This function adds a string content part
	 * 
	 * @param	contentList
	 * 			The content list with all the created parts
	 * @param 	key
	 * 			The parameter key
	 * @param 	value
	 * 			The parameter value
	 */
	protected final void addContentPart(List<ContentPart<?>> contentList,String key,String value)
	{
		if((key!=null)&&(value!=null)&&(value.length()>0))
		{
			if(this.shouldAddContentPart(key))
			{
				contentList.add(new ContentPart<String>(key,value,ContentPartType.STRING));
			}
		}
	}
	
	/**
	 * This function enables to filter out parameters from the HTTP request.<br>
	 * Default implementation uses the default IGNORE_PARAMETER_KEY enum value.
	 * 
	 * @param 	key
	 * 			The parameter key
	 * @return	True to add, false to filter out
	 */
	protected boolean shouldAddContentPart(String key)
	{
		boolean addPart=true;
		if(key.equals(FaxJob2HTTPRequestConverterConfigurationConstants.IGNORE_PARAMETER_KEY.toString()))
		{
			addPart=false;
		}
		
		return addPart;
	}

	/**
	 * This function adds the additional parameters.
	 * 
	 * @param	contentList
	 * 			The content list with all the created parts
	 */
	protected final void addAdditionalParameters(List<ContentPart<?>> contentList)
	{
		//add additional parameters
		if(this.additionalParameters!=null)
		{
			Iterator<Entry<String,String>> iterator=this.additionalParameters.entrySet().iterator();
			Entry<String,String> entry=null;
			String key=null;
			String value=null;
			while(iterator.hasNext())
			{
				//get next entry
				entry=iterator.next();
				
				//get next key/value
				key=entry.getKey();
				value=entry.getValue();
				
				if((key!=null)&&(value!=null))
				{
					this.addContentPart(contentList,key,value);
				}
			}
		}
	}
	
	/**
	 * This function enables extending classes to add additional content parts.
	 * 
	 * @param	faxClientSpi
	 * 			The HTTP fax client SPI
	 * @param	faxActionType
	 * 			The fax action type
	 * @param 	faxJob
	 * 			The fax job object
	 * @param	contentList
	 * 			The content list with all the created parts
	 */
	protected void addAdditionalContentParts(HTTPFaxClientSpi faxClientSpi,FaxActionType faxActionType,FaxJob faxJob,List<ContentPart<?>> contentList)
	{
		//empty hook for extending classes
	}
}