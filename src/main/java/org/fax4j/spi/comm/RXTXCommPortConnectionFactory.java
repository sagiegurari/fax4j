package org.fax4j.spi.comm;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import org.fax4j.FaxException;

/**
 * This class implements the COMM port connection factory using the 
 * RXTX COMM API.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.36
 */
public class RXTXCommPortConnectionFactory extends AbstractCommPortConnectionFactory
{
	/**
	 * This is the class constructor.
	 */
	public RXTXCommPortConnectionFactory()
	{
		super();
	}
	
	/**
	 * This function returns the COMM port identifier object.
	 * 
	 * @param 	portName
	 * 			The port name
	 * @return	The port identifier object
	 * @throws 	NoSuchPortException
	 * 			In case no such port exists
	 */
	protected CommPortIdentifier getPortIdentifier(String portName) throws NoSuchPortException
	{
		return CommPortIdentifier.getPortIdentifier(portName);
	}

	/**
	 * Creates and returns the resource.
	 *  
	 * @return	The resource
	 */
	@Override
	protected CommPortAdapter createResourceImpl()
	{
		//get COMM port name
		String portName=this.getPortName();
		
		//get timeout
		int timeout=this.getTimeOut();

		CommPort commPort=null;
		try
		{
			//get COMM port identifier
			CommPortIdentifier commPortIdentifier=this.getPortIdentifier(portName);

			//open connection
			commPort=commPortIdentifier.open("fax4j",timeout);
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to connect to COMM port: "+portName,exception);
		}

		//create adapter wrapper
		CommPortAdapter adapter=new RXTXCommPortAdapter(commPort);
		
		return adapter;
	}
}