package org.fax4j.util;

import org.fax4j.FaxException;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestConnectionFactory;
import org.fax4j.test.TestUtil.TestResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractConnectionFactoryTest
{
	/**The connection factory to test*/
	private AbstractConnectionFactory<TestResource> connectionFactory;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.connectionFactory=new TestConnectionFactory();
		FaxClientSpi faxClientSpi=TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		this.connectionFactory.initialize(faxClientSpi);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeAgainErrorTest() throws Exception
	{
		FaxClientSpi faxClientSpi=TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
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
		Connection<TestResource> connection=this.connectionFactory.createConnection();
		Assert.assertNotNull(connection);
		TestResource resource=connection.getResource();
		Assert.assertNotNull(resource);
		Assert.assertTrue(resource.isOpen());
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
		Connection<TestResource> connection=this.connectionFactory.createConnection();
		Assert.assertNotNull(connection);
		TestResource resource=connection.getResource();
		Assert.assertNotNull(resource);
		Assert.assertTrue(resource.isOpen());
		this.connectionFactory.releaseConnection(connection);
		Assert.assertFalse(resource.isOpen());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createConnectionForResourceNullResourceTest() throws Exception
	{
		this.connectionFactory.createConnectionForResource(null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createConnectionForResourceValidResourceTest() throws Exception
	{
		Connection<TestResource> connection=this.connectionFactory.createConnectionForResource(new TestResource());
		Assert.assertNotNull(connection);
		TestResource resource=connection.getResource();
		Assert.assertNotNull(resource);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void releaseResourceNullResourceTest() throws Exception
	{
		this.connectionFactory.releaseResource(null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void releaseResourceValidResourceTest() throws Exception
	{
		TestResource resource=new TestResource();
		Assert.assertTrue(resource.isOpen());
		this.connectionFactory.releaseResource(resource);
		Assert.assertFalse(resource.isOpen());
	}
}