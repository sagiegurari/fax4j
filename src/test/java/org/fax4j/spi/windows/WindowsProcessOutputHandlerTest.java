package org.fax4j.spi.windows;

import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class WindowsProcessOutputHandlerTest
{
	/**The handler*/
	private WindowsProcessOutputHandler processOutputHandler;

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		this.processOutputHandler=new WindowsProcessOutputHandler();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void handleProcessOutputGetFaxJobIDTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("TEST\nfax.operation.done=true\nfax.job.id=123\nTEST",null,0);
		FaxJob faxJob=new FaxJobImpl();
		this.processOutputHandler.updateFaxJob(null,faxJob,processOutput,FaxActionType.SUBMIT_FAX_JOB);
		String output=faxJob.getID();
		Assert.assertNotNull(output);
		Assert.assertEquals("123",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void handleProcessOutputGetFaxJobStatusTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("TEST\nfax.operation.done=true\nfax.job.status=JS_PENDING\nTEST",null,0);
		FaxJobStatus output=this.processOutputHandler.getFaxJobStatus(null,processOutput);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.PENDING,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void handleProcessOutputEmptyStringTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=true",null,0);
		FaxJob faxJob=new FaxJobImpl();
		this.processOutputHandler.updateFaxJob(null,faxJob,processOutput,FaxActionType.SUBMIT_FAX_JOB);
		String output=faxJob.getID();
		Assert.assertNull(output);
	}
}