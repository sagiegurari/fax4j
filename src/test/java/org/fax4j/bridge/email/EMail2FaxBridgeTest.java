package org.fax4j.bridge.email;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class EMail2FaxBridgeTest
{
	/**The fax bridge to test*/
	private EMail2FaxBridge faxBridge;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.faxBridge=new EMail2FaxBridge();

		Properties configuration=new Properties();
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
	@Test(expected=FaxException.class)
	public void initializeInvalidParserClassNameTest() throws Exception
	{
		this.faxBridge=new EMail2FaxBridge();

		Properties configuration=new Properties();
		configuration.setProperty(EMail2FaxBridge.MAIL_MESSAGE_PARSER_CLASS_NAME_PROPERTY_KEY,"123");
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
		Message message=new MimeMessage((Session)null);
		message.setSubject("fax:123456789");
		message.setFrom(new InternetAddress("test@test.test"));

		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);
		DataSource source=new FileDataSource(file);
		BodyPart messageFileAttachmentBodyPart=new MimeBodyPart();
		messageFileAttachmentBodyPart.setDataHandler(new DataHandler(source));
		messageFileAttachmentBodyPart.setFileName(file.getName());
		Multipart multipart=new MimeMultipart();
		multipart.addBodyPart(messageFileAttachmentBodyPart);
		message.setContent(multipart);

		FaxJob faxJob=this.faxBridge.submitFaxJob(message);
		Assert.assertNotNull(faxJob);
		Assert.assertNotNull(faxJob.getFile());
		
		file.delete();
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
		Message message=new MimeMessage((Session)null);
		message.setSubject("fax:123456789");
		message.setFrom(new InternetAddress("test@test.test"));
		this.faxBridge.submitFaxJob(message);
	}
}