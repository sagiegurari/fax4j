package org.fax4j.spi.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * This class implements the javax.mail.Authenticator
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.1
 */
public class MailAuthenticator extends Authenticator
{
	/**This is the password authentication*/
	private final transient PasswordAuthentication PASSWORD_AUTHENTICATION;

	/**
	 * This is the class constructor.
	 * 
	 * @param	userName
	 * 			The user name
	 * @param	password
	 * 			The password
	 */
	public MailAuthenticator(String userName,String password)
	{
		super();
		
		//init password authentication
		this.PASSWORD_AUTHENTICATION=new PasswordAuthentication(userName,password);
	}

	/**
	 * This funcion returns a password authentication.
	 * 
	 * @return	A password authentication
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return this.PASSWORD_AUTHENTICATION;
	}
}