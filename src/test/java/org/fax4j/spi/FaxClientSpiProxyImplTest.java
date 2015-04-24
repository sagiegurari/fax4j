package org.fax4j.spi;

import org.fax4j.spi.FaxClientSpiProxy.FaxClientSpiProxyEventType;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestFaxClientSpiInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxClientSpiProxyImplTest
{
	/**The count value*/
	private int count;
	/**The proxy impl*/
	private FaxClientSpiProxyImpl proxy;
	/**The interceptor*/
	private TestFaxClientSpiInterceptor interceptor;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		this.proxy=new FaxClientSpiProxyImpl();
		FaxClientSpi faxClientSpi=new EmptyFaxClientSpi(true);
		this.count=5;
		FaxClientSpiInterceptor[] interceptors=new FaxClientSpiInterceptor[this.count];
		this.interceptor=new TestFaxClientSpiInterceptor();
		for(int index=0;index<this.count;index++)
		{
			interceptors[index]=this.interceptor;
		}
		this.proxy.initialize(faxClientSpi,interceptors);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void invokeInterceptorsImplPreTest() throws Exception
	{
		this.proxy.invokeInterceptorsImpl(FaxClientSpiProxyEventType.PRE_EVENT_TYPE,null,null,null,null);
		Assert.assertEquals(this.count,this.interceptor.preCount);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void invokeInterceptorsImplPostTest() throws Exception
	{
		this.proxy.invokeInterceptorsImpl(FaxClientSpiProxyEventType.POST_EVENT_TYPE,null,null,null,null);
		Assert.assertEquals(this.count,this.interceptor.postCount);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void invokeInterceptorsImplOnErrorTest() throws Exception
	{
		this.proxy.invokeInterceptorsImpl(FaxClientSpiProxyEventType.ERROR_EVENT_TYPE,null,null,null,null);
		Assert.assertEquals(this.count,this.interceptor.onErrorCount);
	}
}