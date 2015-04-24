package org.fax4j.spi.phaxio;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.http.AbstractMappingHTTPResponseHandler;
import org.fax4j.spi.http.FaxJob2HTTPRequestConverter;
import org.fax4j.spi.http.HTTPClientConfiguration;
import org.fax4j.spi.http.HTTPClient.HTTPMethod;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.spi.http.HTTPFaxClientSpi;
import org.fax4j.spi.http.HTTPRequest;
import org.fax4j.spi.http.HTTPResponseHandler;
import org.fax4j.spi.http.JSONHTTPResponseHandler;
import org.fax4j.spi.http.MultiPartFaxJob2HTTPRequestConverter;
import org.fax4j.test.TestUtil;
import org.fax4j.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class PhaxioFaxClientSpiTest
{
	/**The fax client SPI*/
	private PhaxioFaxClientSpi faxClientSpi;
	/**The converter*/
	private MultiPartFaxJob2HTTPRequestConverter converter;

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.phaxio.api.key","testkey");
		configuration.setProperty("org.fax4j.spi.phaxio.api.secret","testsecret");
		this.faxClientSpi=(PhaxioFaxClientSpi)TestUtil.createFaxClientSpi(PhaxioFaxClientSpi.class.getName(),configuration);
		this.converter=(MultiPartFaxJob2HTTPRequestConverter)ReflectionHelper.getField(HTTPFaxClientSpi.class,"faxJob2HTTPRequestConverter").get(this.faxClientSpi);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeImplMissingAPIKeyTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.phaxio.api.key","testkey");
		TestUtil.createFaxClientSpi(PhaxioFaxClientSpi.class.getName(),configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeImplMissingAPISecretTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.phaxio.api.secret","testsecret");
		TestUtil.createFaxClientSpi(PhaxioFaxClientSpi.class.getName(),configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPClientConfigurationTest() throws Exception
	{
		HTTPClientConfiguration output=this.faxClientSpi.createHTTPClientConfiguration();
		Assert.assertEquals("api.phaxio.com",output.getHostName());
		Assert.assertTrue(output.isSSL());
		Assert.assertEquals(HTTPMethod.POST,output.getMethod(FaxActionType.SUBMIT_FAX_JOB));
		Assert.assertEquals(HTTPMethod.POST,output.getMethod(FaxActionType.CANCEL_FAX_JOB));
		Assert.assertEquals(HTTPMethod.POST,output.getMethod(FaxActionType.GET_FAX_JOB_STATUS));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxJob2HTTPRequestConverterTest() throws Exception
	{
		FaxJob2HTTPRequestConverter output=(FaxJob2HTTPRequestConverter)ReflectionHelper.invokeMethod(HTTPFaxClientSpi.class,this.faxClientSpi,"createFaxJob2HTTPRequestConverter",null,null);
		Assert.assertEquals(MultiPartFaxJob2HTTPRequestConverter.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPResponseHandlerTest() throws Exception
	{
		HTTPResponseHandler output=(HTTPResponseHandler)ReflectionHelper.invokeMethod(HTTPFaxClientSpi.class,this.faxClientSpi,"createHTTPResponseHandler",null,null);
		Assert.assertEquals(JSONHTTPResponseHandler.class,output.getClass());
		Assert.assertEquals("faxId",ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getPathToResponseData",new Class[]{FaxActionType.class},new Object[]{FaxActionType.SUBMIT_FAX_JOB}));
		Assert.assertEquals("success",ReflectionHelper.getField(AbstractMappingHTTPResponseHandler.class,"errorDetectionPath").get(output));
		Assert.assertEquals("false",ReflectionHelper.getField(AbstractMappingHTTPResponseHandler.class,"errorDetectionValue").get(output));
		Assert.assertEquals("message",ReflectionHelper.getField(AbstractMappingHTTPResponseHandler.class,"errorMessagePath").get(output));
		Assert.assertEquals("data;status",ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getPathToResponseData",new Class[]{FaxActionType.class},new Object[]{FaxActionType.GET_FAX_JOB_STATUS}));
		Assert.assertEquals(FaxJobStatus.PENDING,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"queued"}));
		Assert.assertEquals(FaxJobStatus.PENDING,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"pendingbatch"}));
		Assert.assertEquals(FaxJobStatus.IN_PROGRESS,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"inprogress"}));
		Assert.assertEquals(FaxJobStatus.ERROR,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"failure"}));
		Assert.assertEquals(FaxJobStatus.ERROR,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"partialsuccess"}));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void suspendFaxJobImplTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxClientSpi.suspendFaxJobImpl(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void resumeFaxJobImplTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxClientSpi.resumeFaxJobImpl(faxJob);
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
		Assert.assertEquals("filename",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"submitFileContentParameter").get(this.converter));
		Assert.assertEquals("to",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"submitTargetAddressParameter").get(this.converter));
		Assert.assertEquals("caller_id",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"submitSenderFaxNumberParameter").get(this.converter));
		Assert.assertEquals("id",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"cancelFaxJobIDParameter").get(this.converter));
		Assert.assertEquals("id",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"getStatusFaxJobIDParameter").get(this.converter));
		Assert.assertFalse(((Boolean)ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"addFileNamePart").get(this.converter)).booleanValue());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPRequestSubmitTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath("myfile.txt");
		faxJob.setTargetAddress("TEST_TARGET_ADDRESS");
		faxJob.setSenderFaxNumber("TEST_SENDER_FAX_NUMBER");
		faxJob.setTargetName("SOME NAME");//this should not be in the output request
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.SUBMIT_FAX_JOB,faxJob);

		Assert.assertNotNull(output);
		Assert.assertNull(output.getParametersText());
		Assert.assertEquals("/v1/send",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		int foundPartsAmount=0;
		for(int index=0;index<parts.length;index++)
		{
			ContentPart<?> part=parts[index];
			if(part.getName().equals("filename"))
			{
				Assert.assertEquals("myfile.txt",((File)part.getContent()).getName());
				foundPartsAmount++;
			}
			else if(part.getName().equals("to"))
			{
				Assert.assertEquals("TEST_TARGET_ADDRESS",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("caller_id"))
			{
				Assert.assertEquals("TEST_SENDER_FAX_NUMBER",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("api_key"))
			{
				Assert.assertEquals("testkey",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("api_secret"))
			{
				Assert.assertEquals("testsecret",part.getContent());
				foundPartsAmount++;
			}
		}
		Assert.assertEquals(5,foundPartsAmount);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPRequestCancelTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("12345");
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.CANCEL_FAX_JOB,faxJob);

		Assert.assertNotNull(output);
		Assert.assertNull(output.getParametersText());
		Assert.assertEquals("/v1/faxCancel",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		Assert.assertEquals(3,parts.length);
		int foundPartsAmount=0;
		for(int index=0;index<parts.length;index++)
		{
			ContentPart<?> part=parts[index];
			if(part.getName().equals("id"))
			{
				Assert.assertEquals("12345",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("api_key"))
			{
				Assert.assertEquals("testkey",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("api_secret"))
			{
				Assert.assertEquals("testsecret",part.getContent());
				foundPartsAmount++;
			}
		}
		Assert.assertEquals(3,foundPartsAmount);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPRequestGetStatusTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("12345");
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.GET_FAX_JOB_STATUS,faxJob);

		Assert.assertNotNull(output);
		Assert.assertNull(output.getParametersText());
		Assert.assertEquals("/v1/faxStatus",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		Assert.assertEquals(3,parts.length);
		int foundPartsAmount=0;
		for(int index=0;index<parts.length;index++)
		{
			ContentPart<?> part=parts[index];
			if(part.getName().equals("id"))
			{
				Assert.assertEquals("12345",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("api_key"))
			{
				Assert.assertEquals("testkey",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("api_secret"))
			{
				Assert.assertEquals("testsecret",part.getContent());
				foundPartsAmount++;
			}
		}
		Assert.assertEquals(3,foundPartsAmount);
	}
}