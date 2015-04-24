package org.fax4j.bridge.process;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class Process2FaxBridgeTest
{
	/**The fax bridge to test*/
	private Process2FaxBridge faxBridge;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.faxBridge=new Process2FaxBridge();

		Properties configuration=new Properties();
		configuration.setProperty(Process2FaxBridge.COMMAND_PARSER_CLASS_NAME_PROPERTY_KEY,DefaultCommandLineArgumentsParser.class.getName());
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
	public void initializeEmptyParserClassNameTest() throws Exception
	{
		this.faxBridge=new Process2FaxBridge();

		Properties configuration=new Properties();
		configuration.setProperty(Process2FaxBridge.COMMAND_PARSER_CLASS_NAME_PROPERTY_KEY,"");
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
	public void initializeNullParserClassNameTest() throws Exception
	{
		this.faxBridge=new Process2FaxBridge();

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
	@Test(expected=FaxException.class)
	public void initializeInvalidParserClassNameTest() throws Exception
	{
		this.faxBridge=new Process2FaxBridge();

		Properties configuration=new Properties();
		configuration.setProperty(Process2FaxBridge.COMMAND_PARSER_CLASS_NAME_PROPERTY_KEY,"123");
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
	public void submitFaxJobValidTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);

		String[] input=new String[]{"-target_address","12345","-file",file.getPath()};
		FaxJob faxJob=this.faxBridge.submitFaxJob(input);
		Assert.assertNotNull(faxJob);
		Assert.assertNotNull(faxJob.getFile());
		Assert.assertTrue(faxJob.getFile().exists());
		
		file.delete();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullArgumentsTest() throws Exception
	{
		this.faxBridge.submitFaxJob(null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullFileInfoTest() throws Exception
	{
		String[] input=new String[]{"NullFileInfoTest"};
		this.faxBridge.submitFaxJob(input);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullFaxJobTest() throws Exception
	{
		String[] input=new String[]{"NullFaxJobTest"};
		this.faxBridge.submitFaxJob(input);
	}
}