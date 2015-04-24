package org.fax4j.spi.adapter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.FaxClientSpiFactory;
import org.fax4j.spi.adapter.AdapterFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.spi.comm.CommFaxClientSpi;
import org.fax4j.spi.efax.EFaxMailFaxClientSpi;
import org.fax4j.spi.email.MailFaxClientSpi;
import org.fax4j.spi.extremefax.ExtremeFaxMailFaxClientSpi;
import org.fax4j.spi.faxage.FaxAgeMailFaxClientSpi;
import org.fax4j.spi.hoiio.HoiioFaxClientSpi;
import org.fax4j.spi.http.HTTPFaxClientSpi;
import org.fax4j.spi.hylafax.HylaFaxClientSpi;
import org.fax4j.spi.interfax.InterfaxMailFaxClientSpi;
import org.fax4j.spi.java4less.RFaxFaxClientSpi;
import org.fax4j.spi.linux.LinuxFaxClientSpi;
import org.fax4j.spi.mac.MacFaxClientSpi;
import org.fax4j.spi.metrofax.MetroFaxMailFaxClientSpi;
import org.fax4j.spi.phaxio.PhaxioFaxClientSpi;
import org.fax4j.spi.process.ProcessFaxClientSpi;
import org.fax4j.spi.send2fax.Send2FaxMailFaxClientSpi;
import org.fax4j.spi.vbs.VBSFaxClientSpi;
import org.fax4j.spi.windows.WindowsFaxClientSpi;
import org.fax4j.test.TestUtil;
import org.fax4j.test.TestUtil.EmptyFaxClientSpi;
import org.fax4j.test.TestUtil.TestFaxModemAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class AdapterFaxClientSpiTest
{
	/**The fax client SPI*/
	private AdapterFaxClientSpi faxClientSpi;
	/**The fax client SPI*/
	private EmptyFaxClientSpi emptyFaxClientSpi;

	/**
	 * Sets up the SPI instance.
	 */
	@Before
    public void setUp()
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","empty");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.condition.empty","");
		configuration.setProperty("org.fax4j.spi.type.map.empty",EmptyFaxClientSpi.class.getName());
		configuration.setProperty("org.fax4j.spi.empty.stable","true");
		configuration.setProperty("p1","v1");
		configuration.setProperty("org.fax4j.spi.adapter.configuration.override.testproperty","value123");
		
		this.faxClientSpi=(AdapterFaxClientSpi)TestUtil.createFaxClientSpi(AdapterFaxClientSpi.class.getName(),configuration);
		this.emptyFaxClientSpi=(EmptyFaxClientSpi)TestUtil.createFaxClientSpi(EmptyFaxClientSpi.class.getName(),null);
    }

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyExistsTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.PROPERTY_CONDITION.toString(),"p1");
		Assert.assertTrue(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyNotExistsTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.PROPERTY_CONDITION.toString(),"p0");
		Assert.assertFalse(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyOSValidTest() throws Exception
	{
		String osName=System.getProperty("os.name");
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.OS_CONDITION.toString(),osName);
		Assert.assertTrue(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyOSInvalidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.OS_CONDITION.toString(),"abc123");
		Assert.assertFalse(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyJavaClassValidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.JAVA_CLASS_CONDITION.toString(),String.class.getName());
		Assert.assertTrue(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyJavaClassInvalidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.JAVA_CLASS_CONDITION.toString(),"test.123.InvalidClass");
		Assert.assertFalse(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyNativeLibValidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.NATIVE_LIB_CONDITION.toString(),"jawt");
		Assert.assertTrue(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyNativeLibInvalidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.NATIVE_LIB_CONDITION.toString(),"InvalidLib");
		Assert.assertFalse(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyExecutableValidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.EXECUTABLE_CONDITION.toString(),"java");
		if(!output)
		{
			output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.EXECUTABLE_CONDITION.toString(),"java.exe");
		}
		Assert.assertTrue(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyExecutableInvalidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.EXECUTABLE_CONDITION.toString(),"InvalidExecutable");
		Assert.assertFalse(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyStablePropertyValidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.STABLE_CONDITION.toString(),"empty");
		if(!output)
		{
			output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.EXECUTABLE_CONDITION.toString(),"java.exe");
		}
		Assert.assertTrue(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void validateConditionPropertyStablePropertyInvalidTest() throws Exception
	{
		boolean output=this.faxClientSpi.validateCondition(FaxClientSpiConfigurationConstants.STABLE_CONDITION.toString(),"some_value");
		Assert.assertFalse(output);
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test(expected=FaxException.class)
	public void validateConditionPropertyInvalidConditionTest() throws Exception
	{
		this.faxClientSpi.validateCondition("invalid_condition","");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void createFaxClientSpiConfigurationTest() throws Exception
	{
		Properties configuration=this.faxClientSpi.createFaxClientSpiConfiguration();
		Assert.assertNotNull(configuration);
		Assert.assertEquals(configuration.getProperty("p1"),"v1");
		Assert.assertEquals(configuration.getProperty("testproperty"),"value123");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void ensureAllMappedSPIsAreValidatedAndHaveConditionsConfigurationTest() throws Exception
	{
		Map<String,String> properties=this.emptyFaxClientSpi.getAllConfiguration();
		Iterator<?> iterator=properties.entrySet().iterator();
		String typeString=this.emptyFaxClientSpi.getConfigurationValue("org.fax4j.spi.adapter.internal.spi.types");
		Assert.assertNotNull(typeString);
		Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(typeString.length()));
		while(iterator.hasNext())
		{
			Entry<?,?> entry=(Entry<?,?>)iterator.next();
			String key=(String)entry.getKey();
			
			if(key.startsWith("org.fax4j.spi.type.map."))
			{
				String type=key.substring("org.fax4j.spi.type.map.".length());
				
				if((!type.equals("test"))&&(!type.equals("adapter")))
				{
					String value=(String)entry.getValue();
					Assert.assertNotNull(value);
					Assert.assertNotSame("No value found for property: "+key,Integer.valueOf(0),Integer.valueOf(value.length()));
	
					//ensure type is on types string
					Assert.assertNotSame("Type not found on types string: "+type,Integer.valueOf(-1),Integer.valueOf(typeString.indexOf(type)));
					int index1=typeString.indexOf(type+";");
					int index2=typeString.indexOf(";"+type);
					Assert.assertTrue((index1!=-1)||(index2!=-1));
					
					//ensure condition exists
					String conditionString=this.emptyFaxClientSpi.getConfigurationValue("org.fax4j.spi.adapter.internal.spi.condition."+type);
					Assert.assertNotNull(conditionString);
					Assert.assertNotSame(Integer.valueOf(0),Integer.valueOf(conditionString.length()));
				}
			}
		}
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void windowsConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","windows");
		
		String condition=this.emptyFaxClientSpi.getConfigurationValue("org.fax4j.spi.adapter.internal.spi.condition.windows");
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(condition.indexOf("OS:windows")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(condition.indexOf("native-lib:winfax")));
		if(!TestUtil.isWindowsOS())
		{
			condition=condition.replace("OS:windows","OS:"+System.getProperty("os.name"));
			condition=condition.replace("native-lib:winfax","native-lib:jawt");
			configuration.setProperty("org.fax4j.spi.adapter.internal.spi.condition.windows",condition);
		}
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(WindowsFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void vbsConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","vbs");
		
		String condition=this.emptyFaxClientSpi.getConfigurationValue("org.fax4j.spi.adapter.internal.spi.condition.vbs");
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(condition.indexOf("OS:windows")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(condition.indexOf("executable:cscript.exe")));
		if(!TestUtil.isWindowsOS())
		{
			condition=condition.replace("OS:windows","OS:"+System.getProperty("os.name"));
			condition=condition.replace("executable:cscript.exe","executable:java");
			configuration.setProperty("org.fax4j.spi.adapter.internal.spi.condition.vbs",condition);
		}
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(VBSFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void mailConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","mail");

		configuration.setProperty("org.fax4j.spi.mail.address.template","a");
		configuration.setProperty("org.fax4j.spi.mail.subject.template","a");
		configuration.setProperty("org.fax4j.spi.mail.user.name","a");
		configuration.setProperty("org.fax4j.spi.mail.password","a");
		configuration.setProperty("mail.host","a");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(MailFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void httpConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","http");

		configuration.setProperty("org.fax4j.spi.http.host.name","a");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(HTTPFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void processConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","process");

		configuration.setProperty("org.fax4j.spi.process.submit.template.command","a");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(ProcessFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void linuxConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","linux");

		String condition=this.emptyFaxClientSpi.getConfigurationValue("org.fax4j.spi.adapter.internal.spi.condition.linux");
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(condition.indexOf("OS:linux")));
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(condition.indexOf("executable:efax")));
		if(System.getProperty("os.name").indexOf("linux")==-1)
		{
			condition=condition.replace("OS:linux","OS:"+System.getProperty("os.name"));
			if(TestUtil.isWindowsOS())
			{
				condition=condition.replace("executable:efax","executable:java.exe");
			}
			else
			{
				condition=condition.replace("executable:efax","executable:java");
			}
			configuration.setProperty("org.fax4j.spi.adapter.internal.spi.condition.linux",condition);
		}
		configuration.setProperty("org.fax4j.spi.linux.submit.template.command","a");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(LinuxFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void macConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","mac");

		String condition=this.emptyFaxClientSpi.getConfigurationValue("org.fax4j.spi.adapter.internal.spi.condition.mac");
		Assert.assertNotSame(Integer.valueOf(-1),Integer.valueOf(condition.indexOf("OS:mac")));
		if(System.getProperty("os.name").indexOf("mac")==-1)
		{
			condition=condition.replace("OS:mac","OS:"+System.getProperty("os.name"));
			configuration.setProperty("org.fax4j.spi.adapter.internal.spi.condition.mac",condition);
		}
		configuration.setProperty("org.fax4j.spi.mac.print.queue.name","a");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(MacFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void hylafaxConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","hylafax");

		configuration.setProperty("org.fax4j.spi.hylafax.host","a");
		configuration.setProperty("org.fax4j.spi.hylafax.user","a");
		configuration.setProperty("org.fax4j.spi.hylafax.mode","a");
		configuration.setProperty("org.fax4j.spi.hylafax.type","a");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(HylaFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void commConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","comm");

		configuration.setProperty("org.fax4j.spi.comm.port.name","a");
		configuration.setProperty("org.fax4j.spi.comm.fax.modem.class.name",TestFaxModemAdapter.class.getName());
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(CommFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @param	type
	 * 			The SPI class
	 * @param	typeName
	 * 			The SPI type mapping name
	 * @throws 	Exception
	 * 			Any exception
	 */
	private static void mailVendorConditionConfigurationTest(Class<?> type,String typeName) throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types",typeName);

		configuration.setProperty("org.fax4j.spi.mail.user.name","a");
		configuration.setProperty("org.fax4j.spi.mail.password","a");
		configuration.setProperty("mail.host","a");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(type,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void interfaxConditionConfigurationTest() throws Exception
	{
		AdapterFaxClientSpiTest.mailVendorConditionConfigurationTest(InterfaxMailFaxClientSpi.class,"interfax");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void send2faxConditionConfigurationTest() throws Exception
	{
		AdapterFaxClientSpiTest.mailVendorConditionConfigurationTest(Send2FaxMailFaxClientSpi.class,"send2fax");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void metrofaxConditionConfigurationTest() throws Exception
	{
		AdapterFaxClientSpiTest.mailVendorConditionConfigurationTest(MetroFaxMailFaxClientSpi.class,"metrofax");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void faxagefaxConditionConfigurationTest() throws Exception
	{
		AdapterFaxClientSpiTest.mailVendorConditionConfigurationTest(FaxAgeMailFaxClientSpi.class,"faxage");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void extremefaxfaxConditionConfigurationTest() throws Exception
	{
		AdapterFaxClientSpiTest.mailVendorConditionConfigurationTest(ExtremeFaxMailFaxClientSpi.class,"extremefax");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void efaxfaxConditionConfigurationTest() throws Exception
	{
		AdapterFaxClientSpiTest.mailVendorConditionConfigurationTest(EFaxMailFaxClientSpi.class,"efax");
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void phaxioConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","phaxio");

		configuration.setProperty("org.fax4j.spi.phaxio.api.key","test");
		configuration.setProperty("org.fax4j.spi.phaxio.api.secret","test");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(PhaxioFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void hoiioConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","hoiio");

		configuration.setProperty("org.fax4j.spi.hoiio.app.id","test");
		configuration.setProperty("org.fax4j.spi.hoiio.access.token","test");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(HoiioFaxClientSpi.class,internalFaxClientSpi.getClass());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void rfaxConditionConfigurationTest() throws Exception
	{
		Properties configuration=new Properties();
		configuration.setProperty("org.fax4j.proxy.enabled","false");
		configuration.setProperty("org.fax4j.spi.adapter.internal.spi.types","rfax");

		configuration.setProperty("org.fax4j.spi.rfax.port.name","a");
		configuration.setProperty("org.fax4j.spi.rfax.fax.class","2");
		
		AdapterFaxClientSpi spi=(AdapterFaxClientSpi)FaxClientSpiFactory.createFaxClientSpi("adapter",configuration);
		FaxClientSpi internalFaxClientSpi=spi.getInternalFaxClientSpi();
		Assert.assertNotNull(internalFaxClientSpi);
		Assert.assertEquals(RFaxFaxClientSpi.class,internalFaxClientSpi.getClass());
	}
}