package org.fax4j.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class LogLevelTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void logLevelTest() throws Exception
	{
		Assert.assertTrue(LogLevel.NONE.getValue()>LogLevel.ERROR.getValue());
		Assert.assertTrue(LogLevel.ERROR.getValue()>LogLevel.INFO.getValue());
		Assert.assertTrue(LogLevel.INFO.getValue()>LogLevel.DEBUG.getValue());
	}
}