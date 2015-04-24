package org.fax4j.bridge.email;

import java.io.File;
import java.util.HashMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.fax4j.bridge.FileInfo;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class DefaultMailMessageParserTest
{
	/**The parser to test*/
	private DefaultMailMessageParser parser;
	/**The message*/
	private Message message;
	/**The file name*/
	private String fileName;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.parser=new DefaultMailMessageParser();
		this.parser.initialize(new HashMap<String,String>());
		this.message=new MimeMessage((Session)null);
		this.message.setSubject("fax:123456789");
		this.message.setFrom(new InternetAddress("test@test.test"));

		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		this.fileName=file.getName();
		IOHelper.writeTextFile("abc",file);
		DataSource source=new FileDataSource(file);
		BodyPart messageFileAttachmentBodyPart=new MimeBodyPart();
		messageFileAttachmentBodyPart.setDataHandler(new DataHandler(source));
		messageFileAttachmentBodyPart.setFileName(file.getName());
		Multipart multipart=new MimeMultipart();
		multipart.addBodyPart(messageFileAttachmentBodyPart);
		this.message.setContent(multipart);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTargetAddressValidTest() throws Exception
	{
		String output=this.parser.getTargetAddress(this.message);
		Assert.assertNotNull(output);
		Assert.assertEquals("123456789",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTargetAddressEmptyAddressTest() throws Exception
	{
		this.message.setSubject("fax:");
		String output=this.parser.getTargetAddress(this.message);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTargetAddressInvalidAddressTest() throws Exception
	{
		this.message.setSubject("123");
		String output=this.parser.getTargetAddress(this.message);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTargetAddressNullAddressTest() throws Exception
	{
		this.message.setSubject(null);
		String output=this.parser.getTargetAddress(this.message);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTargetAddressEmptyStringAddressTest() throws Exception
	{
		this.message.setSubject("");
		String output=this.parser.getTargetAddress(this.message);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSenderEmailValidTest() throws Exception
	{
		String output=this.parser.getSenderEmail(this.message);
		Assert.assertNotNull(output);
		Assert.assertEquals("test@test.test",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSenderEmailMultipleFromTest() throws Exception
	{
		this.message.addFrom(new InternetAddress[]{new InternetAddress("test1@test.test"),new InternetAddress("test2@test.test")});
		String output=this.parser.getSenderEmail(this.message);
		Assert.assertNotNull(output);
		Assert.assertEquals("test@test.test",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSenderEmailNullFromTest() throws Exception
	{
		this.message.setFrom(null);
		String output=this.parser.getSenderEmail(this.message);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSenderEmailEmptyArrayFromTest() throws Exception
	{
		this.message.setFrom(null);
		this.message.addFrom(new Address[0]);
		String output=this.parser.getSenderEmail(this.message);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileInfoValidTest() throws Exception
	{
		FileInfo output=this.parser.getFileInfo(this.message);
		Assert.assertNotNull(output);
		Assert.assertEquals(this.fileName,output.getName());
		Assert.assertArrayEquals("abc".getBytes(IOHelper.getDefaultEncoding()),output.getContent());
	}
}