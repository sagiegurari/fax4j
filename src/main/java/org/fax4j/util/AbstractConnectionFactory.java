package org.fax4j.util;

import org.fax4j.FaxException;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.Logger;
import org.fax4j.common.LoggerManager;

/**
 * This class provides the basic common connection factory capabilities.
 * 
 * @param	<T>
 * 			The resource type
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.39a
 */
public abstract class AbstractConnectionFactory<T> implements ConnectionFactory<T>
{
	/**The logger*/
	protected final Logger LOGGER;
	/**The configuration holder*/
	protected ConfigurationHolder factoryConfigurationHolder;

	/**
	 * This is the class constructor.
	 */
	public AbstractConnectionFactory()
	{
		super();
		
		//get logger
		LoggerManager loggerManager=LoggerManager.getInstance();
		this.LOGGER=loggerManager.getLogger();
	}

	/**
	 * This function initializes the connection factory.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder
	 */
	public final synchronized void initialize(ConfigurationHolder configurationHolder)
	{
		//validate not initialized before
		if(this.factoryConfigurationHolder!=null)
		{
			throw new FaxException("Connection Factory already initialized");
		}
		this.factoryConfigurationHolder=configurationHolder;
		
		//initialize factory specific
		this.initializeImpl();
	}

	/**
	 * Creates and returns the connection to be used.
	 *  
	 * @return	The connection
	 */
	public Connection<T> createConnection()
	{
		//log info
		this.LOGGER.logInfo(new Object[]{"Opening a new connection from factory type: ",this.getClass().getName()},null);

		//create resource
		T resource=this.createResourceImpl();
		if(resource==null)
		{
			throw new FaxException("Unable to create resource.");
		}

		//create connection
		Connection<T> connection=this.createConnectionForResource(resource);

		//register connection
		CloseableResourceManager.registerCloseable(connection);

		//log info
		this.LOGGER.logInfo(new Object[]{"New connection opened."},null);
		
		return connection;
	}

	/**
	 * Releases the connection.
	 *  
	 * @param	connection
	 * 			The connection
	 */
	public void releaseConnection(Connection<T> connection)
	{
		if(connection!=null)
		{
			try
			{
				//log info
				this.LOGGER.logInfo(new Object[]{"Closing connection using factory type: ",this.getClass().getName()},null);

				//get resource
				T resource=connection.getResource();
				
				//release resource
				this.releaseResource(resource);
				
				//unregister connection
				CloseableResourceManager.unregisterCloseable(connection);

				//log info
				this.LOGGER.logInfo(new Object[]{"Connection closed."},null);
			}
			catch(Exception exception)
			{
				//log error
				this.LOGGER.logInfo(new Object[]{"Unable to close connection."},exception);
			}
		}
	}

	/**
	 * Creates and returns the connection to be used.
	 *  
	 * @param	resource
	 * 			The resource held by the connection
	 * @return	The connection
	 */
	protected final Connection<T> createConnectionForResource(T resource)
	{
		if(resource==null)
		{
			throw new FaxException("Connection resource not provided.");
		}

		//create new connection
		Connection<T> connection=new ConnectionImpl<T>(resource,this);
		
		return connection;
	}

	/**
	 * Releases the resource from the connection.
	 *  
	 * @param	resource
	 * 			The resource
	 */
	protected void releaseResource(T resource)
	{
		//release resource
		if(resource!=null)
		{
			this.releaseResourceImpl(resource);
		}
	}

	/**
	 * This function initializes the connection factory.
	 */
	protected abstract void initializeImpl();

	/**
	 * Creates and returns the resource.
	 *  
	 * @return	The resource
	 */
	protected abstract T createResourceImpl();

	/**
	 * Releases the resource from the connection.
	 *  
	 * @param	resource
	 * 			The resource
	 */
	protected abstract void releaseResourceImpl(T resource);
}