package org.fax4j.common;

import org.fax4j.FaxException;

/**
 * This is the logger log levels.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.1
 */
public final class LogLevel
{
	/**The log level name*/
	private final String NAME;
	/**The log level value*/
	private final int VALUE;
	/**The DEBUG log level*/
	public static final int DEBUG_LOG_LEVEL_VALUE=0;
	/**The INFO log level*/
	public static final int INFO_LOG_LEVEL_VALUE=50;
	/**The ERROR log level*/
	public static final int ERROR_LOG_LEVEL_VALUE=100;
	/**The NONE log level*/
	public static final int NONE_LOG_LEVEL_VALUE=1000;
	/**The DEBUG log level*/
	public static final LogLevel DEBUG=new LogLevel("DEBUG",LogLevel.DEBUG_LOG_LEVEL_VALUE);
	/**The INFO log level*/
	public static final LogLevel INFO=new LogLevel("INFO",LogLevel.INFO_LOG_LEVEL_VALUE);
	/**The ERROR log level*/
	public static final LogLevel ERROR=new LogLevel("ERROR",LogLevel.ERROR_LOG_LEVEL_VALUE);
	/**The NONE log level*/
	public static final LogLevel NONE=new LogLevel("NONE",LogLevel.NONE_LOG_LEVEL_VALUE);
	
	/**
	 * This is the default constructor.
	 * 
	 * @param 	name
	 * 			The log level name
	 * @param 	value
	 * 			The log level value
	 */
	private LogLevel(String name,int value)
	{
		super();
		
		//validate values
		if((name==null)||(name.length()==0))
		{
			throw new FaxException("Log leve name not provided.");
		}
		
		//get values
		this.NAME=name;
		this.VALUE=value;
	}

	/**
	 * This function returns the log level name.
	 *  
	 * @return	The log level name
	 */
	public String getName()
	{
		return this.NAME;
	}

	/**
	 * This function returns the log level value.
	 *  
	 * @return	The log level value
	 */
	public int getValue()
	{
		return this.VALUE;
	}
}