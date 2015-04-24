package org.fax4j;

/**
 * A general exception class used by the fax4j framework.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.1
 */
public class FaxException extends RuntimeException
{
	/**Default serialization UID*/
	private static final long serialVersionUID=1L;

	/**
	 * This is the default constructor.
	 * 
	 * @param 	message
	 * 			The error message
	 */
	public FaxException(String message)
	{
		super(message);
	}

	/**
	 * This is the default constructor.
	 * 
	 * @param 	message
	 * 			The error message
	 * @param	cause
	 * 			The root cause of the exception
	 */
	public FaxException(String message,Throwable cause)
	{
		super(message,cause);
	}
}