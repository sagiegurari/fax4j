package org.fax4j.bridge;

import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.Provider;
import org.fax4j.common.Fax4JProvider;
import org.fax4j.spi.FaxJobImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractVendorPolicyTest
{
	/**The vendor policy to test*/
	private AbstractVendorPolicy policy;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.policy=new AbstractVendorPolicy(Fax4JProvider.FAX4J_PROVIDER)
		{
			@Override
			protected void initializeImpl()
			{
				//empty
			}
			@Override
			protected boolean invokePolicyForRequestImpl(Object requestDataHolder)
			{
				return true;
			}
			@Override
			protected boolean invokePolicyForResponseImpl(Object requestDataHolder,FaxJob faxJob)
			{
				return true;
			}
		};
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void nullProviderTest() throws Exception
	{
		this.policy=new AbstractVendorPolicy(null)
		{
			@Override
			protected void initializeImpl()
			{
				//empty
			}
			@Override
			protected boolean invokePolicyForRequestImpl(Object requestDataHolder)
			{
				return true;
			}
			@Override
			protected boolean invokePolicyForResponseImpl(Object requestDataHolder,FaxJob faxJob)
			{
				return true;
			}
		};
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeAgainTest() throws Exception
	{
		this.policy.initialize(new Object());
		this.policy.initialize(new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeNullFlowOwnerTest() throws Exception
	{
		this.policy.initialize(null);
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
		Provider output=this.policy.getProvider();
		Assert.assertEquals(Fax4JProvider.FAX4J_PROVIDER,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFlowOwnerTest() throws Exception
	{
		Object output=this.policy.getFlowOwner();
		Assert.assertNull(output);
		Object owner=new Object();
		this.policy.initialize(owner);
		output=this.policy.getFlowOwner();
		Assert.assertNotNull(output);
		Assert.assertEquals(owner,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void invokePolicyForRequestNullRequestDataHolderTest() throws Exception
	{
		this.policy.invokePolicyForRequest(null);
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
	@Test(expected=FaxException.class)
	public void invokePolicyForResponseNullRequestDataHolderTest() throws Exception
	{
		this.policy.invokePolicyForResponse(null,new FaxJobImpl());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void invokePolicyForResponseNullFaxJobTest() throws Exception
	{
		this.policy.invokePolicyForResponse(new Object(),null);
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