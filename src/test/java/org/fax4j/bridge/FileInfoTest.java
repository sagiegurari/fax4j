package org.fax4j.bridge;

import java.io.File;
import java.util.Random;
import org.fax4j.FaxException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FileInfoTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void nullFileTest() throws Exception
	{
		FileInfo fileInfo=new FileInfo(null);
		Assert.fail("Event should throw error: "+fileInfo);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void nullNameTest() throws Exception
	{
		FileInfo fileInfo=new FileInfo(null,new byte[10]);
		Assert.fail("Event should throw error: "+fileInfo);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void emptyNameTest() throws Exception
	{
		FileInfo fileInfo=new FileInfo("",new byte[10]);
		Assert.fail("Event should throw error: "+fileInfo);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void emptyWithSpacesNameTest() throws Exception
	{
		FileInfo fileInfo=new FileInfo("   ",new byte[10]);
		Assert.fail("Event should throw error: "+fileInfo);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void nullContentTest() throws Exception
	{
		FileInfo fileInfo=new FileInfo("abc",null);
		Assert.fail("Event should throw error: "+fileInfo);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void emptpyContentTest() throws Exception
	{
		FileInfo fileInfo=new FileInfo("abc",new byte[0]);
		Assert.fail("Event should throw error: "+fileInfo);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileTest() throws Exception
	{
		File file=new File("./abc.txt");
		FileInfo fileInfo=new FileInfo(file);
		File output=fileInfo.getFile();
		Assert.assertNotNull(output);
		Assert.assertEquals(file,output);
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
		FileInfo fileInfo=new FileInfo("name",new byte[10]);
		String output=fileInfo.getName();
		Assert.assertNotNull(output);
		Assert.assertEquals("name",output);

		File file=new File("./abc.txt");
		fileInfo=new FileInfo(file);
		output=fileInfo.getName();
		Assert.assertNotNull(output);
		Assert.assertEquals("abc.txt",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getContentTest() throws Exception
	{
		Random random=new Random();
		byte[] content=new byte[50];
		random.nextBytes(content);
		FileInfo fileInfo=new FileInfo("name",content);
		byte[] output=fileInfo.getContent();
		Assert.assertNotNull(output);
		Assert.assertArrayEquals(content,output);
	}
}