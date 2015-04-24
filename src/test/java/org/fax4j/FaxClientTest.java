package org.fax4j;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.fax4j.FaxClientActionEvent.FaxClientActionEventID;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.FaxMonitorEvent.FaxMonitorEventID;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.common.Logger;
import org.fax4j.spi.AbstractFaxClientSpi;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.spi.FaxJobMonitor;
import org.fax4j.spi.FaxJobMonitorImpl;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestFaxClientActionEventListener;
import org.fax4j.test.TestUtil.TestFaxMonitorEventListener;
import org.fax4j.util.IOHelper;
import org.fax4j.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxClientTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void exampleTest() throws Exception
	{
		//data setup
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		File file=File.createTempFile("temp_",".txt");
		IOHelper.writeTextFile("TEST_DATA",file);
		file.deleteOnExit();

		//get new instance of a fax client
		FaxClient faxClient=FaxClientFactory.createFaxClient("test",configuration);
		 
		//create a new fax job
		FaxJob faxJob=faxClient.createFaxJob();

		//set fax job values
		faxJob.setFile(file);
		faxJob.setPriority(FaxJobPriority.HIGH_PRIORITY);
		faxJob.setTargetAddress("555-555");
		faxJob.setTargetName("YourName");
		faxJob.setSenderEmail("myemail@mycompany.com");
		faxJob.setSenderName("MyName");

		//submit fax job
		faxClient.submitFaxJob(faxJob);
		
		file.delete();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void constructorEmptySPITest() throws Exception
	{
		FaxClient client=new FaxClient(null);
		Assert.assertEquals(client.getProvider().getName(),Fax4JProvider.FAX4J_PROVIDER.getName());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void constructorEmptySPILoggerTest() throws Exception
	{
		FaxClientSpi faxClientSpi=new FaxClientSpi()
		{
			/**
			 * Returns the internal logger.
			 *  
			 * @return	The internal logger
			 */
			public Logger getLogger()
			{
				return null;
			}
			public FaxJob createFaxJob()
			{
				return null;
			}
			public void submitFaxJob(FaxJob faxJob)
			{
				//empty
			}
			public void suspendFaxJob(FaxJob faxJob)
			{
				//empty
			}
			public void resumeFaxJob(FaxJob faxJob)
			{
				//empty
			}
			public void cancelFaxJob(FaxJob faxJob)
			{
				//empty
			}
			public FaxJobStatus getFaxJobStatus(FaxJob faxJob)
			{
				return null;
			}
			public void addFaxClientActionEventListener(FaxClientActionEventListener listener)
			{
				//empty
			}
			public void removeFaxClientActionEventListener(FaxClientActionEventListener listener)
			{
				//empty
			}
			public void removeAllFaxClientActionEventListeners()
			{
				//empty
			}
			public void addFaxMonitorEventListener(FaxMonitorEventListener listener)
			{
				//empty
			}
			public void removeFaxMonitorEventListener(FaxMonitorEventListener listener)
			{
				//empty
			}
			public void removeAllFaxMonitorEventListeners()
			{
				//empty
			}
			public Provider getProvider()
			{
				return null;
			}
			public void initialize(Map<String,String> configuration,Logger logger,FaxJobMonitor faxJobMonitor)
			{
				//empty
			}
			public String getConfigurationValue(String key)
			{
				return null;
			}
			public void fireFaxMonitorEvent(FaxMonitorEventID id,FaxJob faxJob,FaxJobStatus faxJobStatus)
			{
				//empty
			}
			public FaxJobMonitor getFaxJobMonitor()
			{
				return null;
			}
			public FaxJobStatus[] pollForFaxJobStatues(FaxJob[] faxJobs)
			{
				return null;
			}
			public boolean isFaxMonitorEventsSupported()
			{
				return false;
			}
			public Map<String,String> getConfiguration()
			{
				return null;
			}
			public String getConfigurationValue(Enum<?> key)
			{
				return this.getConfigurationValue(key.toString());
			}
			public String getPropertyPart()
			{
				return null;
			}
		};
		FaxClient client=new FaxClient(faxClientSpi);
		Assert.assertEquals(client.getProvider().getName(),Fax4JProvider.FAX4J_PROVIDER.getName());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getProviderTest() throws Exception
	{
		EmptyFaxClientSpi faxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		FaxClient faxClient=new FaxClient(faxClientSpi);
		Provider provider=faxClient.getProvider();
		Assert.assertNotNull(provider);
		Assert.assertEquals(faxClientSpi.getProvider(),provider);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void faxJobCreatedEventTest() throws Exception
	{
		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		EmptyFaxClientSpi faxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		FaxClient faxClient=new FaxClient(faxClientSpi);
		faxClient.addFaxClientActionEventListener(listener);
		faxClient.createFaxJob();
		FaxClientActionEventID faxClientActionEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxClientActionEventID);
		Assert.assertEquals(FaxClientActionEventID.CREATE_FAX_JOB,faxClientActionEventID);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void faxJobSubmittedEventTest() throws Exception
	{
		String string="My Text Data";
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile(string,file);

		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		EmptyFaxClientSpi faxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		FaxClient faxClient=new FaxClient(faxClientSpi);
		faxClient.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFile(file);
		faxClient.submitFaxJob(faxJob);

		file.delete();
		
		FaxClientActionEventID faxClientActionEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxClientActionEventID);
		Assert.assertEquals(FaxClientActionEventID.SUBMIT_FAX_JOB,faxClientActionEventID);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void faxJobSuspendedEventTest() throws Exception
	{
		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		EmptyFaxClientSpi faxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		FaxClient faxClient=new FaxClient(faxClientSpi);
		faxClient.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		faxClient.suspendFaxJob(faxJob);
		FaxClientActionEventID faxClientActionEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxClientActionEventID);
		Assert.assertEquals(FaxClientActionEventID.SUSPEND_FAX_JOB,faxClientActionEventID);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void faxJobResumedEventTest() throws Exception
	{
		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		EmptyFaxClientSpi faxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		FaxClient faxClient=new FaxClient(faxClientSpi);
		faxClient.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		faxClient.resumeFaxJob(faxJob);
		FaxClientActionEventID faxClientActionEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxClientActionEventID);
		Assert.assertEquals(FaxClientActionEventID.RESUME_FAX_JOB,faxClientActionEventID);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void faxJobCancelledEventTest() throws Exception
	{
		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		EmptyFaxClientSpi faxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		FaxClient faxClient=new FaxClient(faxClientSpi);
		faxClient.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		faxClient.cancelFaxJob(faxJob);
		FaxClientActionEventID faxClientActionEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxClientActionEventID);
		Assert.assertEquals(FaxClientActionEventID.CANCEL_FAX_JOB,faxClientActionEventID);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void faxJobStatusChangeEventTest() throws Exception
	{
		String string="My Text Data";
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile(string,file);

		EmptyFaxClientSpi faxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
		Field field=ReflectionHelper.getField(AbstractFaxClientSpi.class,"spiFaxJobMonitor");
		FaxJobMonitor faxJobMonitor=new FaxJobMonitorImpl();
		Map<String,String> configuration=faxClientSpi.getAllConfiguration();
		Map<String,String> map=new HashMap<String,String>(configuration);
		map.putAll(configuration);
		map.put("org.fax4j.monitor.polling.interval","100");
		faxJobMonitor.initialize(map,faxClientSpi.getLogger());
		field.set(faxClientSpi,faxJobMonitor);
		
		TestFaxMonitorEventListener listener=new TestFaxMonitorEventListener();
		FaxClient faxClient=new FaxClient(faxClientSpi);
		faxClient.addFaxMonitorEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFile(file);
		faxClient.submitFaxJob(faxJob);

		file.delete();
		
		//wait for event
		Thread.sleep(120);
		
		FaxJobStatus faxJobStatus=listener.getLastFaxJobStatus();
		Assert.assertNotNull(faxJobStatus);
	}
}