package org.fax4j.spi.process;

import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.test.TestUtil;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ProcessFaxClientSpiTest
{
	/**The fax client SPI*/
	private ProcessFaxClientSpi faxClientSpi;
	/**The test fax job*/
	private FaxJob faxJob;
	/**The command suffix*/
	private static final String COMMAND_SUFFIX=" target.address=TEST_TARGET_ADDRESS target.name=TEST_TARGET_NAME sender.name=TEST_SENDER_NAME sender.fax.number=TEST_SENDER_FAX_NUMBER sender.email=TEST_SENDER_EMAIL file: test.txt";

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		String javaHome=System.getProperty("java.home");
		String commandPrefix="\""+javaHome+"\\bin\\java\" -version ";

		Properties configuration=new Properties();
		String templateText="target.address=${target.address} "+
				"target.name=${target.name} "+
				"sender.name=${sender.name} "+
				"sender.fax.number=${sender.fax.number} "+
				"sender.email=${sender.email} "+
				"file: ${file}";		
		configuration.setProperty("org.fax4j.spi.process.submit.template.command",commandPrefix+"submit "+templateText);
		configuration.setProperty("org.fax4j.spi.process.suspend.template.command",commandPrefix+"suspend "+templateText);
		configuration.setProperty("org.fax4j.spi.process.resume.template.command",commandPrefix+"resume "+templateText);
		configuration.setProperty("org.fax4j.spi.process.cancel.template.command",commandPrefix+"cancel "+templateText);
		configuration.setProperty("org.fax4j.spi.process.get.status.template.command",commandPrefix+"get.status "+templateText);
		configuration.setProperty("org.fax4j.spi.process.output.validator",ExitCodeProcessOutputValidator.class.getName());
		configuration.setProperty("org.fax4j.spi.process.output.handler","");
		configuration.setProperty("org.fax4j.spi.process.use.windows.command.prefix","false");

		this.faxClientSpi=(ProcessFaxClientSpi)TestUtil.createFaxClientSpi(ProcessFaxClientSpi.class.getName(),configuration);

		this.faxJob=new FaxJobImpl();
		this.faxJob.setFilePath("test.txt");
		this.faxJob.setTargetAddress("TEST_TARGET_ADDRESS");
		this.faxJob.setTargetName("TEST_TARGET_NAME");
		this.faxJob.setSenderName("TEST_SENDER_NAME");
		this.faxJob.setSenderFaxNumber("TEST_SENDER_FAX_NUMBER");
		this.faxJob.setSenderEmail("TEST_SENDER_EMAIL");
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getPropertyPartTest() throws Exception
	{
		String output=this.faxClientSpi.getPropertyPart();
		Assert.assertNotNull(output);
		Assert.assertEquals("process",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createSubmitFaxJobProcessCommandTest() throws Exception
	{
		String output=this.faxClientSpi.createSubmitFaxJobProcessCommand(this.faxJob);
		Assert.assertNotNull(output);
		Assert.assertTrue(output.endsWith("submit"+ProcessFaxClientSpiTest.COMMAND_SUFFIX));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createSuspendFaxJobProcessCommandTest() throws Exception
	{
		String output=this.faxClientSpi.createSuspendFaxJobProcessCommand(this.faxJob);
		Assert.assertNotNull(output);
		Assert.assertTrue(output.endsWith("suspend"+ProcessFaxClientSpiTest.COMMAND_SUFFIX));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createResumeFaxJobProcessCommandTest() throws Exception
	{
		String output=this.faxClientSpi.createResumeFaxJobProcessCommand(this.faxJob);
		Assert.assertNotNull(output);
		Assert.assertTrue(output.endsWith("resume"+ProcessFaxClientSpiTest.COMMAND_SUFFIX));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createCancelFaxJobProcessCommandTest() throws Exception
	{
		String output=this.faxClientSpi.createCancelFaxJobProcessCommand(this.faxJob);
		Assert.assertNotNull(output);
		Assert.assertTrue(output.endsWith("cancel"+ProcessFaxClientSpiTest.COMMAND_SUFFIX));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createGetStatusFaxJobProcessCommandTest() throws Exception
	{
		String output=this.faxClientSpi.createGetStatusFaxJobProcessCommand(this.faxJob);
		Assert.assertNotNull(output);
		Assert.assertTrue(output.endsWith("get.status"+ProcessFaxClientSpiTest.COMMAND_SUFFIX));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void executeProcessEmptyCommandTest() throws Exception
	{
		this.faxClientSpi.executeProcess(this.faxJob,null,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("","",0);
		FaxJobStatus output=this.faxClientSpi.getFaxJobStatus(processOutput);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void executeProcessTest() throws Exception
	{
		String javaHome=System.getProperty("java.home");
		String command="\""+javaHome+"\\bin\\java\" -version";
		ProcessOutput processOutput=this.faxClientSpi.executeProcess(this.faxJob,command,FaxActionType.SUBMIT_FAX_JOB);
		Assert.assertNotNull(processOutput);
		Assert.assertEquals(0,processOutput.getExitCode());
		Assert.assertNotNull(processOutput.getOutputText());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobImplTest() throws Exception
	{
		this.faxClientSpi.submitFaxJobImpl(this.faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void suspendFaxJobImplTest() throws Exception
	{
		this.faxClientSpi.suspendFaxJobImpl(this.faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void resumeFaxJobImplTest() throws Exception
	{
		this.faxClientSpi.resumeFaxJobImpl(this.faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void cancelFaxJobImplTest() throws Exception
	{
		this.faxClientSpi.cancelFaxJobImpl(this.faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusImplTest() throws Exception
	{
		FaxJobStatus faxJobStatus=this.faxClientSpi.getFaxJobStatusImpl(this.faxJob);
		Assert.assertNotNull(faxJobStatus);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,faxJobStatus);
	}
}