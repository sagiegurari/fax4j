package org.fax4j;

import org.fax4j.FaxClientActionEvent.FaxClientActionEventID;
import org.fax4j.spi.FaxJobImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxClientActionEventTest
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
		FaxClientActionEvent event=new FaxClientActionEvent(null,new FaxJobImpl());
		Assert.fail("Event should throw error: "+event);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getIDSubmitTest() throws Exception
	{
		FaxClientActionEvent event=new FaxClientActionEvent(FaxClientActionEventID.SUBMIT_FAX_JOB,new FaxJobImpl());
		Assert.assertEquals(FaxClientActionEventID.SUBMIT_FAX_JOB,event.getID());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getIDSuspendTest() throws Exception
	{
		FaxClientActionEvent event=new FaxClientActionEvent(FaxClientActionEventID.SUSPEND_FAX_JOB,new FaxJobImpl());
		Assert.assertEquals(FaxClientActionEventID.SUSPEND_FAX_JOB,event.getID());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getIDResumeTest() throws Exception
	{
		FaxClientActionEvent event=new FaxClientActionEvent(FaxClientActionEventID.RESUME_FAX_JOB,new FaxJobImpl());
		Assert.assertEquals(FaxClientActionEventID.RESUME_FAX_JOB,event.getID());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getIDCancelTest() throws Exception
	{
		FaxClientActionEvent event=new FaxClientActionEvent(FaxClientActionEventID.CANCEL_FAX_JOB,new FaxJobImpl());
		Assert.assertEquals(FaxClientActionEventID.CANCEL_FAX_JOB,event.getID());
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
		FaxClientActionEvent event=new FaxClientActionEvent(FaxClientActionEventID.SUBMIT_FAX_JOB,faxJob);
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
		FaxClientActionEvent event=new FaxClientActionEvent(FaxClientActionEventID.SUBMIT_FAX_JOB,new FaxJobImpl());
		String output=event.toString();
		Assert.assertNotNull(output);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(output.length()));
	}
}