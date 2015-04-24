package org.fax4j.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.fax4j.FaxJob;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.util.SpiUtil.TemplateParameterEncoder;
import org.fax4j.util.SpiUtil.URLTemplateParameterEncoder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class SpiUtilTest
{	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void copyPropertiesToMapTest() throws Exception
	{
		Properties source=new Properties();
		source.setProperty("p1","v1");
		source.setProperty("p2","v2");
		Map<String,String> target=new HashMap<String,String>();
		SpiUtil.copyPropertiesToMap(source,target);
		Assert.assertEquals(2,target.size());
		Assert.assertEquals("v1",target.get("p1"));
		Assert.assertEquals("v2",target.get("p2"));
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatTemplateNullEncoderTest() throws Exception
	{
		String template="1:${file}\n2:${target.address}\n3:${target.name}\n4:${sender.name}\n5:${sender.fax.number}\n6:${sender.email}\n";
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath("TEST_FILE_NAME");
		faxJob.setTargetAddress("TEST_TARGET_ADDRESS");
		faxJob.setTargetName("TEST_TARGET_NAME");
		faxJob.setSenderName("TEST_SENDER_NAME");
		faxJob.setSenderFaxNumber("TEST_SENDER_FAX_NUMBER");
		faxJob.setSenderEmail("TEST_SENDER_EMAIL");
		TemplateParameterEncoder encoder=null;
		String output=SpiUtil.formatTemplate(template,faxJob,encoder,false,true);
		Assert.assertNotNull(output);
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("1:TEST_FILE_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("2:TEST_TARGET_ADDRESS\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("3:TEST_TARGET_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("4:TEST_SENDER_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("5:TEST_SENDER_FAX_NUMBER\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("6:TEST_SENDER_EMAIL\n")));
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatTemplateURLEncoderTest() throws Exception
	{
		String template="1:${file}\n2:${target.address}\n3:${target.name}\n4:${sender.name}\n5:${sender.fax.number}\n6:${sender.email}\n";
		FaxJob faxJob=new FaxJobImpl();
		faxJob.setFilePath("TEST FILE_NAME");
		faxJob.setTargetAddress("TEST TARGET_ADDRESS");
		faxJob.setTargetName("TEST TARGET_NAME");
		faxJob.setSenderName("TEST SENDER_NAME");
		faxJob.setSenderFaxNumber("TEST SENDER_FAX_NUMBER");
		faxJob.setSenderEmail("TEST SENDER_EMAIL");
		TemplateParameterEncoder encoder=new URLTemplateParameterEncoder();
		String output=SpiUtil.formatTemplate(template,faxJob,encoder,false,true);
		Assert.assertNotNull(output);
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("1:TEST%20FILE_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("2:TEST%20TARGET_ADDRESS\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("3:TEST%20TARGET_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("4:TEST%20SENDER_NAME\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("5:TEST%20SENDER_FAX_NUMBER\n")));
		Assert.assertNotSame(String.valueOf(-1),String.valueOf(output.indexOf("6:TEST%20SENDER_EMAIL\n")));
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatTemplateFullFileNameTest() throws Exception
	{
		String template="${file}";
		FaxJob faxJob=new FaxJobImpl();
		String path="."+File.pathSeparator+"Dir1"+File.pathSeparator+"Dir2"+File.pathSeparator+"TEST_FILE_NAME";
		faxJob.setFilePath(path);
		TemplateParameterEncoder encoder=null;
		String output=SpiUtil.formatTemplate(template,faxJob,encoder,false,true);
		Assert.assertNotNull(output);
		Assert.assertEquals(path,output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void formatTemplateFileNameWithoutPathTest() throws Exception
	{
		String template="${file}";
		FaxJob faxJob=new FaxJobImpl();
		String path="./Dir1/Dir2/TEST_FILE_NAME";
		faxJob.setFilePath(path);
		TemplateParameterEncoder encoder=null;
		String output=SpiUtil.formatTemplate(template,faxJob,encoder,false,false);
		Assert.assertNotNull(output);
		Assert.assertEquals("TEST_FILE_NAME",output);
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void replaceTemplateParameterNullEncoderTest() throws Exception
	{
		String template="1:${file}:2";
		String parameter="${file}";
		String value="my_value_test";
		TemplateParameterEncoder encoder=null;
		String output=SpiUtil.replaceTemplateParameter(template,parameter,value,encoder);
		Assert.assertNotNull(output);
		Assert.assertEquals("1:"+value+":2",output);
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void replaceTemplateParameterURLEncoderTest() throws Exception
	{
		String template="1:${file}:2";
		String parameter="${file}";
		String value="my value test";
		TemplateParameterEncoder encoder=new URLTemplateParameterEncoder();
		String output=SpiUtil.replaceTemplateParameter(template,parameter,value,encoder);
		Assert.assertNotNull(output);
		String encodedValue=value.replaceAll(" ","+");
		Assert.assertEquals("1:"+encodedValue+":2",output);
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void urlEncodeTest() throws Exception
	{
		String value="my value test";
		String output=SpiUtil.urlEncode(value);
		Assert.assertNotNull(output);
		String encodedValue=value.replaceAll(" ","+");
		Assert.assertEquals(encodedValue,output);
	}
	
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void urlDecodeTest() throws Exception
	{
		String value="my value test";
		String output=SpiUtil.urlEncode(value);
		Assert.assertNotNull(output);
		String encodedValue=value.replaceAll(" ","+");
		Assert.assertEquals(encodedValue,output);
		output=SpiUtil.urlDecode(output);
		Assert.assertNotNull(output);
		Assert.assertEquals(value,output);
	}
}