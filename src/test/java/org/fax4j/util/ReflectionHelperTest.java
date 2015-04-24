package org.fax4j.util;

import java.lang.reflect.Field;
import org.fax4j.FaxException;
import org.fax4j.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ReflectionHelperTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getThreadContextClassLoaderTest() throws Exception
	{
		ClassLoader classLoader=ReflectionHelper.getThreadContextClassLoader();
		Assert.assertNotNull(classLoader);
		Assert.assertEquals(Thread.currentThread().getContextClassLoader(),classLoader);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getTypeValidTest() throws Exception
	{
		String className=String.class.getName();
		Class<?> type=ReflectionHelper.getType(className);
		Assert.assertEquals(String.class,type);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getTypeInvalidTest() throws Exception
	{
		ReflectionHelper.getType("abc");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createInstanceViaTypeValidTest() throws Exception
	{
		Object instance=ReflectionHelper.createInstance(String.class);
		Assert.assertEquals("",instance);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createInstanceViaTypeInvalidTest() throws Exception
	{
		ReflectionHelper.createInstance(System.class);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createInstanceViaClassNameValidTest() throws Exception
	{
		Object instance=ReflectionHelper.createInstance(String.class.getName());
		Assert.assertEquals("",instance);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createInstanceViaClassNameInvalidTest() throws Exception
	{
		ReflectionHelper.createInstance("abc");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void invokeMethodValidTest() throws Exception
	{
		Object output=ReflectionHelper.invokeMethod(String.class,"123","substring",new Class[]{int.class,int.class},new Object[]{Integer.valueOf(1),Integer.valueOf(2)});
		Assert.assertEquals("2",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void invokeMethodNoSuchMethodTest() throws Exception
	{
		ReflectionHelper.invokeMethod(String.class,"123","not_found",new Class[]{int.class,int.class},new Object[]{Integer.valueOf(1),Integer.valueOf(2)});
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void invokeMethodErrorInMethodTest() throws Exception
	{
		ReflectionHelper.invokeMethod(String.class,null,"substring",new Class[]{int.class,int.class},new Object[]{Integer.valueOf(1),Integer.valueOf(2)});
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFieldValidTest() throws Exception
	{
		Field field=ReflectionHelper.getField(TestUtil.PrivateObject.class,"counter");
		Assert.assertNotNull(field);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFieldInvalidTest() throws Exception
	{
		ReflectionHelper.getField(String.class,"not_found");
	}
}