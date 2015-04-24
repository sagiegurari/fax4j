package org.fax4j.spi;

import java.lang.reflect.Method;
import org.fax4j.FaxException;
import org.fax4j.common.Logger;

/**
 * This class provides partial implementation of the fax client SPI proxy which enables
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
public abstract class AbstractFaxClientSpiProxy implements FaxClientSpiProxy
{
	/**The initialized flag*/
	private boolean initialized;
	/**The fax client SPI*/
	private FaxClientSpi faxClientSpiInstance;
	/**The fax client SPI interceptors*/
	private FaxClientSpiInterceptor[] faxClientSpiInterceptors;
	
	/**
	 * This is the default constructor.
	 */
	public AbstractFaxClientSpiProxy()
	{
		super();
		
		//set flag
		this.initialized=false;
	}

	/**
	 * This function initializes the fax client SPI proxy.<br>
	 * This method is called by the FaxClientSpiFactory.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @param	interceptors
	 * 			The fax client SPI interceptors (may be null)
	 */
	public final void initialize(FaxClientSpi faxClientSpi,FaxClientSpiInterceptor[] interceptors)
	{
		if(this.initialized)
		{
			throw new FaxException("Fax client SPI proxy already initialized.");
		}
		
		//set flag
		this.initialized=true;
		
		//get configuration
		this.faxClientSpiInstance=faxClientSpi;
		
		if(interceptors==null)
		{
			this.faxClientSpiInterceptors=new FaxClientSpiInterceptor[0];
		}
		else
		{
			//get amount
			int amount=interceptors.length;
			
			//init array
			this.faxClientSpiInterceptors=new FaxClientSpiInterceptor[amount];
			
			//copy array
			System.arraycopy(interceptors,0,this.faxClientSpiInterceptors,0,amount);
		}
		
		//initialize
		this.initializeImpl();
	}
	
	/**
	 * This function returns the fax client SPI interceptors.
	 * 
	 * @return	The fax client SPI interceptors
	 */
	protected final FaxClientSpiInterceptor[] getFaxClientSpiInterceptors()
	{
		return this.faxClientSpiInterceptors;
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
	 * This function is invoked for each fax client SPI method invocation.<br>
	 * This method invokes the fax client SPI interceptors.
	 * 
	 * @param 	proxy
	 * 			The proxy
	 * @param 	method
	 * 			The method invoked
	 * @param 	arguments
	 * 			The method arguments
	 * @return	The method output
	 * @throws 	Throwable
	 * 			Any throwable
	 */
	public Object invoke(Object proxy,Method method,Object[] arguments) throws Throwable
	{
		//invoke interceptors
		this.invokeInterceptors(FaxClientSpiProxyEventType.PRE_EVENT_TYPE,method,arguments,null,null);
		
		Object output=null;
		Throwable generalError=null;
		try
		{
			//invoke fax client SPI
			output=method.invoke(this.faxClientSpiInstance,arguments);

			//invoke interceptors
			this.invokeInterceptors(FaxClientSpiProxyEventType.POST_EVENT_TYPE,method,arguments,output,null);
		}
		catch(Throwable throwable)
		{
			//get error
			generalError=throwable.getCause();
			if(generalError==null)
			{
				generalError=throwable;
			}
			
			//invoke interceptors
			this.invokeInterceptors(FaxClientSpiProxyEventType.ERROR_EVENT_TYPE,method,arguments,null,generalError);
		}
		
		if(generalError!=null)
		{
			throw generalError;
		}

		return output;
	}
	
	/**
	 * This function initializes the fax client SPI proxy.
	 */
	protected abstract void initializeImpl();

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
	protected abstract void invokeInterceptors(FaxClientSpiProxyEventType eventType,Method method,Object[] arguments,Object output,Throwable throwable);
}