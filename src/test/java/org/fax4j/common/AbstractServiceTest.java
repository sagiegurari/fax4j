package org.fax4j.common;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.Provider;
import org.fax4j.test.TestUtil.TestEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractServiceTest
{
	/**The service to test*/
	private AbstractService service;
	/**The configuration*/
	private Map<String,String> configuration;
	
	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.service=new AbstractService(Fax4JProvider.FAX4J_PROVIDER)
		{
			@Override
			protected void initializeImpl()
			{
				//empty
			}
		};
		
		this.configuration=new HashMap<String,String>();
		this.configuration.put("KEY1","value1");
		this.configuration.put("KEY2","value2");
		this.configuration.put("KEY3","value3");
		this.configuration.put(TestEnum.KEY1.name(),TestEnum.KEY_TEST.name());
		this.service.initialize(this.configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initAbstractServiceNullProviderTest() throws Exception
	{
		this.service=new AbstractService(null)
		{
			@Override
			protected void initializeImpl()
			{
				//empty
			}
		};
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeAgainTest() throws Exception
	{
		this.service.initialize(this.configuration);
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
		Provider output=this.service.getProvider();
		Assert.assertNotNull(output);
		Assert.assertEquals(Fax4JProvider.FAX4J_PROVIDER,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getLoggerTest() throws Exception
	{
		Logger output=this.service.getLogger();
		Assert.assertNotNull(output);
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
		Map<String,String> output=this.service.getConfiguration();
		Assert.assertNotNull(output);
		Assert.assertEquals(this.configuration,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueStringTest() throws Exception
	{
		String output=this.service.getConfigurationValue("KEY2");
		Assert.assertNotNull(output);
		Assert.assertEquals("value2",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueEnumTest() throws Exception
	{
		String output=this.service.getConfigurationValue(TestEnum.KEY1);
		Assert.assertNotNull(output);
		Assert.assertEquals(TestEnum.KEY_TEST.name(),output);
	}
}