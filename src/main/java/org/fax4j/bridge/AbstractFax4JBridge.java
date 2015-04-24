package org.fax4j.bridge;

import org.fax4j.Provider;
import org.fax4j.common.Fax4JProvider;

/**
 * This class partial implementation of the fax bridge.<br>
 * Specific implementations may provide additional, more transport based capabilities.
 * <br>
 * Below table describes the configuration values relevant for this class.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.vendor.policy.class.name</td>
 * 		<td>The vendor policy class name.</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.40.6
 */
public abstract class AbstractFax4JBridge extends AbstractFaxBridge
{
	/**
	 * This is the class constructor.
	 */
	public AbstractFax4JBridge()
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