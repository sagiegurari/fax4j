package org.fax4j.spi.http;

import org.fax4j.FaxException;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides a JSON based implementation of the HTTP response handler interface.<br>
 * This class expects to get JSON response data and will update the fax job as needed.<br>
 * This class only supports extracting the fax job ID and fax job status from the JSON response object,
 * or in case of errors, extract the error message.
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
 * 		<td>org.fax4j.spi.http.response.data.encoding</td>
 * 		<td>The response data encoding, if encoding is not defined, the default system encoding is used.</td>
 * 		<td>none</td>
 * 		<td>The default system encoding</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.submit.json.output.path</td>
 * 		<td>The submit JSON output path used to locate the fax job ID from the submit fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.suspend.json.output.path</td>
 * 		<td>The suspend JSON output path used to locate the fax job ID from the suspend fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.resume.json.output.path</td>
 * 		<td>The resume JSON output path used to locate the fax job ID from the resume fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.cancel.json.output.path</td>
 * 		<td>The cancel JSON output path used to locate the fax job ID from the cancel fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.get.status.json.output.path</td>
 * 		<td>The get fax job status JSON output path used to locate the fax job status string value from the response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.json.pending.status.mappings</td>
 * 		<td>The mapping between the output fax status string value to the fax4j FaxJobStatus.PENDING enum value (format is: value1;value2;value3).</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.json.inprogress.status.mappings</td>
 * 		<td>The mapping between the output fax status string value to the fax4j FaxJobStatus.IN_PROGRESS enum value (format is: value1;value2;value3).</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.json.error.status.mappings</td>
 * 		<td>The mapping between the output fax status string value to the fax4j FaxJobStatus.ERROR enum value (format is: value1;value2;value3).</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.json.error.detection.path</td>
 * 		<td>The error detection path. If defined the handler will search for values in the defined path.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.json.error.detection.value</td>
 * 		<td>If detection path and value are defined, the value in the path is searched within the configured value. If value is not defined, all values are considered as errors.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.json.error.message.path</td>
 * 		<td>The path to the error message if errors are detected. If this property is not defined, the value found at org.fax4j.spi.http.json.error.detection.path will be used instead.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.04
 * @since	0.42.5
 */
public class JSONHTTPResponseHandler extends AbstractMappingHTTPResponseHandler<JSONObject>
{
	/**
	 * This class holds the configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.42.6
	 */
	public enum JSONHTTPResponseHandlerConfigurationConstants
	{
		/**The submit JSON output path property key*/
		SUBMIT_JSON_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.submit.json.output.path"),
		/**The suspend JSON output path property key*/
		SUSPEND_JSON_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.suspend.json.output.path"),
		/**The resume JSON output path property key*/
		RESUME_JSON_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.resume.json.output.path"),
		/**The cancel JSON output path property key*/
		CANCEL_JSON_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.cancel.json.output.path"),
		/**The get fax job status JSON output path property key*/
		GET_STATUS_JSON_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.get.status.json.output.path"),
		/**The FaxJobStatus.PENDING status mapping property key*/
		PENDING_STATUS_MAPPING_PROPERTY_KEY("org.fax4j.spi.{0}.json.pending.status.mappings"),
		/**The FaxJobStatus.IN_PROGRESS status mapping property key*/
		IN_PROGRESS_STATUS_MAPPING_PROPERTY_KEY("org.fax4j.spi.{0}.json.inprogress.status.mappings"),
		/**The FaxJobStatus.ERROR status mapping property key*/
		ERROR_STATUS_MAPPING_PROPERTY_KEY("org.fax4j.spi.{0}.json.error.status.mappings"),
		/**The error detection path property key*/
		ERROR_DETECTION_PATH_PROPERTY_KEY("org.fax4j.spi.{0}.json.error.detection.path"),
		/**The error detection value property key*/
		ERROR_DETECTION_VALUE_PROPERTY_KEY("org.fax4j.spi.{0}.json.error.detection.value"),
		/**The error message path property key*/
		ERROR_MESSAGE_PATH_PROPERTY_KEY("org.fax4j.spi.{0}.json.error.message.path");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private JSONHTTPResponseHandlerConfigurationConstants(String value)
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
	public JSONHTTPResponseHandler()
	{
		super();
	}

