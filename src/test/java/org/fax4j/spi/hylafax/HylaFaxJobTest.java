package org.fax4j.spi.hylafax;

import junit.framework.Assert;
import gnu.hylafax.Job;
import org.fax4j.FaxException;
import org.fax4j.FaxJob.FaxJobPriority;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class HylaFaxJobTest
{
	/**The fax job to test*/
	private HylaFaxJob faxJob;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		Job job=Mockito.mock(Job.class);
		Mockito.when(Long.valueOf(job.getId())).thenReturn(Long.valueOf(123));
		Mockito.when(Integer.valueOf(job.getPriority())).thenReturn(Integer.valueOf(Job.PRIORITY_HIGH));
		Mockito.when(job.getDialstring()).thenReturn("123456789");
		Mockito.when(job.getFromUser()).thenReturn("TEST_SENDER_NAME");
		this.faxJob=new HylaFaxJob(job);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void setIDTest() throws Exception
	{
		this.faxJob.setID("123");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getIDTest() throws Exception
	{
		String output=this.faxJob.getID();
		Assert.assertNotNull(output);
		Assert.assertEquals("123",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getPriorityTest() throws Exception
	{
		FaxJobPriority output=this.faxJob.getPriority();
		Assert.assertNotNull(output);
		Assert.assertEquals(FaxJobPriority.HIGH_PRIORITY,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTargetAddressTest() throws Exception
	{
		String output=this.faxJob.getTargetAddress();
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
	public void getTargetNameTest() throws Exception
	{
		String output=this.faxJob.getTargetName();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSenderNameTest() throws Exception
	{
		String output=this.faxJob.getSenderName();
		Assert.assertNotNull(output);
		Assert.assertEquals("TEST_SENDER_NAME",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSenderFaxNumberTest() throws Exception
	{
		String output=this.faxJob.getSenderFaxNumber();
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getSenderEmailTest() throws Exception
	{
		String output=this.faxJob.getSenderEmail();
		Assert.assertNull(output);
	}
}