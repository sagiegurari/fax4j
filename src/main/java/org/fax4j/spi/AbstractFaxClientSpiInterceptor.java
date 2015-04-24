package org.fax4j.spi;

import org.fax4j.FaxException;
import org.fax4j.common.Logger;

/**
 * This class provides partial implementation of the fax client SPI interceptor which enables
 * to invoke custom logic before and after the fax client SPI method
 * invocations (such as logging).
 * <br>
 * The configuration of the fax4j framework is made up of 3 layers.<br>
 * The configuration is based on simple properties.<br>
 * Each layer overrides the lower layers by adding/changing the property values.<br>
 * The first layer is the internal fax4j.properties file located in the fax4j jar.<br>
 * This layer contains the preconfigured values for the fax4j framework and can be changed
 * by updating these properties in the higher layers.<br>
 * The second layer is the external fax4j.properties file that is located on the classpath.<br>
 * This file is optional and provides the ability to override the internal configuration for the
 * entire fax4j framework.<br>
 * The top most layer is the optional java.util.Properties object provided by the external classes
 * when creating a new fax client.<br>
 * These properties enable to override the configuration of the lower 2 layers.<br>
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
 * 		<td>org.fax4j.proxy.interceptor.list</td>
 * 		<td>A list of interceptor types, separated by a ';' character.<br>
 * 			Each type must have a matching org.fax4j.proxy.interceptor.type.xxx property.
 * 		</td>
 * 		<td>log</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.proxy.interceptor.type.xxx</td>
 * 		<td>The interceptor type to class name mapping.</td>
 * 		<td>org.fax4j.proxy.interceptor.type.log=org.fax4j.spi.LogFaxClientSpiInterceptor</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * </table>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.17
 */
public abstract class AbstractFaxClientSpiInterceptor implements FaxClientSpiInterceptor
{
	/**The initialized flag*/
	private boolean initialized;
	/**The fax client SPI*/
	private FaxClientSpi faxClientSpiInstance;
	
	/**
	 * This is the default constructor.
	 */
	public AbstractFaxClientSpiInterceptor()
	{
		super();
		
		//set flag
		this.initialized=false;
	}

	/**
	 * This function initializes the fax client SPI interceptor.<br>
	 * This method is called by the FaxClientSpiFactory.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 */
	public final void initialize(FaxClientSpi faxClientSpi)
	{
		if(this.initialized)
		{
			throw new FaxException("Fax client SPI interceptor already initialized.");
		}
		
		//set flag
		this.initialized=true;
		
		//get configuration
		this.faxClientSpiInstance=faxClientSpi;
		
		//initialize
		this.initializeImpl();
	}
	
	/**
	 * This function returns the fax client SPI.
	 *  
	 * @return	The fax client SPI
	 */
	protected final FaxClientSpi getFaxClientSpi()
	{
		return this.faxClientSpiInstance;
	}
	
	/**
	 * Returns the value from the SPI configuration based on the provided 
	 * configuration key.
	 *  
	 * @param 	key
	 * 			The configuration key
	 * @return	The value
	 */
	public final String getConfigurationValue(String key)
	{
		return this.faxClientSpiInstance.getConfigurationValue(key);
	}
	
	/**
	 * Returns the internal logger.
	 *  
	 * @return	The internal logger
	 */
	public final Logger getLogger()
	{
		return this.faxClientSpiInstance.getLogger();
	}
	
	/**
	 * This function initializes the fax client SPI interceptor.
	 */
	protected abstract void initializeImpl();
}