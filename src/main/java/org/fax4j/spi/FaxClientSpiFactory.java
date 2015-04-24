package org.fax4j.spi;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.ProductInfo;
import org.fax4j.Provider;
import org.fax4j.common.Logger;
import org.fax4j.common.LoggerManager;
import org.fax4j.util.LibraryConfigurationLoader;
import org.fax4j.util.ReflectionHelper;
import org.fax4j.util.SpiUtil;

/**
 * This is the fax client SPI factory which is used to create
 * new instances of the fax client SPI.<br>
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
 * Below table describes the configuration values relevant for this class,
 * for SPI specific configuration, see the relevant SPI class javadoc.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.type.map.xxx</td>
 * 		<td>For each SPI type, there is a property that (with prefix: org.fax4j.spi.type.map. 
 * 			and type name for example: org.fax4j.spi.type.map.adapter) which defines the SPI class name
 * 			for the given SPI type.<br>
 * 			For example:<br>
 * 			org.fax4j.spi.type.map.adapter=org.fax4j.spi.adapter.AdapterFaxClientSpi<br>
 * 			Means that for SPI type adapter, the SPI class to be used is org.fax4j.spi.adapter.AdapterFaxClientSpi</td>
 * 		<td>
 * 			org.fax4j.spi.type.map.adapter=org.fax4j.spi.adapter.AdapterFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.windows=org.fax4j.spi.windows.WindowsFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.vbs=org.fax4j.spi.vbs.VBSFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.mail=org.fax4j.spi.email.MailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.http=org.fax4j.spi.http.HTTPFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.process=org.fax4j.spi.process.ProcessFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.linux=org.fax4j.spi.linux.LinuxFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.mac=org.fax4j.spi.mac.MacFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.hylafax=org.fax4j.spi.hylafax.HylaFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.comm=org.fax4j.spi.comm.CommFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.interfax=org.fax4j.spi.interfax.InterfaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.nextivafax=org.fax4j.spi.nextivafax.NextivafaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.send2fax=org.fax4j.spi.send2fax.Send2FaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.metrofax=org.fax4j.spi.metrofax.MetroFaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.faxage=org.fax4j.spi.faxage.FaxAgeMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.extremefax=org.fax4j.spi.extremefax.ExtremeFaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.efax=org.fax4j.spi.efax.EFaxMailFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.phaxio=org.fax4j.spi.phaxio.PhaxioFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.hoiio=org.fax4j.spi.hoiio.HoiioFaxClientSpi<br>
 * 			org.fax4j.spi.type.map.rfax=org.fax4j.spi.java4less.RFaxFaxClientSpi
 * 		</td>
 * 		<td>NA</td>
 * 		<td>NA</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.default.type</td>
 * 		<td>In case the SPI type was not provided in the createFaxClientSpi method, the type will be taken from
 * 			this property value.</td>
 * 		<td>adapter</td>
 * 		<td>none</td>
 * 		<td>false</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.logger.class.name</td>
 * 		<td>Defines the internal fax4j logger class name.<br>
 * 			The logger must implement the org.fax4j.common.Logger interface and have an empty constructor.<br>
 * 			This property can only be set in the internal fax4j.properties and the fax4j.properties on the classpath,
 * 			providing these values as part of the java.util.Properties input of the createFaxClientSpi will have no affect.
 * 		</td>
 * 		<td>org.fax4j.common.SimpleLogger</td>
 * 		<td>org.fax4j.common.SimpleLogger</td>
 * 		<td>false</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.logger.log.level</td>
 * 		<td>Defines the internal fax4j logger log level.<br>
 * 			The possible values are: DEBUG, INFO and ERROR.<br>
 * 			This property can only be set in the internal fax4j.properties and the fax4j.properties on the classpath,
 * 			providing these values as part of the java.util.Properties input of the createFaxClientSpi will have no affect.
 * 		</td>
 * 		<td>ERROR</td>
 * 		<td>ERROR</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.monitor.class.name</td>
 * 		<td>Defines the internal fax job monitor class name.<br>
 * 			This property can only be set in the internal fax4j.properties and the fax4j.properties on the classpath,
 * 			providing these values as part of the java.util.Properties input of the createFaxClientSpi will have no affect.
 * 		</td>
 * 		<td>org.fax4j.spi.FaxJobMonitorImpl</td>
 * 		<td>org.fax4j.spi.FaxJobMonitorImpl</td>
 * 		<td>false</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.proxy.enabled</td>
 * 		<td>True to enable the fax client SPI proxy.</td>
 * 		<td>true</td>
 * 		<td>true</td>
 * 		<td>false</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.proxy.class.name</td>
 * 		<td>Defines the fax client SPI proxy class name.</td>
 * 		<td>org.fax4j.spi.FaxClientSpiProxyImpl</td>
 * 		<td>org.fax4j.spi.FaxClientSpiProxyImpl</td>
 * 		<td>false</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.proxy.interceptor.list</td>
 * 		<td>A list of interceptor types, separated by a ';' character.<br>
 * 			Each type must have a matching org.fax4j.proxy.interceptor.type.xxx property.
 * 		</td>
 * 		<td>log</td>
 * 		<td>none</td>
 * 		<td>false</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.proxy.interceptor.type.xxx</td>
 * 		<td>The interceptor type to class name mapping.</td>
 * 		<td>org.fax4j.proxy.interceptor.type.log=org.fax4j.spi.LogFaxClientSpiInterceptor</td>
 * 		<td>none</td>
 * 		<td>false</td>
 *	</tr>
 * </table>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.19
 * @since	0.1
 */
