package org.fax4j.spi.http;

import org.fax4j.Provider;
import org.fax4j.common.AbstractService;
import org.fax4j.util.IOHelper;

/**
 * This class provides partial implementation of the HTTP response handler interface.
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
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.42.4
 */
public abstract class AbstractHTTPResponseHandler extends AbstractService implements HTTPResponseHandler
{
	/**The response data encoding*/
	private String responseDataEncoding;
	/**The response data encoding property key*/
	public static final String RESPONSE_DATA_ENCODING_PROPERTY_KEY="org.fax4j.spi.{0}.response.data.encoding";

	/**
	 * This is the default constructor.
	 * 
	 * @param	provider
	 * 			The provider
	 */
	public AbstractHTTPResponseHandler(Provider provider)
	{
		super(provider);
	}

	/**
	 * This function initializes the component.
	 */
	@Override
	protected void initializeImpl()
	{
		//get encoding
		this.responseDataEncoding=this.getConfigurationValue(AbstractHTTPResponseHandler.RESPONSE_DATA_ENCODING_PROPERTY_KEY);
		if(this.responseDataEncoding==null)
		{
			this.responseDataEncoding=IOHelper.getDefaultEncoding();
		}
	}
	
	/**
	 * This function returns the response data encoding.
	 * 
	 * @return	The response data encoding
	 */
	protected final String getResponseDataEncoding()
	{
		return this.responseDataEncoding;
	}
}