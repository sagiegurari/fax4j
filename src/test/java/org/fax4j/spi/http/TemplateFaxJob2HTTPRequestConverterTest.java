package org.fax4j.spi.http;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxJob;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.http.TemplateFaxJob2HTTPRequestConverter.FaxJob2HTTPRequestConverterConfigurationConstants;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.TestHTTPClient;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class TemplateFaxJob2HTTPRequestConverterTest
{
	/**The fax client SPI*/
	private HTTPFaxClientSpi faxClientSpi;
	/**The converter*/
	private TemplateFaxJob2HTTPRequestConverter converter;
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
		this.configuration.setProperty("org.fax4j.spi.http.faxjob2request.converter.class.name",TemplateFaxJob2HTTPRequestConverter.class.getName());
		this.configuration.setProperty("org.fax4j.spi.http.host.name","localhost");
		this.configuration.setProperty("org.fax4j.spi.http.port","80");
		this.configuration.setProperty("org.fax4j.spi.http.ssl","false");
		this.configuration.setProperty("org.fax4j.spi.http.submit.resource","${target.name}");
		this.configuration.setProperty("org.fax4j.spi.http.url.parameters","param1=value1&param2=value2&target.name=${target.name}");

		String templateText="target.address=${target.address}\n"+
							"target.name=${target.name}\n"+
							"sender.name=${sender.name}\n"+
							"sender.fax.number=${sender.fax.number}\n"+
							"sender.email=${sender.email}\n"+
							"CONTENT:\n${file}";		
		File file=File.createTempFile("submit_fax4j_template_junit",".txt");
		IOHelper.writeTextFile("submit\n"+templateText,file);
		file.deleteOnExit();
		this.configuration.setProperty("org.fax4j.spi.http.submit.template.url",file.toURI().toURL().toExternalForm());
		file=File.createTempFile("suspend_fax4j_template_junit",".txt");
		IOHelper.writeTextFile("suspend\n"+templateText,file);
		file.deleteOnExit();
		this.configuration.setProperty("org.fax4j.spi.http.suspend.template.url",file.toURI().toURL().toExternalForm());
		file=File.createTempFile("resume_fax4j_template_junit",".txt");
		IOHelper.writeTextFile("resume\n"+templateText,file);
		file.deleteOnExit();
		this.configuration.setProperty("org.fax4j.spi.http.resume.template.url",file.toURI().toURL().toExternalForm());
		file=File.createTempFile("cancel_fax4j_template_junit",".txt");
		IOHelper.writeTextFile("cancel\n"+templateText,file);
		file.deleteOnExit();
		this.configuration.setProperty("org.fax4j.spi.http.cancel.template.url",file.toURI().toURL().toExternalForm());
		file=File.createTempFile("getstatus_fax4j_template_junit",".txt");
		IOHelper.writeTextFile("getstatus\n"+templateText,file);
		file.deleteOnExit();
		this.configuration.setProperty("org.fax4j.spi.http.get.status.template.url",file.toURI().toURL().toExternalForm());

		this.faxClientSpi=(HTTPFaxClientSpi)TestUtil.createFaxClientSpi(HTTPFaxClientSpi.class.getName(),this.configuration);
		this.converter=(TemplateFaxJob2HTTPRequestConverter)this.faxClientSpi.getFaxJob2HTTPRequestConverter();
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createHTTPRequestTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath(file.getPath());
		faxJob.setTargetName("TEST_TARGET_NAME");
		HTTPRequest output=this.converter.createHTTPRequest(this.faxClientSpi,FaxActionType.SUBMIT_FAX_JOB,FaxJob2HTTPRequestConverterConfigurationConstants.SUBMIT_FAX_JOB_TEMPLATE_PROPERTY_KEY,faxJob);

		file.delete();

		Assert.assertNotNull(output);
		Assert.assertEquals("TEST_TARGET_NAME",output.getResource());
		Assert.assertEquals("param1=value1&param2=value2&target.name=TEST_TARGET_NAME",output.getParametersText());
		Assert.assertEquals("submit\ntarget.address=\ntarget.name=TEST_TARGET_NAME\nsender.name=\nsender.fax.number=\nsender.email=\nCONTENT:\nTEST_DATA",output.getContentAsString());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatTemplateTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		String template="1:${file}\n2:${target.address}\n3:${target.name}\n4:${sender.name}\n5:${sender.fax.number}\n6:${sender.email}\n";
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath(file.getPath());
		faxJob.setTargetAddress("TEST_TARGET_ADDRESS");
		faxJob.setTargetName("TEST_TARGET_NAME");
		faxJob.setSenderName("TEST_SENDER_NAME");
		faxJob.setSenderFaxNumber("TEST_SENDER_FAX_NUMBER");
		faxJob.setSenderEmail("TEST_SENDER_EMAIL");
		String output=this.converter.formatTemplate(template,faxJob);

		file.delete();

		Assert.assertNotNull(output);
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("1:TEST_DATA\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("2:TEST_TARGET_ADDRESS\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("3:TEST_TARGET_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("4:TEST_SENDER_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("5:TEST_SENDER_FAX_NUMBER\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("6:TEST_SENDER_EMAIL\n")));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatHTTPResourceTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetName("TEST_TARGET_NAME");
		String output=this.converter.formatHTTPResource(this.faxClientSpi,FaxActionType.SUBMIT_FAX_JOB,faxJob);
		Assert.assertNotNull(output);
		Assert.assertEquals("TEST_TARGET_NAME",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatHTTPURLParametersTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetName("TEST_TARGET_NAME");
		String output=this.converter.formatHTTPURLParameters(this.faxClientSpi,faxJob);
		Assert.assertNotNull(output);
		Assert.assertEquals("param1=value1&param2=value2&target.name=TEST_TARGET_NAME",output);
	}
}