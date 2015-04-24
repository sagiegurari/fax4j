package org.fax4j.common;

import org.fax4j.Provider;

/**
 * Defines the methods for classes that have a provider. 
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.34
 */
public interface ProviderImplementation
{
	/**
	 * This function returns the provider.
	 * 
	 * @return	The provider
	 */
	public Provider getProvider();
}