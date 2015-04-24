package org.fax4j.spi;

import java.lang.reflect.Method;

/**
 * This class provides basic implementation of the fax client SPI proxy which enables
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
 * @version 1.02
 * @since	0.17
 */
public class FaxClientSpiProxyImpl extends AbstractFaxClientSpiProxy
{
	/**
	 * This is the default constructor.
	 */
	public FaxClientSpiProxyImpl()
	{
		super();
	}

	/**
	 * This function initializes the fax client SPI proxy.
	 */
	@Override
	protected void initializeImpl()
	{
		//empty implementation
	}

	/**
	 * This function invokes the interceptor for the given event.
	 * 
	 * @param 	eventType
	 * 			The event type
	 * @param 	method
	 * 			The method invoked
	 * @param 	arguments
	 * 			The method arguments
	 * @param 	output
	 * 			The method output
	 * @param	throwable
	 * 			The throwable while invoking the method
	 */
	@Override
	protected void invokeInterceptors(FaxClientSpiProxyEventType eventType,Method method,Object[] arguments,Object output,Throwable throwable)
	{
		try
		{
			this.invokeInterceptorsImpl(eventType,method,arguments,output,throwable);
		}
		catch(Throwable ignore)
		{
			//ignore
		}
	}
	
	/**
	 * This function invokes the interceptor for the given event.
	 * 
	 * @param 	eventType
	 * 			The event type
	 * @param 	method
	 * 			The method invoked
	 * @param 	arguments
	 * 			The method arguments
	 * @param 	output
	 * 			The method output
	 * @param	throwable
	 * 			The throwable while invoking the method
	 */
	protected void invokeInterceptorsImpl(FaxClientSpiProxyEventType eventType,Method method,Object[] arguments,Object output,Throwable throwable)
	{
		//get interceptors
		FaxClientSpiInterceptor[] interceptors=this.getFaxClientSpiInterceptors();
		
		//get interceptors amount
		int amount=interceptors.length;
		
		FaxClientSpiInterceptor interceptor=null;
		for(int index=0;index<amount;index++)
		{
			//get next interceptor
			interceptor=interceptors[index];
			
			//invoke interceptor
			if(eventType==FaxClientSpiProxyEventType.PRE_EVENT_TYPE)
			{
				interceptor.preMethodInvocation(method,arguments);
			}
			else if(eventType==FaxClientSpiProxyEventType.POST_EVENT_TYPE)
			{
				interceptor.postMethodInvocation(method,arguments,output);
			}
			else
			{
				interceptor.onMethodInvocationError(method,arguments,throwable);
			}
		}
	}
}