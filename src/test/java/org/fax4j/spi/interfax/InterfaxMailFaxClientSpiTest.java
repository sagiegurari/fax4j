package org.fax4j.spi.interfax;

import java.io.File;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.email.MailConnectionFactory;
import org.fax4j.spi.email.MailConnectionFactoryImpl;
import org.fax4j.spi.email.MailResourcesHolder;
import org.fax4j.spi.interfax.InterfaxMailFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.test.TestUtil.PredefinedMailFaxClientSpiTest;
import org.fax4j.util.IOHelper;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class InterfaxMailFaxClientSpiTest extends PredefinedMailFaxClientSpiTest
{
	/**
	 * This is the class constructor.
	 */
	public InterfaxMailFaxClientSpiTest()
	{
		super(InterfaxMailFaxClientSpi.class.getName(),FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_VALUE.toString(),FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_VALUE.toString());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createSubmitFaxJobMessageNullFromTest() throws Exception
	{
		InterfaxMailFaxClientSpi interfaxMailFaxClientSpi=(InterfaxMailFaxClientSpi)this.faxClientSpi;
		FaxJob faxJob=new FaxJobImpl();
		interfaxMailFaxClientSpi.createSubmitFaxJobMessage(faxJob,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createSubmitFaxJobMessageEmptyFromTest() throws Exception
	{
		InterfaxMailFaxClientSpi interfaxMailFaxClientSpi=(InterfaxMailFaxClientSpi)this.faxClientSpi;
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setSenderEmail("");
		interfaxMailFaxClientSpi.createSubmitFaxJobMessage(faxJob,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createSubmitFaxJobMessageValidTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);

		InterfaxMailFaxClientSpi interfaxMailFaxClientSpi=(InterfaxMailFaxClientSpi)this.faxClientSpi;
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFile(file);
		faxJob.setSenderEmail("123@test.com");
		MailConnectionFactory factory=new MailConnectionFactoryImpl();
		factory.initialize(interfaxMailFaxClientSpi);
		MailResourcesHolder holder=factory.createConnection().getResource();
		interfaxMailFaxClientSpi.createSubmitFaxJobMessage(faxJob,holder);
		
		file.delete();
	}
}