public final class FaxClientSpiFactory
{
	/**The logger*/
	private static Logger logger;
	/**The fax job monitor*/
	private static FaxJobMonitor faxJobMonitor;
	/**The fax job monitor class name property key*/
	public static final String FAX_JOB_MONITOR_CLASS_NAME_PROPERTY_KEY="org.fax4j.monitor.class.name";
	/**The default SPI type property key*/
	public static final String DEFAULT_SPI_TYPE_PROPERTY_KEY="org.fax4j.spi.default.type";
	/**The SPI class name to type mapping property key prefix*/
	public static final String SPI_CLASS_NAME_TYPE_MAP_PROPERTY_KEY_PREFIX="org.fax4j.spi.type.map.";
	/**The SPI proxy enabled property key*/
	public static final String SPI_PROXY_ENABLED_PROPERTY_KEY="org.fax4j.proxy.enabled";
	/**The SPI proxy class name property key*/
	public static final String SPI_PROXY_CLASS_NAME_PROPERTY_KEY="org.fax4j.proxy.class.name";
	/**The SPI interceptor list property key*/
	public static final String SPI_INTERCEPTOR_LIST_PROPERTY_KEY="org.fax4j.proxy.interceptor.list";
	/**The SPI interceptor type property key prefix*/
	public static final String SPI_INTERCEPTOR_TYPE_PROPERTY_KEY_PREFIX="org.fax4j.proxy.interceptor.type.";
	/**The SPI interceptor list separator ';'*/
	public static final String SPI_INTERCEPTOR_LIST_SEPARATOR=";";
	
	/**
	 * This is the default constructor.
	 */
	private FaxClientSpiFactory()
	{
		super();
	}

	/**
	 * This function creates a new fax client SPI based on the provided configuration.<br>
	 * This is an internal framework method and should not be invoked by classes outside the
	 * fax4j framework.
	 *  
	 * @param	type
	 * 			The fax client type (may be null for default type)
	 * @param	configuration
	 * 			The fax client configuration (may be null)
	 * @return	The fax client SPI instance
	 */
	public static FaxClientSpi createFaxClientSpi(String type,Properties configuration)
	{
		//create fax client SPI
		FaxClientSpi faxClientSpi=FaxClientSpiFactory.createFaxClientSpiImpl(type,configuration,false);

		return faxClientSpi;
	}

	/**
	 * This function creates a new fax client SPI based on the provided configuration.<br>
	 * This is an internal framework method and should not be invoked by classes outside the
	 * fax4j framework.
	 *  
	 * @param	type
	 * 			The fax client type (may be null for default type)
	 * @param	configuration
	 * 			The fax client configuration (may be null)
	 * @return	The fax client SPI instance
	 */
	public static FaxClientSpi createChildFaxClientSpi(String type,Properties configuration)
	{
		//create fax client SPI
		FaxClientSpi faxClientSpi=FaxClientSpiFactory.createFaxClientSpiImpl(type,configuration,true);

		return faxClientSpi;
	}

