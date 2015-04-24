package org.fax4j.spi.vbs;

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
public class VBSProcessOutputHandlerTest
{
	/**The handler*/
	private VBSProcessOutputHandler processOutputHandler;

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		this.processOutputHandler=new VBSProcessOutputHandler();
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
		ProcessOutput processOutput=new ProcessOutput("TEST\nfax.operation.done=true\nfax.job.status=Pending\nTEST",null,0);
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

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void parseFaxJobStatusFromScriptOutputNullTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString(null);
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void parseFaxJobStatusFromScriptOutputEmptyStringTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void parseFaxJobStatusFromScriptOutputUnknownValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("abcd");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringNullTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString(null);
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringEmptyStringTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringUnknownValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("abcd");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsPendingValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("Pending");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.PENDING,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsPausedValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("Paused");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.PENDING,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsRetryingValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("Retrying");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.PENDING,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsInProgressValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("In Progress");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.IN_PROGRESS,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsFailedValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("Failed");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.ERROR,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsNoLineValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("No Line");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.ERROR,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsRetriesExceededValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.processOutputHandler.getFaxJobStatusFromWindowsFaxJobStatusString("Retries Exceeded");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.ERROR,faxJobStatus);
	}
}