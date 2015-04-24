package org.fax4j.spi.email;

import java.io.IOException;
import javax.mail.Message;
import javax.mail.Transport;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.util.Connection;
import org.fax4j.util.ReflectionHelper;

/**
 * This class implements the fax client service provider interface.<br>
 * This parial implementation will invoke the requests by sending emails to a mail server that supports
 * conversion between email messages and fax messages.<br>
 * The mail SPI supports persistent connection to enable to reuse the same connection for all fax
 * operation invocations or to create a new connection for each fax operation invocation.<br>
 * By default the SPI will create a new connection for each operation invocation however the 
 * <b>org.fax4j.spi.mail.persistent.connection</b> set to true will enable to reuse the connection.<br>
 * To set the user/password values of the mail connection the following 2 properties must be defined:
 * <b>org.fax4j.spi.mail.user.name</b> and <b>org.fax4j.spi.mail.password</b><br>
 * All properties defined in the fax4j configuration will be passed to the mail connection therefore it is
 * possible to define mail specific properties (see java mail for more info) in the fax4j properties.<br>
 * Implementing SPI class will have to implement the createXXXFaxJobMessage methods.<br>
 * These methods will return the message to be sent for that fax job operation. In case the method
 * returns null, this class will throw an UnsupportedOperationException exception.
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
 * <b>SPI Status (Draft, Beta, Stable): </b>Stable<br>
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
 * 		<td>org.fax4j.spi.mail.persistent.connection</td>
 * 		<td>True to reuse the same mail connection for all fax activites, false to create a
 * 			new mail connection for each fax activity.</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.connection.factory.class.name</td>
 * 		<td>The connection factory class name</td>
 * 		<td>org.fax4j.spi.email.MailConnectionFactoryImpl</td>
 * 		<td>org.fax4j.spi.email.MailConnectionFactoryImpl</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.user.name</td>
 * 		<td>The mail account user name.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.password</td>
 * 		<td>The mail account password.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * 	<tr>
 * 		<td>javax mail properties</td>
 * 		<td>Any of the javax mail properties can be defined in the fax4j properties.<br>
 * 			These properties will be passed to the java mail framework.</td>
 * 		<td>mail.transport.protocol=smtp<br>
 * 			mail.smtp.port=25</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * </tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>This SPI is based on the java mail infrastructure, therefore this SPI cannot
 * 		connect to mail servers through a proxy server.
 * 	<li>This SPI provides only partial implementation (this is an abstract class).
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>Required jar files: mail-1.4.jar, activation-1.1.jar
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.12
 * @since	0.1
 */
public abstract class AbstractMailFaxClientSpi extends AbstractFax4JClientSpi
{
	/**
	 * The use persistent connection flag which defines if the SPI will use a new
	 * connection for each request or will reuse the same connection
	 */
	private boolean usePersistentConnection;
	/**The mail connection factory*/
	private MailConnectionFactory connectionFactory;
	/**The mail connection*/
	private Connection<MailResourcesHolder> connection;

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.03
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**
		 * The use persistent connection property key which defines if the SPI will use a new
		 * connection for each request or will reuse the same connection
		 */
		USE_PERSISTENT_CONNECTION_PROPERTY_KEY("org.fax4j.spi.mail.persistent.connection"),
		/**The connection factory class name*/
		CONNECTION_FACTORY_CLASS_NAME_PROPERTY_KEY("org.fax4j.spi.mail.connection.factory.class.name"),
		/**The user name used to connect to the mail server*/
		USER_NAME_PROPERTY_KEY("org.fax4j.spi.mail.user.name"),
		/**The password used to connect to the mail server*/
		PASSWORD_PROPERTY_KEY("org.fax4j.spi.mail.password");

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
	public AbstractMailFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//get logger
		Logger logger=this.getLogger();

		//get use persistent connection value
		this.usePersistentConnection=Boolean.parseBoolean(this.getConfigurationValue(FaxClientSpiConfigurationConstants.USE_PERSISTENT_CONNECTION_PROPERTY_KEY));
		logger.logDebug(new Object[]{"Using persistent connection: ",String.valueOf(this.usePersistentConnection)},null);
		
