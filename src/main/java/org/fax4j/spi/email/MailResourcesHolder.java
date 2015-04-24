package org.fax4j.spi.email;

import javax.mail.Session;
import javax.mail.Transport;

/**
 * This class holds the java mail objects.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.39a
 */
public class MailResourcesHolder
{
	/**This is the mail session*/
	private final Session SESSION;
	/**This is the mail transport*/
	private final Transport TRANSPORT;

	/**
	 * This is the default constructor.
	 * 
	 * @param	session
	 * 			The mail session
	 * @param	transport
	 * 			The mail transport
	 */
	public MailResourcesHolder(Session session,Transport transport)
	{
		super();
		
		//get values
		this.SESSION=session;
		this.TRANSPORT=transport;
	}

	/**
	 * This function returns the mail session.
	 *  
	 * @return	The mail session
	 */
	public final Session getSession()
	{
		return this.SESSION;
	}

	/**
	 * This function returns the mail transport.
	 *  
	 * @return	The mail transport
	 */
	public final Transport getTransport()
	{
		return this.TRANSPORT;
	}
}