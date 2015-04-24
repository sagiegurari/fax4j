package org.fax4j.common;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.spi.http.EmptyHTTPResponseHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ServiceFactoryTest
{
	/**The configuration*/
	private ConfigurationHolder configurationHolder;
	
	/**
	 * Test enum.
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static enum ServiceFactoryTestEnum
	{
		/**Test enum*/
		CLASS_NAME_KEY_TEST;
	}
	
	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		Map<String,String> map=new HashMap<String,String>();
		map.put("CLASS_NAME_KEY_TEST",EmptyHTTPResponseHandler.class.getName());
		this.configurationHolder=new ConfigurationHolderImpl(map);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createServiceNullConfigurationTest() throws Exception
	{
		ServiceFactory.createService("abc","abc",null,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createServiceNullClassNameKeyStringTest() throws Exception
	{
		ServiceFactory.createService((String)null,"abc",this.configurationHolder,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createServiceNullClassNameKeyEnumTest() throws Exception
	{
		ServiceFactory.createService((Enum<?>)null,"abc",this.configurationHolder,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createServiceClassNameNotFoundTest() throws Exception
	{
		ServiceFactory.createService("123",null,this.configurationHolder,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createServiceStringTest() throws Exception
	{
		Service output=ServiceFactory.createService("CLASS_NAME_KEY_TEST",null,this.configurationHolder,null);
		Assert.assertNotNull(output);
		Assert.assertEquals(EmptyHTTPResponseHandler.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createServiceEnumTest() throws Exception
	{
		Service output=ServiceFactory.createService(ServiceFactoryTestEnum.CLASS_NAME_KEY_TEST,null,this.configurationHolder,null);
		Assert.assertNotNull(output);
		Assert.assertEquals(EmptyHTTPResponseHandler.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createServiceUseDefaultTest() throws Exception
	{
		Service output=ServiceFactory.createService("CLASS_NAME_KEY_TEST123",EmptyHTTPResponseHandler.class.getName(),this.configurationHolder,null);
		Assert.assertNotNull(output);
		Assert.assertEquals(EmptyHTTPResponseHandler.class,output.getClass());
	}
}