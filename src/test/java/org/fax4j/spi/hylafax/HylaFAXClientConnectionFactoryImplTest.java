package org.fax4j.spi.hylafax;

import gnu.hylafax.HylaFAXClient;
import java.util.Properties;
import org.fax4j.test.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class HylaFAXClientConnectionFactoryImplTest
{
	/**The connection factory to test*/
	private HylaFAXClientConnectionFactoryImpl connectionFactory;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.connectionFactory=new HylaFAXClientConnectionFactoryImpl()
		{
			/**
			 * This function creates and returns the HylaFax client.
			 * 
			 * @return	The HylaFax client
			 */
			@Override
			protected HylaFAXClient createHylaFAXClient()
			{
				HylaFAXClient client=Mockito.mock(HylaFAXClient.class);
				return client;
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
	public void createResourceImplTest() throws Exception
	{
		HylaFAXClient client=this.connectionFactory.createResourceImpl();
		Assert.assertNotNull(client);
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
		HylaFAXClient client=this.connectionFactory.createResourceImpl();
		Assert.assertNotNull(client);
		this.connectionFactory.releaseResourceImpl(client);
	}
}