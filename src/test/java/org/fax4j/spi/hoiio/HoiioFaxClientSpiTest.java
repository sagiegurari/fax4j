package org.fax4j.spi.hoiio;

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
public class HoiioFaxClientSpiTest
{
	/**The fax client SPI*/
	private HoiioFaxClientSpi faxClientSpi;
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
		configuration.setProperty("org.fax4j.spi.hoiio.app.id","testappid");
		configuration.setProperty("org.fax4j.spi.hoiio.access.token","testaccesstoken");
		this.faxClientSpi=(HoiioFaxClientSpi)TestUtil.createFaxClientSpi(HoiioFaxClientSpi.class.getName(),configuration);
		this.converter=(MultiPartFaxJob2HTTPRequestConverter)ReflectionHelper.getField(HTTPFaxClientSpi.class,"faxJob2HTTPRequestConverter").get(this.faxClientSpi);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeImplMissingApplicationIDTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.hoiio.app.id","testappid");
		TestUtil.createFaxClientSpi(HoiioFaxClientSpi.class.getName(),configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeImplMissingAccessTokenTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.hoiio.access.token","testaccesstoken");
		TestUtil.createFaxClientSpi(HoiioFaxClientSpi.class.getName(),configuration);
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
		Assert.assertEquals("secure.hoiio.com",output.getHostName());
		Assert.assertTrue(output.isSSL());
		Assert.assertEquals(HTTPMethod.POST,output.getMethod(FaxActionType.SUBMIT_FAX_JOB));
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
		Assert.assertEquals("txn_ref",ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getPathToResponseData",new Class[]{FaxActionType.class},new Object[]{FaxActionType.SUBMIT_FAX_JOB}));
		Assert.assertEquals("status",ReflectionHelper.getField(AbstractMappingHTTPResponseHandler.class,"errorDetectionPath").get(output));
		Assert.assertEquals("error_",ReflectionHelper.getField(AbstractMappingHTTPResponseHandler.class,"errorDetectionValue").get(output));
		Assert.assertNull(ReflectionHelper.getField(AbstractMappingHTTPResponseHandler.class,"errorMessagePath").get(output));
		Assert.assertEquals("fax_status",ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getPathToResponseData",new Class[]{FaxActionType.class},new Object[]{FaxActionType.GET_FAX_JOB_STATUS}));
		Assert.assertEquals(FaxJobStatus.IN_PROGRESS,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"ongoing"}));
		Assert.assertEquals(FaxJobStatus.ERROR,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"unanswered"}));
		Assert.assertEquals(FaxJobStatus.ERROR,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"failed"}));
		Assert.assertEquals(FaxJobStatus.ERROR,ReflectionHelper.invokeMethod(AbstractMappingHTTPResponseHandler.class,output,"getFaxJobStatusFromStatusString",new Class[]{String.class},new Object[]{"busy"}));
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
	@Test(expected=FaxException.class)
	public void cancelFaxJobImplTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxClientSpi.cancelFaxJobImpl(faxJob);
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
		Assert.assertEquals("file",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"submitFileContentParameter").get(this.converter));
		Assert.assertEquals("dest",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"submitTargetAddressParameter").get(this.converter));
		Assert.assertEquals("caller_id",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"submitSenderFaxNumberParameter").get(this.converter));
		Assert.assertEquals("txn_ref",ReflectionHelper.getField(MultiPartFaxJob2HTTPRequestConverter.class,"getStatusFaxJobIDParameter").get(this.converter));
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
		Assert.assertEquals("/open/fax/send",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		int foundPartsAmount=0;
		for(int index=0;index<parts.length;index++)
		{
			ContentPart<?> part=parts[index];
			if(part.getName().equals("file"))
			{
				Assert.assertEquals("myfile.txt",((File)part.getContent()).getName());
				foundPartsAmount++;
			}
			else if(part.getName().equals("dest"))
			{
				Assert.assertEquals("TEST_TARGET_ADDRESS",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("caller_id"))
			{
				Assert.assertEquals("TEST_SENDER_FAX_NUMBER",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("app_id"))
			{
				Assert.assertEquals("testappid",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("access_token"))
			{
				Assert.assertEquals("testaccesstoken",part.getContent());
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
	public void createHTTPRequestGetStatusTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("12345");
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.GET_FAX_JOB_STATUS,faxJob);

		Assert.assertNotNull(output);
		Assert.assertNull(output.getParametersText());
		Assert.assertEquals("/open/fax/query_status",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		Assert.assertEquals(3,parts.length);
		int foundPartsAmount=0;
		for(int index=0;index<parts.length;index++)
		{
			ContentPart<?> part=parts[index];
			if(part.getName().equals("txn_ref"))
			{
				Assert.assertEquals("12345",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("app_id"))
			{
				Assert.assertEquals("testappid",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("access_token"))
			{
				Assert.assertEquals("testaccesstoken",part.getContent());
				foundPartsAmount++;
			}
		}
		Assert.assertEquals(3,foundPartsAmount);
	}
}