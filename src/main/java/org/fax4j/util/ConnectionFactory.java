package org.fax4j.util;

import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.ProviderImplementation;

/**
 * This class defines the basic common connection factory capabilities.
 * 
 * @param	<T>
 * 			The resource type
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.39a
 */
public interface ConnectionFactory<T> extends ProviderImplementation
{
	/**
	 * This function initializes the connection factory.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder
	 */
	public void initialize(ConfigurationHolder configurationHolder);

	/**
	 * Creates and returns the connection to be used.
	 *  
	 * @return	The connection
	 */
	public Connection<T> createConnection();

	/**
	 * Releases the connection.
	 *  
	 * @param	connection
	 * 			The connection
	 */
	public void releaseConnection(Connection<T> connection);
}