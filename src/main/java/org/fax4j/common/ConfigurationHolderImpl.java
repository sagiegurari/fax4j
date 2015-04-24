package org.fax4j.common;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a common basic implementation of the configuration holder.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.41.7
 */
public final class ConfigurationHolderImpl implements ConfigurationHolder
{
	/**The property part*/
	private final String PROPERTY_PART;
	/**The configuration*/
	private final Map<String,String> CONFIGURATION;
	/**The logger*/
	private final Logger LOGGER;

	/**
	 * This is the class constructor.
	 * 
	 * @param 	map
	 * 			The configuration as map
	 */
	public ConfigurationHolderImpl(Map<String,String> map)
	{
		this(map,null);
	}

	/**
	 * This is the class constructor.
	 * 
	 * @param 	map
	 * 			The configuration as map
	 * @param	propertyPart
	 * 			The property part
	 */
	public ConfigurationHolderImpl(Map<String,String> map,String propertyPart)
	{
		super();
		
		//get logger
		LoggerManager loggerManager=LoggerManager.getInstance();
		this.LOGGER=loggerManager.getLogger();

		//get configuration
		Map<String,String> configuration=new HashMap<String,String>(map);
		this.CONFIGURATION=Collections.unmodifiableMap(configuration);
		
		//get property part
		this.PROPERTY_PART=propertyPart;
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
		return this.PROPERTY_PART;
	}

	/**
	 * Returns the configuration.
	 *  
	 * @return	The configuration
	 */
	public final Map<String,String> getConfiguration()
	{
		return this.CONFIGURATION;
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
		//format property key
		String propertyKey=key;
		if(this.PROPERTY_PART!=null)
		{
			propertyKey=MessageFormat.format(key,new Object[]{this.PROPERTY_PART});
		}

		//get value
		String value=this.CONFIGURATION.get(propertyKey);
		
		//trim value
		if(value!=null)
		{
			value=value.trim();
			
			//set as null if empty string
			if(value.length()==0)
			{
				value=null;
			}
		}
		
		this.LOGGER.logDebug(new Object[]{"Extracted configuration for key: ",propertyKey," value: ",value},null);
		
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
		//get string key
		String keyStr=key.toString();
		
		//get value
		String value=this.getConfigurationValue(keyStr);
		
		return value;
	}
}