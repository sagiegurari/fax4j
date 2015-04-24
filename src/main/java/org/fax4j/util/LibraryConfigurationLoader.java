package org.fax4j.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.fax4j.FaxException;

/**
 * This is an internal utility used to load the library configuration.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.40.6
 */
public final class LibraryConfigurationLoader
{
	/**The system configuration*/
	private static Map<String,String> systemConfiguration;
	/**The configuration file name*/
	public static final String CONFIGURATION_FILE_NAME="fax4j.properties";

	/**
	 * This is the class constructor.
	 */
	private LibraryConfigurationLoader()
	{
		super();
	}

	/**
	 * Closes the provided resource.
	 *  
	 * @param 	resource
	 * 			The resource to close
	 */
	private static void closeResource(Closeable resource)
	{
		if(resource!=null)
		{
			try
			{
				//close resource
				resource.close();
			}
			catch(IOException exception)
			{
				//ignore
			}
		}
	}
	
	/**
	 * This function loads the properties from the input stream
	 * to the provided properties object.
	 * 
	 * @param 	properties
	 * 			The target properties object
	 * @param 	inputStream
	 * 			The input stream to the configuration file
	 * @param	internal
	 * 			True internal, else external
	 */
	private static void loadProperties(Properties properties,InputStream inputStream,boolean internal)
	{
		try
		{
			properties.load(inputStream);
			LibraryConfigurationLoader.closeResource(inputStream);
		}
		catch(Exception exception)
		{
			LibraryConfigurationLoader.closeResource(inputStream);
			String prefix="External";
			if(internal)
			{
				prefix="Internal";
			}
			throw new FaxException(prefix+" "+LibraryConfigurationLoader.CONFIGURATION_FILE_NAME+" not found.",exception);
		}
	}
	
	/**
	 * This function reads and returns the internal fax4j properties.
	 * 
	 * @return	The fax4j.properties data
	 */
	public static Properties readInternalConfiguration()
	{
		//init properties
		Properties properties=new Properties();
		
		//get class loader
		ClassLoader classLoader=ReflectionHelper.getThreadContextClassLoader();

		//load internal properties
		InputStream inputStream=classLoader.getResourceAsStream("org/fax4j/"+LibraryConfigurationLoader.CONFIGURATION_FILE_NAME);
		LibraryConfigurationLoader.loadProperties(properties,inputStream,true);

		return properties;
	}
	
	/**
	 * This function reads and returns the internal and external fax4j properties.
	 * 
	 * @return	The internal and external fax4j.properties data
	 */
	public static Properties readInternalAndExternalConfiguration()
	{
		//read properties
		Properties properties=LibraryConfigurationLoader.readInternalConfiguration();

		ClassLoader classLoader=ReflectionHelper.getThreadContextClassLoader();
		InputStream inputStream=classLoader.getResourceAsStream(LibraryConfigurationLoader.CONFIGURATION_FILE_NAME);
		
		if(inputStream!=null)
		{
			LibraryConfigurationLoader.loadProperties(properties,inputStream,false);
		}

		return properties;
	}

	/**
	 * This function returns the system configuration (internal and external 
	 * fax4j.properties files).
	 *  
	 * @return	The system configuration
	 */
	public static Map<String,String> getSystemConfiguration()
	{
		if(LibraryConfigurationLoader.systemConfiguration==null)
		{
			synchronized(LibraryConfigurationLoader.class)
			{
				if(LibraryConfigurationLoader.systemConfiguration==null)
				{
					//read properties
					Properties properties=LibraryConfigurationLoader.readInternalAndExternalConfiguration();

					//convert to map
					Map<String,String> map=new HashMap<String,String>(properties.size());
					SpiUtil.copyPropertiesToMap(properties,map);

					//normalize map
					map=new HashMap<String,String>(map);
					
					//prevent changes
					map=Collections.unmodifiableMap(map);

					//set instance
					LibraryConfigurationLoader.systemConfiguration=map;
				}
			}
		}
		
		return LibraryConfigurationLoader.systemConfiguration;
	}
}