	/**
	 * This function creates a new fax client SPI based on the provided configuration.<br>
	 * This is an internal framework method and should not be invoked by classes outside the
	 * fax4j framework.
	 *  
	 * @param	type
	 * 			The fax client type (may be null for default type)
	 * @param	configuration
	 * 			The fax client configuration (may be null)
	 * @param	childSPI
	 * 			True if this is a child SPI of another SPI
	 * @return	The fax client SPI instance
	 */
	private static FaxClientSpi createFaxClientSpiImpl(String type,Properties configuration,boolean childSPI)
	{
		//get layered configuration
		Map<String,String> layeredConfiguration=FaxClientSpiFactory.getConfiguration(configuration);

		String updatedType=type;
		if((updatedType==null)||(updatedType.length()==0))
		{
			//get default type
			updatedType=layeredConfiguration.get(FaxClientSpiFactory.DEFAULT_SPI_TYPE_PROPERTY_KEY);

			if((updatedType==null)||(updatedType.length()==0))
			{
				throw new FaxException("Type value not provided and not configured in fax4j.properties");
			}
		}
		
		//get SPI class name
		String key=FaxClientSpiFactory.SPI_CLASS_NAME_TYPE_MAP_PROPERTY_KEY_PREFIX+updatedType;
		String className=layeredConfiguration.get(key);
		if((className==null)||(className.length()==0))
		{
			throw new FaxException("Class name for type: "+updatedType+" not configured in fax4j.properties");
		}

		//get logger
		Logger spiLogger=FaxClientSpiFactory.getLogger();

		//get fax job monitor
		FaxJobMonitor spiFaxJobMonitor=FaxClientSpiFactory.getFaxJobMonitor();

		//create new instance
		FaxClientSpi faxClientSpi=(FaxClientSpi)ReflectionHelper.createInstance(className);

		//initialize SPI
		faxClientSpi.initialize(layeredConfiguration,spiLogger,spiFaxJobMonitor);

		FaxClientSpi faxClientSpiWrapper=faxClientSpi;
		if(!childSPI)
		{
			//check if proxy is enabled
			String valueStr=faxClientSpi.getConfigurationValue(FaxClientSpiFactory.SPI_PROXY_ENABLED_PROPERTY_KEY);
			if(valueStr==null)
			{
				valueStr=Boolean.toString(true);
			}
			boolean enableProxy=Boolean.parseBoolean(valueStr);
	
			if(enableProxy)
			{
				//create fax client SPI proxy
				FaxClientSpiProxy faxClientSpiProxy=FaxClientSpiFactory.createFaxClientSpiProxy(faxClientSpi);
				
				//create proxy
				ClassLoader classLoader=ReflectionHelper.getThreadContextClassLoader();
				faxClientSpiWrapper=(FaxClientSpi)Proxy.newProxyInstance(classLoader,new Class[]{FaxClientSpi.class},faxClientSpiProxy);
			}
		}
		
		//validate provider
		Provider provider=faxClientSpiWrapper.getProvider();
		if(provider==null)
		{
			throw new FaxException("Provider information not available in SPI.");
		}
		String name=provider.getName();
		if(name==null)
		{
			throw new FaxException("Provider name not available in SPI.");
		}

		return faxClientSpiWrapper;
	}

	/**
	 * Returns the configuration to be used by the new SPI instance.<br>
	 * The configuration of the fax client SPI is made up of 3 layers.
	 * The lowest layer is the internal fax4j.properties file located in the fax4j jar.
	 * This configuration file contains the initial default configuration.
	 * The next layer is the external fax4j.properties file that is located on the classpath.
	 * This file is optional and provides the ability to override the internal configuration.
	 * The top most layer is the optional properties object provided by the external classes.
	 * These properties enable to override the configuration of the lower 2 layers.
	 *  
	 * @param	configuration
	 * 			The fax client configuration (may be null)
	 * @return	The fax client layered configuration
	 */
	protected static Map<String,String> getConfiguration(Properties configuration)
	{
		//get system configuration
		Map<String,String> systemConfig=LibraryConfigurationLoader.getSystemConfiguration();
		
		//create new map
		Map<String,String> layeredConfiguration=new HashMap<String,String>(systemConfig);
		if(configuration!=null)
		{
			//convert to map
			SpiUtil.copyPropertiesToMap(configuration,layeredConfiguration);
		}
		
		return layeredConfiguration;
	}
	
	/**
	 * This function returns the internal logger for the fax4j framework.
	 *  
	 * @return	The logger
	 */
	private static Logger getLogger()
	{
		if(FaxClientSpiFactory.logger==null)
		{
			synchronized(FaxClientSpiFactory.class)
			{
				if(FaxClientSpiFactory.logger==null)
				{
					//get logger manager
					LoggerManager loggerManager=LoggerManager.getInstance();

					//get logger
					Logger localLogger=loggerManager.getLogger();
					
					//print product info
					localLogger.logDebug(new Object[]{ProductInfo.getProductInfoPrintout()},null);
					
					//keep reference
					FaxClientSpiFactory.logger=localLogger;
				}
			}
		}

		return FaxClientSpiFactory.logger;
	}

