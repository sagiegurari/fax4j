package org.fax4j.spi.hylafax;

import gnu.hylafax.HylaFAXClient;
import java.io.File;
import java.util.Properties;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.TestHylaFAXClientConnectionFactory;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class HylaFaxClientSpiTest
{
	/**The fax client SPI*/
	private HylaFaxClientSpi faxClientSpi;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.hylafax.connection.factory.class.name",TestHylaFAXClientConnectionFactory.class.getName());
		configuration.setProperty("org.fax4j.spi.hylafax.host","TEST_HOST");
		configuration.setProperty("org.fax4j.spi.hylafax.port","123");
		configuration.setProperty("org.fax4j.spi.hylafax.user","TEST_USER");
		configuration.setProperty("org.fax4j.spi.hylafax.password","TEST_PASSWORD");
		configuration.setProperty("org.fax4j.spi.hylafax.admin","true");
		configuration.setProperty("org.fax4j.spi.hylafax.mode","A");
		configuration.setProperty("org.fax4j.spi.hylafax.type","B");
		this.faxClientSpi=(HylaFaxClientSpi)TestUtil.createFaxClientSpi(HylaFaxClientSpi.class.getName(),configuration);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHylaFAXClientTest() throws Exception
	{
		HylaFAXClient client=this.faxClientSpi.getHylaFAXClient();
		Assert.assertNotNull(client);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxJobImplTest() throws Exception
	{
		HylaFaxJob faxJob=(HylaFaxJob)this.faxClientSpi.createFaxJobImpl();
		Assert.assertNotNull(faxJob);
		Assert.assertNotNull(faxJob.getHylaFaxJob());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);
		HylaFaxJob faxJob=(HylaFaxJob)this.faxClientSpi.createFaxJobImpl();
		faxJob.setSenderName("mysender");
		faxJob.setTargetName("mytargetname");
		faxJob.setTargetAddress("123");
		faxJob.setSenderEmail("aaa@aaaaaa.coma");
		faxJob.setFilePath(file.getPath());	

		this.faxClientSpi.submitFaxJob(faxJob);
		
		String faxJobID=faxJob.getID();
		Assert.assertNotNull(faxJobID);
		Assert.assertEquals(123,Long.parseLong(faxJobID));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobImplTest() throws Exception
	{
		HylaFaxJob faxJob=(HylaFaxJob)this.faxClientSpi.createFaxJobImpl();
		faxJob.setFilePath("TEST");	

		this.faxClientSpi.submitFaxJobImpl(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void suspendFaxJobImplTest() throws Exception
	{
		HylaFaxJob faxJob=(HylaFaxJob)this.faxClientSpi.createFaxJobImpl();
		this.faxClientSpi.suspendFaxJobImpl(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void resumeFaxJobImplTest() throws Exception
	{
		HylaFaxJob faxJob=(HylaFaxJob)this.faxClientSpi.createFaxJobImpl();
		this.faxClientSpi.resumeFaxJobImpl(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void cancelFaxJobImplTest() throws Exception
	{
		HylaFaxJob faxJob=(HylaFaxJob)this.faxClientSpi.createFaxJobImpl();
		this.faxClientSpi.cancelFaxJobImpl(faxJob);
	}
}