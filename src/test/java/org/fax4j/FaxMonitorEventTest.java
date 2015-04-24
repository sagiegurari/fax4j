package org.fax4j;

import org.fax4j.FaxMonitorEvent.FaxMonitorEventID;
import org.fax4j.spi.FaxJobImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxMonitorEventTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void nullIDTest() throws Exception
	{
		FaxMonitorEvent event=new FaxMonitorEvent(null,new FaxJobImpl(),FaxJobStatus.ERROR);
		Assert.fail("Event should throw error: "+event);
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
		FaxMonitorEvent event=new FaxMonitorEvent(FaxMonitorEventID.FAX_JOB_STATUS_CHANGE,new FaxJobImpl(),FaxJobStatus.ERROR);
		Assert.assertEquals(FaxMonitorEventID.FAX_JOB_STATUS_CHANGE,event.getID());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		FaxMonitorEvent event=new FaxMonitorEvent(FaxMonitorEventID.FAX_JOB_STATUS_CHANGE,faxJob,FaxJobStatus.ERROR);
		Assert.assertEquals(faxJob,event.getFaxJob());
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
		FaxMonitorEvent event=new FaxMonitorEvent(FaxMonitorEventID.FAX_JOB_STATUS_CHANGE,new FaxJobImpl(),FaxJobStatus.ERROR);
		String output=event.toString();
		Assert.assertNotNull(output);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(output.length()));
	}
}