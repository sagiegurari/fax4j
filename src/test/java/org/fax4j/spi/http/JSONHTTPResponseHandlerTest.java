package org.fax4j.spi.http;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxJobImpl;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class JSONHTTPResponseHandlerTest
{
	/**The handler to test*/
	private JSONHTTPResponseHandler handler;
	/**The HTTP response used for testing*/
	private HTTPResponse httpResponse;

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
    {
		Map<String,String> configuration=new HashMap<String,String>();
		configuration.put("org.fax4j.spi.http.submit.json.output.path","part1;part2;;;part3;element");
		configuration.put("org.fax4j.spi.http.get.status.json.output.path","element");
		configuration.put("org.fax4j.spi.http.json.pending.status.mappings","PENDING;pending123");
		configuration.put("org.fax4j.spi.http.json.error.status.mappings","ERROR");
		configuration.put("org.fax4j.spi.http.json.error.detection.path","err_flag");
		configuration.put("org.fax4j.spi.http.json.error.detection.value","error");
		configuration.put("org.fax4j.spi.http.json.error.message.path","err_message");
		this.handler=new JSONHTTPResponseHandler();
		this.handler.setPropertyPart(HTTPFaxClientSpi.HTTP_PROPERTY_PART);
		this.handler.initialize(configuration);
		
		this.httpResponse=new HTTPResponse();
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertToObjectWithDataTest() throws Exception
	{
		String text="{\"part1\": {"+
				     "\"mem1\": \"v1\","+
				     "\"mem2\": \"v1\","+
				     "\"part2\": {"+
				     "\"part3\": {"+
				     "\"element\": \"e_value\""+
					 "}}}}";
		this.httpResponse.setContent(text);
		JSONObject output=this.handler.convertToObject(this.httpResponse);
		Assert.assertNotNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertToObjectNullDataTest() throws Exception
	{
		JSONObject output=this.handler.convertToObject(this.httpResponse);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void findValueNullPathTest() throws Exception
	{
		String text="{\"part1\": {"+
			     "\"mem1\": \"v1\","+
			     "\"mem2\": \"v1\","+
			     "\"part2\": {"+
			     "\"part3\": {"+
			     "\"element\": \"e_value\""+
				 "}}}}";
		this.httpResponse.setContent(text);
		String output=this.handler.findValue(this.httpResponse,null);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void findValueNullDataTest() throws Exception
	{
		String output=this.handler.findValue(this.httpResponse,"p1;p2");
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void findValueValidTest() throws Exception
	{
		String text="{\"part1\": {"+
			     "\"mem1\": \"v1\","+
			     "\"mem2\": \"v1\","+
			     "\"part2\": {"+
			     "\"part3\": {"+
			     "\"element\": \"e_value\""+
				 "}}}}";
		this.httpResponse.setContent(text);
		String output=this.handler.findValue(this.httpResponse,";;;part1;part2;;;part3;element");
		Assert.assertNotNull(output);
		Assert.assertEquals("e_value",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void updateFaxJobValidTest() throws Exception
	{
		String text="{\"part1\": {"+
			     "\"mem1\": \"v1\","+
			     "\"mem2\": \"v1\","+
			     "\"part2\": {"+
			     "\"part3\": {"+
			     "\"element\": \"e_value\""+
				 "}}}}";
		this.httpResponse.setContent(text);
		FaxJob faxJob=new FaxJobImpl();
		this.handler.updateFaxJob(faxJob,this.httpResponse,FaxActionType.SUBMIT_FAX_JOB);
		String output=faxJob.getID();
		Assert.assertNotNull(output);
		Assert.assertEquals("e_value",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void updateFaxJobPathNotFoundTest() throws Exception
	{
		String text="{\"part1abc\": {"+
			     "\"mem1\": \"v1\","+
			     "\"mem2\": \"v1\","+
			     "\"part2\": {"+
			     "\"part3\": {"+
			     "\"element\": \"e_value\""+
				 "}}}}";
		this.httpResponse.setContent(text);
		FaxJob faxJob=new FaxJobImpl();
		this.handler.updateFaxJob(faxJob,this.httpResponse,FaxActionType.SUBMIT_FAX_JOB);
		String output=faxJob.getID();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void updateFaxJobErrorDataTest() throws Exception
	{
		String text="{\"err_flag\": \"error\"}";
		this.httpResponse.setContent(text);
		this.handler.updateFaxJob(new FaxJobImpl(),this.httpResponse,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusValidTest() throws Exception
	{
		String text="{\"element\": \"ERROR\"}";
		this.httpResponse.setContent(text);
		FaxJobStatus output=this.handler.getFaxJobStatus(this.httpResponse);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.ERROR,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusValidMultiValuesTest() throws Exception
	{
		String text="{\"element\": \"PENDING\"}";
		this.httpResponse.setContent(text);
		FaxJobStatus output=this.handler.getFaxJobStatus(this.httpResponse);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.PENDING,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusPathNotFoundTest() throws Exception
	{
		String text="{\"abc\": \"PENDING\"}";
		this.httpResponse.setContent(text);
		FaxJobStatus output=this.handler.getFaxJobStatus(this.httpResponse);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusNullDataTest() throws Exception
	{
		FaxJobStatus output=this.handler.getFaxJobStatus(this.httpResponse);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusNullMappedStatusTest() throws Exception
	{
		String text="{\"element\": \"null\"}";
		this.httpResponse.setContent(text);
		FaxJobStatus output=this.handler.getFaxJobStatus(this.httpResponse);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFaxJobStatusErrorDataTest() throws Exception
	{
		String text="{\"err_flag\": \"error\"}";
		this.httpResponse.setContent(text);
		this.handler.getFaxJobStatus(this.httpResponse);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void findErrorNoErrorTest() throws Exception
	{
		String text="{\"err_flag2\": \"error\"}";
		this.httpResponse.setContent(text);
		JSONObject jsonObject=this.handler.convertToObject(this.httpResponse);
		String output=this.handler.findError(jsonObject);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void findErrorNoErrorDueToValueTest() throws Exception
	{
		String text="{\"err_flag\": \"nothing\"}";
		this.httpResponse.setContent(text);
		JSONObject jsonObject=this.handler.convertToObject(this.httpResponse);
		String output=this.handler.findError(jsonObject);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void findErrorNullMessagePathTest() throws Exception
	{
		String text="{\"err_flag\": \"error\"}";
		this.httpResponse.setContent(text);
		JSONObject jsonObject=this.handler.convertToObject(this.httpResponse);
		String output=this.handler.findError(jsonObject);
		Assert.assertNotNull(output);
		Assert.assertEquals("error",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void findErrorWithMessageTest() throws Exception
	{
		String text="{\"err_flag\": \"error\",\"err_message\": \"some message\"}";
		this.httpResponse.setContent(text);
		JSONObject jsonObject=this.handler.convertToObject(this.httpResponse);
		String output=this.handler.findError(jsonObject);
		Assert.assertNotNull(output);
		Assert.assertEquals("some message",output);
	}
}