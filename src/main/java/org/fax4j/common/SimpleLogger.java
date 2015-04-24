package org.fax4j.common;

/**
 * This is a basic implementation of the logger.<br>
 * It enables to print the output to the system out stream.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.05
 * @since	0.1
 */
public class SimpleLogger extends AbstractLogger
{
	/**
	 * This is the default constructor.
	 */
	public SimpleLogger()
	{
		super();
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
		//format log message
		String text=this.formatLogMessage(level,message,throwable);
		
		//print text to system out
		System.out.println(text);
	}
}