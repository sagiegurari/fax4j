package org.fax4j.common;

import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.Provider;

/**
 * This class provides partial implementation of the service interface.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.42.4
 */
public abstract class AbstractService implements Service
{
	/**The provider*/
	private final Provider PROVIDER;
	/**The initialized flag*/
	private boolean initialized;
	/**The property part*/
	private String propertyPart;
	/**The service configuration*/
	private ConfigurationHolder serviceConfiguration;
	/**The logger*/
	private Logger serviceLogger;

	/**
	 * This is the default constructor.
	 * 
	 * @param	provider
	 * 			The provider
	 */
	public AbstractService(Provider provider)
	{
		super();
		
		//set defaults
		this.initialized=false;
		this.propertyPart=null;
		
		//get provider
		this.PROVIDER=provider;
		if(this.PROVIDER==null)
		{
			throw new FaxException("Provider is null.");
		}
	}

	/**
	 * This function returns the provider.
	 * 
	 * @return	The provider
	 */
	public final Provider getProvider()
	{
		return this.PROVIDER;
	}

	/**
	 * This function initializes the component.
	 * 
	 * @param	configuration
	 * 			The component configuration
	 */
	public final synchronized void initialize(Map<String,String> configuration)
	{
		if(this.initialized)
		{
			throw new FaxException("Service already initialized.");
		}
		
		//set flag
		this.initialized=true;

		//get configuration
		this.serviceConfiguration=new ConfigurationHolderImpl(configuration,this.propertyPart);

		//get logger
		LoggerManager loggerManager=LoggerManager.getInstance();
		this.serviceLogger=loggerManager.getLogger();
		
		//initialize
		this.initializeImpl();
	}

	/**
	 * Returns the internal logger.
	 *  
	 * @return	The internal logger
	 */
	public final Logger getLogger()
	{
		return this.serviceLogger;
	}

	/***
	 * This function sets the property part value.<br>
	 * This function should be called before the initialize method.<br>
	 * See org.fax4j.common.ConfigurationHolder.getPropertyPart() for more info.
	 * 
	 * @param 	propertyPart
	 * 			The property part value
	 */
	public void setPropertyPart(String propertyPart)
	{
		this.propertyPart=propertyPart;
	}

	/**
	 * Returns the property part.<br>
	 * Property parts enables to replace the input request key with the part defined
	 * to enable to reuse services with different configuration blocks.<br>
	 * Property parts are the values after the common prefix and before any specific configuration key,
	 * or in other words the top context of the configuration.<br>
	 * For example: org.fax4j.[partvalue].some.key<br>
	 * The org.fax4j is the common prefix, the some.key is the suffix and partvalue is the part to be replaced.
	 *  
	 * @return	The property part
	 */
	public final String getPropertyPart()
	{
		return this.serviceConfiguration.getPropertyPart();
	}

	/**
	 * Returns the configuration.
	 *  
	 * @return	The configuration
	 */
	public final Map<String,String> getConfiguration()
	{
		return this.serviceConfiguration.getConfiguration();
	}

	/**
	 * Returns the value from the component configuration based on the provided 
	 * configuration key.
	 * The value will be trimmed.<br>
	 * If the trimmed configuration value is an empty string,
	 * null will be returned instead.
	 *  
	 * @param 	key
	 * 			The configuration key
	 * @return	The value
	 */
	public final String getConfigurationValue(String key)
	{
		//get value
		String value=this.serviceConfiguration.getConfigurationValue(key);
		
		return value;
	}

	/**
	 * Returns the value from the component configuration based on the provided 
	 * configuration key.
	 * The value will be trimmed.<br>
	 * If the trimmed configuration value is an empty string,
	 * null will be returned instead.
	 *  
	 * @param 	key
	 * 			The configuration key (toString value will be used)
	 * @return	The value
	 */
	public final String getConfigurationValue(Enum<?> key)
	{
		//get value
		String value=this.serviceConfiguration.getConfigurationValue(key);
		
		return value;
	}

	/**
	 * This function initializes the component.
	 */
	protected abstract void initializeImpl();
}