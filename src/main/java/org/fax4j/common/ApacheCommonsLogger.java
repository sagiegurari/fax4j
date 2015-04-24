package org.fax4j.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fax4j.FaxClient;

/**
 * This is a simple proxy implementation of the logger that transfers
 * all log events to the apache commons logger.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.31
 */
public class ApacheCommonsLogger extends AbstractLogger
{
	/**The apache commons logger*/
	protected final Log APACHE_LOGGER;
	
	/**
	 * This is the default constructor.
	 */
	public ApacheCommonsLogger()
	{
		super();

		//init logger
		this.APACHE_LOGGER=ApacheCommonsLogger.initializeLogger();
	}
	
	/**
	 * This function initializes and returns the logger.
	 * 
	 * @return	The logger
	 */
	protected static final Log initializeLogger()
	{
		//get logger
		Log logger=LogFactory.getLog(FaxClient.class.getName());

		return logger;
	}

	/**
	 * Logs the provided data.
	 *  
	 * @param 	level
	 * 			The log level
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The error (may be null)
	 */
	@Override
	protected void logImpl(LogLevel level,Object[] message,Throwable throwable)
	{
		//format log message (without exception)
		String text=this.formatLogMessage(level,message,null);

		//get log level
		int levelValue=level.getValue();
		switch(levelValue)
		{
			case LogLevel.DEBUG_LOG_LEVEL_VALUE:
				this.APACHE_LOGGER.debug(text);
				break;
			case LogLevel.ERROR_LOG_LEVEL_VALUE:
				this.APACHE_LOGGER.error(text,throwable);
				break;
			case LogLevel.INFO_LOG_LEVEL_VALUE:
			default:
				this.APACHE_LOGGER.info(text);
				break;
		}
	}
}