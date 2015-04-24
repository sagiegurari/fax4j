package org.fax4j.common;

import org.fax4j.Provider;

/**
 * This is the internal provider used by the internal fax4j
 * SPI.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.17
 */
public final class Fax4JProvider extends ProviderImpl
{
	/**The fax4j provider*/
	public static final Provider FAX4J_PROVIDER=new Fax4JProvider();
	
	/**
	 * This is the class constructor.
	 */
	public Fax4JProvider()
	{
		super("fax4j","Internal fax4j SPI.");
	}
}