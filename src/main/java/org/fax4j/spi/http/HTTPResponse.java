package org.fax4j.spi.http;

/**
 * Holds the HTTP response data extracted after the fax request was sent
 * to the web server.<br>
 * This class is not thread safe.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.1
 */
public class HTTPResponse
{
	/**The response status*/
	private int statusCode;
	/**The response content*/
	private String content;
	
	/**
	 * This is the default constructor.
	 */
	public HTTPResponse()
	{
		super();
	}

	/**
	 * This function returns the response status.
	 * 
	 * @return 	The response status
	 */
	public final int getStatusCode()
	{
		return this.statusCode;
	}

	/**
	 * This function sets the response status.
	 * 
	 * @param 	statusCode
	 * 			The new value for the response status
	 */
	public final void setStatusCode(int statusCode)
	{
		this.statusCode=statusCode;
	}

	/**
	 * This function returns the content.
	 * 
	 * @return 	The content
	 */
	public final String getContent()
	{
		return this.content;
	}

	/**
	 * This function sets the content.
	 * 
	 * @param 	content
	 * 			The new value for the content
	 */
	public final void setContent(String content)
	{
		this.content=content;
	}
}