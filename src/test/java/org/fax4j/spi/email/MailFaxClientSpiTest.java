package org.fax4j.spi.email;

import java.io.File;
import java.text.MessageFormat;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.PredefinedMailFaxClientSpiTest;
import org.fax4j.util.Connection;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class MailFaxClientSpiTest extends PredefinedMailFaxClientSpiTest
{
	/**
	 * This is the class constructor.
	 */
	public MailFaxClientSpiTest()
	{
		super(MailFaxClientSpi.class.getName(),"{0}@myprovider.com","fax:{0}");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void connectionFactoryClassNameMissingValueTest() throws Exception
	{
		Assert.assertEquals(this.faxClientSpi.createMailConnectionFactoryImpl(null).getClass(),MailConnectionFactoryImpl.class);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void mailAddressTemplateMissingValueTest() throws Exception
	{
		this.configuration.setProperty("org.fax4j.spi.mail.address.template","");
		TestUtil.createFaxClientSpi(MailFaxClientSpi.class.getName(),this.configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void mailSubjectTemplateMissingValueTest() throws Exception
	{
		this.configuration.setProperty("org.fax4j.spi.mail.subject.template","");
		TestUtil.createFaxClientSpi(MailFaxClientSpi.class.getName(),this.configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createMailConnectionTest() throws Exception
	{
		Connection<MailResourcesHolder> connection=this.faxClientSpi.createMailConnection();
		Assert.assertNotNull(connection);
		MailResourcesHolder holder=connection.getResource();
		Assert.assertNotNull(holder);
		Assert.assertNotNull(holder.getSession());
		Assert.assertNotNull(holder.getTransport());
		Assert.assertTrue(holder.getTransport().isConnected());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void closeMailConnectionTest() throws Exception
	{
		Connection<MailResourcesHolder> connection=this.faxClientSpi.createMailConnection();
		Assert.assertNotNull(connection);
		MailResourcesHolder holder=connection.getResource();
		Assert.assertNotNull(holder);
		Assert.assertNotNull(holder.getSession());
		Assert.assertNotNull(holder.getTransport());
		Assert.assertTrue(holder.getTransport().isConnected());
		this.faxClientSpi.closeMailConnection(connection);
		Assert.assertFalse(holder.getTransport().isConnected());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getMailConnectionTest() throws Exception
	{
		Connection<MailResourcesHolder> connection=this.faxClientSpi.getMailConnection();
		Assert.assertNotNull(connection);
		MailResourcesHolder holder=connection.getResource();
		Assert.assertNotNull(holder);
		Assert.assertNotNull(holder.getSession());
		Assert.assertNotNull(holder.getTransport());
		Assert.assertTrue(holder.getTransport().isConnected());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void sendMailValidMessageTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);

		Connection<MailResourcesHolder> connection=this.faxClientSpi.getMailConnection();
		Assert.assertNotNull(connection);
		Message message=new MimeMessage(connection.getResource().getSession());
		message.addRecipient(RecipientType.TO,new InternetAddress("aaa@aaa"));
		DataSource source=new FileDataSource(file);
		MimeBodyPart messageFileAttachmentBodyPart=new MimeBodyPart();
		messageFileAttachmentBodyPart.setDataHandler(new DataHandler(source));
		messageFileAttachmentBodyPart.setFileName(file.getName());
		MimeMultipart multipart=new MimeMultipart();
		multipart.addBodyPart(messageFileAttachmentBodyPart);
		message.setContent(multipart);
		this.faxClientSpi.sendMail(null,connection,message);
		
		file.delete();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void sendMailNullMessageTest() throws Exception
	{
		this.faxClientSpi.sendMail(null,null,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createSubmitFaxJobMessageTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setSenderName("mysender");
		faxJob.setTargetName("mytargetname");
		faxJob.setTargetAddress("123");
		faxJob.setSenderEmail("aaa@aaaaaa.com");
		faxJob.setFilePath(file.getPath());	

		Connection<MailResourcesHolder> connection=this.faxClientSpi.getMailConnection();
		Assert.assertNotNull(connection);
		MailResourcesHolder holder=connection.getResource();
		Message message=this.faxClientSpi.createSubmitFaxJobMessage(faxJob,holder);
		Assert.assertNotNull(message);
		Assert.assertEquals("aaa@aaaaaa.com",message.getFrom()[0].toString());
		String value=MessageFormat.format(this.faxClientSpi.mailAddressTemplate,new Object[]{"123"});
		Assert.assertEquals(value,message.getRecipients(RecipientType.TO)[0].toString());
		value=MessageFormat.format(this.faxClientSpi.mailSubjectTemplate,new Object[]{"123"});
		Assert.assertEquals(value,message.getSubject());
		Assert.assertEquals(file,((FileDataSource)((Multipart)message.getContent()).getBodyPart(0).getDataHandler().getDataSource()).getFile());
		
		file.delete();
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
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setSenderName("mysender");
		faxJob.setTargetName("mytargetname");
		faxJob.setTargetAddress("123");
		faxJob.setSenderEmail("aaa@aaaaaa.com");
		faxJob.setFilePath(file.getPath());	

		this.faxClientSpi.submitFaxJob(faxJob);
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
}