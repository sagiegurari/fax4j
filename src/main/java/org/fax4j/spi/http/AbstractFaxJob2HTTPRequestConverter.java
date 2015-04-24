package org.fax4j.spi.http;

import org.fax4j.Provider;
import org.fax4j.common.AbstractService;

/**
 * This class provides partial implementation of the fax job to HTTP request converter interface.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.4
 */
public abstract class AbstractFaxJob2HTTPRequestConverter extends AbstractService implements FaxJob2HTTPRequestConverter
{
	/**
	 * This is the default constructor.
	 * 
	 * @param	provider
	 * 			The provider
	 */
	public AbstractFaxJob2HTTPRequestConverter(Provider provider)
	{
		super(provider);
	}
}