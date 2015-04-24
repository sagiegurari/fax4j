package org.fax4j.bridge.process;

import org.fax4j.bridge.AbstractContextFaxBridge;

/**
 * This is the process cli2fax based implementation of the fax bridge.
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
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.vendor.policy.class.name</td>
 * 		<td>The vendor policy class name.</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.process.command.parser.class.name</td>
 * 		<td>The command line parser class name used to convert the command line arguments to fax job data.</td>
 * 		<td>org.fax4j.bridge.process.DefaultCommandLineArgumentsParser</td>
 * 		<td>org.fax4j.bridge.process.DefaultCommandLineArgumentsParser</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.40.6
 */
public class Process2FaxBridge extends AbstractContextFaxBridge<String[]>
{
	/**The command parser class name*/
	public static final String COMMAND_PARSER_CLASS_NAME_PROPERTY_KEY="org.fax4j.bridge.process.command.parser.class.name";

	/**
	 * This is the class constructor.
	 */
	public Process2FaxBridge()
	{
		super();
	}

	
	/**
	 * This function returns the key used to fetch the request parser
	 * class name from the fax4j.properties.
	 * 
	 * @return	The request parser class name configuration key
	 */
	@Override
	protected String getRequestParserConfigurationKey()
	{
		return Process2FaxBridge.COMMAND_PARSER_CLASS_NAME_PROPERTY_KEY;
	}
	
	/**
	 * This function returns the default request parser class name.
	 * 
	 * @return	The default request parser class name
	 */
	@Override
	protected String getDefaultParserClassName()
	{
		return DefaultCommandLineArgumentsParser.class.getName();
	}
}