		//setup connection factory
		String className=this.getConfigurationValue(FaxClientSpiConfigurationConstants.CONNECTION_FACTORY_CLASS_NAME_PROPERTY_KEY);
		this.connectionFactory=this.createMailConnectionFactoryImpl(className);
		if(this.connectionFactory==null)
		{
			throw new FaxException("Mail connection factory is not available.");
		}
	}
	
	/**
	 * Creates and returns the mail connection factory.
	 *  
	 * @param	className
	 * 			The connection factory class name
	 * @return	The mail connection factory
	 */
	protected final MailConnectionFactory createMailConnectionFactoryImpl(String className)
	{
		String factoryClassName=className;
		if(factoryClassName==null)
		{
			factoryClassName=MailConnectionFactoryImpl.class.getName();
		}

		//create new instance
		MailConnectionFactory factory=(MailConnectionFactory)ReflectionHelper.createInstance(factoryClassName);
			
		//initialize
		factory.initialize(this);
		
		return factory;
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
		Connection<MailResourcesHolder> mailConnection=this.connection;
		
		//release connection
		this.closeMailConnection(mailConnection);
		
		super.finalize();
	}
	
	/**
	 * Creates and returns the mail connection to be used to send the fax
	 * via mail.
	 *  
	 * @return	The mail connection
	 */
	protected Connection<MailResourcesHolder> createMailConnection()
	{
		//create new connection
		Connection<MailResourcesHolder> mailConnection=this.connectionFactory.createConnection();

		//log debug
		Logger logger=this.getLogger();
		logger.logInfo(new Object[]{"Created mail connection."},null);

		return mailConnection;
	}
	
	/**
	 * This function closes the provided mail connection.
	 * 
	 * @param	mailConnection
	 * 			The mail connection to close
	 * @throws 	IOException
	 * 			Never thrown
	 */
	protected void closeMailConnection(Connection<MailResourcesHolder> mailConnection) throws IOException
	{
		if(mailConnection!=null)
		{
			//get logger
			Logger logger=this.getLogger();
			
			//release connection
			logger.logInfo(new Object[]{"Closing mail connection."},null);
			mailConnection.close();
		}
	}
	
	/**
	 * Returns the mail connection to be used to send the fax
	 * via mail.
	 *  
	 * @return	The mail connection
	 */
	protected Connection<MailResourcesHolder> getMailConnection()
	{
		Connection<MailResourcesHolder> mailConnection=null;
		if(this.usePersistentConnection)
		{
			synchronized(this)
			{
				if(this.connection==null)
				{
					//create new connection
					this.connection=this.createMailConnection();
				}
			}
			
			//get connection
			mailConnection=this.connection;
		}
		else
		{
			//create new connection
			mailConnection=this.createMailConnection();
		}
		
		return mailConnection;
	}
	
	/**
	 * This function will send the mail message.
	 *  
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param 	mailConnection
	 * 			The mail connection (will be released if not persistent)
	 * @param	message
	 * 			The message to send
	 */
	protected void sendMail(FaxJob faxJob,Connection<MailResourcesHolder> mailConnection,Message message)
	{
		if(message==null)
		{
			this.throwUnsupportedException();
		}
		else
		{
			//get holder
			MailResourcesHolder mailResourcesHolder=mailConnection.getResource();

			//get transport
			Transport transport=mailResourcesHolder.getTransport();
			
			try
			{
				//send message
				message.saveChanges();
				if(transport==null)
				{
					Transport.send(message,message.getAllRecipients());
				}
				else
				{
					transport.sendMessage(message,message.getAllRecipients());
				}
			}
			catch(Throwable throwable)
			{
				throw new FaxException("Unable to send message.",throwable);
			}
			finally
			{
				if(!this.usePersistentConnection)
				{
					try
					{
						//close connection
						this.closeMailConnection(mailConnection);
					}
					catch(Exception exception)
					{
						//log error
						Logger logger=this.getLogger();
						logger.logInfo(new Object[]{"Error while releasing mail connection."},exception);
					}
				}
			}
		}
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
		//get connection
		Connection<MailResourcesHolder> mailConnection=this.getMailConnection();

		//get holder
		MailResourcesHolder mailResourcesHolder=mailConnection.getResource();

		//create message
		Message message=this.createSubmitFaxJobMessage(faxJob,mailResourcesHolder);
		
		//send message
		this.sendMail(faxJob,mailConnection,message);
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
		//get connection
		Connection<MailResourcesHolder> mailConnection=this.getMailConnection();

		//get holder
		MailResourcesHolder mailResourcesHolder=mailConnection.getResource();

		//create message
		Message message=this.createSuspendFaxJobMessage(faxJob,mailResourcesHolder);
		
		//send message
		this.sendMail(faxJob,mailConnection,message);
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
		//get connection
		Connection<MailResourcesHolder> mailConnection=this.getMailConnection();

		//get holder
		MailResourcesHolder mailResourcesHolder=mailConnection.getResource();

		//create message
		Message message=this.createResumeFaxJobMessage(faxJob,mailResourcesHolder);
		
		//send message
		this.sendMail(faxJob,mailConnection,message);
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
		//get connection
		Connection<MailResourcesHolder> mailConnection=this.getMailConnection();

		//get holder
		MailResourcesHolder mailResourcesHolder=mailConnection.getResource();

		//create message
		Message message=this.createCancelFaxJobMessage(faxJob,mailResourcesHolder);
		
		//send message
		this.sendMail(faxJob,mailConnection,message);
	}
	
	/**
	 * This function will create the message used to invoke the fax
	 * job action.<br>
	 * If this method returns null, the SPI will throw an UnsupportedOperationException.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param 	mailResourcesHolder
	 * 			The mail resources holder
	 * @return	The message to send (if null, the SPI will throw an UnsupportedOperationException)
	 */
	protected abstract Message createSubmitFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder);
	
	/**
	 * This function will create the message used to invoke the fax
	 * job action.<br>
	 * If this method returns null, the SPI will throw an UnsupportedOperationException.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param 	mailResourcesHolder
	 * 			The mail resources holder
	 * @return	The message to send (if null, the SPI will throw an UnsupportedOperationException)
	 */
	protected abstract Message createSuspendFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder);
	
	/**
	 * This function will create the message used to invoke the fax
	 * job action.<br>
	 * If this method returns null, the SPI will throw an UnsupportedOperationException.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param 	mailResourcesHolder
	 * 			The mail resources holder
	 * @return	The message to send (if null, the SPI will throw an UnsupportedOperationException)
	 */
	protected abstract Message createResumeFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder);
	
	/**
	 * This function will create the message used to invoke the fax
	 * job action.<br>
	 * If this method returns null, the SPI will throw an UnsupportedOperationException.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @param 	mailResourcesHolder
	 * 			The mail resources holder
	 * @return	The message to send (if null, the SPI will throw an UnsupportedOperationException)
	 */
	protected abstract Message createCancelFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder);
}