package org.fax4j.bridge;

import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestRequestParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractContextFaxBridgeTest
{
	/**The fax bridge to test*/
	private AbstractContextFaxBridge<String> faxBridge;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.faxBridge=new AbstractContextFaxBridge<String>()
		{
			@Override
			protected String getRequestParserConfigurationKey()
			{
				return "test.parser.class.name";
			}

			@Override
			protected String getDefaultParserClassName()
			{
				return TestRequestParser.class.getName();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("test.parser.class.name",TestRequestParser.class.getName());
		this.faxBridge.initialize("test",configuration,new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobValidTest() throws Exception
	{
		FaxJob faxJob=this.faxBridge.submitFaxJob("");
		Assert.assertNotNull(faxJob);
		Assert.assertNotNull(faxJob.getFile());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullRequestTest() throws Exception
	{
		this.faxBridge.submitFaxJob(null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullFileInfoTest() throws Exception
	{
		this.faxBridge.submitFaxJob("NullFileInfoTest");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNullFaxJobTest() throws Exception
	{
		this.faxBridge.submitFaxJob("NullFaxJobTest");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeEmptyParserClassNameTest() throws Exception
	{
		this.faxBridge=new AbstractContextFaxBridge<String>()
		{
			@Override
			protected String getRequestParserConfigurationKey()
			{
				return "test.parser.class.name";
			}

			@Override
			protected String getDefaultParserClassName()
			{
				return TestRequestParser.class.getName();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("test.parser.class.name","");
		this.faxBridge.initialize("test",configuration,new Object());
		RequestParser<String> parser=this.faxBridge.createRequestParser();
		Assert.assertNotNull(parser);
		Assert.assertEquals(TestRequestParser.class,parser.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeNullParserClassNameTest() throws Exception
	{
		this.faxBridge=new AbstractContextFaxBridge<String>()
		{
			@Override
			protected String getRequestParserConfigurationKey()
			{
				return "test.parser.class.name";
			}

			@Override
			protected String getDefaultParserClassName()
			{
				return TestRequestParser.class.getName();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		this.faxBridge.initialize("test",configuration,new Object());
		RequestParser<String> parser=this.faxBridge.createRequestParser();
		Assert.assertNotNull(parser);
		Assert.assertEquals(TestRequestParser.class,parser.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeInvalidParserClassNameTest() throws Exception
	{
		this.faxBridge=new AbstractContextFaxBridge<String>()
		{
			@Override
			protected String getRequestParserConfigurationKey()
			{
				return "test.parser.class.name";
			}

			@Override
			protected String getDefaultParserClassName()
			{
				return TestRequestParser.class.getName();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("test.parser.class.name","abc");
		this.faxBridge.initialize("test",configuration,new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeNullParserTest() throws Exception
	{
		this.faxBridge=new AbstractContextFaxBridge<String>()
		{
			@Override
			protected String getRequestParserConfigurationKey()
			{
				return "test.parser.class.name";
			}

			@Override
			protected String getDefaultParserClassName()
			{
				return null;
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		this.faxBridge.initialize("test",configuration,new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeNullConfigurationKeyTest() throws Exception
	{
		this.faxBridge=new AbstractContextFaxBridge<String>()
		{
			@Override
			protected String getRequestParserConfigurationKey()
			{
				return null;
			}

			@Override
			protected String getDefaultParserClassName()
			{
				return TestRequestParser.class.getName();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		this.faxBridge.initialize("test",configuration,new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeCreateParserReturnsNullTest() throws Exception
	{
		this.faxBridge=new AbstractContextFaxBridge<String>()
		{
			@Override
			protected RequestParser<String> createRequestParser()
			{
				return null;
			}

			@Override
			protected String getRequestParserConfigurationKey()
			{
				return null;
			}

			@Override
			protected String getDefaultParserClassName()
			{
				return TestRequestParser.class.getName();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		this.faxBridge.initialize("test",configuration,new Object());
	}
}