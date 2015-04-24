package org.fax4j.spi.comm;

import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.util.Connection;
import org.fax4j.util.ReflectionHelper;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will invoke the requests by sending the data via COMM ports to the fax modem.<br>
 * <br>
 * The configuration of the fax4j framework is made up of 3 layers.<br>
 * The configuration is based on simple properties.<br>
 * Each layer overrides the lower layers by adding/changing the property values.<br>
 * The first layer is the internal fax4j.properties file located in the fax4j jar.<br>
 * This layer contains the preconfigured values for the fax4j framework and can be changed
 * by updating these properties in the higher layers.<br>
 * The second layer is the external fax4j.properties file that is located on the classpath.<br>
 * This file is optional and provides the ability to override the internal configuration for the
 * entire fax4j framework.<br>
 * The top most layer is the optional java.util.Properties object provided by the external classes
 * when creating a new fax client.<br>
 * These properties enable to override the configuration of the lower 2 layers.<br>
 * <br>
 * <b>SPI Status (Draft, Beta, Stable): </b>Beta<br>
 * <br>
 * Below table describes the configuration values relevant for this class.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.comm.port.name</td>
 * 		<td>The COMM port name (COM1, ...) property key</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.comm.connection.factory.class.name</td>
 * 		<td>The connection factory class name</td>
 * 		<td>org.fax4j.spi.comm.CommPortConnectionFactoryImpl</td>
 * 		<td>org.fax4j.spi.comm.CommPortConnectionFactoryImpl</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.comm.fax.modem.class.name</td>
 * 		<td>The fax modem class name</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.comm.connection.timeout</td>
 * 		<td>The timeout value when trying to own a COMM port</td>
 * 		<td>1500</td>
 * 		<td>1500</td>
 * 		<td>false</td>
 * </tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>See the relevant COMM connection and fax modem documentation.
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>See the relevant COMM connection and fax modem documentation.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.13
 * @since	0.33
 */
