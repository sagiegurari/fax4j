package org.fax4j.bridge;

import java.util.HashMap;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.FaxJobImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractRequestParserTest
{
	/**The parser to test*/
	private AbstractRequestParser<String> parser;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.parser=new AbstractRequestParser<String>()
		{
			@Override
			protected void initializeImpl(Map<String,String> configuration)
			{
				//empty
			}
			@Override
			protected FileInfo getFileInfoFromInputDataImpl(String inputData)
			{
				return null;
			}			
			@Override
			protected void updateFaxJobFromInputDataImpl(String inputData,FaxJob faxJob)
			{
				//empty
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
		this.parser.initialize(new HashMap<String,String>());
		this.parser.initialize(new HashMap<String,String>());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void getFileInfoFromInputDataNotInitializedTest() throws Exception
	{
		this.parser.getFileInfoFromInputData("abc");
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
		this.parser.initialize(new HashMap<String,String>());
		this.parser.getFileInfoFromInputData("abc");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void updateFaxJobFromInputDataNotInitializedTest() throws Exception
	{
		this.parser.updateFaxJobFromInputData("NullFaxJobTest",new FaxJobImpl());
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
		this.parser.initialize(new HashMap<String,String>());
		this.parser.updateFaxJobFromInputData("NullFaxJobTest",new FaxJobImpl());
	}
}