package org.fax4j.spi.comm;

import gnu.io.CommPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.fax4j.FaxException;

/**
 * This class holds the COMM port objects.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.39a
 */
public class RXTXCommPortAdapter extends AbstractCommPortAdapter
{
	/**The COMM port*/
	private CommPort commPort;
	
	/**
	 * This is the class constructor.
	 * 
	 * @param 	commPort
	 * 			The COMM port
	 */
	public RXTXCommPortAdapter(CommPort commPort)
	{
		super();
		
		//validate COMM port object provided
		if(commPort==null)
		{
			throw new FaxException("COMM port object not provided.");
		}
		
		this.commPort=commPort;
	}

	/**
	 * Returns true if the adapter is valid and open.
	 * 
	 * @return	Ttrue if the adapter is valid and open
	 */
	public boolean isOpen()
	{
		boolean open=true;
		if(this.commPort==null)
		{
			open=false;
		}
		
		return open;
	}

	/**
	 * This function returns the input stream to the COMM port.
	 *  
	 * @return	The input stream
	 */
	public InputStream getInputStream()
	{
		InputStream stream=null;
		try
		{
			stream=this.commPort.getInputStream();
		}
		catch(IOException exception)
		{
			throw new FaxException("Unable to extract input stream.",exception);
		}
		
		return stream;
	}

	/**
	 * This function returns the output stream to the COMM port.
	 *  
	 * @return	The output stream
	 */
	public OutputStream getOutputStream()
	{
		OutputStream stream=null;
		try
		{
			stream=this.commPort.getOutputStream();
		}
		catch(IOException exception)
		{
			throw new FaxException("Unable to extract output stream.",exception);
		}
		
		return stream;
	}

	/**
	 * Closes the connection.
	 */
	@Override
	protected void closeImpl()
	{
		//remove reference
		CommPort port=this.commPort;
		this.commPort=null;
		
		//close connection
		port.close();
	}
}