package org.fax4j.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;
import org.fax4j.common.LoggerManager;

/**
 * This is an internal general utility class.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.07
 * @since	0.21e
 */
public final class SpiUtil
{
	/**The empty string value*/
	public static final String EMPTY_STRING="";
	/**The URL encoder*/
	public static final URLTemplateParameterEncoder URL_ENCODER=new URLTemplateParameterEncoder();
	/**The UTF-8 encoding*/
	public static final String UTF_8_ENCODING_NAME="UTF-8";
	/**The template parameter prefix*/
	public static final String TEMPLATE_PARAMETER_PREFIX="${";
	/**The template parameter suffix*/
	public static final String TEMPLATE_PARAMETER_SUFFIX="}";
	/**The file template parameter*/
	public static final String FILE_TEMPLATE_PARAMETER="file";
	/**The target address template parameter*/
	public static final String TARGET_ADDRESS_TEMPLATE_PARAMETER="target.address";
	/**The target name template parameter*/
	public static final String TARGET_NAME_TEMPLATE_PARAMETER="target.name";
	/**The sender name template parameter*/
	public static final String SENDER_NAME_TEMPLATE_PARAMETER="sender.name";
	/**The sender fax number template parameter*/
	public static final String SENDER_FAX_NUMBER_TEMPLATE_PARAMETER="sender.fax.number";
	/**The sender email template parameter*/
	public static final String SENDER_EMAIL_TEMPLATE_PARAMETER="sender.email";
	/**The fax job ID template parameter*/
	public static final String FAX_JOB_ID_TEMPLATE_PARAMETER="fax.job.id";
	/**The file template parameter*/
	public static final String FILE_TEMPLATE_PARAMETER_STRING=SpiUtil.TEMPLATE_PARAMETER_PREFIX+SpiUtil.FILE_TEMPLATE_PARAMETER+SpiUtil.TEMPLATE_PARAMETER_SUFFIX;
	/**The target address template parameter*/
	public static final String TARGET_ADDRESS_TEMPLATE_PARAMETER_STRING=SpiUtil.TEMPLATE_PARAMETER_PREFIX+SpiUtil.TARGET_ADDRESS_TEMPLATE_PARAMETER+SpiUtil.TEMPLATE_PARAMETER_SUFFIX;
	/**The target name template parameter*/
	public static final String TARGET_NAME_TEMPLATE_PARAMETER_STRING=SpiUtil.TEMPLATE_PARAMETER_PREFIX+SpiUtil.TARGET_NAME_TEMPLATE_PARAMETER+SpiUtil.TEMPLATE_PARAMETER_SUFFIX;
	/**The sender name template parameter*/
	public static final String SENDER_NAME_TEMPLATE_PARAMETER_STRING=SpiUtil.TEMPLATE_PARAMETER_PREFIX+SpiUtil.SENDER_NAME_TEMPLATE_PARAMETER+SpiUtil.TEMPLATE_PARAMETER_SUFFIX;
	/**The sender fax number template parameter*/
	public static final String SENDER_FAX_NUMBER_TEMPLATE_PARAMETER_STRING=SpiUtil.TEMPLATE_PARAMETER_PREFIX+SpiUtil.SENDER_FAX_NUMBER_TEMPLATE_PARAMETER+SpiUtil.TEMPLATE_PARAMETER_SUFFIX;
	/**The sender email template parameter*/
	public static final String SENDER_EMAIL_TEMPLATE_PARAMETER_STRING=SpiUtil.TEMPLATE_PARAMETER_PREFIX+SpiUtil.SENDER_EMAIL_TEMPLATE_PARAMETER+SpiUtil.TEMPLATE_PARAMETER_SUFFIX;
	/**The fax job ID template parameter*/
	public static final String FAX_JOB_ID_TEMPLATE_PARAMETER_STRING=SpiUtil.TEMPLATE_PARAMETER_PREFIX+SpiUtil.SENDER_EMAIL_TEMPLATE_PARAMETER+SpiUtil.TEMPLATE_PARAMETER_SUFFIX;

	/**
	 * This is the default constructor.
	 */
	private SpiUtil()
	{
		super();
	}

	/**
	 * This function copies all mappings from source properties to target map.
	 * 
	 * @param 	source
	 * 			The source properties
	 * @param 	target
	 * 			The target map to populate
	 */
	public static void copyPropertiesToMap(Properties source,Map<String,String> target)
	{
		if(source!=null)
		{
			//convert to map
			Iterator<Entry<Object,Object>> iterator=source.entrySet().iterator();
			Entry<Object,Object> entry=null;
			String key=null;
			String value=null;
			while(iterator.hasNext())
			{
				//get next entry
				entry=iterator.next();
				
				//get next key/value
				key=(String)entry.getKey();
				value=(String)entry.getValue();
				
				//put in map
				target.put(key,value);
			}
		}
	}
		
