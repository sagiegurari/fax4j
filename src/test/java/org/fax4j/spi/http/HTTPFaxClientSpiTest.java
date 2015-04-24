package org.fax4j.spi.http;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxJobImpl;
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
public class HTTPFaxClientSpiTest
{
	/**The fax client SPI*/
	private HTTPFaxClientSpi faxClientSpi;
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
		this.configuration.setProperty("org.fax4j.spi.http.response.handler.class.name",EmptyHTTPResponseHandler.class.getName());
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
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHTTPClientTest() throws Exception
	{
		HTTPClient httpClient=this.faxClientSpi.getHTTPClient();
		Assert.assertNotNull(httpClient);
		Assert.assertEquals(TestHTTPClient.class,httpClient.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHTTPClientConfigurationTest() throws Exception
	{
		HTTPClientConfiguration httpClientConfiguration=this.faxClientSpi.getHTTPClientConfiguration();
		Assert.assertNotNull(httpClientConfiguration);
		Assert.assertEquals("localhost",httpClientConfiguration.getHostName());
		Assert.assertFalse(httpClientConfiguration.isSSL());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJob2HTTPRequestConverterTest() throws Exception
	{
		FaxJob2HTTPRequestConverter output=this.faxClientSpi.getFaxJob2HTTPRequestConverter();
		Assert.assertNotNull(output);
		Assert.assertEquals(TemplateFaxJob2HTTPRequestConverter.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHTTPResponseHandlerTest() throws Exception
	{
		HTTPResponseHandler output=this.faxClientSpi.getHTTPResponseHandler();
		Assert.assertNotNull(output);
		Assert.assertEquals(EmptyHTTPResponseHandler.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHTTPResourceTest() throws Exception
	{
		String resource=this.faxClientSpi.getHTTPResource(FaxActionType.SUBMIT_FAX_JOB);
		Assert.assertNotNull(resource);
		Assert.assertEquals("${target.name}",resource);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getHTTPURLParametersTest() throws Exception
	{
		String value=this.faxClientSpi.getHTTPURLParameters();
		Assert.assertNotNull(value);
		Assert.assertEquals("param1=value1&param2=value2&target.name=${target.name}",value);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createSubmitFaxJobHTTPRequestTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath(file.getPath());
		HTTPRequest output=this.faxClientSpi.createSubmitFaxJobHTTPRequest(faxJob);

		file.delete();

		Assert.assertNotNull(output);
		String text=output.getContentAsString();
		Assert.assertSame(Integer.valueOf(0),Integer.valueOf(text.indexOf("submit")));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createSuspendFaxJobHTTPRequestTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath(file.getPath());
		HTTPRequest output=this.faxClientSpi.createSuspendFaxJobHTTPRequest(faxJob);

		file.delete();

		Assert.assertNotNull(output);
		String text=output.getContentAsString();
		Assert.assertSame(Integer.valueOf(0),Integer.valueOf(text.indexOf("suspend")));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createResumeFaxJobHTTPRequestTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath(file.getPath());
		HTTPRequest output=this.faxClientSpi.createResumeFaxJobHTTPRequest(faxJob);

		file.delete();

		Assert.assertNotNull(output);
		String text=output.getContentAsString();
		Assert.assertSame(Integer.valueOf(0),Integer.valueOf(text.indexOf("resume")));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createCancelFaxJobHTTPRequestTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath(file.getPath());
		HTTPRequest output=this.faxClientSpi.createCancelFaxJobHTTPRequest(faxJob);

		file.delete();

		Assert.assertNotNull(output);
		String text=output.getContentAsString();
		Assert.assertSame(Integer.valueOf(0),Integer.valueOf(text.indexOf("cancel")));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createGetFaxJobStatusHTTPRequestTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath(file.getPath());
		faxJob.setID("123");
		HTTPRequest output=this.faxClientSpi.createGetFaxJobStatusHTTPRequest(faxJob);

		file.delete();

		Assert.assertNotNull(output);
		String text=output.getContentAsString();
		Assert.assertSame(Integer.valueOf(0),Integer.valueOf(text.indexOf("getstatus")));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
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
	@Test
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
	public void submitFaxJobTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFilePath(file.getPath());
		this.faxClientSpi.submitFaxJob(faxJob);
		
		file.delete();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitHTTPRequestNoHttpRequestTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxClientSpi.submitHTTPRequest(faxJob,null,FaxActionType.SUBMIT_FAX_JOB);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusImplTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		FaxJobStatus output=this.faxClientSpi.getFaxJobStatusImpl(faxJob);
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,output);
	}
}