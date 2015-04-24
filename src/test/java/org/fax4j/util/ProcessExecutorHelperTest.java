package org.fax4j.util;

import org.fax4j.spi.FaxClientSpi;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ProcessExecutorHelperTest
{
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
		FaxClientSpi faxClientSpi=new EmptyFaxClientSpi(true);
		ProcessOutput processOutput=ProcessExecutorHelper.executeProcess(faxClientSpi,command);
		Assert.assertNotNull(processOutput);
		Assert.assertEquals(0,processOutput.getExitCode());
		Assert.assertNotNull(processOutput.getOutputText());
	}
}