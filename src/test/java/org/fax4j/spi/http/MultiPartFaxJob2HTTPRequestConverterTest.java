package org.fax4j.spi.http;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxJob;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.http.HTTPRequest.ContentPart;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.TestHTTPClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class MultiPartFaxJob2HTTPRequestConverterTest
{
	/**The fax client SPI*/
	private HTTPFaxClientSpi faxClientSpi;
	/**The converter*/
	private MultiPartFaxJob2HTTPRequestConverter converter;
	/**The configuration*/
	private Properties configuration;

	/**
	 * Sets up the SPI instance.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
    public void setUp() throws Exception
	{
		this.configuration=new Properties();
		this.configuration.setProperty("org.fax4j.spi.http.client.class.name",TestHTTPClient.class.getName());
		this.configuration.setProperty("org.fax4j.spi.http.faxjob2request.converter.class.name",MultiPartFaxJob2HTTPRequestConverter.class.getName());
		this.configuration.setProperty("org.fax4j.spi.http.host.name","localhost");
		this.configuration.setProperty("org.fax4j.spi.http.port","80");
		this.configuration.setProperty("org.fax4j.spi.http.ssl","false");
		this.configuration.setProperty("org.fax4j.spi.http.submit.resource","resource1");
		this.configuration.setProperty("org.fax4j.spi.http.suspend.resource","resource2");
		this.configuration.setProperty("org.fax4j.spi.http.resume.resource","resource3");
		this.configuration.setProperty("org.fax4j.spi.http.cancel.resource","resource4");
		this.configuration.setProperty("org.fax4j.spi.http.get.status.resource","resource5");
		this.configuration.setProperty("org.fax4j.spi.http.url.parameters","param1=value1&param2=value2");

		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.file.content.parameter","test_file");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.file.name.parameter","test_filename");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.target.address.parameter","test_targetaddress");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.target.name.parameter","test_targetname");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.sender.name.parameter","test_sendername");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.sender.fax.number.parameter","test_senderfaxnumber");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.sender.email.parameter","test_senderemail");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.suspend.fax.job.id.parameter","test_suspend_faxjobid");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.resume.fax.job.id.parameter","test_resume_faxjobid");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.cancel.fax.job.id.parameter","test_cancel_faxjobid");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.get.status.fax.job.id.parameter","test_getstatus_faxjobid");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.parameter.key.KEY1","VALUE1");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.parameter.key.KEY2","VALUE2");

		this.faxClientSpi=(HTTPFaxClientSpi)TestUtil.createFaxClientSpi(HTTPFaxClientSpi.class.getName(),this.configuration);
		this.converter=(MultiPartFaxJob2HTTPRequestConverter)this.faxClientSpi.getFaxJob2HTTPRequestConverter();
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
		Assert.assertEquals("test_file",this.converter.submitFileContentParameter);
		Assert.assertEquals("test_filename",this.converter.submitFileNameParameter);
		Assert.assertEquals("test_targetaddress",this.converter.submitTargetAddressParameter);
		Assert.assertEquals("test_targetname",this.converter.submitTargetNameParameter);
		Assert.assertEquals("test_sendername",this.converter.submitSenderNameParameter);
		Assert.assertEquals("test_senderfaxnumber",this.converter.submitSenderFaxNumberParameter);
		Assert.assertEquals("test_senderemail",this.converter.submitSenderEMailParameter);
		Assert.assertEquals("test_suspend_faxjobid",this.converter.suspendFaxJobIDParameter);
		Assert.assertEquals("test_resume_faxjobid",this.converter.resumeFaxJobIDParameter);
		Assert.assertEquals("test_cancel_faxjobid",this.converter.cancelFaxJobIDParameter);
		Assert.assertEquals("test_getstatus_faxjobid",this.converter.getStatusFaxJobIDParameter);
		Assert.assertEquals(2,this.converter.additionalParameters.size());
		Assert.assertEquals("VALUE1",this.converter.additionalParameters.get("KEY1"));
		Assert.assertEquals("VALUE2",this.converter.additionalParameters.get("KEY2"));
		Assert.assertTrue(this.converter.addFileNamePart);
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
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.file.content.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.file.name.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.target.address.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.target.name.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.sender.name.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.sender.fax.number.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.submit.sender.email.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.suspend.fax.job.id.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.resume.fax.job.id.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.cancel.fax.job.id.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.get.status.fax.job.id.parameter","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.parameter.key.KEY1","");
		this.configuration.setProperty("org.fax4j.spi.http.multi.part.parameter.key.KEY2","");

		this.faxClientSpi=(HTTPFaxClientSpi)TestUtil.createFaxClientSpi(HTTPFaxClientSpi.class.getName(),this.configuration);
		this.converter=(MultiPartFaxJob2HTTPRequestConverter)this.faxClientSpi.getFaxJob2HTTPRequestConverter();

		Assert.assertEquals("file",this.converter.submitFileContentParameter);
		Assert.assertEquals("filename",this.converter.submitFileNameParameter);
		Assert.assertEquals("targetaddress",this.converter.submitTargetAddressParameter);
		Assert.assertEquals("targetname",this.converter.submitTargetNameParameter);
		Assert.assertEquals("sendername",this.converter.submitSenderNameParameter);
		Assert.assertEquals("senderfaxnumber",this.converter.submitSenderFaxNumberParameter);
		Assert.assertEquals("senderemail",this.converter.submitSenderEMailParameter);
		Assert.assertEquals("faxjobid",this.converter.suspendFaxJobIDParameter);
		Assert.assertEquals("faxjobid",this.converter.resumeFaxJobIDParameter);
		Assert.assertEquals("faxjobid",this.converter.cancelFaxJobIDParameter);
		Assert.assertEquals("faxjobid",this.converter.getStatusFaxJobIDParameter);
		Assert.assertEquals(0,this.converter.additionalParameters.size());
		Assert.assertTrue(this.converter.addFileNamePart);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeImplNoValuesTest() throws Exception
	{
		this.configuration.remove("org.fax4j.spi.http.multi.part.submit.file.content.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.submit.file.name.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.submit.target.address.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.submit.target.name.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.submit.sender.name.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.submit.sender.fax.number.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.submit.sender.email.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.suspend.fax.job.id.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.resume.fax.job.id.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.cancel.fax.job.id.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.get.status.fax.job.id.parameter");
		this.configuration.remove("org.fax4j.spi.http.multi.part.parameter.key.KEY1");
		this.configuration.remove("org.fax4j.spi.http.multi.part.parameter.key.KEY2");

		this.faxClientSpi=(HTTPFaxClientSpi)TestUtil.createFaxClientSpi(HTTPFaxClientSpi.class.getName(),this.configuration);
		this.converter=(MultiPartFaxJob2HTTPRequestConverter)this.faxClientSpi.getFaxJob2HTTPRequestConverter();

		Assert.assertEquals("file",this.converter.submitFileContentParameter);
		Assert.assertEquals("filename",this.converter.submitFileNameParameter);
		Assert.assertEquals("targetaddress",this.converter.submitTargetAddressParameter);
		Assert.assertEquals("targetname",this.converter.submitTargetNameParameter);
		Assert.assertEquals("sendername",this.converter.submitSenderNameParameter);
		Assert.assertEquals("senderfaxnumber",this.converter.submitSenderFaxNumberParameter);
		Assert.assertEquals("senderemail",this.converter.submitSenderEMailParameter);
		Assert.assertEquals("faxjobid",this.converter.suspendFaxJobIDParameter);
		Assert.assertEquals("faxjobid",this.converter.resumeFaxJobIDParameter);
		Assert.assertEquals("faxjobid",this.converter.cancelFaxJobIDParameter);
		Assert.assertEquals("faxjobid",this.converter.getStatusFaxJobIDParameter);
		Assert.assertEquals(0,this.converter.additionalParameters.size());
		Assert.assertTrue(this.converter.addFileNamePart);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createCommonHTTPRequestTest() throws Exception
	{
		HTTPRequest output=this.converter.createCommonHTTPRequest(this.faxClientSpi,FaxActionType.SUBMIT_FAX_JOB);
		Assert.assertNotNull(output);
		Assert.assertEquals("param1=value1&param2=value2",output.getParametersText());
		Assert.assertEquals("resource1",output.getResource());
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
		faxJob.setTargetName("TEST_TARGET_NAME");
		faxJob.setSenderName("TEST_SENDER_NAME");
		faxJob.setSenderFaxNumber("TEST_SENDER_FAX_NUMBER");
		faxJob.setSenderEmail("TEST_SENDER_EMAIL");
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.SUBMIT_FAX_JOB,faxJob);

		Assert.assertNotNull(output);
		Assert.assertEquals("param1=value1&param2=value2",output.getParametersText());
		Assert.assertEquals("resource1",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		int foundPartsAmount=0;
		for(int index=0;index<parts.length;index++)
		{
			ContentPart<?> part=parts[index];
			if(part.getName().equals(this.converter.submitFileContentParameter))
			{
				Assert.assertEquals("myfile.txt",((File)part.getContent()).getName());
				foundPartsAmount++;
			}
			else if(part.getName().equals(this.converter.submitFileNameParameter))
			{
				Assert.assertEquals("myfile.txt",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals(this.converter.submitTargetAddressParameter))
			{
				Assert.assertEquals("TEST_TARGET_ADDRESS",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals(this.converter.submitTargetNameParameter))
			{
				Assert.assertEquals("TEST_TARGET_NAME",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals(this.converter.submitSenderNameParameter))
			{
				Assert.assertEquals("TEST_SENDER_NAME",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals(this.converter.submitSenderFaxNumberParameter))
			{
				Assert.assertEquals("TEST_SENDER_FAX_NUMBER",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals(this.converter.submitSenderEMailParameter))
			{
				Assert.assertEquals("TEST_SENDER_EMAIL",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("KEY1"))
			{
				Assert.assertEquals("VALUE1",part.getContent());
				foundPartsAmount++;
			}
			else if(part.getName().equals("KEY2"))
			{
				Assert.assertEquals("VALUE2",part.getContent());
				foundPartsAmount++;
			}
		}
		Assert.assertEquals(9,foundPartsAmount);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPRequestSuspendTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("12345");
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.SUSPEND_FAX_JOB,faxJob);

		Assert.assertNotNull(output);
		Assert.assertEquals("param1=value1&param2=value2",output.getParametersText());
		Assert.assertEquals("resource2",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		Assert.assertEquals(3,parts.length);
		Assert.assertEquals("12345",parts[0].getContent());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPRequestResumeTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("12345");
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.RESUME_FAX_JOB,faxJob);

		Assert.assertNotNull(output);
		Assert.assertEquals("param1=value1&param2=value2",output.getParametersText());
		Assert.assertEquals("resource3",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		Assert.assertEquals(3,parts.length);
		Assert.assertEquals("12345",parts[0].getContent());
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
		Assert.assertEquals("param1=value1&param2=value2",output.getParametersText());
		Assert.assertEquals("resource4",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		Assert.assertEquals(3,parts.length);
		Assert.assertEquals("12345",parts[0].getContent());
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
		Assert.assertEquals("param1=value1&param2=value2",output.getParametersText());
		Assert.assertEquals("resource5",output.getResource());
		ContentPart<?>[] parts=output.getContentAsParts();
		Assert.assertNotNull(parts);
		Assert.assertEquals(3,parts.length);
		Assert.assertEquals("12345",parts[0].getContent());
	}
}