package org.fax4j.bridge.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.bridge.AbstractRequestParser;
import org.fax4j.bridge.FileInfo;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.ConfigurationHolderImpl;
import org.fax4j.spi.http.HTTPRequest;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.spi.http.HTTPRequest.ContentPartType;
import org.fax4j.spi.http.HTTPRequest.ContentType;
import org.fax4j.util.IOHelper;

/**
 * This class provides a multi part implementation of the HTTP request parser
 * used to extract the fax job and file info from the HTTP request.
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
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.file.content.parameter</td>
 * 		<td>The file content parameter name.</td>
 * 		<td>file</td>
 * 		<td>file</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.file.name.parameter</td>
 * 		<td>The file name parameter name.</td>
 * 		<td>filename</td>
 * 		<td>filename</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.priority.parameter</td>
 * 		<td>The priority parameter name.</td>
 * 		<td>priority</td>
 * 		<td>priority</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.target.address.parameter</td>
 * 		<td>The target address parameter name.</td>
 * 		<td>targetaddress</td>
 * 		<td>targetaddress</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.target.name.parameter</td>
 * 		<td>The target name parameter name.</td>
 * 		<td>targetname</td>
 * 		<td>targetname</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.sender.name.parameter</td>
 * 		<td>The sender name parameter name.</td>
 * 		<td>sendername</td>
 * 		<td>sendername</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.sender.fax.number.parameter</td>
 * 		<td>The sender fax number parameter name.</td>
 * 		<td>senderfaxnumber</td>
 * 		<td>senderfaxnumber</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.http.request.parser.multi.part.sender.email.parameter</td>
 * 		<td>The sender email parameter name.</td>
 * 		<td>senderemail</td>
 * 		<td>senderemail</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.7
 */
public class MultiPartHTTPRequestParser extends AbstractRequestParser<HTTPRequest> implements HTTPRequestParser
{
	/**The file content parameter name*/
	protected String fileContentParameter;
	/**The file name parameter name*/
	protected String fileNameParameter;
	/**The priority parameter name*/
	protected String priorityParameter;
	/**The target address parameter name*/
	protected String targetAddressParameter;
	/**The target name parameter name*/
	protected String targetNameParameter;
	/**The sender name parameter name*/
	protected String senderNameParameter;
	/**The sender fax number parameter name*/
	protected String senderFaxNumberParameter;
	/**The sender email parameter name*/
	protected String senderEMailParameter;

	/**
	 * This class holds the configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.42.4
	 */
	public enum HTTPRequestParserConfigurationConstants
	{
		/**The file content parameter name property key*/
		FILE_CONTENT_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.file.content.parameter"),
		/**The file name parameter name property key*/
		FILE_NAME_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.file.name.parameter"),
		/**The priority parameter name property key*/
		PRIORITY_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.priority.parameter"),
		/**The target address parameter name property key*/
		TARGET_ADDRESS_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.target.address.parameter"),
		/**The target name parameter name property key*/
		TARGET_NAME_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.target.name.parameter"),
		/**The sender name parameter name property key*/
		SENDER_NAME_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.sender.name.parameter"),
		/**The sender fax number parameter name property key*/
		SENDER_FAX_NUMBER_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.sender.fax.number.parameter"),
		/**The sender email parameter name property key*/
		SENDER_EMAIL_PARAMETER_NAME_PROPERTY_KEY("org.fax4j.bridge.http.request.parser.multi.part.sender.email.parameter");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private HTTPRequestParserConfigurationConstants(String value)
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
	public MultiPartHTTPRequestParser()
	{
		super();
	}

