package org.fax4j.common;

import org.fax4j.FaxException;
import org.fax4j.Provider;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ProviderImplTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void providerImplTest() throws Exception
	{
		String name="abc";
		String description="123";
		ProviderImpl provider=new ProviderImpl(name,description);
		Assert.assertEquals(name,provider.getName());
		Assert.assertEquals(description,provider.getDescription());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void providerImplWithEmptyNameTest() throws Exception
	{
		Provider provider=new ProviderImpl(null,"123");
		Assert.assertEquals("123",provider.getDescription());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void providerImplWithEmptyDescriptionTest() throws Exception
	{
		String name="abc";
		ProviderImpl provider=new ProviderImpl(name,null);
		Assert.assertEquals(name,provider.getName());
		Assert.assertEquals("",provider.getDescription());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void equalsValidTest() throws Exception
	{
		Provider provider1=new ProviderImpl("abc","123");
		Provider provider2=new ProviderImpl("a"+"bc","1"+"23");
		Assert.assertTrue(provider1.equals(provider2));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void equalsDiffNameTest() throws Exception
	{
		Provider provider1=new ProviderImpl("abc","123");
		Provider provider2=new ProviderImpl("abcd","1"+"23");
		Assert.assertFalse(provider1.equals(provider2));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void equalsDiffDescriptionTest() throws Exception
	{
		Provider provider1=new ProviderImpl("abc","123");
		Provider provider2=new ProviderImpl("abc","1234");
		Assert.assertFalse(provider1.equals(provider2));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getNameTest() throws Exception
	{
		ProviderImpl provider=new ProviderImpl("name",null);
		Assert.assertEquals("name",provider.getName());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getDescriptionTest() throws Exception
	{
		ProviderImpl provider=new ProviderImpl("name","desc");
		Assert.assertEquals("desc",provider.getDescription());
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
		ProviderImpl provider=new ProviderImpl("name","desc");
		String output=provider.toString();
		Assert.assertNotNull(output);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(output.length()));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void hashCodeTest() throws Exception
	{
		ProviderImpl provider1=new ProviderImpl("name","desc");
		ProviderImpl provider2=new ProviderImpl("name","desc");
		Assert.assertEquals(provider1.hashCode(),provider2.hashCode());
	}
}