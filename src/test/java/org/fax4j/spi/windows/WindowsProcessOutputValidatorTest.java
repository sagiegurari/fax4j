package org.fax4j.spi.windows;

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
public class WindowsProcessOutputValidatorTest
{
	/**The validator to test*/
	private WindowsProcessOutputValidator validator;
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
		this.validator=new WindowsProcessOutputValidator();
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
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=true","err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputNoFaxOutputTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("abc","err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputErrorOutputNullMessageTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=false\nfax.error=","err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputErrorOutputEmptyMessageTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=false\nfax.error=\n","err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputErrorOutputWithMessageTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=false\nfax.error=MY ERROR\nSOME MESSAGE\n","err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputNullOutputTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput(null,"err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputEmptyOutputTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("","err",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputInvalidExitCodeTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=true","err",1);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputInvalidNullOutputTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput(null,"err",1);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputInvalidEmptyOutputTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput(null,"err",1);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}
}