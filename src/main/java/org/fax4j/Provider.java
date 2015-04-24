package org.fax4j;

/**
 * This is the provider interface.<br>
 * Each SPI enables access to the SPI provider by returning
 * an instance of this interface.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.17
 */
public interface Provider
{
	/**
	 * This function returns the provider name, 
	 * for example mycompany.
	 * 
	 * @return	The provider name
	 */
	public String getName();

	/**
	 * This function returns the provider description.
	 * 
	 * @return	The provider description
	 */
	public String getDescription();
}