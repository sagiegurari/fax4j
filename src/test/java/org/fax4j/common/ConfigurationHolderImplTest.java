package org.fax4j.common;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.test.TestUtil.TestEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ConfigurationHolderImplTest
{
	/**The configuration holder base map*/
	private Map<String,String> map;
	/**The configuration holder to test*/
	private ConfigurationHolderImpl configurationHolder;
	
	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.map=new HashMap<String,String>();
		for(int index=0;index<10;index++)
		{
			this.map.put("KEY"+index,"value"+index);
		}
		for(int index=0;index<10;index++)
		{
			this.map.put("EMPTY"+index,"");
		}
		this.configurationHolder=new ConfigurationHolderImpl(this.map);
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
		Map<String,String> output=this.configurationHolder.getConfiguration();
		Assert.assertNotNull(output);
		Assert.assertEquals(this.map,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void getConfigurationModifyTest() throws Exception
	{
		Map<String,String> output=this.configurationHolder.getConfiguration();
		Assert.assertNotNull(output);
		output.remove("KEY1");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueStringFoundTest() throws Exception
	{
		String output=this.configurationHolder.getConfigurationValue("KEY1");
		Assert.assertNotNull(output);
		Assert.assertEquals("value1",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueStringNotFoundTest() throws Exception
	{
		String output=this.configurationHolder.getConfigurationValue("NOT_FOUND");
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueStringEmptyValueTest() throws Exception
	{
		String output=this.configurationHolder.getConfigurationValue("EMPTY1");
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueEnumFoundTest() throws Exception
	{
		String output=this.configurationHolder.getConfigurationValue(TestEnum.KEY1);
		Assert.assertNotNull(output);
		Assert.assertEquals("value1",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueEnumNotFoundTest() throws Exception
	{
		String output=this.configurationHolder.getConfigurationValue(TestEnum.KEY_TEST);
		Assert.assertNull(output);
	}
}