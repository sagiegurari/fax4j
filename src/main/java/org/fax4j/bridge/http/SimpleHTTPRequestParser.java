package org.fax4j.bridge.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.bridge.AbstractRequestParser;
import org.fax4j.bridge.FileInfo;
import org.fax4j.spi.http.HTTPRequest;
import org.fax4j.spi.http.HTTPRequest.ContentType;
import org.fax4j.util.IOHelper;
import org.fax4j.util.SpiUtil;

/**
 * This class provides a simple predefined implementation of the HTTP request parser
 * used to extract the fax job and file info from the HTTP request.<br>
 * This parser expects the HTTP request as follows:<br>
 * URL parameters:<br>
 * <ul>
 * 	<li>file [file name without path]
 * 	<li>priority [priority value as defined in the enum]
 * 	<li>target_address [target address]
 * 	<li>target_name [target name]
 * 	<li>sender_name [sender name]
 * 	<li>sender_fax_number [sender fax number]
 * 	<li>sender_email [sender email]
 * 	<li>property:[property name] [property value]
 * </ul>
 * The request Payload should hold the file content only.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.40.6
 */
public class SimpleHTTPRequestParser extends AbstractRequestParser<HTTPRequest> implements HTTPRequestParser
{
	/**
	 * This is the class constructor.
	 */
	public SimpleHTTPRequestParser()
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
		//empty
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
		//get parameters text as map
		Map<String,String> queryStringMap=this.convertParametersTextToMap(inputData);

		//get file info
		FileInfo fileInfo=this.getFileInfoFromRequestImpl(inputData,queryStringMap);
		
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
		//get parameters text as map
		Map<String,String> queryStringMap=this.convertParametersTextToMap(inputData);

		//update fax job
		this.updateFaxJobFromRequestImpl(inputData,faxJob,queryStringMap);
	}
	
	/**
	 * This function converts the provided query string to a map object.
	 * 
	 * @param	request
	 * 			The HTTP request
	 * @return	The query string broken to key/value
	 */
	protected Map<String,String> convertParametersTextToMap(HTTPRequest request)
	{
		//init data structure
		Map<String,String> map=new HashMap<String,String>();

		//get parameters text
		String parametersText=request.getParametersText();
		
		if(parametersText!=null)
		{
			//split to key/value pairs (key=value)
			String[] keyValuePairs=parametersText.split("&");
			
			//get amount
			int pairsAmount=keyValuePairs.length;
			
			String keyValuePair=null;
			String[] parts=null;
			String key=null;
			String value=null;
			for(int index=0;index<pairsAmount;index++)
			{
				//get next key/value pair
				keyValuePair=keyValuePairs[index];
				
				//split to key and value
				parts=keyValuePair.split("=");
				if(parts.length==2)
				{
					//get key
					key=parts[0];
					if(key.length()>0)
					{
						//decode key
						key=SpiUtil.urlDecode(key);

						//get value
						value=parts[1].trim();

						//decode value
						value=SpiUtil.urlDecode(value);
						
						//put in data structure
						map.put(key,value);
					}
				}
			}
		}
		
		return map;
	}

	/**
	 * This function returns the file info from the request data.
	 * 
	 * @param	request
	 * 			The HTTP request
	 * @param	queryStringMap
	 * 			The query string as key/value map
	 * @return	The file info
	 */
	protected FileInfo getFileInfoFromRequestImpl(HTTPRequest request,Map<String,String> queryStringMap)
	{
		//get file name
		String name=queryStringMap.get("file");
		if(name==null)
		{
			throw new FaxException("File name not provided in query string.");
		}
		
		//get content
		byte[] content=null;
		ContentType contentType=request.getContentType();
		switch(contentType)
		{
			case BINARY:
				content=request.getContentAsBinary();
				break;
			case STRING:
				String contentString=request.getContentAsString();
				content=IOHelper.convertStringToBinary(contentString,null);
				break;
			default:
				throw new FaxException("Unsupported content type: "+contentType);
		}
		if((content==null)||(content.length==0))
		{
			throw new FaxException("File content not provided in request payload.");
		}
		
		//create file info
		FileInfo fileInfo=new FileInfo(name,content);
		
		return fileInfo;
	}
	
	/**
	 * This function update the fax job from the request data.<br>
	 * This fax job will not have any file data.
	 * 
	 * @param	request
	 * 			The HTTP request
	 * @param	queryStringMap
	 * 			The query string as key/value map
	 * @param	faxJob
	 * 			The fax job to update
	 */
	protected void updateFaxJobFromRequestImpl(HTTPRequest request,FaxJob faxJob,Map<String,String> queryStringMap)
	{
		//set priority
		String value=queryStringMap.get("priority");
		if(value!=null)
		{
			FaxJobPriority priority=FaxJobPriority.valueOf(value);
			faxJob.setPriority(priority);
		}

		//set target address
		value=queryStringMap.get("target_address");
		if(value==null)
		{
			throw new FaxException("Target address not provided in query string.");
		}
		faxJob.setTargetAddress(value);

		//set target name
		value=queryStringMap.get("target_name");
		if(value!=null)
		{
			faxJob.setTargetName(value);
		}

		//set sender name
		value=queryStringMap.get("sender_name");
		if(value!=null)
		{
			faxJob.setSenderName(value);
		}

		//set sender fax number
		value=queryStringMap.get("sender_fax_number");
		if(value!=null)
		{
			faxJob.setSenderFaxNumber(value);
		}

		//set sender email
		value=queryStringMap.get("sender_email");
		if(value!=null)
		{
			faxJob.setSenderEmail(value);
		}
		
		Iterator<Entry<String,String>> iterator=queryStringMap.entrySet().iterator();
		Entry<String,String> entry=null;
		String key=null;
		String propertyPrefix="property:";
		int propertyPrefixLength=propertyPrefix.length();
		while(iterator.hasNext())
		{
			//get next entry
			entry=iterator.next();
			
			//get next key
			key=entry.getKey();

			if(key.startsWith(propertyPrefix))
			{
				if(key.length()>propertyPrefixLength)
				{
					//get key
					key=key.substring(propertyPrefixLength);
					
					//get value
					value=entry.getValue();
					
					//set property
					faxJob.setProperty(key,value);
				}
			}
		}
	}
}