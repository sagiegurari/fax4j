package org.fax4j.spi.email;

import org.fax4j.util.ConnectionFactory;

/**
 * This class defines the mail connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.39a
 */
public interface MailConnectionFactory extends ConnectionFactory<MailResourcesHolder>
{
	//empty (everything defined in parent interfaces)
}