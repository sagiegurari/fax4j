package org.fax4j.spi.hylafax;

import gnu.hylafax.HylaFAXClient;
import org.fax4j.util.ConnectionFactory;

/**
 * This interface defines the HaylaFAX connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.39a
 */
public interface HylaFAXClientConnectionFactory extends ConnectionFactory<HylaFAXClient>
{
	//empty (everything defined in parent interfaces)
}