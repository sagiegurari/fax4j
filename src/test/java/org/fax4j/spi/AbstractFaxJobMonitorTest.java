package org.fax4j.spi;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxClient;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.util.LibraryConfigurationLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractFaxJobMonitorTest
{
	/**The monitor*/
	private AbstractFaxJobMonitor abstractFaxJobMonitor;
	/**The fax client SPI*/
	private FaxClientSpi faxClientSpi;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		this.abstractFaxJobMonitor=new AbstractFaxJobMonitor()
		{
			@Override
			public void stopMonitoringAllFaxJobsImpl(FaxClientSpi spi)
			{
				//empty
			}
			
			@Override
			public void monitorFaxJobImpl(FaxClientSpi spi,FaxJob faxJob)
			{
				//empty
			}
			
			@Override
			protected void initializeImpl()
			{
				//empty
			}
		};
		this.faxClientSpi=TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		Logger logger=this.faxClientSpi.getLogger();
		Map<String,String> configuration=LibraryConfigurationLoader.getSystemConfiguration();
		Map<String,String> map=new HashMap<String,String>(configuration);
		map.putAll(configuration);
		map.put("org.fax4j.monitor.polling.interval","50");
		this.abstractFaxJobMonitor.initialize(map,logger);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueExistingPropertyTest() throws Exception
	{
		String output=this.abstractFaxJobMonitor.getConfigurationValue("org.fax4j.client.class.name");
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxClient.class.getName(),output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueMissingPropertyTest() throws Exception
	{
		String output=this.abstractFaxJobMonitor.getConfigurationValue("prop2");
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void monitorFaxJobMissingFaxClientSPITest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.abstractFaxJobMonitor.monitorFaxJob(null,faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void monitorFaxJobMissingFaxJobTest() throws Exception
	{
		this.abstractFaxJobMonitor.monitorFaxJob(this.faxClientSpi,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void monitorFaxJobMissingFaxJobIDTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		this.abstractFaxJobMonitor.monitorFaxJob(this.faxClientSpi,faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void monitorFaxJobValidInputTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.abstractFaxJobMonitor.monitorFaxJob(this.faxClientSpi,faxJob);
		
		this.abstractFaxJobMonitor.stopMonitoringAllFaxJobs(this.faxClientSpi);
	}
}