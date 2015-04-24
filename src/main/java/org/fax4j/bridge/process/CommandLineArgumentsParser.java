package org.fax4j.bridge.process;

import org.fax4j.bridge.RequestParser;

/**
 * This interface defines the command line parser used to extract the fax job
 * and file info from the command line arguments.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.40.6
 */
public interface CommandLineArgumentsParser extends RequestParser<String[]>
{
	//all capabilities defined in super interface
}