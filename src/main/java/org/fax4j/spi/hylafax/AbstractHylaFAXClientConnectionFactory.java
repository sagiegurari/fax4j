package org.fax4j.spi.hylafax;

import gnu.hylafax.HylaFAXClient;
import org.fax4j.FaxException;
import org.fax4j.spi.hylafax.HylaFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.util.AbstractFax4JConnectionFactory;

/**
 * Provides partial implementation of the HaylaFAX connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.04
 * @since	0.39a
 */
public abstract class AbstractHylaFAXClientConnectionFactory extends AbstractFax4JConnectionFactory<HylaFAXClient> implements HylaFAXClientConnectionFactory
{
	/**The fax server host*/
	protected String host;
	/**The fax server port*/
	protected int port;
	/**The fax server user name*/
	protected String userName;
	/**The fax server password*/
	protected String password;
	/**The enable admin operations flag*/
	protected boolean enableAdminOperations;
	/**The mode*/
	protected char mode;
	/**The type*/
	protected char type;

	/**
	 * This is the class constructor.
	 */
	public AbstractHylaFAXClientConnectionFactory()
	{
		super();
	}

	/**
	 * This function initializes the connection factory.
	 */
	@Override
	protected void initializeImpl()
	{
		//get values
		this.host=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.HOST_PROPERTY_KEY);
		if(this.host==null)
		{
			throw new FaxException("Host name not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.HOST_PROPERTY_KEY);
		}
		String valueStr=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.PORT_PROPERTY_KEY);
		if(valueStr==null)
		{
			this.port=HylaFaxClientSpi.DEFAULT_PORT_PROPERTY_VALUE;
		}
		else
		{
			this.port=Integer.parseInt(valueStr);
		}
		this.userName=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.USER_NAME_PROPERTY_KEY);
		if(this.userName==null)
		{
			throw new FaxException("User name not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.USER_NAME_PROPERTY_KEY);
		}
		this.password=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.PASSWORD_PROPERTY_KEY);
		this.enableAdminOperations=Boolean.parseBoolean(this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.ENABLE_ADMIN_OPERATIONS_PROPERTY_KEY));
		valueStr=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.MODE_PROPERTY_KEY);
		if(valueStr==null)
		{
			throw new FaxException("Mode not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.MODE_PROPERTY_KEY);
		}
		this.mode=valueStr.charAt(0);
		valueStr=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.TYPE_PROPERTY_KEY);
		if(valueStr==null)
		{
			throw new FaxException("Type not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.TYPE_PROPERTY_KEY);
		}
		this.type=valueStr.charAt(0);
	}
}