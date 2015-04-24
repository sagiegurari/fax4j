package org.fax4j.bridge.http;

import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.http.HTTPRequest;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestHTTPRequestParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class HTTP2FaxBridgeTest
{
	/**The fax bridge to test*/
	private HTTP2FaxBridge faxBridge;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.faxBridge=new HTTP2FaxBridge();

		Properties configuration=new Properties();
		configuration.setProperty(HTTP2FaxBridge.HTTP_REQUEST_PARSER_CLASS_NAME_PROPERTY_KEY,TestHTTPRequestParser.class.getName());
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		this.faxBridge.initialize("test",configuration,new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobValidTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setResource("");
		FaxJob faxJob=this.faxBridge.submitFaxJob(request);
		Assert.assertNotNull(faxJob);
		Assert.assertNotNull(faxJob.getFile());
		Assert.assertTrue(faxJob.getFile().exists());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullRequestTest() throws Exception
	{
		this.faxBridge.submitFaxJob(null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullFileInfoTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setResource("NullFileInfoTest");
		this.faxBridge.submitFaxJob(request);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullFaxJobTest() throws Exception
	{
		HTTPRequest request=new HTTPRequest();
		request.setResource("NullFaxJobTest");
		this.faxBridge.submitFaxJob(request);
	}
}