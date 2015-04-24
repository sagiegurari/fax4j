package org.fax4j.common;

/**
 * This interface defines a simple logger used by the fax4j.<br>
 * This interface can be implemented to proxy the log requests to another
 * logger framework.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.1
 */
public interface Logger
{
	/**The system eol string value*/
	public static final String SYSTEM_EOL=System.getProperty("line.separator","\n");

	/**
	 * Sets the minimum log level.
	 *  
	 * @param 	logLevel
	 * 			The log level
	 */
	public void setLogLevel(LogLevel logLevel);

	/**
	 * Returns the minimum log level.
	 *  
	 * @return 	The log level
	 */
	public LogLevel getLogLevel();
	
	/**
	 * Logs the provided data at the debug level.
	 *  
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The error (may be null)
	 */
	public void logDebug(Object[] message,Throwable throwable);

	/**
	 * Logs the provided data at the info level.
	 *  
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The error (may be null)
	 */
	public void logInfo(Object[] message,Throwable throwable);

	/**
	 * Logs the provided data at the error level.
	 *  
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The error (may be null)
	 */
	public void logError(Object[] message,Throwable throwable);
}