package org.fax4j.spi.email;

import java.util.Iterator;
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
public class AbstractMailConnectionFactoryTest
{
	/**The connection factory to test*/
	private AbstractMailConnectionFactory connectionFactory;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.connectionFactory=new AbstractMailConnectionFactory()
		{
			@Override
			protected MailResourcesHolder createResourceImpl()
			{
				return new MailResourcesHolder(null,null);
			}

			@Override
			protected void releaseResourceImpl(MailResourcesHolder resource)
			{
				//empty
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.mail.address.template","abc");
		configuration.setProperty("org.fax4j.spi.mail.subject.template","abc");
		configuration.setProperty("org.fax4j.spi.mail.user.name","TEST_USER");
		configuration.setProperty("org.fax4j.spi.mail.password","TEST_PASSWORD");
		configuration.setProperty("mail.transport.protocol","TEST_TRANSPORT_PROTOCOL");	
		configuration.setProperty("mail.smtp.host","TEST_TRANSPORT_HOST");
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
	public void initializeValidTest() throws Exception
	{
		Assert.assertNotNull(this.connectionFactory.mailConnectionProperties);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(this.connectionFactory.mailConnectionProperties.size()));
		Assert.assertEquals("TEST_USER",this.connectionFactory.userName);
		Assert.assertEquals("TEST_PASSWORD",this.connectionFactory.password);
		Assert.assertEquals("TEST_TRANSPORT_PROTOCOL",this.connectionFactory.transportProtocol);
		Assert.assertEquals("TEST_TRANSPORT_HOST",this.connectionFactory.transportHost);
		Assert.assertEquals(Integer.valueOf(123),Integer.valueOf(this.connectionFactory.transportPort));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeNoTransportProtocolTest() throws Exception
	{
		AbstractMailConnectionFactory factory=new AbstractMailConnectionFactory()
		{
			@Override
			protected MailResourcesHolder createResourceImpl()
			{
				return new MailResourcesHolder(null,null);
			}

			@Override
			protected void releaseResourceImpl(MailResourcesHolder resource)
			{
				//empty
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.mail.address.template","abc");
		configuration.setProperty("org.fax4j.spi.mail.subject.template","abc");
		configuration.setProperty("org.fax4j.spi.mail.user.name","TEST_USER");
		configuration.setProperty("org.fax4j.spi.mail.password","TEST_PASSWORD");
		configuration.setProperty("mail.transport.protocol","");	
		configuration.setProperty("mail.smtp.host","TEST_TRANSPORT_HOST");
		configuration.setProperty("mail.smtp.port","123");
		MailFaxClientSpi faxClientSpi=(MailFaxClientSpi)TestUtil.createFaxClientSpi(MailFaxClientSpi.class.getName(),configuration);
		factory.initialize(faxClientSpi);

		Assert.assertEquals("smtp",factory.transportProtocol);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeMailConfigurationTest() throws Exception
	{
		Properties properties=this.connectionFactory.mailConnectionProperties;
		Assert.assertNotNull(properties);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(properties.size()));
		Iterator<Object> iterator=properties.keySet().iterator();
		String key=null;
		while(iterator.hasNext())
		{
			key=(String)iterator.next();
			Assert.assertEquals(-1,key.indexOf("org.fax4j."));
		}
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