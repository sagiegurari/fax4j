package org.fax4j.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Properties;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.FaxClientSpiProxyImpl2;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxClientSpiFactoryTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxClientSpiTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		FaxClientSpi faxClientSpi=FaxClientSpiFactory.createFaxClientSpi("test",configuration);
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
	public void createFaxClientSpiWithDefaultTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.default.type","test");
		FaxClientSpi faxClientSpi=FaxClientSpiFactory.createFaxClientSpi(null,configuration);
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
	public void createFaxClientSpiWithProxyTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("org.fax4j.proxy.enabled","true");
		configuration.setProperty("org.fax4j.proxy.class.name",FaxClientSpiProxyImpl2.class.getName());
		FaxClientSpi faxClientSpi=FaxClientSpiFactory.createFaxClientSpi("test",configuration);
		Assert.assertNotNull(faxClientSpi);
		Assert.assertTrue(faxClientSpi instanceof Proxy);
		InvocationHandler handler=Proxy.getInvocationHandler(faxClientSpi);
		Assert.assertEquals(FaxClientSpiProxyImpl2.class,handler.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		Map<String,String> map=FaxClientSpiFactory.getConfiguration(configuration);
		Assert.assertNotNull(map);
		Assert.assertEquals(EmptyFaxClientSpi.class.getName(),map.get("org.fax4j.spi.type.map.test"));
		Assert.assertNotNull(map.get("org.fax4j.product.name"));
	}
}