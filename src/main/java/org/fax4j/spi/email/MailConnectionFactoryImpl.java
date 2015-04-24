package org.fax4j.spi.email;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import org.fax4j.FaxException;

/**
 * This class provides basic implementation of the mail connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.39a
 */
public class MailConnectionFactoryImpl extends AbstractMailConnectionFactory
{
	/**
	 * This is the class constructor.
	 */
	public MailConnectionFactoryImpl()
	{
		super();
	}

	/**
	 * Creates and returns the resource.
	 *  
	 * @return	The resource
	 */
	@Override
	protected MailResourcesHolder createResourceImpl()
	{
		//create authenticator
		Authenticator authenticator=null;
		if(this.password!=null)
		{
			authenticator=new MailAuthenticator(this.userName,this.password);
		}
		
		//create session
		Session session=Session.getInstance(this.mailConnectionProperties,authenticator);

		//create transport
		Transport transport=this.createTransport(session);
		
		//create holder
		MailResourcesHolder resource=new MailResourcesHolder(session,transport);
		
		return resource;
	}
	
	/**
	 * This function returns a transport for the provided session.
	 * 
	 * @param 	session
	 * 			The mail session
	 * @return	The mail transport
	 */
	protected Transport createTransport(Session session)
	{
		Transport transport=null;
		try
		{
			//get the transport
			transport=session.getTransport(this.transportProtocol);

			//connect
			if(this.transportPort>0)
			{
				transport.connect(this.transportHost,this.transportPort,this.userName,this.password);
			}
			else
			{
				transport.connect(this.transportHost,this.userName,this.password);
			}
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while connecting to mail host: "+this.transportHost,exception);
		}
		
		return transport;
	}

	/**
	 * Releases the resource from the connection.
	 *  
	 * @param	resource
	 * 			The resource
	 */
	@Override
	protected void releaseResourceImpl(MailResourcesHolder resource)
	{
		//get transport
		Transport transport=resource.getTransport();

		if(transport!=null)
		{
			if(transport.isConnected())
			{
				try
				{
					//close connection
					transport.close();
				}		
				catch(Throwable throwable)
				{
					//ignore
				}
			}
		}
	}
}