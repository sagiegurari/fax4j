package org.fax4j.spi.adapter;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.common.Logger;
import org.fax4j.spi.FaxClientSpiFactory;
import org.fax4j.util.ReflectionHelper;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will proxy the requests to another internal SPI.<br>
 * The internal SPI created will be based on configuration in the fax4j properties.<br>
 * The configuration contains conditions such as OS, availability of java classes,
 * availability of native libraries, availability of executables or validation that
 * a certain fax4j property is defined and contains a value.<br>
 * Once all conditions are met the internal SPI is created, otherwise the next SPI configuration
 * is used.<br>
 * If no SPI conditions are met, an exception will be thrown.<br>
 * <br>
 * The configuration of the fax4j framework is made up of 3 layers.<br>
 * The configuration is based on simple properties.<br>
 * Each layer overrides the lower layers by adding/changing the property values.<br>
 * The first layer is the internal fax4j.properties file located in the fax4j jar.<br>
 * This layer contains the preconfigured values for the fax4j framework and can be changed
 * by updating these properties in the higher layers.<br>
 * The second layer is the external fax4j.properties file that is located on the classpath.<br>
 * This file is optional and provides the ability to override the internal configuration for the
 * entire fax4j framework.<br>
 * The top most layer is the optional java.util.Properties object provided by the external classes
 * when creating a new fax client.<br>
 * These properties enable to override the configuration of the lower 2 layers.<br>
 * <br>
 * <b>SPI Status (Draft, Beta, Stable): </b>Stable<br>
 * <br>
 * Below table describes the configuration values relevant for this class.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.adapter.internal.spi.types</td>
 * 		<td>A ';' seperated list of SPI types.<br>
 * 			This SPI will go over the list one by one based on the configured order and will validate the type
 * 			conditions.<br>
 * 			Once an SPI is found for which all conditions are met, the adapter SPI will create an instance
 * 			of it and use it to proxy all fax operations to.
 * 		</td>
 * 		<td>mail;http;process;phaxio;hoiio;windows;vbs;linux;mac;hylafax;comm;interfax;nextivafax;send2fax;metrofax;faxage;extremefax;efax;rfax</td>
 * 		<td>none</td>
 * 		<td>true</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.adapter.internal.spi.condition.xxx</td>
 * 		<td>For each type defined in the org.fax4j.spi.adapter.internal.spi.types property, you can define custom
 * 			conditions to validate.<br>
 * 			These conditions include:<br>
 * 			property - checks a fax4j property is defined and contains a value<br>
 * 			OS - checks OS name contains the value (case insensitive)<br>
 * 			java-class - checks that java class can be loaded<br>
 * 			native-lib - checks that native lib can be loaded<br>
 * 			executable - checks executable is on system path<br>
 * 			stable - checks for a fax4j property org.fax4j.spi.xxx.stable is defined and equals true, where xxx is the SPI key (for example: org.fax4j.spi.adapter.stable=true)<br>
 * 			Conditions are separated by a ';' character and the condition type and value are seperated by the ':' 
 * 			character, for example:<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.vbs=OS:windows;executable:cscript.exe<br>
 * 			The following conditions are always checked (even if not defined):<br>
 * 			<ul>
 * 				<li>property:org.fax4j.spi.type.map.xxx where xxx is the SPI key (for example: org.fax4j.spi.type.map.adapter)<br>
 * 				<li>java-class:xxx where xxx is the SPI class name based on the fax4j mapping (for example: org.fax4j.spi.type.map.adapter=org.fax4j.spi.adapter.AdapterFaxClientSpi)
 * 				<li>stable
 * 			</ul>
 * 		</td>
 * 		<td>
 * 			org.fax4j.spi.adapter.internal.spi.condition.windows=OS:windows;native-lib:winfax<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.vbs=OS:windows;executable:cscript.exe<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.mail=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.address.template;property:org.fax4j.spi.mail.subject.template;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.http=property:org.fax4j.spi.http.client.class.name;property:org.fax4j.spi.http.host.name;property:org.fax4j.spi.http.faxjob2request.converter.class.name;property:org.fax4j.spi.http.response.handler.class.name<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.process=property:org.fax4j.spi.process.submit.template.command<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.linux=OS:linux;executable:efax;property:org.fax4j.spi.linux.submit.template.command<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.mac=OS:mac;property:org.fax4j.spi.mac.print.queue.name<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.hylafax=property:org.fax4j.spi.hylafax.host;property:org.fax4j.spi.hylafax.user;property:org.fax4j.spi.hylafax.mode;property:org.fax4j.spi.hylafax.type;property:org.fax4j.spi.hylafax.connection.factory.class.name;java-class:gnu.hylafax.HylaFAXClient<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.comm=property:org.fax4j.spi.comm.port.name;property:org.fax4j.spi.comm.connection.factory.class.name;property:org.fax4j.spi.comm.fax.modem.class.name<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.interfax=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.nextivafax=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.send2fax=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.metrofax=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport
 * 			org.fax4j.spi.adapter.internal.spi.condition.faxage=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport
 * 			org.fax4j.spi.adapter.internal.spi.condition.extremefax=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport
 * 			org.fax4j.spi.adapter.internal.spi.condition.efax=property:org.fax4j.spi.mail.connection.factory.class.name;property:org.fax4j.spi.mail.user.name;property:org.fax4j.spi.mail.password;property:mail.host;java-class:javax.mail.Transport<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.phaxio=property:org.fax4j.spi.phaxio.api.key;property:org.fax4j.spi.phaxio.api.secret;java-class:org.json.JSONObject<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.hoiio=property:org.fax4j.spi.hoiio.app.id;property:org.fax4j.spi.hoiio.access.token;java-class:org.json.JSONObject<br>
 * 			org.fax4j.spi.adapter.internal.spi.condition.rfax=property:org.fax4j.spi.rfax.port.name;property:org.fax4j.spi.rfax.fax.class;java-class:com.java4less.rfax.FaxModem
 * 		</td>
 * 		<td>none</td>
 * 		<td>true</td>
 *	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.adapter.configuration.override.xxx</td>
 * 		<td>The adapter SPI enables to override the fax4j configuration for the internal SPI,<br>
 * 			by defining org.fax4j.spi.adapter.configuration.override.xxx properties where xxx is the original property name for example:<br>
 * 			org.fax4j.spi.adapter.configuration.override.<b>org.fax4j.proxy.enabled</b>=false overrides the org.fax4j.proxy.enabled property for
 * 			the interal SPI.
 * 		</td>
 * 		<td>
 * 			org.fax4j.spi.adapter.configuration.override.org.fax4j.proxy.enabled=false
 * 		</td>
 * 		<td>none</td>
 * 		<td>false</td>
 *	</tr>
 * </table>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.16
 * @since	0.1
 */
