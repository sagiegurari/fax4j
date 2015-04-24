package org.fax4j.util;

import java.util.Map;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class LibraryConfigurationLoaderTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void readInternalConfigurationTest() throws Exception
	{
		Properties output=LibraryConfigurationLoader.readInternalConfiguration();
		Assert.assertNotNull(output);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(output.size()));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void readInternalAndExternalConfigurationTest() throws Exception
	{
		Properties output=LibraryConfigurationLoader.readInternalAndExternalConfiguration();
		Assert.assertNotNull(output);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(output.size()));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSystemConfigurationTest() throws Exception
	{
		Map<String,String> properties=LibraryConfigurationLoader.getSystemConfiguration();
		Assert.assertNotNull(properties);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(properties.size()));
	}
}