	/**
	 * This function returns the fax job monitor.
	 *  
	 * @return	The fax job monitor
	 */
	private static FaxJobMonitor getFaxJobMonitor()
	{
		//get system configuration
		Map<String,String> systemConfig=LibraryConfigurationLoader.getSystemConfiguration();
		
		if(FaxClientSpiFactory.faxJobMonitor==null)
		{
			synchronized(FaxClientSpiFactory.class)
			{
				if(FaxClientSpiFactory.faxJobMonitor==null)
				{
					//create fax job monitor
					FaxJobMonitor localFaxJobMonitor=FaxClientSpiFactory.createFaxJobMonitor(systemConfig);
					
					//get logger
					Logger localLogger=FaxClientSpiFactory.getLogger();

					//initialize
					localFaxJobMonitor.initialize(systemConfig,localLogger);

					//keep reference
					FaxClientSpiFactory.faxJobMonitor=localFaxJobMonitor;
				}
			}
		}

		return FaxClientSpiFactory.faxJobMonitor;
	}

	/**
	 * This function creates the fax job monitor used by the fax4j framework.
	 *  
	 * @param 	systemConfig
	 * 			The system configuration
	 * @return	The logger
	 */
	private static FaxJobMonitor createFaxJobMonitor(Map<String,String> systemConfig)
	{
		//get class name
		String className=systemConfig.get(FaxClientSpiFactory.FAX_JOB_MONITOR_CLASS_NAME_PROPERTY_KEY);
		if((className==null)||(className.length()==0))
		{
			className=FaxJobMonitorImpl.class.getName();
		}

		//create new instance
		FaxJobMonitor faxJobMonitorInstance=(FaxJobMonitor)ReflectionHelper.createInstance(className);

		return faxJobMonitorInstance;
	}

	/**
	 * This function creates the fax client SPI proxy.
	 *  
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @return	The fax client SPI proxy
	 */
	private static FaxClientSpiProxy createFaxClientSpiProxy(FaxClientSpi faxClientSpi)
	{
		//create fax client SPI interceptors
		FaxClientSpiInterceptor[] interceptors=FaxClientSpiFactory.createFaxClientSpiInterceptors(faxClientSpi);
		
		//get class name
		String className=faxClientSpi.getConfigurationValue(FaxClientSpiFactory.SPI_PROXY_CLASS_NAME_PROPERTY_KEY);
		if(className==null)
		{
			className=FaxClientSpiProxyImpl.class.getName();
		}

		//create new instance
		FaxClientSpiProxy faxClientSpiProxy=(FaxClientSpiProxy)ReflectionHelper.createInstance(className);
		
		//initialize
		faxClientSpiProxy.initialize(faxClientSpi,interceptors);
		
		return faxClientSpiProxy;
	}

	/**
	 * This function creates the fax client SPI interceptors.
	 *  
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @return	The interceptors
	 */
	private static FaxClientSpiInterceptor[] createFaxClientSpiInterceptors(FaxClientSpi faxClientSpi)
	{
		//get type list
		String typeListStr=faxClientSpi.getConfigurationValue(FaxClientSpiFactory.SPI_INTERCEPTOR_LIST_PROPERTY_KEY);
		FaxClientSpiInterceptor[] interceptors=null;
		if(typeListStr!=null)
		{
			//split list
			String[] types=typeListStr.split(FaxClientSpiFactory.SPI_INTERCEPTOR_LIST_SEPARATOR);
			
			//get amount
			int amount=types.length;
			
			String type=null;
			String propertyKey=null;
			String className=null;
			FaxClientSpiInterceptor interceptor=null;
			List<FaxClientSpiInterceptor> interceptorList=new LinkedList<FaxClientSpiInterceptor>();
			for(int index=0;index<amount;index++)
			{
				//get next type
				type=types[index];
				
				if((type!=null)&&(type.length()>0))
				{
					//get property key
					propertyKey=FaxClientSpiFactory.SPI_INTERCEPTOR_TYPE_PROPERTY_KEY_PREFIX+type;
					
					//get class name
					className=faxClientSpi.getConfigurationValue(propertyKey);
					
					if(className==null)
					{
						throw new FaxException("Class name not defined by property: "+propertyKey+" for SPI interceptor.");
					}

					//create new instance
					interceptor=(FaxClientSpiInterceptor)ReflectionHelper.createInstance(className);
					
					//initialize
					interceptor.initialize(faxClientSpi);
					
					//add to list
					interceptorList.add(interceptor);
				}
				
				//convert to array
				interceptors=interceptorList.toArray(new FaxClientSpiInterceptor[interceptorList.size()]);
			}
		}
		
		return interceptors;
	}
}