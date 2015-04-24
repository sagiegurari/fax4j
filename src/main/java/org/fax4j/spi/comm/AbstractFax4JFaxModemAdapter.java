package org.fax4j.spi.comm;

import org.fax4j.Provider;
import org.fax4j.common.Fax4JProvider;

/**
 * This class provides partial/common functionlity of the fax modem adapter interface.<br>
 * This class should be used only by <b>internal</b> fax4j implementation and not
 * by implementations written by external vendors.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.38
 */
public abstract class AbstractFax4JFaxModemAdapter extends AbstractFaxModemAdapter
{
	/**
	 * This is the class constructor.
	 */
	public AbstractFax4JFaxModemAdapter()
	{
		super();
	}

	/**
	 * This function returns the provider.
	 * 
	 * @return	The provider
	 */
	public final Provider getProvider()
	{
		return Fax4JProvider.FAX4J_PROVIDER;
	}
}