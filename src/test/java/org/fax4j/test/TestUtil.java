package org.fax4j.test;

import gnu.hylafax.HylaFAXClient;
import gnu.hylafax.Job;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.mail.Session;
import javax.mail.Transport;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.PostMethod;
import org.fax4j.FaxClient;
import org.fax4j.FaxClientActionEvent;
import org.fax4j.FaxClientActionEvent.FaxClientActionEventID;
import org.fax4j.FaxClientActionEventListener;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.FaxMonitorEvent;
import org.fax4j.FaxMonitorEventListener;
import org.fax4j.bridge.EmptyVendorPolicy;
import org.fax4j.bridge.FileInfo;
import org.fax4j.bridge.RequestParser;
import org.fax4j.bridge.http.HTTPRequestParser;
import org.fax4j.common.LogLevel;
import org.fax4j.common.Logger;
import org.fax4j.common.SimpleLogger;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.FaxClientSpiFactory;
import org.fax4j.spi.FaxClientSpiInterceptor;
import org.fax4j.spi.FaxClientSpiProxyImpl;
import org.fax4j.spi.FaxJobMonitorImpl;
import org.fax4j.spi.comm.AbstractCommPortAdapter;
import org.fax4j.spi.comm.AbstractCommPortConnectionFactory;
import org.fax4j.spi.comm.AbstractFax4JFaxModemAdapter;
import org.fax4j.spi.comm.CommPortAdapter;
import org.fax4j.spi.email.MailConnectionFactoryImpl;
import org.fax4j.spi.email.MailFaxClientSpi;
import org.fax4j.spi.http.ApacheHTTPClient;
import org.fax4j.spi.http.HTTPRequest;
import org.fax4j.spi.hylafax.HylaFAXClientConnectionFactoryImpl;
import org.fax4j.util.AbstractFax4JConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;
import org.mockito.Mockito;

/**
 * Helper for JUnit test cases.
 * 
 * @author 	Sagie Gur-Ari
 */
public final class TestUtil
{
	/**Port lock*/
	public static final Object PORT_LOCK=new Object();
	
	/**
	 * This is the class constructor.
	 */
	private TestUtil()
	{
		super();
	}

	/**
	 * Test method. 
	 * 
	 * @param 	className
	 * 			The SPI class name
	 * @param 	configuration
	 * 			The configuration
	 * @return	The configuration
	 */
	public static Properties createFaxClientSpiConfiguration(String className,Properties configuration)
	{
		Properties updatedConfiguration=configuration;
		if(updatedConfiguration==null)
		{
			updatedConfiguration=new Properties();
		}
		updatedConfiguration.setProperty("org.fax4j.proxy.enabled","false");
		updatedConfiguration.setProperty("org.fax4j.spi.type.map.test",className);
		updatedConfiguration.setProperty(FaxClientSpiFactory.DEFAULT_SPI_TYPE_PROPERTY_KEY,"test");
		
		return updatedConfiguration;
	}
	
	/**
	 * Test method. 
	 * 
	 * @param 	configuration
	 * 			The configuration
	 * @return	The configuration
	 */
	public static Properties createEmptyFaxClientSpiConfiguration(Properties configuration)
	{
		return TestUtil.createFaxClientSpiConfiguration(EmptyFaxClientSpi.class.getName(),configuration);
	}
	
	/**
	 * Helper method 
	 * 
	 * @param 	className
	 * 			The SPI class name
	 * @param 	configuration
	 * 			The configuration
	 * @return	The SPI instance
	 */
	public static FaxClientSpi createFaxClientSpi(String className,Properties configuration)
	{
		Properties updatedConfiguration=TestUtil.createFaxClientSpiConfiguration(className,configuration);
		
		FaxClientSpi faxClientSpi=FaxClientSpiFactory.createFaxClientSpi("test",updatedConfiguration);
		Assert.assertNotNull(faxClientSpi);
		Assert.assertEquals(className,faxClientSpi.getClass().getName());
		
		return faxClientSpi;
	}
	
