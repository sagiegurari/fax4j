package org.fax4j.spi.hylafax;

import gnu.hylafax.HylaFAXClient;
import org.fax4j.FaxException;

/**
 * Provides the default implementation of the HaylaFAX connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.39a
 */
public class HylaFAXClientConnectionFactoryImpl extends AbstractHylaFAXClientConnectionFactory
{
	/**
	 * This is the class constructor.
	 */
	public HylaFAXClientConnectionFactoryImpl()
	{
		super();
	}
	
	/**
	 * This function creates and returns the HylaFax client.
	 * 
	 * @return	The HylaFax client
	 */
	protected HylaFAXClient createHylaFAXClient()
	{
		return new HylaFAXClient();
	}

	/**
	 * Creates and returns the resource.
	 *  
	 * @return	The resource
	 */
	@Override
	protected HylaFAXClient createResourceImpl()
	{
		HylaFAXClient client=null;
		try
		{
			//create new instance
			client=this.createHylaFAXClient();
			
			//open host
			client.open(this.host,this.port);
			
			//set user
			client.user(this.userName);
			
			//set transfer mode and type
			client.mode(this.mode);
			client.type(this.type);
			
			//set password
			if(this.password!=null)
			{
				client.pass(this.password);
			}
			
			if(this.enableAdminOperations)
			{
				//enable admin operations
				client.admin(this.password);
			}
		}
		catch(RuntimeException exception)
		{
			//release client
			this.releaseResource(client);

			throw exception;
		}
		catch(Exception exception)
		{
			//release client
			this.releaseResource(client);

			throw new FaxException("General error.",exception);
		}

		return client;
	}

	/**
	 * Releases the resource from the connection.
	 *  
	 * @param	resource
	 * 			The resource
	 */
	@Override
	protected void releaseResourceImpl(HylaFAXClient resource)
	{
		try
		{
			//release client
			resource.quit();
		}
		catch(Exception exception)
		{
			//log error
			this.LOGGER.logDebug(new Object[]{"Error while closing client."},exception);
		}
	}
}