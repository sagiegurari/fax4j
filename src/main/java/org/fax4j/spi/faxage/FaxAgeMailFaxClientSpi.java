package org.fax4j.spi.faxage;

import org.fax4j.spi.email.MailFaxClientSpi;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will invoke the requests by sending emails to a mail server that supports
 * conversion between email messages and fax messages.<br>
 * The FaxAge mail SPI supports persistent connection to enable to reuse the same connection for all fax
 * operation invocations or to create a new connection for each fax operation invocation.<br>
 * By default the SPI will create a new connection for each operation invocation however the 
 * <b>org.fax4j.spi.mail.persistent.connection</b> set to true will enable to reuse the connection.<br>
 * To set the user/password values of the mail connection the following 2 properties must be defined:
 * <b>org.fax4j.spi.mail.user.name</b> and <b>org.fax4j.spi.mail.password</b><br>
 * All properties defined in the fax4j configuration will be passed to the mail connection therefore it is
 * possible to define mail specific properties (see java mail for more info) in the fax4j properties.<br>
 * This SPI only supports to submit new fax jobs.<br>
 * This implementation is not meant to promote the commercial service but to serve
 * as a service provider interface for it.<br>
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
 * 	<li>Currently this SPI only supports submitting new fax jobs.
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>Required jar files: mail-1.4.jar, activation-1.1.jar
 * </ul>
 * <br>
 * 
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.21c
 */
public class FaxAgeMailFaxClientSpi extends MailFaxClientSpi
{
	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.21c
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The mail address template value*/
		MAIL_ADDRESS_TEMPLATE_VALUE("{0}@faxage.com"),
		/**The mail subject template value*/
		MAIL_SUBJECT_TEMPLATE_VALUE("fax");

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
	public FaxAgeMailFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the mail templates.
	 */
	@Override
	protected void initializeMailTemplates()
	{
		//mail address template
		this.mailAddressTemplate=FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_VALUE.toString();

		//mail subject template
		this.mailSubjectTemplate=FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_VALUE.toString();
	}
}