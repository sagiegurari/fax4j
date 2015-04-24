package org.fax4j.spi.java4less;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxJob;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.java4less.RFaxFaxClientSpi.FaxJobExtendedPropertyConstants;
import org.fax4j.test.TestUtil;
import org.fax4j.util.IOHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class RFaxFaxClientSpiTest
{
	/**The fax client SPI*/
	private RFaxFaxClientSpi faxClientSpi;

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
		configuration.setProperty("org.fax4j.spi.rfax.port.name","COM1");
		configuration.setProperty("org.fax4j.spi.rfax.fax.class","1");

		this.faxClientSpi=(RFaxFaxClientSpi)TestUtil.createFaxClientSpi(RFaxFaxClientSpi.class.getName(),configuration);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobImplTypeTextTest() throws Exception
	{
		File file=File.createTempFile("text_junit",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("TEST_DATA",file);

		FaxJob faxJob=new FaxJobImpl();
		faxJob.setProperty(FaxJobExtendedPropertyConstants.DATA_TYPE_FAX_JOB_PROPERTY_KEY.toString(),"TEXT");
		faxJob.setProperty(FaxJobExtendedPropertyConstants.WIDTH_FAX_JOB_PROPERTY_KEY.toString(),"10");
		faxJob.setProperty(FaxJobExtendedPropertyConstants.HEIGHT_FAX_JOB_PROPERTY_KEY.toString(),"10");
		faxJob.setFile(file);
		faxJob.setTargetAddress("123123123123123123");
		this.faxClientSpi.submitFaxJobImpl(faxJob);
		
		file.delete();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobImplTypePDFTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setProperty(FaxJobExtendedPropertyConstants.DATA_TYPE_FAX_JOB_PROPERTY_KEY.toString(),"PDF");
		faxJob.setProperty(FaxJobExtendedPropertyConstants.WIDTH_FAX_JOB_PROPERTY_KEY.toString(),"10");
		faxJob.setProperty(FaxJobExtendedPropertyConstants.HEIGHT_FAX_JOB_PROPERTY_KEY.toString(),"10");
		faxJob.setFilePath("./test.pdf");
		faxJob.setTargetAddress("123123123123123123");
		this.faxClientSpi.submitFaxJobImpl(faxJob);
	}
}