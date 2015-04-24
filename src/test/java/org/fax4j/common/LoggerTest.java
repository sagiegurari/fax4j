package org.fax4j.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.fax4j.FaxException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class LoggerTest
{
	/**The logger*/
	private AbstractLogger logger;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.logger=new SimpleLogger();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setLogLevelTest() throws Exception
	{
		this.logger.setLogLevel(LogLevel.NONE);
		LogLevel level=this.logger.getLogLevel();
		Assert.assertEquals(LogLevel.NONE,level);
		this.logger.setLogLevel(LogLevel.DEBUG);
		level=this.logger.getLogLevel();
		Assert.assertEquals(LogLevel.DEBUG,level);
		this.logger.setLogLevel(LogLevel.INFO);
		level=this.logger.getLogLevel();
		Assert.assertEquals(LogLevel.INFO,level);
		this.logger.setLogLevel(LogLevel.ERROR);
		level=this.logger.getLogLevel();
		Assert.assertEquals(LogLevel.ERROR,level);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatLogMessageNullValuesTest() throws Exception
	{
		String output=this.logger.formatLogMessage(LogLevel.ERROR,null,null);
		Assert.assertNotNull(output);
		Assert.assertEquals("[fax4j][ERROR] ",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatLogMessageOnlyMessageTest() throws Exception
	{
		String output=this.logger.formatLogMessage(LogLevel.ERROR,new Object[]{"a","b"},null);
		Assert.assertNotNull(output);
		Assert.assertEquals("[fax4j][ERROR] ab",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatLogMessageOnlyThrowableTest() throws Exception
	{
		Throwable throwable=new FaxException("some message",new NumberFormatException("some text"));
		StringWriter writer=new StringWriter(1500);
		throwable.printStackTrace(new PrintWriter(writer));
		String text=writer.toString();

		String output=this.logger.formatLogMessage(LogLevel.ERROR,null,throwable);
		Assert.assertNotNull(output);
		Assert.assertEquals("[fax4j][ERROR] "+text,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatLogMessageMessageAndThrowableTest() throws Exception
	{
		Throwable throwable=new FaxException("some message",new NumberFormatException("some text"));
		StringWriter writer=new StringWriter(1500);
		throwable.printStackTrace(new PrintWriter(writer));
		String text=writer.toString();

		String output=this.logger.formatLogMessage(LogLevel.ERROR,new Object[]{"a","b"},throwable);
		Assert.assertNotNull(output);
		Assert.assertEquals("[fax4j][ERROR] ab"+Logger.SYSTEM_EOL+text,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatMessageTest() throws Exception
	{
		String output=this.logger.format(new Object[]{"a","bc","123"});
		Assert.assertNotNull(output);
		Assert.assertEquals("abc123",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatThrowableTest() throws Exception
	{
		Throwable throwable=new FaxException("some message",new NumberFormatException("some text"));
		StringWriter writer=new StringWriter(1500);
		throwable.printStackTrace(new PrintWriter(writer));
		String text=writer.toString();

		String output=this.logger.format(throwable);
		Assert.assertNotNull(output);
		Assert.assertEquals(text,output);
	}
}