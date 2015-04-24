package org.fax4j.common;

import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.util.ReflectionHelper;

/**
 * The factory that creates, initializes and returns new service objects.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.42.4
 */
public final class ServiceFactory
{
	/**
	 * This is the default constructor.
	 */
	private ServiceFactory()
	{
		super();
	}
	
	/**
	 * This function creates, initializes and returns new service objects.
	 * 
	 * @param 	classNameKey
	 * 			The configuration key holding the service object class name
	 * @param 	defaultClassName
	 * 			The default service object class name if the value was not found in the configuration
	 * @param 	configurationHolder
	 * 			The configuration holder used to provide the configuration to the service
	 * @param 	propertyPart
	 * 			The service property part
	 * @return	The initialized service object
	 */
	public static Service createService(Enum<?> classNameKey,String defaultClassName,ConfigurationHolder configurationHolder,String propertyPart)
	{
		//validate input
		if(classNameKey==null)
		{
			throw new FaxException("Service class name key not provided.");
		}

		//convert to string
		String classNameKeyString=classNameKey.toString();
		
		//create service
		Service service=ServiceFactory.createService(classNameKeyString,defaultClassName,configurationHolder,propertyPart);
		
		return service;
	}
	
	/**
	 * This function creates, initializes and returns new service objects.
	 * 
	 * @param 	classNameKey
	 * 			The configuration key holding the service object class name
	 * @param 	defaultClassName
	 * 			The default service object class name if the value was not found in the configuration
	 * @param 	configurationHolder
	 * 			The configuration holder used to provide the configuration to the service
	 * @param 	propertyPart
	 * 			The service property part
	 * @return	The initialized service object
	 */
	public static Service createService(String classNameKey,String defaultClassName,ConfigurationHolder configurationHolder,String propertyPart)
	{
		//validate input
		if(configurationHolder==null)
		{
			throw new FaxException("Service configuration not provided.");
		}
		if(classNameKey==null)
		{
			throw new FaxException("Service class name key not provided.");
		}

		//get class name
		String className=configurationHolder.getConfigurationValue(classNameKey);
		if(className==null)
		{
			className=defaultClassName;
			if(className==null)
			{
				throw new FaxException("Service class name not found in configuration and no default value provided.");
			}
		}
		
		//get configuration
		Map<String,String> configuration=configurationHolder.getConfiguration();
		
		//create service
		Service service=(Service)ReflectionHelper.createInstance(className);
		
		//set property part
		service.setPropertyPart(propertyPart);
		
		//initialize service
		service.initialize(configuration);
		
		return service;
	}
}