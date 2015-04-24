package org.fax4j.spi.mac;

import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.process.ExitCodeProcessOutputValidator;
import org.fax4j.spi.process.ProcessOutputValidator;
import org.fax4j.test.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class MacFaxClientSpiTest
{
	/**The fax client SPI*/
	private MacFaxClientSpi faxClientSpi;
	/**The command string value*/
	private String command;

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
		this.command="\""+javaHome+"\\bin\\java\" -version";

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.mac.submit.command",this.command);
		configuration.setProperty("org.fax4j.spi.mac.print.queue.parameter","testp2");
		configuration.setProperty("org.fax4j.spi.mac.print.queue.name","testp3");
		configuration.setProperty("org.fax4j.spi.mac.general.parameters","testp4");
		configuration.setProperty("org.fax4j.spi.mac.phone.parameter","testp5");
		configuration.setProperty("org.fax4j.spi.mac.fax.to.parameter","testp6");
		this.faxClientSpi=(MacFaxClientSpi)TestUtil.createFaxClientSpi(MacFaxClientSpi.class.getName(),configuration);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeImplMissingPrintQueueNameTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.mac.submit.command",this.command);
		configuration.setProperty("org.fax4j.spi.mac.print.queue.parameter","testp2");
		configuration.setProperty("org.fax4j.spi.mac.print.queue.name","");
		configuration.setProperty("org.fax4j.spi.mac.general.parameters","testp4");
		configuration.setProperty("org.fax4j.spi.mac.phone.parameter","testp5");
		configuration.setProperty("org.fax4j.spi.mac.fax.to.parameter","testp6");
		TestUtil.createFaxClientSpi(MacFaxClientSpi.class.getName(),configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeImplDefaultValuesTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.mac.submit.command","");
		configuration.setProperty("org.fax4j.spi.mac.print.queue.parameter","");
		configuration.setProperty("org.fax4j.spi.mac.print.queue.name","testp3");
		configuration.setProperty("org.fax4j.spi.mac.general.parameters","");
		configuration.setProperty("org.fax4j.spi.mac.phone.parameter","");
		configuration.setProperty("org.fax4j.spi.mac.fax.to.parameter","");
		this.faxClientSpi=(MacFaxClientSpi)TestUtil.createFaxClientSpi(MacFaxClientSpi.class.getName(),configuration);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123123123");
		faxJob.setTargetName("TARGET_NAME");
		faxJob.setFilePath("test_file");
		String output=this.faxClientSpi.createSubmitFaxCommand(faxJob);
		Assert.assertNotNull(output);
		Assert.assertEquals("lp -d testp3 -o phone=123123123 faxTo=\"TARGET_NAME\" \"test_file\"",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createMacProcessOutputValidatorTest() throws Exception
	{
		ProcessOutputValidator output=this.faxClientSpi.createMacProcessOutputValidator();
		Assert.assertNotNull(output);
		Assert.assertEquals(ExitCodeProcessOutputValidator.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createSubmitFaxCommandTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123123123");
		faxJob.setTargetName("TARGET_NAME");
		faxJob.setFilePath("test_file");
		String output=this.faxClientSpi.createSubmitFaxCommand(faxJob);
		Assert.assertNotNull(output);
		Assert.assertEquals(this.command+" testp2 testp3 testp4 testp5=123123123 testp6=\"TARGET_NAME\" \"test_file\"",output);
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
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setSenderName("TEST");
		faxJob.setTargetName("TEST");
		faxJob.setTargetAddress("TEST");
		faxJob.setSenderEmail("TEST");
		faxJob.setSenderFaxNumber("111");
		faxJob.setFilePath("TEST");
		this.faxClientSpi.submitFaxJobImpl(faxJob);
	}
}