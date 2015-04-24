package org.fax4j.spi.comm;

import org.fax4j.util.AbstractCloseable;

/**
 * This class provides partial implementation of the basic and common adapter capabilities.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.39a
 */
public abstract class AbstractCommPortAdapter extends AbstractCloseable implements CommPortAdapter
{
	/**
	 * This is the class constructor.
	 */
	protected AbstractCommPortAdapter()
	{
		super();
	}
}