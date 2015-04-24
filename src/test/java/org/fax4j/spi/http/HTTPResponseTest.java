package org.fax4j.spi.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class HTTPResponseTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentTest() throws Exception
	{
		HTTPResponse response=new HTTPResponse();
		response.setContent("123");
		String output=response.getContent();
		Assert.assertEquals("123",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getStatusCodeTest() throws Exception
	{
		HTTPResponse response=new HTTPResponse();
		response.setStatusCode(123);
		int output=response.getStatusCode();
		Assert.assertEquals(123,output);
	}
}