public class CommFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The connection factory class name*/
	private String commConnectionFactoryClassName;
	/**The fax modem class name*/
	private String faxModemClassName;
	/**The connection factory*/
	private CommPortConnectionFactory commConnectionFactory;
	/**The connection*/
	private Connection<CommPortAdapter> commConnection;
	/**The fax modem adapter*/
	private FaxModemAdapter faxModemAdapter;
	/**The timeout default value*/
	public static final int TIMEOUT_DEFAULT_VALUE=1500;

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.34
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The COMM port name (COM1, ...) property key*/
		PORT_NAME_PROPERTY_KEY("org.fax4j.spi.comm.port.name"),
		/**The connection factory class name property key*/
		CONNECTION_FACTORY_CLASS_NAME_PROPERTY_KEY("org.fax4j.spi.comm.connection.factory.class.name"),
		/**The fax modem class name property key*/
		FAX_MODEM_CLASS_NAME_PROPERTY_KEY("org.fax4j.spi.comm.fax.modem.class.name"),
		/**The timeout property key*/
		TIMEOUT_PROPERTY_KEY("org.fax4j.spi.comm.connection.timeout");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxClientSpiConfigurationConstants(String value)
		{
			this.value=value;
		}
		
		/**
		 * This function returns the string value.
		 * 
		 * @return	The string value
		 */
		@Override
		public final String toString()
		{
			return this.value;
		}
	}

	/**
	 * This is the default constructor.
	 */
	public CommFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//get connection factory class name
		this.commConnectionFactoryClassName=this.getConfigurationValue(FaxClientSpiConfigurationConstants.CONNECTION_FACTORY_CLASS_NAME_PROPERTY_KEY);
		if(this.commConnectionFactoryClassName==null)
		{
			this.commConnectionFactoryClassName=RXTXCommPortConnectionFactory.class.getName();
		}
		
		//create connection factory
		this.commConnectionFactory=this.createCommPortConnectionFactory();
		if(this.commConnectionFactory==null)
		{
			throw new FaxException("COMM connection factory is not available.");
		}

		//get fax modem class name
		this.faxModemClassName=this.getConfigurationValue(FaxClientSpiConfigurationConstants.FAX_MODEM_CLASS_NAME_PROPERTY_KEY);
		if(this.faxModemClassName==null)
		{
			throw new FaxException("Fax modem class name not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.FAX_MODEM_CLASS_NAME_PROPERTY_KEY);
		}
		
		//create fax modem adapter
		this.faxModemAdapter=this.createFaxModemAdapter();
		if(this.faxModemAdapter==null)
		{
			throw new FaxException("Fax modem adapter is not available.");
		}
	}
	
	/**
	 * Creates and returns the COMM port connection factory.
	 *  
	 * @return	The COMM port connection factory
	 */
	protected final CommPortConnectionFactory createCommPortConnectionFactory()
	{
		//create new instance
		CommPortConnectionFactory factory=(CommPortConnectionFactory)ReflectionHelper.createInstance(this.commConnectionFactoryClassName);
			
		//initialize
		factory.initialize(this);
		
		return factory;
	}
	
	/**
	 * Creates and returns the fax modem adapter.
	 *  
	 * @return	The fax modem adapter
	 */
	protected final FaxModemAdapter createFaxModemAdapter()
	{
		//create new instance
		FaxModemAdapter adapter=(FaxModemAdapter)ReflectionHelper.createInstance(this.faxModemClassName);
			
		//initialize
		adapter.initialize(this);
		
		return adapter;
	}
	
	/**
	 * Releases the COMM port connection if open. 
	 * 
	 * @throws 	Throwable
	 * 			Any throwable
	 */
	@Override
	protected void finalize() throws Throwable
	{
		//release connection
		this.releaseCommPortConnection();
		
		super.finalize();
	}
	
	/**
	 * Creates and returns the COMM port connection to be used.
	 *  
	 * @return	The COMM port connection
	 */
	protected Connection<CommPortAdapter> createCommPortConnection()
	{
		//create connection
		Connection<CommPortAdapter> connection=this.commConnectionFactory.createConnection();

		return connection;
	}

	/**
	 * Releases the COMM port connection if open. 
	 */
	protected void releaseCommPortConnection()
	{
		//get reference
		Connection<CommPortAdapter> connection=this.commConnection;
		this.commConnection=null;

		if(connection!=null)
		{
			//get logger
			Logger logger=this.getLogger();

			try
			{
				//release connection
				logger.logInfo(new Object[]{"Closing COMM port connection."},null);
				connection.close();
			}
			catch(Exception exception)
			{
				logger.logError(new Object[]{"Error while closing COMM port connection."},exception);
			}
		}
	}
	
	/**
	 * Returns the COMM port connection to be used.
	 *  
	 * @return	The COMM port connection
	 */
	protected Connection<CommPortAdapter> getCommPortConnection()
	{
		Connection<CommPortAdapter> connection=null;
		synchronized(this)
		{
			boolean connectionValid=true;
			//if no connection available
			if(this.commConnection==null)
			{
				connectionValid=false;
			}
			else
			{
				//get adapter
				CommPortAdapter adapter=this.commConnection.getResource();

				//if current adapter is not valid/open
				if(!adapter.isOpen())
				{
					connectionValid=false;
				}
			}
			
			if(!connectionValid)
			{
				//release old connection
				this.releaseCommPortConnection();

				//create new connection
				this.commConnection=this.createCommPortConnection();
			}
		}
			
		//get connection
		connection=this.commConnection;
		
		return connection;
	}

	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected synchronized void submitFaxJobImpl(FaxJob faxJob)
	{
		//get the COMM port connection
		Connection<CommPortAdapter> connection=this.getCommPortConnection();

		//get adapter
		CommPortAdapter adapter=connection.getResource();

		//invoke action
		this.submitFaxJobImpl(faxJob,adapter);
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param	adapter
	 * 			The COMM port adapter
	 */
	protected void submitFaxJobImpl(FaxJob faxJob,CommPortAdapter adapter)
	{
		//invoke fax modem adapter
		this.faxModemAdapter.submitFaxJob(faxJob,adapter);
	}
}