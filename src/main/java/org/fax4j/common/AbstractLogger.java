package org.fax4j.common;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This is a partial implementation of the logger.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.21f
 */
public abstract class AbstractLogger implements Logger
{
	/**The log level*/
	private LogLevel logLevel;
	
	/**
	 * This is the default constructor.
	 */
	public AbstractLogger()
	{
		super();

		//set default log level
		this.setLogLevel(LogLevel.NONE);
	}
	
	/**
	 * Converts the provided data to string.
	 *  
	 * @param 	level
	 * 			The log level
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The throwable (may be null)
	 * @return	The string
	 */
	protected String formatLogMessage(LogLevel level,Object[] message,Throwable throwable)
	{
		//get text
		String messageText=this.format(message);
		String throwableText=this.format(throwable);
		
		//init buffer
		StringBuilder buffer=new StringBuilder();

		//append prefix
		buffer.append("[fax4j][");
		buffer.append(level.getName());
		buffer.append("] ");
		
		if(messageText!=null)
		{
			buffer.append(messageText);
			
			if(throwableText!=null)
			{
				buffer.append(Logger.SYSTEM_EOL);
				buffer.append(throwableText);
			}
		}
		else if(throwableText!=null)
		{
			buffer.append(throwableText);
		}
		
		//get text
		String text=buffer.toString();
	
		return text;
	}
	
	/**
	 * Converts the provided message object array to string.
	 *  
	 * @param	message
	 * 			The message parts (may be null)
	 * @return	The string
	 */
	protected String format(Object[] message)
	{
		String text=null;
		if(message!=null)
		{
			//init buffer
			StringBuilder buffer=new StringBuilder();
			
			//get array size
			int amount=message.length;
			
			Object object=null;
			for(int index=0;index<amount;index++)
			{
				//get next element
				object=message[index];
				
				//append to buffer
				buffer.append(object);
			}
			
			//get text
			text=buffer.toString();
		}
		
		return text;
	}
	
	/**
	 * Converts the provided throwable to string.
	 *  
	 * @param 	throwable
	 * 			The throwable (may be null)
	 * @return	The string
	 */
	protected String format(Throwable throwable)
	{
		String text=null;
		if(throwable!=null)
		{
			//init writer
			StringWriter writer=new StringWriter(1500);
			
			//write throwable
			throwable.printStackTrace(new PrintWriter(writer));
			
			//get text
			text=writer.toString();
		}
		
		return text;
	}

	/**
	 * Sets the minimum log level.
	 *  
	 * @param 	logLevel
	 * 			The log level
	 */
	public final void setLogLevel(LogLevel logLevel)
	{
		LogLevel updatedLogLevel=logLevel;
		if(updatedLogLevel==null)
		{
			updatedLogLevel=LogLevel.NONE;
		}
		
		this.logLevel=updatedLogLevel;
	}

	/**
	 * Returns the minimum log level.
	 *  
	 * @return 	The log level
	 */
	public final LogLevel getLogLevel()
	{
		return this.logLevel;
	}
	
	/**
	 * Logs the provided data at the debug level.
	 *  
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The error (may be null)
	 */
	public void logDebug(Object[] message,Throwable throwable)
	{
		this.log(LogLevel.DEBUG,message,throwable);
	}

	/**
	 * Logs the provided data at the info level.
	 *  
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The error (may be null)
	 */
	public void logInfo(Object[] message,Throwable throwable)
	{
		this.log(LogLevel.INFO,message,throwable);
	}

	/**
	 * Logs the provided data at the error level.
	 *  
	 * @param	message
	 * 			The message parts (may be null)
	 * @param 	throwable
	 * 			The error (may be null)
	 */
	public void logError(Object[] message,Throwable throwable)
	{
		this.log(LogLevel.ERROR,message,throwable);
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
	protected void log(LogLevel level,Object[] message,Throwable throwable)
	{
		if(level.getValue()>=this.logLevel.getValue())
		{
			//log the message
			this.logImpl(level,message,throwable);
		}
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
	protected abstract void logImpl(LogLevel level,Object[] message,Throwable throwable);
}