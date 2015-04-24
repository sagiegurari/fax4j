package org.fax4j.spi.linux;

import java.util.Properties;
import org.fax4j.spi.process.ProcessOutputValidator;
import org.fax4j.spi.windows.WindowsProcessOutputValidator;
import org.fax4j.test.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class LinuxFaxClientSpiTest
{
	/**The fax client SPI*/
	private LinuxFaxClientSpi faxClientSpi;

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
		configuration.setProperty("org.fax4j.spi.linux.submit.template.command","abc");
		configuration.setProperty("org.fax4j.spi.linux.output.validator",WindowsProcessOutputValidator.class.getName());
		this.faxClientSpi=(LinuxFaxClientSpi)TestUtil.createFaxClientSpi(LinuxFaxClientSpi.class.getName(),configuration);
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
		Assert.assertEquals("linux",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getProcessOutputValidatorTest() throws Exception
	{
		ProcessOutputValidator output=this.faxClientSpi.getProcessOutputValidator();
		Assert.assertNotNull(output);
		Assert.assertEquals(WindowsProcessOutputValidator.class,output.getClass());
	}
}