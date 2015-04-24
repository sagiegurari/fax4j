package org.fax4j.spi.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.fax4j.FaxException;
import org.fax4j.common.Logger;
import org.fax4j.common.LoggerManager;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.spi.http.HTTPRequest.ContentPartType;
import org.fax4j.spi.http.HTTPRequest.ContentType;

/**
 * This HTTP client is based on the Apache HTTP client and is used
 * to submit HTTP requests.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.12
 * @since	0.1
 */
public class ApacheHTTPClient extends AbstractHTTPClient
{
	/**
	 * This is the default constructor.
	 */
	public ApacheHTTPClient()
	{
		super();
	}
	
	/**
	 * This function creates and returns a new HTTP client.
	 * 
	 * @return	The new HTTP client
	 */
	protected HttpClient createHttpClient()
	{
		//create HTTP client
		HttpClient httpClient=new HttpClient();
		
		return httpClient; 
	}

	/**
	 * This function creates and returns a new HTTP method.
	 * 
	 * @param	url
	 * 			The target URL
	 * @param 	httpMethod
	 * 			The HTTP method to use
	 * @return	The new HTTP method
	 */
	protected HttpMethodBase createMethod(String url,HTTPMethod httpMethod)
	{
		if(httpMethod==null)
		{
			throw new FaxException("HTTP method not provided.");
		}
		if((url==null)||(url.length()==0))
		{
			throw new FaxException("HTTP URL not provided.");
		}
		
		//create method
		HttpMethodBase httpMethodClient=null;
		switch(httpMethod)
		{
			case POST:
				httpMethodClient=new PostMethod(url);
				break;
			case GET:
				httpMethodClient=new GetMethod(url);
				break;
			case PUT:
				httpMethodClient=new PutMethod(url);
				break;
		}

		return httpMethodClient; 
	}
	
	/**
	 * This function creates and returns the HTTP response object.
	 * 
	 * @param 	statusCode
	 * 			The HTTP response status code
	 * @param 	responseContent
	 * 			The response content as string
	 * @return	The HTTP response object
	 */
	protected HTTPResponse createHTTPResponse(int statusCode,String responseContent)
	{
		//create HTTP response
		HTTPResponse httpResponse=new HTTPResponse();
		httpResponse.setStatusCode(statusCode);
		httpResponse.setContent(responseContent);

		return httpResponse;
	}
	
	/**
	 * This function appends the base URL (protocol, host, port and resource) to the
	 * provided buffer.
	 * 
	 * @param 	buffer
	 * 			The buffer to update
	 * @param 	resource
	 * 			The HTTP resource
	 * @param 	configuration
	 * 			The HTTP configuration
	 */
	protected void appendBaseURL(StringBuilder buffer,String resource,HTTPClientConfiguration configuration)
	{
		buffer.append("http");
		if(configuration.isSSL())
		{
			buffer.append("s");
		}
		buffer.append("://");
		buffer.append(configuration.getHostName());
		int port=configuration.getPort();
		if(port>0)
		{
			buffer.append(":");
			buffer.append(port);
		}
		if((resource!=null)&&(resource.length()>0))
		{
			if(!resource.startsWith("/"))
			{
				buffer.append("/");
			}
			buffer.append(resource);
		}
	}
	
	/**
	 * This function appends the parameters text to the base URL.
	 * 
	 * @param 	buffer
	 * 			The buffer to update
	 * @param 	parameters
	 * 			The parameters line
	 */
	protected void appendParameters(StringBuilder buffer,String parameters)
	{
		if((parameters!=null)&&(parameters.length()>0))
		{
			String updatedParameters=parameters;
			if(parameters.startsWith("?"))
			{
				updatedParameters=parameters.substring(1);
			}
			
			if(updatedParameters.length()>0)
			{
				int currentLength=buffer.length();
				if((currentLength>0)&&(buffer.charAt(currentLength-1)=='/'))
				{
					int length=currentLength;
					buffer.delete(length-1,length);
				}
				
				//add separator
				buffer.append("?");
	
				try
				{
					String[] parameterPairs=updatedParameters.split("&");
					int amount=parameterPairs.length;
					String parameterPair=null;
					String[] values=null;
					boolean addedParameters=false;
					for(int index=0;index<amount;index++)
					{
						//get next element
						parameterPair=parameterPairs[index];
						
						//split to key/value
						values=parameterPair.split("=");
						if(values.length==2)
						{
							if(addedParameters)
							{
								buffer.append("&");
							}
							
							buffer.append(URLEncoder.encode(values[0],"UTF-8"));
							buffer.append("=");
							buffer.append(URLEncoder.encode(values[1],"UTF-8"));
							
							//set flag
							addedParameters=true;
						}
					}
				}
				catch(Exception exception)
				{
					throw new FaxException("Unable to encode parameters.",exception);
				}
			}
		}
	}
	
