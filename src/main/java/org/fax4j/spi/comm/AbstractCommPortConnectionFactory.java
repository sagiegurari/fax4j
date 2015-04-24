package org.fax4j.spi.comm;

import java.io.IOException;
import org.fax4j.FaxException;
import org.fax4j.spi.comm.CommFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.util.AbstractFax4JConnectionFactory;

/**
 * Provides partial implementation of the COMM port connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.05
 * @since	0.34
 */
public abstract class AbstractCommPortConnectionFactory extends AbstractFax4JConnectionFactory<CommPortAdapter> implements CommPortConnectionFactory
{
	/**The COMM port name (COM1, ...)*/
	private String portName;
	/**The timeout*/
	private int timeout;

	/**
	 * This is the class constructor.
	 */
	public AbstractCommPortConnectionFactory()
	{
		super();
	}

	/**
	 * This function initializes the connection factory.
	 */
	@Override
	protected void initializeImpl()
	{
		//get port name
		this.portName=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.PORT_NAME_PROPERTY_KEY);
		if(this.portName==null)
		{
			throw new FaxException("Port name not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.PORT_NAME_PROPERTY_KEY);
		}

		//get timeout
		String valueStr=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.TIMEOUT_PROPERTY_KEY);
		this.timeout=CommFaxClientSpi.TIMEOUT_DEFAULT_VALUE;
		if((valueStr!=null)&&(valueStr.length()>0))
		{
			try
			{
				this.timeout=Integer.parseInt(valueStr);
			}
			catch(Exception exception)
			{
				this.timeout=CommFaxClientSpi.TIMEOUT_DEFAULT_VALUE;
			}
		}
		if(this.timeout<=0)
		{
			this.timeout=CommFaxClientSpi.TIMEOUT_DEFAULT_VALUE;
		}
	}

	/**
	 * Releases the resource from the connection.
	 *  
	 * @param	resource
	 * 			The resource
	 */
	@Override
	protected void releaseResourceImpl(CommPortAdapter resource)
	{
		try
		{
			resource.close();
		}
		catch(IOException exception)
		{
			throw new FaxException("Unable to close COMM port adapter.",exception);
		}
	}
	
	/**
	 * This function returns the port name.
	 * 
	 * @return	The port name
	 */
	public final String getPortName()
	{
		return this.portName;
	}
	
	/**
	 * This function returns the timeout.
	 * 
	 * @return	The timeout
	 */
	public final int getTimeOut()
	{
		return this.timeout;
	}
}