package org.fax4j.spi.email;

import java.util.Properties;
import org.fax4j.test.TestUtil;
import org.fax4j.util.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class MailConnectionFactoryImplTest
{
	/**The connection factory to test*/
	private MailConnectionFactoryImpl connectionFactory;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.connectionFactory=new MailConnectionFactoryImpl();

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.mail.address.template","abc");
		configuration.setProperty("org.fax4j.spi.mail.subject.template","abc");
		configuration.setProperty("org.fax4j.spi.mail.user.name","test@test.com");
		configuration.setProperty("org.fax4j.spi.mail.password","test_password");
		configuration.setProperty("mail.transport.protocol","smtp");	
		configuration.setProperty("mail.smtp.host","localhost");
		configuration.setProperty("mail.smtp.port","123");
		MailFaxClientSpi faxClientSpi=(MailFaxClientSpi)TestUtil.createFaxClientSpi(MailFaxClientSpi.class.getName(),configuration);
		this.connectionFactory.initialize(faxClientSpi);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createConnectionTest() throws Exception
	{
		Connection<MailResourcesHolder> connection=this.connectionFactory.createConnection();
		Assert.assertNotNull(connection);
		MailResourcesHolder holder=connection.getResource();
		Assert.assertNotNull(holder);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void releaseConnectionTest() throws Exception
	{
		Connection<MailResourcesHolder> connection=this.connectionFactory.createConnection();
		Assert.assertNotNull(connection);
		MailResourcesHolder holder=connection.getResource();
		Assert.assertNotNull(holder);
		this.connectionFactory.releaseConnection(connection);
	}
}