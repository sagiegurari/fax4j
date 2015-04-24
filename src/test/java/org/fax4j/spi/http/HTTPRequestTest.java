package org.fax4j.spi.http;

import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.spi.http.HTTPRequest.ContentType;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class HTTPRequestTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getResourceWithDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setResource("abc");
		String output=request.getResource();
		Assert.assertEquals("abc",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getResourceNullDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setResource(null);
		String output=request.getResource();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getParametersTextWithDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setParametersText("abc");
		String output=request.getParametersText();
		Assert.assertEquals("abc",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getParametersTextNullDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setParametersText(null);
		String output=request.getParametersText();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHeaderPropertiesWithDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		Properties properties=new Properties();
		properties.setProperty("key","value");
		request.setHeaderProperties(properties);
		Properties output=request.getHeaderProperties();
		Assert.assertEquals(properties.size(),output.size());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHeaderPropertiesNullDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setHeaderProperties(null);
		Properties output=request.getHeaderProperties();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentTypeInitTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		ContentType output=request.getContentType();
		Assert.assertEquals(ContentType.STRING,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentTypeBinaryDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent(new byte[0]);
		ContentType output=request.getContentType();
		Assert.assertEquals(ContentType.BINARY,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentTypeStringDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent("");
		ContentType output=request.getContentType();
		Assert.assertEquals(ContentType.STRING,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentTypeMultiPartDataTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent(new ContentPart[0]);
		ContentType output=request.getContentType();
		Assert.assertEquals(ContentType.MULTI_PART,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentBinaryDataValidTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent("123".getBytes(IOHelper.getDefaultEncoding()));
		byte[] output=request.getContentAsBinary();
		Assert.assertArrayEquals("123".getBytes(IOHelper.getDefaultEncoding()),output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentBinaryDataNullTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent((byte[])null);
		byte[] output=request.getContentAsBinary();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getContentBinaryDataViaStringTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent((byte[])null);
		request.getContentAsString();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getContentBinaryDataViaPartsTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent((byte[])null);
		request.getContentAsParts();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentStringDataValidTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent("123");
		String output=request.getContentAsString();
		Assert.assertEquals("123",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentStringDataNullTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent((String)null);
		String output=request.getContentAsString();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getContentStringDataViaBinaryTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent((String)null);
		request.getContentAsBinary();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getContentStringDataViaPartsTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setContent((String)null);
		request.getContentAsParts();
	}
}