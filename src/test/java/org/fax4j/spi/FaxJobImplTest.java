package org.fax4j.spi;

import java.io.File;
import org.fax4j.FaxJob.FaxJobPriority;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxJobImplTest
{
	/**The fax job to test*/
	private FaxJobImpl faxJob;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		this.faxJob=new FaxJobImpl();
		this.faxJob.setFile(new File("TEST_FILE"));
		this.faxJob.setID("TEST_ID");
		this.faxJob.setPriority(FaxJobPriority.LOW_PRIORITY);
		this.faxJob.setProperty("prop1","value1");
		this.faxJob.setProperty("prop2","value2");
		this.faxJob.setSenderEmail("TEST_SENDER_EMAIL");
		this.faxJob.setSenderName("TEST_SENDER_NAME");
		this.faxJob.setSenderFaxNumber("TEST_SENDER_FAX_NUMBER");
		this.faxJob.setTargetAddress("TEST_TARGET_ADDRESS");
		this.faxJob.setTargetName("TEST_TARGET_NAME");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void toStringTest() throws Exception
	{
		String output=this.faxJob.toString();
		Assert.assertNotNull(output);
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("ID: TEST_ID")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("Priority: LOW_PRIORITY")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("Sender Email: TEST_SENDER_EMAIL")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("Sender Fax Number: TEST_SENDER_FAX_NUMBER")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("Target Address: TEST_TARGET_ADDRESS")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("Target Name: TEST_TARGET_NAME")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("Sender Name: TEST_SENDER_NAME")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(output.indexOf("File: TEST_FILE")));
	}
}