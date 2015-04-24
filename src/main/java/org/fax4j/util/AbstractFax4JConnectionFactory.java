package org.fax4j.util;

import org.fax4j.Provider;
import org.fax4j.common.Fax4JProvider;

/**
 * This class provides the basic common connection factory capabilities.
 * This class should only be used by internal fax4j connection factories.
 * 
 * @param	<T>
 * 			The resource type
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.39a
 */
public abstract class AbstractFax4JConnectionFactory<T> extends AbstractConnectionFactory<T>
{
	/**
	 * This is the class constructor.
	 */
	public AbstractFax4JConnectionFactory()
	{
		super();
	}
	/**
	 * This function returns the provider.
	 * 
	 * @return	The provider
	 */
	public final Provider getProvider()
	{
		return Fax4JProvider.FAX4J_PROVIDER;
	}
}