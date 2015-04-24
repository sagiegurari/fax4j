package org.fax4j.common;

import java.util.Map;

/**
 * Common interface for configuration holder components.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.41.7
 */
public interface ConfigurationHolder
{
	/**
	 * Returns the property part.<br>
	 * Property parts enables to replace the input request key with the part defined
	 * to enable to reuse services with different configuration blocks.<br>
	 * Property parts will be replaced with the {0} values in the key,
	 * for example org.fax4j.{0}.somekey with part abc will be replaced to org.fax4j.abc.somekey
	 *  
	 * @return	The property part
	 */
	public String getPropertyPart();

	/**
	 * Returns the configuration.
	 *  
	 * @return	The configuration
	 */
	public Map<String,String> getConfiguration();

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
	public String getConfigurationValue(String key);

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
	public String getConfigurationValue(Enum<?> key);
}