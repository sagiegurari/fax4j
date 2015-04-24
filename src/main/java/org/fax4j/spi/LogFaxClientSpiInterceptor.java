package org.fax4j.spi;

import java.lang.reflect.Method;
import org.fax4j.common.Logger;
import org.fax4j.spi.FaxClientSpiProxy.FaxClientSpiProxyEventType;

/**
 * This class provides the logging implementation of the fax client SPI interceptor which enables
 * to log all fax client SPI method invocations.
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
 * @version 1.04
 * @since	0.17
 */
public class LogFaxClientSpiInterceptor extends AbstractFaxClientSpiInterceptor
{
	/**
	 * This is the default constructor.
	 */
	public LogFaxClientSpiInterceptor()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI interceptor.
	 */
	@Override
	protected void initializeImpl()
	{
		//empty implementation
	}
	
	/**
	 * This function logs the event.
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
	protected void logEvent(FaxClientSpiProxyEventType eventType,Method method,Object[] arguments,Object output,Throwable throwable)
	{
		//init log data
		int amount=3;
		int argumentsAmount=0;
		if(arguments!=null)
		{
			argumentsAmount=arguments.length;
		}
		if(eventType==FaxClientSpiProxyEventType.POST_EVENT_TYPE)
		{
			amount=amount+2;
		}
		Object[] logData=new Object[amount+argumentsAmount];
		
		//set log data values
		switch(eventType)
		{
			case PRE_EVENT_TYPE:
				logData[0]="Invoking ";
				break;
			case POST_EVENT_TYPE:
				logData[0]="Invoked ";
				break;
			case ERROR_EVENT_TYPE:
				logData[0]="Error while invoking ";
				break;
		}
		logData[1]=method.getName();
		if(argumentsAmount>0)
		{
			logData[2]=" with data: ";
			System.arraycopy(arguments,0,logData,3,argumentsAmount);
		}
		else
		{
			logData[2]="";
		}
		if(eventType==FaxClientSpiProxyEventType.POST_EVENT_TYPE)
		{
			logData[3+argumentsAmount]="\nOutput: ";
			logData[4+argumentsAmount]=output;
		}

		//get logger
		Logger logger=this.getLogger();
		
		//log event
		switch(eventType)
		{
			case PRE_EVENT_TYPE:
			case POST_EVENT_TYPE:
				logger.logDebug(logData,throwable);
				break;
			case ERROR_EVENT_TYPE:
				logger.logError(logData,throwable);
				break;
		}
	}
	
	/**
	 * This function is invoked by the fax client SPI proxy before invoking
	 * the method in the fax client SPI itself.
	 * 
	 * @param 	method
	 * 			The method invoked
	 * @param 	arguments
	 * 			The method arguments
	 */
	public final void preMethodInvocation(Method method,Object[] arguments)
	{
		this.logEvent(FaxClientSpiProxyEventType.PRE_EVENT_TYPE,method,arguments,null,null);
	}
	
	/**
	 * This function is invoked by the fax client SPI proxy after invoking
	 * the method in the fax client SPI itself.
	 * 
	 * @param 	method
	 * 			The method invoked
	 * @param 	arguments
	 * 			The method arguments
	 * @param 	output
	 * 			The method output
	 */
	public final void postMethodInvocation(Method method,Object[] arguments,Object output)
	{
		this.logEvent(FaxClientSpiProxyEventType.POST_EVENT_TYPE,method,arguments,output,null);
	}
	
	/**
	 * This function is invoked by the fax client SPI proxy in of an error
	 * while invoking the method in the fax client SPI itself.
	 * 
	 * @param 	method
	 * 			The method invoked
	 * @param 	arguments
	 * 			The method arguments
	 * @param	throwable
	 * 			The throwable while invoking the method
	 */
	public final void onMethodInvocationError(Method method,Object[] arguments,Throwable throwable)
	{
		this.logEvent(FaxClientSpiProxyEventType.ERROR_EVENT_TYPE,method,arguments,null,throwable);
	}
}