	/**
	 * This function creates the full URL from the provided values.
	 * 
	 * @param 	httpRequest
	 * 			The HTTP request to send
	 * @param 	configuration
	 * 			HTTP client configuration
	 * @return	The full URL
	 */
	protected String createURL(HTTPRequest httpRequest,HTTPClientConfiguration configuration)
	{
		//init buffer
		StringBuilder buffer=new StringBuilder(100);

		//create URL
		String resource=httpRequest.getResource();
		this.appendBaseURL(buffer,resource,configuration);
		String parameters=httpRequest.getParametersText();
		this.appendParameters(buffer,parameters);
		String url=buffer.toString();
		
		return url;
	}
	
	/**
	 * This function sets the header properties in the HTTP method.
	 * 
	 * @param 	httpRequest
	 * 			The HTTP request
	 * @param 	httpMethodClient
	 * 			The apache HTTP method
	 */
	protected void setupHTTPRequestHeaderProperties(HTTPRequest httpRequest,HttpMethodBase httpMethodClient)
	{
		//setup header properties
		Properties headerProperties=httpRequest.getHeaderProperties();
		if(headerProperties!=null)
		{
			Iterator<Entry<Object,Object>> iterator=headerProperties.entrySet().iterator();
			Entry<Object,Object> entry=null;
			while(iterator.hasNext())
			{
				//get next entry
				entry=iterator.next();
				
				//set header values
				httpMethodClient.addRequestHeader((String)entry.getKey(),(String)entry.getValue());
			}
		}
	}

	/**
	 * This function creates a string type request entity and populates it 
	 * with the data from the provided HTTP request.
	 * 
	 * @param 	httpRequest
	 * 			The HTTP request
	 * @return	The request entity
	 */
	protected RequestEntity createStringRequestContent(HTTPRequest httpRequest)
	{
		RequestEntity requestEntity=null;
		String contentString=httpRequest.getContentAsString();
		if(contentString!=null)
		{
			try
			{
				requestEntity=new StringRequestEntity(contentString,"text/plain",null);
			}
			catch(UnsupportedEncodingException exception)
			{
				throw new FaxException("Unable to set string request entity.",exception);
			}
		}
		
		return requestEntity;
	}

	/**
	 * This function creates a binary type request entity and populates it 
	 * with the data from the provided HTTP request.
	 * 
	 * @param 	httpRequest
	 * 			The HTTP request
	 * @return	The request entity
	 */
	protected RequestEntity createBinaryRequestContent(HTTPRequest httpRequest)
	{
		RequestEntity requestEntity=null;
		byte[] contentBinary=httpRequest.getContentAsBinary();
		if(contentBinary!=null)
		{
			requestEntity=new ByteArrayRequestEntity(contentBinary,"binary/octet-stream");
		}
		
		return requestEntity;
	}

