package org.fax4j.spi;

import java.lang.reflect.InvocationHandler;

/**
 * This interface defines the fax client SPI proxy which enables
 * to invoke interceptors before and after the fax client SPI method
 * invocations.<br>
 * With this proxy, it is possible to intercept all calls to the
 * fax client SPIs and perform custom logic such as logging.
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
 * 		<td>org.fax4j.proxy.enabled</td>
 * 		<td>True to enable the fax client SPI proxy.</td>
 * 		<td>true</td>
 * 		<td>true</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.proxy.class.name</td>
 * 		<td>Defines the fax client SPI proxy class name.</td>
 * 		<td>org.fax4j.spi.FaxClientSpiProxyImpl</td>
 * 		<td>org.fax4j.spi.FaxClientSpiProxyImpl</td>
 * 		<td>false</td>
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
 * @version 1.0
 * @since	0.17
 */
public interface FaxClientSpiProxy extends InvocationHandler
{
	/**
	 * This is the fax client SPI proxy event type.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.1
	 */
	public enum FaxClientSpiProxyEventType
	{
		/**The pre event type*/
		PRE_EVENT_TYPE,
		/**The post event type*/
		POST_EVENT_TYPE,
		/**The on error event type*/
		ERROR_EVENT_TYPE
	}

	/**
	 * This function initializes the fax client SPI proxy.<br>
	 * This method is called by the FaxClientSpiFactory.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @param	interceptors
	 * 			The interceptors (may be null)
	 */
	public void initialize(FaxClientSpi faxClientSpi,FaxClientSpiInterceptor[] interceptors);
}