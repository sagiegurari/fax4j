package org.fax4j.common;

import java.util.Map;

/**
 * Common interface for initializable components.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.41.3
 */
public interface Initializable
{
	/**
	 * This function initializes the component.
	 * 
	 * @param	configuration
	 * 			The component configuration
	 */
	public void initialize(Map<String,String> configuration);
}