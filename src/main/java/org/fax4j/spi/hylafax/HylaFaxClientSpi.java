package org.fax4j.spi.hylafax;

import gnu.hylafax.HylaFAXClient;
import gnu.hylafax.HylaFAXClientProtocol;
import gnu.hylafax.Job;
import java.io.Closeable;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.util.Connection;
import org.fax4j.util.ConnectionFactory;
import org.fax4j.util.ReflectionHelper;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation is based on the open source hylafax project.<br>
 * It implements the hylafax protocol used by the open source hylafax fax server.<br>
 * See http://sourceforge.net/projects/gnu-hylafax/ for more info.<br>
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
 * 		<td>org.fax4j.spi.hylafax.connection.factory.class.name</td>
 * 		<td>The connection factory class name</td>
 * 		<td>org.fax4j.spi.hylafax.HylaFAXClientConnectionFactoryImpl</td>
 * 		<td>org.fax4j.spi.hylafax.HylaFAXClientConnectionFactoryImpl</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hylafax.host</td>
 * 		<td>See gnu-hylafax project for more info.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hylafax.port</td>
 * 		<td>See gnu-hylafax project for more info.</td>
 * 		<td>none</td>
 * 		<td>gnu.hylafax.HylaFAXClientProtocol.DEFAULT_PORT</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hylafax.user</td>
 * 		<td>See gnu-hylafax project for more info.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hylafax.password</td>
 * 		<td>See gnu-hylafax project for more info.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hylafax.admin</td>
 * 		<td>See gnu-hylafax project for more info.</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hylafax.mode</td>
 * 		<td>See gnu-hylafax project for more info.</td>
 * 		<td>Z</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.hylafax.type</td>
 * 		<td>See gnu-hylafax project for more info.</td>
 * 		<td>A</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>See http://sourceforge.net/projects/gnu-hylafax/ web site.
 * 	<li>Works only with HylaFax fax servers.
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>See http://sourceforge.net/projects/gnu-hylafax/ web site.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.12
 * @since	0.1
 */
