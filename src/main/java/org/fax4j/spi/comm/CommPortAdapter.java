package org.fax4j.spi.comm;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class defines the COMM port adapter.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.39a
 */
public interface CommPortAdapter extends Closeable
{
	/**
	 * Returns true if the adapter is valid and open.
	 * 
	 * @return	Ttrue if the adapter is valid and open
	 */
	public boolean isOpen();

	/**
	 * This function returns the input stream to the COMM port.
	 *  
	 * @return	The input stream
	 */
	public InputStream getInputStream();

	/**
	 * This function returns the output stream to the COMM port.
	 *  
	 * @return	The output stream
	 */
	public OutputStream getOutputStream();
}