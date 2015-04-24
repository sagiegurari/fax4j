package org.fax4j.bridge.http;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.bridge.FileInfo;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.http.HTTPRequest;
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
public class MultiPartHTTPRequestParserTest
{
	/**The parser to test*/
	private MultiPartHTTPRequestParser parser;
	/**The HTTP request*/
	private HTTPRequest httpRequest;
	/**The configuration*/
	private Map<String,String> configuration;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.configuration=new HashMap<String,String>();
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.file.content.parameter","test_file");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.file.name.parameter","test_filename");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.priority.parameter","test_priority");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.target.address.parameter","test_targetaddress");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.target.name.parameter","test_targetname");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.sender.name.parameter","test_sendername");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.sender.fax.number.parameter","test_senderfaxnumber");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.sender.email.parameter","test_senderemail");
		this.parser=new MultiPartHTTPRequestParser();
		this.parser.initialize(this.configuration);
		this.httpRequest=new HTTPRequest();
		this.httpRequest.setParametersText("a=b");
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<byte[]>("test_file",IOHelper.convertStringToBinary("TEST_FILE_CONTENT",null),ContentPartType.BINARY));
		list.add(new ContentPart<String>("test_filename","TEST_FILE_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_priority",FaxJobPriority.HIGH_PRIORITY.toString(),ContentPartType.STRING));
		list.add(new ContentPart<String>("test_targetaddress","TEST_TARGET_ADDRESS",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_targetname","TEST_TARGET_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_sendername","TEST_SENDER_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderfaxnumber","TEST_SENDER_FAX_NUMBER",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderemail","TEST_SENDER_EMAIL",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeImplConfiguredValuesTest() throws Exception
	{
		Assert.assertEquals("test_file",this.parser.fileContentParameter);
		Assert.assertEquals("test_filename",this.parser.fileNameParameter);
		Assert.assertEquals("test_priority",this.parser.priorityParameter);
		Assert.assertEquals("test_targetaddress",this.parser.targetAddressParameter);
		Assert.assertEquals("test_targetname",this.parser.targetNameParameter);
		Assert.assertEquals("test_sendername",this.parser.senderNameParameter);
		Assert.assertEquals("test_senderfaxnumber",this.parser.senderFaxNumberParameter);
		Assert.assertEquals("test_senderemail",this.parser.senderEMailParameter);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeImplEmptyValuesTest() throws Exception
	{
		this.configuration=new HashMap<String,String>();
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.file.content.parameter","");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.file.name.parameter","");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.priority.parameter","");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.target.address.parameter","");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.target.name.parameter","");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.sender.name.parameter","");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.sender.fax.number.parameter","");
		this.configuration.put("org.fax4j.bridge.http.request.parser.multi.part.sender.email.parameter","");
		this.parser=new MultiPartHTTPRequestParser();
		this.parser.initialize(this.configuration);
		Assert.assertEquals("file",this.parser.fileContentParameter);
		Assert.assertEquals("filename",this.parser.fileNameParameter);
		Assert.assertEquals("priority",this.parser.priorityParameter);
		Assert.assertEquals("targetaddress",this.parser.targetAddressParameter);
		Assert.assertEquals("targetname",this.parser.targetNameParameter);
		Assert.assertEquals("sendername",this.parser.senderNameParameter);
		Assert.assertEquals("senderfaxnumber",this.parser.senderFaxNumberParameter);
		Assert.assertEquals("senderemail",this.parser.senderEMailParameter);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeImplNullValuesTest() throws Exception
	{
		this.configuration=new HashMap<String,String>();
		this.parser=new MultiPartHTTPRequestParser();
		this.parser.initialize(this.configuration);
		Assert.assertEquals("file",this.parser.fileContentParameter);
		Assert.assertEquals("filename",this.parser.fileNameParameter);
		Assert.assertEquals("priority",this.parser.priorityParameter);
		Assert.assertEquals("targetaddress",this.parser.targetAddressParameter);
		Assert.assertEquals("targetname",this.parser.targetNameParameter);
		Assert.assertEquals("sendername",this.parser.senderNameParameter);
		Assert.assertEquals("senderfaxnumber",this.parser.senderFaxNumberParameter);
		Assert.assertEquals("senderemail",this.parser.senderEMailParameter);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentPartsAsMapValidDataTest() throws Exception
	{
		Map<String,ContentPart<?>> output=this.parser.getContentPartsAsMap(this.httpRequest);
		Assert.assertNotNull(output);
		Assert.assertArrayEquals(IOHelper.convertStringToBinary("TEST_FILE_CONTENT",null),(byte[])output.get(this.parser.fileContentParameter).getContent());
		Assert.assertEquals("TEST_FILE_NAME",output.get(this.parser.fileNameParameter).getContent());
		Assert.assertEquals(FaxJobPriority.HIGH_PRIORITY.toString(),output.get(this.parser.priorityParameter).getContent());
		Assert.assertEquals("TEST_TARGET_ADDRESS",output.get(this.parser.targetAddressParameter).getContent());
		Assert.assertEquals("TEST_TARGET_NAME",output.get(this.parser.targetNameParameter).getContent());
		Assert.assertEquals("TEST_SENDER_NAME",output.get(this.parser.senderNameParameter).getContent());
		Assert.assertEquals("TEST_SENDER_FAX_NUMBER",output.get(this.parser.senderFaxNumberParameter).getContent());
		Assert.assertEquals("TEST_SENDER_EMAIL",output.get(this.parser.senderEMailParameter).getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getContentPartsAsMapUnsupportedTypeTest() throws Exception
	{
		this.httpRequest.setContent("123");
		this.parser.getContentPartsAsMap(this.httpRequest);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void updateFaxJobFromInputDataImplValidDataTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		this.parser.updateFaxJobFromInputDataImpl(this.httpRequest,faxJob);
		Assert.assertEquals(FaxJobPriority.HIGH_PRIORITY,faxJob.getPriority());
		Assert.assertEquals("TEST_TARGET_ADDRESS",faxJob.getTargetAddress());
		Assert.assertEquals("TEST_TARGET_NAME",faxJob.getTargetName());
		Assert.assertEquals("TEST_SENDER_NAME",faxJob.getSenderName());
		Assert.assertEquals("TEST_SENDER_FAX_NUMBER",faxJob.getSenderFaxNumber());
		Assert.assertEquals("TEST_SENDER_EMAIL",faxJob.getSenderEmail());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void updateFaxJobFromInputDataImplNullPartsTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<String>("test_targetaddress","TEST_TARGET_ADDRESS",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		FaxJob faxJob=new FaxJobImpl();
		this.parser.updateFaxJobFromInputDataImpl(this.httpRequest,faxJob);
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
	public void updateFaxJobFromInputDataImplEmptyPartsDataTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<String>("test_priority","",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_targetaddress","TEST_TARGET_ADDRESS",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_targetname","",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_sendername","",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderfaxnumber","",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderemail","",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		FaxJob faxJob=new FaxJobImpl();
		this.parser.updateFaxJobFromInputDataImpl(this.httpRequest,faxJob);
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
	@Test(expected=FaxException.class)
	public void updateFaxJobFromInputDataImplEmptyTargetAddressTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<byte[]>("test_file",IOHelper.convertStringToBinary("TEST_FILE_CONTENT",null),ContentPartType.BINARY));
		list.add(new ContentPart<String>("test_filename","TEST_FILE_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_priority",FaxJobPriority.HIGH_PRIORITY.toString(),ContentPartType.STRING));
		list.add(new ContentPart<String>("test_targetaddress","",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_targetname","TEST_TARGET_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_sendername","TEST_SENDER_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderfaxnumber","TEST_SENDER_FAX_NUMBER",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderemail","TEST_SENDER_EMAIL",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		FaxJob faxJob=new FaxJobImpl();
		this.parser.updateFaxJobFromInputDataImpl(this.httpRequest,faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void updateFaxJobFromInputDataImplNullTargetAddressPartTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<byte[]>("test_file",IOHelper.convertStringToBinary("TEST_FILE_CONTENT",null),ContentPartType.BINARY));
		list.add(new ContentPart<String>("test_filename","TEST_FILE_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_priority",FaxJobPriority.HIGH_PRIORITY.toString(),ContentPartType.STRING));
		list.add(new ContentPart<String>("test_targetname","TEST_TARGET_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_sendername","TEST_SENDER_NAME",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderfaxnumber","TEST_SENDER_FAX_NUMBER",ContentPartType.STRING));
		list.add(new ContentPart<String>("test_senderemail","TEST_SENDER_EMAIL",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		FaxJob faxJob=new FaxJobImpl();
		this.parser.updateFaxJobFromInputDataImpl(this.httpRequest,faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileInfoFromInputDataImplValidDataBinaryTest() throws Exception
	{
		FileInfo output=this.parser.getFileInfoFromInputDataImpl(this.httpRequest);
		Assert.assertNotNull(output);
		Assert.assertEquals("TEST_FILE_NAME",output.getName());
		Assert.assertArrayEquals(IOHelper.convertStringToBinary("TEST_FILE_CONTENT",null),output.getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileInfoFromInputDataImplValidDataFileTest() throws Exception
	{
		File file=new File("myfile.txt");
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<File>("test_file",file,ContentPartType.FILE));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		FileInfo output=this.parser.getFileInfoFromInputDataImpl(this.httpRequest);
		Assert.assertNotNull(output);
		Assert.assertEquals(file,output.getFile());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataImplNullFileDataTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		FileInfo output=this.parser.getFileInfoFromInputDataImpl(this.httpRequest);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataImplUnsupportedTypeTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<String>("test_file","test",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		this.parser.getFileInfoFromInputDataImpl(this.httpRequest);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentPartAsStringNullPartTest() throws Exception
	{
		Map<String,ContentPart<?>> map=this.parser.getContentPartsAsMap(this.httpRequest);
		String output=this.parser.getContentPartAsString(map,"parameter1");
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentPartAsStringEmptyPartValueTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<String>("parameter1","",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		Map<String,ContentPart<?>> map=this.parser.getContentPartsAsMap(this.httpRequest);
		String output=this.parser.getContentPartAsString(map,"parameter1");
		Assert.assertNotNull(output);
		Assert.assertEquals("",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentPartAsStringValidStringDataTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<String>("parameter1","value1",ContentPartType.STRING));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		Map<String,ContentPart<?>> map=this.parser.getContentPartsAsMap(this.httpRequest);
		String output=this.parser.getContentPartAsString(map,"parameter1");
		Assert.assertNotNull(output);
		Assert.assertEquals("value1",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentPartAsStringValidBinaryDataTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<byte[]>("parameter1","value1".getBytes(),ContentPartType.BINARY));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		Map<String,ContentPart<?>> map=this.parser.getContentPartsAsMap(this.httpRequest);
		String output=this.parser.getContentPartAsString(map,"parameter1");
		Assert.assertNotNull(output);
		Assert.assertEquals("value1",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getContentPartAsStringUnsupportedPartTypeTest() throws Exception
	{
		List<ContentPart<?>> list=new LinkedList<HTTPRequest.ContentPart<?>>();
		list.add(new ContentPart<File>("parameter1",new File("test.test"),ContentPartType.FILE));
		ContentPart<?>[] content=list.toArray(new ContentPart<?>[list.size()]);
		this.httpRequest.setContent(content);
		Map<String,ContentPart<?>> map=this.parser.getContentPartsAsMap(this.httpRequest);
		String output=this.parser.getContentPartAsString(map,"parameter1");
		Assert.assertNotNull(output);
		Assert.assertEquals("value1",output);
	}
}