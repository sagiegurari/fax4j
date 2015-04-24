package org.fax4j.bridge;

import java.util.Properties;
import org.fax4j.FaxJob;
import org.fax4j.Provider;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractFax4JBridgeTest
{
	/**The fax bridge to test*/
	private AbstractFax4JBridge faxBridge;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.faxBridge=new AbstractFax4JBridge()
		{
			@Override
			protected void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo)
			{
				//empty
			}
			@Override
			protected VendorPolicy createVendorPolicy()
			{
				return new EmptyVendorPolicy();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		this.faxBridge.initialize("test",configuration,new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getProviderTest() throws Exception
	{
		Provider output=this.faxBridge.getProvider();
		Assert.assertNotNull(output);
		Assert.assertEquals(Fax4JProvider.FAX4J_PROVIDER,output);
	}
}