	/**
	 * Returns true if windows OS. 
	 * 
	 * @return	True if windows OS
	 */
	public static boolean isWindowsOS()
	{
		boolean windows=false;
		if(Boolean.parseBoolean(System.getenv("FAX4J_JUNIT_WINDOWS")))
		{
			windows=true;
		}
		else
		{
			String osName=System.getProperty("os.name");
			if(osName.toLowerCase().indexOf("windows")!=-1)
			{
				windows=true;
			}
		}

		return windows;
	}
	
	/**
	 * Returns a free port
	 * 
	 * @return	The free port
	 * @throws	Exception
	 * 			Any exception
	 */
	protected static int getFreePortNoSych() throws Exception
	{
		ServerSocket socket=new ServerSocket(0);
		int port=socket.getLocalPort();
		socket.close();

		return port;
	}

	/**
	 * Test enum. 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public enum TestEnum
	{
		/**Test value*/
		KEY1,
		/**Test value*/
		KEY_TEST
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class FaxClient2 extends FaxClient
	{
		/**
		 * This is the default constructor.
		 * 
		 * @param	faxClientSpi
		 * 			The fax client SPI
		 */
		public FaxClient2(FaxClientSpi faxClientSpi)
		{
			super(faxClientSpi);
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class EmptyFaxClientSpi extends AbstractFax4JClientSpi
	{
		/**
		 * This is the class constructor.
		 */
		public EmptyFaxClientSpi()
		{
			super();
		}

		/**
		 * This is the class constructor.
		 * 
		 * @param	init
		 * 			True to run the init
		 */
		public EmptyFaxClientSpi(boolean init)
		{
			this();
			if(init)
			{
				Logger logger=new SimpleLogger();
				logger.setLogLevel(LogLevel.NONE);
				this.initialize(new HashMap<String,String>(),logger,new FaxJobMonitorImpl());
			}
		}

		/**
		 * Returns the configuration.
		 *  
		 * @return	The configuration
		 */
		public Map<String,String> getAllConfiguration()
		{
			return this.getConfiguration();
		}

		/**
		 * Empty method.
		 */
		@Override
		protected void initializeImpl()
		{
			//empty
		}

		/**
		 * This function returns true if the fax monitor events are supported by this SPI.
		 * 
		 * @return	True if the fax monitor events are supported by this SPI
		 */
		@Override
		public boolean isFaxMonitorEventsSupported()
		{
			return true;
		}
		
		/**
		 * This function will submit a new fax job.<br>
		 * The fax job ID may be populated by this method in the provided
		 * fax job object.
		 * 
		 * @param 	faxJob
		 * 			The fax job object containing the needed information
		 */
		@Override
		protected void submitFaxJobImpl(FaxJob faxJob)
		{
			faxJob.setID(String.valueOf((new Random()).nextInt()));
		}
		
		/**
		 * This function will suspend an existing fax job.
		 * 
		 * @param 	faxJob
		 * 			The fax job object containing the needed information
		 */
		@Override
		protected void suspendFaxJobImpl(FaxJob faxJob)
		{
			//empty
		}
		
		/**
		 * This function will resume an existing fax job.
		 * 
		 * @param 	faxJob
		 * 			The fax job object containing the needed information
		 */
		@Override
		protected void resumeFaxJobImpl(FaxJob faxJob)
		{
			//empty
		}
		
		/**
		 * This function will cancel an existing fax job.
		 * 
		 * @param 	faxJob
		 * 			The fax job object containing the needed information
		 */
		@Override
		protected void cancelFaxJobImpl(FaxJob faxJob)
		{
			//empty
		}
		
		/**
		 * This function returns the fax job status.<br>
		 * Not all SPIs support extraction of the fax job status.<br>
		 * In case the SPI is unable to extract or does not support extracting
		 * of the fax job status, it will return the UNKNOWN status.
		 * 
		 * @param 	faxJob
		 * 			The fax job object containing the needed information
		 * @return	The fax job status
		 */
		@Override
		protected FaxJobStatus getFaxJobStatusImpl(FaxJob faxJob)
		{
			String status=faxJob.getProperty("test.status",null);
			FaxJobStatus jobStatus=null;
			if(status==null)
			{
				jobStatus=FaxJobStatus.PENDING;
			}
			else
			{
				FaxJobStatus lastStatus=FaxJobStatus.valueOf(status);
				switch(lastStatus)
				{
					case PENDING:
						jobStatus=FaxJobStatus.IN_PROGRESS;
						break;
					default:
						jobStatus=FaxJobStatus.UNKNOWN;
						break;
				}
			}
			status=jobStatus.name();
			faxJob.setProperty("test.status", status);

			return jobStatus;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static abstract class PredefinedMailFaxClientSpiTest
	{
		/**class member*/
		private String spiClassName;
		/**class member*/
		private String mailAddressTemplate;
		/**class member*/
		private String mailSubjectTemplate;
		/**The fax client SPI*/
		protected MailFaxClientSpi faxClientSpi;
		/**The SPI configuration*/
		protected Properties configuration;

		/**
		 * Sets up the SPI instance.
		 * 
		 * @throws 	Exception
		 * 			Any exception
		 */
		@Before
	    public void setUp() throws Exception
		{
			this.configuration=new Properties();
			this.configuration.setProperty("org.fax4j.spi.mail.persistent.connection","true");
			this.configuration.setProperty("org.fax4j.spi.mail.connection.factory.class.name",TestMailConnectionFactory.class.getName());
			this.configuration.setProperty("org.fax4j.spi.mail.address.template","{0}@myprovider.com");
			this.configuration.setProperty("org.fax4j.spi.mail.subject.template","fax:{0}");
			this.configuration.setProperty("org.fax4j.spi.mail.user.name","test@test.com");
			this.configuration.setProperty("org.fax4j.spi.mail.password","test_password");
			this.configuration.setProperty("mail.transport.protocol","smtp");	
			this.configuration.setProperty("mail.smtp.host","localhost");
			this.configuration.setProperty("mail.smtp.port","123");
			this.faxClientSpi=(MailFaxClientSpi)TestUtil.createFaxClientSpi(this.spiClassName,this.configuration);
	    }

		/**
		 * Releases the test objects.
		 * 
		 * @throws 	Exception
		 * 			Any exception
		 */
		@After
		public void tearDown() throws Exception
		{
			Mailbox.clearAll();
		}

		/**
		 * This is the class constructor.
		 * 
		 * @param	spiClassName
		 * 			The SPI class name
		 * @param 	mailAddressTemplate
		 * 			Value to test
		 * @param 	mailSubjectTemplate
		 * 			Value to test
		 */
		protected PredefinedMailFaxClientSpiTest(String spiClassName,String mailAddressTemplate,String mailSubjectTemplate)
		{
			super();
			
			this.spiClassName=spiClassName;
			this.mailAddressTemplate=mailAddressTemplate;
			this.mailSubjectTemplate=mailSubjectTemplate;
		}

		/**
		 * Test 
		 * 
		 * @throws 	Exception
		 * 			Any exception
		 */
		@Test
		public void mailAddressTemplateTest() throws Exception
		{
			Assert.assertEquals(this.mailAddressTemplate,this.faxClientSpi.getMailAddressTemplate());
		}

		/**
		 * Test 
		 * 
		 * @throws 	Exception
		 * 			Any exception
		 */
		@Test
		public void mailSubjectTemplateTest() throws Exception
		{
			Assert.assertEquals(this.mailSubjectTemplate, this.faxClientSpi.getMailSubjectTemplate());
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class FaxClientSpiProxyImpl2 extends FaxClientSpiProxyImpl
	{
		/**
		 * This is the class constructor.
		 */
		public FaxClientSpiProxyImpl2()
		{
			super();
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestFaxClientSpiInterceptor implements FaxClientSpiInterceptor
	{
		/**Class member*/
		public int preCount=0;
		/**Class member*/
		public int postCount=0;
		/**Class member*/
		public int onErrorCount=0;

		/**
		 * This function initializes the fax client SPI interceptor.<br>
		 * This method is called by the FaxClientSpiFactory.
		 * 
		 * @param	faxClientSpi
		 * 			The fax client SPI
		 */
		public void initialize(FaxClientSpi faxClientSpi)
		{
			//do nothing
		}

		/**
		 * This function is invoked by the fax client SPI proxy before invoking
		 * the method in the fax client SPI itself.
		 * 
		 * @param 	method
		 * 			The method invoked
		 * @param 	arguments
		 * 			The method arguments
		 */
		public void preMethodInvocation(Method method,Object[] arguments)
		{
			this.preCount++;
		}
		
		/**
		 * This function is invoked by the fax client SPI proxy after invoking
		 * the method in the fax client SPI itself.
		 * 
		 * @param 	method
		 * 			The method invoked
		 * @param 	arguments
		 * 			The method arguments
		 * @param 	output
		 * 			The method output
		 */
		public void postMethodInvocation(Method method,Object[] arguments,Object output)
		{
			this.postCount++;
		}
		
		/**
		 * This function is invoked by the fax client SPI proxy in of an error
		 * while invoking the method in the fax client SPI itself.
		 * 
		 * @param 	method
		 * 			The method invoked
		 * @param 	arguments
		 * 			The method arguments
		 * @param	throwable
		 * 			The throwable while invoking the method
		 */
		public void onMethodInvocationError(Method method,Object[] arguments,Throwable throwable)
		{
			this.onErrorCount++;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestCloseable implements Closeable
	{
		/**Flag for testing*/
		private boolean closed=false;
		
		/**
		 * Sets flag to true.
		 */
		public void close()
		{
			this.closed=true;
		}
		/**
		 * Getter method. 
		 * 
		 * @return	The value
		 */
		public boolean isClosed()
		{
			return this.closed;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestFaxModemAdapter extends AbstractFax4JFaxModemAdapter
	{
		/**
		 * This function initializes the fax modem adapter.
		 * 
		 * @param	faxClientSpi
		 * 			The fax client SPI
		 */
		@Override
		protected void initializeImpl(FaxClientSpi faxClientSpi)
		{
			//empty
		}
		
		/**
		 * This function will submit a new fax job.<br>
		 * The fax job ID may be populated by this method in the provided
		 * fax job object.
		 * 
		 * @param 	faxJob
		 * 			The fax job object containing the needed information
		 * @param	adapter
		 * 			The COMM port adapter
		 */
		@Override
		protected void submitFaxJobImpl(FaxJob faxJob,CommPortAdapter adapter)
		{
			faxJob.setID("123");
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestCommPortConnectionFactory extends AbstractCommPortConnectionFactory
	{
		/**
		 * Creates and returns the resource.
		 *  
		 * @return	The resource
		 */
		@Override
		protected CommPortAdapter createResourceImpl()
		{
			return new AbstractCommPortAdapter()
			{
				private boolean open=true;

				public boolean isOpen()
				{
					return this.open;
				}
				
				public OutputStream getOutputStream()
				{
					return new ByteArrayOutputStream();
				}
				
				public InputStream getInputStream()
				{
					return new ByteArrayInputStream(new byte[0]);
				}
				
				@Override
				protected void closeImpl()
				{
					this.open=false;
				}
			};
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestFaxClientActionEventListener implements FaxClientActionEventListener
	{
		/**Class member*/
		private FaxClientActionEventID faxEventID;
		
		/**
		 * Returns the last fax event ID. 
		 * 
		 * @return	The last fax event ID
		 */
		public FaxClientActionEventID getLastFaxEventID()
		{
			return this.faxEventID;
		}

		/**
		 * This function is invoked after a new fax job is created.
		 * 
		 * @param 	event
		 * 			The fax event
		 */
		public void faxJobCreated(FaxClientActionEvent event)
		{
			this.faxEventID=event.getID();
		}

		/**
		 * This function is invoked after a fax job is submitted.
		 * 
		 * @param 	event
		 * 			The fax event
		 */
		public void faxJobSubmitted(FaxClientActionEvent event)
		{
			this.faxEventID=event.getID();
		}

		/**
		 * This function is invoked after a fax job is suspended.
		 * 
		 * @param 	event
		 * 			The fax event
		 */
		public void faxJobSuspended(FaxClientActionEvent event)
		{
			this.faxEventID=event.getID();
		}

		/**
		 * This function is invoked after a fax job is resumed.
		 * 
		 * @param 	event
		 * 			The fax event
		 */
		public void faxJobResumed(FaxClientActionEvent event)
		{
			this.faxEventID=event.getID();
		}

		/**
		 * This function is invoked after a fax job is cancelled.
		 * 
		 * @param 	event
		 * 			The fax event
		 */
		public void faxJobCancelled(FaxClientActionEvent event)
		{
			this.faxEventID=event.getID();
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestFaxMonitorEventListener implements FaxMonitorEventListener
	{
		/**Class member*/
		private FaxJobStatus faxJobStatus;

		/**
		 * This function is invoked when the fax job status is changed.
		 * 
		 * @param 	event
		 * 			The fax event
		 */
		public void faxJobStatusChanged(FaxMonitorEvent event)
		{
			this.faxJobStatus=event.getFaxJobStatus();
		}
		
		/**
		 * Test method 
		 * 
		 * @return	The last status
		 */
		public FaxJobStatus getLastFaxJobStatus()
		{
			return this.faxJobStatus;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestConnectionFactory extends AbstractFax4JConnectionFactory<TestResource>
	{
		/**
		 * Emtpy method
		 */
		@Override
		protected void initializeImpl()
		{
			//empty
		}

		/**
		 * Creates and returns the resource.
		 *  
		 * @return	The resource
		 */
		@Override
		protected TestResource createResourceImpl()
		{
			return new TestResource();
		}

		/**
		 * Releases the resource from the connection.
		 *  
		 * @param	resource
		 * 			The resource
		 */
		@Override
		protected void releaseResourceImpl(TestResource resource)
		{
			resource.close();
		}

	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestResource
	{
		/**Class member*/
		private boolean open;
		
		/**
		 * This is the class constructor.
		 */
		public TestResource()
		{
			super();
			
			this.open=true;
		}

		/**
		 * Closes the connection.
		 */
		public void close()
		{
			this.open=false;
		}
		
		/**
		 * This function returns true if open.
		 * 
		 * @return	True if open
		 */
		public boolean isOpen()
		{
			return this.open;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestMailConnectionFactory extends MailConnectionFactoryImpl
	{
		/**
		 * This function returns a transport for the provided session.
		 * 
		 * @param 	session
		 * 			The mail session
		 * @return	The mail transport
		 */
		@Override
		protected Transport createTransport(Session session)
		{
			Transport transport=Mockito.mock(Transport.class);
			Mockito.when(Boolean.valueOf(transport.isConnected())).thenReturn(Boolean.TRUE).thenReturn(Boolean.FALSE);
			
			return transport;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestHTTPClient extends ApacheHTTPClient
	{
		/**
		 * This function creates and returns a new HTTP client.
		 * 
		 * @return	The new HTTP client
		 */
		@Override
		protected HttpClient createHttpClient()
		{
			HttpClient httpClient=Mockito.mock(HttpClient.class);
			try
			{
				Mockito.when(Integer.valueOf(httpClient.executeMethod((HttpMethod)null))).thenReturn(Integer.valueOf(200));
			}
			catch(Exception exception)
			{
				throw new RuntimeException(exception);
			}
			return httpClient;
		}

		/**
		 * This function creates and returns a new HTTP method.
		 * 
		 * @param	url
		 * 			The target URL
		 * @param 	httpMethod
		 * 			The HTTP method to use
		 * @return	The new HTTP method
		 */
		@Override
		protected HttpMethodBase createMethod(String url,HTTPMethod httpMethod)
		{
			PostMethod postMethod=Mockito.mock(PostMethod.class);
			return postMethod;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestHylaFAXClientConnectionFactory extends HylaFAXClientConnectionFactoryImpl
	{
		/**
		 * Creates and returns the resource.
		 *  
		 * @return	The resource
		 */
		@Override
		protected HylaFAXClient createResourceImpl()
		{
			HylaFAXClient client=Mockito.mock(HylaFAXClient.class);
			try
			{
				Mockito.doNothing().when(client).open("TEST_HOST",123);
				Job job=Mockito.mock(Job.class);
				Mockito.when(client.createJob()).thenReturn(job);
				Mockito.doReturn(Long.valueOf(123)).when(job).getId();
				Mockito.doReturn("abc").when(job).getDialstring();
			}
			catch(Exception exception)
			{
				throw new RuntimeException(exception);
			}
			
			return client;
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestRequestParser implements RequestParser<String>
	{
		/**
		 * Test function
		 * 
		 * @param 	configuration
		 * 			The configuration
		 */
		public void initialize(Map<String,String> configuration)
		{
			//empty
		}

		/**
		 * This function returns the file info from the input data.
		 * 
		 * @param	inputData
		 * 			The input data
		 * @return	The file info
		 */
		public FileInfo getFileInfoFromInputData(String inputData)
		{
			if(inputData.equals("NullFileInfoTest"))
			{
				return null;
			}
			return new FileInfo("abc.txt",new byte[10]);
		}

		/**
		 * This function update the fax job from the input data.<br>
		 * This fax job will not have any file data.
		 * 
		 * @param	inputData
		 * 			The input data
		 * @param	faxJob
		 * 			The fax job to update
		 */
		public void updateFaxJobFromInputData(String inputData,FaxJob faxJob)
		{
			if(!inputData.equals("NullFaxJobTest"))
			{
				faxJob.setTargetAddress("12345");
			}
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestHTTPRequestParser implements HTTPRequestParser
	{
		/**
		 * Test function
		 * 
		 * @param 	configuration
		 * 			The configuration
		 */
		public void initialize(Map<String,String> configuration)
		{
			//empty
		}

		/**
		 * This function returns the file info from the input data.
		 * 
		 * @param	inputData
		 * 			The input data
		 * @return	The file info
		 */
		public FileInfo getFileInfoFromInputData(HTTPRequest inputData)
		{
			if(inputData.getResource().equals("NullFileInfoTest"))
			{
				return null;
			}
			return new FileInfo("abc.txt",new byte[10]);
		}

		/**
		 * This function update the fax job from the input data.<br>
		 * This fax job will not have any file data.
		 * 
		 * @param	inputData
		 * 			The input data
		 * @param	faxJob
		 * 			The fax job to update
		 */
		public void updateFaxJobFromInputData(HTTPRequest inputData,FaxJob faxJob)
		{
			if(!inputData.getResource().equals("NullFaxJobTest"))
			{
				faxJob.setTargetAddress("12345");
			}
		}
	}

	/**
	 * Helper class 
	 * 
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestVendorPolicy extends EmptyVendorPolicy
	{
		/**
		 * Test method 
		 * 
		 * @param 	requestDataHolder
		 * 			Ignored
		 * @return	Always false
		 */
		@Override
		protected boolean invokePolicyForRequestImpl(Object requestDataHolder)
		{
			return false;
		}

		/**
		 * Test method 
		 * 
		 * @param 	requestDataHolder
		 * 			Ignored
		 * @return	Always false
		 */
		@Override
		protected boolean invokePolicyForResponseImpl(Object requestDataHolder,FaxJob faxJob)
		{
			return false;
		}
	}

	/**
	 * Helper class 
	 *
	 * @author 	Sagie Gur-Ari
	 */
	public static class TestVendorPolicy2 extends TestVendorPolicy implements FaxMonitorEventListener
	{
		/**
		 * Test method 
		 *
		 * @param 	event
		 * 			Ignored
		 */
		public void faxJobStatusChanged(FaxMonitorEvent event)
		{
			//empty
		}
	}

	/**
	 * Helper class
	 *
	 * @author 	Sagie Gur-Ari
	 */
	public static class PrivateObject
	{
		private int counter;
	}
}