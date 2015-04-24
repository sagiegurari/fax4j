package org.fax4j.spi.http;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.common.FaxActionType;

/**
 * This class provides an abstract mapping based implementation of the HTTP response handler interface.<br>
 * This class expects to get a data object and will update the fax job as needed.<br>
 * This class only supports extracting the fax job ID and fax job status from the response object,
 * or in case of errors, extract the error message.
 * 
 * @param	<T>
 * 			The specific data object
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.42.8
 */
public abstract class AbstractMappingHTTPResponseHandler<T> extends AbstractHTTPResponseHandler
{
	/**The fax action type to object specific response data path mapping*/
	private Map<FaxActionType,String> faxActionType2ReponseDataPathMap;
	/**The status string value to fax status mapping*/
	private Map<String,FaxJobStatus> statusString2FaxStatusMap;
	/**The error detection path*/
	private String errorDetectionPath;
	/**The error detection value*/
	private String errorDetectionValue;
	/**The error message path*/
	private String errorMessagePath;
	/**The values seperator ';'*/
	public static final String VALUES_SEPERATOR=";";

	/**
	 * This is the default constructor.
	 */
	public AbstractMappingHTTPResponseHandler()
	{
		super(Fax4JProvider.FAX4J_PROVIDER);
	}

	/**
	 * This function initializes the component.
	 */
	@Override
	protected void initializeImpl()
	{
		super.initializeImpl();

		//init maps
		this.faxActionType2ReponseDataPathMap=new HashMap<FaxActionType,String>();
		this.statusString2FaxStatusMap=new HashMap<String,FaxJobStatus>();

		//populate maps
		this.populateFaxActionType2ReponseDataPathMappings();
		this.populateStatusString2FaxStatusMappings();

		//set error values
		Enum<?> configurationKey=this.getErrorDetectionPathPropertyKey();
		this.errorDetectionPath=this.getConfigurationValue(configurationKey);
		configurationKey=this.getErrorDetectionValuePropertyKey();
		this.errorDetectionValue=this.getConfigurationValue(configurationKey);
		configurationKey=this.getErrorMessagePathPropertyKey();
		this.errorMessagePath=this.getConfigurationValue(configurationKey);
	}
	
	/**
	 * This function populates the fax action type to response data path mappings
	 * based on the configuration of the handler.
	 * 
	 * @param	faxActionTypes
	 * 			The fax action types used for the mapping
	 * @param	configurationKeys
	 * 			The configuration keys used for the mapping
	 */
	protected void populateFaxActionType2ReponseDataPathMappings(FaxActionType[] faxActionTypes,Enum<?>[] configurationKeys)
	{
		//populate map
		String path=null;
		for(int index=0;index<faxActionTypes.length;index++)
		{
			//get next path
			path=this.getConfigurationValue(configurationKeys[index]);
			if(path!=null)
			{
				this.faxActionType2ReponseDataPathMap.put(faxActionTypes[index],path);
			}
		}
	}
	
	/**
	 * This function populates the status strings to fax status enum values
	 * based on the configuration of the handler.
	 * 
	 * @param	faxJobStatuses
	 * 			The fax job statues used for the mapping
	 * @param	configurationKeys
	 * 			The configuration keys used for the mapping
	 */
	protected void populateStatusString2FaxStatusMappings(FaxJobStatus[] faxJobStatuses,Enum<?>[] configurationKeys)
	{
		//populate map
		FaxJobStatus faxJobStatus=null;
		String valuesStr=null;
		String[] values=null;
		String value=null;
		for(int index=0;index<configurationKeys.length;index++)
		{
			//get next values
			valuesStr=this.getConfigurationValue(configurationKeys[index]);
			if(valuesStr!=null)
			{
				//get next status
				faxJobStatus=faxJobStatuses[index];

				//split
				values=valuesStr.split(AbstractMappingHTTPResponseHandler.VALUES_SEPERATOR);
				for(int valueIndex=0;valueIndex<values.length;valueIndex++)
				{
					//get next value
					value=values[valueIndex];
					
					value=value.trim();
					if(value.length()>0)
					{
						this.statusString2FaxStatusMap.put(value,faxJobStatus);
					}
				}
			}
		}
	}

	/**
	 * This function returns the path to the relevant response data.
	 * 
	 * @param 	faxActionType
	 * 			The fax action type used to select the path
	 * @return	The path string value
	 */
	protected String getPathToResponseData(FaxActionType faxActionType)
	{
		return this.faxActionType2ReponseDataPathMap.get(faxActionType);
	}

	/**
	 * This function returns the fax job status value based on the
	 * fax job status string value.
	 * 
	 * @param 	faxJobStatusStr
	 * 			The fax job status string value
	 * @return	The fax job status
	 */
	protected FaxJobStatus getFaxJobStatusFromStatusString(String faxJobStatusStr)
	{
		return this.statusString2FaxStatusMap.get(faxJobStatusStr);
	}
	
