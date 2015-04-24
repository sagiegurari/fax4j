package org.fax4j.spi.vbs;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.FaxJobStatus;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.test.TestUtil;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class VBSFaxClientSpiTest
{
	/**The fax client SPI*/
	private VBSFaxClientSpi faxClientSpi;

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
		configuration.setProperty("org.fax4j.spi.vbs.exe.path","cscript.exe");
		configuration.setProperty("org.fax4j.spi.vbs.always.use.win2000.api","true");

		this.faxClientSpi=(VBSFaxClientSpi)TestUtil.createFaxClientSpi(VBSFaxClientSpi.class.getName(),configuration);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getVBSExePathTest() throws Exception
	{
		String output=this.faxClientSpi.getVBSExePath();
		Assert.assertNotNull(output);
		Assert.assertEquals("cscript.exe",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatObjectStringTest() throws Exception
	{
		Object output=this.faxClientSpi.formatObject("\n\r\t\f\b'\"abc");
		Assert.assertNotNull(output);
		Assert.assertEquals("abc",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatObjectFileTest() throws Exception
	{
		File file=File.createTempFile("fax4j_",".temp");
		Object output=this.faxClientSpi.formatObject(file);
		Assert.assertNotNull(output);
		Assert.assertEquals(file.getCanonicalPath().replaceAll("\\\\","\\\\\\\\"),output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatObjectNullTest() throws Exception
	{
		Object output=this.faxClientSpi.formatObject(null);
		Assert.assertNotNull(output);
		Assert.assertEquals("",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatObjectUnsupportedTest() throws Exception
	{
		Integer input=Integer.valueOf(0);
		Object output=this.faxClientSpi.formatObject(input);
		Assert.assertNotNull(output);
		Assert.assertEquals(input,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void generateScriptNameNotFoundTest() throws Exception
	{
		this.faxClientSpi.generateScript("abc",null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void generateScriptNameValidTest() throws Exception
	{
		Object[] input=new Object[10];
		for(int index=0;index<input.length;index++)
		{
			input[index]="##TEST_START"+index+"TEST_END##";
		}
		String output=this.faxClientSpi.generateScript("win2k_submit_fax.vbs",input);
		Assert.assertNotNull(output);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(output.length()));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setupSubmitFaxJobInputWin2kTest() throws Exception
	{
		this.faxClientSpi.useWin2kAPI=true;

		FaxJob faxJob=new FaxJobImpl();
		faxJob.setPriority(FaxJobPriority.HIGH_PRIORITY);
		faxJob.setSenderName("TEST_SENDER");
		faxJob.setTargetName("TEST_TARGET_NAME");
		faxJob.setTargetAddress("TEST_ADDRESS");
		faxJob.setSenderEmail("TEST_SENDER_EMAIL");
		faxJob.setSenderFaxNumber("TEST_SENDER_FAX");
		faxJob.setFilePath("TEST_FILE");
		
		Object[] input=this.faxClientSpi.setupSubmitFaxJobInput(faxJob);
		Assert.assertNotNull(input);
		Assert.assertEquals(this.faxClientSpi.faxServerName,input[0]);
		Assert.assertEquals(new File("TEST_FILE"),input[1]);
		Assert.assertEquals("TEST_FILE",input[2]);
		Assert.assertEquals("TEST_ADDRESS",input[3]);
		Assert.assertEquals("TEST_TARGET_NAME",input[4]);
		Assert.assertEquals("TEST_SENDER",input[5]);
		Assert.assertEquals("TEST_SENDER_FAX",input[6]);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setupSubmitFaxJobInputNotWin2kTest() throws Exception
	{
		this.faxClientSpi.useWin2kAPI=false;

		FaxJob faxJob=new FaxJobImpl();
		faxJob.setPriority(FaxJobPriority.HIGH_PRIORITY);
		faxJob.setSenderName("TEST_SENDER");
		faxJob.setTargetName("TEST_TARGET_NAME");
		faxJob.setTargetAddress("TEST_ADDRESS");
		faxJob.setSenderEmail("TEST_SENDER_EMAIL");
		faxJob.setSenderFaxNumber("TEST_SENDER_FAX");
		faxJob.setFilePath("TEST_FILE");
		
		Object[] input=this.faxClientSpi.setupSubmitFaxJobInput(faxJob);
		Assert.assertNotNull(input);
		Assert.assertEquals(this.faxClientSpi.faxServerName,input[0]);
		Assert.assertEquals(new File("TEST_FILE"),input[1]);
		Assert.assertEquals("TEST_FILE",input[2]);
		Assert.assertEquals("fptHIGH",input[3]);
		Assert.assertEquals("TEST_ADDRESS",input[4]);
		Assert.assertEquals("TEST_TARGET_NAME",input[5]);
		Assert.assertEquals("TEST_SENDER",input[6]);
		Assert.assertEquals("TEST_SENDER_FAX",input[7]);
		Assert.assertEquals("TEST_SENDER_EMAIL",input[8]);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
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
		faxJobStatus=this.faxClientSpi.getFaxJobStatus(faxJob);
		Assert.assertNotNull(faxJobStatus);
		boolean cancelled=false;
		switch(faxJobStatus)
		{
			case UNKNOWN:
				cancelled=true;
				break;
			default:
				submitted=false;
				break;
		}
		Assert.assertTrue(cancelled);
	}
}