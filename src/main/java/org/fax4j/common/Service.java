package org.fax4j.common;

/**
 * Common interface for services.<br>
 * Services have common initialization and setup process.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.42.4
 */
public interface Service extends ProviderImplementation,Initializable,ConfigurationHolder
{
	/***
	 * This function sets the property part value.<br>
	 * This function should be called before the initialize method.<br>
	 * See org.fax4j.common.ConfigurationHolder.getPropertyPart() for more info.
	 * 
	 * @param 	propertyPart
	 * 			The property part value
	 */
	public void setPropertyPart(String propertyPart);
}