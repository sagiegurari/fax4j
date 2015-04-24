package org.fax4j.spi.windows;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.test.TestUtil;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class WindowsFaxClientSpiTest
{
	/**The fax client SPI*/
	private WindowsFaxClientSpi faxClientSpi;

	/**
	 * Setup.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@BeforeClass
    public static void beforeClass() throws Exception
	{
		//get target directory
		File directory=IOHelper.getFax4jInternalTemporaryDirectory();
		
		//extract resources
		String[] names=new String[]{"fax4j.dll","fax4j.exe"};
		int amount=names.length;
		String name=null;
		File file=null;
		for(int index=0;index<amount;index++)
		{
			//get next resource
			name=names[index];

			InputStream inputStream=WindowsFaxClientSpiHelper.class.getResourceAsStream(name);
			if(inputStream!=null)
			{
				IOHelper.closeResource(inputStream);
				file=new File(directory,name);
				file.delete();
			}
		}
	}

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.windows.use.jni","false");
		this.faxClientSpi=(WindowsFaxClientSpi)TestUtil.createFaxClientSpi(WindowsFaxClientSpi.class.getName(),configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFaxJobStatusImplNoExistingFaxJobTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("-123132");	
		this.faxClientSpi.getFaxJobStatus(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void nativeFlowViaJNITest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.windows.use.jni","true");
		this.faxClientSpi=(WindowsFaxClientSpi)TestUtil.createFaxClientSpi(WindowsFaxClientSpi.class.getName(),configuration);
		this.nativeFlowTest();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void nativeFlowViaExeTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.windows.use.jni","false");
		this.faxClientSpi=(WindowsFaxClientSpi)TestUtil.createFaxClientSpi(WindowsFaxClientSpi.class.getName(),configuration);
		this.nativeFlowTest();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	public void nativeFlowTest() throws Exception
	{
		Assume.assumeTrue(TestUtil.isWindowsOS());

		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setSenderName("mysender");
		faxJob.setTargetName("mytargetname");
		faxJob.setTargetAddress("123");
		faxJob.setSenderEmail("aaa@aaaaaa.coma");
		faxJob.setFilePath(file.getPath());	

		this.faxClientSpi.submitFaxJob(faxJob);
		file.delete();
		String faxJobID=faxJob.getID();
		Assert.assertNotNull(faxJobID);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(faxJobID.length()));		
		FaxJobStatus faxJobStatus=this.faxClientSpi.getFaxJobStatus(faxJob);
		Assert.assertNotNull(faxJobStatus);
		boolean submitted=false;
		switch(faxJobStatus)
		{
			case IN_PROGRESS:
			case PENDING:
				submitted=true;
				break;
			default:
				submitted=false;
				break;
		}
		Assert.assertTrue(submitted);
		
		this.faxClientSpi.suspendFaxJob(faxJob);	
		faxJobStatus=this.faxClientSpi.getFaxJobStatus(faxJob);
		Assert.assertNotNull(faxJobStatus);
		boolean suspended=false;
		switch(faxJobStatus)
		{
			case PENDING:
				suspended=true;
				break;
			default:
				suspended=false;
				break;
		}
		Assert.assertTrue(suspended);

		this.faxClientSpi.resumeFaxJob(faxJob);	
		faxJobStatus=this.faxClientSpi.getFaxJobStatus(faxJob);
		Assert.assertNotNull(faxJobStatus);
		submitted=false;
		switch(faxJobStatus)
		{
			case IN_PROGRESS:
			case PENDING:
				submitted=true;
				break;
			default:
				submitted=false;
				break;
		}
		Assert.assertTrue(submitted);

		this.faxClientSpi.cancelFaxJob(faxJob);
	}
}