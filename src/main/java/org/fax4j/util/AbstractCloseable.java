package org.fax4j.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Defines a basic closable class which prevents errors from being thrown during
 * the close invocation.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.33
 */
public abstract class AbstractCloseable implements Closeable
{
	/**
	 * This is the class constructor.
	 */
	protected AbstractCloseable()
	{
		super();
	}
	
	/**
	 * Closes the connection. 
	 * 
	 * @throws 	IOException
	 * 			Never thrown
	 */
	public final synchronized void close() throws IOException
	{
		try
		{
			//close connection
			this.closeImpl();
		}
		catch(Exception exception)
		{
			//ignore
		}
	}
	
	/**
	 * Closes the connection.
	 */
	protected abstract void closeImpl();
}