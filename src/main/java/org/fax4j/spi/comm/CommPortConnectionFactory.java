package org.fax4j.spi.comm;

import org.fax4j.util.ConnectionFactory;

/**
 * This class defines the COMM port connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.34
 */
public interface CommPortConnectionFactory extends ConnectionFactory<CommPortAdapter>
{
	//empty (everything defined in parent interfaces)
}