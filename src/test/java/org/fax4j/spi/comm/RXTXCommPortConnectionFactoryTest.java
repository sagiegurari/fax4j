package org.fax4j.spi.comm;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.TestFaxModemAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("gnu.io.CommPortIdentifier")
public class RXTXCommPortConnectionFactoryTest
{
	/**The connection factory to test*/
	private RXTXCommPortConnectionFactory connectionFactory;

	/**
	 * Sets up the test objects.
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.connectionFactory=new RXTXCommPortConnectionFactory()
		{
			/**
			 * This function returns the COMM port identifier object.
			 * 
			 * @param 	portName
			 * 			The port name
			 * @return	The port identifier object
			 * @throws 	NoSuchPortException
			 * 			In case no such port exists
			 */
			@Override
			protected CommPortIdentifier getPortIdentifier(String portName) throws NoSuchPortException
			{
				CommPortIdentifier commPortIdentifier=Mockito.mock(CommPortIdentifier.class);
				try
				{
					CommPort commPort=Mockito.mock(CommPort.class);
					Mockito.when(commPort.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
					Mockito.when(commPort.getOutputStream()).thenReturn(new ByteArrayOutputStream());
					Mockito.when(commPortIdentifier.open("fax4j",123)).thenReturn(commPort);
				}
				catch(Exception exception)
				{
					throw new RuntimeException(exception);
				}
				return commPortIdentifier;
			}
		};

		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.comm.port.name","abc");
		configuration.setProperty("org.fax4j.spi.comm.connection.factory.class.name",RXTXCommPortConnectionFactory.class.getName());
		configuration.setProperty("org.fax4j.spi.comm.fax.modem.class.name",TestFaxModemAdapter.class.getName());	
		configuration.setProperty("org.fax4j.spi.comm.connection.timeout","123");
		CommFaxClientSpi faxClientSpi=(CommFaxClientSpi)TestUtil.createFaxClientSpi(CommFaxClientSpi.class.getName(),configuration);
		this.connectionFactory.initialize(faxClientSpi);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createResourceImplTest() throws Exception
	{
		CommPortAdapter adapter=this.connectionFactory.createResourceImpl();
		Assert.assertNotNull(adapter);
		Assert.assertTrue(adapter.isOpen());
		Assert.assertNotNull(adapter.getInputStream());
		Assert.assertNotNull(adapter.getOutputStream());
	}
}