package org.fax4j.spi.comm;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.TestCommPortConnectionFactory;
import org.fax4j.test.TestUtil.TestFaxModemAdapter;
import org.fax4j.util.Connection;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class CommFaxClientSpiTest
{
	/**The fax client SPI*/
	private CommFaxClientSpi faxClientSpi;
	/**The configuration*/
	private Properties configuration;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		this.configuration=new Properties();
		this.configuration.setProperty("org.fax4j.spi.comm.port.name","COM1");
		this.configuration.setProperty("org.fax4j.spi.comm.connection.factory.class.name",TestCommPortConnectionFactory.class.getName());
		this.configuration.setProperty("org.fax4j.spi.comm.fax.modem.class.name",TestFaxModemAdapter.class.getName());
		
		this.faxClientSpi=(CommFaxClientSpi)TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),this.configuration);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeImplNoConnectionFactoryClassNameTest() throws Exception
	{
		this.configuration.setProperty("org.fax4j.spi.comm.connection.factory.class.name","");		
		this.faxClientSpi=(CommFaxClientSpi)TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),this.configuration);
		Assert.assertEquals(RXTXCommPortConnectionFactory.class,this.faxClientSpi.createCommPortConnectionFactory().getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeImplNoFaxModemAdapterClassNameTest() throws Exception
	{
		this.configuration.setProperty("org.fax4j.spi.comm.fax.modem.class.name","");		
		TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),this.configuration);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getCommPortConnectionTest() throws Exception
	{
		Connection<CommPortAdapter> connection=this.faxClientSpi.getCommPortConnection();
		Assert.assertNotNull(connection);
		CommPortAdapter adapter=connection.getResource();
		Assert.assertTrue(adapter.isOpen());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void releaseCommPortConnectionTest() throws Exception
	{
		Connection<CommPortAdapter> connection=this.faxClientSpi.getCommPortConnection();
		Assert.assertNotNull(connection);
		CommPortAdapter adapter=connection.getResource();
		Assert.assertTrue(adapter.isOpen());
		this.faxClientSpi.releaseCommPortConnection();
		Assert.assertFalse(adapter.isOpen());
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
		String string="My Text Data";
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile(string,file);

		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFile(file);
		this.faxClientSpi.submitFaxJob(faxJob);
		String faxJobID=faxJob.getID();
		Assert.assertNotNull(faxJobID);
		Assert.assertEquals("123",faxJobID);
	}
}