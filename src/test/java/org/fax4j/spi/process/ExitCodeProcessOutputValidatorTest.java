package org.fax4j.spi.process;

import org.fax4j.FaxException;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ExitCodeProcessOutputValidatorTest
{
	/**The validator to test*/
	private ExitCodeProcessOutputValidator validator;
	/**Empty fax client SPI*/
	private FaxClientSpi faxClientSpi;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		this.validator=new ExitCodeProcessOutputValidator();
		this.faxClientSpi=TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateProcessOutputValidTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("out","err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputInvalidTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("out","err",1);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}
}