	/**
	 * This function formats the provided template.
	 * 
	 * @param 	template
	 * 			The template
	 * @param 	faxJob
	 * 			The fax job object
	 * @param	encoder
	 * 			The encoder that encodes the template values (may be null)
	 * @param	getFileContent
	 * 			True to get the file content, false to get the file path for the file template parameter
	 * @param	fullFilePath
	 * 			If getFileContent=false, the param will be replaced to the file name (full path in case this is true, or just the name in case of false)
	 * @return	The formatted template
	 */
	public static String formatTemplate(String template,FaxJob faxJob,TemplateParameterEncoder encoder,boolean getFileContent,boolean fullFilePath)
	{
		//get logger
		LoggerManager loggerManager=LoggerManager.getInstance();
		Logger logger=loggerManager.getLogger();

		//log
		logger.logDebug(new Object[]{"Formatting template:",Logger.SYSTEM_EOL,template},null);
		
		String text=template;
		if(text!=null)
		{
			String value=null;
			if(text.indexOf(SpiUtil.FILE_TEMPLATE_PARAMETER_STRING)!=-1)
			{
				if(getFileContent)
				{
					value=SpiUtil.getFileParameterValue(faxJob);
				}
				else if(fullFilePath)
				{
					value=faxJob.getFilePath();
				}
				else
				{
					File file=faxJob.getFile();
					value=file.getName();
				}
				text=SpiUtil.replaceTemplateParameter(text,SpiUtil.FILE_TEMPLATE_PARAMETER_STRING,value,encoder);
			}
			value=faxJob.getTargetAddress();
			text=SpiUtil.replaceTemplateParameter(text,SpiUtil.TARGET_ADDRESS_TEMPLATE_PARAMETER_STRING,value,encoder);
			value=faxJob.getTargetName();
			text=SpiUtil.replaceTemplateParameter(text,SpiUtil.TARGET_NAME_TEMPLATE_PARAMETER_STRING,value,encoder);
			value=faxJob.getSenderName();
			text=SpiUtil.replaceTemplateParameter(text,SpiUtil.SENDER_NAME_TEMPLATE_PARAMETER_STRING,value,encoder);
			value=faxJob.getSenderFaxNumber();
			text=SpiUtil.replaceTemplateParameter(text,SpiUtil.SENDER_FAX_NUMBER_TEMPLATE_PARAMETER_STRING,value,encoder);
			value=faxJob.getSenderEmail();
			text=SpiUtil.replaceTemplateParameter(text,SpiUtil.SENDER_EMAIL_TEMPLATE_PARAMETER_STRING,value,encoder);
			value=faxJob.getID();
			text=SpiUtil.replaceTemplateParameter(text,SpiUtil.FAX_JOB_ID_TEMPLATE_PARAMETER_STRING,value,encoder);
		}

		//log
		logger.logDebug(new Object[]{"Formated template:",Logger.SYSTEM_EOL,text},null);
		
		return text;
	}
	
	/**
	 * This function replaces the parameter with the provided value.
	 * 
	 * @param 	template
	 * 			The template
	 * @param 	parameter
	 * 			The parameter
	 * @param 	value
	 * 			The value
	 * @param	encoder
	 * 			The encoder that encodes the template values (may be null)
	 * @return	The updated template
	 */
	public static String replaceTemplateParameter(String template,String parameter,String value,TemplateParameterEncoder encoder)
	{
		String text=template;
		if((text!=null)&&(parameter!=null))
		{
			String updatedValue=value;
			if(updatedValue==null)
			{
				updatedValue=SpiUtil.EMPTY_STRING;
			}
			else if(encoder!=null)
			{
				updatedValue=encoder.encodeTemplateParameter(updatedValue);
			}
			text=text.replace(parameter,updatedValue);
		}
		
		return text;
	}
	
	/**
	 * This function URL encodes the given text.
	 * 
	 * @param 	text
	 * 			The text to encode
	 * @return	The encoded text
	 */
	public static String urlEncode(String text)
	{
		String urlEncodedString=text;
		if(text!=null)
		{
			try
			{
				urlEncodedString=URLEncoder.encode(text,SpiUtil.UTF_8_ENCODING_NAME);
			}
			catch(UnsupportedEncodingException exception)
			{
				throw new FaxException("Error while URL encoding text.",exception);
			}
		}
		
		return urlEncodedString;
	}

	/**
	 * This function URL decodes the given text.
	 * 
	 * @param 	text
	 * 			The text to decode
	 * @return	The decoded text
	 */
	public static String urlDecode(String text)
	{
		String urlEncodedString=text;
		if(text!=null)
		{
			try
			{
				urlEncodedString=URLDecoder.decode(text,SpiUtil.UTF_8_ENCODING_NAME);
			}
			catch(UnsupportedEncodingException exception)
			{
				throw new FaxException("Error while URL decoding text.",exception);
			}
		}
		
		return urlEncodedString;
	}
	
	/**
	 * This function returns the file parameter value based on the file
	 * content.
	 * 
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The file parameter value
	 */
	private static String getFileParameterValue(FaxJob faxJob)
	{
		String value=null;
		File file=faxJob.getFile();
		if(file!=null)
		{
			try
			{
				//read file (only text files supported)
				value=IOHelper.readTextFile(faxJob.getFile());
			}
			catch(IOException exception)
			{
				throw new FaxException("Error while reading file.",exception);
			}
		}
	
		return value;
	}

	/**
	 * This interface defines a template parameter encoder.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.21e
	 */
	public static interface TemplateParameterEncoder
	{
		/**
		 * This function encodes the given template parameter text.
		 * 
		 * @param 	text
		 * 			The text to encode
		 * @return	The encoded text
		 */
		public String encodeTemplateParameter(String text);
	}

	/**
	 * This class implements the URL encoder.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.21e
	 */
	public static class URLTemplateParameterEncoder implements TemplateParameterEncoder
	{
		/**
		 * This is the class constructor.
		 */
		public URLTemplateParameterEncoder()
		{
			super();
		}

		/**
		 * This function encodes the given template parameter text.
		 * 
		 * @param 	text
		 * 			The text to encode
		 * @return	The encoded text
		 */
		public String encodeTemplateParameter(String text)
		{
			return SpiUtil.urlEncode(text);
		}
	}
}