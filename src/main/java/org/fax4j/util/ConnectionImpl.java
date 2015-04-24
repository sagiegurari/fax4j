package org.fax4j.util;

import org.fax4j.FaxException;

/**
 * This class provides the basic common connection implementation.
 * 
 * @param	<T>
 * 			The resource type
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.39a
 */
public class ConnectionImpl<T> extends AbstractCloseable implements Connection<T>
{
	/**The resource held by this connection*/
	private final T RESOURCE;
	/**The owner connection factory*/
	private final ConnectionFactory<T> CONNECTION_FACTORY;

	/**
	 * This is the class constructor.
	 * 
	 * @param	resource
	 * 			The resource held by this connection
	 * @param	connectionFactory
	 * 			The owner connection factory
	 */
	public ConnectionImpl(T resource,ConnectionFactory<T> connectionFactory)
	{
		super();
		
		if(resource==null)
		{
			throw new FaxException("Connection resource not provided.");
		}
		if(connectionFactory==null)
		{
			throw new FaxException("Owner connection factory not provided.");
		}
		
		//get resource
		this.RESOURCE=resource;

		//get connection factory
		this.CONNECTION_FACTORY=connectionFactory;
	}
	
	/**
	 * Closes the connection.
	 */
	@Override
	protected final void closeImpl()
	{
		this.CONNECTION_FACTORY.releaseConnection(this);
	}

	/**
	 * This function returns the resource held by the connection.
	 * 
	 * @return	The resource held by the connection
	 */
	public final T getResource()
	{
		return this.RESOURCE;
	}
}