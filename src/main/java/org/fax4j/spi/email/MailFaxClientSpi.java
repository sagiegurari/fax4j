package org.fax4j.spi.email;

import java.io.File;
import java.text.MessageFormat;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will invoke the requests by sending emails to a mail server that supports
 * conversion between email messages and fax messages.<br>
 * The mail SPI supports persistent connection to enable to reuse the same connection for all fax
 * operation invocations or to create a new connection for each fax operation invocation.<br>
 * By default the SPI will create a new connection for each operation invocation however the 
 * <b>org.fax4j.spi.mail.persistent.connection</b> set to true will enable to reuse the connection.<br>
 * To set the user/password values of the mail connection the following 2 properties must be defined:
 * <b>org.fax4j.spi.mail.user.name</b> and <b>org.fax4j.spi.mail.password</b><br>
 * All properties defined in the fax4j configuration will be passed to the mail connection therefore it is
 * possible to define mail specific properties (see java mail for more info) in the fax4j properties.<br>
 * This SPI only supports to submit new fax jobs.<br>
 * The SPI supports embedding the target destination number into the TO address field and subject field by setting the
 * <b>org.fax4j.spi.mail.address.template</b> and <b>org.fax4j.spi.mail.subject.template</b> properties.<br>
 * For example: org.fax4j.spi.mail.address.template={0}@myprovider.com<br>
 * For example: org.fax4j.spi.mail.subject.template=fax:{0}<br>
 * The {0} will be replaced with the destination number.<br>
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
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.address.template</td>
 * 		<td>The address template used when sending the mail message.<br>
 * 			In order to provide the target fax number as part of the email address you must
 * 			specify {0} in the template.<br>
 * 			For example: org.fax4j.spi.mail.address.template={0}@myprovider.com
 *		</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.subject.template</td>
 * 		<td>The subject template used when sending the mail message.<br>
 * 			In order to provide the target fax number as part of the email subject line you must
 * 			specify {0} in the template.<br>
 * 			For example: org.fax4j.spi.mail.subject.template=fax:{0}
 *		</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.persistent.connection</td>
 * 		<td>True to reuse the same mail connection for all fax activites, false to create a
 * 			new mail connection for each fax activity.</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.connection.factory.class.name</td>
 * 		<td>The connection factory class name</td>
 * 		<td>org.fax4j.spi.email.MailConnectionFactoryImpl</td>
 * 		<td>org.fax4j.spi.email.MailConnectionFactoryImpl</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.user.name</td>
 * 		<td>The mail account user name.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.mail.password</td>
 * 		<td>The mail account password.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>javax mail properties</td>
 * 		<td>Any of the javax mail properties can be defined in the fax4j properties.<br>
 * 			These properties will be passed to the java mail framework.</td>
 * 		<td>mail.transport.protocol=smtp<br>
 * 			mail.smtp.port=25</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>This SPI is based on the java mail infrastructure, therefore this SPI cannot
 * 		connect to mail servers through a proxy server.
 * 	<li>Currently this SPI only supports submitting new fax jobs.
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>Required jar files: mail-1.4.jar, activation-1.1.jar
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.08
 * @since	0.1
 */
public class MailFaxClientSpi extends AbstractMailFaxClientSpi
{
	/**The mail address template*/
	protected String mailAddressTemplate;
	/**The mail subject template*/
	protected String mailSubjectTemplate;

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The mail address template property key*/
		MAIL_ADDRESS_TEMPLATE_PROPERTY_KEY("org.fax4j.spi.mail.address.template"),
		/**The mail subject template property key*/
		MAIL_SUBJECT_TEMPLATE_PROPERTY_KEY("org.fax4j.spi.mail.subject.template");

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
	public MailFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//initialize
		super.initializeImpl();
		
		//initialize templates
		this.initializeMailTemplates();

		//get logger
		Logger logger=this.getLogger();

		//validate mail address template
		if(this.mailAddressTemplate==null)
		{
			throw new FaxException("Mail address template not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_PROPERTY_KEY);
		}
		logger.logDebug(new Object[]{"Using mail address template: ",this.mailAddressTemplate},null);
		
		//validate mail subject template
		if(this.mailSubjectTemplate==null)
		{
			throw new FaxException("Mail subject template not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_PROPERTY_KEY);
		}
		logger.logDebug(new Object[]{"Using mail subject template: ",this.mailSubjectTemplate},null);
	}
	
	/**
	 * This function initializes the mail templates.
	 */
	protected void initializeMailTemplates()
	{
		//mail address template
		this.mailAddressTemplate=this.getConfigurationValue(FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_PROPERTY_KEY);

		//mail subject template
		this.mailSubjectTemplate=this.getConfigurationValue(FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_PROPERTY_KEY);
	}
	
	/**
	 * This function returns the mail address template.
	 * 
	 * @return	The mail address template
	 */
	public final String getMailAddressTemplate()
	{
		return this.mailAddressTemplate;
	}
	
	/**
	 * This function returns the mail subject template.
	 * 
	 * @return	The mail subject template
	 */
	public final String getMailSubjectTemplate()
	{
		return this.mailSubjectTemplate;
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
	@Override
	protected Message createSubmitFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder)
	{
		//get logger
		Logger logger=this.getLogger();
		
		//get target address
		String targetAddress=faxJob.getTargetAddress();
		
		//format TO value
		String toAddress=MessageFormat.format(this.mailAddressTemplate,new Object[]{targetAddress});
		logger.logDebug(new Object[]{"Formatted TO address: ",toAddress},null);
		
		//format subject value
		String subject=MessageFormat.format(this.mailSubjectTemplate,new Object[]{targetAddress});
		logger.logDebug(new Object[]{"Formatted subject: ",subject},null);
		
		//get session
		Session session=mailResourcesHolder.getSession();
		
		//create message
		Message message=new MimeMessage(session);
		
		//set from
		String from=faxJob.getSenderEmail();
		if((from!=null)&&(from.length()>0))
		{
			try
			{
				message.setFrom(new InternetAddress(from));
			}
			catch(Exception exception)
			{
				throw new FaxException("Error while setting from address: "+from,exception);
			}
		}

		try
		{
			//add message recipient
			message.addRecipient(RecipientType.TO,new InternetAddress(toAddress));
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while setting TO address: "+toAddress,exception);
		}
		
		try
		{
			//set message subject
			message.setSubject(subject);
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while setting subject: "+subject,exception);
		}

		//create multi part
		Multipart multipart=new MimeMultipart();

		//init body part
		BodyPart messageFileAttachmentBodyPart=new MimeBodyPart();
	
		//init data source
		File file=faxJob.getFile();
		DataSource source=new FileDataSource(file);

		try
		{
			//set data
			messageFileAttachmentBodyPart.setDataHandler(new DataHandler(source));
			messageFileAttachmentBodyPart.setFileName(file.getName());
			
			//add to body
			multipart.addBodyPart(messageFileAttachmentBodyPart);
	
			//set content
			message.setContent(multipart);
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while setting attachment.",exception);
		}
		
		return message;
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
	@Override
	protected Message createSuspendFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder)
	{
		return null;
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
	@Override
	protected Message createResumeFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder)
	{
		return null;
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
	@Override
	protected Message createCancelFaxJobMessage(FaxJob faxJob,MailResourcesHolder mailResourcesHolder)
	{
		return null;
	}
}