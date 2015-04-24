package org.fax4j.spi.http;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxException;

/**
 * Holds the HTTP request data used to send the fax data to the
 * web server.<br>
 * This class is not thread safe.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.1
 */
public class HTTPRequest
{
	/**The requested resource*/
	private String resource;
	/**The URL parameters text*/
	private String parametersText;
	/**The header properties*/
	private Properties headerProperties;
	/**The content type*/
	private ContentType contentType;
	/**The content*/
	private String contentString;
	/**The content*/
	private byte[] contentBinary;
	/**The content parts*/
	private ContentPart<?>[] contentParts;

	/**
	 * This is the default constructor.
	 */
	public HTTPRequest()
	{
		super();
		
		//set defaults
		this.setContent((String)null);
	}

	/**
	 * This function returns the requested resource.
	 * 
	 * @return 	The requested resource
	 */
	public final String getResource()
	{
		return this.resource;
	}

	/**
	 * This function returns the URL parameters text.
	 * 
	 * @return 	The URL parameters text
	 */
	public final String getParametersText()
	{
		return this.parametersText;
	}

	/**
	 * This function returns the header properties.
	 * 
	 * @return 	The header properties
	 */
	public final Properties getHeaderProperties()
	{
		return this.headerProperties;
	}

	/**
	 * This function sets the requested resource.
	 * 
	 * @param 	resource
	 * 			The new value for the requested resource
	 */
	public final void setResource(String resource)
	{
		this.resource=resource;
	}

	/**
	 * This function sets the URL parameters text.
	 * 
	 * @param 	parametersText
	 * 			The new value for the URL parameters text
	 */
	public final void setParametersText(String parametersText)
	{
		this.parametersText=parametersText;
	}

	/**
	 * This function sets the header properties.
	 * 
	 * @param 	headerProperties
	 * 			The new value for the header properties
	 */
	public final void setHeaderProperties(Properties headerProperties)
	{
		this.headerProperties=null;
		if(headerProperties!=null)
		{
			this.headerProperties=new Properties();
			this.headerProperties.putAll(headerProperties);
		}
	}
	
	/**
	 * This function returns the content type of this request.
	 * 
	 * @return	The content type
	 */
	public final ContentType getContentType()
	{
		return this.contentType;
	}

	/**
	 * This function returns the content.
	 * 
	 * @return 	The content
	 */
	public final byte[] getContentAsBinary()
	{
		if(!this.contentType.equals(ContentType.BINARY))
		{
			throw new FaxException("Current content type is: "+this.contentType);
		}

		byte[] clone=null;
		if(this.contentBinary!=null)
		{
			clone=this.contentBinary.clone();
		}
		return clone;
	}

	/**
	 * This function sets the content.
	 * 
	 * @param 	content
	 * 			The new value for the content
	 */
	public final void setContent(byte[] content)
	{
		//clear non relevant flags
		this.contentString=null;
		this.contentParts=null;
		this.contentType=ContentType.BINARY;

		if(content==null)
		{
			this.contentBinary=null;
		}
		else
		{
			this.contentBinary=content.clone();
		}
	}

	/**
	 * This function returns the content.
	 * 
	 * @return 	The content
	 */
	public final String getContentAsString()
	{
		if(!this.contentType.equals(ContentType.STRING))
		{
			throw new FaxException("Current content type is: "+this.contentType);
		}

		return this.contentString;
	}

	/**
	 * This function sets the content.
	 * 
	 * @param 	content
	 * 			The new value for the content
	 */
	public final void setContent(String content)
	{
		//clear non relevant flags
		this.contentBinary=null;
		this.contentParts=null;
		this.contentType=ContentType.STRING;

		this.contentString=content;
	}

	/**
	 * This function returns the content.
	 * 
	 * @return 	The content
	 */
	public final ContentPart<?>[] getContentAsParts()
	{
		if(!this.contentType.equals(ContentType.MULTI_PART))
		{
			throw new FaxException("Current content type is: "+this.contentType);
		}

		ContentPart<?>[] clone=null;
		if(this.contentParts!=null)
		{
			clone=this.contentParts.clone();
		}
		return clone;
	}

	/**
	 * This function sets the content.
	 * 
	 * @param 	content
	 * 			The new value for the content
	 */
	public final void setContent(ContentPart<?>[] content)
	{
		//clear non relevant flags
		this.contentString=null;
		this.contentBinary=null;
		this.contentType=ContentType.MULTI_PART;

		if(content==null)
		{
			this.contentParts=null;
		}
		else
		{
			this.contentParts=content.clone();
		}
	}

	/**
	 * This enum defines the content types which are supported.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.42.7
	 */
	public static enum ContentType
	{
		/**
		 * Content type
		 */
		STRING,
		/**
		 * Content type
		 */
		BINARY,
		/**
		 * Content type
		 */
		MULTI_PART
	}

	/**
	 * This enum defines the content part types which are supported.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.42.7
	 */
	public static enum ContentPartType
	{
		/**
		 * File content part type.
		 */
		FILE,
		/**
		 * String content part type
		 */
		STRING,
		/**
		 * Binary content part type.
		 */
		BINARY
	}

	/**
	 * This class holds the content part data.
	 * 
	 * @param 	<T> 
	 * 			The part type
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.42.7
	 */
	public static final class ContentPart<T>
	{
		/**The part name/key*/
		private final String NAME;
		/**The part data*/
		private final T CONTENT;
		/**The part type*/
		private final ContentPartType TYPE;
		
		/**
		 * This is the class constructor.
		 * 
		 * @param 	name
		 * 			The part name/key
		 * @param 	content
		 * 			The part data
		 * @param 	type
		 * 			The part type
		 */
		public ContentPart(String name,T content,ContentPartType type)
		{
			super();

			//validate data
			if(name==null)
			{
				throw new FaxException("Part name not provided.");
			}
			if(content==null)
			{
				throw new FaxException("Part content not provided.");
			}
			if(type==null)
			{
				throw new FaxException("Part type not provided.");
			}
			switch(type)
			{
				case FILE:
					if(!(content instanceof File))
					{
						throw new FaxException("File type content must be a File object.");
					}
					break;
				case STRING:
					if(!(content instanceof String))
					{
						throw new FaxException("String type content must be a String object.");
					}
					break;
				case BINARY:
					if(!(content instanceof byte[]))
					{
						throw new FaxException("Binary type content must be a byte[] array.");
					}
					break;
				default:
					throw new FaxException("Unsupported content type provided: "+type);
			}
			
			this.NAME=name;
			this.CONTENT=content;
			this.TYPE=type;
		}

		/**
		 * This function returns the part name/key.
		 * 
		 * @return	The part name/key
		 */
		public final String getName()
		{
			return this.NAME;
		}

		/**
		 * This function returns the content of the part.
		 * 
		 * @return	The content of the part
		 */
		public final T getContent()
		{
			return this.CONTENT;
		}

		/**
		 * This function returns the content type.
		 * 
		 * @return	The content type
		 */
		public final ContentPartType getType()
		{
			return this.TYPE;
		}
	}
}