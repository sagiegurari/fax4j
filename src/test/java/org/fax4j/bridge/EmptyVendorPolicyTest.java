package org.fax4j.bridge;

import org.fax4j.spi.FaxJobImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class EmptyVendorPolicyTest
{
	/**The vendor policy to test*/
	private EmptyVendorPolicy policy;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.policy=new EmptyVendorPolicy();
		this.policy.initialize(new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void invokePolicyForRequestValidTest() throws Exception
	{
		boolean output=this.policy.invokePolicyForRequest(new Object());
		Assert.assertTrue(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void invokePolicyForResponseValidTest() throws Exception
	{
		boolean output=this.policy.invokePolicyForResponse(new Object(),new FaxJobImpl());
		Assert.assertTrue(output);
	}
}