package org.fax4j.spi.http;

import java.io.File;
import java.util.Properties;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.fax4j.FaxException;
import org.fax4j.spi.http.HTTPClient.HTTPMethod;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.spi.http.HTTPRequest.ContentPartType;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ApacheHTTPClientTest
{
	/**The client to test*/
	private ApacheHTTPClient client;

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		this.client=new ApacheHTTPClient();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHttpClientTest() throws Exception
	{
		HttpClient output=this.client.createHttpClient();
		Assert.assertNotNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createMethodGetTest() throws Exception
	{
		HttpMethodBase output=this.client.createMethod("http://fax4j.org/",HTTPMethod.GET);
		Assert.assertNotNull(output);
		Assert.assertEquals(GetMethod.class,output.getClass());
		Assert.assertEquals("http://fax4j.org/",output.getURI().toString());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createMethodPostTest() throws Exception
	{
		HttpMethodBase output=this.client.createMethod("http://fax4j.org/",HTTPMethod.POST);
		Assert.assertNotNull(output);
		Assert.assertEquals(PostMethod.class,output.getClass());
		Assert.assertEquals("http://fax4j.org/",output.getURI().toString());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createMethodPutTest() throws Exception
	{
		HttpMethodBase output=this.client.createMethod("http://fax4j.org/",HTTPMethod.PUT);
		Assert.assertNotNull(output);
		Assert.assertEquals(PutMethod.class,output.getClass());
		Assert.assertEquals("http://fax4j.org/",output.getURI().toString());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createMethodNullMethodTest() throws Exception
	{
		this.client.createMethod("http://fax4j.org/",null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createMethodNullURLTest() throws Exception
	{
		this.client.createMethod(null,HTTPMethod.PUT);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createMethodEmptyURLTest() throws Exception
	{
		this.client.createMethod(null,HTTPMethod.PUT);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPResponseWithValuesTest() throws Exception
	{
		HTTPResponse output=this.client.createHTTPResponse(150,"TEST\n123");
		Assert.assertNotNull(output);
		Assert.assertEquals(150,output.getStatusCode());
		Assert.assertEquals("TEST\n123",output.getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPResponseNullResponseContentTest() throws Exception
	{
		HTTPResponse output=this.client.createHTTPResponse(150,null);
		Assert.assertNotNull(output);
		Assert.assertEquals(150,output.getStatusCode());
		Assert.assertNull(output.getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendBaseURLAllDataTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX--");
		CommonHTTPClientConfiguration configuration=new CommonHTTPClientConfiguration();
		configuration.setSSL(false);
		configuration.setHostName("testhost");
		configuration.setPort(456);
		this.client.appendBaseURL(buffer,"/path1/path2",configuration);
		String output=buffer.toString();
		Assert.assertEquals("PREFIX--http://testhost:456/path1/path2",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendBaseURLPartialDataTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX--");
		CommonHTTPClientConfiguration configuration=new CommonHTTPClientConfiguration();
		configuration.setSSL(true);
		configuration.setHostName("testhost");
		configuration.setPort(-1000);
		this.client.appendBaseURL(buffer,"path1/path2",configuration);
		String output=buffer.toString();
		Assert.assertEquals("PREFIX--https://testhost/path1/path2",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendBaseURLEmptyResourceTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder();
		CommonHTTPClientConfiguration configuration=new CommonHTTPClientConfiguration();
		configuration.setSSL(false);
		configuration.setHostName("testhost");
		configuration.setPort(0);
		this.client.appendBaseURL(buffer,"",configuration);
		String output=buffer.toString();
		Assert.assertEquals("http://testhost",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendBaseURLNullResourceTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder();
		CommonHTTPClientConfiguration configuration=new CommonHTTPClientConfiguration();
		configuration.setSSL(false);
		configuration.setHostName("testhost");
		configuration.setPort(0);
		this.client.appendBaseURL(buffer,null,configuration);
		String output=buffer.toString();
		Assert.assertEquals("http://testhost",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendParametersEmptyDataTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX");
		this.client.appendParameters(buffer,"");
		String output=buffer.toString();
		Assert.assertEquals("PREFIX",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendParametersNullDataTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX");
		this.client.appendParameters(buffer,null);
		String output=buffer.toString();
		Assert.assertEquals("PREFIX",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendParametersOnlySeparatorTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX/");
		this.client.appendParameters(buffer,"?");
		String output=buffer.toString();
		Assert.assertEquals("PREFIX/",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendParametersFullDataTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX/");
		this.client.appendParameters(buffer,"param1=value1&param2=value2");
		String output=buffer.toString();
		Assert.assertEquals("PREFIX?param1=value1&param2=value2",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendParametersFullDataWithPrefixAndEncodingTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX/");
		this.client.appendParameters(buffer,"?param1=value1&param2=value2 and value3");
		String output=buffer.toString();
		Assert.assertEquals("PREFIX?param1=value1&param2=value2+and+value3",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void appendParametersWithPartialParamsTest() throws Exception
	{
		StringBuilder buffer=new StringBuilder("PREFIX/");
		this.client.appendParameters(buffer,"param1=value1&param2=value2&param3=&param4=value4");
		String output=buffer.toString();
		Assert.assertEquals("PREFIX?param1=value1&param2=value2&param4=value4",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createURLValidTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		httpRequest.setResource("/path1/path2/");
		httpRequest.setParametersText("param1=value1 and valueABC&param2=value2&param3=&param4=value4");
		CommonHTTPClientConfiguration configuration=new CommonHTTPClientConfiguration();
		configuration.setSSL(false);
		configuration.setHostName("testhost");
		configuration.setPort(456);
		String output=this.client.createURL(httpRequest,configuration);
		Assert.assertEquals("http://testhost:456/path1/path2?param1=value1+and+valueABC&param2=value2&param4=value4",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setupHTTPRequestHeaderPropertiesWithDataTest() throws Exception
	{
		Properties headerProperties=new Properties();
		headerProperties.setProperty("key1","value1");
		headerProperties.setProperty("key2","value2");
		headerProperties.setProperty("key3","value3");
		HTTPRequest httpRequest=new HTTPRequest();
		httpRequest.setHeaderProperties(headerProperties);
		HttpMethodBase method=this.client.createMethod("http://fax4j.org",HTTPMethod.GET);
		this.client.setupHTTPRequestHeaderProperties(httpRequest,method);
		Header[] headers=method.getRequestHeaders();
		Assert.assertEquals(3,headers.length);
		Assert.assertEquals("value1",method.getRequestHeader("key1").getValue());
		Assert.assertEquals("value2",method.getRequestHeader("key2").getValue());
		Assert.assertEquals("value3",method.getRequestHeader("key3").getValue());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setupHTTPRequestHeaderPropertiesNullDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		HttpMethodBase method=this.client.createMethod("http://fax4j.org",HTTPMethod.GET);
		this.client.setupHTTPRequestHeaderProperties(httpRequest,method);
		Header[] headers=method.getRequestHeaders();
		Assert.assertEquals(0,headers.length);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createStringRequestContentWithDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		httpRequest.setContent("TEST_DATA");
		RequestEntity output=this.client.createStringRequestContent(httpRequest);
		Assert.assertNotNull(output);
		Assert.assertEquals(StringRequestEntity.class,output.getClass());
		Assert.assertEquals("TEST_DATA",((StringRequestEntity)output).getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createStringRequestContentNullDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		RequestEntity output=this.client.createStringRequestContent(httpRequest);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createBinaryRequestContentWithDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		byte[] data=IOHelper.convertStringToBinary("TEST_DATA",null);
		httpRequest.setContent(data);
		RequestEntity output=this.client.createBinaryRequestContent(httpRequest);
		Assert.assertNotNull(output);
		Assert.assertEquals(ByteArrayRequestEntity.class,output.getClass());
		Assert.assertArrayEquals(data,((ByteArrayRequestEntity)output).getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createBinaryRequestContentNullDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		httpRequest.setContent((byte[])null);
		RequestEntity output=this.client.createBinaryRequestContent(httpRequest);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createMultiPartRequestContentWithDataTest() throws Exception
	{
		String string="FILE_DATA";
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		byte[] input=string.getBytes(IOHelper.getDefaultEncoding());
		IOHelper.writeFile(input,file);

		HTTPRequest httpRequest=new HTTPRequest();
		ContentPart<?>[] parts=new ContentPart[5];	//make array bigger to simulate null parts
		parts[0]=new ContentPart<String>("string_part","TEST_DATA",ContentPartType.STRING);
		parts[1]=new ContentPart<String>("string_part2","TEST_DATA2",ContentPartType.STRING);
		parts[2]=new ContentPart<File>("file_part",file,ContentPartType.FILE);
		httpRequest.setContent(parts);
		HttpMethodBase method=this.client.createMethod("http://fax4j.org",HTTPMethod.POST);
		RequestEntity output=this.client.createMultiPartRequestContent(httpRequest,method);
		
		file.delete();

		Assert.assertNotNull(output);
		Assert.assertEquals(MultipartRequestEntity.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createMultiPartRequestContentNullDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		httpRequest.setContent((ContentPart<?>[])null);
		HttpMethodBase method=this.client.createMethod("http://fax4j.org",HTTPMethod.POST);
		RequestEntity output=this.client.createMultiPartRequestContent(httpRequest,method);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setRequestContentStringDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		httpRequest.setContent("TEST_DATA");
		PostMethod method=(PostMethod)this.client.createMethod("http://fax4j.org",HTTPMethod.POST);
		this.client.setRequestContent(httpRequest,method);
		RequestEntity output=method.getRequestEntity();
		Assert.assertNotNull(output);
		Assert.assertEquals(StringRequestEntity.class,output.getClass());
		Assert.assertEquals("TEST_DATA",((StringRequestEntity)output).getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setRequestContentBinaryDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		byte[] data=IOHelper.convertStringToBinary("TEST_DATA",null);
		httpRequest.setContent(data);
		PostMethod method=(PostMethod)this.client.createMethod("http://fax4j.org",HTTPMethod.POST);
		this.client.setRequestContent(httpRequest,method);
		RequestEntity output=method.getRequestEntity();
		Assert.assertNotNull(output);
		Assert.assertEquals(ByteArrayRequestEntity.class,output.getClass());
		Assert.assertArrayEquals(data,((ByteArrayRequestEntity)output).getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setRequestContentMultiPartDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		ContentPart<?>[] parts=new ContentPart[3];	//make array bigger to simulate null parts
		parts[0]=new ContentPart<String>("string_part","TEST_DATA",ContentPartType.STRING);
		httpRequest.setContent(parts);
		PostMethod method=(PostMethod)this.client.createMethod("http://fax4j.org",HTTPMethod.POST);
		this.client.setRequestContent(httpRequest,method);
		RequestEntity output=method.getRequestEntity();
		Assert.assertNotNull(output);
		Assert.assertEquals(MultipartRequestEntity.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setRequestContentNullDataTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		PostMethod method=(PostMethod)this.client.createMethod("http://fax4j.org",HTTPMethod.POST);
		this.client.setRequestContent(httpRequest,method);
		RequestEntity output=method.getRequestEntity();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void setRequestContentGetMethodTest() throws Exception
	{
		HTTPRequest httpRequest=new HTTPRequest();
		GetMethod method=(GetMethod)this.client.createMethod("http://fax4j.org",HTTPMethod.GET);
		this.client.setRequestContent(httpRequest,method);
	}
}