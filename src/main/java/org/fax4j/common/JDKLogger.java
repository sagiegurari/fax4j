package org.fax4j.common;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import org.fax4j.FaxClient;

/**
 * This is a simple proxy implementation of the logger that transfers
 * all log events to the JDK logger.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.27
 */
public class JDKLogger extends AbstractLogger
{
	/**The JDK logger*/
	protected final Logger JDK_LOGGER;
	
	/**
	 * This is the default constructor.
	 */
	public JDKLogger()
	{
		super();

		//init logger
		this.JDK_LOGGER=JDKLogger.initializeLogger();
	}
	
	/**
	 * This function initializes and returns the logger.
	 * 
	 * @return	The logger
	 */
	protected static final Logger initializeLogger()
	{
		//get logger
		Logger logger=Logger.getLogger(FaxClient.class.getName());
		
		//enable all log events (fax4j logger filters out uneeded log events)
		logger.setLevel(Level.ALL);
		logger.setFilter(null);
		
		//enable to pass log events to parent loggers
		logger.setUseParentHandlers(true);

		//create handler
		Formatter formatter=new SimpleFormatter();
		Handler handler=new StreamHandler(System.out,formatter);
		
		//set filtering
		handler.setLevel(logger.getLevel());
		handler.setFilter(logger.getFilter());

		//add handler
		logger.addHandler(handler);

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
		//get log level
		int levelValue=level.getValue();
		Level jdkLevel=null;
		switch(levelValue)
		{
			case LogLevel.DEBUG_LOG_LEVEL_VALUE:
				jdkLevel=Level.FINEST;
				break;
			case LogLevel.ERROR_LOG_LEVEL_VALUE:
				jdkLevel=Level.SEVERE;
				break;
			case LogLevel.INFO_LOG_LEVEL_VALUE:
			default:
				jdkLevel=Level.FINE;
				break;
		}

		if(jdkLevel!=null)
		{
			//format log message (without exception)
			String text=this.formatLogMessage(level,message,null);

			//log
			this.JDK_LOGGER.log(jdkLevel,text,throwable);
		}
	}
}