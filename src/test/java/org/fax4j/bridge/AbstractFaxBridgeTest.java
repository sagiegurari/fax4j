package org.fax4j.bridge;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import org.fax4j.FaxClient;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxMonitorEventListener;
import org.fax4j.Provider;
import org.fax4j.spi.AbstractFaxClientSpi;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.FaxJobImpl;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestEnum;
import org.fax4j.test.TestUtil.TestVendorPolicy2;
import org.fax4j.util.IOHelper;
import org.fax4j.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AbstractFaxBridgeTest
{
	/**The fax bridge to test*/
	private AbstractFaxBridge faxBridge;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.faxBridge=new AbstractFaxBridge()
		{
			public Provider getProvider()
			{
				return null;
			}
			@Override
			protected void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo)
			{
				try
				{
					File file=File.createTempFile("temp_",".txt");
					file.deleteOnExit();
					IOHelper.writeTextFile("My Text Data",file);

					faxJob.setFile(file);
				}
				catch(IOException exception)
				{
					throw new RuntimeException(exception);
				}
			}
			@Override
			protected VendorPolicy createVendorPolicy()
			{
				return new EmptyVendorPolicy();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		for(int index=0;index<10;index++)
		{
			configuration.setProperty("KEY"+index,"value"+index);
		}
		for(int index=0;index<10;index++)
		{
			configuration.setProperty("EMPTY"+index,"");
		}
		this.faxBridge.initialize("test",configuration,new Object());
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
		this.faxBridge.initialize("test",null,new Object());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void initializeWithListenerVendorPolicy() throws Exception
	{
		this.faxBridge=new AbstractFaxBridge()
		{
			public Provider getProvider()
			{
				return null;
			}
			@Override
			protected void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo)
			{
				try
				{
					File file=File.createTempFile("temp_",".txt");
					file.deleteOnExit();
					IOHelper.writeTextFile("My Text Data",file);

					faxJob.setFile(file);
				}
				catch(IOException exception)
				{
					throw new RuntimeException(exception);
				}
			}
			@Override
			protected VendorPolicy createVendorPolicy()
			{
				return new TestVendorPolicy2();
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.type.map.test",EmptyFaxClientSpi.class.getName());
		for(int index=0;index<10;index++)
		{
			configuration.setProperty("KEY"+index,"value"+index);
		}
		for(int index=0;index<10;index++)
		{
			configuration.setProperty("EMPTY"+index,"");
		}
		this.faxBridge.initialize("test",configuration,new Object());
		FaxClient faxClient=this.faxBridge.getFaxClient();
		FaxClientSpi faxClientSpi=(FaxClientSpi)ReflectionHelper.invokeMethod(FaxClient.class,faxClient,"getFaxClientSpi",null,null);
		@SuppressWarnings("unchecked")
		Set<FaxMonitorEventListener> listeners=(Set<FaxMonitorEventListener>)ReflectionHelper.getField(AbstractFaxClientSpi.class,"faxMonitorEventListeners").get(faxClientSpi);
		Assert.assertEquals(1,listeners.size());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void initializeNullVendorPolicyTest() throws Exception
	{
		this.faxBridge=new AbstractFaxBridge()
		{
			public Provider getProvider()
			{
				return null;
			}
			@Override
			protected void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo)
			{
				//empty
			}
			@Override
			protected VendorPolicy createVendorPolicy()
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
	@Test
	public void getVendorPolicyTest() throws Exception
	{
		VendorPolicy output=this.faxBridge.getVendorPolicy();
		Assert.assertNotNull(output);
		Assert.assertEquals(EmptyVendorPolicy.class,output.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxJobValidTest() throws Exception
	{
		FaxJob faxJob=this.faxBridge.createFaxJob();
		Assert.assertNotNull(faxJob);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void createFaxJobNotInitializedTest() throws Exception
	{
		AbstractFaxBridge bridge=new AbstractFaxBridge()
		{
			public Provider getProvider()
			{
				return null;
			}
			@Override
			protected void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo)
			{
				//empty
			}
			@Override
			protected VendorPolicy createVendorPolicy()
			{
				return new EmptyVendorPolicy();
			}
		};
		bridge.createFaxJob();
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
		FaxJob faxJob=this.faxBridge.createFaxJob();
		faxJob.setTargetAddress("12345");
		FileInfo fileInfo=new FileInfo("abc.txt",new byte[10]);
		this.faxBridge.submitFaxJob(faxJob,fileInfo);
		
		faxJob.getFile().delete();
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNoTargetAddressTest() throws Exception
	{
		FaxJob faxJob=this.faxBridge.createFaxJob();
		FileInfo fileInfo=new FileInfo("abc.txt",new byte[10]);
		this.faxBridge.submitFaxJob(faxJob,fileInfo);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void submitFaxJobNotInitializedTest() throws Exception
	{
		AbstractFaxBridge bridge=new AbstractFaxBridge()
		{
			public Provider getProvider()
			{
				return null;
			}
			@Override
			protected void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo)
			{
				//empty
			}
			@Override
			protected VendorPolicy createVendorPolicy()
			{
				return new EmptyVendorPolicy();
			}
		};
		FaxJob faxJob=new FaxJobImpl();
		FileInfo fileInfo=new FileInfo("abc.txt",new byte[10]);
		bridge.submitFaxJob(faxJob,fileInfo);
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
		FileInfo fileInfo=new FileInfo("abc.txt",new byte[10]);
		this.faxBridge.submitFaxJob(null,fileInfo);
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
		FaxJob faxJob=this.faxBridge.createFaxJob();
		this.faxBridge.submitFaxJob(faxJob,null);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getFaxClientTest() throws Exception
	{
		Assert.assertNotNull(this.faxBridge.getFaxClient());
		Assert.assertEquals(FaxClient.class,this.faxBridge.getFaxClient().getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueEnumFoundTest() throws Exception
	{
		String output=this.faxBridge.getConfigurationValue(TestEnum.KEY1);
		Assert.assertNotNull(output);
		Assert.assertEquals("value1",output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void getConfigurationValueEnumNotFoundTest() throws Exception
	{
		String output=this.faxBridge.getConfigurationValue(TestEnum.KEY_TEST);
		Assert.assertNull(output);
	}
}