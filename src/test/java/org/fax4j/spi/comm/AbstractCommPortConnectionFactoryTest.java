package org.fax4j.spi.comm;

import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.TestCommPortConnectionFactory;
import org.fax4j.test.TestUtil.TestFaxModemAdapter;
import org.fax4j.util.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractCommPortConnectionFactoryTest
{
	/**The connection factory to test*/
	private AbstractCommPortConnectionFactory connectionFactory;
	/**The configuration*/
	private Properties configuration;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.connectionFactory=new TestCommPortConnectionFactory();

		this.configuration=new Properties();
		this.configuration.setProperty("org.fax4j.spi.comm.port.name","abc");
		this.configuration.setProperty("org.fax4j.spi.comm.connection.factory.class.name",TestCommPortConnectionFactory.class.getName());
		this.configuration.setProperty("org.fax4j.spi.comm.fax.modem.class.name",TestFaxModemAdapter.class.getName());
		this.configuration.setProperty("org.fax4j.spi.comm.connection.timeout","123");
		CommFaxClientSpi faxClientSpi=(CommFaxClientSpi)TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),this.configuration);
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
		Assert.assertEquals("abc",this.connectionFactory.getPortName());
		Assert.assertEquals(Integer.valueOf(123),Integer.valueOf(this.connectionFactory.getTimeOut()));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeNoPortNameTest() throws Exception
	{
		this.connectionFactory=new TestCommPortConnectionFactory();

		this.configuration.setProperty("org.fax4j.spi.comm.port.name","");
		CommFaxClientSpi faxClientSpi=(CommFaxClientSpi)TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),this.configuration);
		this.connectionFactory.initialize(faxClientSpi);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeNoTimeoutTest() throws Exception
	{
		this.connectionFactory=new TestCommPortConnectionFactory();

		this.configuration.setProperty("org.fax4j.spi.comm.connection.timeout","");
		CommFaxClientSpi faxClientSpi=(CommFaxClientSpi)TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),this.configuration);
		this.connectionFactory.initialize(faxClientSpi);
		Assert.assertEquals(Integer.valueOf(CommFaxClientSpi.TIMEOUT_DEFAULT_VALUE),Integer.valueOf(this.connectionFactory.getTimeOut()));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeMinusTimeoutTest() throws Exception
	{
		this.connectionFactory=new TestCommPortConnectionFactory();

		this.configuration.setProperty("org.fax4j.spi.comm.connection.timeout","-123");
		CommFaxClientSpi faxClientSpi=(CommFaxClientSpi)TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),this.configuration);
		this.connectionFactory.initialize(faxClientSpi);
		Assert.assertEquals(Integer.valueOf(CommFaxClientSpi.TIMEOUT_DEFAULT_VALUE),Integer.valueOf(this.connectionFactory.getTimeOut()));
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
		Connection<CommPortAdapter> connection=this.connectionFactory.createConnection();
		Assert.assertNotNull(connection);
		CommPortAdapter adapter=connection.getResource();
		Assert.assertTrue(adapter.isOpen());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void releaseResourceImplTest() throws Exception
	{
		Connection<CommPortAdapter> connection=this.connectionFactory.createConnection();
		Assert.assertNotNull(connection);
		CommPortAdapter adapter=connection.getResource();
		Assert.assertTrue(adapter.isOpen());
		this.connectionFactory.releaseResourceImpl(adapter);
		Assert.assertFalse(adapter.isOpen());
	}
}