	/**
	 * This function creates a multi part type request entity and populates it 
	 * with the data from the provided HTTP request.
	 * 
	 * @param 	httpRequest
	 * 			The HTTP request
	 * @param 	httpMethodClient
	 * 			The apache HTTP method
	 * @return	The request entity
	 */
	protected RequestEntity createMultiPartRequestContent(HTTPRequest httpRequest,HttpMethodBase httpMethodClient)
	{
		RequestEntity requestEntity=null;
		ContentPart<?>[] contentParts=httpRequest.getContentAsParts();
		if(contentParts!=null)
		{
			int partsAmount=contentParts.length;
			
			if(partsAmount>0)
			{
				//init array
				Part[] parts=new Part[partsAmount];

				ContentPart<?> contentPart=null;
				String name=null;
				Object content=null;
				ContentPartType contentPartType=null;
				for(int index=0;index<partsAmount;index++)
				{
					//get next part
					contentPart=contentParts[index];

					if(contentPart!=null)
					{
						//get part values
						name=contentPart.getName();
						content=contentPart.getContent();
						contentPartType=contentPart.getType();
						
						//create new part
						switch(contentPartType)
						{
							case FILE:
								File file=(File)content;
								try
								{
									parts[index]=new FilePart(name,file);
								}
								catch(FileNotFoundException exception)
								{
									throw new FaxException("Fax file: "+file.getAbsolutePath()+" not found.",exception);
								}
								break;
							case STRING:
								parts[index]=new StringPart(name,(String)content);
								break;
							default:
								throw new FaxException("Unsupported content type provided: "+contentPartType);
						}
					}
				}
				requestEntity=new MultipartRequestEntity(parts,httpMethodClient.getParams());
			}
		}
		
		return requestEntity;
	}

	/**
	 * This function sets the request content.
	 * 
	 * @param 	httpRequest
	 * 			The HTTP request
	 * @param 	httpMethodClient
	 * 			The apache HTTP method
	 */
	protected void setRequestContent(HTTPRequest httpRequest,HttpMethodBase httpMethodClient)
	{
		//set content
		if(httpMethodClient instanceof EntityEnclosingMethod)
		{
			EntityEnclosingMethod pushMethod=(EntityEnclosingMethod)httpMethodClient;
			
			RequestEntity requestEntity=null;
			ContentType contentType=httpRequest.getContentType();
			switch(contentType)
			{
				case STRING:
					requestEntity=this.createStringRequestContent(httpRequest);
					break;
				case BINARY:
					requestEntity=this.createBinaryRequestContent(httpRequest);
					break;
				case MULTI_PART:
					requestEntity=this.createMultiPartRequestContent(httpRequest,httpMethodClient);
					break;
				default:
					throw new FaxException("Unsupported content type: "+contentType);
			}

			//set request data
			if(requestEntity!=null)
			{
				pushMethod.setRequestEntity(requestEntity);
				pushMethod.setContentChunked(false);
			}
		}
	}

	/**
	 * Submits the HTTP request and returns the HTTP response.
	 *  
	 * @param 	httpRequest
	 * 			The HTTP request to send
	 * @param 	httpClientConfiguration
	 * 			HTTP client configuration
	 * @param 	httpMethod
	 * 			The HTTP method to use
	 * @return	The HTTP response
	 */
	public HTTPResponse submitHTTPRequest(HTTPRequest httpRequest,HTTPClientConfiguration httpClientConfiguration,HTTPMethod httpMethod)
	{
		//create HTTP client
		HttpClient httpClient=this.createHttpClient();
		
		//create URL
		String url=this.createURL(httpRequest,httpClientConfiguration);
		
		//create request
		HttpMethodBase httpMethodClient=this.createMethod(url,httpMethod);
		
		//setup header properties
		this.setupHTTPRequestHeaderProperties(httpRequest,httpMethodClient);

		//set content
		this.setRequestContent(httpRequest,httpMethodClient);

        //get logger
		LoggerManager loggerManager=LoggerManager.getInstance();
        Logger logger=loggerManager.getLogger();
        
        try
		{
            logger.logDebug(new Object[]{"Submitting HTTP request: ",httpMethodClient.getURI()},null);
		}
		catch(URIException exception)
		{
            logger.logDebug(new Object[]{"Submitting HTTP request"},null);
		}

		String responseContent=null;
		int statusCode=-1;
		try
		{
			//submit HTTP request
			statusCode=httpClient.executeMethod(httpMethodClient);

			if(statusCode>=400)
			{
				throw new FaxException("Error while invoking HTTP request, return status code: "+statusCode);
			}
			
			//get response content
			responseContent=httpMethodClient.getResponseBodyAsString();
		}
		catch(FaxException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while executing HTTP request.",exception);
		}
		finally
		{
			//release connection
			httpMethodClient.releaseConnection();
		}

		//create HTTP response
		HTTPResponse httpResponse=this.createHTTPResponse(statusCode,responseContent);

		return httpResponse;
	}
}