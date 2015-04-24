package org.fax4j.spi.linux;

import org.fax4j.spi.process.ProcessFaxClientSpi;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation will proxy the requests to linux native fax commands.<br>
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
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.linux.submit.template.command</td>
 * 		<td>The linux submit fax job template command.</td>
 * 		<td>efax -d /dev/modem -t ${target.address} ${file}</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.linux.suspend.template.command</td>
 * 		<td>The suspend fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.linux.resume.template.command</td>
 * 		<td>The resume fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.linux.cancel.template.command</td>
 * 		<td>The cancel fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.linux.get.status.template.command</td>
 * 		<td>The get status fax job template command.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.linux.output.validator</td>
 * 		<td>The process output validator class name.</td>
 * 		<td>org.fax4j.spi.process.ExitCodeProcessOutputValidator</td>
 * 		<td>org.fax4j.spi.process.ExitCodeProcessOutputValidator</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.linux.output.handler</td>
 * 		<td>The process output handler class name.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>This SPI will only work on linux OS.
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>Required linux efax utility.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.04
 * @since	0.21e
 */
public class LinuxFaxClientSpi extends ProcessFaxClientSpi
{
	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.03
	 * @since	0.21e
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The linux property part default value*/
		LINUX_PROPERTY_PART_DEFAULT_VALUE("linux");

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
	}

	/**
	 * This is the default constructor.
	 */
	public LinuxFaxClientSpi()
	{
		super();
	}

	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//initialize
		super.initializeImpl();
		
		//set defaults
		this.useWindowsCommandPrefix=false;
	}

	/**
	 * Returns the property part.<br>
	 * Property parts enables to replace the input request key with the part defined
	 * to enable to reuse services with different configuration blocks.<br>
	 * Property parts will be replaced with the {0} values in the key,
	 * for example org.fax4j.{0}.somekey with part abc will be replaced to org.fax4j.abc.somekey
	 *  
	 * @return	The property part
	 */
	@Override
	public String getPropertyPart()
	{
		return FaxClientSpiConfigurationConstants.LINUX_PROPERTY_PART_DEFAULT_VALUE.toString();
	}
}