public class HylaFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The HylaFax client connection factory*/
	private ConnectionFactory<HylaFAXClient> connectionFactory;
	/**The connection used to connect to the HylaFax client*/
	private Connection<HylaFAXClient> connection;
	/**The fax server default port*/
	public static final int DEFAULT_PORT_PROPERTY_VALUE=HylaFAXClientProtocol.DEFAULT_PORT;

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.03
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The connection factory class name property key*/
		CONNECTION_FACTORY_CLASS_NAME_PROPERTY_KEY("org.fax4j.spi.hylafax.connection.factory.class.name"),
		/**The fax server host property key*/
		HOST_PROPERTY_KEY("org.fax4j.spi.hylafax.host"),
		/**The fax server port property key*/
		PORT_PROPERTY_KEY("org.fax4j.spi.hylafax.port"),
		/**The fax server user name property key*/
		USER_NAME_PROPERTY_KEY("org.fax4j.spi.hylafax.user"),
		/**The fax server password property key*/
		PASSWORD_PROPERTY_KEY("org.fax4j.spi.hylafax.password"),
		/**The enable admin operations property key*/
		ENABLE_ADMIN_OPERATIONS_PROPERTY_KEY("org.fax4j.spi.hylafax.admin"),
		/**The mode property key*/
		MODE_PROPERTY_KEY("org.fax4j.spi.hylafax.mode"),
		/**The type property key*/
		TYPE_PROPERTY_KEY("org.fax4j.spi.hylafax.type");

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
	public HylaFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//setup connection factory
		String className=this.getConfigurationValue(FaxClientSpiConfigurationConstants.CONNECTION_FACTORY_CLASS_NAME_PROPERTY_KEY);
		if(className==null)
		{
			className=HylaFAXClientConnectionFactoryImpl.class.getName();
		}
		this.connectionFactory=this.createHylaFAXClientConnectionFactory(className);
		if(this.connectionFactory==null)
		{
			throw new FaxException("HylaFAX connection factory is not available.");
		}
	}
	
	/**
	 * Creates and returns the hylafax client connection factory.
	 *  
	 * @param	className
	 * 			The connection factory class name
	 * @return	The hylafax client connection factory
	 */
	protected final HylaFAXClientConnectionFactory createHylaFAXClientConnectionFactory(String className)
	{
		//create new instance
		HylaFAXClientConnectionFactory factory=(HylaFAXClientConnectionFactory)ReflectionHelper.createInstance(className);
			
		//initialize
		factory.initialize(this);
		
		return factory;
	}

	/**
	 * Returns an instance of the hyla fax client.
	 *  
	 * @return	The client instance
	 */
	protected synchronized HylaFAXClient getHylaFAXClient()
	{
		HylaFAXClient client=null;
		if(this.connection==null)
		{
			//create new connection
			this.connection=this.connectionFactory.createConnection();
		}
		
		//get client
		client=this.connection.getResource();
		
		return client;
	}
	
	/**
	 * Releases the connection if open. 
	 * 
	 * @throws 	Throwable
	 * 			Any throwable
	 */
	@Override
	protected void finalize() throws Throwable
	{
		//get reference
		Closeable closeable=this.connection;
		
		if(closeable!=null)
		{
			//release connection
			closeable.close();
		}
		
		super.finalize();
	}

	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	@Override
	protected FaxJob createFaxJobImpl()
	{
		//get client
		HylaFAXClient client=this.getHylaFAXClient();

		FaxJob faxJob=null;
		try
		{
			//create job
			Job job=client.createJob();
			faxJob=new HylaFaxJob(job);
		}
		catch(RuntimeException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("General error.",exception);
		}
		
		return faxJob;
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
	protected void submitFaxJobImpl(FaxJob faxJob)
	{
		//get fax job
		HylaFaxJob hylaFaxJob=(HylaFaxJob)faxJob;
		
		//get client
		HylaFAXClient client=this.getHylaFAXClient();

		try
		{
			this.submitFaxJob(hylaFaxJob,client);
		}
		catch(FaxException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("General error.",exception);
		}
	}
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void suspendFaxJobImpl(FaxJob faxJob)
	{
		//get fax job
		HylaFaxJob hylaFaxJob=(HylaFaxJob)faxJob;
		
		//get client
		HylaFAXClient client=this.getHylaFAXClient();

		try
		{
			this.suspendFaxJob(hylaFaxJob,client);
		}
		catch(FaxException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("General error.",exception);
		}
	}
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void resumeFaxJobImpl(FaxJob faxJob)
	{
		//get fax job
		HylaFaxJob hylaFaxJob=(HylaFaxJob)faxJob;
		
		//get client
		HylaFAXClient client=this.getHylaFAXClient();

		try
		{
			this.resumeFaxJob(hylaFaxJob,client);
		}
		catch(FaxException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("General error.",exception);
		}
	}
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void cancelFaxJobImpl(FaxJob faxJob)
	{
		//get fax job
		HylaFaxJob hylaFaxJob=(HylaFaxJob)faxJob;
		
		//get client
		HylaFAXClient client=this.getHylaFAXClient();

		try
		{
			this.cancelFaxJob(hylaFaxJob,client);
		}
		catch(FaxException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("General error.",exception);
		}
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param	client
	 * 			The client instance
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @throws	Exception
	 * 			Any exception
	 */
	protected void submitFaxJob(HylaFaxJob faxJob,HylaFAXClient client) throws Exception
	{
		//get job
		Job job=faxJob.getHylaFaxJob();

		//add document to fax
		String filePath=faxJob.getFilePath();
		job.addDocument(filePath);
		
		//submit job
		client.submit(job);
	}
	
	/**
	 * This function will suspend an existing fax job.
	 * 
	 * @param	client
	 * 			The client instance
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @throws	Exception
	 * 			Any exception
	 */
	protected void suspendFaxJob(HylaFaxJob faxJob,HylaFAXClient client) throws Exception
	{
		//get job
		Job job=faxJob.getHylaFaxJob();
		
		//suspend job
		client.suspend(job);
	}
	
	/**
	 * This function will resume an existing fax job.
	 * 
	 * @param	client
	 * 			The client instance
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @throws	Exception
	 * 			Any exception
	 */
	protected void resumeFaxJob(HylaFaxJob faxJob,HylaFAXClient client) throws Exception
	{
		//get job
		Job job=faxJob.getHylaFaxJob();
		
		//get job ID
		long faxJobID=job.getId();
		
		//resume job
		client.retry(faxJobID);
	}
	
	/**
	 * This function will cancel an existing fax job.
	 * 
	 * @param	client
	 * 			The client instance
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @throws	Exception
	 * 			Any exception
	 */
	protected void cancelFaxJob(HylaFaxJob faxJob,HylaFAXClient client) throws Exception
	{
		//get job
		Job job=faxJob.getHylaFaxJob();
		
		//cancel job
		client.kill(job);
	}
}