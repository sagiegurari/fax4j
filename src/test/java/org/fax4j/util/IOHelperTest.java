package org.fax4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import org.fax4j.test.TestUtil.TestCloseable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class IOHelperTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getDefaultEncodingTest() throws Exception
	{
		String output=IOHelper.getDefaultEncoding();
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
	public void getFax4jInternalTemporaryDirectoryTest() throws Exception
	{
		File output=IOHelper.getFax4jInternalTemporaryDirectory();
		Assert.assertNotNull(output);
		Assert.assertTrue(output.exists());
		Assert.assertTrue(output.isDirectory());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertStringToBinaryDefaultEncodingTest() throws Exception
	{
		String text="abc";
		byte[] output=IOHelper.convertStringToBinary(text,IOHelper.getDefaultEncoding());
		Assert.assertNotNull(output);
		Assert.assertArrayEquals("abc".getBytes(IOHelper.getDefaultEncoding()),output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertStringToBinaryNullEncodingTest() throws Exception
	{
		String text="abc";
		byte[] output=IOHelper.convertStringToBinary(text,null);
		Assert.assertNotNull(output);
		Assert.assertArrayEquals("abc".getBytes(IOHelper.getDefaultEncoding()),output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertStringToBinaryBadEncodingTest() throws Exception
	{
		String text="abc";
		byte[] output=IOHelper.convertStringToBinary(text,"THIS_ENCODING_DOES_NOT_EXISTS");
		Assert.assertNotNull(output);
		Assert.assertArrayEquals("abc".getBytes(IOHelper.getDefaultEncoding()),output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertStringToBinaryValidEncodingTest() throws Exception
	{
		String text="abc";
		byte[] output=IOHelper.convertStringToBinary(text,"UTF-8");
		Assert.assertNotNull(output);
		Assert.assertArrayEquals("abc".getBytes("UTF-8"),output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertStringToBinaryNullTextTest() throws Exception
	{
		byte[] output=IOHelper.convertStringToBinary(null,null);
		Assert.assertNull(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void convertStringToBinaryEmptyTextTest() throws Exception
	{
		byte[] output=IOHelper.convertStringToBinary("",null);
		Assert.assertNotNull(output);
		Assert.assertArrayEquals(new byte[0],output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void closeResourceNullValueTest() throws Exception
	{
		IOHelper.closeResource(null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void closeResourceTest() throws Exception
	{
		TestCloseable closeable=new TestCloseable();
		Assert.assertFalse(closeable.isClosed());
		IOHelper.closeResource(closeable);
		Assert.assertTrue(closeable.isClosed());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createReaderTest() throws Exception
	{
		ByteArrayInputStream inputStream=new ByteArrayInputStream(new byte[0]);
		Reader reader=IOHelper.createReader(inputStream,null);
		Assert.assertNotNull(reader);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createWriterTest() throws Exception
	{
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		Writer writer=IOHelper.createWriter(outputStream,null);
		Assert.assertNotNull(writer);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void readTextStreamTest() throws Exception
	{
		String string="My Text Data";
		StringReader reader=new StringReader(string);
		String output=IOHelper.readTextStream(reader);
		Assert.assertEquals(string,output);
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void readAndWriteTextFileTest() throws Exception
	{
		String string="My Text Data";
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile(string,file);
		String output=IOHelper.readTextFile(file);
		file.delete();
		Assert.assertEquals(string,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void readAndWriteFileTest() throws Exception
	{
		String string="My Text Data";
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		byte[] input=string.getBytes(IOHelper.getDefaultEncoding());
		IOHelper.writeFile(input,file);
		byte[] output=IOHelper.readFile(file);
		file.delete();
		Assert.assertArrayEquals(input,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void readAndWriteStreamsTest() throws Exception
	{
		String string="My Text Data";
		byte[] input=string.getBytes(IOHelper.getDefaultEncoding());
		ByteArrayInputStream inputStream=new ByteArrayInputStream(input);
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		IOHelper.readAndWriteStreams(inputStream,outputStream);
		Assert.assertArrayEquals(input,outputStream.toByteArray());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void readStreamTest() throws Exception
	{
		String string="My Text Data";
		byte[] input=string.getBytes(IOHelper.getDefaultEncoding());
		byte[] output=IOHelper.readStream(new ByteArrayInputStream(input));
		Assert.assertArrayEquals(input,output);
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileFromPathListTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		String fileNameWithNoPath=file.getName();
		String directory=file.getParent();
		String[] pathList=new String[]{".","..",directory,"."};
		File output=IOHelper.getFileFromPathList(fileNameWithNoPath,pathList);
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
	public void getFileFromNativePathTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		String fileNameWithNoPath=file.getName();
		String directory=file.getParent();
		String systemPath=System.getProperty("java.library.path");
		String pathSeperator=System.getProperty("path.separator");
		systemPath=systemPath+pathSeperator+directory;
		System.setProperty("java.library.path",systemPath);
		File output=IOHelper.getFileFromNativePath(fileNameWithNoPath);
		Assert.assertNotNull(output);
		Assert.assertEquals(file,output);
	}
}