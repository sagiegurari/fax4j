package org.fax4j.spi.windows;

import java.lang.reflect.Field;
import java.util.Properties;
import org.fax4j.FaxJob;
import org.fax4j.test.TestUtil;
import org.fax4j.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class WindowsProcessFaxClientSpiTest
{
	/**The fax client SPI*/
	private WindowsProcessFaxClientSpi faxClientSpi;

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		this.faxClientSpi=(WindowsProcessFaxClientSpi)TestUtil.createFaxClientSpi(WindowsProcessFaxClientSpi.class.getName(),null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void addCommandLineArgumentTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder();
		buffer.append("START ");
		this.faxClientSpi.addCommandLineArgument(buffer,"key","value");
		String output=buffer.toString();
		output=output.trim();
		Assert.assertEquals("START key \"value\"",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createProcessCommandTest() throws Exception
	{
		Field field=ReflectionHelper.getField(WindowsProcessFaxClientSpi.class,"fax4jExecutableFileLocation");
		field.set(this.faxClientSpi,"fax4j.exe");
		String output=this.faxClientSpi.createProcessCommand("a b c");
		Assert.assertNotNull(output);
		Assert.assertEquals("\"fax4j.exe\" a b c",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createProcessCommandArgumentsForExistingFaxJobWithServerNameTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.put("org.fax4j.spi.windows.server.name","my_server");
		this.faxClientSpi=(WindowsProcessFaxClientSpi)TestUtil.createFaxClientSpi(WindowsProcessFaxClientSpi.class.getName(),configuration);
		FaxJob faxJob=this.faxClientSpi.createFaxJob();
		faxJob.setID("500");
		String output=this.faxClientSpi.createProcessCommandArgumentsForExistingFaxJob("action_type",faxJob);
		Assert.assertNotNull(output);
		Assert.assertEquals("-action \"action_type\" -server \"my_server\" -job \"500\" ",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createProcessCommandArgumentsForExistingFaxJobWithoutServerNameTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.put("org.fax4j.spi.windows.server.name","");
		this.faxClientSpi=(WindowsProcessFaxClientSpi)TestUtil.createFaxClientSpi(WindowsProcessFaxClientSpi.class.getName(),configuration);
		FaxJob faxJob=this.faxClientSpi.createFaxJob();
		faxJob.setID("500");
		String output=this.faxClientSpi.createProcessCommandArgumentsForExistingFaxJob("action_type",faxJob);
		Assert.assertNotNull(output);
		Assert.assertEquals("-action \"action_type\" -job \"500\" ",output);
	}
}