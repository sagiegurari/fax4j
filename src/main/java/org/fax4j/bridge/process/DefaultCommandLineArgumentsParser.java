package org.fax4j.bridge.process;

import java.io.File;
import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.bridge.AbstractRequestParser;
import org.fax4j.bridge.FileInfo;

/**
 * This class provides default predefined implementation of the command line parser
 * used to extract the fax job and file info from the command line arguments.<br>
 * This default parser expects the command line arguments as follows:<br>
 * <ul>
 * 	<li>-file [full file name]
 * 	<li>-priority [priority value as defined in the enum]
 * 	<li>-target_address [target address]
 * 	<li>-target_name [target name]
 * 	<li>-sender_name [sender name]
 * 	<li>-sender_fax_number [sender fax number]
 * 	<li>-sender_email [sender email]
 * 	<li>-property:[property name] [property value]
 * </ul>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.40.6
 */
public class DefaultCommandLineArgumentsParser extends AbstractRequestParser<String[]> implements CommandLineArgumentsParser
{
	/**
	 * This is the class constructor.
	 */
	public DefaultCommandLineArgumentsParser()
	{
		super();
	}

	/**
	 * This function initializes the component.
	 * 
	 * @param	configuration
	 * 			The component configuration
	 */
	@Override
	protected void initializeImpl(Map<String,String> configuration)
	{
		//empty
	}

	/**
	 * This function returns the file info from the request data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @return	The file info
	 */
	@Override
	protected FileInfo getFileInfoFromInputDataImpl(String[] inputData)
	{
		//get amount
		int amount=inputData.length;

		FileInfo fileInfo=null;
		if((amount%2)==0)
		{
			String argument=null;
			String value=null;
			for(int index=0;index<inputData.length;index=index+2)
			{
				//get next argument and value
				argument=inputData[index];
				value=inputData[index+1];

				if(argument.equals("-file"))	//only looks for file argument, the update fax job will validate the full command line arguments for correctness
				{
					//get file
					File file=new File(value);

					//create file info
					fileInfo=new FileInfo(file);

					break;
				}
			}
		}
		else
		{
			throw new FaxException("Invalid input provided, expected even number of arguments.");
		}

		return fileInfo;
	}

	/**
	 * This function update the fax job from the request data.<br>
	 * This fax job will not have any file data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @param	faxJob
	 * 			The fax job to update
	 */
	@Override
	protected void updateFaxJobFromInputDataImpl(String[] inputData,FaxJob faxJob)
	{
		//get amount
		int amount=inputData.length;

		if((amount%2)==0)
		{
			String argument=null;
			String key=null;
			String value=null;
			String propertyPrefix="-property:";
			int propertyPrefixLength=propertyPrefix.length();
			for(int index=0;index<inputData.length;index=index+2)
			{
				//get next argument and value
				argument=inputData[index];
				value=inputData[index+1];

				if(argument.equals("-priority"))
				{
					FaxJobPriority priority=FaxJobPriority.valueOf(value);
					faxJob.setPriority(priority);
				}
				else if(argument.equals("-target_address"))
				{
					faxJob.setTargetAddress(value);
				}
				else if(argument.equals("-target_name"))
				{
					faxJob.setTargetName(value);
				}
				else if(argument.equals("-sender_name"))
				{
					faxJob.setSenderName(value);
				}
				else if(argument.equals("-sender_fax_number"))
				{
					faxJob.setSenderFaxNumber(value);
				}
				else if(argument.equals("-sender_email"))
				{
					faxJob.setSenderEmail(value);
				}
				else if(argument.startsWith(propertyPrefix))
				{
					if(argument.length()>propertyPrefixLength)
					{
						//get key
						key=argument.substring(propertyPrefixLength);
						
						//set property
						faxJob.setProperty(key,value);
					}
				}
				else if(!argument.equals("-file"))
				{
					throw new FaxException("Invalid input provided, unsupported argument: "+argument);
				}
			}
		}
		else
		{
			throw new FaxException("Invalid input provided, expected even number of arguments.");
		}
	}
}