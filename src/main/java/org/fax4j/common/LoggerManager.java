package org.fax4j.common;

import java.util.Map;
import org.fax4j.util.LibraryConfigurationLoader;
import org.fax4j.util.ReflectionHelper;

/**
 * Manages the internal fax4j logger.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.04
 * @since	0.40.6
 */
public final class LoggerManager
{
	/**The fax4j logger*/
	private Logger logger;
	/**The global instance*/
	private static final LoggerManager INSTANCE=new LoggerManager();
	/**The logger class name property key*/
	public static final String LOGGER_CLASS_NAME_PROPERTY_KEY="org.fax4j.logger.class.name";
	/**The logger log level property key*/
	public static final String LOGGER_LOG_LEVEL_PROPERTY_KEY="org.fax4j.logger.log.level";
	
	/**
	 * This is the class constructor.
	 */
	private LoggerManager()
	{
		super();

		//initialize logger
		this.initializeLogger();
	}
	
	/**
	 * This function returns the global instance.
	 * 
	 * @return	The global instance
	 */
	public static LoggerManager getInstance()
	{
		return LoggerManager.INSTANCE;
	}
	
	/**
	 * This function initializes the fax4j global logger.
	 */
	private void initializeLogger()
	{
		//get system configuration
		Map<String,String> systemConfiguration=LibraryConfigurationLoader.getSystemConfiguration();

		//create new logger
		this.createLogger(systemConfiguration);
	}
	
	/**
	 * This function returns the fax4j internal logger.
	 * 
	 * @return	The fax4j internal logger
	 */
	public Logger getLogger()
	{
		return this.logger;
	}

	/**
	 * This function creates the logger used by the fax4j framework.<br>
	 * The logger must extend org.fax4j.util.Logger and it's class name is defined
	 * by the org.fax4j.logger.class.name property in the fax4j configuration.<br>
	 * The org.fax4j.logger.log.level property is used to set the intial log level of
	 * the logger.
	 * 
	 * @param	systemConfiguration
	 * 			The system configuration
	 */
	private void createLogger(Map<String,String> systemConfiguration)
	{
		//get class name
		String className=systemConfiguration.get(LoggerManager.LOGGER_CLASS_NAME_PROPERTY_KEY);
		if((className==null)||(className.length()==0))
		{
			className=SimpleLogger.class.getName();
		}

		//create new instance
		Logger loggerInstance=(Logger)ReflectionHelper.createInstance(className);
		
		//set default log level as NONE
		loggerInstance.setLogLevel(LogLevel.NONE);
		
		//get log level
		String logLevelName=systemConfiguration.get(LoggerManager.LOGGER_LOG_LEVEL_PROPERTY_KEY);
		
		//set log level (ignore invalid log level)
		if(logLevelName!=null)
		{
			if(logLevelName.equalsIgnoreCase(LogLevel.ERROR.getName()))
			{
				loggerInstance.setLogLevel(LogLevel.ERROR);
			}
			else if(logLevelName.equalsIgnoreCase(LogLevel.INFO.getName()))
			{
				loggerInstance.setLogLevel(LogLevel.INFO);
			}
			else if(logLevelName.equalsIgnoreCase(LogLevel.DEBUG.getName()))
			{
				loggerInstance.setLogLevel(LogLevel.DEBUG);
			}
		}
		
		//get reference
		this.logger=loggerInstance;
	}
}