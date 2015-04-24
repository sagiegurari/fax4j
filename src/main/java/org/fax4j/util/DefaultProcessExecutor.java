package org.fax4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.fax4j.FaxException;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.util.IOHelper.OutputReadThread;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * The process executer enables to execute native processes.<br>
 * This class provides the default implementation of the executer.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.40.1
 */
public class DefaultProcessExecutor extends AbstractProcessExecutor
{
	/**
	 * This is the class constructor.
	 */
	public DefaultProcessExecutor()
	{
		super();
	}

	/**
	 * This function executes the given command and returns the process output.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder used when invoking the process
	 * @param	command
	 * 			The command to execute
	 * @return	The process output
	 * @throws	IOException
	 * 			Any IO exception
	 * @throws	InterruptedException
	 * 			If thread interrupted during waitFor for the process
	 */
	@Override
	protected ProcessOutput executeProcessImpl(ConfigurationHolder configurationHolder,String command) throws IOException,InterruptedException
	{
		//parse command
		List<String> commandList=this.parseCommand(configurationHolder,command);
		
		//invoke process
		ProcessBuilder processBuilder=new ProcessBuilder(commandList);
		Process process=processBuilder.start();
		
		//read output
		InputStream inputStream=process.getInputStream();
		OutputReadThread outputThread=new OutputReadThread(inputStream);
		outputThread.start();
		inputStream=process.getErrorStream();
		OutputReadThread errorThread=new OutputReadThread(inputStream);
		errorThread.start();
		
		//wait for process to end
		int exitCode=process.waitFor();
		
		//get output
		String outputText=outputThread.getText();
		String errorText=errorThread.getText();
		
		//create output
		ProcessOutput processOutput=new ProcessOutput(outputText,errorText,exitCode);

		return processOutput;
	}
	
	/**
	 * This function parsers the command and converts to a command array.
	 * 
	 * @param	configurationHolder
	 * 			The configuration holder used when invoking the process
	 * @param	command
	 * 			The command to parse
	 * @return	The parsed command as an array
	 */
	protected List<String> parseCommand(ConfigurationHolder configurationHolder,String command)
	{
		//init list
		List<String> commandList=new LinkedList<String>();

		StringBuilder buffer=new StringBuilder(command);
		String part=null;
		int quoteIndex=-1;
		int spaceIndex=-1;
		int length=-1;
		do
		{
			quoteIndex=buffer.indexOf("\"");
			spaceIndex=buffer.indexOf(" ");
			if(quoteIndex==-1)
			{
				if(spaceIndex==-1)
				{
					buffer=this.addAllBuffer(commandList,buffer);
				}
				else
				{
					buffer=this.addPart(commandList,buffer,spaceIndex,true);
				}
			}
			else if(spaceIndex==-1)
			{
				if(quoteIndex==-1)
				{
					buffer=this.addAllBuffer(commandList,buffer);
				}
				else if(quoteIndex==0)
				{
					quoteIndex=buffer.indexOf("\"",1);
					if(quoteIndex==-1)
					{
						throw new FaxException("Unable to parse command: "+command);
					}

					buffer=this.addPart(commandList,buffer,quoteIndex,false);
				}
				else
				{
					throw new FaxException("Unable to parse command: "+command);
				}
			}
			else if(quoteIndex<spaceIndex)
			{
				if(quoteIndex==0)
				{
					quoteIndex=buffer.indexOf("\"",1);
					if(quoteIndex==-1)
					{
						throw new FaxException("Unable to parse command: "+command);
					}

					buffer=this.addPart(commandList,buffer,quoteIndex,false);
				}
				else
				{
					buffer=this.addPart(commandList,buffer,spaceIndex,true);
				}
			}
			else //spaceIndex<quoteIndex
			{
				buffer=this.addPart(commandList,buffer,spaceIndex,true);
			}
			
			length=buffer.length();
			if(length>0)
			{
				part=buffer.toString();
				part=part.trim();
				buffer.delete(0,length);
				buffer.append(part);
				length=buffer.length();
			}
		}while(length>0);

	 	return commandList;
	}
	
	/**
	 * Updates the command list and the buffer. 
	 * 
	 * @param 	commandList
	 * 			The command list to update
	 * @param 	buffer
	 * 			The input buffer
	 * @return	The updated buffer
	 */
	protected StringBuilder addAllBuffer(List<String> commandList,StringBuilder buffer)
	{
		commandList.add(buffer.toString());
		buffer.delete(0,buffer.length());

		return buffer;
	}
	
	/**
	 * Updates the command list and the buffer. 
	 * 
	 * @param 	commandList
	 * 			The command list to update
	 * @param 	buffer
	 * 			The input buffer
	 * @param 	endIndex 
	 * 			The end index in the buffer
	 * @param 	typeSpace 
	 * 			True space, false for quote
	 * @return	The updated buffer
	 */
	protected StringBuilder addPart(List<String> commandList,StringBuilder buffer,int endIndex,boolean typeSpace)
	{
		int delta=1;
		if(typeSpace)
		{
			delta=0;
		}
		
		String part=buffer.substring(delta,endIndex);
		commandList.add(part);
		buffer.delete(0,endIndex+delta);

		return buffer;
	}
}