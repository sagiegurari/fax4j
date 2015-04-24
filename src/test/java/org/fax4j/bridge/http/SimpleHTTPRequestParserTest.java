package org.fax4j.bridge.http;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.bridge.FileInfo;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.http.HTTPRequest;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class SimpleHTTPRequestParserTest
{
	/**The parser to test*/
	private SimpleHTTPRequestParser parser;
	/**The HTTP request*/
	private HTTPRequest httpRequest;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.parser=new SimpleHTTPRequestParser();
		this.parser.initialize(new HashMap<String,String>());
		this.httpRequest=new HTTPRequest();
		this.httpRequest.setParametersText("bad=bad&file=TEST_FILE&"+
										   "priority="+FaxJobPriority.LOW_PRIORITY.name()+
										   "&target_address=TEST_TARGET_ADDRESS"+
										   "&target_name=TEST_TARGET_NAME"+
										   "&sender_name=TEST_SENDER_NAME"+
										   "&sender_fax_number=TEST_SENDER_FAX_NUMBER"+
										   "&sender_email=TEST_SENDER_EMAIL"+
										   "&property:p1=v1"+
										   "&property:p2=v2"+
										   "&property:p3_novalue="+
										   "&property:=nokey"+
										   "&bad2=bad2&keynovalue=&=valuenokey");
		this.httpRequest.setContent("TEST123".getBytes(IOHelper.getDefaultEncoding()));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertParametersTextToMapValidTest() throws Exception
	{
		Map<String,String> properties=this.parser.convertParametersTextToMap(this.httpRequest);
		Assert.assertNotNull(properties);
		Assert.assertEquals("TEST_FILE",properties.get("file"));
		Assert.assertEquals("LOW_PRIORITY",properties.get("priority"));
		Assert.assertEquals("TEST_TARGET_ADDRESS",properties.get("target_address"));
		Assert.assertEquals("TEST_TARGET_NAME",properties.get("target_name"));
		Assert.assertEquals("TEST_SENDER_NAME",properties.get("sender_name"));
		Assert.assertEquals("TEST_SENDER_FAX_NUMBER",properties.get("sender_fax_number"));
		Assert.assertEquals("TEST_SENDER_EMAIL",properties.get("sender_email"));
		Assert.assertEquals("v1",properties.get("property:p1"));
		Assert.assertEquals("v2",properties.get("property:p2"));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertParametersTextToMapNullParametersTextTest() throws Exception
	{
		Map<String,String> properties=this.parser.convertParametersTextToMap(new HTTPRequest());
		Assert.assertNotNull(properties);
		Assert.assertEquals(0,properties.size());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileInfoFromInputDataValidTest() throws Exception
	{
		FileInfo fileInfo=this.parser.getFileInfoFromInputData(this.httpRequest);
		Assert.assertNotNull(fileInfo);
		Assert.assertEquals("TEST_FILE",fileInfo.getName());
		Assert.assertArrayEquals("TEST123".getBytes(IOHelper.getDefaultEncoding()),fileInfo.getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataNullFileNameTest() throws Exception
	{
		this.httpRequest.setParametersText("a=b");
		this.parser.getFileInfoFromInputData(this.httpRequest);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataEmptyFileNameTest() throws Exception
	{
		this.httpRequest.setParametersText("file=");
		this.parser.getFileInfoFromInputData(this.httpRequest);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataNullFileContentTest() throws Exception
	{
		this.httpRequest.setContent((byte[])null);
		this.parser.getFileInfoFromInputData(this.httpRequest);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataEmptyFileContentTest() throws Exception
	{
		this.httpRequest.setContent(new byte[0]);
		this.parser.getFileInfoFromInputData(this.httpRequest);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void updateFaxJobFromInputDataValidTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		this.parser.updateFaxJobFromInputData(this.httpRequest,faxJob);
		Assert.assertEquals("LOW_PRIORITY",faxJob.getPriority().name());
		Assert.assertEquals("TEST_TARGET_ADDRESS",faxJob.getTargetAddress());
		Assert.assertEquals("TEST_TARGET_NAME",faxJob.getTargetName());
		Assert.assertEquals("TEST_SENDER_NAME",faxJob.getSenderName());
		Assert.assertEquals("TEST_SENDER_FAX_NUMBER",faxJob.getSenderFaxNumber());
		Assert.assertEquals("TEST_SENDER_EMAIL",faxJob.getSenderEmail());
		Assert.assertEquals("v1",faxJob.getProperty("p1","bad"));
		Assert.assertEquals("v2",faxJob.getProperty("p2","bad"));
		Assert.assertEquals("EMPTY",faxJob.getProperty("p3_novalue","EMPTY"));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void updateFaxJobFromInputDataNoTargetAddressTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		this.httpRequest.setParametersText(this.httpRequest.getParametersText().replace("target_address","abc"));
		this.parser.updateFaxJobFromInputData(this.httpRequest,faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void updateFaxJobFromInputDataOnlyTargetAddressTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		this.httpRequest.setParametersText("target_address=TEST_TARGET_ADDRESS");
		this.parser.updateFaxJobFromInputData(this.httpRequest,faxJob);
		Assert.assertNull(faxJob.getPriority());
		Assert.assertEquals("TEST_TARGET_ADDRESS",faxJob.getTargetAddress());
		Assert.assertNull(faxJob.getTargetName());
		Assert.assertNull(faxJob.getSenderName());
		Assert.assertNull(faxJob.getSenderFaxNumber());
		Assert.assertNull(faxJob.getSenderEmail());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileInfoFromRequestImplValidStringDataTest() throws Exception
	{
		Map<String,String> queryStringMap=new HashMap<String,String>();
		queryStringMap.put("file","test_file");
		this.httpRequest.setContent("TEXT_VALUE");
		FileInfo output=this.parser.getFileInfoFromRequestImpl(this.httpRequest,queryStringMap);
		Assert.assertNotNull(output);
		Assert.assertArrayEquals("TEXT_VALUE".getBytes(),output.getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileInfoFromRequestImplValidBinaryDataTest() throws Exception
	{
		Map<String,String> queryStringMap=new HashMap<String,String>();
		queryStringMap.put("file","test_file");
		this.httpRequest.setContent("TEXT_VALUE".getBytes());
		FileInfo output=this.parser.getFileInfoFromRequestImpl(this.httpRequest,queryStringMap);
		Assert.assertNotNull(output);
		Assert.assertArrayEquals("TEXT_VALUE".getBytes(),output.getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromRequestImplUnsupportedTypeTest() throws Exception
	{
		Map<String,String> queryStringMap=new HashMap<String,String>();
		queryStringMap.put("file","test_file");
		this.httpRequest.setContent(new ContentPart<?>[0]); 
		this.parser.getFileInfoFromRequestImpl(this.httpRequest,queryStringMap);
	}
}