package org.fax4j.util;

import java.io.Closeable;

/**
 * This class defines basic common connection capabilities.
 * 
 * @param	<T>
 * 			The resource type
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.1
 */
public interface Connection<T> extends Closeable
{
	/**
	 * This function returns the resource held by the connection.
	 * 
	 * @return	The resource held by the connection
	 */
	public T getResource();
}