public class AdapterFaxClientSpi extends AbstractAdapterFaxClientSpi
{
	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The SPI types property key (SPI type list separated by ;)*/
		SPI_TYPES_PROPERTY_KEY("org.fax4j.spi.adapter.internal.spi.types"),
		/**The SPI condition property key prefix*/
		SPI_CONDITION_PROPERTY_KEY_PREFIX("org.fax4j.spi.adapter.internal.spi.condition."),
		/**The SPI stable property key prefix*/
		SPI_STABLE_PROPERTY_KEY_PREFIX("org.fax4j.spi."),
		/**The SPI stable property key suffix*/
		SPI_STABLE_PROPERTY_KEY_SUFFIX(".stable"),
		/**The SPI configuration override property key prefix*/
		SPI_CONFIGURATION_OVERRIDE_PROPERTY_KEY_PREFIX("org.fax4j.spi.adapter.configuration.override."),
		/**The property value list separator ';'*/
		PROPERTY_VALUE_LIST_SEPARATOR(";"),
		/**The property value separator ':'*/
		PROPERTY_BLOCK_VALUE_SEPARATOR(":"),
		/**The property condition - checks a fax4j property is defined and contains a value*/
		PROPERTY_CONDITION("property"),
		/**The OS condition - checks OS name contains the value (case insensitive)*/
		OS_CONDITION("OS"),
		/**The java class condition - checks that java class can be loaded*/
		JAVA_CLASS_CONDITION("java-class"),
		/**The native lib condition - checks that native lib can be loaded*/
		NATIVE_LIB_CONDITION("native-lib"),
		/**The executable condition - checks executable is on system path*/
		EXECUTABLE_CONDITION("executable"),
		/**The stable condition - checks for a fax4j property org.fax4j.spi.xxx.stable is defined and equals true, where xxx is the SPI key (for example: org.fax4j.spi.adapter.stable=true)*/
		STABLE_CONDITION("stable");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxClientSpiConfigurationConstants(String value)
		{
			this.value=value;
		}
		
