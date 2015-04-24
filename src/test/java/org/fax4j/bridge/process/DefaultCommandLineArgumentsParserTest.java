package org.fax4j.bridge.process;

import java.io.File;
import java.util.HashMap;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.bridge.FileInfo;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class DefaultCommandLineArgumentsParserTest
{
	/**The parser to test*/
	private DefaultCommandLineArgumentsParser parser;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.parser=new DefaultCommandLineArgumentsParser();
		this.parser.initialize(new HashMap<String,String>());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFileInfoFromInputDataValidTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);

		FileInfo fileInfo=this.parser.getFileInfoFromInputData(new String[]{"-abc","abc","-file",file.getPath()});
		Assert.assertNotNull(fileInfo);
		Assert.assertTrue(fileInfo.getName().startsWith("temp_"));
		Assert.assertTrue(fileInfo.getName().endsWith(".txt"));
		Assert.assertEquals(file,fileInfo.getFile());
		
		file.delete();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataOddInputTest() throws Exception
	{
		this.parser.getFileInfoFromInputData(new String[]{"-abc","abc","123"});
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void updateFaxJobFromInputDataValidTest() throws Exception
	{
		FaxJob faxJob=new FaxJobImpl();
		this.parser.updateFaxJobFromInputData(new String[]{"-priority",FaxJobPriority.LOW_PRIORITY.name(),
														  "-target_address","12345",
														  "-target_name","tname",
														  "-sender_name","sname",
														  "-sender_fax_number","sfax",
														  "-sender_email","smail",
														  "-property:p1","v1",
														  "-property:p2","v2",
														  "-property:","v3"},faxJob);
		Assert.assertEquals(FaxJobPriority.LOW_PRIORITY,faxJob.getPriority());
		Assert.assertEquals("12345",faxJob.getTargetAddress());
		Assert.assertEquals("tname",faxJob.getTargetName());
		Assert.assertEquals("sname",faxJob.getSenderName());
		Assert.assertEquals("sfax",faxJob.getSenderFaxNumber());
		Assert.assertEquals("smail",faxJob.getSenderEmail());
		Assert.assertEquals("v1",faxJob.getProperty("p1","bad"));
		Assert.assertEquals("v2",faxJob.getProperty("p2","bad"));
		Assert.assertEquals("empty",faxJob.getProperty("","empty"));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void updateFaxJobFromInputDataUnsupportedArgumentTest() throws Exception
	{
		this.parser.updateFaxJobFromInputData(new String[]{"-abc","abc"},new FaxJobImpl());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void updateFaxJobFromInputDataOddInputTest() throws Exception
	{
		this.parser.updateFaxJobFromInputData(new String[]{"-target_address","abc","-sender_name"},new FaxJobImpl());
	}
}