package org.fax4j.spi.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.fax4j.FaxException;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.util.IOHelper;
import org.w3c.dom.Document;

/**
 * This class provides a XML based implementation of the HTTP response handler interface.<br>
 * This class expects to get XML response data and will update the fax job as needed.<br>
 * This class only supports extracting the fax job ID and fax job status from the XML response object,
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
 * 		<td>org.fax4j.spi.http.submit.xml.output.path</td>
 * 		<td>The submit XML output path used to locate the fax job ID from the submit fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.suspend.xml.output.path</td>
 * 		<td>The suspend XML output path used to locate the fax job ID from the suspend fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.resume.xml.output.path</td>
 * 		<td>The resume XML output path used to locate the fax job ID from the resume fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.cancel.xml.output.path</td>
 * 		<td>The cancel XML output path used to locate the fax job ID from the cancel fax job response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.get.status.xml.output.path</td>
 * 		<td>The get fax job status XML output path used to locate the fax job status string value from the response.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.xml.pending.status.mappings</td>
 * 		<td>The mapping between the output fax status string value to the fax4j FaxJobStatus.PENDING enum value (format is: value1;value2;value3).</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.xml.inprogress.status.mappings</td>
 * 		<td>The mapping between the output fax status string value to the fax4j FaxJobStatus.IN_PROGRESS enum value (format is: value1;value2;value3).</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.xml.error.status.mappings</td>
 * 		<td>The mapping between the output fax status string value to the fax4j FaxJobStatus.ERROR enum value (format is: value1;value2;value3).</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.xml.error.detection.path</td>
 * 		<td>The error detection path. If defined the handler will search for values in the defined path.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.xml.error.detection.value</td>
 * 		<td>If detection path and value are defined, the value in the path is searched within the configured value. If value is not defined, all values are considered as errors.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.http.xml.error.message.path</td>
 * 		<td>The path to the error message if errors are detected. If this property is not defined, the value found at org.fax4j.spi.http.xml.error.detection.path will be used instead.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.8
 */
public class XMLHTTPResponseHandler extends AbstractMappingHTTPResponseHandler<Document>
{
	/**
	 * This class holds the configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.42.8
	 */
	public enum XMLHTTPResponseHandlerConfigurationConstants
	{
		/**The submit XML output path property key*/
		SUBMIT_XML_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.submit.xml.output.path"),
		/**The suspend XML output path property key*/
		SUSPEND_XML_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.suspend.xml.output.path"),
		/**The resume XML output path property key*/
		RESUME_XML_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.resume.xml.output.path"),
		/**The cancel XML output path property key*/
		CANCEL_XML_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.cancel.xml.output.path"),
		/**The get fax job status XML output path property key*/
		GET_STATUS_XML_OUTPUT_PROPERTY_KEY("org.fax4j.spi.{0}.get.status.xml.output.path"),
		/**The FaxJobStatus.PENDING status mapping property key*/
		PENDING_STATUS_MAPPING_PROPERTY_KEY("org.fax4j.spi.{0}.xml.pending.status.mappings"),
		/**The FaxJobStatus.IN_PROGRESS status mapping property key*/
		IN_PROGRESS_STATUS_MAPPING_PROPERTY_KEY("org.fax4j.spi.{0}.xml.inprogress.status.mappings"),
		/**The FaxJobStatus.ERROR status mapping property key*/
		ERROR_STATUS_MAPPING_PROPERTY_KEY("org.fax4j.spi.{0}.xml.error.status.mappings"),
		/**The error detection path property key*/
		ERROR_DETECTION_PATH_PROPERTY_KEY("org.fax4j.spi.{0}.xml.error.detection.path"),
		/**The error detection value property key*/
		ERROR_DETECTION_VALUE_PROPERTY_KEY("org.fax4j.spi.{0}.xml.error.detection.value"),
		/**The error message path property key*/
		ERROR_MESSAGE_PATH_PROPERTY_KEY("org.fax4j.spi.{0}.xml.error.message.path");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private XMLHTTPResponseHandlerConfigurationConstants(String value)
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
	public XMLHTTPResponseHandler()
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
		Enum<?>[] configurationKeys=new Enum<?>[]{XMLHTTPResponseHandlerConfigurationConstants.SUBMIT_XML_OUTPUT_PROPERTY_KEY,
									 XMLHTTPResponseHandlerConfigurationConstants.SUSPEND_XML_OUTPUT_PROPERTY_KEY,
									 XMLHTTPResponseHandlerConfigurationConstants.RESUME_XML_OUTPUT_PROPERTY_KEY,
									 XMLHTTPResponseHandlerConfigurationConstants.CANCEL_XML_OUTPUT_PROPERTY_KEY,
									 XMLHTTPResponseHandlerConfigurationConstants.GET_STATUS_XML_OUTPUT_PROPERTY_KEY};
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
		Enum<?>[] configurationKeys=new Enum<?>[]{XMLHTTPResponseHandlerConfigurationConstants.PENDING_STATUS_MAPPING_PROPERTY_KEY,
									 XMLHTTPResponseHandlerConfigurationConstants.IN_PROGRESS_STATUS_MAPPING_PROPERTY_KEY,
									 XMLHTTPResponseHandlerConfigurationConstants.ERROR_STATUS_MAPPING_PROPERTY_KEY};
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
		return XMLHTTPResponseHandlerConfigurationConstants.ERROR_DETECTION_PATH_PROPERTY_KEY;
	}

