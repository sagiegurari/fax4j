package org.fax4j.spi.hylafax;

import gnu.hylafax.HylaFAXClient;
import java.util.Properties;
import org.fax4j.test.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractHylaFAXClientConnectionFactoryTest
{
	/**The connection factory to test*/
	private AbstractHylaFAXClientConnectionFactory connectionFactory;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.connectionFactory=new AbstractHylaFAXClientConnectionFactory()
		{
			/**
			 * Creates and returns the resource.
			 *  
			 * @return	The resource
			 */
			@Override
			protected HylaFAXClient createResourceImpl()
			{
				return null;
			}

			/**
			 * Releases the resource from the connection.
			 *  
			 * @param	resource
			 * 			The resource
			 */
			@Override
			protected void releaseResourceImpl(HylaFAXClient resource)
			{
				//empty
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.hylafax.host","TEST_HOST");
		configuration.setProperty("org.fax4j.spi.hylafax.port","123");
		configuration.setProperty("org.fax4j.spi.hylafax.user","TEST_USER");
		configuration.setProperty("org.fax4j.spi.hylafax.password","TEST_PASSWORD");
		configuration.setProperty("org.fax4j.spi.hylafax.admin","true");
		configuration.setProperty("org.fax4j.spi.hylafax.mode","A");
		configuration.setProperty("org.fax4j.spi.hylafax.type","B");

		HylaFaxClientSpi faxClientSpi=(HylaFaxClientSpi)TestUtil.createFaxClientSpi(HylaFaxClientSpi.class.getName(),configuration);
		this.connectionFactory.initialize(faxClientSpi);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeTest() throws Exception
	{
		Assert.assertEquals("TEST_HOST",this.connectionFactory.host);
		Assert.assertEquals(Integer.valueOf(123),Integer.valueOf(this.connectionFactory.port));
		Assert.assertEquals("TEST_USER",this.connectionFactory.userName);
		Assert.assertEquals("TEST_PASSWORD",this.connectionFactory.password);
		Assert.assertEquals(Boolean.TRUE,Boolean.valueOf(this.connectionFactory.enableAdminOperations));
		Assert.assertEquals(Character.valueOf('A'),Character.valueOf(this.connectionFactory.mode));
		Assert.assertEquals(Character.valueOf('B'),Character.valueOf(this.connectionFactory.type));
	}
}