	/**
	 * This function populates the fax action type to response data path mappings
	 * based on the configuration of the handler.
	 */
	@Override
	protected void populateFaxActionType2ReponseDataPathMappings()
	{
		//populate map
		FaxActionType[] faxActionTypes=new FaxActionType[]{FaxActionType.SUBMIT_FAX_JOB,
														   FaxActionType.SUSPEND_FAX_JOB,
														   FaxActionType.RESUME_FAX_JOB,
														   FaxActionType.CANCEL_FAX_JOB,
														   FaxActionType.GET_FAX_JOB_STATUS};
		Enum<?>[] configurationKeys=new Enum<?>[]{JSONHTTPResponseHandlerConfigurationConstants.SUBMIT_JSON_OUTPUT_PROPERTY_KEY,
									 JSONHTTPResponseHandlerConfigurationConstants.SUSPEND_JSON_OUTPUT_PROPERTY_KEY,
									 JSONHTTPResponseHandlerConfigurationConstants.RESUME_JSON_OUTPUT_PROPERTY_KEY,
									 JSONHTTPResponseHandlerConfigurationConstants.CANCEL_JSON_OUTPUT_PROPERTY_KEY,
									 JSONHTTPResponseHandlerConfigurationConstants.GET_STATUS_JSON_OUTPUT_PROPERTY_KEY};
		this.populateFaxActionType2ReponseDataPathMappings(faxActionTypes,configurationKeys);
	}
	
	/**
	 * This function populates the status strings to fax status enum values
	 * based on the configuration of the handler.
	 */
	@Override
	protected void populateStatusString2FaxStatusMappings()
	{
		//populate map
		FaxJobStatus[] faxJobStatuses=new FaxJobStatus[]{FaxJobStatus.PENDING,FaxJobStatus.IN_PROGRESS,FaxJobStatus.ERROR};
		Enum<?>[] configurationKeys=new Enum<?>[]{JSONHTTPResponseHandlerConfigurationConstants.PENDING_STATUS_MAPPING_PROPERTY_KEY,
									 JSONHTTPResponseHandlerConfigurationConstants.IN_PROGRESS_STATUS_MAPPING_PROPERTY_KEY,
									 JSONHTTPResponseHandlerConfigurationConstants.ERROR_STATUS_MAPPING_PROPERTY_KEY};
		this.populateStatusString2FaxStatusMappings(faxJobStatuses,configurationKeys);
	}

	/**
	 * Returns the error detection path configuration key.
	 * 
	 * @return	The error detection path configuration key
	 */
	@Override
	protected Enum<?> getErrorDetectionPathPropertyKey()
	{
		return JSONHTTPResponseHandlerConfigurationConstants.ERROR_DETECTION_PATH_PROPERTY_KEY;
	}

	/**
	 * Returns the error detection value configuration key.
	 * 
	 * @return	The error detection value configuration key
	 */
	@Override
	protected Enum<?> getErrorDetectionValuePropertyKey()
	{
		return JSONHTTPResponseHandlerConfigurationConstants.ERROR_DETECTION_VALUE_PROPERTY_KEY;
	}

	/**
	 * Returns the error message path configuration key.
	 * 
	 * @return	The error message path configuration key
	 */
	@Override
	protected Enum<?> getErrorMessagePathPropertyKey()
	{
		return JSONHTTPResponseHandlerConfigurationConstants.ERROR_MESSAGE_PATH_PROPERTY_KEY;
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
	@Override
	protected String findValueImpl(JSONObject object,String path)
	{
		//split path to parts
		String[] pathParts=path.split(AbstractMappingHTTPResponseHandler.VALUES_SEPERATOR);
		int pathPartsAmount=pathParts.length;
		String pathPart=null;
		JSONObject currentJSONObject=object;
		String value=null;
		for(int index=0;index<pathPartsAmount;index++)
		{
			//get next path part
			pathPart=pathParts[index];
			
			if(pathPart.length()>0)
			{
				if(currentJSONObject.isNull(pathPart))
				{
					break;
				}

				try
				{
					//if last element
					if((index+1)==pathPartsAmount)
					{
						value=currentJSONObject.getString(pathPart);
					}
					else
					{
						//get sub object
						currentJSONObject=currentJSONObject.getJSONObject(pathPart);
						if(currentJSONObject==null)
						{
							break;
						}
					}
				}
				catch(JSONException exception)
				{
					throw new FaxException("Unable to extract JSON data for path: "+path,exception);
				}
			}
		}
		
		return value;
	}

	/**
	 * This function converts the HTTP response content to the specific object.
	 * 
	 * @param	httpResponse
	 * 			The HTTP response
	 * @return	The object
	 */
	@Override
	protected JSONObject convertToObject(HTTPResponse httpResponse)
	{
		//get response text
		JSONObject jsonObject=null;
		String content=httpResponse.getContent();
		if(content!=null)
		{
			try
			{
				//convert to JSON
				jsonObject=new JSONObject(content);
			}
			catch(JSONException exception)
			{
				throw new FaxException("Unable to parse HTTP response text as JSON.",exception);
			}
		}
		
		return jsonObject;
	}
}