	/**
	 * Returns the error detection value configuration key.
	 * 
	 * @return	The error detection value configuration key
	 */
	@Override
	protected Enum<?> getErrorDetectionValuePropertyKey()
	{
		return XMLHTTPResponseHandlerConfigurationConstants.ERROR_DETECTION_VALUE_PROPERTY_KEY;
	}

	/**
	 * Returns the error message path configuration key.
	 * 
	 * @return	The error message path configuration key
	 */
	@Override
	protected Enum<?> getErrorMessagePathPropertyKey()
	{
		return XMLHTTPResponseHandlerConfigurationConstants.ERROR_MESSAGE_PATH_PROPERTY_KEY;
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
	protected String findValueImpl(Document object,String path)
	{
		//split path to parts
		String[] pathParts=path.split(AbstractMappingHTTPResponseHandler.VALUES_SEPERATOR);
		int pathPartsAmount=pathParts.length;
		String pathPart=null;
		StringBuilder buffer=new StringBuilder(500);
		for(int index=0;index<pathPartsAmount;index++)
		{
			//get next path part
			pathPart=pathParts[index];
			
			if(pathPart.length()>0)
			{
				buffer.append("/");
				buffer.append(pathPart);
			}
		}
		String xpathExpressionStr=buffer.toString();

		XPathFactory xpathFactory=XPathFactory.newInstance();
		XPath xpath=xpathFactory.newXPath();
		String value=null;
		try
		{
			//compile path to xpath
			XPathExpression xpathExpression=xpath.compile(xpathExpressionStr);

			//find value
			value=xpathExpression.evaluate(object);
		}
		catch(XPathExpressionException exception)
		{
			throw new FaxException("Unable to parse/evaluate xpath expression: "+path,exception);
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
	protected Document convertToObject(HTTPResponse httpResponse)
	{
		//get response text
		Document document=null;
		String content=httpResponse.getContent();
		if(content!=null)
		{
			//get response encoding
			String encoding=this.getResponseDataEncoding();

			//create in memory stream
			byte[] data=IOHelper.convertStringToBinary(content,encoding);
			InputStream inputStream=new ByteArrayInputStream(data);

			try
			{
				//parse XML
				DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
				document=documentBuilder.parse(inputStream);
			}
			catch(Exception exception)
			{
				throw new FaxException("Unable to parse HTTP response text as XML.",exception);
			}
			finally
			{
				//'close' stream
				IOHelper.closeResource(inputStream);
			}
		}
		
		return document;
	}
}