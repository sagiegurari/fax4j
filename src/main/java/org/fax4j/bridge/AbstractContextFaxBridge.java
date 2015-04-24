package org.fax4j.bridge;

import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.util.ReflectionHelper;

/**
 * This provides almost complete implementation of the context fax bridge.<br>
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
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.vendor.policy.class.name</td>
 * 		<td>The vendor policy class name.</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @param	<T>
 * 			The context type
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.41.4
 */
public abstract class AbstractContextFaxBridge<T> extends FaxBridgeImpl implements ContextFaxBridge<T>
{
	/**The request parser*/
	private RequestParser<T> requestParser;

	/**
	 * This is the class constructor.
	 */
	public AbstractContextFaxBridge()
	{
		super();
	}

	/**
	 * This function initializes the fax bridge.
	 */
	@Override
	protected void initializeImpl()
	{
		//create parser
		this.requestParser=this.createRequestParser();
		if(this.requestParser==null)
		{
			throw new FaxException("Unable to create request parser.");
		}
	}

	/**
	 * This function will submit a new fax job.
	 * 
	 * @param	inputData
	 * 			The input data holding the fax job information
	 * @return	The submitted fax job
	 */
	public FaxJob submitFaxJob(T inputData)
	{
		if(inputData==null)
		{
			throw new FaxException("Input data not provided.");
		}
		
		//create fax job
		FaxJob faxJob=this.createFaxJob();
		
		//update fax job info
		this.requestParser.updateFaxJobFromInputData(inputData,faxJob);
		
		//create file info
		FileInfo fileInfo=this.requestParser.getFileInfoFromInputData(inputData);
		if(fileInfo==null)
		{
			throw new FaxException("Unable to extract file info from input data.");
		}
		
		//submit fax job
		this.submitFaxJob(faxJob,fileInfo);
		
		return faxJob;
	}
	
	/**
	 * This function creates, initializes and returns a new request parser.
	 * 
	 * @return	The new request parser
	 */
	protected RequestParser<T> createRequestParser()
	{
		//get parser configuration
		String configurationKey=this.getRequestParserConfigurationKey();
		String defaultParserClassName=this.getDefaultParserClassName();
		
		//create parser
		RequestParser<T> parser=this.createRequestParser(configurationKey,defaultParserClassName);
		
		return parser;
	}
	
	/**
	 * This function creates, initializes and returns a new request parser.
	 * 
	 * @param	configurationKey
	 * 			The configuration key used to fetch the parser class name
	 * @param	defaultParserClassName
	 * 			The default parser class name
	 * @return	The new request parser
	 */
	@SuppressWarnings("unchecked")
	protected RequestParser<T> createRequestParser(String configurationKey,String defaultParserClassName)
	{
		if(configurationKey==null)
		{
			throw new FaxException("Configuration key for request parser not provided.");
		}

		//get class name
		String className=this.getConfigurationValue(configurationKey);

		//if no class name configured, use default
		if(className==null)
		{
			className=defaultParserClassName;
			
			if(className==null)
			{
				throw new FaxException("Request parser class name not found.");
			}
		}

		//create new instance
		RequestParser<T> parser=(RequestParser<T>)ReflectionHelper.createInstance(className);
			
		//initialize
		Map<String,String> configuration=this.getConfiguration();
		parser.initialize(configuration);
		
		return parser;
	}
	
	/**
	 * This function returns the key used to fetch the request parser
	 * class name from the fax4j.properties.
	 * 
	 * @return	The request parser class name configuration key
	 */
	protected abstract String getRequestParserConfigurationKey();
	
	/**
	 * This function returns the default request parser class name.
	 * 
	 * @return	The default request parser class name
	 */
	protected abstract String getDefaultParserClassName();
}