	/**
	 * This function initializes the component.
	 * 
	 * @param	configuration
	 * 			The component configuration
	 */
	@Override
	protected void initializeImpl(Map<String,String> configuration)
	{
		//create configuration holder
		ConfigurationHolder configurationHolder=new ConfigurationHolderImpl(configuration);

		this.fileContentParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.FILE_CONTENT_PARAMETER_NAME_PROPERTY_KEY);
		if(this.fileContentParameter==null)
		{
			this.fileContentParameter="file";
		}
		this.fileNameParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.FILE_NAME_PARAMETER_NAME_PROPERTY_KEY);
		if(this.fileNameParameter==null)
		{
			this.fileNameParameter="filename";
		}
		this.priorityParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.PRIORITY_PARAMETER_NAME_PROPERTY_KEY);
		if(this.priorityParameter==null)
		{
			this.priorityParameter="priority";
		}
		this.targetAddressParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.TARGET_ADDRESS_PARAMETER_NAME_PROPERTY_KEY);
		if(this.targetAddressParameter==null)
		{
			this.targetAddressParameter="targetaddress";
		}
		this.targetNameParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.TARGET_NAME_PARAMETER_NAME_PROPERTY_KEY);
		if(this.targetNameParameter==null)
		{
			this.targetNameParameter="targetname";
		}
		this.senderNameParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.SENDER_NAME_PARAMETER_NAME_PROPERTY_KEY);
		if(this.senderNameParameter==null)
		{
			this.senderNameParameter="sendername";
		}
		this.senderFaxNumberParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.SENDER_FAX_NUMBER_PARAMETER_NAME_PROPERTY_KEY);
		if(this.senderFaxNumberParameter==null)
		{
			this.senderFaxNumberParameter="senderfaxnumber";
		}
		this.senderEMailParameter=configurationHolder.getConfigurationValue(HTTPRequestParserConfigurationConstants.SENDER_EMAIL_PARAMETER_NAME_PROPERTY_KEY);
		if(this.senderEMailParameter==null)
		{
			this.senderEMailParameter="senderemail";
		}
	}

	/**
	 * This function returns the file info from the request data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @return	The file info
	 */
	@Override
	protected FileInfo getFileInfoFromInputDataImpl(HTTPRequest inputData)
	{
		//get content
		Map<String,ContentPart<?>> contentPartsMap=this.getContentPartsAsMap(inputData);

		//get file info
		FileInfo fileInfo=null;
		ContentPart<?> contentPart=contentPartsMap.get(this.fileContentParameter);
		if(contentPart==null)
		{
			throw new FaxException("File info not provided.");
		}
		ContentPartType contentPartType=contentPart.getType();
		switch(contentPartType)
		{
			case FILE:
				File file=(File)contentPart.getContent();
				fileInfo=new FileInfo(file);
				break;
			case BINARY:
				byte[] data=(byte[])contentPart.getContent();

				//get file name
				String fileName=this.getContentPartAsString(contentPartsMap,this.fileNameParameter);

				fileInfo=new FileInfo(fileName,data);
				break;
			default:
				throw new FaxException("Unsupported content part type: "+contentPartType);
		}

		return fileInfo;
	}

	/**
	 * This function update the fax job from the request data.<br>
	 * This fax job will not have any file data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @param	faxJob
	 * 			The fax job to update
	 */
	@Override
	protected void updateFaxJobFromInputDataImpl(HTTPRequest inputData,FaxJob faxJob)
	{
		//get content
		Map<String,ContentPart<?>> contentPartsMap=this.getContentPartsAsMap(inputData);

		//set priority
		String value=this.getContentPartAsString(contentPartsMap,this.priorityParameter);
		if((value!=null)&&(value.length()>0))
		{
			FaxJobPriority priority=FaxJobPriority.valueOf(value);
			faxJob.setPriority(priority);
		}

		//set target address
		value=this.getContentPartAsString(contentPartsMap,this.targetAddressParameter);
		if((value==null)||(value.length()==0))
		{
			throw new FaxException("Target address not provided.");
		}
		faxJob.setTargetAddress(value);

		//set target address
		value=this.getContentPartAsString(contentPartsMap,this.targetNameParameter);
		if((value!=null)&&(value.length()>0))
		{
			faxJob.setTargetName(value);
		}

		//set target address
		value=this.getContentPartAsString(contentPartsMap,this.senderNameParameter);
		if((value!=null)&&(value.length()>0))
		{
			faxJob.setSenderName(value);
		}

		//set target address
		value=this.getContentPartAsString(contentPartsMap,this.senderFaxNumberParameter);
		if((value!=null)&&(value.length()>0))
		{
			faxJob.setSenderFaxNumber(value);
		}

		//set target address
		value=this.getContentPartAsString(contentPartsMap,this.senderEMailParameter);
		if((value!=null)&&(value.length()>0))
		{
			faxJob.setSenderEmail(value);
		}
	}
	
	/**
	 * This function returns the content part string value.
	 * 
	 * @param 	contentPartsMap
	 * 			The content parts map
	 * @param 	parameter
	 * 			The part parameter name
	 * @return	The part string value
	 */
	protected String getContentPartAsString(Map<String,ContentPart<?>> contentPartsMap,String parameter)
	{
		//get part
		String stringValue=null;
		ContentPart<?> contentPart=contentPartsMap.get(parameter);
		if(contentPart!=null)
		{
			ContentPartType contentPartType=contentPart.getType();
			Object content=contentPart.getContent();
			if(content!=null)
			{
				switch(contentPartType)
				{
					case STRING:
						stringValue=(String)content;
						break;
					case BINARY:
						byte[] data=(byte[])content;
						String encoding=IOHelper.getDefaultEncoding();
						try
						{
							stringValue=new String(data,encoding);
						}
						catch(UnsupportedEncodingException exception)
						{
							throw new FaxException("Unable to convert binary data to string for parameter: "+parameter,exception);
						}
						break;
					default:
						throw new FaxException("Unsupported content part type: "+contentPartType);
				}
			}
		}
		
		return stringValue;
	}
	
	/**
	 * This function returns the HTTP request multi parts as map.
	 * 
	 * @param 	httpRequest
	 * 			The HTTP request
	 * @return	The HTTP request multi parts as map
	 */
	protected Map<String,ContentPart<?>> getContentPartsAsMap(HTTPRequest httpRequest)
	{
		Map<String,ContentPart<?>> contentPartsMap=null;
		ContentType contentType=httpRequest.getContentType();
		switch(contentType)
		{
			case MULTI_PART:
				//init map
				contentPartsMap=new HashMap<String,HTTPRequest.ContentPart<?>>();

				//get parts
				ContentPart<?>[] contentParts=httpRequest.getContentAsParts();
				int amount=contentParts.length;
				ContentPart<?> contentPart=null;
				String partName=null;
				for(int index=0;index<amount;index++)
				{
					//get next part
					contentPart=contentParts[index];
					
					//get part name
					partName=contentPart.getName();
					
					//put in map
					contentPartsMap.put(partName,contentPart);
				}
				break;
			default:
				throw new FaxException("Unsupported content type: "+contentType);
		}
		
		return contentPartsMap;
	}
}