		/**
		 * This function returns the string value.
		 * 
		 * @return	The string value
		 */
		@Override
		public final String toString()
		{
			return this.value;
		}

		/**
		 * This function returns the enum for the provided value.
		 * 
		 * @param	value
		 * 			The value to search for
		 * @return	The enum
		 */
		public static FaxClientSpiConfigurationConstants getEnum(String value)
		{
			if(value==null)
			{
				throw new FaxException("Null value provided.");
			}
			
			FaxClientSpiConfigurationConstants[] values=FaxClientSpiConfigurationConstants.values();
			int amount=values.length;
			FaxClientSpiConfigurationConstants constant=null;
			for(int index=0;index<amount;index++)
			{
				//get next constant
				constant=values[index];

				if(value.equalsIgnoreCase(constant.toString()))
				{
					return constant;
				}
			}

			throw new FaxException("Illegal value provided: "+value);
		}
	}
	 
	/**
	 * This is the default constructor.
	 */
	public AdapterFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeChildFaxClientSpi()
	{
		//get logger
		Logger logger=this.getLogger();
		
		//get types
		String types=this.getConfigurationValue(FaxClientSpiConfigurationConstants.SPI_TYPES_PROPERTY_KEY);
		logger.logDebug(new Object[]{FaxClientSpiConfigurationConstants.SPI_TYPES_PROPERTY_KEY," value is ",types},null);
		
		if(types!=null)
		{
			//split type values
			String[] typeValues=types.split(FaxClientSpiConfigurationConstants.PROPERTY_VALUE_LIST_SEPARATOR.toString());
			
			//get amount
			int amount=typeValues.length;
			
			String typeValue=null;
			String conditionString=null;
			String[] conditionBlocks=null;
			String conditionBlock=null;
			String[] conditionBlockValues=null;
			String conditionKey=null;
			String conditionValue=null;
			int subAmount=-1;
			boolean conditionValid=true;
			StringBuilder conditionStringBuffer=new StringBuilder();
			for(int index=0;index<amount;index++)
			{
				//get next element
				typeValue=typeValues[index];
				logger.logDebug(new Object[]{"Checking conditions for type: ",typeValue},null);
				
				//get condition string
				conditionString=this.getConfigurationValue(FaxClientSpiConfigurationConstants.SPI_CONDITION_PROPERTY_KEY_PREFIX+typeValue);
				logger.logDebug(new Object[]{"Configured condition for type: ",typeValue," is set to: ",conditionString},null);
				
				//append mandatory conditions
				conditionStringBuffer.delete(0,conditionStringBuffer.length());
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.PROPERTY_CONDITION);
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.PROPERTY_BLOCK_VALUE_SEPARATOR);
				conditionStringBuffer.append(FaxClientSpiFactory.SPI_CLASS_NAME_TYPE_MAP_PROPERTY_KEY_PREFIX);
				conditionStringBuffer.append(typeValue);
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.PROPERTY_VALUE_LIST_SEPARATOR);
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.JAVA_CLASS_CONDITION);
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.PROPERTY_BLOCK_VALUE_SEPARATOR);
				conditionStringBuffer.append(this.getConfigurationValue(FaxClientSpiFactory.SPI_CLASS_NAME_TYPE_MAP_PROPERTY_KEY_PREFIX+typeValue));
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.PROPERTY_VALUE_LIST_SEPARATOR);
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.STABLE_CONDITION);
				conditionStringBuffer.append(FaxClientSpiConfigurationConstants.PROPERTY_BLOCK_VALUE_SEPARATOR);
				conditionStringBuffer.append(typeValue);
				if(conditionString!=null)
				{
					conditionStringBuffer.append(FaxClientSpiConfigurationConstants.PROPERTY_VALUE_LIST_SEPARATOR);
					conditionStringBuffer.append(conditionString);
				}
				conditionString=conditionStringBuffer.toString();
				logger.logDebug(new Object[]{"Updated condition for type: ",typeValue," is set to: ",conditionString},null);
								
				//split condition to blocks
				conditionBlocks=conditionString.split(FaxClientSpiConfigurationConstants.PROPERTY_VALUE_LIST_SEPARATOR.toString());
				
				//get amount
				subAmount=conditionBlocks.length;
				
				conditionValid=true;
				for(int blockIndex=0;blockIndex<subAmount;blockIndex++)
				{
					//get next element
					conditionBlock=conditionBlocks[blockIndex];
					
					//split condition block
					conditionBlockValues=conditionBlock.split(FaxClientSpiConfigurationConstants.PROPERTY_BLOCK_VALUE_SEPARATOR.toString());
					
					//check condition block contains only 2 parts (key and value)
					if(conditionBlockValues.length!=2)
					{
						throw new FaxException("Condition block must contain 2 parts separated by "+FaxClientSpiConfigurationConstants.PROPERTY_BLOCK_VALUE_SEPARATOR);
					}
					
					//get condition key and value
					conditionKey=conditionBlockValues[0];
					conditionValue=conditionBlockValues[1];
					
					//validate condition
					logger.logDebug(new Object[]{"Validating condition for type: ",typeValue," key: ",conditionKey," value: ",conditionValue},null);
					if(!this.validateCondition(conditionKey,conditionValue))
					{
						conditionValid=false;
						break;
					}
				}
				
				if(conditionValid)
				{
					logger.logDebug(new Object[]{"Conditions validated for type: ",typeValue},null);
					
					//create SPI
					this.createFaxClientSpi(typeValue);
					
					break;
				}
			}
		}
		
		if(this.faxClientSpi==null)
		{
			throw new FaxException("No internal SPI available.");
		}
	}
	
	/**
	 * Validates the SPI condition.
	 *  
	 * @param 	key
	 * 			The condition key <br>
	 * 			property - checks a fax4j property is defined and contains a value<br>
	 * 			OS - checks OS name contains the value (case insensitive)<br>
	 * 			java-class - checks that java class can be loaded<br>
	 * 			native-lib - checks that native lib can be loaded<br>
	 * 			executable - checks executable is on system path<br>
	 * 			stable - checks for a fax4j property org.fax4j.spi.xxx.stable is defined and equals true, where xxx is the SPI key (for example: org.fax4j.spi.adapter.stable=true)
	 * @param 	value
	 * 			The condition value
	 * @return	True if the condition is met
	 */
	protected boolean validateCondition(String key,String value)
	{
		boolean valid=false;
		FaxClientSpiConfigurationConstants condition=FaxClientSpiConfigurationConstants.getEnum(key);
		String propertyValue=null;
		switch(condition)
		{
			case PROPERTY_CONDITION:
				//get value
				propertyValue=this.getConfigurationValue(value);
				
				if(propertyValue!=null)
				{
					valid=true;
				}
				break;
			case OS_CONDITION:
				//get OS name
				String osName=System.getProperty("os.name");
				
				//change values to lower case
				osName=osName.toLowerCase();
				String updatedValue=value.toLowerCase();
				
				if(osName.contains(updatedValue))
				{
					valid=true;
				}
				break;
			case JAVA_CLASS_CONDITION:
				try
				{
					//load class
					ReflectionHelper.getType(value);
					
					valid=true;
				}
				catch(Throwable throwable)
				{
					//ignore
				}
				break;
			case NATIVE_LIB_CONDITION:
				try
				{
					//load library
					System.loadLibrary(value);
					
					valid=true;
				}
				catch(Throwable throwable)
				{
					//ignore
				}
				break;
			case EXECUTABLE_CONDITION:
				//get system path
				String systemPath=System.getProperty("java.library.path");
				String[] systemPathElements=systemPath.split(System.getProperty("path.separator"));
				
				//get amount
				int amount=systemPathElements.length;
				
				String directoryPath=null;
				File file=null;
				for(int index=0;index<amount;index++)
				{
					//get next element
					directoryPath=systemPathElements[index];

					//get file
					file=new File(directoryPath,value);
					
					if((file.exists())&&(file.isFile()))
					{
						valid=true;
					}
				}
				break;
			case STABLE_CONDITION:
				//get property key
				String propertyKey=FaxClientSpiConfigurationConstants.SPI_STABLE_PROPERTY_KEY_PREFIX+value+FaxClientSpiConfigurationConstants.SPI_STABLE_PROPERTY_KEY_SUFFIX;
				
				//get value
				propertyValue=this.getConfigurationValue(propertyKey);
				
				valid=Boolean.parseBoolean(propertyValue);
				break;
			default:
				throw new FaxException("Invalid condition key provided: "+key);
		}
		
		return valid;
	}
	
	/**
	 *This function creates a new fax client SPI based on the provided configuration.
	 *  
	 * @param	type
	 * 			The fax client type
	 */
	protected void createFaxClientSpi(String type)
	{
		//setup fax client SPI configuration
		Properties configuration=this.createFaxClientSpiConfiguration();
		
		//create fax client SPI
		this.faxClientSpi=FaxClientSpiFactory.createChildFaxClientSpi(type,configuration);
	}
	
	/**
	 * This function creates and returns the internal fax client SPI
	 * configuration.
	 * 
	 * @return	The internal fax client SPI configuration
	 */
	protected Properties createFaxClientSpiConfiguration()
	{
		//get configuration
		Map<String,String> configuration=this.getConfiguration();
		
		//create new configuration
		Properties internalSPIConfiguration=new Properties();
		Properties overrideConfiguration=new Properties();
		
		//go over current configuration
		Iterator<Entry<String,String>> iterator=configuration.entrySet().iterator();
		Entry<String,String> entry=null;
		String key=null;
		String value=null;
		String overrideKey=null;
		String overridePrefix=FaxClientSpiConfigurationConstants.SPI_CONFIGURATION_OVERRIDE_PROPERTY_KEY_PREFIX.toString();
		int prefixLength=overridePrefix.length();
		while(iterator.hasNext())
		{
			//get next entry
			entry=iterator.next();
			
			//get key/value
			key=entry.getKey();
			value=entry.getValue();
			
			//override internal configuration
			if((key.startsWith(overridePrefix)))
			{
				if(key.length()!=prefixLength)
				{
					overrideKey=key.substring(prefixLength);
					
					//put in override configuration
					overrideConfiguration.setProperty(overrideKey,value);
				}
			}
			
			//put in new configuration
			internalSPIConfiguration.setProperty(key,value);
		}
		
		//override configuration
		internalSPIConfiguration.putAll(overrideConfiguration);
		
		return internalSPIConfiguration;
	}
}