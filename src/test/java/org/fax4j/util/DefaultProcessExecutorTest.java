package org.fax4j.util;

import java.util.List;
import org.fax4j.FaxException;
import org.fax4j.spi.FaxClientSpi;
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
public class DefaultProcessExecutorTest
{
	/**The process executer to test*/
	private DefaultProcessExecutor processExecutor;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.processExecutor=new DefaultProcessExecutor();
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void executeProcessValidTest() throws Exception
	{
		String javaHome=System.getProperty("java.home");
		String command="\""+javaHome+"\\bin\\java\" -version";
		FaxClientSpi faxClientSpi=new EmptyFaxClientSpi(true);
		ProcessOutput processOutput=this.processExecutor.executeProcess(faxClientSpi,command);
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
	@Test(expected=FaxException.class)
	public void executeProcessNullFaxClientSPITest() throws Exception
	{
		this.processExecutor.executeProcess(null,"123");
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void executeProcessNullCommandTest() throws Exception
	{
		FaxClientSpi faxClientSpi=new EmptyFaxClientSpi(true);
		this.processExecutor.executeProcess(faxClientSpi,null);
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
		FaxClientSpi faxClientSpi=new EmptyFaxClientSpi(true);
		this.processExecutor.executeProcess(faxClientSpi,null);
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void parseCommandTest() throws Exception
	{
		String command="\"a b c\"   \"1 2 3\" A   B   C \"D\"  \" abc def  \"  ";
		List<String> commandList=this.processExecutor.parseCommand(null,command);
		Assert.assertNotNull(commandList);
		Assert.assertEquals("a b c",commandList.get(0));
		Assert.assertEquals("1 2 3",commandList.get(1));
		Assert.assertEquals("A",commandList.get(2));
		Assert.assertEquals("B",commandList.get(3));
		Assert.assertEquals("C",commandList.get(4));
		Assert.assertEquals("D",commandList.get(5));
		Assert.assertEquals(" abc def  ",commandList.get(6));
	}
}