package org.fax4j.spi;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.Logger;
import org.fax4j.spi.FaxJobMonitorImpl.PollerTask;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.util.LibraryConfigurationLoader;
import org.fax4j.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxJobMonitorImplTest
{
	/**The monitor*/
	private FaxJobMonitorImpl faxJobMonitor;
	/**The fax client SPI*/
	private FaxClientSpi faxClientSpi;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		this.faxJobMonitor=new FaxJobMonitorImpl();
		this.faxClientSpi=TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		Logger logger=this.faxClientSpi.getLogger();
		Map<String,String> configuration=LibraryConfigurationLoader.getSystemConfiguration();
		Map<String,String> map=new HashMap<String,String>(configuration);
		map.putAll(configuration);
		map.put("org.fax4j.monitor.polling.interval","50");
		this.faxJobMonitor.initialize(map,logger);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTimerTaskStoppedTest() throws Exception
	{
		Assert.assertFalse(this.faxJobMonitor.pollerTask.isRunning());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTimerTaskOnOffFlagTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		Assert.assertFalse(this.faxJobMonitor.pollerTask.isRunning());
		this.faxJobMonitor.monitorFaxJob(this.faxClientSpi,faxJob);
		Assert.assertTrue(this.faxJobMonitor.pollerTask.isRunning());
		this.faxJobMonitor.stopMonitoringAllFaxJobs(this.faxClientSpi);
		Assert.assertFalse(this.faxJobMonitor.pollerTask.isRunning());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void runPollingCycleSingleSPITest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxJobMonitor.monitorFaxJob(this.faxClientSpi,faxJob);
		this.faxJobMonitor.pollerTask.setRunning(false);
		this.faxJobMonitor.runPollingCycle();
		String currentStatus=faxJob.getProperty("test.status",null);
		Assert.assertNotNull(currentStatus);
		Assert.assertEquals(FaxJobStatus.IN_PROGRESS,FaxJobStatus.valueOf(currentStatus));
		this.faxJobMonitor.runPollingCycle();
		currentStatus=faxJob.getProperty("test.status",null);
		Assert.assertNotNull(currentStatus);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,FaxJobStatus.valueOf(currentStatus));
		this.faxJobMonitor.runPollingCycle();
		this.faxJobMonitor.pollerTask.setRunning(true);
		this.faxJobMonitor.runPollingCycle();
		Assert.assertFalse(this.faxJobMonitor.pollerTask.isRunning());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void runPollingCycleMultiSPITest() throws Exception
	{
		FaxClientSpi[] faxClientSpis=new FaxClientSpi[10];
		for(int index=0;index<faxClientSpis.length;index++)
		{
			faxClientSpis[index]=TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		}
		FaxJob[] faxJobs=new FaxJob[faxClientSpis.length*10];
		for(int index=0;index<faxJobs.length;index++)
		{
			faxJobs[index]=new FaxJobImpl();
			faxJobs[index].setID(String.valueOf(index));
		}
		Field field=ReflectionHelper.getField(PollerTask.class,"running");
		field.set(this.faxJobMonitor.pollerTask,Boolean.TRUE);
		for(int index=0;index<faxJobs.length;index++)
		{
			this.faxJobMonitor.monitorFaxJob(faxClientSpis[index%10],faxJobs[index]);
		}
		FaxJob faxJob=faxJobs[0];
		this.faxJobMonitor.runPollingCycle();
		String currentStatus=faxJob.getProperty("test.status",null);
		Assert.assertNotNull(currentStatus);
		Assert.assertEquals(FaxJobStatus.IN_PROGRESS,FaxJobStatus.valueOf(currentStatus));
		this.faxJobMonitor.runPollingCycle();
		currentStatus=faxJob.getProperty("test.status",null);
		Assert.assertNotNull(currentStatus);
		Assert.assertEquals(FaxJobStatus.UNKNOWN,FaxJobStatus.valueOf(currentStatus));
		this.faxJobMonitor.runPollingCycle();
		this.faxJobMonitor.pollerTask.setRunning(true);
		this.faxJobMonitor.runPollingCycle();
		Assert.assertFalse(this.faxJobMonitor.pollerTask.isRunning());
	}
}