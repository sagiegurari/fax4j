package org.fax4j;

import java.util.Properties;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.FaxClient2;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxClientFactoryTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxClientWithSimpleParametersTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		FaxClient faxClient=FaxClientFactory.createFaxClient("test",configuration);
		Assert.assertNotNull(faxClient);
		FaxClientSpi faxClientSpi=faxClient.getFaxClientSpi();
		Assert.assertNotNull(faxClientSpi);
		Assert.assertEquals(EmptyFaxClientSpi.class,faxClientSpi.getClass());		
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxClientWithExtendedFaxClientTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("org.fax4j.client.class.name",FaxClient2.class.getName());
		FaxClient faxClient=FaxClientFactory.createFaxClient("test",configuration);
		Assert.assertNotNull(faxClient);
		FaxClientSpi faxClientSpi=faxClient.getFaxClientSpi();
		Assert.assertNotNull(faxClientSpi);
		Assert.assertEquals(FaxClient2.class,faxClient.getClass());		
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxClientWithNoFaxClientTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("org.fax4j.client.class.name","");
		FaxClient faxClient=FaxClientFactory.createFaxClient("test",configuration);
		Assert.assertNotNull(faxClient);
		FaxClientSpi faxClientSpi=faxClient.getFaxClientSpi();
		Assert.assertNotNull(faxClientSpi);
		Assert.assertEquals(FaxClient.class,faxClient.getClass());		
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxClientWithNoParametersTest() throws Exception
	{
		FaxClient faxClient=FaxClientFactory.createFaxClient();
		Assert.assertNotNull(faxClient);
		FaxClientSpi faxClientSpi=faxClient.getFaxClientSpi();
		Assert.assertNotNull(faxClientSpi);
		Assert.assertEquals(FaxClient.class,faxClient.getClass());		
	}
}