package org.fax4j.spi;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.fax4j.FaxClientActionEvent.FaxClientActionEventID;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.Logger;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestFaxClientActionEventListener;
import org.fax4j.test.TestUtil.TestFaxMonitorEventListener;
import org.fax4j.util.IOHelper;
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
public class AbstractFax4JClientSpiTest
{
	/**The fax client SPI*/
	private FaxClientSpi faxClientSpi;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		Properties configuration=new Properties();
		configuration.setProperty("prop1","  value1  ");

		this.faxClientSpi=TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),configuration);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getLoggerTest() throws Exception
	{
		Logger output=this.faxClientSpi.getLogger();
		Assert.assertNotNull(output);
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
		String output=this.faxClientSpi.getConfigurationValue("prop1");
		Assert.assertNotNull(output);
		Assert.assertEquals("value1",output);
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
		String output=this.faxClientSpi.getConfigurationValue("prop2");
		Assert.assertNull(output);
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
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		this.faxClientSpi.createFaxJob();
		FaxClientActionEventID faxEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxEventID);
		Assert.assertEquals(FaxClientActionEventID.CREATE_FAX_JOB,faxEventID);
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
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFile(file);
		this.faxClientSpi.submitFaxJob(faxJob);

		file.delete();
		
		FaxClientActionEventID faxEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxEventID);
		Assert.assertEquals(FaxClientActionEventID.SUBMIT_FAX_JOB,faxEventID);
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
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxClientSpi.suspendFaxJob(faxJob);
		FaxClientActionEventID faxEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxEventID);
		Assert.assertEquals(FaxClientActionEventID.SUSPEND_FAX_JOB,faxEventID);
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
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxClientSpi.resumeFaxJob(faxJob);
		FaxClientActionEventID faxEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxEventID);
		Assert.assertEquals(FaxClientActionEventID.RESUME_FAX_JOB,faxEventID);
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
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		this.faxClientSpi.cancelFaxJob(faxJob);
		FaxClientActionEventID faxEventID=listener.getLastFaxEventID();
		Assert.assertNotNull(faxEventID);
		Assert.assertEquals(FaxClientActionEventID.CANCEL_FAX_JOB,faxEventID);
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

		Field field=ReflectionHelper.getField(AbstractFaxClientSpi.class,"spiFaxJobMonitor");
		FaxJobMonitor faxJobMonitor=new FaxJobMonitorImpl();
		Map<String,String> configuration=LibraryConfigurationLoader.getSystemConfiguration();
		Map<String,String> map=new HashMap<String,String>(configuration);
		map.putAll(configuration);
		map.put("org.fax4j.monitor.polling.interval","100");
		faxJobMonitor.initialize(map,this.faxClientSpi.getLogger());
		field.set(this.faxClientSpi,faxJobMonitor);
		
		TestFaxMonitorEventListener listener=new TestFaxMonitorEventListener();
		this.faxClientSpi.addFaxMonitorEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFile(file);
		this.faxClientSpi.submitFaxJob(faxJob);

		file.delete();
		
		//wait for event
		Thread.sleep(120);
		
		FaxJobStatus faxJobStatus=listener.getLastFaxJobStatus();
		Assert.assertNotNull(faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void pollForFaxJobStatuesNullInputTest() throws Exception
	{
		FaxJobStatus[] output=this.faxClientSpi.pollForFaxJobStatues(null);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void pollForFaxJobStatuesEmptyInputTest() throws Exception
	{
		FaxJobStatus[] output=this.faxClientSpi.pollForFaxJobStatues(new FaxJob[0]);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void pollForFaxJobStatuesMissingFaxJobIDTest() throws Exception
	{
		FaxJob[] faxJobs=new FaxJob[5];
		for(int index=0;index<faxJobs.length;index++)
		{
			faxJobs[index]=new FaxJobImpl();
		}
		FaxJobStatus[] output=this.faxClientSpi.pollForFaxJobStatues(faxJobs);
		Assert.assertNotNull(output);
		for(int index=0;index<faxJobs.length;index++)
		{
			Assert.assertNotNull(output[index]);
			Assert.assertEquals(FaxJobStatus.UNKNOWN,output[index]);
		}
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void pollForFaxJobStatuesValidInputTest() throws Exception
	{
		FaxJob[] faxJobs=new FaxJob[5];
		for(int index=0;index<faxJobs.length;index++)
		{
			faxJobs[index]=new FaxJobImpl();
			faxJobs[index].setID(String.valueOf(index));
		}
		FaxJobStatus[] output=this.faxClientSpi.pollForFaxJobStatues(faxJobs);
		Assert.assertNotNull(output);
		for(int index=0;index<faxJobs.length;index++)
		{
			Assert.assertNotNull(output[index]);
			Assert.assertEquals(FaxJobStatus.PENDING,output[index]);
		}
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNoTargetAddressErrorTest() throws Exception
	{
		String string="My Text Data";
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile(string,file);

		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFile(file);
		this.faxClientSpi.submitFaxJob(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNoFileErrorTest() throws Exception
	{
		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		this.faxClientSpi.submitFaxJob(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobFileDoesNotExistErrorTest() throws Exception
	{
		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFilePath("./no_file.txt");
		this.faxClientSpi.submitFaxJob(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobFileIsDirectoryErrorTest() throws Exception
	{
		TestFaxClientActionEventListener listener=new TestFaxClientActionEventListener();
		this.faxClientSpi.addFaxClientActionEventListener(listener);
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setTargetAddress("123");
		faxJob.setFilePath(".");
		this.faxClientSpi.submitFaxJob(faxJob);
	}
}