	/**
	 * Updates the fax job based on the data from the HTTP response data.
	 *  
	 * @param 	faxJob
	 * 			The fax job object
	 * @param	httpResponse
	 * 			The HTTP response
	 * @param	faxActionType
	 * 			The fax action type
	 */
	public void updateFaxJob(FaxJob faxJob,HTTPResponse httpResponse,FaxActionType faxActionType)
	{
		//get path
		String path=this.getPathToResponseData(faxActionType);

		//get fax job ID
		String id=this.findValue(httpResponse,path);
		
		if(id!=null)
		{
			faxJob.setID(id);
		}
	}
	
	/**
	 * This function extracts the fax job status from the HTTP response data.
	 * 
	 * @param	httpResponse
	 * 			The HTTP response
	 * @return	The fax job status
	 */
	public FaxJobStatus getFaxJobStatus(HTTPResponse httpResponse)
	{
		//get path
		String path=this.getPathToResponseData(FaxActionType.GET_FAX_JOB_STATUS);

		//get fax job status string
		String faxJobStatusStr=this.findValue(httpResponse,path);
		
		FaxJobStatus faxJobStatus=FaxJobStatus.UNKNOWN;
		if(faxJobStatusStr!=null)
		{
			faxJobStatus=this.getFaxJobStatusFromStatusString(faxJobStatusStr);
			if(faxJobStatus==null)
			{
				faxJobStatus=FaxJobStatus.UNKNOWN;
			}
		}
		
		return faxJobStatus;
	}

	/**
	 * This function returns the requested value from the HTTP response content.<br>
	 * The path is a set of key names seperated by ';'.
	 * 
	 * @param	httpResponse
	 * 			The HTTP response
	 * @param 	path
	 * 			The path to the value (elements seperated by ;)
	 * @return	The value (null if not found)
	 */
	protected String findValue(HTTPResponse httpResponse,String path)
	{
		String value=null;
		if((path!=null)&&(httpResponse!=null))
		{
			//parse output
			T object=this.convertToObject(httpResponse);

			if(object!=null)
			{
				//look for an error
				String errorMessage=this.findError(object);
				if(errorMessage!=null)
				{
					throw new FaxException("Error found in response: "+errorMessage);
				}
	
				//find value
				value=this.findValue(object,path);
			}
		}
		
		return value;
	}

	/**
	 * This function returns the requested value from the object data.<br>
	 * The path is a set of key names seperated by ';'.
	 * 
	 * @param	object
	 * 			The object holding all the data
	 * @param 	path
	 * 			The path to the value (elements seperated by ;)
	 * @return	The value (null if not found)
	 */
	protected String findValue(T object,String path)
	{
		String value=null;
		if((path!=null)&&(object!=null))
		{
			//find value
			value=this.findValueImpl(object,path);

			if(value!=null)
			{
				value=value.trim();
				if(value.length()==0)
				{
					value=null;
				}
			}
		}
		
		return value;
	}
	
	/**
	 * This function will look for errors in the object and if found
	 * will return the error message.<br>
	 * If no errors were found, this method will return null.
	 * 
	 * @param	object
	 * 			The object holding all the data
	 * @return	The error message (if found) or null
	 */
	protected String findError(T object)
	{
		String errorMessage=null;
		if(this.errorDetectionPath!=null)
		{
			String value=this.findValue(object,this.errorDetectionPath);
			if(value!=null)
			{
				boolean errorFound=false;
				if(this.errorDetectionValue==null)
				{
					errorFound=true;
				}
				else if(this.errorDetectionValue.indexOf(value)!=-1)	//look for value in configured value
				{
					errorFound=true;
				}
				
				if(errorFound)
				{
					errorMessage=value;
					if(this.errorMessagePath!=null)
					{
						value=this.findValue(object,this.errorMessagePath);
						if(value!=null)
						{
							errorMessage=value;
						}
					}
				}
			}
		}
		
		return errorMessage;
	}
	
	/**
	 * This function populates the fax action type to response data path mappings
	 * based on the configuration of the handler.
	 */
	protected abstract void populateFaxActionType2ReponseDataPathMappings();
	
	/**
	 * This function populates the status strings to fax status enum values
	 * based on the configuration of the handler.
	 */
	protected abstract void populateStatusString2FaxStatusMappings();

	/**
	 * Returns the error detection path configuration key.
	 * 
	 * @return	The error detection path configuration key
	 */
	protected abstract Enum<?> getErrorDetectionPathPropertyKey();

	/**
	 * Returns the error detection value configuration key.
	 * 
	 * @return	The error detection value configuration key
	 */
	protected abstract Enum<?> getErrorDetectionValuePropertyKey();

	/**
	 * Returns the error message path configuration key.
	 * 
	 * @return	The error message path configuration key
	 */
	protected abstract Enum<?> getErrorMessagePathPropertyKey();
	
	/**
	 * This function returns the requested value from the object data.<br>
	 * The path is a set of key names seperated by ';'.
	 * 
	 * @param	object
	 * 			The object holding all the data
	 * @param 	path
	 * 			The path to the value (elements seperated by ;)
	 * @return	The value (null if not found)
	 */
	protected abstract String findValueImpl(T object,String path);
	
	/**
	 * This function converts the HTTP response content to the specific object.
	 * 
	 * @param	httpResponse
	 * 			The HTTP response
	 * @return	The object
	 */
	protected abstract T convertToObject(HTTPResponse httpResponse);
}