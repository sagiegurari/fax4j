package org.fax4j.spi.windows;

import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class WindowsFaxClientSpiHelperTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getServerNameFromConfigurationEmptyValueTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.windows.server.name","");
		WindowsFaxClientSpi faxClientSpi=(WindowsFaxClientSpi)TestUtil.createFaxClientSpi(WindowsFaxClientSpi.class.getName(),configuration);
		String output=WindowsFaxClientSpiHelper.getServerNameFromConfiguration(faxClientSpi);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getServerNameFromConfigurationNotEmptyValueTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.windows.server.name","abc");
		WindowsFaxClientSpi faxClientSpi=(WindowsFaxClientSpi)TestUtil.createFaxClientSpi(WindowsFaxClientSpi.class.getName(),configuration);
		String output=WindowsFaxClientSpiHelper.getServerNameFromConfiguration(faxClientSpi);
		Assert.assertNotNull(output);
		Assert.assertEquals("abc",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateFaxJobIDIntValidTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID(1);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateFaxJobIDIntInvalidTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID(0);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateFaxJobIDStringValidTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID("123");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateFaxJobIDStringInvalidZeroTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID("0");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateFaxJobIDStringInvalidEmptyTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID("");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateFaxJobIDStringInvalidNullStringTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID((String)null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=NumberFormatException.class)
	public void validateFaxJobIDStringInvalidNonNumericTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID("abc");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateFaxJobIDStringInvalidNullFaxJobTest() throws Exception
	{
		WindowsFaxClientSpiHelper.validateFaxJobID((FaxJob)null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateFaxJobIDIntValidFaxJobTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobIDValidTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("123");
		int faxJobID=WindowsFaxClientSpiHelper.getFaxJobID(faxJob);
		Assert.assertEquals(123,faxJobID);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=NumberFormatException.class)
	public void getFaxJobIDInvalidTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setID("abc");
		WindowsFaxClientSpiHelper.getFaxJobID(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringNullTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString(null);
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringEmptyStringTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringUnknownValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("abcd");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.UNKNOWN,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsPendingValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("JS_PENDING");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.PENDING,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsPausedValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("JS_PAUSED");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.PENDING,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsRetryingValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("JS_RETRYING");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.PENDING,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsInProgressValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("JS_INPROGRESS");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.IN_PROGRESS,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsFailedValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("JS_FAILED");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.ERROR,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsNoLineValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("JS_NOLINE");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.ERROR,faxJobStatus);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxJobStatusFromWindowsFaxJobStatusStringWindowsRetriesExceededValueTest() throws Exception
	{
		FaxJobStatus faxJobStatus=WindowsFaxClientSpiHelper.getFaxJobStatusFromWindowsFaxJobStatusString("JS_RETRIES_EXCEEDED");
		Assert.assertNotNull(faxJobStatus);
		Assert.assertSame(FaxJobStatus.ERROR,faxJobStatus);
	}
}