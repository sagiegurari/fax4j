package org.fax4j.spi.vbs;

import org.fax4j.FaxException;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class VBSProcessOutputValidatorTest
{
	/**The validator to test*/
	private VBSProcessOutputValidator validator;
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
		this.validator=new VBSProcessOutputValidator();
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
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=false\n","err",0);
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
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=false\n",".vbs(13, ",0);
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

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateProcessOutputActiveXErrorTest() throws Exception
	{
		ProcessOutput processOutput=new ProcessOutput("fax.operation.done=false","ActiveX component can't create object",0);
		this.validator.validateProcessOutput(this.faxClientSpi,processOutput,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getVBSFailedLineErrorMessageFoundTest() throws Exception
	{
		int lineNumber=15;
		String errorPut="TEST.vbs("+lineNumber+", ";
		Object output=this.validator.getVBSFailedLineErrorMessage(errorPut);
		Assert.assertNotNull(output);
		Assert.assertEquals(" error found at line "+lineNumber+", ",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getVBSFailedLineErrorMessageNotFoundTest() throws Exception
	{
		String errorPut="TEST";
		Object output=this.validator.getVBSFailedLineErrorMessage(errorPut);
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
	public void getVBSFailedLineErrorMessageNullTest() throws Exception
	{
		Object output=this.validator.getVBSFailedLineErrorMessage(null);
		Assert.assertNotNull(output);
		Assert.assertEquals("",output);
	}
}