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
public class ConnectionImplTest
{
	/**The connection factory to use*/
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
	public void createConnectionForResourceNullResourceTest() throws Exception
	{
		Connection<TestResource> connection=new ConnectionImpl<TestResource>(null,this.connectionFactory);
		Assert.assertNull(connection);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createConnectionForResourceNullConnectionFactoryTest() throws Exception
	{
		Connection<TestResource> connection=new ConnectionImpl<TestResource>(new TestResource(),null);
		Assert.assertNull(connection);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getResourceTest() throws Exception
	{
		Connection<TestResource> connection=new ConnectionImpl<TestResource>(new TestResource(),this.connectionFactory);
		TestResource resource=connection.getResource();
		Assert.assertNotNull(resource);
		Assert.assertTrue(resource.isOpen());
		connection.close();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void closeTest() throws Exception
	{
		Connection<TestResource> connection=new ConnectionImpl<TestResource>(new TestResource(),this.connectionFactory);
		TestResource resource=connection.getResource();
		Assert.assertNotNull(resource);
		Assert.assertTrue(resource.isOpen());
		connection.close();
		Assert.assertFalse(resource.isOpen());
	}
}