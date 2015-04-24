package org.fax4j.bridge;

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
public class FaxBridgeImplTest
{
	/**The fax bridge to test*/
	private FaxBridgeImpl faxBridge;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.faxBridge=new FaxBridgeImpl();

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
	public void initializeInvalidVendorPolicyClassNameTest() throws Exception
	{
		this.faxBridge=new FaxBridgeImpl();
		Properties configuration=new Properties();
		configuration.setProperty(FaxBridge.VENDOR_POLICY_CLASS_NAME,"BAD_VALUE");
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
	public void updateFaxJobWithFileInfoTest() throws Exception
	{
		FaxJob faxJob=this.faxBridge.createFaxJob();
		byte[] content="1234567890".getBytes(IOHelper.getDefaultEncoding());
		FileInfo fileInfo=new FileInfo("abc.txt",content);
		this.faxBridge.updateFaxJobWithFileInfo(faxJob,fileInfo);
		File file=faxJob.getFile();
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		String text=IOHelper.readTextFile(file);
		Assert.assertNotNull(file);
		byte[] textBytes=text.getBytes(IOHelper.getDefaultEncoding());
		Assert.assertArrayEquals(content,textBytes);
		
		file.delete();
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
		FaxJob faxJob=this.faxBridge.createFaxJob();
		faxJob.setTargetAddress("12345");
		FileInfo fileInfo=new FileInfo("abc.txt",new byte[10]);
		this.faxBridge.submitFaxJob(faxJob,fileInfo);
	}
}